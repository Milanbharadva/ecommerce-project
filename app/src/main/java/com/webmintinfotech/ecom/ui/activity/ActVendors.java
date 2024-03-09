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
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.base.BaseAdaptor;
import com.webmintinfotech.ecom.databinding.ActVendorsBinding;
import com.webmintinfotech.ecom.databinding.RowVendorsdetailsBinding;
import com.webmintinfotech.ecom.model.VenDorsDataItem;
import com.webmintinfotech.ecom.model.VendorsResponse;
import com.webmintinfotech.ecom.utils.Common;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActVendors extends BaseActivity {
    private ActVendorsBinding vendorsBinding;
    private ArrayList<VenDorsDataItem> vendorsDataList = new ArrayList<>();
    private BaseAdaptor<VenDorsDataItem, RowVendorsdetailsBinding> vendorsAdapter;
    private LinearLayoutManager layoutLinearLayoutManager;
    private int currentPage = 1;
    private int total_pages = 0;
    private int visibleItemCount = 0;
    private int totalItemCount = 0;
    private int pastVisibleItems = 0;
    private String avgRatting = "0";
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
        return vendorsBinding.getRoot();
    }

    @Override
    public void initView() {
        vendorsBinding = ActVendorsBinding.inflate(LayoutInflater.from(this));
        layoutLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        vendorsBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                setResult(RESULT_OK);
            }
        });
        vendorsDataList(vendorsDataList);
        if (Common.isCheckNetwork(this)) {
            callApiVendors(String.valueOf(currentPage));
        } else {
            Common.alertErrorOrValidationDialog(this, getResources().getString(R.string.no_internet));
        }
        vendorsBinding.rvAllVendors.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = layoutLinearLayoutManager.getChildCount();
                    totalItemCount = layoutLinearLayoutManager.getItemCount();
                    pastVisibleItems = layoutLinearLayoutManager.findFirstVisibleItemPosition();
                    if (currentPage < total_pages) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            currentPage += 1;
                            callApiVendors(String.valueOf(currentPage));
                        }
                    }
                }
            }
        });
    }

    //TODO CALL VENDORS API
    private void callApiVendors(String currentPage) {
        Common.showLoadingProgress(this);
        Call<VendorsResponse> call = ApiClient.getClient().getVendors(currentPage);
        call.enqueue(new Callback<VendorsResponse>() {
            @Override
            public void onResponse(Call<VendorsResponse> call, Response<VendorsResponse> response) {
                if (response.code() == 200) {
                    VendorsResponse restResponce = response.body();
                    Common.dismissLoadingProgress();
                    if (restResponce.getStatus() == 1) {
                        if (currentPage.equals("1")) {
                            vendorsDataList.clear();
                        }
                        vendorsDataList.addAll(restResponce.getVendors().getData());
                        ActVendors.this.currentPage = restResponce.getVendors().getCurrentPage();
                        ActVendors.this.total_pages = restResponce.getVendors().getLastPage();
                        vendorsDataList(vendorsDataList);
                    } else if (restResponce.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(ActVendors.this, restResponce.getMessage());
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(ActVendors.this, getResources().getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(Call<VendorsResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActVendors.this, getResources().getString(R.string.error_msg));
            }
        });
    }

    //TODO VENDORS DATA SET
    private void vendorsDataList(ArrayList<VenDorsDataItem> vendorsDataList) {
        vendorsAdapter = new BaseAdaptor<VenDorsDataItem, RowVendorsdetailsBinding>(this, vendorsDataList) {
            @Override
            public void onBindData(RecyclerView.ViewHolder holder, VenDorsDataItem val, int position) {
                RowVendorsdetailsBinding binding = ((RowVendorsdetailsBindingHolder) holder).binding;
                binding.tvVendorsName.setText(vendorsDataList.get(position).getName());
                Glide.with(ActVendors.this).load(vendorsDataList.get(position).getImageUrl())
                        .into(binding.ivvendors);
                binding.ivvendors.setBackgroundColor(Color.parseColor(colorArray[position % 6]));

                if (vendorsDataList.get(position).getRattings() == null) {
                    binding.tvRatePro.setText("0.0");
                } else {
                    if (vendorsDataList.get(position).getRattings().size() > 0) {
                        binding.tvRatePro.setText(vendorsDataList.get(position).getRattings().get(0).getAvgRatting());
                        avgRatting = vendorsDataList.get(position).getRattings().get(0).getAvgRatting();
                    } else {
                        binding.tvRatePro.setText("0.0");
                        avgRatting = "0.0";
                    }
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("vendor_id--->", vendorsDataList.get(position).getId().toString());
                        Intent intent = new Intent(ActVendors.this, ActVendorsDetails.class);
                        intent.putExtra("vendor_id", vendorsDataList.get(position).getId().toString());
                        intent.putExtra("vendors_name", vendorsDataList.get(position).getName().toString());
                        intent.putExtra("vendors_iv", vendorsDataList.get(position).getImageUrl().toString());
                        intent.putExtra("vendors_rate", avgRatting);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public int setItemLayout() {
                return R.layout.row_vendorsdetails;
            }

            @Override
            public RowVendorsdetailsBinding getBinding(ViewGroup parent) {
                return null;
            }

            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                RowVendorsdetailsBinding binding = RowVendorsdetailsBinding.inflate(layoutInflater, parent, false);
                return new RowVendorsdetailsBindingHolder(binding);
            }
        };
        if (vendorsDataList.size() > 0) {
            vendorsBinding.rvAllVendors.setVisibility(View.VISIBLE);
            vendorsBinding.tvNoDataFound.setVisibility(View.GONE);
            vendorsBinding.rvAllVendors.setLayoutManager(layoutLinearLayoutManager);
            vendorsBinding.rvAllVendors.setItemAnimator(new DefaultItemAnimator());
            vendorsBinding.rvAllVendors.setAdapter(vendorsAdapter);
        } else {
            vendorsBinding.rvAllVendors.setVisibility(View.GONE);
            vendorsBinding.tvNoDataFound.setVisibility(View.VISIBLE);
        }
    }

    private static class RowVendorsdetailsBindingHolder extends RecyclerView.ViewHolder {
        private RowVendorsdetailsBinding binding;

        RowVendorsdetailsBindingHolder(RowVendorsdetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
