package com.webmintinfotech.ecom.ui.fragment;

import static com.webmintinfotech.ecom.utils.Common.alertErrorOrValidationDialog;
import static com.webmintinfotech.ecom.utils.Common.dismissLoadingProgress;
import static com.webmintinfotech.ecom.utils.Common.getCurrentLanguage;
import static com.webmintinfotech.ecom.utils.Common.isCheckNetwork;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.gson.Gson;
import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.api.SingleResponse;
import com.webmintinfotech.ecom.base.BaseAdaptor;
import com.webmintinfotech.ecom.base.BaseFragment;
import com.webmintinfotech.ecom.databinding.*;
import com.webmintinfotech.ecom.model.*;
import com.webmintinfotech.ecom.ui.activity.*;
import com.webmintinfotech.ecom.ui.authentication.ActLogin;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.SharePreference;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragment<FragHomeBinding> {
    private FragHomeBinding fragHomeBinding;
    private ArrayList<TopbannerItem> bannerList;
    private ArrayList<LeftbannerItem> leftbannerList;
    private ArrayList<BottombannerItem> bottombannerList;
    private ArrayList<FeaturedProductsItem> featuredProductsList;
    private ArrayList<HotProductsItem> hotdealsList;
    private ArrayList<NewProductsItem> newProductsList;
    private ArrayList<LargebannerItem> largebannerList;
    private ArrayList<DataItem> categoriesList;
    private BaseAdaptor<FeaturedProductsItem, RowFeaturedproductBinding> featuredProductsAdapter;
    private BaseAdaptor<NewProductsItem, RowFeaturedproductBinding> newProductsAdaptor;
    private BaseAdaptor<HotProductsItem, RowFeaturedproductBinding> hotdealsAdaptor;
    boolean isAPICalling = false;
    String[] colorArray = {
            "#FDF7FF",
            "#FDF3F0",
            "#EDF7FD",
            "#FFFAEA",
            "#F1FFF6",
            "#FFF5EC"
    };

    @Override
    public void initView(View view) {
        fragHomeBinding = FragHomeBinding.bind(view);
        init();
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(fragHomeBinding.rvBannerproduct);
        snapHelper.attachToRecyclerView(fragHomeBinding.rvBanner);
        snapHelper.attachToRecyclerView(fragHomeBinding.rvNewBanner);
        snapHelper.attachToRecyclerView(fragHomeBinding.rvHotDealsBanner);
    }

    private void init() {
        fragHomeBinding.tvViewAllCate.setOnClickListener(view -> openActivity(ActAllCategories.class));
        fragHomeBinding.tvViewAllfp.setOnClickListener(view -> {
            Intent intent = new Intent(requireActivity(), ActViewAll.class);
            intent.putExtra("title", fragHomeBinding.tvfeaturedproduct.getText().toString());
            startActivity(intent);
        });
        fragHomeBinding.tvViewAllvendors.setOnClickListener(view -> openActivity(ActVendors.class));
        fragHomeBinding.tvViewArrivals.setOnClickListener(view -> {
            Intent intent = new Intent(requireActivity(), ActViewAll.class);
            intent.putExtra("title", fragHomeBinding.tvnewArrivals.getText().toString());
            startActivity(intent);
        });
        fragHomeBinding.tvViewAllhotdeals.setOnClickListener(view -> {
            Intent intent = new Intent(requireActivity(), ActViewAll.class);
            intent.putExtra("title", fragHomeBinding.tvHotDeals.getText().toString());
            startActivity(intent);
        });

        fragHomeBinding.tvViewAllBrand.setOnClickListener(view -> openActivity(ActBrand.class));

        fragHomeBinding.ivnotification.setOnClickListener(view -> openActivity(ActNotification.class));

        fragHomeBinding.ivSearch.setOnClickListener(view -> openActivity(ActSearch.class));
    }

    @Override
    public FragHomeBinding getBinding() {
        fragHomeBinding = FragHomeBinding.inflate(getLayoutInflater());
        return fragHomeBinding;
    }

    //TODO FirstBanner
    private void callApiBanner() {
        Common.showLoadingProgress(requireActivity());
        Call<BannerResponse> call = ApiClient.getClient().getbanner();
        call.enqueue(new Callback<BannerResponse>() {
            @Override
            public void onResponse(Call<BannerResponse> call, Response<BannerResponse> response) {
                if (response.code() == 200) {
                    BannerResponse restResponce = response.body();
                    dismissLoadingProgress();
                    if (restResponce.getStatus() == 1) {
                        fragHomeBinding.rvBanner.setVisibility(View.VISIBLE);
                        fragHomeBinding.rvBannerproduct.setVisibility(View.VISIBLE);
                        fragHomeBinding.rvBrandBanner.setVisibility(View.VISIBLE);
                        fragHomeBinding.view.setVisibility(View.VISIBLE);
                        fragHomeBinding.tvNoDataFound.setVisibility(View.GONE);
                        bannerList = restResponce.getTopbanner();
                        bottombannerList = restResponce.getBottombanner();
                        leftbannerList = restResponce.getLeftbanner();
                        largebannerList = restResponce.getLargebanner();
                        if (isAdded()) {
                            if (restResponce.getSliders() != null) {
                                loadPagerImagesSliders(restResponce.getSliders());
                                callCategories();
                            }
                        }
                    } else if (restResponce.getStatus() == 0) {
                        fragHomeBinding.rvBanner.setVisibility(View.GONE);
                        fragHomeBinding.tvNoDataFound.setVisibility(View.VISIBLE);
                        fragHomeBinding.rvBannerproduct.setVisibility(View.GONE);
                        fragHomeBinding.rvBrandBanner.setVisibility(View.GONE);
                        fragHomeBinding.view.setVisibility(View.GONE);
                        fragHomeBinding.view7.setVisibility(View.GONE);
                        dismissLoadingProgress();
                        alertErrorOrValidationDialog(requireActivity(), restResponce.getMessage());
                    }
                } else {
                    if (isAdded()) {
                        dismissLoadingProgress();
                        alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.error_msg));
                    }
                }
            }

            @Override
            public void onFailure(Call<BannerResponse> call, Throwable t) {
                if (isAdded()) {
                    dismissLoadingProgress();
                    alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.error_msg));
                }
            }
        });
    }

    //TODO first banner
    private void loadPagerImagesSliders(ArrayList<SlidersItem> slidersList) {
        final com.webmintinfotech.ecom.databinding.RowBannerBinding[] binding = new com.webmintinfotech.ecom.databinding.RowBannerBinding[1];
        BaseAdaptor<SlidersItem, RowBannerBinding> bannerAdapter = new BaseAdaptor<SlidersItem, RowBannerBinding>(requireActivity(), slidersList) {
            @Override
            public void onBindData(RecyclerView.ViewHolder holder, SlidersItem val, int position) {
                Glide.with(requireActivity()).load(slidersList.get(position).getImageUrl()).into(binding[0].ivBanner);
                binding[0].ivBanner.setBackgroundColor(Color.parseColor(colorArray[position % 6]));

                holder.itemView.setOnClickListener(view -> {
                    Uri webpage = Uri.parse(slidersList.get(position).getLink());
                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                    if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    }
                });
            }

            @Override
            public int setItemLayout() {
                return R.layout.row_banner;
            }

            @Override
            public RowBannerBinding getBinding(ViewGroup parent) {
                binding[0] = RowBannerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return binding[0];
            }
        };

        fragHomeBinding.rvBanner.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
        fragHomeBinding.rvBanner.setItemAnimator(new DefaultItemAnimator());
        fragHomeBinding.rvBanner.setAdapter(bannerAdapter);
    }

    //TODO SecondBanner Images
    private void loadPagerImages(ArrayList<TopbannerItem> topbannerList) {
        final com.webmintinfotech.ecom.databinding.RowBannerproductBinding[] binding = {null};
        BaseAdaptor<TopbannerItem, RowBannerproductBinding> bannerAdapter = new BaseAdaptor<TopbannerItem, RowBannerproductBinding>(requireActivity(), topbannerList) {
            @Override
            public void onBindData(RecyclerView.ViewHolder holder, TopbannerItem val, int position) {
                TopbannerItem data = topbannerList.get(position);
                binding[0].ivBanner.setBackgroundColor(Color.parseColor(colorArray[position % 6]));

                Glide.with(requireActivity()).load(data.getImageUrl()).into(binding[0].ivBanner);
                holder.itemView.setOnClickListener(view -> typeWiseNavigation(
                        data.getType().toString(),
                        data.getCatId().toString(),
                        data.getCategoryName().toString(),
                        data.getProductId().toString()
                ));
            }

            @Override
            public int setItemLayout() {
                return R.layout.row_bannerproduct;
            }

            @Override
            public RowBannerproductBinding getBinding(ViewGroup parent) {
                binding[0] = RowBannerproductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return binding[0];
            }
        };

        if (isAdded()) {
            fragHomeBinding.rvBannerproduct.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
            fragHomeBinding.rvBannerproduct.setItemAnimator(new DefaultItemAnimator());
            fragHomeBinding.rvBannerproduct.setAdapter(bannerAdapter);
        }
    }

    private void typeWiseNavigation(String type, String catId, String catName, String productId) {
        if ("category".equals(type)) {
            Intent intent = new Intent(requireActivity(), ActAllSubCategories.class);
            Bundle extras = new Bundle();
            extras.putString("cat_id", catId);
            extras.putString("categoryName", catName);
            intent.putExtras(extras);
            startActivity(intent);
        } else if ("product".equals(type)) {
            Intent intent = new Intent(requireActivity(), ActProductDetails.class);
            intent.putExtra("product_id", productId);
            startActivity(intent);
        }
    }

    //TODO categories api call
    private void callCategories() {
        Call<CategoriesResponse> call = ApiClient.getClient().getcategory();
        call.enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                if (response.code() == 200) {
                    CategoriesResponse restResponce = response.body();
                    if (restResponce.getStatus() == 1) {
                        dismissLoadingProgress();
                        categoriesList = restResponce.getData();
                        if (isAdded()) {
                            if (categoriesList != null && categoriesList.size() > 0) {
                                fragHomeBinding.rvCategories.setVisibility(View.VISIBLE);
                                fragHomeBinding.tvCategories.setVisibility(View.VISIBLE);
                                fragHomeBinding.tvViewAllCate.setVisibility(View.VISIBLE);
                                fragHomeBinding.view1.setVisibility(View.VISIBLE);
                                loadCategoriesDeals(categoriesList);
                                callApiFeaturedProducts();
                            } else {
                                fragHomeBinding.rvCategories.setVisibility(View.GONE);
                                fragHomeBinding.tvCategories.setVisibility(View.GONE);
                                fragHomeBinding.tvViewAllCate.setVisibility(View.GONE);
                                fragHomeBinding.view1.setVisibility(View.GONE);
                            }
                        }
                    } else if (restResponce.getStatus() == 0) {
                        dismissLoadingProgress();
                        alertErrorOrValidationDialog(requireActivity(), restResponce.getMessage());
                    }
                } else {
                    if (isAdded()) {
                        dismissLoadingProgress();
                        alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.error_msg));
                    }
                }
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                dismissLoadingProgress();
                alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.error_msg));
            }
        });
    }

    //TODO categories Adapter
    private void loadCategoriesDeals(ArrayList<DataItem> categoriesList) {
        final com.webmintinfotech.ecom.databinding.RowCategoriesBinding[] binding = {null};
        BaseAdaptor<DataItem, RowCategoriesBinding> categoriesAdaptor = new BaseAdaptor<DataItem, RowCategoriesBinding>(requireActivity(), categoriesList) {
            @Override
            public void onBindData(RecyclerView.ViewHolder holder, DataItem val, int position) {
                binding[0].tvCategoriesName.setText(categoriesList.get(position).getCategoryName());

                Glide.with(requireActivity())
                        .load(categoriesList.get(position).getImageUrl()).into(binding[0].ivCategories);
                binding[0].clMain.setBackgroundColor(Color.parseColor(colorArray[position % 6]));
                binding[0].ivCategories.setBackgroundColor(Color.parseColor(colorArray[position % 6]));
                holder.itemView.setOnClickListener(view -> {
                    Log.e("cat_id--->", categoriesList.get(position).getId().toString());
                    Intent intent = new Intent(requireActivity(), ActAllSubCategories.class);
                    Bundle extras = new Bundle();
                    extras.putString("cat_id", categoriesList.get(position).getId().toString());
                    extras.putString("categoryName", categoriesList.get(position).getCategoryName().toString());
                    intent.putExtras(extras);
                    startActivity(intent);
                });
            }

            @Override
            public int setItemLayout() {
                return R.layout.row_categories;
            }

            @Override
            public RowCategoriesBinding getBinding(ViewGroup parent) {
                binding[0] = RowCategoriesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return binding[0];
            }
        };
        if (isAdded()) {
            fragHomeBinding.rvCategories.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
            fragHomeBinding.rvCategories.setItemAnimator(new DefaultItemAnimator());
            fragHomeBinding.rvCategories.setAdapter(categoriesAdaptor);
        }
    }

    //TODO Api HomeFeed call
    private void callApiFeaturedProducts() {
        HashMap<String, String> hasmap = new HashMap<>();
        hasmap.put("user_id", SharePreference.getStringPref(requireActivity(), SharePreference.userId));
        Call<HomefeedResponse> call = ApiClient.getClient().gethomefeeds(hasmap);
        call.enqueue(new Callback<HomefeedResponse>() {
            @Override
            public void onResponse(Call<HomefeedResponse> call, Response<HomefeedResponse> response) {
                if (response.code() == 200) {
                    HomefeedResponse restResponce = response.body();
                    if (restResponce.getStatus() == 1) {
                        featuredProductsList = restResponce.getFeaturedProducts();
                        newProductsList = restResponce.getNewProducts();
                        hotdealsList = restResponce.getHotProducts();
                        if (isAdded()) {
                            dismissLoadingProgress();
                            if (getActivity() != null) {
                                SharePreference.setStringPref(getActivity(), SharePreference.Currency, restResponce.getCurrency());
                            }
                            if (getActivity() != null) {
                                SharePreference.setStringPref(getActivity(), SharePreference.CurrencyPosition, restResponce.getCurrencyPosition());
                            }
                            if (getActivity() != null) {
                                SharePreference.setStringPref(getActivity(), SharePreference.ReferralAmount, restResponce.getReferralAmount());
                            }
                            if (bannerList != null && bannerList.size() > 0) {
                                loadPagerImages(bannerList);
                                fragHomeBinding.rvBannerproduct.setVisibility(View.VISIBLE);
                                fragHomeBinding.view2.setVisibility(View.VISIBLE);
                            } else {
                                fragHomeBinding.rvBannerproduct.setVisibility(View.GONE);
                                fragHomeBinding.view2.setVisibility(View.GONE);
                            }
                            if (featuredProductsList != null && featuredProductsList.size() > 0) {
                                loadFeaturedProducts(featuredProductsList, restResponce.getCurrency(), restResponce.getCurrencyPosition());
                                fragHomeBinding.rvfeaturedproduct.setVisibility(View.VISIBLE);
                                fragHomeBinding.tvfeaturedproduct.setVisibility(View.VISIBLE);
                                fragHomeBinding.tvViewAllfp.setVisibility(View.VISIBLE);
                                fragHomeBinding.view1.setVisibility(View.VISIBLE);
                            } else {
                                fragHomeBinding.rvfeaturedproduct.setVisibility(View.GONE);
                                fragHomeBinding.tvfeaturedproduct.setVisibility(View.GONE);
                                fragHomeBinding.tvViewAllfp.setVisibility(View.GONE);
                                fragHomeBinding.view1.setVisibility(View.GONE);
                            }
                            if (restResponce.getVendors() != null && restResponce.getVendors().size() > 0) {
                                loadVendors(restResponce.getVendors());
                                fragHomeBinding.rvvendors.setVisibility(View.GONE);
                                fragHomeBinding.tvvendors.setVisibility(View.GONE);
                                fragHomeBinding.tvViewAllvendors.setVisibility(View.GONE);
                                fragHomeBinding.view3.setVisibility(View.GONE);
                            } else {
                                fragHomeBinding.rvvendors.setVisibility(View.GONE);
                                fragHomeBinding.tvvendors.setVisibility(View.GONE);
                                fragHomeBinding.tvViewAllvendors.setVisibility(View.GONE);
                                fragHomeBinding.view3.setVisibility(View.GONE);
                            }
                            if (newProductsList != null && newProductsList.size() > 0) {
                                loadNewProduct(newProductsList, restResponce.getCurrency(), restResponce.getCurrencyPosition());
                                fragHomeBinding.tvnewArrivals.setVisibility(View.VISIBLE);
                                fragHomeBinding.tvViewArrivals.setVisibility(View.VISIBLE);
                                fragHomeBinding.rvnewArrivals.setVisibility(View.VISIBLE);
                                fragHomeBinding.view4.setVisibility(View.VISIBLE);
                            } else {
                                fragHomeBinding.rvnewArrivals.setVisibility(View.GONE);
                                fragHomeBinding.tvnewArrivals.setVisibility(View.GONE);
                                fragHomeBinding.tvViewArrivals.setVisibility(View.GONE);
                                fragHomeBinding.view4.setVisibility(View.GONE);
                            }
                            if (bottombannerList != null && bottombannerList.size() > 0) {
                                loadPagerImagesBottomBanner(bottombannerList);
                                fragHomeBinding.rvNewBanner.setVisibility(View.VISIBLE);
                                fragHomeBinding.view5.setVisibility(View.VISIBLE);
                            } else {
                                fragHomeBinding.rvNewBanner.setVisibility(View.GONE);
                                fragHomeBinding.view5.setVisibility(View.GONE);
                            }
                            if (leftbannerList != null && leftbannerList.size() > 0) {
                                loadPagerImagesLeftBanner(leftbannerList);
                                fragHomeBinding.rvBrandBanner.setVisibility(View.VISIBLE);
                            } else {
                                fragHomeBinding.rvBrandBanner.setVisibility(View.GONE);
                            }
                            if (restResponce.getBrands() != null && restResponce.getBrands().size() > 0) {
                                loadBrands(restResponce.getBrands());
                                fragHomeBinding.rvBrand.setVisibility(View.VISIBLE);
                                fragHomeBinding.tvBrand.setVisibility(View.VISIBLE);
                                fragHomeBinding.tvViewAllBrand.setVisibility(View.VISIBLE);
                                fragHomeBinding.view6.setVisibility(View.VISIBLE);
                            } else {
                                fragHomeBinding.rvBrand.setVisibility(View.GONE);
                                fragHomeBinding.tvBrand.setVisibility(View.GONE);
                                fragHomeBinding.tvViewAllBrand.setVisibility(View.GONE);
                                fragHomeBinding.view6.setVisibility(View.GONE);
                            }
                            if (hotdealsList != null && hotdealsList.size() > 0) {
                                loadHotDeals(hotdealsList, restResponce.getCurrency(), restResponce.getCurrencyPosition());
                                fragHomeBinding.rvHotDeals.setVisibility(View.VISIBLE);
                                fragHomeBinding.tvHotDeals.setVisibility(View.VISIBLE);
                                fragHomeBinding.tvViewAllhotdeals.setVisibility(View.VISIBLE);
                                fragHomeBinding.view7.setVisibility(View.VISIBLE);
                            } else {
                                fragHomeBinding.rvHotDeals.setVisibility(View.GONE);
                                fragHomeBinding.tvHotDeals.setVisibility(View.GONE);
                                fragHomeBinding.tvViewAllhotdeals.setVisibility(View.GONE);
                                fragHomeBinding.view7.setVisibility(View.GONE);
                            }
                            if (largebannerList != null && largebannerList.size() > 0) {
                                loadPagerImagesLargeBanner(largebannerList);
                                fragHomeBinding.rvHotDealsBanner.setVisibility(View.VISIBLE);
                            } else {
                                fragHomeBinding.rvHotDealsBanner.setVisibility(View.GONE);
                            }
                            if (restResponce.getNotifications() == 1) {
                                fragHomeBinding.rlCountnotification.setVisibility(View.VISIBLE);
                            } else {
                                fragHomeBinding.rlCountnotification.setVisibility(View.GONE);
                            }
                        }
                    } else if (restResponce.getStatus() == 0) {
                        dismissLoadingProgress();
                        alertErrorOrValidationDialog(requireActivity(), restResponce.getMessage());
                    }

                } else {
                    if (isAdded()) {
                        dismissLoadingProgress();
                        alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.error_msg));
                    }
                }
            }

            @Override
            public void onFailure(Call<HomefeedResponse> call, Throwable t) {
                dismissLoadingProgress();
                alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.error_msg));
            }
        });
    }


