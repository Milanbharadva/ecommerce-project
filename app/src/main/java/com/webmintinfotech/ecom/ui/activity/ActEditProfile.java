package com.webmintinfotech.ecom.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.res.ResourcesCompat;
import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.api.SingleResponse;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.databinding.ActEditProfileBinding;
import com.webmintinfotech.ecom.model.GetProfileResponse;
import com.webmintinfotech.ecom.model.UserData;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.SharePreference;

import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.io.File;
import java.util.HashMap;

public class ActEditProfile extends BaseActivity {

    private ActEditProfileBinding editProfileBinding;
    private File imageFile;

    @Override
    public View setLayout() {
        editProfileBinding = ActEditProfileBinding.inflate(getLayoutInflater());
        return editProfileBinding.getRoot();
    }

    @Override
    public void initView() {
        if (Common.isCheckNetwork(this)) {
            HashMap<String, String> hasmap = new HashMap<>();
            hasmap.put("user_id", SharePreference.getStringPref(this, SharePreference.userId));
            callApiProfile(hasmap);
        } else {
            Common.alertErrorOrValidationDialog(this, getResources().getString(R.string.no_internet));
        }
        initClickListeners();
    }

    private void initClickListeners() {
        editProfileBinding.ivGellary.setOnClickListener(v -> {
//            ImagePicker.with(this)
//                    .cropSquare()
//                    .compress(1024)
//                    .saveDir(new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ServiceProvider"))
//                    .maxResultSize(1080, 1080)
//                    .createIntent(intent -> startForProfileImageResult.launch(intent)) ;
        });

        editProfileBinding.ivBack.setOnClickListener(v -> {
            finish();
            setResult(RESULT_OK);
        });

        editProfileBinding.btnsave.setOnClickListener(v -> {
            if (editProfileBinding.edtName.getText().toString().isEmpty()) {
                Common.showErrorFullMsg(this, getResources().getString(R.string.validation_all));
            } else {
                if (Common.isCheckNetwork(this)) {
                    callApiEditProfile();
                } else {
                    Common.alertErrorOrValidationDialog(this, getResources().getString(R.string.no_internet));
                }
            }
        });
    }

    private void callApiEditProfile() {
        Common.showLoadingProgress(this);
        Call<SingleResponse> call;
        if (imageFile != null) {
            call = ApiClient.getClient().setProfile(
                    Common.setRequestBody(SharePreference.getStringPref(this, SharePreference.userId)),
                    Common.setRequestBody(editProfileBinding.edtName.getText().toString()),
                    Common.setImageUpload("image", imageFile)
            );
        } else {
            call = ApiClient.getClient().setProfile(
                    Common.setRequestBody(SharePreference.getStringPref(this, SharePreference.userId)),
                    Common.setRequestBody(editProfileBinding.edtName.getText().toString()),
                    null
            );
        }
        call.enqueue(new Callback<SingleResponse>() {
            @Override
            public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {
                if (response.code() == 200) {
                    Common.dismissLoadingProgress();
                    SingleResponse editProfileResponce = response.body();
                    successfulDialog(ActEditProfile.this, editProfileResponce.getMessage());
                    if (editProfileResponce.getStatus().equals("1")) {
                        Common.dismissLoadingProgress();
                        Common.isProfileEdit = true;
                        Common.isProfileMainEdit = true;
                    } else if (editProfileResponce.getStatus().equals("0")) {
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(ActEditProfile.this, editProfileResponce.getMessage());
                    }
                } else {
                    try {
                        String restResponse = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(restResponse);
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(ActEditProfile.this, jsonObject.getString("message"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SingleResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActEditProfile.this, getResources().getString(R.string.error_msg));
            }
        });
    }

    private final ActivityResultLauncher<Intent> startForProfileImageResult =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    //Image Uri will not be null for RESULT_OK
                    try {
                        String filePath = String.valueOf(result.getData());
                        if (filePath != null) {
                            imageFile = new File(filePath);
                            Glide.with(this).load(imageFile).into(editProfileBinding.ivProfile);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
                    Toast.makeText(this, ImagePicker.Companion.getError(result.getData()), Toast.LENGTH_SHORT).show();
                }
            });

    private void callApiProfile(HashMap<String, String> hasmap) {
        Common.showLoadingProgress(this);
        Call<GetProfileResponse> call = ApiClient.getClient().getProfile(hasmap);
        call.enqueue(new Callback<GetProfileResponse>() {
            @Override
            public void onResponse(Call<GetProfileResponse> call, Response<GetProfileResponse> response) {
                if (response.code() == 200) {
                    GetProfileResponse restResponce = response.body();
                    if (restResponce.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        UserData dataResponse = restResponce.getData();
                        setProfileData(dataResponse);
                    } else if (restResponce.getData().equals("0")) {
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(ActEditProfile.this, restResponce.getMessage());
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(ActEditProfile.this, getResources().getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(Call<GetProfileResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActEditProfile.this, getResources().getString(R.string.error_msg));
            }
        });
    }

    private void setProfileData(UserData dataResponse) {
        editProfileBinding.edtEmail.setText(dataResponse.getEmail());
        editProfileBinding.edtName.setText(dataResponse.getName());
        editProfileBinding.edMobileNumber.setText(dataResponse.getMobile());
        Glide.with(this).load(dataResponse.getProfilePic()).placeholder(
                ResourcesCompat.getDrawable(getResources(), R.drawable.profile, null)
        ).into(editProfileBinding.ivProfile);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Common.getCurrentLanguage(this, false);
    }

    // Profile Update Success Dialog
    private void successfulDialog(Activity act, String msg) {
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
            LayoutInflater mInflater = LayoutInflater.from(act);
            View mView = mInflater.inflate(R.layout.dlg_validation, null, false);
            TextView textDesc = mView.findViewById(R.id.tvMessage);
            textDesc.setText(msg);
            TextView tvOk = mView.findViewById(R.id.tvOk);
            Dialog finalDialog = dialog;
            tvOk.setOnClickListener(v -> {
                finalDialog.dismiss();
                setResult(RESULT_OK);
                finish();
            });
            dialog.setContentView(mView);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
