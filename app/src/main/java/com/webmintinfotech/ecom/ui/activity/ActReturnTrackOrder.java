package com.webmintinfotech.ecom.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.databinding.ActReturnTrackOrderBinding;
import com.webmintinfotech.ecom.model.TrackOrderInfo;
import com.webmintinfotech.ecom.model.TrackOrderResponse;
import com.webmintinfotech.ecom.ui.authentication.ActLogin;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.SharePreference;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActReturnTrackOrder extends BaseActivity {
    private ActReturnTrackOrderBinding returntrackOrderBinding;
    private TrackOrderInfo trackOrderDetailsList;
    private String currency = "";
    private int productprice = 0;
    private String currencyPosition = "";
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
        return returntrackOrderBinding.getRoot();
    }

    @Override
    public void initView() {
        returntrackOrderBinding = ActReturnTrackOrderBinding.inflate(getLayoutInflater());
        returntrackOrderBinding.ivBack.setOnClickListener(v -> {
            finish();
            setResult(RESULT_OK);
        });

        currency = SharePreference.getStringPref(this, SharePreference.Currency);
        currencyPosition = SharePreference.getStringPref(this, SharePreference.CurrencyPosition);
    }

    //TODO API ORDER RETURN TRACK CALL
    private void callApiReturnTrackDetail() {
        Common.showLoadingProgress(this);
        HashMap<String, String> hasmap = new HashMap<>();
        hasmap.put("user_id", SharePreference.getStringPref(this, SharePreference.userId));
        hasmap.put("order_id", getIntent().getStringExtra("orderId"));
        Call<TrackOrderResponse> call = ApiClient.getClient().getTrackOrder(hasmap);
        call.enqueue(new Callback<TrackOrderResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<TrackOrderResponse> call, Response<TrackOrderResponse> response) {
                if (response.code() == 200) {
                    TrackOrderResponse restResponce = response.body();
                    if (restResponce.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        trackOrderDetailsList = restResponce.getOrderInfo();
                        loadTrackOrderDetails(trackOrderDetailsList);
                    } else if (restResponce.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(ActReturnTrackOrder.this, restResponce.getMessage().toString());
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(ActReturnTrackOrder.this, getResources().getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(Call<TrackOrderResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActReturnTrackOrder.this, getResources().getString(R.string.error_msg));
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void loadTrackOrderDetails(TrackOrderInfo trackOrderDetailsList) {
        // Your code for loading track order details goes here
        // Make sure to update UI accordingly
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Common.isCheckNetwork(this)) {
            if (SharePreference.getBooleanPref(this, SharePreference.isLogin)) {
                callApiReturnTrackDetail();
            } else {
                openActivity(ActLogin.class);
                this.finish();
            }
        } else {
            Common.alertErrorOrValidationDialog(this, getResources().getString(R.string.no_internet));
        }
    }
}