//TODO featured Products Adapter
private void loadFeaturedProducts(
        ArrayList<FeaturedProductsItem> featuredProductsList,
        String currency,
        String currencyPosition
) {
    final com.webmintinfotech.ecom.databinding.RowFeaturedproductBinding[] binding = {null};
    featuredProductsAdapter = new BaseAdaptor<FeaturedProductsItem, RowFeaturedproductBinding>(
            requireActivity(),
            featuredProductsList
    ) {
        @SuppressLint({"NewApi", "ResourceType", "SetTextI18n"})
        @Override
        public void onBindData(
                RecyclerView.ViewHolder holder,
                FeaturedProductsItem val,
                int position
        ) {
            if (featuredProductsList.get(position).getIsWishlist() == 0) {
                binding[0].ivwishlist.setImageDrawable(
                        ResourcesCompat.getDrawable(
                                getResources(),
                                R.drawable.ic_dislike,
                                null
                        )
                );
            } else {
                binding[0].ivwishlist.setImageDrawable(
                        ResourcesCompat.getDrawable(
                                getResources(),
                                R.drawable.ic_like,
                                null
                        )
                );
            }

            binding[0].ivwishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SharePreference.getBooleanPref(
                            requireActivity(),
                            SharePreference.isLogin
                    )) {
                        if (featuredProductsList.get(position).getIsWishlist() == 0) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("product_id", String.valueOf(featuredProductsList.get(position).getId()));
                            map.put("user_id", SharePreference.getStringPref(
                                    requireActivity(),
                                    SharePreference.userId
                            ));

                            if (isCheckNetwork(requireActivity())) {
                                callApiFavourite(map, position, "1");
                            } else {
                                alertErrorOrValidationDialog(
                                        requireActivity(),
                                        getResources().getString(R.string.no_internet)
                                );
                            }
                        }

                        if (featuredProductsList.get(position).getIsWishlist() == 1) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("product_id", String.valueOf(featuredProductsList.get(position).getId()));
                            map.put("user_id", SharePreference.getStringPref(
                                    requireActivity(),
                                    SharePreference.userId
                            ));

                            if (isCheckNetwork(requireActivity())) {
                                callApiRemoveFavourite(map, position, "1");
                            } else {
                                alertErrorOrValidationDialog(
                                        requireActivity(),
                                        getResources().getString(R.string.no_internet)
                                );
                            }
                        }
                    } else {
                        openActivity(ActLogin.class);
                        getActivity().finish();
                    }
                }
            });

            if (featuredProductsList.get(position).getRattings().size() == 0) {
                binding[0].tvRatePro.setText("0.0");
            } else {
                binding[0].tvRatePro.setText(featuredProductsList.get(position).getRattings().get(0).getAvgRatting().toString());
            }

            binding[0].tvProductName.setText(featuredProductsList.get(position).getProductName());

            binding[0].tvProductPrice.setText(Common.getPrice(
                    currencyPosition,
                    currency,
                    featuredProductsList.get(position).getProductPrice().toString()
            ));
            if ("0".equals(featuredProductsList.get(position).getDiscountedPrice()) || featuredProductsList.get(position).getDiscountedPrice() == null) {
                binding[0].tvProductDisprice.setVisibility(View.GONE);
            } else {
                binding[0].tvProductDisprice.setText(Common.getPrice(
                        currencyPosition,
                        currency,
                        featuredProductsList.get(position).getDiscountedPrice().toString()
                ));
            }
            Glide.with(requireActivity())
                    .load(featuredProductsList.get(position).getProductimage().getImageUrl())
                    .transition(DrawableTransitionOptions.withCrossFade(1000))
                    .into(binding[0].ivProduct);
            binding[0].ivProduct.setBackgroundColor(Color.parseColor(colorArray[position % 6]));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("product_id--->", String.valueOf(featuredProductsList.get(position).getId()));
                    Intent intent = new Intent(requireActivity(), ActProductDetails.class);
                    intent.putExtra("product_id", String.valueOf(featuredProductsList.get(position).getId()));
                    startActivity(intent);
                }
            });
        }

        @Override
        public int setItemLayout() {
            return R.layout.row_featuredproduct;
        }

        @Override
        public RowFeaturedproductBinding getBinding(ViewGroup parent) {
            binding[0] = RowFeaturedproductBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false
            );
            return binding[0];
        }
    };
    if (isAdded()) {
      fragHomeBinding.rvfeaturedproduct.setNestedScrollingEnabled(true);
        fragHomeBinding.rvfeaturedproduct.setLayoutManager(new GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false));
        fragHomeBinding.rvfeaturedproduct.setItemAnimator(new DefaultItemAnimator());
        fragHomeBinding.rvfeaturedproduct.setAdapter(featuredProductsAdapter);
    }
}

