package com.webmintinfotech.ecom.ui.authentication;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.ui.activity.ActMain;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.databinding.ActSplashScreenBinding;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.SharePreference;

public class ActSplashScreen extends BaseActivity {

    private ActSplashScreenBinding splashScreenBinding;

    @Override
    public View setLayout() {
        return splashScreenBinding.getRoot();
    }

    @Override
    public void initView() {
        splashScreenBinding = ActSplashScreenBinding.inflate(getLayoutInflater());
        if (Common.isCheckNetwork(this)) {
            init();
        } else {
            Common.alertErrorOrValidationDialog(
                    this,
                    getResources().getString(R.string.no_internet)
            );
        }
    }

    private void init() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            openActivity(ActMain.class);
            finish();
        }, 3000);
    }
}
