package com.webmintinfotech.ecom.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.base.BaseAdaptor;
import com.webmintinfotech.ecom.databinding.ActOffersBinding;
import com.webmintinfotech.ecom.databinding.RowOffersBinding;
import com.webmintinfotech.ecom.model.CouponDataItem;
import com.webmintinfotech.ecom.model.GetCouponResponse;
import com.webmintinfotech.ecom.ui.authentication.ActLogin;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.SharePreference;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActOffers extends BaseActivity {

    private ActOffersBinding offersBinding;
    private ArrayList<CouponDataItem> couponList = new ArrayList<>();
    private BaseAdapter couponAllDataAdapter;
    private LinearLayoutManager manager;
    private int currentPage = 1;
    private int total_pages = 0;
    private int visibleItemCount = 0;
    private int totalItemCount = 0;
    private int pastVisibleItems = 0;
    private int pos = 0;
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
        return offersBinding.getRoot();
    }

    @Override
    public void initView() {
        offersBinding = ActOffersBinding.inflate(LayoutInflater.from(this));
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        offersBinding.ivBack.setOnClickListener(view -> finish());

        offersBinding.rvOffers.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = manager.getChildCount();
                    totalItemCount = manager.getItemCount();
                    pastVisibleItems = manager.findFirstVisibleItemPosition();
                    if (currentPage < total_pages) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            currentPage += 1;
                            callApiOffers();
                        }
                    }
                }
            }
        });
    }

    private void callApiOffers() {
        Common.showLoadingProgress(this);
        Call<GetCouponResponse> call = ApiClient.getClient().getCoupon(String.valueOf(currentPage));
        call.enqueue(new Callback<GetCouponResponse>() {
            @Override
            public void onResponse(Call<GetCouponResponse> call, Response<GetCouponResponse> response) {
                if (response.code() == 200) {
                    GetCouponResponse restResponce = response.body();
                    if (restResponce.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        if ((restResponce.getData().getData() != null ? restResponce.getData().getData().size() : 0) > 0) {
                            offersBinding.rvOffers.setVisibility(View.VISIBLE);
                            offersBinding.tvNoDataFound.setVisibility(View.GONE);
                            currentPage = Integer.parseInt(String.valueOf(restResponce.getData().getCurrentPage()));
                            total_pages = restResponce.getData().getLastPage() != null ? Integer.parseInt(String.valueOf(restResponce.getData().getLastPage())) : 0;
                            couponList.addAll(restResponce.getData().getData());
                        } else {
                            offersBinding.rvOffers.setVisibility(View.GONE);
                            offersBinding.tvNoDataFound.setVisibility(View.VISIBLE);
                        }
                        couponAllDataAdapter.notifyDataSetChanged();
                    } else if (restResponce.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(ActOffers.this, restResponce.getMessage());
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(ActOffers.this, getResources().getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(Call<GetCouponResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActOffers.this, getResources().getString(R.string.error_msg));
            }
        });
    }

    private void loadCouponDetails(ArrayList<CouponDataItem> couponList) {
//        couponAllDataAdapter = new BaseAdaptor<CouponDataItem, RowOffersBinding>(this, couponList) {
//            @Override
//            public void onBindData(BaseAdapter.BaseViewHolder holder, CouponDataItem val, int position) {
//                RowOffersBinding binding = ((RowOffersViewHolder) holder).binding;
//                binding.cloffers.setBackgroundColor(Color.parseColor(colorArray[position % 6]));
//                binding.tvCouponDate.setText(Common.getDate(couponList.get(position).getEndDate()));
//                binding.btnCouponName.setText(couponList.get(position).getCouponName());
//                binding.tvCouponTitle.setText(couponList.get(position).getDescription());
//                binding.tvMinimumAmount.setText("Minimum amount for this offer " +
//                        SharePreference.getStringPref(ActOffers.this, SharePreference.Currency) + couponList.get(position).getMin_amount());
//            }
//
//            @Override
//            public BaseAdapter.BaseViewHolder onCreateHolder(ViewGroup parent, int viewType) {
//                RowOffersBinding binding = RowOffersBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
//                return new RowOffersViewHolder(binding);
//            }
//        };
//
//        offersBinding.rvOffers.setLayoutManager(manager);
//        offersBinding.rvOffers.setItemAnimator(new DefaultItemAnimator());
//        offersBinding.rvOffers.setAdapter(couponAllDataAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentPage = 1;
        couponList.clear();
        loadCouponDetails(couponList);
        if (Common.isCheckNetwork(this)) {
            if (SharePreference.getBooleanPref(this, SharePreference.isLogin)) {
                callApiOffers();
            } else {
                openActivity(ActLogin.class);
                this.finish();
            }
        } else {
            Common.alertErrorOrValidationDialog(this, getResources().getString(R.string.no_internet));
        }
    }

//    private static class RowOffersViewHolder extends BaseAdaptor.BaseViewHolder<RowOffersBinding> {
//        public RowOffersViewHolder(RowOffersBinding binding) {
//            super(binding);
//        }
//    }
}