//TODO SecondBanner Images
private void loadPagerImagesBottomBanner(ArrayList<BottombannerItem> bottombannerList) {
    final com.webmintinfotech.ecom.databinding.RowBannernewBinding[] binding = {null};
    BaseAdaptor<BottombannerItem, RowBannernewBinding> bottombannerAdaptor = new BaseAdaptor<BottombannerItem, RowBannernewBinding>(
            requireActivity(),
            bottombannerList
    ) {
        @SuppressLint({"NewApi", "ResourceType"})
        @Override
        public void onBindData(
                RecyclerView.ViewHolder holder,
                BottombannerItem val,
                int position
        ) {
            BottombannerItem data = bottombannerList.get(position);
            Glide.with(requireActivity()).load(bottombannerList.get(position).getImageUrl())
                    .into(binding[0].ivBanner);
            binding[0].ivBanner.setBackgroundColor(Color.parseColor(colorArray[position % 6]));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    typeWiseNavigation(
                            data.getType().toString(),
                            data.getCatId().toString(),
                            data.getCategoryName().toString(),
                            data.getProductId().toString()
                    );
                }
            });
        }

        @Override
        public int setItemLayout() {
            return R.layout.row_bannernew;
        }

        @Override
        public RowBannernewBinding getBinding(ViewGroup parent) {
            binding[0] = RowBannernewBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false
            );
            return binding[0];
        }
    };
    if (isAdded()) {
//        fragHomeBinding.rvNewBanner.apply {
//            setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//            setItemAnimator(new DefaultItemAnimator());
//            setAdapter(bottombannerAdaptor);
//        }

        fragHomeBinding.rvNewBanner.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        fragHomeBinding.rvNewBanner.setItemAnimator(new DefaultItemAnimator());
        fragHomeBinding.rvNewBanner.setAdapter(bottombannerAdaptor);
    }
}

