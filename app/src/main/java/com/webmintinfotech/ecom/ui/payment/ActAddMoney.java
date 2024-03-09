package com.webmintinfotech.ecom.ui.payment;

import static com.flutterwave.raveandroid.RavePayActivity.RESULT_SUCCESS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.adapter.PaymentListAdapter;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.api.SingleResponse;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.databinding.ActAddMoneyBinding;
import com.webmintinfotech.ecom.model.FlutterWaveData;
import com.webmintinfotech.ecom.model.FlutterWaveResponse;
import com.webmintinfotech.ecom.model.PaymentListResponse;
import com.webmintinfotech.ecom.model.PaymentlistItem;
import com.webmintinfotech.ecom.ui.activity.ActCardInfo;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.Constants;
import com.webmintinfotech.ecom.utils.SharePreference;
import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.RaveUiManager;
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants;
import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActAddMoney extends BaseActivity implements PaymentResultListener {
    private String paymentName = "";
    private ArrayList<PaymentlistItem> paymentList = new ArrayList<>();
    private PaymentListAdapter paymentAdapter = null;
    private String strRezorPayKey = "";
    private String payStackKey = "";
    private String flutterWaveKey = "";
    private String encryptionKey = "";
    private String logoimg = "";
    private String stripekey = "";
    private String currency = "";
    private String currencyPosition = "";
    private ActAddMoneyBinding addMoneyBinding;

    @Override
    public View setLayout() {
        addMoneyBinding = ActAddMoneyBinding.inflate(getLayoutInflater());
        return addMoneyBinding.getRoot();
    }

    @Override
    public void initView() {
        currency = SharePreference.getStringPref(this, SharePreference.Currency);
        currencyPosition = SharePreference.getStringPref(this, SharePreference.CurrencyPosition);
        Checkout.preload(this);
        initClickListeners();
        setupAdapter();
        callPaymentListApi();
    }

    private void initClickListeners() {
        if (SharePreference.getStringPref(this, SharePreference.SELECTED_LANGUAGE)
                .equals(getResources().getString(R.string.language_hindi))) {
            addMoneyBinding.ivBack.setRotation(180F);
        } else {
            addMoneyBinding.ivBack.setRotation(0F);
        }
        addMoneyBinding.tvwallettype.setText(currency);
        addMoneyBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addMoneyBinding.btnProccedtoPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addMoneyBinding.edAmount.getText().toString().isEmpty() ||
                        addMoneyBinding.edAmount.getText().toString().equals(".")) {
                    Common.showErrorFullMsg(ActAddMoney.this, getResources().getString(R.string.enter_amount));
                } else if (!Common.isValidAmount(addMoneyBinding.edAmount.getText().toString())) {
                    Common.showErrorFullMsg(ActAddMoney.this, getResources().getString(R.string.valid_amount));
                } else if (Double.parseDouble(addMoneyBinding.edAmount.getText().toString()) < 1) {
                    Common.showErrorFullMsg(ActAddMoney.this, getResources().getString(R.string.one_amount));
                } else {
                    switch (paymentName) {
                        case "RazorPay":
                            Common.showLoadingProgress(ActAddMoney.this);
                            razorPayPayment();
                            break;
                        case "Paystack":
                            double amount = Double.parseDouble(addMoneyBinding.edAmount.getText().toString());
                            int totalAmount = (int) (Math.round(amount) * 100);
                            Intent i = new Intent(ActAddMoney.this, ActPayStack.class);
                            i.putExtra("email", SharePreference.getStringPref(ActAddMoney.this, SharePreference.userEmail));
                            i.putExtra("public_key", payStackKey);
                            i.putExtra("amount", String.valueOf(totalAmount));
                            activityResult.launch(i);
                            break;
                        case "Flutterwave":
                            flutterWavePayment();
                            break;
                        case "Stripe":
                            Intent intent = new Intent(ActAddMoney.this, ActCardInfo.class);
                            activityResult.launch(intent);
                            break;
                        default:
                            Common.showErrorFullMsg(ActAddMoney.this, getResources().getString(R.string.payment_type_selection_error));
                            break;
                    }
                }
            }
        });
    }

    private void setupAdapter() {
        paymentAdapter = new PaymentListAdapter(ActAddMoney.this, paymentList, (i, s) -> {
            if (s.equals(Constants.ItemClick)) {
                paymentName = paymentList.get(i).getPaymentName().toString();
            }
        });

        addMoneyBinding.rvpaymentlist.setLayoutManager(new LinearLayoutManager(ActAddMoney.this));
        addMoneyBinding.rvpaymentlist.setItemAnimator(new DefaultItemAnimator());
        addMoneyBinding.rvpaymentlist.setAdapter(paymentAdapter);
    }

    private void callPaymentListApi() {
        Common.showLoadingProgress(ActAddMoney.this);
        HashMap<String, String> hasmap = new HashMap<>();
        hasmap.put("user_id", SharePreference.getStringPref(ActAddMoney.this, SharePreference.userId));
        Call<PaymentListResponse> call = ApiClient.getClient().getPaymentList(hasmap);
        call.enqueue(new Callback<PaymentListResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<PaymentListResponse> call, Response<PaymentListResponse> response) {
                if (response.code() == 200) {
                    PaymentListResponse restResponce = response.body();
                    if (restResponce.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        addMoneyBinding.rvpaymentlist.setVisibility(View.VISIBLE);
                        addMoneyBinding.tvNoDataFound.setVisibility(View.GONE);

                        runOnUiThread(() -> {
                            paymentList.addAll(restResponce.getPaymentlist());

//                            paymentList.removeIf(item ->
//                                    item.getPaymentName().equals("COD") || item.getPaymentName().equals("Wallet")
//                            );

                            paymentAdapter.notifyDataSetChanged();
                            for (int i = 0; i < paymentList.size(); i++) {
                                switch (paymentList.get(i).getPaymentName()) {
                                    case "RazorPay":
                                        strRezorPayKey = (paymentList.get(i).getEnvironment() == 1) ?
                                                paymentList.get(i).getTestPublicKey() :
                                                paymentList.get(i).getLivePublicKey();
                                        break;
                                    case "Stripe":
                                        stripekey = (paymentList.get(i).getEnvironment() == 1) ?
                                                paymentList.get(i).getTestPublicKey().toString() :
                                                paymentList.get(i).getLivePublicKey().toString();
                                        break;
                                    case "Paystack":
                                        payStackKey = (paymentList.get(i).getEnvironment() == 1) ?
                                                paymentList.get(i).getTestPublicKey() :
                                                paymentList.get(i).getLivePublicKey();
                                        break;
                                    case "Flutterwave":
                                        flutterWaveKey = (paymentList.get(i).getEnvironment() == 1) ?
                                                paymentList.get(i).getTestPublicKey() :
                                                paymentList.get(i).getLivePublicKey();
                                        encryptionKey = paymentList.get(i).getEncryptionKey() != null ? paymentList.get(i).getEncryptionKey() : "";
                                        break;
                                }
                            }
                        });
                    } else if (restResponce.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        addMoneyBinding.rvpaymentlist.setVisibility(View.GONE);
                        addMoneyBinding.tvNoDataFound.setVisibility(View.VISIBLE);
                        Common.alertErrorOrValidationDialog(ActAddMoney.this, restResponce.getMessage());
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(ActAddMoney.this, getResources().getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(Call<PaymentListResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActAddMoney.this, getResources().getString(R.string.error_msg));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Common.getCurrentLanguage(ActAddMoney.this, false);
    }

    private void razorPayPayment() {
        Activity activity = this;
        Checkout co = new Checkout();
        try {
            co.setKeyID(strRezorPayKey);
            double amount = Double.parseDouble(addMoneyBinding.edAmount.getText().toString()) * 100;
            Log.d("test", String.valueOf(amount));
            JSONObject options = new JSONObject();
            options.put("name", getResources().getString(R.string.app_name));
            options.put("description", getResources().getString(R.string.order_payment));
            options.put("image", logoimg);
            options.put("currency", "INR");
            options.put("amount", String.format(Locale.US, "%d", (long) amount));
            JSONObject prefill = new JSONObject();
            prefill.put("email", SharePreference.getStringPref(ActAddMoney.this, SharePreference.userEmail));
            prefill.put("contact", SharePreference.getStringPref(ActAddMoney.this, SharePreference.userMobile));
            options.put("prefill", prefill);
            JSONObject theme = new JSONObject();
            theme.put("color", "#366ed4");
            options.put("theme", theme);
            co.open(activity, options);
        } catch (Exception e) {
            Common.dismissLoadingProgress();
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void flutterWavePayment() {
        new RaveUiManager(this)
                .setAmount(Double.parseDouble(addMoneyBinding.edAmount.getText().toString()))
                .setEmail(SharePreference.getStringPref(ActAddMoney.this, SharePreference.userEmail))
                .setfName(SharePreference.getStringPref(ActAddMoney.this, SharePreference.userName))
                .setlName(SharePreference.getStringPref(ActAddMoney.this, SharePreference.userName))
                .setPublicKey(flutterWaveKey)
                .setEncryptionKey(encryptionKey)
                .setCountry("NG")
                .setCurrency("NGN")
                .setTxRef(System.currentTimeMillis() + "Ref")
                .setPhoneNumber(SharePreference.getStringPref(ActAddMoney.this, SharePreference.userMobile), false)
                .acceptMpesaPayments(true)
                .acceptBankTransferPayments(true, true)
                .acceptAccountPayments(true)
                .acceptSaBankPayments(true)
                .acceptBankTransferPayments(true)
                .acceptCardPayments(true)
                .onStagingEnv(false)
                .withTheme(R.style.DefaultPayTheme)
                .allowSaveCardFeature(false, false)
                .initialize();
    }

    @Override
    public void onPaymentSuccess(String razorPayId) {
        Common.dismissLoadingProgress();
        if (razorPayId != null) {
            callAddMoneyToWalletApi(razorPayId, "3");
        }
    }

    @Override
    public void onPaymentError(int error, String response) {
        try {
            Toast.makeText(this, response, Toast.LENGTH_LONG).show();
            Common.dismissLoadingProgress();
        } catch (Exception e) {
            Log.e("Exception", e.getMessage(), e);
        }
    }

    private final ActivityResultLauncher<Intent> activityResult =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        Log.e("code", String.valueOf(result.getResultCode()));
                        Log.e("response", String.valueOf(result.getData()));
                        if (result.getResultCode() == RESULT_OK) {
                            String id = result.getData().getStringExtra("id");
                            callAddMoneyToWalletApi(id, "6");
                            setResult(RESULT_OK);
                        }
                    });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 401) {
            String cardNumber = data != null ? data.getStringExtra("card_number") : "";
            String expMonth = data != null ? data.getStringExtra("exp_month") : "";
            String expYear = data != null ? data.getStringExtra("exp_year") : "";
            String cvv = data != null ? data.getStringExtra("cvv") : "";
            Log.d("CardInfomation", cardNumber + expMonth + expYear + cvv);
            callAddMoneyToWalletToStripe(cardNumber, expMonth, expYear, cvv);
        } else if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            switch (resultCode) {
//                case RESULT_SUCCESS:
//                    Toast.makeText(this, "Transaction Successful", Toast.LENGTH_SHORT).show();
//                    String message = data.getStringExtra("response");
//                    Log.e("message", message != null ? message : "");
//                    FlutterWaveResponse json = new Gson().fromJson(message, FlutterWaveResponse.class);
//                    FlutterWaveData dataValue = json.getData();
//                    String id = dataValue != null ? dataValue.getFlwRef() : "";
//                    callAddMoneyToWalletApi(id, "5");
//                    break;
//                case RavePayActivity.RESULT_ERROR:
//                    Toast.makeText(this, "An Error Occur", Toast.LENGTH_SHORT).show();
//                    break;
//                case RavePayActivity.RESULT_CANCELLED:
//                    Toast.makeText(this, "Transaction Canceled", Toast.LENGTH_SHORT).show();
//                    break;
            }
        }
    }


    private void callAddMoneyToWalletApi(String transactionId, String paymentType) {
        Common.showLoadingProgress(ActAddMoney.this);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("user_id", SharePreference.getStringPref(ActAddMoney.this, SharePreference.userId));
        hashMap.put("recharge_amount", addMoneyBinding.edAmount.getText().toString());
        hashMap.put("payment_id", transactionId);
        hashMap.put("payment_type", paymentType);
        Log.d("addmoney", hashMap.toString());
        Call<SingleResponse> call = ApiClient.getClient().addMoney(hashMap);
        call.enqueue(new Callback<SingleResponse>() {
            @Override
            public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {
                if (response.code() == 200) {
                    Common.dismissLoadingProgress();
                    Common.isAddOrUpdated = true;
                    if (response.body() != null && response.body().getStatus() == 1) {
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Common.showErrorFullMsg(ActAddMoney.this,
                                (response.body() != null) ? response.body().getMessage() : "");
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(ActAddMoney.this, getResources().getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(Call<SingleResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActAddMoney.this, getResources().getString(R.string.error_msg));
            }
        });
    }

    private void callAddMoneyToWalletToStripe(String cardNumber, String expMonth, String expYear, String cvv) {
        Common.showLoadingProgress(ActAddMoney.this);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("user_id", SharePreference.getStringPref(ActAddMoney.this, SharePreference.userId));
        hashMap.put("recharge_amount", addMoneyBinding.edAmount.getText().toString());
        hashMap.put("payment_id", "");
        hashMap.put("payment_type", "4");
        hashMap.put("card_number", cardNumber);
        hashMap.put("card_exp_month", expMonth);
        hashMap.put("card_exp_year", expYear);
        hashMap.put("card_cvc", cvv);
        Log.d("addmoney", hashMap.toString());
        Call<SingleResponse> call = ApiClient.getClient().addMoney(hashMap);
        call.enqueue(new Callback<SingleResponse>() {
            @Override
            public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {
                if (response.code() == 200) {
                    Common.dismissLoadingProgress();
                    Common.isAddOrUpdated = true;
                    if (response.body() != null && response.body().getStatus() == 1) {
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Common.showErrorFullMsg(ActAddMoney.this,
                                (response.body() != null) ? response.body().getMessage() : "");
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(ActAddMoney.this, getResources().getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(Call<SingleResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActAddMoney.this, getResources().getString(R.string.error_msg));
            }
        });
    }
}