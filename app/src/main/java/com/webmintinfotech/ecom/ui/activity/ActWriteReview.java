package com.webmintinfotech.ecom.ui.activity;

import android.view.View;

import com.bumptech.glide.Glide;
import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.api.SingleResponse;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.databinding.ActWriteReviewBinding;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.SharePreference;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActWriteReview extends BaseActivity {
    private ActWriteReviewBinding writeReviewBinding;
    private String ratting = "";

    @Override
    public View setLayout() {
        return writeReviewBinding.getRoot();
    }

    @Override
    public void initView() {
        writeReviewBinding = ActWriteReviewBinding.inflate(getLayoutInflater());
        writeReviewBinding.ivBack.setOnClickListener(v -> finish());
        writeReviewBinding.tvproductname.setText(getIntent().getStringExtra("proName"));
        Glide.with(ActWriteReview.this)
                .load(getIntent().getStringExtra("proImage"))
                .into(writeReviewBinding.ivproduct);
        writeReviewBinding.btnsubmit.setOnClickListener(v -> callApiAddRattingAndReview());
    }

    private void callApiAddRattingAndReview() {
        if (writeReviewBinding.edtreview.getText().toString().isEmpty()) {
            Common.showErrorFullMsg(this, getResources().getString(R.string.please_write_comment));
        } else {
            Common.showLoadingProgress(this);
            HashMap<String, String> hasmap = new HashMap<>();
            hasmap.put("user_id", SharePreference.getStringPref(this, SharePreference.userId));
            hasmap.put("ratting", ratting);
            hasmap.put("comment", writeReviewBinding.edtreview.getText().toString());
            hasmap.put("vendor_id", getIntent().getStringExtra("vendorsID"));
            hasmap.put("product_id", getIntent().getStringExtra("proID"));
            Call<SingleResponse> call = ApiClient.getClient().addRatting(hasmap);
            call.enqueue(new Callback<SingleResponse>() {
                @Override
                public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {
                    if (response.code() == 200) {
                        Common.dismissLoadingProgress();
                        Common.isAddOrUpdated = true;
                        if (response.body().getStatus() == 1) {
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Common.showErrorFullMsg(ActWriteReview.this, response.body().getMessage());
                        }
                    } else {
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(ActWriteReview.this, getResources().getString(R.string.error_msg));
                    }
                }

                @Override
                public void onFailure(Call<SingleResponse> call, Throwable t) {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(ActWriteReview.this, getResources().getString(R.string.error_msg));
                }
            });
        }
    }
}