//TODO Vendors Adapter
private void loadVendors(ArrayList<VendorsItem> vendorsList) {
    final com.webmintinfotech.ecom.databinding.RowVendorsBinding[] binding = {null};
    BaseAdaptor<VendorsItem, RowVendorsBinding> vendorsAdapter = new BaseAdaptor<VendorsItem, RowVendorsBinding>(requireActivity(), vendorsList) {
        @SuppressLint({"NewApi", "ResourceType"})
        @Override
        public void onBindData(
                RecyclerView.ViewHolder holder,
                VendorsItem val,
                int position
        ) {
            binding[0].tvVendorsName.setText(vendorsList.get(position).getName());
            Glide.with(requireActivity()).load(vendorsList.get(position).getImageUrl())
                    .into(binding[0].ivvendors);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("vendor_id--->", String.valueOf(vendorsList.get(position).getId()));
                    Intent intent = new Intent(requireActivity(), ActVendorsDetails.class);
                    intent.putExtra("vendor_id", String.valueOf(vendorsList.get(position).getId()));
                    intent.putExtra("vendors_name", vendorsList.get(position).getName());
                    intent.putExtra("vendors_iv", vendorsList.get(position).getImageUrl());
                    intent.putExtra("vendors_rate", "0.0");
                    startActivity(intent);
                }
            });
        }

        @Override
        public int setItemLayout() {
            return R.layout.row_vendors;
        }

        @Override
        public RowVendorsBinding getBinding(ViewGroup parent) {
            binding[0] = RowVendorsBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false
            );
            return binding[0];
        }
    };
    if (isAdded()) {

        fragHomeBinding.rvvendors.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        fragHomeBinding.rvvendors.setItemAnimator(new DefaultItemAnimator());
        fragHomeBinding.rvvendors.setAdapter(vendorsAdapter);
    }
}

