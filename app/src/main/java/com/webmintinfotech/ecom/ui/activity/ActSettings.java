package com.webmintinfotech.ecom.ui.activity;

import static com.webmintinfotech.ecom.utils.Common.getCurrentLanguage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.api.SingleResponse;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.databinding.ActSettingsBinding;
import com.webmintinfotech.ecom.ui.authentication.ActChangePassword;
import com.webmintinfotech.ecom.ui.payment.ActWallet;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.SharePreference;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActSettings extends BaseActivity {

    private ActSettingsBinding settingsBinding;
    private String is_notification = "";
    private String referralCode = "";

    @Override
    public View setLayout() {
        return settingsBinding.getRoot();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void initView() {
        settingsBinding = ActSettingsBinding.inflate(getLayoutInflater());
        referralCode = getIntent().getStringExtra("referralCode");

        settingsBinding.ivBack.setOnClickListener(view -> {
            finish();
            setResult(RESULT_OK);
        });

        settingsBinding.clChangepassword.setOnClickListener(view -> openActivity(ActChangePassword.class));

        settingsBinding.clMyaddress.setOnClickListener(view -> openActivity(ActAddress.class));

        settingsBinding.clWallet.setOnClickListener(view -> openActivity(ActWallet.class));

        settingsBinding.clOffers.setOnClickListener(view -> {
//            openActivity(ActOffers.class);
        });

        settingsBinding.clReferandEarn.setOnClickListener(view -> {
            Intent intent = new Intent(ActSettings.this, ActReferAndEarn.class);
            intent.putExtra("referralCode", referralCode);
            Log.d("referralCode", referralCode);
            startActivity(intent);
        });

        settingsBinding.clChnagelayout.setOnClickListener(view -> {
            BottomSheetDialog dialog = new BottomSheetDialog(ActSettings.this);
            if (Common.isCheckNetwork(ActSettings.this)) {
                View bottomSheetView = getLayoutInflater().inflate(R.layout.row_bottomsheetlayout, null);
                dialog.getWindow().setBackgroundDrawable(getDrawable(R.color.tr));
                TextView btnLTREng = bottomSheetView.findViewById(R.id.tvLTR);
                TextView btnRTLHindi = bottomSheetView.findViewById(R.id.tvRTL);
                TextView btncancel = bottomSheetView.findViewById(R.id.tvCancel);
                btncancel.setOnClickListener(v -> dialog.dismiss());
                btnLTREng.setOnClickListener(v -> {
                    SharePreference.setStringPref(
                            ActSettings.this,
                            SharePreference.SELECTED_LANGUAGE,
                            getResources().getString(R.string.language_english)
                    );
                    getCurrentLanguage(ActSettings.this, true);
                });
                btnRTLHindi.setOnClickListener(v -> {
                    SharePreference.setStringPref(
                            ActSettings.this,
                            SharePreference.SELECTED_LANGUAGE,
                            getResources().getString(R.string.language_hindi)
                    );
                    getCurrentLanguage(ActSettings.this, true);
                });
                dialog.setCancelable(false);
                dialog.setContentView(bottomSheetView);
                dialog.show();
            } else {
                Common.alertErrorOrValidationDialog(ActSettings.this, getResources().getString(R.string.no_internet));
                dialog.dismiss();
            }
        });

        is_notification = getIntent().getStringExtra("notificationStatus");
        settingsBinding.swichNotification.setChecked("1".equals(is_notification));
        settingsBinding.swichNotification.setOnCheckedChangeListener((view, isChecked) -> {
            if (isChecked) {
                is_notification = "1";
                apicallNotification();
            } else {
                is_notification = "2";
                apicallNotification();
            }
        });
    }

    private void apicallNotification() {
        Common.showLoadingProgress(ActSettings.this);
        HashMap<String, String> hasmap = new HashMap<>();
        hasmap.put("user_id", SharePreference.getStringPref(ActSettings.this, SharePreference.userId));
        hasmap.put("notification_status", is_notification);
        Call<SingleResponse> call = ApiClient.getClient().changeNotificationStatus(hasmap);
        call.enqueue(new Callback<SingleResponse>() {
            @Override
            public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {
                if (response.code() == 200) {
                    Common.dismissLoadingProgress();
                    Common.isAddOrUpdated = true;
                    if (response.body() != null && response.body().getStatus() == 1) {
                        Common.dismissLoadingProgress();
                    } else {
                        Common.showErrorFullMsg(ActSettings.this, response.body() != null ? response.body().getMessage() : "");
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.showErrorFullMsg(ActSettings.this, getResources().getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(Call<SingleResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.showErrorFullMsg(ActSettings.this, getResources().getString(R.string.error_msg));
            }
        });
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        getCurrentLanguage(ActSettings.this, false);
    }
}
