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
import com.webmintinfotech.ecom.databinding.ActChangePasswordBinding;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.SharePreference;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.HashMap;

public class ActChangePassword extends BaseActivity {

    private ActChangePasswordBinding changePasswordBinding;

    @Override
    public View setLayout() {
        return changePasswordBinding.getRoot();
    }

    @Override
    public void initView() {
        changePasswordBinding = ActChangePasswordBinding.inflate(getLayoutInflater());
        changePasswordBinding.ivBack.setOnClickListener(view -> finish());
        changePasswordBinding.btnreset.setOnClickListener(view -> {
            if (changePasswordBinding.edOldPass.getText().toString().equals("")) {
                Common.showErrorFullMsg(ActChangePassword.this, getResources().getString(R.string.validation_oldpassword));
            } else if (changePasswordBinding.edNewPassword.getText().toString().equals("")) {
                Common.showErrorFullMsg(ActChangePassword.this, getResources().getString(R.string.validation_password));
            } else if (changePasswordBinding.edNewPassword.getText().toString().length() < 7) {
                Common.showErrorFullMsg(ActChangePassword.this, getResources().getString(R.string.validation_valid_password));
            } else if (changePasswordBinding.edConfirmPassword.getText().toString().equals("")) {
                Common.showErrorFullMsg(ActChangePassword.this, getResources().getString(R.string.validation_cpassword));
            } else if (!changePasswordBinding.edConfirmPassword.getText().toString().equals(changePasswordBinding.edNewPassword.getText().toString())) {
                Common.showErrorFullMsg(ActChangePassword.this, getResources().getString(R.string.validation_valid_cpassword));
            } else {
                HashMap<String, String> hasmap = new HashMap<>();
                hasmap.put("user_id", SharePreference.getStringPref(ActChangePassword.this, SharePreference.userId));
                hasmap.put("old_password", changePasswordBinding.edOldPass.getText().toString());
                hasmap.put("new_password", changePasswordBinding.edNewPassword.getText().toString());

                if (Common.isCheckNetwork(ActChangePassword.this)) {
                    callApiChangePassword(hasmap);
                } else {
                    Common.alertErrorOrValidationDialog(ActChangePassword.this, getResources().getString(R.string.no_internet));
                }
            }
        });
    }

    //TODO API CHANGE PASSWORD CALL
    private void callApiChangePassword(HashMap<String, String> hasmap) {
        Common.showLoadingProgress(ActChangePassword.this);
        Call<SingleResponse> call = ApiClient.getClient().setChangePassword(hasmap);
        call.enqueue(new Callback<SingleResponse>() {
            @Override
            public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {
                if (response.code() == 200) {
                    SingleResponse restResponse = response.body();
                    if (restResponse != null && restResponse.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        successfulDialog(ActChangePassword.this, restResponse.getMessage());
                    }
                } else {
                    Common.dismissLoadingProgress();
                    try {
                        if (response.errorBody() != null) {
                            JSONObject error = new JSONObject(response.errorBody().string());
                            Common.alertErrorOrValidationDialog(ActChangePassword.this, error.getString("message"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SingleResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActChangePassword.this, getResources().getString(R.string.error_msg));
            }
        });
    }

    //TODO PASSWORD CHANGE SUCCESS DIALOG
    @SuppressLint("InflateParams")
    public void successfulDialog(Activity act, String msg) {
        Dialog dialog = null;
        try {
            if (dialog != null) {
                dialog.dismiss();
            }
            dialog = new Dialog(act, R.style.AppCompatAlertDialogStyleBig);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            LayoutInflater m_inflater = LayoutInflater.from(act);
            View m_view = m_inflater.inflate(R.layout.dlg_validation, null, false);
            TextView textDesc = m_view.findViewById(R.id.tvMessage);
            textDesc.setText(msg);
            TextView tvOk = m_view.findViewById(R.id.tvOk);
            Dialog finalDialog = dialog;
            tvOk.setOnClickListener(view -> {
                finalDialog.dismiss();
                finish();
            });
            dialog.setContentView(m_view);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Common.getCurrentLanguage(ActChangePassword.this, false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Common.getCurrentLanguage(ActChangePassword.this, false);
    }
}