//TODO New Product Adapter
private void loadNewProduct(ArrayList<NewProductsItem> newProductsList, String currency, String currencyPosition) {
    final com.webmintinfotech.ecom.databinding.RowFeaturedproductBinding[] binding = new com.webmintinfotech.ecom.databinding.RowFeaturedproductBinding[1];
    newProductsAdaptor = new BaseAdaptor<NewProductsItem, RowFeaturedproductBinding>(requireActivity(), newProductsList) {
        @SuppressLint({"NewApi", "ResourceType", "SetTextI18n"})
        @Override
        public void onBindData(RecyclerView.ViewHolder holder, NewProductsItem val, int position) {
            binding[0] = ((ViewHolder) holder).getBinding();
            if (newProductsList.get(position).getRattings().size() == 0) {
                binding[0].tvRatePro.setText("0.0");
            } else {
                binding[0].tvRatePro.setText(newProductsList.get(position).getRattings().get(0).getAvgRatting().toString());
            }

            if (newProductsList.get(position).getIsWishlist() == 0) {
                binding[0].ivwishlist.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_dislike, null));
            } else {
                binding[0].ivwishlist.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_like, null));
            }

            binding[0].ivwishlist.setOnClickListener(view -> {
                if (SharePreference.getBooleanPref(requireActivity(), SharePreference.isLogin)) {
                    if (newProductsList.get(position).getIsWishlist() == 0) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("product_id", String.valueOf(newProductsList.get(position).getId()));
                        map.put("user_id", SharePreference.getStringPref(requireActivity(), SharePreference.userId));
                        if (isCheckNetwork(requireActivity())) {
                            callApiFavourite(map, position, "2");
                        } else {
                            alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.no_internet));
                        }
                    }

                    if (newProductsList.get(position).getIsWishlist() == 1) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("product_id", String.valueOf(newProductsList.get(position).getId()));
                        map.put("user_id", SharePreference.getStringPref(requireActivity(), SharePreference.userId));
                        if (isCheckNetwork(requireActivity())) {
                            callApiRemoveFavourite(map, position, "2");
                        } else {
                            alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.no_internet));
                        }
                    }
                } else {
                    openActivity(ActLogin.class);
                    getActivity().finish();
                }
            });

            binding[0].tvProductName.setText(newProductsList.get(position).getProductName());

            binding[0].tvProductPrice.setText(Common.getPrice(currencyPosition, currency, newProductsList.get(position).getProductPrice().toString()));
            if ("0".equals(newProductsList.get(position).getDiscountedPrice()) || newProductsList.get(position).getDiscountedPrice() == null) {
                binding[0].tvProductDisprice.setVisibility(View.GONE);
            } else {
                binding[0].tvProductDisprice.setText(Common.getPrice(currencyPosition, currency, newProductsList.get(position).getDiscountedPrice().toString()));
            }
            Glide.with(requireActivity())
                    .load(newProductsList.get(position).getProductimage().getImageUrl())
                    .transition(DrawableTransitionOptions.withCrossFade(1000))
                    .into(binding[0].ivProduct);
            binding[0].ivProduct.setBackgroundColor(Color.parseColor(colorArray[position % 6]));
            holder.itemView.setOnClickListener(view -> {
                Log.e("product_id--->", String.valueOf(newProductsList.get(position).getId()));
                Intent intent = new Intent(requireActivity(), ActProductDetails.class);
                intent.putExtra("product_id", String.valueOf(newProductsList.get(position).getId()));
                startActivity(intent);
            });
        }

        @Override
        public int setItemLayout() {
            return R.layout.row_featuredproduct;
        }

        @Override
        public RowFeaturedproductBinding getBinding(ViewGroup parent) {
            return RowFeaturedproductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        }
    };

    if (isAdded()) {
        fragHomeBinding.rvnewArrivals.setLayoutManager(new GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false));
        fragHomeBinding.rvnewArrivals.setItemAnimator(new DefaultItemAnimator());
        fragHomeBinding.rvnewArrivals.setAdapter(newProductsAdaptor);
    }
}


