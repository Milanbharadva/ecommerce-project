package com.webmintinfotech.ecom.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import androidx.annotation.RequiresApi;
import com.bumptech.glide.Glide;
import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.databinding.ActTrackOrderBinding;
import com.webmintinfotech.ecom.model.TrackOrderResponse;
import com.webmintinfotech.ecom.ui.authentication.ActLogin;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.SharePreference;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActTrackOrder extends BaseActivity {
    private ActTrackOrderBinding trackOrderBinding;
    private TrackOrderResponse trackOrderDetailsList;
    private String currency = "";
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
        return trackOrderBinding.getRoot();
    }

    @Override
    public void initView() {
        trackOrderBinding = ActTrackOrderBinding.inflate(getLayoutInflater());
        setContentView(trackOrderBinding.getRoot());
        trackOrderBinding.ivBack.setOnClickListener(v -> {
            finish();
            setResult(RESULT_OK);
        });
        if (Common.isCheckNetwork(this)) {
            if (SharePreference.getBooleanPref(this, SharePreference.isLogin)) {
                callApiTrackDetail();
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
        currencyPosition =
                SharePreference.getStringPref(
                        this,
                        SharePreference.CurrencyPosition
                );
    }

    private void callApiTrackDetail() {
        Common.showLoadingProgress(this);
        HashMap<String, String> hasmap = new HashMap<>();
        hasmap.put("user_id", SharePreference.getStringPref(this, SharePreference.userId));
        hasmap.put("order_id", getIntent().getStringExtra("order_id"));
        Call<TrackOrderResponse> call = ApiClient.getClient().getTrackOrder(hasmap);
        call.enqueue(new Callback<TrackOrderResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<TrackOrderResponse> call, Response<TrackOrderResponse> response) {
                if (response.code() == 200) {
                    TrackOrderResponse restResponce = response.body();
                    if (restResponce.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        trackOrderDetailsList = (TrackOrderResponse) restResponce.getOrderInfo();
                        loadTrackOrderDetails(trackOrderDetailsList);
                    } else if (restResponce.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(
                                ActTrackOrder.this,
                                restResponce.getMessage()
                        );
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(
                            ActTrackOrder.this,
                            getResources().getString(R.string.error_msg)
                    );
                }
            }

            @Override
            public void onFailure(Call<TrackOrderResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(
                        ActTrackOrder.this,
                        getResources().getString(R.string.error_msg)
                );
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    private void loadTrackOrderDetails(TrackOrderResponse trackOrderDetailsList) {
        if (trackOrderDetailsList != null) {
            trackOrderBinding.clordertrack.setVisibility(View.VISIBLE);
            trackOrderBinding.ivOrderTrack.setVisibility(View.VISIBLE);
            trackOrderBinding.tvTrackOrderqty.setVisibility(View.VISIBLE);
            trackOrderBinding.tvordertrackname.setVisibility(View.VISIBLE);
            trackOrderBinding.tvOrderProductSize.setVisibility(View.VISIBLE);
            trackOrderBinding.tvcartitemprice.setVisibility(View.VISIBLE);
            trackOrderBinding.tvcartqtytitle.setVisibility(View.VISIBLE);
            trackOrderBinding.tvcartitemprice.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(trackOrderDetailsList.getImageUrl())
                    .into(trackOrderBinding.ivOrderTrack);
            trackOrderBinding.ivOrderTrack.setBackgroundColor(Color.parseColor(colorArray[pos % 6]));
            if (trackOrderDetailsList.getStatus() == 1) {
                trackOrderBinding.ivOrderPlaced.setImageResource(R.drawable.ic_green_round);
                trackOrderBinding.ivOrderConfirmed.setImageResource(R.drawable.ic_round);
                trackOrderBinding.ivOrderShipped.setImageResource(R.drawable.ic_round);
                trackOrderBinding.ivOrderDelivery.setImageResource(R.drawable.ic_round);
                trackOrderBinding.view.setBackgroundColor(getColor(R.color.medium_gray));
                trackOrderBinding.view1.setBackgroundColor(getColor(R.color.medium_gray));
                trackOrderBinding.view2.setBackgroundColor(getColor(R.color.medium_gray));
                trackOrderBinding.tvOrderPlacedDate.setVisibility(View.VISIBLE);
                trackOrderBinding.tvOrderPlacedDate.setText(
                        Common.getDateTime(trackOrderDetailsList.getCreatedAt())
                );
                trackOrderBinding.tvOrderConfirmedDate.setVisibility(View.GONE);
                trackOrderBinding.tvOrderShippedDate.setVisibility(View.GONE);
                trackOrderBinding.btnAddReview.setVisibility(View.GONE);
                trackOrderBinding.tvOrderDeliveryDate.setVisibility(View.GONE);
            } else if (trackOrderDetailsList.getStatus() == 2) {
                trackOrderBinding.ivOrderPlaced.setImageResource(R.drawable.ic_green_round);
                trackOrderBinding.ivOrderConfirmed.setImageResource(R.drawable.ic_green_round);
                trackOrderBinding.ivOrderShipped.setImageResource(R.drawable.ic_round);
                trackOrderBinding.ivOrderDelivery.setImageResource(R.drawable.ic_round);
                trackOrderBinding.view.setBackgroundColor(getColor(R.color.green));
                trackOrderBinding.view1.setBackgroundColor(getColor(R.color.medium_gray));
                trackOrderBinding.view2.setBackgroundColor(getColor(R.color.medium_gray));
                trackOrderBinding.tvOrderPlacedDate.setText(
                        Common.getDateTime(trackOrderDetailsList.getCreatedAt())
                );
                trackOrderBinding.tvOrderConfirmedDate.setText(
                        Common.getDateTime(trackOrderDetailsList.getConfirmedAt())
                );
                trackOrderBinding.tvOrderConfirmedDate.setVisibility(View.VISIBLE);
                trackOrderBinding.tvOrderShippedDate.setVisibility(View.GONE);
                trackOrderBinding.tvOrderDeliveryDate.setVisibility(View.GONE);
                trackOrderBinding.btnAddReview.setVisibility(View.GONE);
            } else if (trackOrderDetailsList.getStatus() == 3) {
                trackOrderBinding.ivOrderPlaced.setImageResource(R.drawable.ic_green_round);
                trackOrderBinding.ivOrderConfirmed.setImageResource(R.drawable.ic_green_round);
                trackOrderBinding.ivOrderShipped.setImageResource(R.drawable.ic_green_round);
                trackOrderBinding.ivOrderDelivery.setImageResource(R.drawable.ic_round);
                trackOrderBinding.view.setBackgroundColor(getColor(R.color.green));
                trackOrderBinding.view1.setBackgroundColor(getColor(R.color.green));
                trackOrderBinding.view2.setBackgroundColor(getColor(R.color.medium_gray));
                trackOrderBinding.tvOrderPlacedDate.setText(
                        Common.getDateTime(trackOrderDetailsList.getCreatedAt())
                );
                trackOrderBinding.tvOrderShippedDate.setText(
                        Common.getDateTime(trackOrderDetailsList.getShippedAt())
                );
                trackOrderBinding.tvOrderConfirmedDate.setText(
                        Common.getDateTime(trackOrderDetailsList.getConfirmedAt())
                );
                trackOrderBinding.tvOrderConfirmedDate.setVisibility(View.VISIBLE);
                trackOrderBinding.tvOrderShippedDate.setVisibility(View.VISIBLE);
                trackOrderBinding.tvOrderDeliveryDate.setVisibility(View.GONE);
                trackOrderBinding.btnAddReview.setVisibility(View.GONE);
            } else if (trackOrderDetailsList.getStatus() == 4 || trackOrderDetailsList.getStatus() == 7 ||
                    trackOrderDetailsList.getStatus() == 8 || trackOrderDetailsList.getStatus() == 9 || trackOrderDetailsList.getStatus() == 10) {
                trackOrderBinding.ivOrderPlaced.setImageResource(R.drawable.ic_green_round);
                trackOrderBinding.ivOrderConfirmed.setImageResource(R.drawable.ic_green_round);
                trackOrderBinding.ivOrderShipped.setImageResource(R.drawable.ic_green_round);
                trackOrderBinding.ivOrderDelivery.setImageResource(R.drawable.ic_green_round);
                trackOrderBinding.view.setBackgroundColor(getColor(R.color.green));
                trackOrderBinding.view1.setBackgroundColor(getColor(R.color.green));
                trackOrderBinding.view2.setBackgroundColor(getColor(R.color.green));
                trackOrderBinding.tvOrderPlacedDate.setText(
                        Common.getDateTime(trackOrderDetailsList.getCreatedAt())
                );
                trackOrderBinding.tvOrderShippedDate.setText(
                        Common.getDateTime(trackOrderDetailsList.getShippedAt())
                );
                trackOrderBinding.tvOrderConfirmedDate.setText(
                        Common.getDateTime(trackOrderDetailsList.getConfirmedAt())
                );
                trackOrderBinding.tvOrderDeliveryDate.setText(
                        Common.getDateTime(trackOrderDetailsList.getDeliveredAt())
                );
                trackOrderBinding.tvOrderConfirmedDate.setVisibility(View.VISIBLE);
                trackOrderBinding.tvOrderShippedDate.setVisibility(View.VISIBLE);
                trackOrderBinding.tvOrderDeliveryDate.setVisibility(View.VISIBLE);
                trackOrderBinding.btnAddReview.setVisibility(View.VISIBLE);
                trackOrderBinding.btnAddReview.setOnClickListener(v -> {
                    Intent intent = new Intent(this, ActWriteReview.class);
                    intent.putExtra("proName", trackOrderDetailsList.getProductName());
                    intent.putExtra("proID", trackOrderDetailsList.getProductId());
                    intent.putExtra("vendorsID", trackOrderDetailsList.getVendorId());
                    intent.putExtra("proImage", trackOrderDetailsList.getImageUrl());
                    startActivity(intent);
                });
            }
            trackOrderBinding.tvTrackOrderqty.setText(String.valueOf(trackOrderDetailsList.getQty()));
            trackOrderBinding.tvordertrackname.setText(trackOrderDetailsList.getProductName());
            Log.d("Size", String.valueOf(trackOrderDetailsList.getVariation()));
            trackOrderBinding.tvOrderProductSize.setText(
                    getIntent().getStringExtra("Size")
            );
            double qty = Double.parseDouble(trackOrderDetailsList.getQty());
            double price = Double.parseDouble(trackOrderDetailsList.getPrice());
            double totalpriceqty = price * qty;
            Log.d("totalpriceqty", String.valueOf(totalpriceqty));
            Log.d("totalpriceqty", String.valueOf(qty));
            Log.d("totalpriceqty", String.valueOf(price));
            trackOrderBinding.tvcartitemprice.setText(
                    Common.getPrice(
                            currencyPosition, currency,
                            String.valueOf(totalpriceqty)
                    )
            );
        } else {
            trackOrderBinding.clordertrack.setVisibility(View.GONE);
            trackOrderBinding.ivOrderTrack.setVisibility(View.GONE);
            trackOrderBinding.tvTrackOrderqty.setVisibility(View.GONE);
            trackOrderBinding.tvordertrackname.setVisibility(View.GONE);
            trackOrderBinding.tvOrderProductSize.setVisibility(View.GONE);
            trackOrderBinding.tvcartitemprice.setVisibility(View.GONE);
            trackOrderBinding.tvcartqtytitle.setVisibility(View.GONE);
            trackOrderBinding.btnAddReview.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        callApiTrackDetail();
    }
}
