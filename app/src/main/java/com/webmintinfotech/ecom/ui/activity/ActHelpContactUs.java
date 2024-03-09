package com.webmintinfotech.ecom.ui.activity;

import android.view.View;

import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.api.SingleResponse;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.databinding.ActHelpContactUsBinding;
import com.webmintinfotech.ecom.model.ContactInfo;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.SharePreference;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActHelpContactUs extends BaseActivity {
    private ContactInfo contactInfo = new ContactInfo();
    private ActHelpContactUsBinding actHelpContactUsBinding;

    @Override
    public View setLayout() {
        return actHelpContactUsBinding.getRoot();
    }

    @Override
    public void initView() {
        actHelpContactUsBinding = ActHelpContactUsBinding.inflate(getLayoutInflater());
        contactInfo = (ContactInfo) getIntent().getSerializableExtra("contact");

        actHelpContactUsBinding.tvPhoneNumber.setText(contactInfo.getContact());
        actHelpContactUsBinding.tvAddress.setText(contactInfo.getAddress());
        actHelpContactUsBinding.tvEmailAddress.setText(contactInfo.getEmail());

        actHelpContactUsBinding.ivBack.setOnClickListener(v -> finish());
        actHelpContactUsBinding.btnSubmit.setOnClickListener(v -> {
            if (actHelpContactUsBinding.tvFirstName.getText().toString().isEmpty() ||
                    actHelpContactUsBinding.tvLastName.getText().toString().isEmpty() ||
                    actHelpContactUsBinding.edMobile.getText().toString().isEmpty() ||
                    actHelpContactUsBinding.edEmail.getText().toString().isEmpty() ||
                    actHelpContactUsBinding.edSubject.getText().toString().isEmpty() ||
                    actHelpContactUsBinding.edMessage.getText().toString().isEmpty()) {
                Common.showErrorFullMsg(ActHelpContactUs.this, getResources().getString(R.string.validation_all));
            } else {
                HashMap<String, String> map = new HashMap<>();
                map.put("user_id", SharePreference.getStringPref(ActHelpContactUs.this, SharePreference.userId));
                map.put("first_name", actHelpContactUsBinding.tvFirstName.getText().toString());
                map.put("last_name", actHelpContactUsBinding.tvLastName.getText().toString());
                map.put("mobile", actHelpContactUsBinding.edMobile.getText().toString());
                map.put("email", actHelpContactUsBinding.edEmail.getText().toString());
                map.put("subject", actHelpContactUsBinding.edSubject.getText().toString());
                map.put("message", actHelpContactUsBinding.edMessage.getText().toString());
                callHelp(map);
            }
        });
    }

    private void callHelp(HashMap<String, String> hasmap) {
        Common.showLoadingProgress(this);
        Call<SingleResponse> call = ApiClient.getClient().help(hasmap);
        call.enqueue(new Callback<SingleResponse>() {
            @Override
            public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {
                if (response.code() == 200) {
                    Common.dismissLoadingProgress();
                    Common.isAddOrUpdated = true;
                    if (response.body().getStatus() == 1) {
                        Common.showSuccessFullMsg(ActHelpContactUs.this, response.body().getMessage());
                        actHelpContactUsBinding.tvFirstName.getText().clear();
                        actHelpContactUsBinding.tvLastName.getText().clear();
                        actHelpContactUsBinding.edEmail.getText().clear();
                        actHelpContactUsBinding.edMobile.getText().clear();
                        actHelpContactUsBinding.edSubject.getText().clear();
                        actHelpContactUsBinding.edMessage.getText().clear();
                    } else {
                        Common.showErrorFullMsg(ActHelpContactUs.this, response.body().getMessage());
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(ActHelpContactUs.this, getResources().getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(Call<SingleResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActHelpContactUs.this, getResources().getString(R.string.error_msg));
            }
        });
    }
}