//TODO ThirdBanner Images
private void loadPagerImagesLeftBanner(ArrayList<LeftbannerItem> leftbannerList) {
    final com.webmintinfotech.ecom.databinding.RowBannerleftBinding[] binding = {null};
    BaseAdaptor<LeftbannerItem, RowBannerleftBinding> leftbannerAdaptor = new BaseAdaptor<LeftbannerItem, RowBannerleftBinding>(
            requireActivity(),
            leftbannerList
    ) {
        @SuppressLint({"NewApi", "ResourceType"})
        @Override
        public void onBindData(
                RecyclerView.ViewHolder holder,
                LeftbannerItem val,
                int position
        ) {
            LeftbannerItem data = leftbannerList.get(position);
            Glide.with(requireActivity()).load(leftbannerList.get(position).getImageUrl())
                    .into(binding[0].ivBanner);
            binding[0].ivBanner.setBackgroundColor(Color.parseColor(colorArray[position % 6]));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    typeWiseNavigation(
                            data.getType().toString(),
                            data.getCatId().toString(),
                            data.getCategoryName().toString(),
                            data.getProductId().toString()
                    );
                }
            });
        }

        @Override
        public int setItemLayout() {
            return R.layout.row_bannerleft;
        }

        @Override
        public RowBannerleftBinding getBinding(ViewGroup parent) {
            binding[0] = RowBannerleftBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false
            );
            return binding[0];
        }
    };
    if (isAdded()) {
        fragHomeBinding.rvBrandBanner.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        fragHomeBinding.rvBrandBanner.setItemAnimator(new DefaultItemAnimator());
        fragHomeBinding.rvBrandBanner.setAdapter(leftbannerAdaptor);
    }
}

