package com.webmintinfotech.ecom.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.ui.authentication.ActLogin;
import com.webmintinfotech.ecom.ui.activity.ActOrderDetails;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.base.BaseAdaptor;
import com.webmintinfotech.ecom.base.BaseFragment;
import com.webmintinfotech.ecom.databinding.FragOrderHistoryBinding;
import com.webmintinfotech.ecom.databinding.RowOrderBinding;
import com.webmintinfotech.ecom.model.OrderHistoryDataItem;
import com.webmintinfotech.ecom.model.OrderHistoryResponse;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.SharePreference;
import java.util.ArrayList;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryFragment extends BaseFragment<FragOrderHistoryBinding> {

    private FragOrderHistoryBinding fragOrderHistoryBinding;
    private ArrayList<OrderHistoryDataItem> orderHistoryDataList = new ArrayList<>();
    private String currency = "";
    private String currencyPosition = "";
    private BaseAdaptor<OrderHistoryDataItem, RowOrderBinding> wishListDataAdapter;
    private LinearLayoutManager linearlayoutManager;
    private int currentPage = 1;
    private int total_pages = 0;
    private int visibleItemCount = 0;
    private int totalItemCount = 0;
    private int pastVisibleItems = 0;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return getBinding().getRoot();
    }

    @Override
    public void initView(View view) {
        fragOrderHistoryBinding = FragOrderHistoryBinding.bind(view);
        linearlayoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);
        currency = SharePreference.getStringPref(requireActivity(), SharePreference.Currency);
        currencyPosition = SharePreference.getStringPref(requireActivity(), SharePreference.CurrencyPosition);

        fragOrderHistoryBinding.rvOrderlist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = linearlayoutManager.getChildCount();
                    totalItemCount = linearlayoutManager.getItemCount();
                    pastVisibleItems = linearlayoutManager.findFirstVisibleItemPosition();

                    if (currentPage < total_pages) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            currentPage += 1;
                            if (Common.isCheckNetwork(requireActivity())) {
                                callApiOrderHistory();
                            } else {
                                Common.alertErrorOrValidationDialog(
                                        requireActivity(),
                                        getResources().getString(R.string.no_internet)
                                );
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public FragOrderHistoryBinding getBinding() {
        fragOrderHistoryBinding = FragOrderHistoryBinding.inflate(getLayoutInflater());
        return fragOrderHistoryBinding;
    }

    //TODO API ORDER HISTORY CALL
    private void callApiOrderHistory() {
        Common.showLoadingProgress(requireActivity());
        HashMap<String, String> hasmap = new HashMap<>();
        hasmap.put("user_id", SharePreference.getStringPref(requireActivity(), SharePreference.userId));

        Call<OrderHistoryResponse> call = ApiClient.getClient().getOrderHistory(String.valueOf(currentPage), hasmap);
        call.enqueue(new Callback<OrderHistoryResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<OrderHistoryResponse> call, Response<OrderHistoryResponse> response) {
                if (response.code() == 200) {
                    OrderHistoryResponse restResponce = response.body();
                    if (restResponce.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        if (restResponce.getData() != null && restResponce.getData().getData() != null && restResponce.getData().getData().size() > 0) {
                            fragOrderHistoryBinding.rvOrderlist.setVisibility(View.VISIBLE);
                            fragOrderHistoryBinding.tvNoDataFound.setVisibility(View.GONE);
                            currentPage = restResponce.getData().getCurrentPage();
                            total_pages = restResponce.getData().getLastPage() != null ? restResponce.getData().getLastPage() : 0;
                            orderHistoryDataList.addAll(restResponce.getData().getData());
                        } else {
                            fragOrderHistoryBinding.rvOrderlist.setVisibility(View.GONE);
                            fragOrderHistoryBinding.tvNoDataFound.setVisibility(View.VISIBLE);
                        }
                        wishListDataAdapter.notifyDataSetChanged();
                    } else if (restResponce.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(
                                requireActivity(),
                                restResponce.getMessage().toString()
                        );
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(
                            requireActivity(),
                            getResources().getString(R.string.error_msg)
                    );
                }
            }

            @Override
            public void onFailure(Call<OrderHistoryResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(
                        requireActivity(),
                        getResources().getString(R.string.error_msg)
                );
            }
        });
    }

    //TODO SET ORDER HISTORY DATA
    private void loadOrderHistory(ArrayList<OrderHistoryDataItem> orderHistoryDataList) {
        final com.webmintinfotech.ecom.databinding.RowOrderBinding[] binding = {null};
        wishListDataAdapter = new BaseAdaptor<OrderHistoryDataItem, RowOrderBinding>(requireActivity(), orderHistoryDataList) {
            @SuppressLint({"NewApi", "ResourceType", "SetTextI18n"})
            @Override
            public void onBindData(RecyclerView.ViewHolder holder, OrderHistoryDataItem val, int position) {
                binding[0].tvorderid.setText(orderHistoryDataList.get(position).getOrderNumber());
                switch (orderHistoryDataList.get(position).getPaymentType()) {
                    case 1:
                        binding[0].tvpaymenttype.setText(getString(R.string.cash));
                        break;
                    case 2:
                        binding[0].tvpaymenttype.setText(getString(R.string.wallet));
                        break;
                    case 3:
                        binding[0].tvpaymenttype.setText(getString(R.string.razorpay));
                        break;
                    case 4:
                        binding[0].tvpaymenttype.setText(getString(R.string.stripe));
                        break;
                    case 5:
                        binding[0].tvpaymenttype.setText(getString(R.string.flutterwave));
                        break;
                    case 6:
                        binding[0].tvpaymenttype.setText(getString(R.string.paystack));
                        break;
                }
                binding[0].tvorderdate.setText(orderHistoryDataList.get(position).getDate() != null ?
                        Common.getDate(orderHistoryDataList.get(position).getDate()) : "");
                binding[0].tvordercost.setText(Common.getPrice(currencyPosition, currency,
                        String.valueOf(orderHistoryDataList.get(position).getGrandTotal())));
                holder.itemView.setOnClickListener(v -> {
                    Log.e("order_number--->", orderHistoryDataList.get(position).getOrderNumber());
                    Intent intent = new Intent(requireActivity(), ActOrderDetails.class);
                    intent.putExtra("order_number", orderHistoryDataList.get(position).getOrderNumber());
                    startActivity(intent);
                });
            }

            @Override
            public int setItemLayout() {
                return R.layout.row_order;
            }

            @Override
            public RowOrderBinding getBinding(ViewGroup parent) {
                binding[0] = RowOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return binding[0];
            }
        };

        if (isAdded()) {
            fragOrderHistoryBinding.rvOrderlist.setLayoutManager(linearlayoutManager);
            fragOrderHistoryBinding.rvOrderlist.setItemAnimator(new DefaultItemAnimator());
            fragOrderHistoryBinding.rvOrderlist.setAdapter(wishListDataAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Common.getCurrentLanguage(requireActivity(), false);
        currentPage = 1;
        orderHistoryDataList.clear();
        loadOrderHistory(orderHistoryDataList);
        if (Common.isCheckNetwork(requireActivity())) {
            if (SharePreference.getBooleanPref(requireActivity(), SharePreference.isLogin)) {
                callApiOrderHistory();
            } else {
                openActivity(ActLogin.class);
                requireActivity().finish();
            }
        } else {
            Common.alertErrorOrValidationDialog(
                    requireActivity(),
                    getResources().getString(R.string.no_internet)
            );
        }
    }
}