package com.webmintinfotech.ecom.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.databinding.ActReferAndEarnBinding;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.SharePreference;

public class ActReferAndEarn extends BaseActivity {
    private ActReferAndEarnBinding referAndEarnBinding;
    private String refercode = "";
    private String currency = "";
    private String currencyPosition = "";

    @Override
    public View setLayout() {
        return referAndEarnBinding.getRoot();
    }

    @Override
    public void initView() {
        referAndEarnBinding = ActReferAndEarnBinding.inflate(getLayoutInflater());
        init();
    }

    private void init() {
        currency = SharePreference.getStringPref(this, SharePreference.Currency);
        currencyPosition = SharePreference.getStringPref(this, SharePreference.CurrencyPosition);
        refercode = getIntent().getStringExtra("referralCode");
        referAndEarnBinding.ivBack.setOnClickListener(v -> finish());
        Log.d("referralCode", refercode);
        Log.d("referralAmount", SharePreference.getStringPref(this, SharePreference.ReferralAmount));
        referAndEarnBinding.tvReferCode.setText(refercode);
        referAndEarnBinding.btnShare.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.usethiscode) + " " + refercode + " " +
                    getString(R.string.toregisterwithecommapandget) + " " + Common.getPrice(currencyPosition, currency,
                    SharePreference.getStringPref(this, SharePreference.ReferralAmount)) + " " +
                    getString(R.string.bonus_amount));
            startActivity(Intent.createChooser(shareIntent, "Refer and Earn"));
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Common.getCurrentLanguage(this, false);
    }
}
