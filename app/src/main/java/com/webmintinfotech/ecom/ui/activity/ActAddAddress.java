package com.webmintinfotech.ecom.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.api.SingleResponse;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.databinding.ActAddAddressBinding;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.Common.*;
import com.webmintinfotech.ecom.utils.SharePreference;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActAddAddress extends BaseActivity {
    private ActAddAddressBinding addAddressBinding;
    private int addressId = 0;
    private int type = 0;
    private boolean isClick = false;

    @Override
    protected View setLayout() {
        addAddressBinding = ActAddAddressBinding.inflate(getLayoutInflater());
        return addAddressBinding.getRoot();
    }

    @Override
    protected void initView() {
        addAddressBinding.ivBack.setOnClickListener(view -> finish());
        addAddressBinding.btnsave.setOnClickListener(view -> {
            if (Common.isCheckNetwork(ActAddAddress.this)) {
                validation();
            } else {
                Common.alertErrorOrValidationDialog(
                        ActAddAddress.this,
                        getResources().getString(R.string.no_internet)
                );
            }
        });

        type = getIntent().getIntExtra("Type", 0);
        if (type == 1) {
            isClick = true;
            addAddressBinding.tvAddressTitle.setText(getResources().getString(R.string.edit_address));
            addAddressBinding.btnsave.setText(getResources().getString(R.string.update_address));
            getData();
        } else {
            addAddressBinding.tvAddressTitle.setText(getResources().getString(R.string.new_address));
            addAddressBinding.btnsave.setText(getResources().getString(R.string.save_address));
        }
    }

    //TODO SET ADDRESS TO EDIT DATA
    private void getData() {
        addressId = getIntent().getIntExtra("address_id", 0);
        addAddressBinding.edFullname.setText(getIntent().getStringExtra("FirstName"));
        addAddressBinding.edLastName.setText(getIntent().getStringExtra("LastName"));
        addAddressBinding.edStreerAddress.setText(getIntent().getStringExtra("StreetAddress"));
        addAddressBinding.edLandmark.setText(getIntent().getStringExtra("Landmark"));
        addAddressBinding.edPostCodeZip.setText(getIntent().getStringExtra("Pincode"));
        addAddressBinding.edPhone.setText(getIntent().getStringExtra("Mobile"));
        addAddressBinding.edtEmailAddress.setText(getIntent().getStringExtra("Email"));
    }

    //TODO ADDRESS VALIDATION
    private void validation() {
        if (addAddressBinding.edFullname.getText().toString().equals("")
                || addAddressBinding.edLastName.getText().toString().equals("")
                || addAddressBinding.edStreerAddress.getText().toString().equals("")
                || addAddressBinding.edLandmark.getText().toString().equals("")
                || addAddressBinding.edPostCodeZip.getText().toString().equals("")
                || addAddressBinding.edPhone.getText().toString().equals("")
                || addAddressBinding.edtEmailAddress.getText().toString().equals("")) {
            Common.showErrorFullMsg(
                    ActAddAddress.this,
                    getResources().getString(R.string.validation_all)
            );
        } else {
            HashMap<String, String> addadrress = new HashMap<>();
            addadrress.put("user_id", SharePreference.getStringPref(ActAddAddress.this, SharePreference.userId));
            addadrress.put("first_name", addAddressBinding.edFullname.getText().toString());
            addadrress.put("last_name", addAddressBinding.edLastName.getText().toString());
            addadrress.put("street_address", addAddressBinding.edStreerAddress.getText().toString());
            addadrress.put("landmark", addAddressBinding.edLandmark.getText().toString());
            addadrress.put("pincode", addAddressBinding.edPostCodeZip.getText().toString());
            addadrress.put("mobile", addAddressBinding.edPhone.getText().toString());
            addadrress.put("email", addAddressBinding.edtEmailAddress.getText().toString());

            if (type == 1) {
                addadrress.put("address_id", String.valueOf(addressId));
                Log.e("request", addadrress.toString());
                callApiUpdateAddress(addadrress);
            } else {
                callApiAddAddress(addadrress);
            }
        }
    }

    //TODO API ADD ADDRESS CALL
    private void callApiAddAddress(HashMap<String, String> addadrress) {
        Common.showLoadingProgress(ActAddAddress.this);
        Call<SingleResponse> call = ApiClient.getClient().addAddress(addadrress);
        call.enqueue(new Callback<SingleResponse>() {
            @Override
            public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {
                Common.dismissLoadingProgress();
                if (response.code() == 200) {
                    if (response.body().getStatus() == 1) {
                        Common.isAddOrUpdated = true;
                        finish();
                    } else {
                        Common.showErrorFullMsg(
                                ActAddAddress.this,
                                response.body().getMessage()
                        );
                    }
                } else {
                    Common.alertErrorOrValidationDialog(
                            ActAddAddress.this,
                            getResources().getString(R.string.error_msg)
                    );
                }
            }

            @Override
            public void onFailure(Call<SingleResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(
                        ActAddAddress.this,
                        getResources().getString(R.string.error_msg)
                );
            }
        });
    }

    //TODO API UPDATE ADDRESS CALL
    private void callApiUpdateAddress(HashMap<String, String> addressRequest) {
        Common.showLoadingProgress(ActAddAddress.this);
        Call<SingleResponse> call = ApiClient.getClient().updateAddress(addressRequest);
        call.enqueue(new Callback<SingleResponse>() {
            @Override
            public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {
                if (response.code() == 200) {
                    Common.dismissLoadingProgress();
                    Common.isAddOrUpdated=true;

                    if (response.body().getStatus() == 1) {
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Common.showErrorFullMsg(
                                ActAddAddress.this,
                                response.body().getMessage()
                        );
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(
                            ActAddAddress.this,
                            getResources().getString(R.string.error_msg)
                    );
                }
            }

            @Override
            public void onFailure(Call<SingleResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(
                        ActAddAddress.this,
                        getResources().getString(R.string.error_msg)
                );
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Common.getCurrentLanguage(ActAddAddress.this, false);
    }
}
