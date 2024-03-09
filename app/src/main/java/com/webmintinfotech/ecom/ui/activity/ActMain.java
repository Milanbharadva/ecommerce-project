package com.webmintinfotech.ecom.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.databinding.ActMainBinding;
import com.webmintinfotech.ecom.ui.authentication.ActLogin;
import com.webmintinfotech.ecom.ui.fragment.FavoriteFragment;
import com.webmintinfotech.ecom.ui.fragment.HomeFragment;
import com.webmintinfotech.ecom.ui.fragment.MyAccountFragment;
import com.webmintinfotech.ecom.ui.fragment.MyCartFragment;
import com.webmintinfotech.ecom.ui.fragment.OrderHistoryFragment;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.SharePreference;

import java.util.Objects;

public class ActMain extends BaseActivity {

    private ActMainBinding mainBinding;
    private int temp = 1;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("WrongConstant")
    @Override
    public View setLayout() {
        return mainBinding.getRoot();
    }

    @Override
    public void initView() {
        mainBinding = ActMainBinding.inflate(getLayoutInflater());
        temp = getIntent().getStringExtra("pos") != null ? Integer.parseInt(Objects.requireNonNull(getIntent().getStringExtra("pos"))) : 1;
        setFragment(temp);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_home:
                if (temp != 1) {
                    setFragment(1);
                    temp = 1;
                }
                break;
            case R.id.menu_fav:
                if (SharePreference.getBooleanPref(this, SharePreference.isLogin)) {
                    if (temp != 2) {
                        setFragment(2);
                        temp = 2;
                    }
                } else {
                    openActivity(ActLogin.class);
                    finish();
                    finishAffinity();
                }
                break;
            case R.id.menu_cart:
                if (SharePreference.getBooleanPref(this, SharePreference.isLogin)) {
                    if (temp != 3) {
                        setFragment(3);
                        temp = 3;
                    }
                } else {
                    openActivity(ActLogin.class);
                    finish();
                    finishAffinity();
                }
                break;
            case R.id.menu_doce:
                if (SharePreference.getBooleanPref(this, SharePreference.isLogin)) {
                    if (temp != 4) {
                        setFragment(4);
                        temp = 4;
                    }
                } else {
                    openActivity(ActLogin.class);
                    finish();
                    finishAffinity();
                }
                break;
            case R.id.menu_profile:
                if (temp != 5) {
                    setFragment(5);
                    temp = 5;
                }
                break;
        }
    }

    @SuppressLint("NewApi")
    public void setFragment(int pos) {
        mainBinding.ivHome.setImageTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(), R.color.gray, null)));
        mainBinding.ivFav.setImageTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(), R.color.gray, null)));
        mainBinding.ivCart.setImageTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(), R.color.gray, null)));
        mainBinding.ivDoce.setImageTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(), R.color.gray, null)));
        mainBinding.ivProfile.setImageTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(), R.color.gray, null)));

        switch (pos) {
            case 1:
                mainBinding.ivHome.setImageTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(), R.color.Blackcolor, null)));
                replaceFragment(new HomeFragment());
                break;
            case 2:
                mainBinding.ivFav.setImageTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(), R.color.Blackcolor, null)));
                replaceFragment(new FavoriteFragment());
                break;
            case 3:
                mainBinding.ivCart.setImageTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(), R.color.Blackcolor, null)));
                replaceFragment(new MyCartFragment());
                break;
            case 4:
                mainBinding.ivDoce.setImageTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(), R.color.Blackcolor, null)));
                replaceFragment(new OrderHistoryFragment());
                break;
            case 5:
                mainBinding.ivProfile.setImageTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(), R.color.Blackcolor, null)));
                replaceFragment(new MyAccountFragment());
                break;
        }
    }

    @SuppressLint("WrongConstant")
    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.FramFragment, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (temp != 1) {
            temp = 1;
            setFragment(1);
        } else {
            mExitDialog();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Common.getCurrentLanguage(this, false);
    }

    private void mExitDialog() {
        Dialog dialog;
        try {
            dialog = new Dialog(this, R.style.AppCompatAlertDialogStyleBig);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            LayoutInflater mInflater = LayoutInflater.from(this);
            View mView = mInflater.inflate(R.layout.dlg_confomation, null, false);
            TextView tvYes = mView.findViewById(R.id.tvYes);
            TextView tvNo = mView.findViewById(R.id.tvNo);
            dialog.setContentView(mView);
            tvYes.setOnClickListener(v -> {
                dialog.dismiss();
                ActivityCompat.finishAfterTransition(this);
                ActivityCompat.finishAffinity(this);
                finish();
            });
            tvNo.setOnClickListener(v -> dialog.dismiss());
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
