package com.webmintinfotech.ecom.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.base.BaseAdaptor;
import com.webmintinfotech.ecom.databinding.ActBrandBinding;
import com.webmintinfotech.ecom.databinding.RowBrandsBinding;
import com.webmintinfotech.ecom.model.BrandDataItem;
import com.webmintinfotech.ecom.model.BrandResponse;
import com.webmintinfotech.ecom.utils.Common;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;

public class ActBrand extends BaseActivity {
    private ActBrandBinding brandBinding;
    private ArrayList<BrandDataItem> brandDataList = new ArrayList<>();
    private BaseAdaptor<BrandDataItem, RowBrandsBinding> bannerAdapter = null;
    private GridLayoutManager gridLayoutManager;
    private int currentPage = 1;
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
        return brandBinding.getRoot();
    }

    @Override
    public void initView() {
        brandBinding = ActBrandBinding.inflate(getLayoutInflater());
        brandBinding.ivBack.setOnClickListener(view -> {
            finish();
            setResult(RESULT_OK);
        });
        gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        brandBinding.rvAllBrands.setLayoutManager(gridLayoutManager);
        loadBrandDataList(brandDataList);
        if (Common.isCheckNetwork(this)) {
            callApiBrand(String.valueOf(currentPage));
        } else {
            Common.alertErrorOrValidationDialog(
                    this,
                    getResources().getString(R.string.no_internet)
            );
        }
        brandBinding.rvAllBrands.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = gridLayoutManager.getChildCount();
                    totalItemCount = gridLayoutManager.getItemCount();
                    pastVisibleItems = gridLayoutManager.findFirstVisibleItemPosition();
                    if (currentPage < total_pages) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            currentPage += 1;
                            if (Common.isCheckNetwork(ActBrand.this)) {
                                callApiBrand(String.valueOf(currentPage));
                            } else {
                                Common.alertErrorOrValidationDialog(
                                        ActBrand.this,
                                        getResources().getString(R.string.no_internet)
                                );
                            }
                        }
                    }
                }
            }
        });
    }

    //TODO API BRAND CALL
    private void callApiBrand(String currentPage) {
        Common.showLoadingProgress(this);
        Call<BrandResponse> call = ApiClient.getClient().getBrands(currentPage);
        call.enqueue(new Callback<BrandResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<BrandResponse> call, Response<BrandResponse> response) {
                if (response.code() == 200) {
                    BrandResponse restResponse = response.body();
                    if (restResponse.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        if (restResponse.getVendors() != null && restResponse.getVendors().getData() != null) {
                            brandDataList.addAll(restResponse.getVendors().getData());
                        }
                        total_pages = Integer.parseInt(String.valueOf(restResponse.getVendors().getLastPage()));
                        bannerAdapter.notifyDataSetChanged();
                    } else if (restResponse.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(
                                ActBrand.this,
                                restResponse.getMessage()
                        );
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(
                            ActBrand.this,
                            getResources().getString(R.string.error_msg)
                    );
                }
            }

            @Override
            public void onFailure(Call<BrandResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(
                        ActBrand.this,
                        getResources().getString(R.string.error_msg)
                );
            }
        });
    }

    //TODO SET BRAND DATA
    private void loadBrandDataList(ArrayList<BrandDataItem> brandDataList) {
        bannerAdapter = new BaseAdaptor<BrandDataItem, RowBrandsBinding>(this, brandDataList) {
            @SuppressLint({"NewApi", "ResourceType"})
            @Override
            public void onBindData(RecyclerView.ViewHolder holder, BrandDataItem val, int position) {
                RowBrandsBinding binding = ((RowBrandsBindingHolder) holder).getBinding();
                binding.tvBrandsName.setText(brandDataList.get(position).getBrandName());
                binding.tvBrandsName.setVisibility(View.GONE);
                Glide.with(ActBrand.this).load(brandDataList.get(position).getImageUrl()).into(binding.ivBrands);
                binding.ivBrands.setBackgroundColor(Color.parseColor(colorArray[position % 6]));
                binding.clMain.setBackgroundColor(Color.parseColor(colorArray[position % 6]));
                binding.ivBrands.setBackgroundColor(Color.parseColor(colorArray[position % 6]));
                holder.itemView.setOnClickListener(view -> {
                    Log.e("brand_id--->", brandDataList.get(position).getId().toString());
                    Intent intent = new Intent(ActBrand.this, ActBrandDetails.class);
                    intent.putExtra("brand_id", brandDataList.get(position).getId().toString());
                    intent.putExtra("brand_name", brandDataList.get(position).getBrandName());
                    startActivity(intent);
                });
            }

            @Override
            public int setItemLayout() {
                return R.layout.row_brands;
            }

            @Override
            public RowBrandsBinding getBinding(ViewGroup parent) {
                return new RowBrandsBindingHolder(RowBrandsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false)).getBinding();
            }
        };
        brandBinding.rvAllBrands.setLayoutManager(gridLayoutManager);
        brandBinding.rvAllBrands.setItemAnimator(new DefaultItemAnimator());
        brandBinding.rvAllBrands.setAdapter(bannerAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        Common.getCurrentLanguage(this, false);
    }

    static class RowBrandsBindingHolder extends RecyclerView.ViewHolder {
        private RowBrandsBinding binding;

        public RowBrandsBindingHolder(RowBrandsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public RowBrandsBinding getBinding() {
            return binding;
        }
    }
}
