package com.webmintinfotech.ecom.ui.payment;

import android.view.View;
import com.webmintinfotech.ecom.ui.activity.ActMain;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.databinding.ActPaymentSuccessFullBinding;

public class ActPaymentSuccessFull extends BaseActivity {
    private ActPaymentSuccessFullBinding actPaymentSuccessFullBinding;

    @Override
    public View setLayout() {
        return actPaymentSuccessFullBinding.getRoot();
    }

    @Override
    public void initView() {
        actPaymentSuccessFullBinding = ActPaymentSuccessFullBinding.inflate(getLayoutInflater());
        actPaymentSuccessFullBinding.btncontinueshopping.setOnClickListener(v -> openActivity(ActMain.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openActivity(ActMain.class);
    }
}