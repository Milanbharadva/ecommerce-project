package com.webmintinfotech.ecom.ui.authentication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import com.facebook.*;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.ui.activity.ActMain;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.api.RestResponse;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.databinding.ActLoginBinding;
import com.webmintinfotech.ecom.model.LoginModel;
import com.webmintinfotech.ecom.model.RegistrationModel;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.SharePreference;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActLogin extends BaseActivity {
    private ActLoginBinding loginBinding;

    // Google Login
    private GoogleSignInClient mGoogleSignInClient;
    private final int RC_SIGN_IN = 1;

    // Facebook Login
    private CallbackManager callbackManager;
    private FacebookCallback<LoginResult> callback;
    private String strToken = "";
    private String getUserLoginType = "0";

    @Override
    public View setLayout() {
        return loginBinding.getRoot();
    }

    @Override
    public void initView() {
        loginBinding = ActLoginBinding.inflate(getLayoutInflater());
        FirebaseApp.initializeApp(this);
        strToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Token-->", strToken);
        Common.getLog("Token== ", strToken);

        getUserLoginType = SharePreference.getStringPref(this, SharePreference.UserLoginType);
        loginBinding.btnLogin.setOnClickListener(v -> login());
        loginBinding.tvSignUp.setOnClickListener(v -> openActivity(ActSignUp.class));

        // Google Login
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        loginBinding.googleSignUp.setOnClickListener(v -> {
            if (Common.isCheckNetwork(this)) {
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(this, task -> signInGoogle());
            } else {
                Common.alertErrorOrValidationDialog(this, getResources().getString(R.string.no_internet));
            }
        });

        // Facebook Login
        loginBinding.facebookSignUp.setOnClickListener(v -> {
            if (AccessToken.getCurrentAccessToken() != null) {
                LoginManager.getInstance().logOut();
            }
            LoginManager.getInstance().logInWithReadPermissions(this, getFacebookPermissions());
        });

        loginBinding.tvForgetPassword.setOnClickListener(v -> openActivity(ActForgotPassword.class));
        loginBinding.tvSkip.setOnClickListener(v -> openActivity(ActMain.class));

        FacebookSdk.setApplicationId(getResources().getString(R.string.facebook_id));
        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                updateFacebookUI(loginResult);
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            nextGmailActivity(account);
        } catch (ApiException e) {
            Log.e("Google Login", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    @SuppressLint("HardwareIds")
    private void nextGmailActivity(GoogleSignInAccount profile) {
        if (profile != null) {
            String loginType = "google";
            String FristName = profile.getDisplayName();
            String profileEmail = profile.getEmail();
            String profileId = profile.getId();
            loginApiCall(FristName, profileEmail, profileId, loginType, strToken);
        }
    }

    public List<String> getFacebookPermissions() {
        return Arrays.asList("email");
    }

    private void updateFacebookUI(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), (object, response) -> getFacebookData(object));
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email,age_range, gender, birthday, location");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void getFacebookData(JSONObject object) {
        try {
            String profileId = object.getString("id");
            String name = "";
            if (object.has("first_name")) {
                name = object.getString("first_name");
            }
            if (object.has("last_name")) {
                name += " " + object.getString("last_name");
            }
            String email = "";
            if (object.has("email")) {
                email = object.getString("email");
            }
            String loginType = "facebook";
            loginApiCall(name, email, profileId, loginType, strToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loginApiCall(String name, String email, String profileId, String loginType, String strToken) {
        HashMap<String, String> hasmap = new HashMap<>();
        hasmap.put("name", name);
        hasmap.put("email", email);
        hasmap.put("mobile", "");
        hasmap.put("token", strToken);
        hasmap.put("login_type", loginType);
        if (loginType.equals("google")) {
            hasmap.put("google_id", profileId);
            hasmap.put("facebook_id", "");
        } else {
            hasmap.put("facebook_id", profileId);
            hasmap.put("google_id", "");
        }
        Common.showLoadingProgress(this);
        Call<RestResponse<RegistrationModel>> call = ApiClient.getClient().setRegistration(hasmap);
        call.enqueue(new Callback<RestResponse<RegistrationModel>>() {
            @Override
            public void onResponse(Call<RestResponse<RegistrationModel>> call, Response<RestResponse<RegistrationModel>> response) {
                if (response.code() == 200) {
                    RestResponse<RegistrationModel> registrationResponse = response.body();
                    if (registrationResponse.getStatus().equals("1")) {
                        Common.dismissLoadingProgress();
                        setProfileData(registrationResponse.getData(), registrationResponse.getMessage());
                    } else if (registrationResponse.getStatus().equals("0")) {
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(ActLogin.this, registrationResponse.getMessage());
                    } else if (registrationResponse.getStatus().equals("2")) {
                        Common.dismissLoadingProgress();
                        mGoToRegistration(name, email, profileId, loginType, strToken);
                    } else if (registrationResponse.getStatus().equals("3")) {
                        Common.dismissLoadingProgress();
                        if (getUserLoginType.equals("1")) {
                            startActivity(new Intent(ActLogin.this, ActOTPVerification.class).putExtra("email", registrationResponse.getMobile().toString()));
                        } else {
                            startActivity(new Intent(ActLogin.this, ActOTPVerification.class).putExtra("email", email));
                        }
                    }
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        if (error.getString("status").equals("3")) {
                            Common.dismissLoadingProgress();
                            if (getUserLoginType.equals("1")) {
                                startActivity(new Intent(ActLogin.this, ActOTPVerification.class).putExtra("email", error.getString("mobile").toString()));
                            } else {
                                startActivity(new Intent(ActLogin.this, ActOTPVerification.class).putExtra("email", email));
                            }
                        } else {
                            Common.dismissLoadingProgress();
                            Common.alertErrorOrValidationDialog(ActLogin.this, error.getString("message"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<RestResponse<RegistrationModel>> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActLogin.this, getResources().getString(R.string.error_msg));
            }
        });
    }

    private void login() {
        if (loginBinding.edtEmail.getText().toString().equals("")) {
            Common.showErrorFullMsg(ActLogin.this, getResources().getString(R.string.validation_all));
        } else if (!Common.isValidEmail(loginBinding.edtEmail.getText().toString())) {
            Common.showErrorFullMsg(ActLogin.this, getResources().getString(R.string.validation_valid_email));
        } else if (loginBinding.edtPassword.getText().toString().equals("")) {
            Common.showErrorFullMsg(ActLogin.this, getResources().getString(R.string.validation_all));
        } else {
            HashMap<String, String> hasmap = new HashMap<>();
            hasmap.put("email", loginBinding.edtEmail.getText().toString());
            hasmap.put("password", loginBinding.edtPassword.getText().toString());
            hasmap.put("token", strToken);
            if (Common.isCheckNetwork(this)) {
                callApiLogin(hasmap);
            } else {
                Common.alertErrorOrValidationDialog(ActLogin.this, getResources().getString(R.string.no_internet));
            }
        }
    }

    private void callApiLogin(HashMap<String, String> hasmap) {
        Common.showLoadingProgress(ActLogin.this);
        Call<RestResponse<LoginModel>> call = ApiClient.getClient().getLogin(hasmap);
        call.enqueue(new Callback<RestResponse<LoginModel>>() {
            @Override
            public void onResponse(Call<RestResponse<LoginModel>> call, Response<RestResponse<LoginModel>> response) {
                if (response.code() == 200) {
                    RestResponse<LoginModel> loginResponse = response.body();
                    if (loginResponse.getStatus().equals("1")) {
                        Common.dismissLoadingProgress();
                        LoginModel loginModel = loginResponse.getData();
                        SharePreference.setBooleanPref(ActLogin.this, SharePreference.isLogin, true);
                        SharePreference.setStringPref(ActLogin.this, SharePreference.userId, loginModel.getId());
                        SharePreference.setStringPref(ActLogin.this, SharePreference.userMobile, loginModel.getMobile());
                        SharePreference.setStringPref(ActLogin.this, SharePreference.userEmail, loginModel.getEmail());
                        SharePreference.setStringPref(ActLogin.this, SharePreference.userName, loginModel.getName());
                        SharePreference.setStringPref(ActLogin.this, SharePreference.userRefralCode, loginModel.getReferral_code());
                        SharePreference.setStringPref(ActLogin.this, SharePreference.userProfile, loginModel.getProfile_pic());
                        Intent intent = new Intent(ActLogin.this, ActMain.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        finishAffinity();
                    } else if (loginResponse.getStatus().equals("2")) {
                        Common.dismissLoadingProgress();
                        String otpController = SharePreference.getStringPref(ActLogin.this, SharePreference.UserLoginType).equals("1")
                                ? loginBinding.edtEmail.getText().toString()
                                : loginBinding.edtEmail.getText().toString();
                        startActivity(new Intent(ActLogin.this, ActOTPVerification.class).putExtra("email", otpController));
                    }
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        int status = error.getInt("status");
                        if (status == 2) {
                            Common.dismissLoadingProgress();
                            String otpController = SharePreference.getStringPref(ActLogin.this, SharePreference.UserLoginType).equals("1")
                                    ? loginBinding.edtEmail.getText().toString()
                                    : loginBinding.edtEmail.getText().toString();
                            startActivity(new Intent(ActLogin.this, ActOTPVerification.class).putExtra("email", otpController));
                        } else {
                            Common.dismissLoadingProgress();
                            Common.showErrorFullMsg(ActLogin.this, error.getString("message"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<RestResponse<LoginModel>> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActLogin.this, getResources().getString(R.string.error_msg));
            }
        });
    }

    private void mGoToRegistration(String name, String profileEmail, String profileId, String loginType, String strToken) {
        Intent intent = new Intent(ActLogin.this, ActSignUp.class);
        intent.putExtra("name", name);
        intent.putExtra("profileEmail", profileEmail);
        intent.putExtra("profileId", profileId);
        intent.putExtra("loginType", loginType);
        intent.putExtra("strToken", strToken);
        startActivity(intent);
    }

    private void setProfileData(RegistrationModel dataResponse, String message) {
        SharePreference.setBooleanPref(ActLogin.this, SharePreference.isLogin, true);
        SharePreference.setStringPref(ActLogin.this, SharePreference.userId, dataResponse.getId().toString());
        SharePreference.setStringPref(ActLogin.this, SharePreference.loginType, dataResponse.getLogin_type().toString());
        SharePreference.setStringPref(ActLogin.this, SharePreference.userName, dataResponse.getName().toString());
        SharePreference.setStringPref(ActLogin.this, SharePreference.userMobile, dataResponse.getMobile().toString());
        SharePreference.setStringPref(ActLogin.this, SharePreference.userEmail, dataResponse.getEmail().toString());
        SharePreference.setStringPref(ActLogin.this, SharePreference.userProfile, dataResponse.getProfile_image().toString());
        SharePreference.setStringPref(ActLogin.this, SharePreference.userRefralCode, dataResponse.getReferral_code().toString());
        startActivity(new Intent(ActLogin.this, ActMain.class));
        finish();
        finishAffinity();
    }

    @Override
    public void onBackPressed() {
        mExitDialog();
    }

    private void mExitDialog() {
        Dialog dialog = null;
        try {
            if (dialog != null) {
                dialog.dismiss();
            }
            dialog = new Dialog(ActLogin.this, R.style.AppCompatAlertDialogStyleBig);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            LayoutInflater mInflater = LayoutInflater.from(ActLogin.this);
            View mView = mInflater.inflate(R.layout.dlg_confomation, null, false);
            TextView tvYes = mView.findViewById(R.id.tvYes);
            TextView tvNo = mView.findViewById(R.id.tvNo);
            Dialog finalDialog = dialog;
            tvYes.setOnClickListener(v -> {
                finalDialog.dismiss();
                ActivityCompat.finishAfterTransition(ActLogin.this);
                ActivityCompat.finishAffinity(ActLogin.this);
                finish();
            });
            tvNo.setOnClickListener(v -> finalDialog.dismiss());
            dialog.setContentView(mView);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}