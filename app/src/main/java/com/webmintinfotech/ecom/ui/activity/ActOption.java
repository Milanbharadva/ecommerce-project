package com.webmintinfotech.ecom.ui.activity;

import android.view.LayoutInflater;
import android.view.View;

import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.databinding.ActSplashBinding;
import com.webmintinfotech.ecom.ui.authentication.ActLogin;
import com.webmintinfotech.ecom.ui.authentication.ActSignUp;

public class ActOption extends BaseActivity {

    private ActSplashBinding splashBinding;

    @Override
    public View setLayout() {
        return splashBinding.getRoot();
    }

    @Override
    public void initView() {
        splashBinding = ActSplashBinding.inflate(LayoutInflater.from(this));
        splashBinding.btnLogin.setOnClickListener(view -> openActivity(ActLogin.class));
        splashBinding.tvSignUp.setOnClickListener(view -> openActivity(ActSignUp.class));
        splashBinding.btnSkip.setOnClickListener(view -> openActivity(ActMain.class));
    }
}
