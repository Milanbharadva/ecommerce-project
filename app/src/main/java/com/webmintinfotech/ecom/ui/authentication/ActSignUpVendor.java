package com.webmintinfotech.ecom.ui.authentication;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.ui.activity.ActMain;
import com.webmintinfotech.ecom.ui.activity.ActPrivacyPolicy;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.api.SingleResponse;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.databinding.ActSignUpVendorBinding;
import com.webmintinfotech.ecom.utils.Common;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.android.gms.tasks.OnCompleteListener;

import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActSignUpVendor extends BaseActivity {
    private ActSignUpVendorBinding signUpVendorBinding;
    private String strToken = "";

    @Override
    public View setLayout() {
        return signUpVendorBinding.getRoot();
    }

    @Override
    public void initView() {
        signUpVendorBinding = ActSignUpVendorBinding.inflate(getLayoutInflater());
        init();
    }

    private void init() {
        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                return;
            }
            strToken = task.getResult();
        });

        signUpVendorBinding.btnSignUpVendor.setOnClickListener(view -> {
            signupVendor();
            setResult(RESULT_OK);
        });

        signUpVendorBinding.ivBack.setOnClickListener(view -> finish());

        signUpVendorBinding.tvTermsCondition.setOnClickListener(view ->
                startActivity(new Intent(ActSignUpVendor.this, ActPrivacyPolicy.class)
                        .putExtra("Type", "Terms Condition"))
        );
    }

    private void signupVendor() {
        Log.d("token", strToken);

        if (signUpVendorBinding.edtFullname.getText().toString().equals("")) {
            Common.showErrorFullMsg(this, getResources().getString(R.string.validation_all));
        } else if (signUpVendorBinding.edtEmail.getText().toString().equals("")) {
            Common.showErrorFullMsg(this, getResources().getString(R.string.validation_all));
        } else if (!Common.isValidEmail(signUpVendorBinding.edtEmail.getText().toString())) {
            Common.showErrorFullMsg(this,
                    getResources().getString(R.string.validation_valid_email));
        } else if (signUpVendorBinding.edtMobile.getText().toString().equals("")) {
            Common.showErrorFullMsg(this, getResources().getString(R.string.validation_all));
        } else if (signUpVendorBinding.edtPassword.getText().toString().equals("")) {
            Common.showErrorFullMsg(this, getResources().getString(R.string.validation_all));
        } else {
            if (signUpVendorBinding.chbTermsCondition.isChecked()) {
                HashMap<String, String> hasmap = new HashMap<>();
                hasmap.put("name", signUpVendorBinding.edtFullname.getText().toString());
                hasmap.put("email", signUpVendorBinding.edtEmail.getText().toString());
                hasmap.put("mobile", "+" + signUpVendorBinding.ccp.getSelectedCountryCode()
                        + " " + signUpVendorBinding.edtMobile.getText().toString());
                hasmap.put("password", signUpVendorBinding.edtPassword.getText().toString());
                hasmap.put("token", strToken);
                if (Common.isCheckNetwork(this)) {
                    callApiRegistrationVendor(hasmap);
                } else {
                    Common.alertErrorOrValidationDialog(this, getResources().getString(R.string.no_internet));
                }
            } else {
                Common.showErrorFullMsg(this, getResources().getString(R.string.terms_condition_error));
            }
        }
    }

    private void callApiRegistrationVendor(HashMap<String, String> hasmap) {
        Common.showLoadingProgress(this);
        Call<SingleResponse> call = ApiClient.getClient().setVendorsRegister(hasmap);
        call.enqueue(new Callback<SingleResponse>() {
            @Override
            public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {
                if (response.code() == 200) {
                    SingleResponse restResponse = response.body();
                    if (restResponse.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        openSuccessDialog();
                    } else if (restResponse.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        Common.showErrorFullMsg(ActSignUpVendor.this, restResponse.getMessage());
                    }
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Common.dismissLoadingProgress();
                        Common.showErrorFullMsg(ActSignUpVendor.this, error.getString("message"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SingleResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.showErrorFullMsg(ActSignUpVendor.this, getResources().getString(R.string.error_msg));
            }
        });
    }

    private void openSuccessDialog() {
        Dialog dialog = null;
        try {
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
            dialog = new Dialog(ActSignUpVendor.this, R.style.AppCompatAlertDialogStyleBig);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);

            LayoutInflater mInflater = LayoutInflater.from(ActSignUpVendor.this);
            View mView = mInflater.inflate(R.layout.dlg_validation, null, false);
            TextView textDesc = mView.findViewById(R.id.tvMessage);
            textDesc.setText("Vendor account registered successfully. \nPlease wait for approval process");

            TextView tvOk = mView.findViewById(R.id.tvOk);
            Dialog finalDialog = dialog;
            tvOk.setOnClickListener(view -> {
                finalDialog.dismiss();
                openActivity(ActMain.class);
            });

            dialog.setContentView(mView);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}