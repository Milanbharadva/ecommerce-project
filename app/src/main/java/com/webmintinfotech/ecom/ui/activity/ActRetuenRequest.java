package com.webmintinfotech.ecom.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.bumptech.glide.Glide;
import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.api.SingleResponse;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.base.BaseAdaptor;
import com.webmintinfotech.ecom.databinding.ActRetuenRequestBinding;
import com.webmintinfotech.ecom.databinding.RowReturnconditionsBinding;
import com.webmintinfotech.ecom.model.OrderRetuenRequestDataItem;
import com.webmintinfotech.ecom.model.OrderRetuenRequestOrderInfo;
import com.webmintinfotech.ecom.model.OrderRetuenRequestResponse;
import com.webmintinfotech.ecom.ui.authentication.ActLogin;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.SharePreference;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActRetuenRequest extends BaseActivity {
    private ActRetuenRequestBinding retuenRequestBinding;
    private OrderRetuenRequestOrderInfo orderReturnrequestDetailsList;
    private String currency = "";
    private String currencyPosition = "";
    private String retuencon = "";
    private int pos = 0;
    private final String[] colorArray = {
            "#FDF7FF",
            "#FDF3F0",
            "#EDF7FD",
            "#FFFAEA",
            "#F1FFF6",
            "#FFF5EC"
    };

    @Override
    public View setLayout() {
        return retuenRequestBinding.getRoot();
    }

    @Override
    public void initView() {
        retuenRequestBinding = ActRetuenRequestBinding.inflate(getLayoutInflater());
        retuenRequestBinding.ivBack.setOnClickListener(v -> {
            finish();
            setResult(RESULT_OK);
        });
        if (Common.isCheckNetwork(this)) {
            if (SharePreference.getBooleanPref(this, SharePreference.isLogin)) {
                callApiOrderReturnRequestDetail();
            } else {
                openActivity(ActLogin.class);
                this.finish();
            }
        } else {
            Common.alertErrorOrValidationDialog(
                    this,
                    getResources().getString(R.string.no_internet)
            );
        }
        currency = SharePreference.getStringPref(this, SharePreference.Currency);
        currencyPosition = SharePreference.getStringPref(
                this,
                SharePreference.CurrencyPosition
        );

        retuenRequestBinding.btnreturnrequest.setOnClickListener(v -> {
            if (SharePreference.getBooleanPref(this, SharePreference.isLogin)) {
                callApireturnRequest();
            } else {
                openActivity(ActLogin.class);
                this.finish();
            }
        });
    }

    private void callApireturnRequest() {
        if (retuencon.equals("")) {
            Common.showErrorFullMsg(
                    this,
                    getResources().getString(R.string.return_conditions_selection_error)
            );
        } else {
            Common.showLoadingProgress(this);
            HashMap<String, String> hasmap = new HashMap<>();
            hasmap.put("user_id",
                    SharePreference.getStringPref(this, SharePreference.userId)
            );
            hasmap.put("order_id", getIntent().getStringExtra("order_id"));
            hasmap.put("return_reason", retuencon);
            hasmap.put("comment", retuenRequestBinding.edtReturnComments.getText().toString());
            hasmap.put("status", "7");
            Call<SingleResponse> call = ApiClient.getClient().returnRequest(hasmap);
            call.enqueue(new Callback<SingleResponse>() {
                @Override
                public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {
                    if (response.code() == 200) {
                        Common.dismissLoadingProgress();
                        Common.isAddOrUpdated=true;
                        if (response.body().getStatus() == 1) {
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Common.showErrorFullMsg(
                                    ActRetuenRequest.this,
                                    response.body().getMessage()
                            );
                        }
                    } else {
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(
                                ActRetuenRequest.this,
                                getResources().getString(R.string.error_msg)
                        );
                    }
                }

                @Override
                public void onFailure(Call<SingleResponse> call, Throwable t) {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(
                            ActRetuenRequest.this,
                            getResources().getString(R.string.error_msg)
                    );
                }
            });
        }
    }

    private void callApiOrderReturnRequestDetail() {
        Common.showLoadingProgress(this);
        HashMap<String, String> hasmap = new HashMap<>();
        hasmap.put("user_id",
                SharePreference.getStringPref(this, SharePreference.userId)
        );
        hasmap.put("order_id", getIntent().getStringExtra("order_id"));
        Call<OrderRetuenRequestResponse> call = ApiClient.getClient().getOrderReturnRequest(hasmap);
        call.enqueue(new Callback<OrderRetuenRequestResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<OrderRetuenRequestResponse> call, Response<OrderRetuenRequestResponse> response) {
                if (response.code() == 200) {
                    OrderRetuenRequestResponse restResponce = response.body();
                    if (restResponce.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        orderReturnrequestDetailsList = restResponce.getOrderInfo();
                        loadOrderReturnRequestDetails(orderReturnrequestDetailsList);
                        if (restResponce.getData() != null) {
                            loadReturnConditions(restResponce.getData());
                        }
                    } else if (restResponce.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(
                                ActRetuenRequest.this,
                                restResponce.getMessage()
                        );
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(
                            ActRetuenRequest.this,
                            getResources().getString(R.string.error_msg)
                    );
                }
            }

            @Override
            public void onFailure(Call<OrderRetuenRequestResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(
                        ActRetuenRequest.this,
                        getResources().getString(R.string.error_msg)
                );
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void loadOrderReturnRequestDetails(OrderRetuenRequestOrderInfo orderReturnrequestDetailsList) {
        Glide.with(this)
                .load(orderReturnrequestDetailsList.getImageUrl())
                .into(retuenRequestBinding.ivCartitemm);
        retuenRequestBinding.ivCartitemm.setBackgroundColor(Color.parseColor(colorArray[pos % 6]));
        retuenRequestBinding.tvcartitemqty.setText(getString(R.string.qty_) + orderReturnrequestDetailsList.getQty());
        retuenRequestBinding.tvcateitemname.setText(orderReturnrequestDetailsList.getProductName());
        if (orderReturnrequestDetailsList.getVariation().isEmpty()) {
            retuenRequestBinding.tvcartitemsize.setText("-");
        } else {
            retuenRequestBinding.tvcartitemsize.setText(
                    getIntent().getStringExtra("att") + ": " + orderReturnrequestDetailsList.getVariation()
            );
        }
        retuenRequestBinding.tvcartitemprice.setText(
                Common.getPrice(
                        currencyPosition, currency,
                        orderReturnrequestDetailsList.getPrice().toString()
                )
        );
    }

    private void loadReturnConditions(ArrayList<OrderRetuenRequestDataItem> orderReturnList) {
        BaseAdaptor<OrderRetuenRequestDataItem, ViewBinding> returnAdapter = new BaseAdaptor<>(
                this,
                orderReturnList
        ) {
            private RowReturnconditionsBinding binding;

            @SuppressLint({"NewApi", "ResourceType"})
            @Override
            public void onBindData(RecyclerView.ViewHolder holder, OrderRetuenRequestDataItem val, int position) {
                binding.tvreturncon.setText(orderReturnList.get(position).getReturnConditions());
                holder.itemView.setOnClickListener(v -> {
                    for (OrderRetuenRequestDataItem item : orderReturnList) {
                        item.setIsSelect(false);
                    }
                    retuencon = orderReturnList.get(position).getReturnConditions();
                    orderReturnList.get(position).setIsSelect(true);
                    new Handler(Looper.getMainLooper()).postDelayed(() -> loadReturnConditions(orderReturnList), 10L);
                });
                if (orderReturnList.get(position).getIsSelect()) {
                    binding.ivCheck.setVisibility(View.VISIBLE);
                } else {
                    binding.ivCheck.setVisibility(View.GONE);
                }
            }

            @Override
            public int setItemLayout() {
                return R.layout.row_returnconditions;
            }

            @Override
            public RowReturnconditionsBinding getBinding(ViewGroup parent) {
                return RowReturnconditionsBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                );
            }
        };
        retuenRequestBinding.rvreturntext.setLayoutManager(
                new LinearLayoutManager(ActRetuenRequest.this, LinearLayoutManager.VERTICAL, false)
        );
        retuenRequestBinding.rvreturntext.setItemAnimator(new DefaultItemAnimator());
        retuenRequestBinding.rvreturntext.setAdapter(returnAdapter);
    }
}
