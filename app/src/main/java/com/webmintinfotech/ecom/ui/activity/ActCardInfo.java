package com.webmintinfotech.ecom.ui.activity;

import android.content.Intent;
import android.view.View;
import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.databinding.ActCardInfoBinding;
import com.webmintinfotech.ecom.utils.Common;

public class ActCardInfo extends BaseActivity {
    private ActCardInfoBinding actCardInfoBinding;

    @Override
    public View setLayout() {
        return actCardInfoBinding.getRoot();
    }

    @Override
    public void initView() {
        actCardInfoBinding = ActCardInfoBinding.inflate(getLayoutInflater());
        startCheckout();
        actCardInfoBinding.ivClose.setOnClickListener(v -> finish());
    }

    private void startCheckout() {
        actCardInfoBinding.btnSubmit.setOnClickListener(v -> {
            if (actCardInfoBinding.edHolderName.getText().toString().isEmpty()) {
                Common.showSuccessFullMsg(ActCardInfo.this, getResources().getString(R.string.validation_all));
            } else if (actCardInfoBinding.edCardNumber.getText().toString().isEmpty()) {
                Common.showSuccessFullMsg(ActCardInfo.this, getResources().getString(R.string.validation_all));
            } else if (actCardInfoBinding.edCardNumber.getText().length() < 16) {
                Common.showSuccessFullMsg(ActCardInfo.this, "Please enter valid card detail");
            } else if (actCardInfoBinding.etMonth.getText().toString().isEmpty()) {
                Common.showSuccessFullMsg(ActCardInfo.this, getResources().getString(R.string.validation_all));
            } else if (actCardInfoBinding.etYear.getText().toString().isEmpty()) {
                Common.showSuccessFullMsg(ActCardInfo.this, getResources().getString(R.string.validation_all));
            } else if (actCardInfoBinding.etCvv.getText().toString().isEmpty()) {
                Common.showSuccessFullMsg(ActCardInfo.this, getResources().getString(R.string.validation_all));
            } else if (actCardInfoBinding.etCvv.getText().length() < 3) {
                Common.showSuccessFullMsg(ActCardInfo.this, "please enter valid CVV");
            } else {
                Intent intent = new Intent();
                intent.putExtra("card_number", actCardInfoBinding.edCardNumber.getText().toString());
                intent.putExtra("exp_month", actCardInfoBinding.etMonth.getText().toString());
                intent.putExtra("exp_year", actCardInfoBinding.etYear.getText().toString());
                intent.putExtra("cvv", actCardInfoBinding.etCvv.getText().toString());
                setResult(401, intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Common.getCurrentLanguage(ActCardInfo.this, false);
    }
}
