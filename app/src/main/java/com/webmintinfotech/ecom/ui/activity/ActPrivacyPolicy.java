package com.webmintinfotech.ecom.ui.activity;

import android.view.View;
import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.databinding.ActPrivacyPolicyBinding;
import com.webmintinfotech.ecom.model.CmsPageResponse;
import com.webmintinfotech.ecom.utils.Common;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActPrivacyPolicy extends BaseActivity {
    private ActPrivacyPolicyBinding actPrivacyPolicyBinding;

    @Override
    public View setLayout() {
        return actPrivacyPolicyBinding.getRoot();
    }

    @Override
    public void initView() {
        actPrivacyPolicyBinding = ActPrivacyPolicyBinding.inflate(getLayoutInflater());
        actPrivacyPolicyBinding.ivBack.setOnClickListener(view -> finish());

        String type = getIntent().getStringExtra("Type");
        if (type != null) {
            actPrivacyPolicyBinding.tvTitle.setText(type);
        }

        callCmsDataApi();
    }

    private void callCmsDataApi() {
        Common.showLoadingProgress(this);

        Call<CmsPageResponse> call = ApiClient.getClient().getCmsData();
        call.enqueue(new Callback<CmsPageResponse>() {
            @Override
            public void onResponse(Call<CmsPageResponse> call, Response<CmsPageResponse> response) {
                if (response.code() == 200) {
                    CmsPageResponse restResponce = response.body();
                    Common.dismissLoadingProgress();

                    if (restResponce != null && restResponce.getStatus() == 1) {
                        String type = getIntent().getStringExtra("Type");
                        switch (type) {
                            case "Policy":
                                actPrivacyPolicyBinding.tvCmsData.setText(restResponce.getPrivacypolicy());
                                break;
                            case "About":
                                actPrivacyPolicyBinding.tvCmsData.setText(restResponce.getAbout());
                                break;
                            case "Terms Condition":
                                actPrivacyPolicyBinding.tvCmsData.setText(restResponce.getTermsconditions());
                                break;
                        }
                    } else if (restResponce != null && restResponce.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(ActPrivacyPolicy.this, restResponce.getMessage());
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(ActPrivacyPolicy.this, getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(Call<CmsPageResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActPrivacyPolicy.this, getString(R.string.error_msg));
            }
        });
    }
}