//TODO Brand Adapter
private void loadBrands(ArrayList<BrandsItem> brandsList) {
    final com.webmintinfotech.ecom.databinding.RowBrandBinding[] binding = {null};
    BaseAdaptor<BrandsItem, RowBrandBinding> brandsAdapter = new BaseAdaptor<BrandsItem, RowBrandBinding>(requireActivity(), brandsList) {
        @SuppressLint({"NewApi", "ResourceType"})
        @Override
        public void onBindData(RecyclerView.ViewHolder holder, BrandsItem val, int position) {
            Glide.with(requireActivity())
                    .load(brandsList.get(position).getImageUrl())
                    .into(binding[0].ivBrands);
            binding[0].clMain.setBackgroundColor(Color.parseColor(colorArray[position % 6]));
            binding[0].ivBrands.setBackgroundColor(Color.parseColor(colorArray[position % 6]));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("brand_id--->", String.valueOf(brandsList.get(position).getId()));
                    Intent intent = new Intent(requireActivity(), ActBrandDetails.class);
                    intent.putExtra("brand_id", String.valueOf(brandsList.get(position).getId()));
                    intent.putExtra("brand_name", brandsList.get(position).getBrandName());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int setItemLayout() {
            return R.layout.row_brand;
        }

        @Override
        public RowBrandBinding getBinding(ViewGroup parent) {
            binding[0] = RowBrandBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false
            );
            return binding[0];
        }
    };

    if (isAdded()) {
        fragHomeBinding.rvBrand.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        fragHomeBinding.rvBrand.setItemAnimator(new DefaultItemAnimator());
        fragHomeBinding.rvBrand.setAdapter(brandsAdapter);
    }
}

//TODO Hot Deals Adapter
private void loadHotDeals(ArrayList<HotProductsItem> hotdealsList, String currency, String currencyPosition) {
    final com.webmintinfotech.ecom.databinding.RowFeaturedproductBinding[] binding = {null};
    hotdealsAdaptor = new BaseAdaptor<HotProductsItem, RowFeaturedproductBinding>(requireActivity(), hotdealsList) {
        @SuppressLint({"NewApi", "ResourceType", "SetTextI18n"})
        @Override
        public void onBindData(RecyclerView.ViewHolder holder, HotProductsItem val, int position) {
            if (hotdealsList.get(position).getRattings().size() == 0) {
                binding[0].tvRatePro.setText("0.0");
            } else {
                binding[0].tvRatePro.setText(hotdealsList.get(position).getRattings().get(0).getAvgRatting().toString());
            }

            if (hotdealsList.get(position).getIsWishlist() == 0) {
                binding[0].ivwishlist.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_dislike, null));
            } else {
                binding[0].ivwishlist.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_like, null));
            }

            binding[0].ivwishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SharePreference.getBooleanPref(requireActivity(), SharePreference.isLogin)) {
                        if (hotdealsList.get(position).getIsWishlist() == 0) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("product_id", String.valueOf(hotdealsList.get(position).getId()));
                            map.put("user_id", SharePreference.getStringPref(requireActivity(), SharePreference.userId));
                            if (isCheckNetwork(requireActivity())) {
                                callApiFavourite(map, position, "3");
                            } else {
                                alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.no_internet));
                            }
                        }

                        if (hotdealsList.get(position).getIsWishlist() == 1) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("product_id", String.valueOf(hotdealsList.get(position).getId()));
                            map.put("user_id", SharePreference.getStringPref(requireActivity(), SharePreference.userId));
                            if (isCheckNetwork(requireActivity())) {
                                callApiRemoveFavourite(map, position, "3");
                            } else {
                                alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.no_internet));
                            }
                        }
                    } else {
                        openActivity(ActLogin.class);
                        getActivity().finish();
                    }
                }
            });

            binding[0].tvProductName.setText(hotdealsList.get(position).getProductName());

            binding[0].tvProductPrice.setText(Common.getPrice(currencyPosition, currency, hotdealsList.get(position).getProductPrice().toString()));
            if ("0".equals(hotdealsList.get(position).getDiscountedPrice()) || hotdealsList.get(position).getDiscountedPrice() == null) {
                binding[0].tvProductDisprice.setVisibility(View.GONE);
            } else {
                binding[0].tvProductDisprice.setText(Common.getPrice(currencyPosition, currency, hotdealsList.get(position).getDiscountedPrice().toString()));
            }
            Glide.with(requireActivity())
                    .load(hotdealsList.get(position).getProductimage().getImageUrl())
                    .transition(DrawableTransitionOptions.withCrossFade(1000))
                    .into(binding[0].ivProduct);
            binding[0].ivProduct.setBackgroundColor(Color.parseColor(colorArray[position % 6]));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("product_id--->", String.valueOf(hotdealsList.get(position).getId()));
                    Intent intent = new Intent(requireActivity(), ActProductDetails.class);
                    intent.putExtra("product_id", String.valueOf(hotdealsList.get(position).getId()));
                    startActivity(intent);
                }
            });
        }

        @Override
        public int setItemLayout() {
            return R.layout.row_featuredproduct;
        }

        @Override
        public RowFeaturedproductBinding getBinding(ViewGroup parent) {
            binding[0] = RowFeaturedproductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return binding[0];
        }
    };

    if (isAdded()) {
        fragHomeBinding.rvHotDeals.setLayoutManager(new GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false));
        fragHomeBinding.rvHotDeals.setItemAnimator(new DefaultItemAnimator());
        fragHomeBinding.rvHotDeals.setAdapter(hotdealsAdaptor);
    }
}

