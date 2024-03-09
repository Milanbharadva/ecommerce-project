package com.webmintinfotech.ecom.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.api.SingleResponse;
import com.webmintinfotech.ecom.base.BaseAdaptor;
import com.webmintinfotech.ecom.base.BaseFragment;
import com.webmintinfotech.ecom.databinding.FragFavoriteBinding;
import com.webmintinfotech.ecom.databinding.RowViewallBinding;
import com.webmintinfotech.ecom.model.GetWishListResponse;
import com.webmintinfotech.ecom.model.WishListDataItem;
import com.webmintinfotech.ecom.ui.activity.ActProductDetails;
import com.webmintinfotech.ecom.ui.authentication.ActLogin;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.SharePreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteFragment extends BaseFragment<FragFavoriteBinding> {
    private FragFavoriteBinding fragFavBinding;
    private ArrayList<WishListDataItem> wishListDataList = new ArrayList<>();
    private String currency = "";
    private String currencyPosition = "";
    private String[] colorArray = {
            "#FDF7FF", "#FDF3F0", "#EDF7FD", "#FFFAEA", "#F1FFF6", "#FFF5EC"
    };
    private BaseAdaptor<WishListDataItem, RowViewallBinding> wishListDataAdapter;
    private LinearLayoutManager manager;

    private int currentPage = 1;
    private int total_pages = 0;
    private int visibleItemCount = 0;
    private int totalItemCount = 0;
    private int pastVisibleItems = 0;

    @Override
    public void initView(View view) {
        fragFavBinding = FragFavoriteBinding.bind(view);
        manager = new GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false);
        currency = SharePreference.getStringPref(requireActivity(), SharePreference.Currency);
        currencyPosition = SharePreference.getStringPref(requireActivity(), SharePreference.CurrencyPosition);
        fragFavBinding.rvwhishliast.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = manager.getChildCount();
                    totalItemCount = manager.getItemCount();
                    pastVisibleItems = manager.findFirstVisibleItemPosition();
                    if (currentPage < total_pages) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            currentPage += 1;
                            if (Common.isCheckNetwork(requireActivity())) {
                                callApiWishList();
                            } else {
                                Common.alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.no_internet));
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public FragFavoriteBinding getBinding() {
        fragFavBinding = FragFavoriteBinding.inflate(LayoutInflater.from(getContext()));
        return fragFavBinding;
    }

    private void callApiWishList() {
        Common.showLoadingProgress(requireActivity());
        HashMap<String, String> hasmap = new HashMap<>();
        hasmap.put("user_id", SharePreference.getStringPref(requireActivity(), SharePreference.userId));

        Call<GetWishListResponse> call = ApiClient.getClient().getWishList(String.valueOf(currentPage), hasmap);
        call.enqueue(new Callback<GetWishListResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<GetWishListResponse> call, Response<GetWishListResponse> response) {
                if (response.code() == 200) {
                    GetWishListResponse restResponce = response.body();
                    if (restResponce.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        if (restResponce.getAllData() != null && restResponce.getAllData().getData() != null && restResponce.getAllData().getData().size() > 0) {
                            fragFavBinding.rvwhishliast.setVisibility(View.VISIBLE);
                            fragFavBinding.tvNoDataFound.setVisibility(View.GONE);
                            currentPage = restResponce.getAllData().getCurrentPage();
                            total_pages = restResponce.getAllData().getLastPage();
                            wishListDataList.addAll(restResponce.getAllData().getData());
                        } else {
                            fragFavBinding.rvwhishliast.setVisibility(View.GONE);
                            fragFavBinding.tvNoDataFound.setVisibility(View.VISIBLE);
                        }
                        wishListDataAdapter.notifyDataSetChanged();
                    } else if (restResponce.getStatus() == 0) {
                        Common.dismissLoadingProgress();

                        Common.alertErrorOrValidationDialog(requireActivity(), restResponce.getMessage());
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(Call<GetWishListResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.error_msg));
            }
        });
    }

    private void loadFeaturedProducts(ArrayList<WishListDataItem> wishListDataList) {
        wishListDataAdapter = new BaseAdaptor<WishListDataItem, RowViewallBinding>(requireActivity(), wishListDataList) {
            @SuppressLint({"NewApi", "ResourceType", "SetTextI18n"})
            @Override
            public void onBindData(RecyclerView.ViewHolder holder, WishListDataItem val, int position) {
                RowViewallBinding binding = ((ViewHolder) holder).getBinding();

                if (wishListDataList.get(position).getIsWishlist() == 0) {
                    binding.ivwishlist.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_dislike, null));
                } else {
                    binding.ivwishlist.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_like, null));
                }

                binding.ivwishlist.setOnClickListener(view -> {
                    if (SharePreference.getBooleanPref(requireActivity(), SharePreference.isLogin)) {
                        if (wishListDataList.get(position).getIsWishlist() == 0) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("product_id", String.valueOf(wishListDataList.get(position).getId()));
                            map.put("user_id", SharePreference.getStringPref(requireActivity(), SharePreference.userId));

                            if (Common.isCheckNetwork(requireActivity())) {
                                // callApiFavourite(map, position)
                            } else {
                                Common.alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.no_internet));
                            }
                        }
                        if (wishListDataList.get(position).getIsWishlist() == 1) {
                            if (Common.isCheckNetwork(requireActivity())) {
                                HashMap<String, String> map = new HashMap<>();
                                map.put("product_id", String.valueOf(wishListDataList.get(position).getId()));
                                map.put("user_id", SharePreference.getStringPref(requireActivity(), SharePreference.userId));
                                callApiRemoveFavourite(map, position);

                            } else {
                                Common.alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.no_internet));
                            }
                        }
                    } else {
                        openActivity(ActLogin.class);
                        requireActivity().finish();
                    }
                });

                if (wishListDataList.get(position).getRattings().size() == 0) {
                    binding.tvRatePro.setText("0.0");
                } else {
                    binding.tvRatePro.setText(wishListDataList.get(position).getRattings().get(0).getAvgRatting());
                }

                binding.tvProductName.setText(wishListDataList.get(position).getProductName());

                binding.tvProductPrice.setText(currency + String.format(Locale.US, "%,.2f", Double.parseDouble(wishListDataList.get(position).getProductPrice())));
                if ("0".equals(wishListDataList.get(position).getProductPrice()) || wishListDataList.get(position).getDiscountedPrice() == null) {
                    binding.tvProductDisprice.setVisibility(View.GONE);
                } else {
                    binding.tvProductDisprice.setText(currency + String.format(Locale.US, "%,.2f", Double.parseDouble(wishListDataList.get(position).getDiscountedPrice())));
                }
                Glide.with(requireActivity()).load(wishListDataList.get(position).getProductimage().getImageUrl()).into(binding.ivProduct);
                binding.ivProduct.setBackgroundColor(Color.parseColor(colorArray[position % 6]));

                holder.itemView.setOnClickListener(view -> {
                    Log.e("product_id--->", String.valueOf(wishListDataList.get(position).getProductimage().getProductId()));
                    Intent intent = new Intent(requireActivity(), ActProductDetails.class);
                    intent.putExtra("product_id", String.valueOf(wishListDataList.get(position).getProductimage().getProductId()));
                    startActivity(intent);
                });
            }

            @Override
            public int setItemLayout() {
                return R.layout.row_viewall;
            }

            @Override
            public RowViewallBinding getBinding(ViewGroup parent) {
                return RowViewallBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            }
        };
        fragFavBinding.rvwhishliast.setLayoutManager(manager);
        fragFavBinding.rvwhishliast.setItemAnimator(new DefaultItemAnimator());
        fragFavBinding.rvwhishliast.setAdapter(wishListDataAdapter);
    }

    private void callApiRemoveFavourite(HashMap<String, String> map, int position) {
        Common.showLoadingProgress(requireActivity());
        Call<SingleResponse> call = ApiClient.getClient().setRemoveFromWishList(map);
        Log.e("remove-->", new Gson().toJson(map));
        call.enqueue(new Callback<SingleResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {
                if (response.code() == 200) {
                    SingleResponse restResponse = response.body();
                    if (restResponse.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        wishListDataList.get(position).setIsWishlist(0);
                        wishListDataAdapter.notifyItemRemoved(position);
                        wishListDataList.remove(position);
                    } else if (restResponse.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(requireActivity(), restResponse.getMessage());
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(requireActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<SingleResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.error_msg));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        currentPage = 1;
        wishListDataList.clear();
        loadFeaturedProducts(wishListDataList);
        if (Common.isCheckNetwork(requireActivity())) {
            if (SharePreference.getBooleanPref(requireActivity(), SharePreference.isLogin)) {
                callApiWishList();
            } else {
                openActivity(ActLogin.class);
                requireActivity().finish();
            }
        } else {
            Common.alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.no_internet));
        }
        Common.getCurrentLanguage(requireActivity(), false);
    }
}
