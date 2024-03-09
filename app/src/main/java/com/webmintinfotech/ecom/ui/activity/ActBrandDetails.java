package com.webmintinfotech.ecom.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.api.SingleResponse;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.base.BaseAdaptor;
import com.webmintinfotech.ecom.databinding.ActBrandDetailsBinding;
import com.webmintinfotech.ecom.databinding.RowGravityBinding;
import com.webmintinfotech.ecom.model.BrandDetailsDataItem;
import com.webmintinfotech.ecom.model.BrandDetailsResponse;
import com.webmintinfotech.ecom.ui.authentication.ActLogin;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.SharePreference;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.HashMap;

public class ActBrandDetails extends BaseActivity {
    private ActBrandDetailsBinding brandDetailsBinding;
    private ArrayList<BrandDetailsDataItem> branddetailsDataList = new ArrayList<>();
    private String currency = "";
    private String currencyPosition = "";
    private BaseAdaptor<BrandDetailsDataItem, RowGravityBinding> brandDetailsAdapter = null;
    private LinearLayoutManager linearLayoutManager;
    private  int currentPage = 1;
    private int total_pages = 0;
    private int visibleItemCount = 0;
    private int totalItemCount = 0;
    private int pastVisibleItems = 0;
    private String[] colorArray = {
            "#FDF7FF",
            "#FDF3F0",
            "#EDF7FD",
            "#FFFAEA",
            "#F1FFF6",
            "#FFF5EC"
    };

    @Override
    public View setLayout() {
        return brandDetailsBinding.getRoot();
    }

