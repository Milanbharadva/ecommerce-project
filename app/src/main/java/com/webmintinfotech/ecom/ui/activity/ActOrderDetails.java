package com.webmintinfotech.ecom.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.api.SingleResponse;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.base.BaseAdaptor;
import com.webmintinfotech.ecom.databinding.ActOrderDetailsBinding;
import com.webmintinfotech.ecom.databinding.RemoveItemDialogBinding;
import com.webmintinfotech.ecom.databinding.RowOrderdetailsproductBinding;
import com.webmintinfotech.ecom.model.OrderDataItem;
import com.webmintinfotech.ecom.model.OrderDetailsResponse;
import com.webmintinfotech.ecom.model.OrderInfo;
import com.webmintinfotech.ecom.ui.authentication.ActLogin;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.SharePreference;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActOrderDetails extends BaseActivity {
    private ActOrderDetailsBinding orderDetailsBinding;
    private ArrayList<OrderDataItem> orderData;
    private String currency = "";
    private String currencyPosition = "";
    private OrderInfo orderDetailsList;
    private String orderstatus = "";
    private final String[] colorArray = {
            "#FDF7FF",
            "#FDF3F0",
            "#EDF7FD",
            "#FFFAEA",
            "#F1FFF6",
            "#FFF5EC"
    };

    @Override
    public View setLayout() {
        return orderDetailsBinding.getRoot();
    }

    @Override
    public void initView() {
        orderDetailsBinding = ActOrderDetailsBinding.inflate(LayoutInflater.from(this));
        if (Common.isCheckNetwork(this)) {
            if (SharePreference.getBooleanPref(this, SharePreference.isLogin)) {
                callApiOrderDetail();
            } else {
                openActivity(ActLogin.class);
                finish();
            }
        } else {
            Common.alertErrorOrValidationDialog(this, getResources().getString(R.string.no_internet));
        }
        currency = SharePreference.getStringPref(this, SharePreference.Currency);
        currencyPosition = SharePreference.getStringPref(this, SharePreference.CurrencyPosition);
        orderDetailsBinding.ivBack.setOnClickListener(view -> {
            finish();
            setResult(RESULT_OK);
        });
    }

    //TODO API ORDER DETAILS CALL
    private void callApiOrderDetail() {
        Common.showLoadingProgress(this);
        HashMap<String, String> hasmap = new HashMap<>();
        hasmap.put("order_number", getIntent().getStringExtra("order_number"));
        Call<OrderDetailsResponse> call = ApiClient.getClient().getOrderDetails(hasmap);
        call.enqueue(new Callback<OrderDetailsResponse>() {
            @Override
            public void onResponse(Call<OrderDetailsResponse> call, Response<OrderDetailsResponse> response) {
                if (response.code() == 200) {
                    OrderDetailsResponse restResponce = response.body();
                    if (restResponce.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        orderDetailsList = restResponce.getOrderInfo();
                        loadOrderDetails(orderDetailsList);
                        if (restResponce.getOrderData().size() > 0) {
                            orderDetailsBinding.rvorderproduct.setVisibility(View.VISIBLE);
                            orderDetailsBinding.tvNoDataFound.setVisibility(View.GONE);
                            orderData = restResponce.getOrderData();
                            loadOrderProductDetails(orderData);
                        } else {
                            orderDetailsBinding.rvorderproduct.setVisibility(View.VISIBLE);
                            orderDetailsBinding.tvNoDataFound.setVisibility(View.GONE);
                        }
                    } else if (restResponce.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(ActOrderDetails.this, restResponce.getMessage());
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(ActOrderDetails.this, getResources().getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(Call<OrderDetailsResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActOrderDetails.this, getResources().getString(R.string.error_msg));
            }
        });
    }

    //TODO SET ORDER PRODUCT DETAILS DATA
    private void loadOrderProductDetails(ArrayList<OrderDataItem> orderData) {
        BaseAdaptor<OrderDataItem, RowOrderdetailsproductBinding> orderDetailsAdpater =
                new BaseAdaptor<OrderDataItem, RowOrderdetailsproductBinding>(this, orderData) {
                    @SuppressLint({"NewApi", "ResourceType", "SetTextI18n", "InflateParams", "UseCompatLoadingForDrawables"})
                    @Override
                    public void onBindData(RecyclerView.ViewHolder holder, OrderDataItem val, int position) {
                        RowOrderdetailsproductBinding binding = ((RowOrderdetailsproductBinding) holder.itemView.getTag());
                        Glide.with(ActOrderDetails.this)
                                .load(orderData.get(position).getImageUrl()).into(binding.ivCartitemm);
                        binding.ivCartitemm.setBackgroundColor(Color.parseColor(colorArray[position % 6]));
                        binding.tvcateitemname.setText(orderData.get(position).getProductName());
                        binding.tvcartitemqty.setText("Qty: " + orderData.get(position).getQty() + "*" +
                                Common.getPrice(currencyPosition, currency, orderData.get(position).getPrice()));
                        int qty = orderData.get(position).getQty();
                        double price = Double.parseDouble(orderData.get(position).getPrice());
                        double totalpriceqty = price * qty;
                        if (orderData.get(position).getVariation() == null) {
                            binding.tvcartitemsize.setText("-");
                        } else {
                            binding.tvcartitemsize.setText(orderData.get(position).getAttribute() + ": " + orderData.get(position).getVariation());
                        }
                        orderstatus = orderData.get(position).getStatus().toString();
                        if (orderstatus.equals("5") || orderstatus.equals("7")) {
                            binding.swipe.setSwipeEnabled(false);
                        }
                        switch (orderstatus) {
                            case "5":
                                binding.swipe.setSwipeEnabled(true);
                                binding.tvorderstatus.setVisibility(View.VISIBLE);
                                binding.tvorderstatus.setText(getString(R.string.order_cancelled));
                                break;
                            case "7":
                                binding.swipe.setSwipeEnabled(true);
                                binding.tvorderstatus.setVisibility(View.VISIBLE);
                                binding.tvorderstatus.setText(getString(R.string.return_request));
                                break;
                            default:
                                binding.tvorderstatus.setVisibility(View.GONE);
                                break;
                        }

                        binding.tvcartitemprice.setText(Common.getPrice(currencyPosition, currency, String.valueOf(totalpriceqty)));

                        binding.tvshippingcost.setText(Common.getPrice(currencyPosition, currency, orderData.get(position).getShippingCost().toString()));

                        binding.tvtax.setText(Common.getPrice(currencyPosition, currency, orderData.get(position).getTax().toString()));

                        binding.tvTotalCart.setText(Common.getPrice(currencyPosition, currency, orderData.get(position).getTotal().toString()));

                        binding.tvmore.setOnClickListener(view -> {
                            BottomSheetDialog dialog = new BottomSheetDialog(ActOrderDetails.this);
                            if (Common.isCheckNetwork(ActOrderDetails.this)) {
                                View dialogView = LayoutInflater.from(ActOrderDetails.this).inflate(R.layout.row_bottomsheetorderdetails, null);
                                dialog.getWindow().setBackgroundDrawable(getDrawable(R.color.tr));
                                TextView btnCancelorder = dialogView.findViewById(R.id.tvcancelorder);
                                TextView btnTrackorder = dialogView.findViewById(R.id.tvtrackorder);
                                TextView btnCancel = dialogView.findViewById(R.id.tvCancel);
                                TextView btnRetuenRequest = dialogView.findViewById(R.id.tvReturnReq);
                                View viewreturnrequest = dialogView.findViewById(R.id.view2);
                                if (orderstatus.equals("4")) {
                                    btnRetuenRequest.setVisibility(View.VISIBLE);
                                    viewreturnrequest.setVisibility(View.VISIBLE);
                                } else {
                                    btnRetuenRequest.setVisibility(View.GONE);
                                    viewreturnrequest.setVisibility(View.GONE);
                                }
                                if (orderstatus.equals("7")) {
                                    btnRetuenRequest.setVisibility(View.GONE);
                                    viewreturnrequest.setVisibility(View.GONE);
                                }
                                btnCancel.setOnClickListener(view1 -> {
                                    dialog.dismiss();
                                    onResume();
                                });
                                btnCancelorder.setOnClickListener(view12 -> {
                                    cancelOrderDialog(orderData.get(position).getId());
                                    dialog.dismiss();
                                });
                                btnRetuenRequest.setOnClickListener(view13 -> {
                                    Log.e("order_id--->", orderData.get(position).getId().toString());
                                    Intent intent = new Intent(ActOrderDetails.this, ActRetuenRequest.class);
                                    intent.putExtra("order_id", orderData.get(position).getId().toString());
                                    intent.putExtra("att", orderData.get(position).getAttribute().toString());
                                    startActivity(intent);
                                    dialog.dismiss();
                                });
                                btnTrackorder.setOnClickListener(view14 -> {
                                    Log.e("order_id--->", orderData.get(position).getId().toString());
                                    Intent intent = new Intent(ActOrderDetails.this, ActTrackOrder.class);
                                    intent.putExtra("order_id", orderData.get(position).getId().toString());
                                    intent.putExtra("att", orderData.get(position).getAttribute().toString());
                                    intent.putExtra("Size", binding.tvcartitemsize.getText());
                                    startActivity(intent);
                                    dialog.dismiss();
                                });
                                dialog.setCancelable(false);
                                dialog.setContentView(dialogView);
                                dialog.show();
                            } else {
                                Common.alertErrorOrValidationDialog(ActOrderDetails.this, getResources().getString(R.string.no_internet));
                                dialog.dismiss();
                            }
                        });
                    }

                    @Override
                    public int setItemLayout() {
                        return R.layout.row_orderdetailsproduct;
                    }

                    @Override
                    public RowOrderdetailsproductBinding getBinding(ViewGroup parent) {
                        return new RowOrderdetailsproductBindingHolder(RowOrderdetailsproductBinding.inflate(getLayoutInflater(),parent,false)).getBinding();
                    }


                };
        orderDetailsBinding.rvorderproduct.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        orderDetailsBinding.rvorderproduct.setItemAnimator(new DefaultItemAnimator());
        orderDetailsBinding.rvorderproduct.setAdapter(orderDetailsAdpater);
    }


    public void cancelOrderDialog(Integer orderId) {
        RemoveItemDialogBinding removeDialogBinding = RemoveItemDialogBinding.inflate(getLayoutInflater());
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(removeDialogBinding.getRoot());
        removeDialogBinding.tvRemoveTitle.setText(getResources().getString(R.string.cancel_product));
        removeDialogBinding.tvAlertMessage.setText(getResources().getString(R.string.cancel_product_desc));
        removeDialogBinding.btnProceed.setOnClickListener(view -> {
            if (Common.isCheckNetwork(ActOrderDetails.this)) {
                dialog.dismiss();
                cancelOrder(orderId);

            } else {
                Common.alertErrorOrValidationDialog(ActOrderDetails.this, getResources().getString(R.string.no_internet));
            }
        });
        removeDialogBinding.ivClose.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    //TODO API ORDER CANCEL CALL
    private void cancelOrder(Integer id) {
        Common.showLoadingProgress(ActOrderDetails.this);
        HashMap<String, String> hasmap = new HashMap<>();
        hasmap.put("user_id", SharePreference.getStringPref(ActOrderDetails.this, SharePreference.userId));
        hasmap.put("order_id", id.toString());
        hasmap.put("status", "5");
        Call<SingleResponse> call = ApiClient.getClient().getCancelOrder(hasmap);
        call.enqueue(new Callback<SingleResponse>() {
            @Override
            public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {
                Common.dismissLoadingProgress();
                if (response.code() == 200) {
                    if (response.body().getStatus() == 1) {
                        Common.isAddOrUpdated = true;
                        callApiOrderDetail();
                        loadOrderProductDetails(orderData);
                    } else {
                        Common.showErrorFullMsg(ActOrderDetails.this, response.body().getMessage());
                    }
                } else {
                    Common.alertErrorOrValidationDialog(ActOrderDetails.this, getResources().getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(Call<SingleResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActOrderDetails.this, getResources().getString(R.string.error_msg));
            }
        });
    }

    public static class RowOrderdetailsproductBindingHolder extends RecyclerView.ViewHolder {
        private final RowOrderdetailsproductBinding binding;

        public RowOrderdetailsproductBindingHolder(RowOrderdetailsproductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public RowOrderdetailsproductBinding getBinding() {
            return binding;
        }
    }

    //TODO SET ORDER DETAILS DATA
    @SuppressLint("SetTextI18n")
    private void loadOrderDetails(OrderInfo orderDetailsList) {
        if (!orderDetailsList.equals(0)) {
            orderDetailsBinding.tvorderid.setVisibility(View.VISIBLE);
            orderDetailsBinding.tvpaymenttype.setVisibility(View.VISIBLE);
            orderDetailsBinding.tvorderdate.setVisibility(View.VISIBLE);
            orderDetailsBinding.tvusermailid.setVisibility(View.VISIBLE);
            orderDetailsBinding.tvphone.setVisibility(View.VISIBLE);
            orderDetailsBinding.tvareaaddress.setVisibility(View.VISIBLE);
            orderDetailsBinding.tvUserName.setVisibility(View.VISIBLE);
            orderDetailsBinding.tvsubtotal.setVisibility(View.VISIBLE);
            orderDetailsBinding.tvtaxtotal.setVisibility(View.VISIBLE);
            orderDetailsBinding.tvshippingtotal.setVisibility(View.VISIBLE);
            orderDetailsBinding.tvtotal.setVisibility(View.VISIBLE);
            orderDetailsBinding.tvdiscounttotal.setVisibility(View.VISIBLE);
            orderDetailsBinding.tvorderid.setText(orderDetailsList.getOrderNumber());
            switch (orderDetailsList.getPaymentType()) {
                case 1:
                    orderDetailsBinding.tvpaymenttype.setText(getString(R.string.cash));
                    break;
                case 2:
                    orderDetailsBinding.tvpaymenttype.setText(getString(R.string.wallet));
                    break;
                case 3:
                    orderDetailsBinding.tvpaymenttype.setText(getString(R.string.razorpay));
                    break;
                case 4:
                    orderDetailsBinding.tvpaymenttype.setText(getString(R.string.stripe));
                    break;
                case 5:
                    orderDetailsBinding.tvpaymenttype.setText(getString(R.string.flutterwave));
                    break;
                case 6:
                    orderDetailsBinding.tvpaymenttype.setText(getString(R.string.paystack));
                    break;
            }
            orderDetailsBinding.tvorderdate.setText(Common.getDate(orderDetailsList.getDate()));
            orderDetailsBinding.tvusermailid.setText(orderDetailsList.getEmail());
            orderDetailsBinding.tvphone.setText(orderDetailsList.getMobile());
            if (orderDetailsList.getCouponName() != null) {
                orderDetailsBinding.tvdiscounttitle.setText(getString(R.string.discount) + "(" + orderDetailsList.getCouponName() + ")");
            } else {
                orderDetailsBinding.tvdiscounttitle.setText(getString(R.string.discount));
            }
            String address = "";
            if (orderDetailsList.getLandmark() == null) {
                address = orderDetailsList.getStreetAddress() + "-" + orderDetailsList.getPincode();
            } else if (orderDetailsList.getStreetAddress() == null) {
                address = orderDetailsList.getLandmark() + "-" + orderDetailsList.getPincode();
            } else if (orderDetailsList.getPincode() == null) {
                address = orderDetailsList.getLandmark() + " " + orderDetailsList.getStreetAddress();
            } else {
                address = orderDetailsList.getLandmark() + " " + orderDetailsList.getStreetAddress() + "-" + orderDetailsList.getPincode();
            }
            orderDetailsBinding.tvareaaddress.setText(address);
            if (orderDetailsList.getOrderNotes() == null) {
                orderDetailsBinding.clNote.setVisibility(View.GONE);
            } else {
                orderDetailsBinding.edNote.setText(orderDetailsList.getOrderNotes());
            }
            orderstatus = orderDetailsList.getStatus().toString();
            orderDetailsBinding.tvUserName.setText(orderDetailsList.getFullName());
            orderDetailsBinding.tvsubtotal.setText(Common.getPrice(currencyPosition, currency, orderDetailsList.getSubtotal().toString()));
            orderDetailsBinding.tvtaxtotal.setText(Common.getPrice(currencyPosition, currency, orderDetailsList.getTax().toString()));
            orderDetailsBinding.tvshippingtotal.setText(Common.getPrice(currencyPosition, currency, orderDetailsList.getShippingCost().toString()));
            if (orderDetailsList.getDiscountAmount() != null) {
                orderDetailsBinding.tvdiscounttotal.setText("-" + Common.getPrice(currencyPosition, currency, orderDetailsList.getDiscountAmount().toString()));
            } else {
                orderDetailsBinding.tvdiscounttotal.setText(Common.getPrice(currencyPosition, currency, "0.00"));
            }
            orderDetailsBinding.tvtotal.setText(Common.getPrice(currencyPosition, currency, orderDetailsList.getGrandTotal().toString()));

        } else {
            orderDetailsBinding.tvorderid.setVisibility(View.GONE);
            orderDetailsBinding.tvpaymenttype.setVisibility(View.GONE);
            orderDetailsBinding.tvorderdate.setVisibility(View.GONE);
            orderDetailsBinding.tvusermailid.setVisibility(View.GONE);
            orderDetailsBinding.tvphone.setVisibility(View.GONE);
            orderDetailsBinding.tvareaaddress.setVisibility(View.GONE);
            orderDetailsBinding.tvUserName.setVisibility(View.GONE);
            orderDetailsBinding.tvsubtotal.setVisibility(View.GONE);
            orderDetailsBinding.tvtaxtotal.setVisibility(View.GONE);
            orderDetailsBinding.tvshippingtotal.setVisibility(View.GONE);
            orderDetailsBinding.tvtotal.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        callApiOrderDetail();
    }
}
