package com.webmintinfotech.ecom.ui.authentication;

import android.view.View;

import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.databinding.ActWelComeBinding;

public class ActWelCome extends BaseActivity {

    private ActWelComeBinding actWelComeBinding;

    @Override
    public View setLayout() {
        actWelComeBinding = ActWelComeBinding.inflate(getLayoutInflater());
        return actWelComeBinding.getRoot();
    }

    @Override
    public void initView() {
        // Your initialization code goes here
    }
}