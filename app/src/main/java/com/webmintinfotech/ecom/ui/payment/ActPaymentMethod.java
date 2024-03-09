package com.webmintinfotech.ecom.ui.payment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.model.FlutterWaveData;
import com.webmintinfotech.ecom.ui.activity.ActCardInfo;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.api.SingleResponse;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.base.BaseAdaptor;
import com.webmintinfotech.ecom.databinding.ActPaymentMethodBinding;
import com.webmintinfotech.ecom.databinding.RowPaymentBinding;
import com.webmintinfotech.ecom.model.FlutterWaveResponse;
import com.webmintinfotech.ecom.model.PaymentListResponse;
import com.webmintinfotech.ecom.model.PaymentlistItem;
import com.webmintinfotech.ecom.ui.authentication.ActLogin;
import com.webmintinfotech.ecom.utils.Common;
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
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static com.webmintinfotech.ecom.utils.Common.alertErrorOrValidationDialog;
import static com.webmintinfotech.ecom.utils.Common.dismissLoadingProgress;
import static com.webmintinfotech.ecom.utils.Common.isCheckNetwork;
import static com.webmintinfotech.ecom.utils.Common.showErrorFullMsg;
import static com.webmintinfotech.ecom.utils.Common.showLoadingProgress;
import static com.webmintinfotech.ecom.utils.Common.showSuccessFullMsg;
import static com.webmintinfotech.ecom.utils.SharePreference.getStringPref;
import static com.webmintinfotech.ecom.utils.SharePreference.userEmail;
import static com.webmintinfotech.ecom.utils.SharePreference.userId;
import static com.webmintinfotech.ecom.utils.SharePreference.userName;

public class ActPaymentMethod extends BaseActivity implements PaymentResultListener {
    private ActPaymentMethodBinding actPaymentMethodBinding;
    private ArrayList<PaymentlistItem> paymentDataList = null;
    private String currency = "";
    private String currencyPosition = "";
    private String strGetData = "";
    private String walletAmount = "";
    private String logoimg = "";
    private String strRezorPayKey = "";
    private double price = 0.0;
    private String type = "";
    private String fname = "";
    private String landmark = "";
    private String email = "";
    private String pincode = "";
    private String mobile = "";
    private String streetAddress = "";
    private String coupon_name = "";
    private String vendorid = "";
    private String discountAmount = "";
    private String orderNote = "";
    private String flutterWaveKey = "";
    private String stripeKey = "";
    private String encryptionKey = "";
    private String payStackKey = "";

    @Override
    public View setLayout() {
        return actPaymentMethodBinding.getRoot();
    }

