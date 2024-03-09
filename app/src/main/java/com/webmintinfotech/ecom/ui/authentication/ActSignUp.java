package  com.webmintinfotech.ecom.ui.authentication;

import static com.webmintinfotech.ecom.utils.Common.alertErrorOrValidationDialog;
import static com.webmintinfotech.ecom.utils.Common.showErrorFullMsg;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.api.RestResponse;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.databinding.ActSignUpBinding;
import com.webmintinfotech.ecom.model.RegistrationModel;
import com.webmintinfotech.ecom.ui.activity.ActMain;
import com.webmintinfotech.ecom.ui.activity.ActPrivacyPolicy;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.SharePreference;

import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActSignUp extends BaseActivity implements CountryCodePicker.OnCountryChangeListener {
    private ActSignUpBinding signUpBinding;
    private String strToken = "";

    @Override
    protected View setLayout() {
        return signUpBinding.getRoot();
    }

    @Override
    protected void initView() {
        signUpBinding = ActSignUpBinding.inflate(getLayoutInflater());
        FirebaseApp.initializeApp(this);

        signUpBinding.tvTermsCondition.setOnClickListener(view -> {
            startActivity(new Intent(ActSignUp.this, ActPrivacyPolicy.class)
                    .putExtra("Type", "Terms Condition"));
        });

        if (getIntent().getStringExtra("loginType") != null) {
            signUpBinding.edtFullname.setText(getIntent().getStringExtra("name"));
            signUpBinding.edtEmail.setText(getIntent().getStringExtra("profileEmail"));
            signUpBinding.edtPassword.setVisibility(View.GONE);
            signUpBinding.tvPass.setVisibility(View.GONE);
            signUpBinding.edtEmail.setActivated(false);
            signUpBinding.edtEmail.setInputType(InputType.TYPE_NULL);
        } else {
            signUpBinding.edtPassword.setVisibility(View.VISIBLE);
        }

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                return;
            }
            strToken = task.getResult();
        });

        signUpBinding.btnSignUp.setOnClickListener(view -> {
            signUpBinding.ccp.setOnCountryChangeListener(this);
            signup();
            setResult(RESULT_OK);
        });

        signUpBinding.tvSkip.setOnClickListener(view -> openActivity(ActMain.class));
        signUpBinding.tvLogin.setOnClickListener(view -> openActivity(ActLogin.class));
        signUpBinding.tvBecomeavendors.setOnClickListener(view -> openActivity(ActSignUpVendor.class));
    }

    private void signup() {
        if (getIntent().getStringExtra("loginType") != null) {
            if (signUpBinding.edtMobile.getText().toString().isEmpty()) {
                showErrorFullMsg(this, getResources().getString(R.string.validation_all));
            } else if (getIntent().getStringExtra("loginType").equals("facebook") ||
                    getIntent().getStringExtra("loginType").equals("google")) {
                HashMap<String, String> hasmap = new HashMap<>();
                hasmap.put("name", getIntent().getStringExtra("name"));
                hasmap.put("email", getIntent().getStringExtra("profileEmail"));
                hasmap.put("mobile", "+" + signUpBinding.ccp.getSelectedCountryCode() + " " +
                        signUpBinding.edtMobile.getText().toString());
                hasmap.put("referral_code", signUpBinding.edtRefrrelCode.getText().toString());
                hasmap.put("token", getIntent().getStringExtra("strToken"));
                hasmap.put("register_type", "email");
                hasmap.put("login_type", getIntent().getStringExtra("loginType"));
                if (Common.isCheckNetwork(this)) {
                    if (signUpBinding.chbTermsCondition.isChecked()) {
                        callApiRegistration(hasmap);
                    } else {
                        showErrorFullMsg(this, getResources().getString(R.string.terms_condition_error));
                    }
                } else {
                    alertErrorOrValidationDialog(this, getResources().getString(R.string.no_internet));
                }
            }
        } else {
            Log.d("token", strToken);

            if (signUpBinding.edtFullname.getText().toString().isEmpty()) {
                showErrorFullMsg(this, getResources().getString(R.string.validation_all));
            } else if (signUpBinding.edtEmail.getText().toString().isEmpty()) {
                showErrorFullMsg(this, getResources().getString(R.string.validation_all));
            } else if (!Common.isValidEmail(signUpBinding.edtEmail.getText().toString())) {
                showErrorFullMsg(this, getResources().getString(R.string.validation_valid_email));
            } else if (signUpBinding.edtMobile.getText().toString().isEmpty()) {
                showErrorFullMsg(this, getResources().getString(R.string.validation_all));
            } else if (signUpBinding.edtPassword.getText().toString().isEmpty()) {
                showErrorFullMsg(this, getResources().getString(R.string.validation_all));
            } else {
                if (signUpBinding.chbTermsCondition.isChecked()) {
                    HashMap<String, String> hasmap = new HashMap<>();
                    hasmap.put("name", signUpBinding.edtFullname.getText().toString());
                    hasmap.put("email", signUpBinding.edtEmail.getText().toString());
                    hasmap.put("mobile", "+" + signUpBinding.ccp.getSelectedCountryCode() + " " +
                            signUpBinding.edtMobile.getText().toString());
                    hasmap.put("password", signUpBinding.edtPassword.getText().toString());
                    hasmap.put("token", strToken);
                    hasmap.put("login_type", "email");
                    hasmap.put("register_type", "email");
                    hasmap.put("referral_code", signUpBinding.edtRefrrelCode.getText().toString());
                    if (Common.isCheckNetwork(this)) {
                        callApiRegistration(hasmap);
                    } else {
                        alertErrorOrValidationDialog(this, getResources().getString(R.string.no_internet));
                    }
                } else {
                    showErrorFullMsg(this, getResources().getString(R.string.terms_condition_error));
                }
            }
        }
    }

    private void callApiRegistration(HashMap<String, String> hasmap) {
        Common.showLoadingProgress(this);
        Call<RestResponse<RegistrationModel>> call = ApiClient.getClient().setRegistration(hasmap);
        call.enqueue(new Callback<RestResponse<RegistrationModel>>() {
            @Override
            public void onResponse(Call<RestResponse<RegistrationModel>> call,
                                   Response<RestResponse<RegistrationModel>> response) {
                if (response.code() == 200) {
                    RestResponse<RegistrationModel> registrationResponse = response.body();
                    if ("1".equals(registrationResponse.getStatus())) {
                        Common.dismissLoadingProgress();
                        String otpController = "";
                        if (SharePreference.getStringPref(ActSignUp.this,
                                SharePreference.UserLoginType).equals("1")) {
                            otpController = signUpBinding.edtMobile.getText().toString();
                        } else {
                            otpController = signUpBinding.edtEmail.getText().toString();
                        }
                        startActivity(new Intent(ActSignUp.this, ActOTPVerification.class)
                                .putExtra("email", otpController));
                    } else if ("0".equals(registrationResponse.getStatus())) {
                        Common.dismissLoadingProgress();
                        alertErrorOrValidationDialog(ActSignUp.this,
                                registrationResponse.getMessage());
                    }
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        int status = error.getInt("status");
                        if (status == 2) {
                            Common.dismissLoadingProgress();
                            String otpController = SharePreference.getStringPref(ActSignUp.this,
                                    SharePreference.UserLoginType).equals("1")
                                    ? signUpBinding.edtMobile.getText().toString()
                                    : signUpBinding.edtEmail.getText().toString();
                            startActivity(new Intent(ActSignUp.this, ActOTPVerification.class)
                                    .putExtra("email", otpController));
                        } else {
                            Common.dismissLoadingProgress();
                            showErrorFullMsg(ActSignUp.this, error.getString("message"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<RestResponse<RegistrationModel>> call, Throwable t) {
                Common.dismissLoadingProgress();
                alertErrorOrValidationDialog(ActSignUp.this,
                        getResources().getString(R.string.error_msg));
            }
        });
    }

    @Override
    public void onCountrySelected(Country selectedCountry) {
        String code = signUpBinding.ccp.getSelectedCountryCode();
        Toast.makeText(ActSignUp.this, code, Toast.LENGTH_SHORT).show();
        Log.d("CCPCode", code);
    }
}