    @Override
    public void initView() {
        brandDetailsBinding = ActBrandDetailsBinding.inflate(getLayoutInflater());
        brandDetailsBinding.ivBack.setOnClickListener(view -> {
            finish();
            setResult(RESULT_OK);
        });
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        currency = SharePreference.getStringPref(this, SharePreference.Currency);
        currencyPosition = SharePreference.getStringPref(this, SharePreference.CurrencyPosition);

        if (Common.isCheckNetwork(this)) {
            callApiBrandsDetail(String.valueOf(currentPage), getIntent().getStringExtra("brand_id"));
            brandDetailsBinding.tvtitle.setText(getIntent().getStringExtra("brand_name"));
        } else {
            Common.alertErrorOrValidationDialog(this, getResources().getString(R.string.no_internet));
        }

        brandDetailsBinding.rvBrandList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();
                    if (currentPage < total_pages) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            currentPage += 1;
                            callApiBrandsDetail(String.valueOf(currentPage), getIntent().getStringExtra("brand_id"));
                        }
                    }
                }
            }
        });
    }

    // TODO API BRANDS DETAILS CALL
    private void callApiBrandsDetail(String currentPage, String brandId) {
        Common.showLoadingProgress(this);
        HashMap<String, String> hasmap = new HashMap<>();
        hasmap.put("user_id", SharePreference.getStringPref(this, SharePreference.userId));
        hasmap.put("brand_id", brandId);

        Call<BrandDetailsResponse> call = ApiClient.getClient().getBrandDetails(currentPage, hasmap);
        call.enqueue(new Callback<BrandDetailsResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<BrandDetailsResponse> call, Response<BrandDetailsResponse> response) {
                if (response.code() == 200) {
                    BrandDetailsResponse restResponce = response.body();
                    if (restResponce.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        if (restResponce.getAlldata() != null && restResponce.getAlldata().getData() != null) {
                            branddetailsDataList.addAll(restResponce.getAlldata().getData());
                        }
                        loadBrandDetails(branddetailsDataList);
//                        currentPage = String.valueOf(Integer.parseInt(String.valueOf(restResponce.getAlldata().getCurrentPage())));
                        total_pages = Integer.parseInt(String.valueOf(restResponce.getAlldata().getLastPage()));
                        brandDetailsAdapter.notifyDataSetChanged();
                    } else if (restResponce.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(ActBrandDetails.this, restResponce.getMessage());
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(ActBrandDetails.this, getResources().getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(Call<BrandDetailsResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActBrandDetails.this, getResources().getString(R.string.error_msg));
            }
        });
    }

    // TODO SET BRANDS DETAILS DATA
    private void loadBrandDetails(ArrayList<BrandDetailsDataItem> branddetailsDataList) {
        brandDetailsAdapter = new BaseAdaptor<BrandDetailsDataItem, RowGravityBinding>(this, branddetailsDataList) {
            @NonNull
            @Override
            public RowGravityBinding getBinding(@NonNull ViewGroup parent) {
                return new RowGravityBindingHolder(RowGravityBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false)).getBinding();
            }

            @Override
            public int setItemLayout() {
                return R.layout.row_gravity;
            }

            @SuppressLint({"NewApi", "ResourceType", "SetTextI18n"})
            @Override
            public void onBindData(RecyclerView.ViewHolder holder, BrandDetailsDataItem val, int position) {
                RowGravityBinding binding = ((RowGravityBindingHolder) holder).getBinding();

                if (branddetailsDataList.get(position).getIsWishlist() == 0) {
                    binding.ivwishlist.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_dislike, null));
                } else {
                    binding.ivwishlist.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_like, null));
                }

                binding.ivwishlist.setOnClickListener(view -> {
                    if (SharePreference.getBooleanPref(ActBrandDetails.this, SharePreference.isLogin)) {
                        if (branddetailsDataList.get(position).getIsWishlist() == 0) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("product_id", branddetailsDataList.get(position).getId().toString());
                            map.put("user_id", SharePreference.getStringPref(ActBrandDetails.this, SharePreference.userId));

                            if (Common.isCheckNetwork(ActBrandDetails.this)) {
                                callApiFavourite(map, position, branddetailsDataList);
                            } else {
                                Common.alertErrorOrValidationDialog(ActBrandDetails.this, getResources().getString(R.string.no_internet));
                            }
                        }

                        if (branddetailsDataList.get(position).getIsWishlist() == 1) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("product_id", branddetailsDataList.get(position).getId().toString());
                            map.put("user_id", SharePreference.getStringPref(ActBrandDetails.this, SharePreference.userId));

                            if (Common.isCheckNetwork(ActBrandDetails.this)) {
                                callApiRemoveFavourite(map, position, branddetailsDataList);
                            } else {
                                Common.alertErrorOrValidationDialog(ActBrandDetails.this, getResources().getString(R.string.no_internet));
                            }
                        }

                    } else {
                        openActivity(ActLogin.class);
                        finish();
                    }
                });

                binding.tvcateitemname.setText(branddetailsDataList.get(position).getProductName());

                binding.tvProductPrice.setText(Common.getPrice(currencyPosition, currency, branddetailsDataList.get(position).getProductPrice().toString()));

                if (branddetailsDataList.get(position).getDiscountedPrice() == null || branddetailsDataList.get(position).getDiscountedPrice().equals("0")) {
                    binding.tvProductDisprice.setVisibility(View.GONE);
                } else {
                    binding.tvProductDisprice.setText(Common.getPrice(currencyPosition, currency, branddetailsDataList.get(position).getDiscountedPrice()));
                }

                binding.getRoot().setOnClickListener(view -> {
                    Log.e("product_id--->", branddetailsDataList.get(position).getId().toString());
                    Intent intent = new Intent(ActBrandDetails.this, ActProductDetails.class);
                    intent.putExtra("product_id", branddetailsDataList.get(position).getProductimage().getProductId().toString());
                    startActivity(intent);
                });

                Glide.with(ActBrandDetails.this)
                        .load(branddetailsDataList.get(position).getProductimage().getImageUrl())
                        .into(binding.ivCartitemm);

                binding.ivCartitemm.setBackgroundColor(Color.parseColor(colorArray[position % 6]));
            }


        };

        if (branddetailsDataList.size() > 0) {
            brandDetailsBinding.rvBrandList.setVisibility(View.VISIBLE);
            brandDetailsBinding.tvNoDataFound.setVisibility(View.GONE);
            brandDetailsBinding.rvBrandList.setLayoutManager(linearLayoutManager);
            brandDetailsBinding.rvBrandList.setItemAnimator(new DefaultItemAnimator());
            brandDetailsBinding.rvBrandList.setAdapter(brandDetailsAdapter);
        } else {
            brandDetailsBinding.rvBrandList.setVisibility(View.GONE);
            brandDetailsBinding.tvNoDataFound.setVisibility(View.VISIBLE);
        }
    }

    private void callApiRemoveFavourite(HashMap<String, String> map, int position, ArrayList<BrandDetailsDataItem> branddetailsDataList) {
        Common.showLoadingProgress(ActBrandDetails.this);
        Call<SingleResponse> call = ApiClient.getClient().setRemoveFromWishList(map);
        call.enqueue(new Callback<SingleResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {
                if (response.code() == 200) {
                    SingleResponse restResponse = response.body();
                    if (restResponse.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        branddetailsDataList.get(position).setIsWishlist(0);
                        brandDetailsAdapter.notifyItemChanged(position);
                    } else if (restResponse.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(ActBrandDetails.this, restResponse.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<SingleResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActBrandDetails.this, getResources().getString(R.string.error_msg));
            }
        });
    }

    private void callApiFavourite(HashMap<String, String> map, int position, ArrayList<BrandDetailsDataItem> branddetailsDataList) {
        Common.showLoadingProgress(ActBrandDetails.this);
        Call<SingleResponse> call = ApiClient.getClient().setAddToWishList(map);
        call.enqueue(new Callback<SingleResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {
                if (response.code() == 200) {
                    SingleResponse restResponse = response.body();
                    if (restResponse.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        branddetailsDataList.get(position).setIsWishlist(1);
                        brandDetailsAdapter.notifyItemChanged(position);
                    } else if (restResponse.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(ActBrandDetails.this, restResponse.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<SingleResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActBrandDetails.this, getResources().getString(R.string.error_msg));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Common.getCurrentLanguage(this, false);
    }

    public static class RowGravityBindingHolder extends RecyclerView.ViewHolder {
        private final RowGravityBinding binding;

        public RowGravityBindingHolder(RowGravityBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public RowGravityBinding getBinding() {
            return binding;
        }
    }
}