    @Override
    public void initView() {
        actPaymentMethodBinding = ActPaymentMethodBinding.inflate(getLayoutInflater());
        Checkout.preload(this);

        actPaymentMethodBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                setResult(RESULT_OK);
            }
        });

        currency = getStringPref(this, SharePreference.Currency);
        currencyPosition = getStringPref(this, SharePreference.CurrencyPosition);

        if (isCheckNetwork(this)) {
            if (SharePreference.getBooleanPref(this, SharePreference.isLogin)) {
                callApiPaymentDetail();
            } else {
                openActivity(ActLogin.class);
                finish();
            }
        } else {
            alertErrorOrValidationDialog(this, getResources().getString(R.string.no_internet));
        }

        actPaymentMethodBinding.btnProccedtoPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payNow();
            }
        });

        fname = Objects.requireNonNull(getIntent().getStringExtra("fname")) + " " + getIntent().getStringExtra("lname");
        landmark = getIntent().getStringExtra("landmark") != null ? getIntent().getStringExtra("landmark") : "";
        mobile = getIntent().getStringExtra("mobile");
        orderNote = getIntent().getStringExtra("order_notes") != null ? getIntent().getStringExtra("order_notes") : "0";
        price = Double.parseDouble(Objects.requireNonNull(getIntent().getStringExtra("grand_total")));
        pincode = getIntent().getStringExtra("pincode");
        streetAddress = getIntent().getStringExtra("street_address");
        coupon_name = getIntent().getStringExtra("coupon_name") != null ? getIntent().getStringExtra("coupon_name") : "0";
        discountAmount = getIntent().getStringExtra("discount_amount") != null ? getIntent().getStringExtra("discount_amount") : "0.0";
        vendorid = getIntent().getStringExtra("vendorid");
    }

    //TODO CALL PAYMENT DETAILS API
    private void callApiPaymentDetail() {
        showLoadingProgress(this);

        HashMap<String, String> hasmap = new HashMap<>();
        hasmap.put("user_id", getStringPref(this, userId));

        Call<PaymentListResponse> call = ApiClient.getClient().getPaymentList(hasmap);
        call.enqueue(new Callback<PaymentListResponse>() {
            @Override
            public void onResponse(Call<PaymentListResponse> call, Response<PaymentListResponse> response) {
                if (response.code() == 200) {
                    PaymentListResponse restResponce = response.body();
                    if (restResponce.getStatus() == 1) {
                        dismissLoadingProgress();
                        actPaymentMethodBinding.rvpaymentlist.setVisibility(View.VISIBLE);
                        paymentDataList = restResponce.getPaymentlist();
                        walletAmount = String.valueOf(restResponce.getWalletamount());
                        loadPaymentDetails(paymentDataList, walletAmount);
                    } else if (restResponce.getStatus() == 0) {
                        dismissLoadingProgress();
                        actPaymentMethodBinding.rvpaymentlist.setVisibility(GONE);
                        alertErrorOrValidationDialog(ActPaymentMethod.this, restResponce.getMessage());
                    }
                } else {
                    dismissLoadingProgress();
                    alertErrorOrValidationDialog(ActPaymentMethod.this, getResources().getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(Call<PaymentListResponse> call, Throwable t) {
                dismissLoadingProgress();
                alertErrorOrValidationDialog(ActPaymentMethod.this, getResources().getString(R.string.error_msg));
            }
        });
    }

    //TODO PAYMENT DETAILS DATA SET
    private void loadPaymentDetails(ArrayList<PaymentlistItem> paymentDataList, String walletamount) {
        final com.webmintinfotech.ecom.databinding.RowPaymentBinding[] binding = {null};
        BaseAdaptor<PaymentlistItem, RowPaymentBinding> viewAllDataAdapter = new BaseAdaptor<PaymentlistItem, RowPaymentBinding>(this, paymentDataList) {
            @SuppressLint({"NewApi", "ResourceType", "SetTextI18n"})
            @Override
            public void onBindData(RecyclerView.ViewHolder holder, PaymentlistItem val, int position) {
                Objects.requireNonNull(holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (PaymentlistItem item : paymentDataList) {
                            item.setIsSelect(false);
                        }
                        strGetData = paymentDataList.get(position).getPaymentName();
                        Log.e("paymentname", strGetData);

                        paymentDataList.get(position).setIsSelect(true);
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadPaymentDetails(paymentDataList, walletamount);
                            }
                        }, 10L);
                    }
                });

                binding[0].tvpaymenttype.setText(paymentDataList.get(position).getPaymentName());
                if (paymentDataList.get(position).getIsSelect()) {
                    binding[0].ivCheck.setVisibility(View.VISIBLE);
                } else {
                    binding[0].ivCheck.setVisibility(View.GONE);
                }

                switch (paymentDataList.get(position).getPaymentName()) {
                    case "Wallet":
                        binding[0].ivPayment.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_wallet, null));
                        if (walletAmount.isEmpty()) {
                            binding[0].tvpaymenttype.setText(getString(R.string.walletpayment) + "(" +
                                    Common.getPrice(currencyPosition, currency, "0") + ")");
                        } else {
                            binding[0].tvpaymenttype.setText(getString(R.string.walletpayment) + "(" +
                                    Common.getPrice(currencyPosition, currency, walletamount) + ")");
                        }
                        break;

                    case "COD":
                        binding[0].ivPayment.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_codpayment, null));
                        binding[0].tvpaymenttype.setText(getString(R.string.cashpayment));
                        break;

                    case "RazorPay":
                        if (paymentDataList.get(position).getEnvironment() == 1) {
                            strRezorPayKey = paymentDataList.get(position).getTestPublicKey();
                        } else {
                            strRezorPayKey = paymentDataList.get(position).getLivePublicKey();
                        }
                        binding[0].ivPayment.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_rezorpaypayment, null));
                        binding[0].tvpaymenttype.setText(getString(R.string.razorpaypayment));
                        break;

                    case "Stripe":
                        binding[0].ivPayment.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_stripepayment, null));
                        binding[0].tvpaymenttype.setText(getString(R.string.stripepayment));
                        break;

                    case "Flutterwave":
                        encryptionKey = paymentDataList.get(position).getEncryptionKey();
                        if (paymentDataList.get(position).getEnvironment() == 1) {
                            flutterWaveKey = paymentDataList.get(position).getTestPublicKey();
                        } else {
                            flutterWaveKey = paymentDataList.get(position).getLivePublicKey();
                        }
                        binding[0].ivPayment.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_flutterwavepayment, null));
                        binding[0].tvpaymenttype.setText(getString(R.string.flutterwaveepayment));
                        break;

                    case "Paystack":
                        if (paymentDataList.get(position).getEnvironment() == 1) {
                            payStackKey = paymentDataList.get(position).getTestPublicKey();
                        } else {
                            payStackKey = paymentDataList.get(position).getLivePublicKey();
                        }
                        binding[0].ivPayment.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_paystackpayment, null));
                        binding[0].tvpaymenttype.setText(getString(R.string.paystackpayment));
                        break;
                }
            }

            @Override
            public int setItemLayout() {
                return R.layout.row_payment;
            }

            @Override
            public RowPaymentBinding getBinding(ViewGroup parent) {
                binding[0] = RowPaymentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return binding[0];
            }
        };


        if (paymentDataList.size() > 0) {
            actPaymentMethodBinding.rvpaymentlist.setVisibility(View.VISIBLE);
            actPaymentMethodBinding.tvNoDataFound.setVisibility(View.GONE);
            actPaymentMethodBinding.rvpaymentlist.setLayoutManager(new LinearLayoutManager(ActPaymentMethod.this, LinearLayoutManager.VERTICAL, false));
            actPaymentMethodBinding.rvpaymentlist.setItemAnimator(new DefaultItemAnimator());
            actPaymentMethodBinding.rvpaymentlist.setAdapter(viewAllDataAdapter);
        } else {
            actPaymentMethodBinding.rvpaymentlist.setVisibility(GONE);
            actPaymentMethodBinding.tvNoDataFound.setVisibility(View.VISIBLE);
        }

    }

    // TODO ORDER PAYMENT METHOD CALL
    private void payNow() {
        if ("COD".equals(strGetData)) {
            if (isCheckNetwork(ActPaymentMethod.this)) {
                showLoadingProgress(ActPaymentMethod.this);
                HashMap<String, String> hasmap = new HashMap<>();
                hasmap.put("user_id", getStringPref(ActPaymentMethod.this, userId));
                hasmap.put("email", getStringPref(ActPaymentMethod.this, userEmail));
                hasmap.put("full_name", Objects.requireNonNull(getIntent().getStringExtra("fname")) + " " + getIntent().getStringExtra("lname"));
                hasmap.put("landmark", getIntent().getStringExtra("landmark") != null ? getIntent().getStringExtra("landmark") : "");
                hasmap.put("mobile", getIntent().getStringExtra("mobile"));
                hasmap.put("order_notes", getIntent().getStringExtra("order_notes") != null ? getIntent().getStringExtra("order_notes") : "0");
                hasmap.put("grand_total", getIntent().getStringExtra("grand_total"));
                hasmap.put("payment_type", "1");
                hasmap.put("pincode", getIntent().getStringExtra("pincode"));
                hasmap.put("street_address", getIntent().getStringExtra("street_address"));
                hasmap.put("coupon_name", getIntent().getStringExtra("coupon_name") != null ? getIntent().getStringExtra("coupon_name") : "0");
                hasmap.put("discount_amount", getIntent().getStringExtra("discount_amount") != null ? getIntent().getStringExtra("discount_amount") : "0.0");
                hasmap.put("vendor_id", getIntent().getStringExtra("vendorid"));
                Log.d("HadMap", hasmap.toString());
                callApiOrder(hasmap);
            } else {
                alertErrorOrValidationDialog(ActPaymentMethod.this, getResources().getString(R.string.no_internet));
            }
        } else if ("Wallet".equals(strGetData)) {
            if (Double.parseDouble(walletAmount) >= Double.parseDouble(getIntent().getStringExtra("grand_total"))) {
                if (isCheckNetwork(ActPaymentMethod.this)) {
                    showLoadingProgress(ActPaymentMethod.this);
                    HashMap<String, String> hasmap = new HashMap<>();
                    hasmap.put("user_id", getStringPref(ActPaymentMethod.this, userId));
                    hasmap.put("email", getStringPref(ActPaymentMethod.this, userEmail));
                    hasmap.put("full_name", Objects.requireNonNull(getIntent().getStringExtra("fname")) + " " + getIntent().getStringExtra("lname"));
                    hasmap.put("landmark", getIntent().getStringExtra("landmark") != null ? getIntent().getStringExtra("landmark") : "");
                    hasmap.put("mobile", getIntent().getStringExtra("mobile"));
                    hasmap.put("order_notes", getIntent().getStringExtra("order_notes") != null ? getIntent().getStringExtra("order_notes") : "0");
                    hasmap.put("grand_total", getIntent().getStringExtra("grand_total"));
                    hasmap.put("payment_type", "2");
                    hasmap.put("pincode", getIntent().getStringExtra("pincode"));
                    hasmap.put("street_address", getIntent().getStringExtra("street_address"));
                    hasmap.put("coupon_name", getIntent().getStringExtra("coupon_name") != null ? getIntent().getStringExtra("coupon_name") : "0");
                    hasmap.put("discount_amount", getIntent().getStringExtra("discount_amount"));
                    hasmap.put("vendor_id", getIntent().getStringExtra("vendorid"));
                    Log.d("HadMap", hasmap.toString());
                    callApiOrder(hasmap);
                } else {
                    alertErrorOrValidationDialog(ActPaymentMethod.this, getResources().getString(R.string.no_internet));
                }
            } else {
                showErrorFullMsg(ActPaymentMethod.this, "You don't have sufficient amount in your wallet to place this order.");
            }
        } else if ("RazorPay".equals(strGetData)) {
            if (isCheckNetwork(ActPaymentMethod.this)) {
                showLoadingProgress(ActPaymentMethod.this);
                startPayment();
            } else {
                alertErrorOrValidationDialog(ActPaymentMethod.this, getResources().getString(R.string.no_internet));
            }
        } else if ("Stripe".equals(strGetData)) {
            Intent intent = new Intent(ActPaymentMethod.this, ActCardInfo.class);
            startActivityForResult(getIntent(), 401);
            Log.e("data", getIntent().toString());
        } else if ("Flutterwave".equals(strGetData)) {
            flutterWavePayment();
        } else if ("Paystack".equals(strGetData)) {
            String amount = getIntent().getStringExtra("grand_total") != null ? getIntent().getStringExtra("grand_total") : "0.00";
            int totalAmount = (int) Math.round(Double.parseDouble(amount) * 100);
            Intent i = new Intent(ActPaymentMethod.this, ActPayStack.class);
            i.putExtra("email", getStringPref(ActPaymentMethod.this, userEmail));
            i.putExtra("public_key", payStackKey);
            i.putExtra("amount", String.valueOf(totalAmount));
            startActivityForResult(i, 500);
        } else if ("".equals(strGetData)) {
            showErrorFullMsg(ActPaymentMethod.this, getResources().getString(R.string.payment_type_selection_error));
        }
    }

    // TODO START FLUTTER WAVE PAYMENT
    private void flutterWavePayment() {
        new RaveUiManager(this)
                .setAmount(Double.parseDouble(getIntent().getStringExtra("grand_total")))
                .setEmail(getStringPref(ActPaymentMethod.this, userEmail))
                .setfName(getStringPref(ActPaymentMethod.this, userName))
                .setlName(getStringPref(ActPaymentMethod.this, userName))
                .setPublicKey(flutterWaveKey)
                .setEncryptionKey(encryptionKey)
                .setCountry("NG")
                .setCurrency("NGN")
                .setTxRef(System.currentTimeMillis() + "Ref")
                .setPhoneNumber(getStringPref(ActPaymentMethod.this, SharePreference.userMobile), false)
                .acceptMpesaPayments(true)
                .acceptBankTransferPayments(true, true)
                .acceptAccountPayments(true)
                .acceptSaBankPayments(true)
                .acceptBankTransferPayments(true)
                .acceptCardPayments(true)
                .onStagingEnv(false)
                .allowSaveCardFeature(true, false)
                .withTheme(R.style.DefaultPayTheme)
                .initialize();
    }

    // TODO START RAZORPAY PAYMENT
    private void startPayment() {
        Common.getLog("test", getIntent().getStringExtra("grand_total"));
        Activity activity = this;
        Checkout co = new Checkout();
        try {
            co.setKeyID(strRezorPayKey);
            double amount = Double.parseDouble(Objects.requireNonNull(getIntent().getStringExtra("grand_total"))) * 100;
            Common.getLog("test", String.valueOf(amount));
            JSONObject options = new JSONObject();
            options.put("name", getResources().getString(R.string.app_name));
            options.put("description", getResources().getString(R.string.order_payment));
            options.put("image", logoimg);
            options.put("currency", "INR");
            options.put("amount", String.format(Locale.US, "%d", (long) amount));
            JSONObject prefill = new JSONObject();
            prefill.put("email", getStringPref(ActPaymentMethod.this, userEmail));
            prefill.put("contact", getStringPref(ActPaymentMethod.this, SharePreference.userMobile));
            options.put("prefill", prefill);
            JSONObject theme = new JSONObject();
            theme.put("color", "#366ed4");
            options.put("theme", theme);
            co.open(activity, options);
        } catch (Exception e) {
            dismissLoadingProgress();
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    // TODO RAZORPAY SUCCESS METHOD
    @Override
    public void onPaymentSuccess(String razorpayPaymentId) {
        try {
            HashMap<String, String> hasmap = new HashMap<>();
            hasmap.put("user_id", getStringPref(ActPaymentMethod.this, userId));
            hasmap.put("email", getStringPref(ActPaymentMethod.this, userEmail));
            hasmap.put("full_name", Objects.requireNonNull(getIntent().getStringExtra("fname")) + " " + getIntent().getStringExtra("lname"));
            hasmap.put("landmark", getIntent().getStringExtra("landmark") != null ? getIntent().getStringExtra("landmark") : "");
            hasmap.put("mobile", getIntent().getStringExtra("mobile"));
            hasmap.put("order_notes", getIntent().getStringExtra("order_notes") != null ? getIntent().getStringExtra("order_notes") : "0");
            hasmap.put("grand_total", getIntent().getStringExtra("grand_total"));
            hasmap.put("payment_id", razorpayPaymentId);
            hasmap.put("payment_type", "3");
            hasmap.put("pincode", getIntent().getStringExtra("pincode"));
            hasmap.put("street_address", getIntent().getStringExtra("street_address"));
            hasmap.put("coupon_name", getIntent().getStringExtra("coupon_name") != null ? getIntent().getStringExtra("coupon_name") : "0");
            hasmap.put("discount_amount", getIntent().getStringExtra("discount_amount"));
            hasmap.put("vendor_id", getIntent().getStringExtra("vendorid"));
            callApiOrder(hasmap);
        } catch (Exception e) {
            Log.e("Exception", "Exception in onPaymentSuccess", e);
        }
    }

    // TODO ERROE MESSAHE PAYMENT GETAWAY
    @Override
    public void onPaymentError(int errorCode, String response) {
        try {
            Toast.makeText(this, response, Toast.LENGTH_LONG).show();
            dismissLoadingProgress();
        } catch (Exception e) {
            Log.e("Exception", e.getMessage(), e);
        }
    }

    // TODO CALL ORDER API
    private void callApiOrder(HashMap<String, String> hasmap) {
        showLoadingProgress(ActPaymentMethod.this);
        Call<SingleResponse> call = ApiClient.getClient().setOrderPayment(hasmap);
        call.enqueue(new Callback<SingleResponse>() {
            @Override
            public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {
                if (response.code() == 200) {
                    dismissLoadingProgress();
                    Common.isAddOrUpdated = true;
                    if (response.body() != null && response.body().getStatus() == 1) {
                        showSuccessFullMsg(ActPaymentMethod.this, response.body().getMessage());
                        openActivity(ActPaymentSuccessFull.class);
                    } else {
                        showErrorFullMsg(ActPaymentMethod.this, response.body() != null ? response.body().getMessage() : "");
                    }
                } else {
                    dismissLoadingProgress();
                    alertErrorOrValidationDialog(ActPaymentMethod.this, getResources().getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(Call<SingleResponse> call, Throwable t) {
                dismissLoadingProgress();
                alertErrorOrValidationDialog(ActPaymentMethod.this, getResources().getString(R.string.error_msg));
            }
        });
    }

    // TODO ACTIVITY RESULT
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 500) {
                showLoadingProgress(ActPaymentMethod.this);
                String id = data != null ? data.getStringExtra("id") : "";
                Log.e("PAystackId", id);
                HashMap<String, String> hasmap = new HashMap<>();
                hasmap.put("user_id", getStringPref(ActPaymentMethod.this, userId));
                hasmap.put("email", getStringPref(ActPaymentMethod.this, userEmail));
                hasmap.put("full_name", Objects.requireNonNull(getIntent().getStringExtra("fname")) + " " + getIntent().getStringExtra("lname"));
                hasmap.put("landmark", getIntent().getStringExtra("landmark") != null ? getIntent().getStringExtra("landmark") : "");
                hasmap.put("mobile", getIntent().getStringExtra("mobile"));
                hasmap.put("order_notes", getIntent().getStringExtra("order_notes") != null ? getIntent().getStringExtra("order_notes") : "0");
                hasmap.put("grand_total", getIntent().getStringExtra("grand_total"));
                hasmap.put("payment_id", id);
                hasmap.put("payment_type", "6");
                hasmap.put("pincode", getIntent().getStringExtra("pincode"));
                hasmap.put("street_address", getIntent().getStringExtra("street_address"));
                hasmap.put("coupon_name", getIntent().getStringExtra("coupon_name") != null ? getIntent().getStringExtra("coupon_name") : "0");
                hasmap.put("discount_amount", getIntent().getStringExtra("discount_amount"));
                hasmap.put("vendor_id", getIntent().getStringExtra("vendorid"));
                callApiOrder(hasmap);
            }
        } else if (resultCode == 401) {
            String cardNumber = data != null ? data.getStringExtra("card_number") : "";
            String expMonth = data != null ? data.getStringExtra("exp_month") : "";
            String expYear = data != null ? data.getStringExtra("exp_year") : "";
            String cvv = data != null ? data.getStringExtra("cvv") : "";
            Log.d("CardInfomation", cardNumber + expMonth + expYear + cvv);
            callApiOrderstripe(cardNumber, expMonth, expYear, cvv);
        } else if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {

            if (requestCode == RavePayActivity.RESULT_SUCCESS) {
                Toast.makeText(this, "Transaction Successful", Toast.LENGTH_SHORT).show();
                String message = data.getStringExtra("response");
                Log.e("message", message != null ? message : "");
                FlutterWaveResponse json = new Gson().fromJson(message, FlutterWaveResponse.class);
                FlutterWaveData dataValue = json.getData();
                String id = dataValue != null ? dataValue.getFlwRef() : "";
                showLoadingProgress(ActPaymentMethod.this);
                HashMap<String, String> hasmap = new HashMap<>();
                hasmap.put("user_id", getStringPref(ActPaymentMethod.this, userId));
                hasmap.put("email", getStringPref(ActPaymentMethod.this, userEmail));
                hasmap.put("full_name", Objects.requireNonNull(getIntent().getStringExtra("fname")) + " " + getIntent().getStringExtra("lname"));
                hasmap.put("landmark", getIntent().getStringExtra("landmark") != null ? getIntent().getStringExtra("landmark") : "");
                hasmap.put("mobile", getIntent().getStringExtra("mobile"));
                hasmap.put("order_notes", getIntent().getStringExtra("order_notes") != null ? getIntent().getStringExtra("order_notes") : "0");
                hasmap.put("grand_total", getIntent().getStringExtra("grand_total"));
                hasmap.put("stripeEmail", getStringPref(ActPaymentMethod.this, userEmail));
                hasmap.put("payment_id", id);
                hasmap.put("payment_type", "5");
                hasmap.put("pincode", getIntent().getStringExtra("pincode"));
                hasmap.put("street_address", getIntent().getStringExtra("street_address"));
                hasmap.put("coupon_name", getIntent().getStringExtra("coupon_name") != null ? getIntent().getStringExtra("coupon_name") : "0");
                hasmap.put("discount_amount", getIntent().getStringExtra("discount_amount"));
                hasmap.put("vendor_id", getIntent().getStringExtra("vendorid"));
                callApiOrder(hasmap);

            } else if (requestCode == RavePayActivity.RESULT_ERROR) {
                Toast.makeText(this, "An Error Occur", Toast.LENGTH_SHORT).show();
            } else if (requestCode == RavePayActivity.RESULT_CANCELLED) {
                Toast.makeText(this, "Transaction Canceled", Toast.LENGTH_SHORT).show();

            }

        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        Common.getCurrentLanguage(ActPaymentMethod.this, false);
    }

    // TODO CALL ORDER API
    private void callApiOrderstripe(String cardNumber, String expMonth, String expYear, String cvv) {
        showLoadingProgress(ActPaymentMethod.this);

        HashMap<String, String> hasmap = new HashMap<>();
        hasmap.put("user_id", getStringPref(ActPaymentMethod.this, userId));
        hasmap.put("email", getStringPref(ActPaymentMethod.this, userEmail));
        hasmap.put("full_name", Objects.requireNonNull(getIntent().getStringExtra("fname")) + " " + getIntent().getStringExtra("lname"));
        hasmap.put("landmark", getIntent().getStringExtra("landmark") != null ? getIntent().getStringExtra("landmark") : "");
        hasmap.put("mobile", getIntent().getStringExtra("mobile"));
        hasmap.put("order_notes", getIntent().getStringExtra("order_notes") != null ? getIntent().getStringExtra("order_notes") : "0");
        hasmap.put("grand_total", getIntent().getStringExtra("grand_total"));
        hasmap.put("stripeToken", "");
        hasmap.put("stripeEmail", getStringPref(ActPaymentMethod.this, userEmail));
        hasmap.put("payment_id", "");
        hasmap.put("payment_type", "4");
        hasmap.put("pincode", getIntent().getStringExtra("pincode"));
        hasmap.put("street_address", getIntent().getStringExtra("street_address"));
        hasmap.put("coupon_name", getIntent().getStringExtra("coupon_name") != null ? getIntent().getStringExtra("coupon_name") : "0");
        hasmap.put("discount_amount", getIntent().getStringExtra("discount_amount"));
        hasmap.put("vendor_id", getIntent().getStringExtra("vendorid"));
        hasmap.put("card_number", cardNumber);
        hasmap.put("card_exp_month", expMonth);
        hasmap.put("card_exp_year", expYear);
        hasmap.put("card_cvc", cvv);
        Call<SingleResponse> call = ApiClient.getClient().setOrderPayment(hasmap);
        call.enqueue(new Callback<SingleResponse>() {
            @Override
            public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {
                if (response.code() == 200) {
                    dismissLoadingProgress();
                    Common.isAddOrUpdated = true;
                    if (response.body() != null && response.body().getStatus() == 1) {
                        showSuccessFullMsg(ActPaymentMethod.this, response.body().getMessage());
                        openActivity(ActPaymentSuccessFull.class);
                    } else {
                        showErrorFullMsg(ActPaymentMethod.this, response.body() != null ? response.body().getMessage() : "");
                    }
                } else {
                    dismissLoadingProgress();
                    alertErrorOrValidationDialog(ActPaymentMethod.this, getResources().getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(Call<SingleResponse> call, Throwable t) {
                dismissLoadingProgress();
                alertErrorOrValidationDialog(ActPaymentMethod.this, getResources().getString(R.string.error_msg));
            }
        });
    }
}