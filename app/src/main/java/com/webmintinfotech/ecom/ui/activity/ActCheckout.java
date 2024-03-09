package com.webmintinfotech.ecom.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.databinding.ActCheckoutBinding;
import com.webmintinfotech.ecom.databinding.RowCheckoutBinding;
import com.webmintinfotech.ecom.model.CheckOutData;
import com.webmintinfotech.ecom.model.CheckOutDataItem;
import com.webmintinfotech.ecom.model.GetCheckOutResponse;
import com.webmintinfotech.ecom.ui.authentication.ActLogin;
import com.webmintinfotech.ecom.ui.payment.ActPaymentMethod;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.SharePreference;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActCheckout extends BaseActivity {
    private ActCheckoutBinding checkoutBinding;
    private ArrayList<CheckOutDataItem> checkOutDataList;
    private String currency = "";
    private String currencyPosition = "";
    private int subtotal = 0;
    private int tax = 0;
    private int checkoutprice = 0;
    private String discountsum = "";
    private double price = 0.0;
    private String fname = "";
    private String landmark = "";
    private String lname = "";
    private String email = "";
    private String pincode = "";
    private String mobile = "";
    private String streetAddress = "";
    private String coupon_name = "";
    private String vendorid = "";
    private String discountprice = "";
    private String[] colorArray = {"#FDF7FF", "#FDF3F0", "#EDF7FD", "#FFFAEA", "#F1FFF6", "#FFF5EC"};

    @Override
    public View setLayout() {
        return checkoutBinding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    @Override
    public void initView() {
        checkoutBinding = ActCheckoutBinding.inflate(getLayoutInflater());
        checkoutBinding.tvapplycoupon.setVisibility(View.GONE);
        checkoutBinding.clCoupon.setVisibility(View.GONE);
        currency = SharePreference.getStringPref(this, SharePreference.Currency);
        currencyPosition = SharePreference.getStringPref(this, SharePreference.CurrencyPosition);
        checkoutBinding.btnaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(ActAddress.class);
            }
        });
        checkoutBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (SharePreference.getBooleanPref(this, SharePreference.isLogin)) {
            if (Common.isCheckNetwork(this)) {
                HashMap<String, String> map = new HashMap<>();
                map.put("user_id", SharePreference.getStringPref(this, SharePreference.userId));
                callApiCheckoutItem(map, false);
            } else {
                Common.alertErrorOrValidationDialog(this, getResources().getString(R.string.no_internet));
            }
        } else {
            openActivity(ActLogin.class);
            finish();
            finishAffinity();
        }
        checkoutBinding.btnaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActCheckout.this, ActAddress.class);
                intent.putExtra("isComeFromSelectAddress", true);
                addressDataSet.launch(intent);
            }
        });
        checkoutBinding.tveditaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActCheckout.this, ActAddress.class);
                intent.putExtra("isComeFromSelectAddress", true);
                addressDataSet.launch(intent);
            }
        });
        checkoutBinding.clhaveacoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SharePreference.getBooleanPref(ActCheckout.this, SharePreference.isCoupon)) {
                    SharePreference.setBooleanPref(ActCheckout.this, SharePreference.isCoupon, true);
                    checkoutBinding.tvapplycoupon.setVisibility(View.GONE);
                    checkoutBinding.clCoupon.setVisibility(View.GONE);
                } else {
                    checkoutBinding.tvapplycoupon.setVisibility(View.VISIBLE);
                    checkoutBinding.clCoupon.setVisibility(View.VISIBLE);
                }
            }
        });
        checkoutBinding.btnapplycoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkoutBinding.btnapplycoupon.getText().toString().equals(getResources().getString(R.string.apply))) {
                    if (!checkoutBinding.edtcouponcode.getText().toString().isEmpty()) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("user_id", SharePreference.getStringPref(ActCheckout.this, SharePreference.userId));
                        map.put("coupon_name", checkoutBinding.edtcouponcode.getText().toString());
                        callApiCheckoutItem(map, true);
                        checkoutBinding.edtcouponcode.requestFocus();
                        checkoutBinding.btnapplycoupon.setTextColor(getResources().getColor(R.color.red));
                        checkoutBinding.btnapplycoupon.setText(getResources().getString(R.string.remove));
                        checkoutBinding.tvdiscounttitle.setText(getString(R.string.discount) + "(" + checkoutBinding.edtcouponcode.getText() + ")");
                        coupon_name = checkoutBinding.edtcouponcode.getText().toString();
                    } else {
                        Common.alertErrorOrValidationDialog(ActCheckout.this, getResources().getString(R.string.coupan_code_validation));
                    }
                } else if (checkoutBinding.btnapplycoupon.getText().toString().equals(getResources().getString(R.string.remove))) {
                    checkoutBinding.edtcouponcode.setText("");
                    checkoutBinding.btnapplycoupon.setText(getResources().getString(R.string.apply));
                    checkoutBinding.tvdiscounttitle.setText(getString(R.string.discount));
                    checkoutBinding.tvdiscounttotal.setText(Common.getPrice(currencyPosition, currency, "0.00"));
                    checkoutBinding.tvtotal.setText(String.valueOf(price));
                    checkoutBinding.btnapplycoupon.setTextColor(getResources().getColor(R.color.Blackcolor));
                    HashMap<String, String> map = new HashMap<>();
                    map.put("user_id", SharePreference.getStringPref(ActCheckout.this, SharePreference.userId));
                    callApiCheckoutItem(map, false);
                    checkoutBinding.edtcouponcode.setClickable(true);
                    checkoutBinding.edtcouponcode.setEnabled(true);
                    coupon_name = "";
                }
            }
        });
    }

    private void callApiCheckoutItem(HashMap<String, String> map, boolean couponCodeApplied) {
        Common.showLoadingProgress(ActCheckout.this);
        Call<GetCheckOutResponse> call = ApiClient.getClient().getCheckOut(map);
        call.enqueue(new Callback<GetCheckOutResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<GetCheckOutResponse> call, Response<GetCheckOutResponse> response) {
                Log.e("code", String.valueOf(response.code()));
                Log.e("status", String.valueOf(response.body()));
                if (response.code() == 200) {
                    GetCheckOutResponse restResponce = response.body();
                    if (restResponce.getStatus() == 1) {
                        Common.dismissLoadingProgress();

                        if (couponCodeApplied) {
                            checkoutBinding.edtcouponcode.setClickable(false);
                            checkoutBinding.edtcouponcode.setFocusableInTouchMode(false);
                            checkoutBinding.edtcouponcode.setEnabled(false);
                        } else {
                            checkoutBinding.edtcouponcode.setFocusableInTouchMode(true);
                            checkoutBinding.edtcouponcode.setEnabled(true);
                            checkoutBinding.edtcouponcode.setClickable(true);
                        }

                        checkOutDataList = restResponce.getCheckoutdata();
                        if (checkOutDataList != null) {
                            loadCheckOutItem(checkOutDataList);
                        }
                        loadCheckOutDetails(restResponce.getData());
                    } else if (restResponce.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        checkoutBinding.btnapplycoupon.setText(getResources().getString(R.string.apply));
                        checkoutBinding.edtcouponcode.setText("");
                        checkoutBinding.btnapplycoupon.setTextColor(getResources().getColor(R.color.Blackcolor));
                        Common.alertErrorOrValidationDialog(ActCheckout.this, restResponce.getMessage());
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(ActCheckout.this, getResources().getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(Call<GetCheckOutResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Log.e("error", t.getMessage());
                Common.alertErrorOrValidationDialog(ActCheckout.this, getResources().getString(R.string.error_msg));
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void loadCheckOutItem(ArrayList<CheckOutDataItem> checkOutDataList) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            discountsum = checkOutDataList == null ? "0" : String.valueOf(checkOutDataList.stream().mapToDouble(checkOutDataItem -> Double.parseDouble(checkOutDataItem.getDiscountAmount())).sum());
        }
        checkoutBinding.btnProccedtoPayment.setOnClickListener(v -> {
            if (checkoutBinding.btnaddress.getVisibility() == View.VISIBLE) {
                Common.showErrorFullMsg(ActCheckout.this, getResources().getString(R.string.select_your_address));
            } else {
                Intent intent = new Intent(ActCheckout.this, ActPaymentMethod.class);
                intent.putExtra("email", email);
                intent.putExtra("fname", fname);
                intent.putExtra("lname", lname);
                intent.putExtra("landmark", landmark);
                intent.putExtra("mobile", mobile);
                intent.putExtra("order_notes", checkoutBinding.edtordernote.getText().toString());
                intent.putExtra("pincode", pincode);
                intent.putExtra("street_address", streetAddress);
                intent.putExtra("coupon_name", coupon_name);
                intent.putExtra("discount_amount", discountprice);
                intent.putExtra("grand_total", String.valueOf(price));
                intent.putExtra("vendorid", vendorid);
                Log.d("intent", discountprice);
                startActivity(intent);
            }
        });

        RecyclerView.Adapter<RecyclerView.ViewHolder> wishListDataAdapter = new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                RowCheckoutBinding binding = RowCheckoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new RecyclerView.ViewHolder(binding.getRoot()) {};
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                RowCheckoutBinding binding = RowCheckoutBinding.bind(holder.itemView);
                binding.tvcateitemname.setText(checkOutDataList.get(position).getProductName());
                if (checkOutDataList.get(position).getAttribute() == null || checkOutDataList.get(position).getVariation() == null) {
                    binding.tvcartitemsize.setText("-");
                } else {
                    binding.tvcartitemsize.setText(checkOutDataList.get(position).getAttribute() + " : " + checkOutDataList.get(position).getVariation());
                }
                int qty = Integer.parseInt(String.valueOf(checkOutDataList.get(position).getQty()));
                double qtyprice = Double.parseDouble(checkOutDataList.get(position).getPrice());
                double totalpriceqty = qtyprice * qty;
                binding.tvcartitemqty.setText("Qty: " + checkOutDataList.get(position).getQty() + "*" + Common.getPrice(currencyPosition, currency, checkOutDataList.get(position).getPrice()));
                Log.d("tax", String.valueOf(checkOutDataList.get(position).getTax()));
                String shippingCost = checkOutDataList.get(position).getShippingCost().equals("Free Shipping") ? "0.0" : checkOutDataList.get(position).getShippingCost();
                binding.tvcartitemprice.setText(Common.getPrice(currencyPosition, currency, String.valueOf(totalpriceqty)));
                binding.tvtax.setText(Common.getPrice(currencyPosition, currency, String.valueOf(checkOutDataList.get(position).getTax())));
                binding.tvTotalCart.setText(Common.getPrice(currencyPosition, currency, checkOutDataList.get(position).getTotal()));
                binding.tvshippingcost.setText(Common.getPrice(currencyPosition, currency, shippingCost));
                Glide.with(ActCheckout.this).load(checkOutDataList.get(position).getImageUrl()).into(binding.ivCartitemm);
                binding.ivCartitemm.setBackgroundColor(Color.parseColor(colorArray[position % 6]));
                vendorid = String.valueOf(checkOutDataList.get(position).getVendorId());
                holder.itemView.setOnClickListener(v -> {
                    Log.e("product_id--->", String.valueOf(checkOutDataList.get(position).getProductId()));
                    Intent intent = new Intent(ActCheckout.this, ActProductDetails.class);
                    intent.putExtra("product_id", String.valueOf(checkOutDataList.get(position).getProductId()));
                    startActivity(intent);
                });
            }

            @Override
            public int getItemCount() {
                return checkOutDataList.size();
            }
        };
        checkoutBinding.rvCheckoutdata.setLayoutManager(new LinearLayoutManager(ActCheckout.this, LinearLayoutManager.VERTICAL, false));
        checkoutBinding.rvCheckoutdata.setItemAnimator(new DefaultItemAnimator());
        checkoutBinding.rvCheckoutdata.setAdapter(wishListDataAdapter);
    }

    @SuppressLint("SetTextI18n")
    private void loadCheckOutDetails(CheckOutData checkOutList) {
        subtotal = checkOutList.getSubtotal();
        tax = (int) Double.parseDouble(checkOutList.getTax());
        int disc = Math.round(Float.parseFloat(discountsum));
        String shippingCost = checkOutList.getShippingCost().equals("Free Shipping") ? "0.0" : checkOutList.getShippingCost();
        price = Double.parseDouble(checkOutList.getGrand_total());
        checkoutBinding.tvsubtotal.setText(Common.getPrice(currencyPosition, currency, String.valueOf(checkOutList.getSubtotal())));
        checkoutBinding.tvtaxtotal.setText(Common.getPrice(currencyPosition, currency, checkOutList.getTax()));
        checkoutBinding.tvshippingtotal.setText(Common.getPrice(currencyPosition, currency, shippingCost));
        checkoutBinding.tvdiscounttotal.setText("-" + Common.getPrice(currencyPosition, currency, checkOutList.getDiscount_amount()));
        discountprice = checkOutList.getDiscount_amount();
        checkoutBinding.tvtotal.setText(Common.getPrice(currencyPosition, currency, checkOutList.getGrand_total()));
    }

    @SuppressLint("SetTextI18n")
    private void setAddressToCheckoutPage(int result, Intent data) {
        if (result == 500) {
            checkoutBinding.tvUserName.setVisibility(View.VISIBLE);
            checkoutBinding.tvUserphone.setVisibility(View.VISIBLE);
            checkoutBinding.tvareaaddress.setVisibility(View.VISIBLE);
            checkoutBinding.tvusermailid.setVisibility(View.VISIBLE);
            checkoutBinding.clAddressselect.setVisibility(View.VISIBLE);
            checkoutBinding.btnaddress.setVisibility(View.GONE);
            checkoutBinding.tveditaddress.setVisibility(View.VISIBLE);
            checkoutBinding.tvUserName.setText(data.getStringExtra("FirstName") + "" + data.getStringExtra("LastName"));
            checkoutBinding.tvareaaddress.setText(data.getStringExtra("StreetAddress") + " " + data.getStringExtra("Landmark") + "-" + data.getStringExtra("Pincode"));
            checkoutBinding.tvUserphone.setText(data.getStringExtra("Mobile"));
            checkoutBinding.tvusermailid.setText(data.getStringExtra("Email"));
            email = data.getStringExtra("Email");
            fname = data.getStringExtra("FirstName");
            lname = data.getStringExtra("LastName");
            mobile = data.getStringExtra("Mobile");
            landmark = data.getStringExtra("Landmark");
            streetAddress = data.getStringExtra("StreetAddress");
            pincode = data.getStringExtra("Pincode");
        } else {
            checkoutBinding.tvUserName.setVisibility(View.VISIBLE);
            checkoutBinding.tvUserphone.setVisibility(View.VISIBLE);
            checkoutBinding.tvareaaddress.setVisibility(View.VISIBLE);
            checkoutBinding.tvusermailid.setVisibility(View.VISIBLE);
            checkoutBinding.clAddressselect.setVisibility(View.VISIBLE);
            checkoutBinding.btnaddress.setVisibility(View.GONE);
            checkoutBinding.tveditaddress.setVisibility(View.VISIBLE);
        }
    }

    private final ActivityResultLauncher<Intent> addressDataSet = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        Intent data = result.getData();
        setAddressToCheckoutPage(result.getResultCode(),data);
    });

    @Override
    protected void onResume() {
        super.onResume();
        Common.getCurrentLanguage(ActCheckout.this, false);
    }
}
