package com.webmintinfotech.ecom.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.base.BaseAdaptor;
import com.webmintinfotech.ecom.databinding.ActNotificationBinding;
import com.webmintinfotech.ecom.databinding.RowNotificationBinding;
import com.webmintinfotech.ecom.model.NotificationDataItem;
import com.webmintinfotech.ecom.model.NotificationsResponse;
import com.webmintinfotech.ecom.ui.authentication.ActLogin;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.SharePreference;
import java.util.ArrayList;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActNotification extends BaseActivity {
    private ActNotificationBinding notificationBinding;
    private ArrayList<NotificationDataItem> notificationsList = new ArrayList<>();
    private BaseAdaptor<NotificationDataItem, RowNotificationBinding> notificationAdapter = null;
    private LinearLayoutManager manager = null;
    private int currentPage = 1;
    private int total_pages = 0;
    private int visibleItemCount = 0;
    private int totalItemCount = 0;
    private int pastVisibleItems = 0;

    @Override
    public View setLayout() {
        return notificationBinding.getRoot();
    }

    @Override
    public void initView() {
        notificationBinding = ActNotificationBinding.inflate(getLayoutInflater());
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        notificationBinding.ivBack.setOnClickListener(v -> finish());

        notificationBinding.rvNotification.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = manager.getChildCount();
                    totalItemCount = manager.getItemCount();
                    pastVisibleItems = manager.findFirstVisibleItemPosition();
                    if (currentPage < total_pages) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            currentPage += 1;
                            callApiNotifications();
                        }
                    }
                }
            }
        });
    }

    private void callApiNotifications() {
        Common.showLoadingProgress(this);
        HashMap<String, String> hasmap = new HashMap<>();
        hasmap.put("user_id", SharePreference.getStringPref(this, SharePreference.userId));
        Call<NotificationsResponse> call = ApiClient.getClient().getNotificatios(String.valueOf(currentPage), hasmap);
        call.enqueue(new Callback<NotificationsResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<NotificationsResponse> call, Response<NotificationsResponse> response) {
                if (response.code() == 200) {
                    NotificationsResponse restResponce = response.body();
                    if (restResponce.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        if (restResponce.getData().getData().size() > 0) {
                            notificationBinding.rvNotification.setVisibility(View.VISIBLE);
                            notificationBinding.tvNoDataFound.setVisibility(View.GONE);
                            currentPage = Integer.parseInt(String.valueOf(restResponce.getData().getCurrentPage()));
                            total_pages = restResponce.getData().getLastPage() != null ? restResponce.getData().getLastPage() : 0;
                            notificationsList.addAll(restResponce.getData().getData());
                        } else {
                            notificationBinding.rvNotification.setVisibility(View.GONE);
                            notificationBinding.tvNoDataFound.setVisibility(View.VISIBLE);
                        }
                        notificationAdapter.notifyDataSetChanged();
                    } else if (restResponce.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(ActNotification.this, restResponce.getMessage());
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(ActNotification.this, getResources().getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(Call<NotificationsResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActNotification.this, getResources().getString(R.string.error_msg));
            }
        });
    }

//    private void loadNotificationDetails(ArrayList<NotificationDataItem> notificationsList) {
//        final com.webmintinfotech.ecom.databinding.RowNotificationBinding[] binding = new com.webmintinfotech.ecom.databinding.RowNotificationBinding[1];
//        notificationAdapter = new BaseAdaptor<NotificationDataItem, RowNotificationBinding>(this, notificationsList) {
//            @SuppressLint({"NewApi", "ResourceType", "SetTextI18n", "UseCompatLoadingForDrawables"})
//            @Override
//            public void onBindData(RecyclerView.ViewHolder holder, NotificationDataItem val, int position) {
//                RowNotificationBinding binding = ((RowNotificationViewHolder) holder).binding;
//                switch (notificationsList.get(position).getOrderStatus()) {
//                    case 1:
//                        binding.tvnotificationsName.setText(getString(R.string.order_place));
//                        binding.ivnotification.setImageResource(R.drawable.ic_orderplace);
//                        binding.ivnotification.setBackground(getDrawable(R.drawable.orderplace));
//                        break;
//                    case 2:
//                        binding.tvnotificationsName.setText(getString(R.string.order_confirmed));
//                        binding.ivnotification.setImageResource(R.drawable.ic_orderconfirmed);
//                        binding.ivnotification.setBackground(getDrawable(R.drawable.orderconfirmed));
//                        break;
//                    case 3:
//                        binding.tvnotificationsName.setText(getString(R.string.order_shipped));
//                        binding.ivnotification.setImageResource(R.drawable.delivery);
//                        binding.ivnotification.setBackground(getDrawable(R.drawable.ordershipped));
//                        break;
//                    case 4:
//                        binding.tvnotificationsName.setText(getString(R.string.order_delivered));
//                        binding.ivnotification.setImageResource(R.drawable.orderdelivery);
//                        binding.ivnotification.setBackground(getDrawable(R.drawable.orderdelivered));
//                        break;
//                    case 5:
//                        binding.tvnotificationsName.setText(getString(R.string.order_cancelled));
//                        binding.ivnotification.setImageResource(R.drawable.ordercancelledpackage);
//                        binding.ivnotification.setBackground(getDrawable(R.drawable.ordercancelled));
//                        break;
//                    case 6:
//                        binding.tvnotificationsName.setText(getString(R.string.order_cancelled));
//                        binding.ivnotification.setImageResource(R.drawable.ordercancelledpackage);
//                        binding.ivnotification.setBackground(getDrawable(R.drawable.ordercancelled));
//                        break;
//                    case 7:
//                        binding.tvnotificationsName.setText(getString(R.string.order_return_created));
//                        binding.ivnotification.setImageResource(R.drawable.ic_orderreturn);
//                        binding.ivnotification.setBackground(getDrawable(R.drawable.orderreturn));
//                        break;
//                    case 8:
//                        binding.tvnotificationsName.setText(getString(R.string.order_return_accepted));
//                        binding.ivnotification.setImageResource(R.drawable.orderdelivery);
//                        binding.ivnotification.setBackground(getDrawable(R.drawable.orderdelivered));
//                        break;
//                    case 9:
//                        binding.tvnotificationsName.setText(getString(R.string.order_return_completed));
//                        binding.ivnotification.setImageResource(R.drawable.delivery);
//                        binding.ivnotification.setBackground(getDrawable(R.drawable.ordershipped));
//                        break;
//                    case 10:
//                        binding.tvnotificationsName.setText(getString(R.string.order_return_rejected));
//                        binding.ivnotification.setImageResource(R.drawable.ordercancelledpackage);
//                        binding.ivnotification.setBackground(getDrawable(R.drawable.ordercancelled));
//                        break;
//                }
//                binding.tvdeliverydate.setText(Common.getDate(notificationsList.get(position).getDate()));
//                binding.tvDeliveryDetails.setText(notificationsList.get(position).getMessage());
//                holder.itemView.setOnClickListener(v -> {
//                    String orderstatus = String.valueOf(notificationsList.get(position).getOrderStatus());
//                    if (orderstatus.equals("7") || orderstatus.equals("8") || orderstatus.equals("9") || orderstatus.equals("10")) {
//                        Log.e("order_number--->", String.valueOf(notificationsList.get(position).getOrderId()));
//                        Intent intent = new Intent(ActNotification.this, ActReturnTrackOrder.class);
//                        intent.putExtra("orderId", String.valueOf(notificationsList.get(position).getOrderId()));
//                        startActivity(intent);
//                    } else if (orderstatus.equals("2") || orderstatus.equals("3") || orderstatus.equals("4")) {
//                        Log.e("order_number--->", String.valueOf(notificationsList.get(position).getOrderNumber()));
//                        Intent intent = new Intent(ActNotification.this, ActTrackOrder.class);
//                        intent.putExtra("order_id", String.valueOf(notificationsList.get(position).getOrderId()));
//                        startActivity(intent);
//                    } else {
//                        Log.e("order_number--->", String.valueOf(notificationsList.get(position).getOrderNumber()));
//                        Intent intent = new Intent(ActNotification.this, ActOrderDetails.class);
//                        intent.putExtra("order_number", String.valueOf(notificationsList.get(position).getOrderNumber()));
//                        startActivity(intent);
//                    }
//                });
//            }
//
//            @Override
//            public int setItemLayout() {
//                return R.layout.row_notification;
//            }
//
//            @Override
//            public RowNotificationBinding getBinding(ViewGroup parent) {
//                binding[0] = RowNotificationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
//                return binding[0];
//            }
//        };
//        notificationBinding.rvNotification.setLayoutManager(manager);
//        notificationBinding.rvNotification.setItemAnimator(new DefaultItemAnimator());
//        notificationBinding.rvNotification.setAdapter(notificationAdapter);
//    }

//    private static class RowNotificationViewHolder extends BaseAdapter.BaseViewHolder<RowNotificationBinding> {
//        public RowNotificationViewHolder(RowNotificationBinding binding) {
//            super(binding);
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        currentPage = 1;
        notificationsList.clear();
//        loadNotificationDetails(notificationsList);
        if (Common.isCheckNetwork(this)) {
            if (SharePreference.getBooleanPref(this, SharePreference.isLogin)) {
                callApiNotifications();
            } else {
                openActivity(ActLogin.class);
                this.finish();
            }
        } else {
            Common.alertErrorOrValidationDialog(this, getResources().getString(R.string.no_internet));
        }
        Common.getCurrentLanguage(this, false);
    }
}