//TODO LastBanner Images
private void loadPagerImagesLargeBanner(ArrayList<LargebannerItem> largebannerList) {
    final com.webmintinfotech.ecom.databinding.RowHotdealsbannerBinding[] binding = {null};
    BaseAdaptor<LargebannerItem, RowHotdealsbannerBinding> largebannerAdapter = new BaseAdaptor<LargebannerItem, RowHotdealsbannerBinding>(requireActivity(), largebannerList) {
        @SuppressLint({"NewApi", "ResourceType"})
        @Override
        public void onBindData(RecyclerView.ViewHolder holder, LargebannerItem val, int position) {
            LargebannerItem data = largebannerList.get(position);
            Glide.with(requireActivity()).load(largebannerList.get(position).getImageUrl())
                    .into(binding[0].ivBanner);
            binding[0].ivBanner.setBackgroundColor(Color.parseColor(colorArray[position % 6]));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    typeWiseNavigation(
                            data.getType().toString(),
                            data.getCatId().toString(),
                            data.getCategoryName().toString(),
                            data.getProductId().toString()
                    );
                }
            });
        }

        @Override
        public int setItemLayout() {
            return R.layout.row_hotdealsbanner;
        }

        @Override
        public RowHotdealsbannerBinding getBinding(ViewGroup parent) {
            binding[0] = RowHotdealsbannerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return binding[0];
        }
    };

    if (isAdded()) {

        fragHomeBinding.rvHotDealsBanner.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        fragHomeBinding.rvHotDealsBanner.setItemAnimator(new DefaultItemAnimator());
        fragHomeBinding.rvHotDealsBanner.setAdapter(largebannerAdapter);

    }
}

//TODO product remover favourite
private void callApiRemoveFavourite(HashMap<String, String> map, int position, String type) {
    if (isAPICalling) {
        return;
    }
    isAPICalling = true;
    Common.showLoadingProgress(requireActivity());
    Call<SingleResponse> call = ApiClient.getClient().setRemoveFromWishList(map);
    Log.e("remove-->", new Gson().toJson(map));
    call.enqueue(new Callback<SingleResponse>() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {
            if (response.code() == 200) {
                SingleResponse restResponse = response.body();
                if (restResponse != null && restResponse.getStatus() == 1) {
                    dismissLoadingProgress();
                    switch (type) {
                        case "1":
                            featuredProductsList.get(position).setIsWishlist(0);
                            featuredProductsAdapter.notifyItemChanged(position);
                            break;
                        case "2":
                            newProductsList.get(position).setIsWishlist(0);
                            newProductsAdaptor.notifyItemChanged(position);
                            break;
                        case "3":
                            hotdealsList.get(position).setIsWishlist(0);
                            hotdealsAdaptor.notifyItemChanged(position);
                            break;
                    }
                    onResume();
                } else if (restResponse != null && restResponse.getStatus() == 0) {
                    dismissLoadingProgress();
                    alertErrorOrValidationDialog(requireActivity(), restResponse.getMessage());
                }
            }
            isAPICalling = false;
        }

        @Override
        public void onFailure(Call<SingleResponse> call, Throwable t) {
            dismissLoadingProgress();
            alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.error_msg));
            isAPICalling = false;
        }
    });
}

//TODO product favourite
private void callApiFavourite(HashMap<String, String> map, int position, String type) {
    if (isAPICalling) {
        return;
    }
    isAPICalling = true;
    Common.showLoadingProgress(requireActivity());
    Call<SingleResponse> call = ApiClient.getClient().setAddToWishList(map);
    call.enqueue(new Callback<SingleResponse>() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {
            if (response.code() == 200) {
                SingleResponse restResponse = response.body();
                if (restResponse != null && restResponse.getStatus() == 1) {
                    dismissLoadingProgress();
                    switch (type) {
                        case "1":
                            featuredProductsList.get(position).setIsWishlist(1);
                            featuredProductsAdapter.notifyItemChanged(position);
                            break;
                        case "2":
                            newProductsList.get(position).setIsWishlist(1);
                            newProductsAdaptor.notifyItemChanged(position);
                            break;
                        case "3":
                            hotdealsList.get(position).setIsWishlist(1);
                            hotdealsAdaptor.notifyItemChanged(position);
                            break;
                    }
                    onResume();
                } else if (restResponse != null && restResponse.getStatus() == 0) {
                    dismissLoadingProgress();
                    alertErrorOrValidationDialog(requireActivity(), restResponse.getMessage());
                }
            }
            isAPICalling = false;
        }

        @Override
        public void onFailure(Call<SingleResponse> call, Throwable t) {
            dismissLoadingProgress();
            alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.error_msg));
            isAPICalling = false;
        }
    });
}

@Override
public void onResume() {
    super.onResume();
    getCurrentLanguage(requireActivity(), false);
    if (isAdded()) {
        if (isCheckNetwork(requireActivity())) {
            callApiBanner();
        } else {
            alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.no_internet));
        }
    }
}

}