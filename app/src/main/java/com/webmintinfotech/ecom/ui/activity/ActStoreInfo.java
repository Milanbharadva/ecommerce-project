package com.webmintinfotech.ecom.ui.activity;

import android.view.View;

import com.bumptech.glide.Glide;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.databinding.ActStoreInfoBinding;

public class ActStoreInfo extends BaseActivity {

    private ActStoreInfoBinding actStoreInfoBinding;

    @Override
    public View setLayout() {
        return actStoreInfoBinding.getRoot();
    }

    @Override
    public void initView() {
        actStoreInfoBinding = ActStoreInfoBinding.inflate(getLayoutInflater());

        actStoreInfoBinding.ivClose.setOnClickListener(view -> {
            finish();
            setResult(RESULT_OK);
        });

        // TODO: SET VENDOR'S STORE INFO
        actStoreInfoBinding.tvstorephone.setText(getIntent().getStringExtra("mobile") != null ? getIntent().getStringExtra("mobile") : "");
        actStoreInfoBinding.tvstoremail.setText(getIntent().getStringExtra("email") != null ? getIntent().getStringExtra("email") : "");
        actStoreInfoBinding.tvstoreaddress.setText(getIntent().getStringExtra("storeaddress") != null ? getIntent().getStringExtra("storeaddress") : "Address");
        actStoreInfoBinding.tvRatePro.setText(getIntent().getStringExtra("rate") != null ? getIntent().getStringExtra("rate") : "0.0");
        actStoreInfoBinding.tvstorename.setText(getIntent().getStringExtra("vendorsName") != null ? getIntent().getStringExtra("vendorsName") : "");

        Glide.with(ActStoreInfo.this)
                .load(getIntent().getStringExtra("image"))
                .into(actStoreInfoBinding.ivvendors);
    }
}
