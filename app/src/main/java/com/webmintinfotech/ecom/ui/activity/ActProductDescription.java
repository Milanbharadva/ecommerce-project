package com.webmintinfotech.ecom.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.databinding.ActProductDescriptionBinding;

public class ActProductDescription extends BaseActivity {

    private ActProductDescriptionBinding productDescriptionBinding;

    @Override
    public View setLayout() {
        return productDescriptionBinding.getRoot();
    }

    @Override
    public void initView() {
        productDescriptionBinding = ActProductDescriptionBinding.inflate(getLayoutInflater());

        // TODO: PRODUCT DESCRIPTION TEXT SET
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String description = extras.getString("description");
            productDescriptionBinding.tvProductDescription.setText(description);
        }

        productDescriptionBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                setResult(RESULT_OK);
            }
        });
    }
}
