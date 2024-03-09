package com.webmintinfotech.ecom.ui.authentication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.api.SingleResponse;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.databinding.ActForgotPasswordBinding;
import com.webmintinfotech.ecom.utils.Common;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActForgotPassword extends BaseActivity {

    private ActForgotPasswordBinding forgotPasswordBinding;

    @Override
    public View setLayout() {
        return forgotPasswordBinding.getRoot();
    }

    @Override
    public void initView() {
        forgotPasswordBinding = ActForgotPasswordBinding.inflate(getLayoutInflater());
        forgotPasswordBinding.ivBack.setOnClickListener(v -> finish());
        forgotPasswordBinding.btnForgotPassword.setOnClickListener(v -> {
            if (forgotPasswordBinding.edtForgetEmail.getText().toString().equals("")) {
                Common.showErrorFullMsg(
                        this,
                        getResources().getString(R.string.validation_all)
                );
            } else if (!Common.isValidEmail(forgotPasswordBinding.edtForgetEmail.getText().toString())) {
                Common.showErrorFullMsg(
                        this,
                        getResources().getString(R.string.validation_valid_email)
                );
            } else {
                HashMap<String, String> hasmap = new HashMap<>();
                hasmap.put("email", forgotPasswordBinding.edtForgetEmail.getText().toString());
                if (Common.isCheckNetwork(this)) {
                    callApiForgetPassword(hasmap);
                } else {
                    Common.alertErrorOrValidationDialog(
                            this,
                            getResources().getString(R.string.no_internet)
                    );
                }
            }
        });
    }

    //TODO API FORGET PASSWORD CALL
    private void callApiForgetPassword(HashMap<String, String> hasmap) {
        Common.showLoadingProgress(this);
        Call<SingleResponse> call = ApiClient.getClient().setforgotPassword(hasmap);
        call.enqueue(new Callback<SingleResponse>() {
            @Override
            public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {
                if (response.code() == 200) {
                    Common.dismissLoadingProgress();
                    SingleResponse restResponse = response.body();
                    if (restResponse.getStatus() == 1) {
                        successfulDialog(
                                ActForgotPassword.this,
                                restResponse.getMessage()
                        );
                    } else {
                        successfulDialog(
                                ActForgotPassword.this,
                                restResponse.getMessage()
                        );
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.showErrorFullMsg(
                            ActForgotPassword.this,
                            getResources().getString(R.string.error_msg)
                    );
                }
            }

            @Override
            public void onFailure(Call<SingleResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.showErrorFullMsg(
                        ActForgotPassword.this,
                        getResources().getString(R.string.error_msg)
                );
            }
        });
    }

    //TODO FORGET PASSWORD SUCCESS DIALOG
    @SuppressLint("InflateParams")
    public void successfulDialog(Activity act, String msg) {
        Dialog dialog = null;
        try {
            if (dialog != null) {
                dialog.dismiss();
            }
            dialog = new Dialog(act, R.style.AppCompatAlertDialogStyleBig);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT
            );
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            LayoutInflater mInflater = LayoutInflater.from(act);
            View mView = mInflater.inflate(R.layout.dlg_validation, null, false);
            TextView textDesc = mView.findViewById(R.id.tvMessage);
            textDesc.setText(msg);
            TextView tvOk = mView.findViewById(R.id.tvOk);
            Dialog finalDialog = dialog;
            tvOk.setOnClickListener(v -> {
                finalDialog.dismiss();
                finish();
            });
            dialog.setContentView(mView);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Common.getCurrentLanguage(this, false);
    }
}