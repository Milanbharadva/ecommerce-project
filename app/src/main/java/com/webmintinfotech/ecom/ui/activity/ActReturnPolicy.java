package com.webmintinfotech.ecom.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.databinding.ActReturnPolicyBinding;

public class ActReturnPolicy extends BaseActivity {
    private ActReturnPolicyBinding actReturnPolicyBinding;

    @Override
    public View setLayout() {
        return actReturnPolicyBinding.getRoot();
    }

    @Override
    public void initView() {
        actReturnPolicyBinding = ActReturnPolicyBinding.inflate(getLayoutInflater());
        //TODO PRODUCT RETURN POLICIES TEXT SET
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String description = extras.getString("return_policies");
            actReturnPolicyBinding.tvreturnpoliciesDescription.setText(description);
        }
        actReturnPolicyBinding.ivBack.setOnClickListener(v -> finish());
    }
}
