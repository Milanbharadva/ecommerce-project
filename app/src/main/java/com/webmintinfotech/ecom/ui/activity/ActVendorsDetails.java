package com.webmintinfotech.ecom.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.adapter.VendorsDetailsAdapter;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.base.BaseAdaptor;
import com.webmintinfotech.ecom.databinding.ActVendorsDetailsBinding;
import com.webmintinfotech.ecom.databinding.RowStoreBannerBinding;
import com.webmintinfotech.ecom.model.TopbannerItem;
import com.webmintinfotech.ecom.model.VendorsDetailsDataItem;
import com.webmintinfotech.ecom.model.VendorsDetailsResponse;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.SharePreference;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActVendorsDetails extends BaseActivity {
    private ActVendorsDetailsBinding vendorsdetailsBinding;
    private ArrayList<TopbannerItem> bannerList = new ArrayList<>();
    private String currency = "";
    private String currencyPosition = "";
    private ArrayList<VendorsDetailsDataItem> vendorsdetailsDataList = new ArrayList<>();
    private VendorsDetailsAdapter vendorsDataAdapter;
    private LinearLayoutManager linearLayoutManager;
    private int currentPage = 1;
    private int total_pages = 0;
    private int visibleItemCount = 0;
    private int totalItemCount = 0;
    private int pastVisibleItems = 0;
    private String image = "";
    private String rate = "";
    private String vendorsName = "";
    private String[] colorArray = {
            "#FDF7FF",
            "#FDF3F0",
            "#EDF7FD",
            "#FFFAEA",
            "#F1FFF6",
            "#FFF5EC"
    };
    private String vendorsId = "";

    @Override
    public View setLayout() {
        return vendorsdetailsBinding.getRoot();
    }

    @Override
    public void initView() {
        vendorsdetailsBinding = ActVendorsDetailsBinding.inflate(LayoutInflater.from(this));
        currency = SharePreference.getStringPref(this, SharePreference.Currency);
        currencyPosition = SharePreference.getStringPref(this, SharePreference.CurrencyPosition);
        vendorsdetailsBinding.ivAboutus.setVisibility(View.GONE);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        vendorsdetailsBinding.ivBack.setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
        });
        vendorsId = getIntent().getStringExtra("vendor_id");
        vendorsdetailsBinding.tvtitle.setText(getIntent().getStringExtra("vendors_name"));
        if (Common.isCheckNetwork(this)) {
            image = getIntent().getStringExtra("vendors_iv");
            rate = getIntent().getStringExtra("vendors_rate") != null ? getIntent().getStringExtra("vendors_rate") : "0.0";
            vendorsName = getIntent().getStringExtra("vendors_name");
        }

        vendorsdetailsBinding.rvVendorsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();
                    if (currentPage < total_pages) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            currentPage += 1;
                            if (Common.isCheckNetwork(ActVendorsDetails.this)) {
                                callApiVendorsDetail(vendorsId);
                            } else {
                                Common.alertErrorOrValidationDialog(ActVendorsDetails.this, getResources().getString(R.string.no_internet));
                            }
                        }
                    }
                }
            }
        });
    }

    //TODO first banner
    private void loadPagerImagesSliders(ArrayList<TopbannerItem> slidersList) {
        BaseAdaptor<TopbannerItem, RowStoreBannerBinding> bannerAdapter = new BaseAdaptor<TopbannerItem, RowStoreBannerBinding>(ActVendorsDetails.this, slidersList) {
            @Override
            public void onBindData(RecyclerView.ViewHolder holder, TopbannerItem val, int position) {
                RowStoreBannerBinding binding = ((RowStoreBannerBindingHolder) holder).binding;
                Glide.with(ActVendorsDetails.this).load(slidersList.get(position).getImageUrl())
                        .into(binding.ivBanner);
                binding.ivBanner.setBackgroundColor(Color.parseColor(colorArray[position % 6]));
            }

            @Override
            public int setItemLayout() {
                return R.layout.row_banner;
            }

            @Override
            public RowStoreBannerBinding getBinding(ViewGroup parent) {
                return null;
            }


            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                RowStoreBannerBinding binding = RowStoreBannerBinding.inflate(layoutInflater, parent, false);
                return new RowStoreBannerBindingHolder(binding);
            }
        };
        if (bannerList.size() > 0) {
            vendorsdetailsBinding.rvBanner.setVisibility(View.VISIBLE);
            vendorsdetailsBinding.rvBanner.setLayoutManager(new LinearLayoutManager(ActVendorsDetails.this, LinearLayoutManager.HORIZONTAL, false));
            vendorsdetailsBinding.rvBanner.setItemAnimator(new DefaultItemAnimator());
            vendorsdetailsBinding.rvBanner.setAdapter(bannerAdapter);
            vendorsdetailsBinding.rvBanner.setNestedScrollingEnabled(true);
        } else {
            vendorsdetailsBinding.rvBanner.setVisibility(View.GONE);
        }
    }

    //TODO CALL VENDORES DETAILS API
    private void callApiVendorsDetail(String vendorsId) {
        Common.showLoadingProgress(ActVendorsDetails.this);
        HashMap<String, String> hasmap = new HashMap<>();
        hasmap.put("user_id", SharePreference.getStringPref(ActVendorsDetails.this, SharePreference.userId));
        hasmap.put("vendor_id", vendorsId);
        Call<VendorsDetailsResponse> call = ApiClient.getClient().getVendorsDetails(Integer.toString(currentPage), hasmap);
        call.enqueue(new Callback<VendorsDetailsResponse>() {
            @Override
            public void onResponse(Call<VendorsDetailsResponse> call, Response<VendorsDetailsResponse> response) {
                if (response.code() == 200) {
                    VendorsDetailsResponse restResponce = response.body();
                    if (restResponce.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        rate = restResponce.getVendordetails().getRattings().size() == 0 ? restResponce.getVendordetails().getRattings().get(0).getAvgRatting().toString() : " 0.0";
                        bannerList.addAll(restResponce.getBannerList());
                        loadPagerImagesSliders(bannerList);

                        if (restResponce.getData().getData().size() > 0) {
                            vendorsdetailsBinding.rvVendorsList.setVisibility(View.VISIBLE);
                            vendorsdetailsBinding.tvNoDataFound.setVisibility(View.GONE);
                            currentPage = restResponce.getData().getCurrentPage();
                            total_pages = restResponce.getData().getLastPage();
                            vendorsdetailsDataList.addAll(restResponce.getData().getData());
                        } else {
                            vendorsdetailsBinding.rvVendorsList.setVisibility(View.GONE);
                            vendorsdetailsBinding.tvNoDataFound.setVisibility(View.VISIBLE);
                        }
                        vendorsDataAdapter.notifyDataSetChanged();
                        vendorsdetailsBinding.ivAboutus.setVisibility(View.VISIBLE);
                        vendorsdetailsBinding.ivAboutus.setOnClickListener(v -> {
                            Intent intent = new Intent(ActVendorsDetails.this, ActStoreInfo.class);
                            intent.putExtra("mobile", restResponce.getVendordetails().getMobile());
                            intent.putExtra("email", restResponce.getVendordetails().getEmail());
                            intent.putExtra("image", image);
                            intent.putExtra("rate", rate);
                            intent.putExtra("vendorsName", vendorsName);
                            intent.putExtra("storeaddress", restResponce.getVendordetails().getStoreAddress());
                            startActivity(intent);
                        });
                    } else if (restResponce.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(ActVendorsDetails.this, restResponce.getMessage());
                        vendorsdetailsBinding.ivAboutus.setVisibility(View.GONE);
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(ActVendorsDetails.this, getResources().getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(Call<VendorsDetailsResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActVendorsDetails.this, getResources().getString(R.string.error_msg));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        vendorsdetailsDataList.clear();
        currentPage = 1;
        loadVendorsDetails(vendorsdetailsDataList);
        callApiVendorsDetail(vendorsId);
    }

    private void loadVendorsDetails(ArrayList<VendorsDetailsDataItem> vendorsdetailsDataList) {
        vendorsDataAdapter = new VendorsDetailsAdapter(ActVendorsDetails.this, vendorsdetailsDataList);
        vendorsdetailsBinding.rvVendorsList.setLayoutManager(linearLayoutManager);
        vendorsdetailsBinding.rvVendorsList.setAdapter(vendorsDataAdapter);
    }

    private static class RowStoreBannerBindingHolder extends RecyclerView.ViewHolder {
        private RowStoreBannerBinding binding;

        RowStoreBannerBindingHolder(RowStoreBannerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
