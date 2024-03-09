package com.webmintinfotech.ecom.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.api.SingleResponse;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.base.BaseAdaptor;
import com.webmintinfotech.ecom.databinding.ActAddressBinding;
import com.webmintinfotech.ecom.databinding.RemoveItemDialogBinding;
import com.webmintinfotech.ecom.databinding.RowAddressBinding;
import com.webmintinfotech.ecom.model.AddressData;
import com.webmintinfotech.ecom.model.GetAddressResponse;
import com.webmintinfotech.ecom.ui.authentication.ActLogin;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.SharePreference;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActAddress extends BaseActivity {
    private ActAddressBinding addressBinding;
    private ArrayList<AddressData> addressDataList;
    private BaseAdaptor<AddressData, RowAddressBinding> addressDataAdapter;

    @Override
    public View setLayout() {
        return addressBinding.getRoot();
    }

    @Override
    public void initView() {
        addressBinding = ActAddressBinding.inflate(getLayoutInflater());
        if (SharePreference.getBooleanPref(this, SharePreference.isLogin)) {
            if (Common.isCheckNetwork(this)) {
                callApiAddress();
            } else {
                Common.alertErrorOrValidationDialog(this, getResources().getString(R.string.no_internet));
            }
        } else {
            openActivity(ActLogin.class);
            finish();
            finishAffinity();
        }
        addressBinding.ivAdd.setOnClickListener(v -> openActivity(ActAddAddress.class));
        addressBinding.ivBack.setOnClickListener(v -> finish());
    }

    private void callApiAddress() {
        Common.showLoadingProgress(this);
        HashMap<String, String> hasmap = new HashMap<>();
        hasmap.put("user_id", SharePreference.getStringPref(this, SharePreference.userId));

        Call<GetAddressResponse> call = ApiClient.getClient().getAddress(hasmap);
        call.enqueue(new Callback<GetAddressResponse>() {
            @Override
            public void onResponse(@NotNull Call<GetAddressResponse> call, @NotNull Response<GetAddressResponse> response) {
                if (response.code() == 200) {
                    GetAddressResponse restResponce = response.body();
                    if (restResponce != null) {
                        if (restResponce.getStatus() == 1) {
                            Common.dismissLoadingProgress();
                            addressDataList = restResponce.getData();
                            if (addressDataList != null && !addressDataList.isEmpty()) {
                                setAddressData(addressDataList);
                                addressBinding.rvaddress.setVisibility(View.VISIBLE);
                                addressBinding.tvNoDataFound.setVisibility(View.GONE);
                            } else {
                                addressBinding.rvaddress.setVisibility(View.GONE);
                                addressBinding.tvNoDataFound.setVisibility(View.VISIBLE);
                            }
                        } else if (restResponce.getStatus() == 0) {
                            Common.dismissLoadingProgress();
                            Common.alertErrorOrValidationDialog(ActAddress.this, restResponce.getMessage());
                        }
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(ActAddress.this, getResources().getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(@NotNull Call<GetAddressResponse> call, @NotNull Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActAddress.this, getResources().getString(R.string.error_msg));
            }
        });
    }

    private void setAddressData(ArrayList<AddressData> addressDataList) {
        addressDataAdapter = new BaseAdaptor<AddressData, RowAddressBinding>(this, addressDataList) {
            @Override
            public void onBindData(@NonNull RecyclerView.ViewHolder holder, AddressData val, int position) {
                RowAddressBinding binding = (RowAddressBinding) ((ViewHolder) holder).getBinding();
                binding.tvUserName.setText(addressDataList.get(position).getFirstName() + " " + addressDataList.get(position).getLastName());
                binding.tvareaaddress.setText(addressDataList.get(position).getStreetAddress() + ", " + addressDataList.get(position).getLandmark() + " - " + addressDataList.get(position).getPincode());
                binding.tvUserphone.setText(addressDataList.get(position).getMobile());
                binding.tvusermailid.setText(addressDataList.get(position).getEmail());
                binding.tvDelete.setOnClickListener(v -> removeAddressDialog(addressDataList.get(position).getId().toString(), position));
                binding.tvEdit.setOnClickListener(v -> {
                    Intent intent = new Intent(ActAddress.this, ActAddAddress.class);
                    intent.putExtra("FirstName", addressDataList.get(position).getFirstName());
                    intent.putExtra("LastName", addressDataList.get(position).getLastName());
                    intent.putExtra("StreetAddress", addressDataList.get(position).getStreetAddress());
                    intent.putExtra("Landmark", addressDataList.get(position).getLandmark());
                    intent.putExtra("Pincode", addressDataList.get(position).getPincode());
                    intent.putExtra("Mobile", addressDataList.get(position).getMobile());
                    intent.putExtra("Email", addressDataList.get(position).getEmail());
                    intent.putExtra("address_id", addressDataList.get(position).getId());
                    intent.putExtra("Type", 1);
                    startActivity(intent);
                });
                boolean isComeFromSelectAddress = getIntent().getBooleanExtra("isComeFromSelectAddress", false);
                binding.swipe.getSurfaceView().setOnClickListener(v -> {
                    if (isComeFromSelectAddress) {
                        Intent intent = new Intent(ActAddress.this, ActCheckout.class);
                        intent.putExtra("FirstName", addressDataList.get(position).getFirstName());
                        intent.putExtra("LastName", addressDataList.get(position).getLastName());
                        intent.putExtra("StreetAddress", addressDataList.get(position).getStreetAddress());
                        intent.putExtra("Landmark", addressDataList.get(position).getLandmark());
                        intent.putExtra("Pincode", addressDataList.get(position).getPincode());
                        intent.putExtra("Mobile", addressDataList.get(position).getMobile());
                        intent.putExtra("Email", addressDataList.get(position).getEmail());
                        intent.putExtra("address_id", addressDataList.get(position).getId());
                        intent.putExtra("Type", 1);
                        setResult(500, intent);
                        finish();
                    }
                });
            }

            @Override
            public int setItemLayout() {
                return R.layout.row_address;
            }

            @Override
            public RowAddressBinding getBinding(ViewGroup parent) {
                return RowAddressBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            }
        };

        addressBinding.rvaddress.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        addressBinding.rvaddress.setItemAnimator(new DefaultItemAnimator());
        addressBinding.rvaddress.setAdapter(addressDataAdapter);
    }

    public void removeAddressDialog(String addressId, int pos) {
        RemoveItemDialogBinding removeDialogBinding = RemoveItemDialogBinding.inflate(getLayoutInflater());
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(removeDialogBinding.getRoot());
        removeDialogBinding.tvRemoveTitle.setText(getResources().getString(R.string.remove_address));
        removeDialogBinding.tvAlertMessage.setText(getResources().getString(R.string.remove_address_desc));
        removeDialogBinding.btnProceed.setOnClickListener(v -> {
            if (Common.isCheckNetwork(this)) {
                dialog.dismiss();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("user_id", SharePreference.getStringPref(this, SharePreference.userId));
                hashMap.put("address_id", addressId);
                callDeleteApi(hashMap, pos);
            } else {
                Common.alertErrorOrValidationDialog(this, getResources().getString(R.string.no_internet));
            }
        });

        removeDialogBinding.ivClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void callDeleteApi(HashMap<String, String> deleteRequest, int pos) {
        Common.showLoadingProgress(this);
        Call<SingleResponse> call = ApiClient.getClient().deleteAddress(deleteRequest);
        call.enqueue(new Callback<SingleResponse>() {
            @Override
            public void onResponse(@NotNull Call<SingleResponse> call, @NotNull Response<SingleResponse> response) {
                Common.dismissLoadingProgress();
                if (response.code() == 200) {
                    SingleResponse responseBody = response.body();
                    if (responseBody != null) {
                        if (responseBody.getStatus() == 1) {
                            Common.showSuccessFullMsg(ActAddress.this, responseBody.getMessage());
                            addressDataList.remove(pos);
                            addressDataAdapter.notifyDataSetChanged();
                            if (addressDataList.size() > 0) {
                                addressBinding.tvNoDataFound.setVisibility(View.GONE);
                                addressBinding.rvaddress.setVisibility(View.VISIBLE);
                            } else {
                                addressBinding.tvNoDataFound.setVisibility(View.VISIBLE);
                                addressBinding.rvaddress.setVisibility(View.GONE);
                            }
                        } else {
                            Common.showErrorFullMsg(ActAddress.this, responseBody.getMessage());
                        }
                    }
                } else {
                    Common.alertErrorOrValidationDialog(ActAddress.this, getResources().getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(@NotNull Call<SingleResponse> call, @NotNull Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActAddress.this, getResources().getString(R.string.error_msg));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        callApiAddress();
        Common.getCurrentLanguage(this, false);
    }
}
