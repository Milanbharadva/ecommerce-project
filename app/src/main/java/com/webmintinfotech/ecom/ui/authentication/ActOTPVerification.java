package com.webmintinfotech.ecom.ui.authentication;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.api.SingleResponse;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.databinding.ActOtpverificationBinding;
import com.webmintinfotech.ecom.ui.activity.ActMain;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.SharePreference;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActOTPVerification extends BaseActivity {
    private ActOtpverificationBinding otpverificationBinding;
    private String strEmail = "";
    private String strToken = "";
    private String loginUserType = "";

    @Override
    protected View setLayout() {
        return otpverificationBinding.getRoot();
    }

    @Override
    protected void initView() {
        otpverificationBinding = ActOtpverificationBinding.inflate(getLayoutInflater());
        strEmail = getIntent().getStringExtra("email");
        strToken = FirebaseInstanceId.getInstance().getToken();
        otpverificationBinding.tvCheckout.setOnClickListener(view -> {
            Common.closeKeyBoard(ActOTPVerification.this);
            if (otpverificationBinding.edOTP.getText().toString().length() != 6) {
                Common.alertErrorOrValidationDialog(ActOTPVerification.this, getResources().getString(R.string.validation_otp));
            } else {
                if (loginUserType.equals("1")) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("mobile", strEmail);
                    map.put("otp", otpverificationBinding.edOTP.getText().toString());
                    map.put("token", strToken);
                    if (Common.isCheckNetwork(ActOTPVerification.this)) {
                        callApiOTP(map);
                    } else {
                        Common.alertErrorOrValidationDialog(ActOTPVerification.this, getResources().getString(R.string.no_internet));
                    }
                } else {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("email", strEmail);
                    map.put("otp", otpverificationBinding.edOTP.getText().toString());
                    map.put("token", strToken);
                    if (Common.isCheckNetwork(ActOTPVerification.this)) {
                        callApiOTP(map);
                    } else {
                        Common.alertErrorOrValidationDialog(ActOTPVerification.this, getResources().getString(R.string.no_internet));
                    }
                }
            }
        });
        timer();
        otpverificationBinding.tvResendOtp.setOnClickListener(view -> {
            HashMap<String, String> map = new HashMap<>();
            map.put("email", strEmail);
            map.put("otp", otpverificationBinding.edOTP.getText().toString());
            map.put("token", strToken);
            if (Common.isCheckNetwork(ActOTPVerification.this)) {
                callApiResendOTP(map);
            } else {
                Common.alertErrorOrValidationDialog(ActOTPVerification.this, getResources().getString(R.string.no_internet));
            }
        });
    }

    private void callApiOTP(HashMap<String, String> map) {
        Common.showLoadingProgress(ActOTPVerification.this);
        Call<JsonObject> call = ApiClient.getClient().setEmailVerify(map);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    try {
                        JSONObject mainObject = new JSONObject(response.body().toString());
                        int statusType = mainObject.getInt("status");
                        String statusMessage = mainObject.getString("message");
                        if (statusType == 0) {
                            Common.dismissLoadingProgress();
                            Common.alertErrorOrValidationDialog(ActOTPVerification.this, statusMessage);
                        } else if (statusType == 1) {
                            Common.dismissLoadingProgress();
                            SharePreference.setStringPref(ActOTPVerification.this, SharePreference.userId, mainObject.getJSONObject("data").getString("id"));
                            SharePreference.setStringPref(ActOTPVerification.this, SharePreference.userName, mainObject.getJSONObject("data").getString("name"));
                            SharePreference.setStringPref(ActOTPVerification.this, SharePreference.userEmail, mainObject.getJSONObject("data").getString("email"));
                            SharePreference.setStringPref(ActOTPVerification.this, SharePreference.userMobile, mainObject.getJSONObject("data").getString("mobile"));
                            SharePreference.setStringPref(ActOTPVerification.this, SharePreference.userProfile, mainObject.getJSONObject("data").getString("profile_pic"));
                            SharePreference.setStringPref(ActOTPVerification.this, SharePreference.userRefralCode, mainObject.getJSONObject("data").getString("referral_code"));
                            SharePreference.setBooleanPref(ActOTPVerification.this, SharePreference.isLogin, true);
                            openActivity(ActMain.class);
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (response.code() == 500) {
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(ActOTPVerification.this, getResources().getString(R.string.error_msg));
                    } else {
                        try {
                            JSONObject mainErrorObject = new JSONObject(response.errorBody().string());
                            String strMessage = mainErrorObject.getString("message");
                            Common.dismissLoadingProgress();
                            Common.alertErrorOrValidationDialog(ActOTPVerification.this, strMessage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActOTPVerification.this, getResources().getString(R.string.error_msg));
            }
        });
    }

    private void callApiResendOTP(HashMap<String, String> map) {
        Common.showLoadingProgress(ActOTPVerification.this);
        Call<SingleResponse> call = ApiClient.getClient().setResendEmailVerification(map);
        call.enqueue(new Callback<SingleResponse>() {
            @Override
            public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {
                if (response.code() == 200) {
                    SingleResponse registrationResponse = response.body();
                    if (registrationResponse.getStatus() == 1) {
                        otpverificationBinding.edOTP.getText().clear();
                        Common.dismissLoadingProgress();
                        Common.showSuccessFullMsg(ActOTPVerification.this, registrationResponse.getMessage());
                    } else if (registrationResponse.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        Common.showErrorFullMsg(ActOTPVerification.this, registrationResponse.getMessage());
                    }
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Common.dismissLoadingProgress();
                        Common.showErrorFullMsg(ActOTPVerification.this, error.getString("message"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SingleResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.showErrorFullMsg(ActOTPVerification.this, getResources().getString(R.string.error_msg));
            }
        });
    }

    @Override
    public void onBackPressed() {
        Common.closeKeyBoard(ActOTPVerification.this);
        openActivity(ActLogin.class);
        finish();
        finishAffinity();
    }

    private void timer() {
        new CountDownTimer(120000, 1000) {
            @Override
            public void onTick(long millis) {
                otpverificationBinding.llOTP.setVisibility(View.GONE);
                otpverificationBinding.tvResendOtp.setVisibility(View.GONE);
                otpverificationBinding.tvTimer.setVisibility(View.VISIBLE);
                String timer = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                otpverificationBinding.tvTimer.setText(timer);
                if (timer.equals("00:00")) {
                    otpverificationBinding.tvTimer.setVisibility(View.GONE);
                    otpverificationBinding.llOTP.setVisibility(View.VISIBLE);
                    otpverificationBinding.tvResendOtp.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFinish() {
                otpverificationBinding.tvTimer.setVisibility(View.GONE);
                otpverificationBinding.llOTP.setVisibility(View.VISIBLE);
                otpverificationBinding.tvResendOtp.setVisibility(View.VISIBLE);
            }
        }.start();
    }
}
