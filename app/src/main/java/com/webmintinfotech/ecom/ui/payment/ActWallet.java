
package com.webmintinfotech.ecom.ui.payment;

import android.annotation.SuppressLint;
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
import com.webmintinfotech.ecom.databinding.ActWalletBinding;
import com.webmintinfotech.ecom.databinding.RowTransactionhistoryBinding;
import com.webmintinfotech.ecom.model.WalletDataItem;
import com.webmintinfotech.ecom.model.WalletResponse;
import com.webmintinfotech.ecom.ui.authentication.ActLogin;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.SharePreference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.HashMap;

public class ActWallet extends BaseActivity {
    private ActWalletBinding actWalletBinding;
    private ArrayList<WalletDataItem> walletList = new ArrayList<>();
    private BaseAdaptor<WalletDataItem, RowTransactionhistoryBinding> walletAdapter = null;
    private LinearLayoutManager manager = null;
    private int currentPage = 1;
    private int total_pages = 0;
    private int visibleItemCount = 0;
    private int totalItemCount = 0;
    private int pastVisibleItems = 0;
    private String currency = "";
    private String currencyPosition = "";

    @Override
    protected View setLayout() {
        actWalletBinding = ActWalletBinding.inflate(getLayoutInflater());
        return actWalletBinding.getRoot();
    }

    @Override
    protected void initView() {
        actWalletBinding.ivBack.setOnClickListener(v -> {
            finish();
            setResult(RESULT_OK);
        });

        currency = SharePreference.getStringPref(this, SharePreference.Currency);
        currencyPosition = SharePreference.getStringPref(this, SharePreference.CurrencyPosition);

        actWalletBinding.tvAddMoney.setText(currency);

        actWalletBinding.tvAddMoney.setOnClickListener(v -> openActivity(ActAddMoney.class));

        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        actWalletBinding.rvtransactionhistory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = manager.getChildCount();
                    totalItemCount = manager.getItemCount();
                    pastVisibleItems = manager.findFirstVisibleItemPosition();
                    if (currentPage < total_pages) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            currentPage += 1;
                            callApiWallet();
                        }
                    }
                }
            }
        });
    }

    // TODO CALL WALLET API
    private void callApiWallet() {
        Common.showLoadingProgress(this);
        HashMap<String, String> hasmap = new HashMap<>();
        hasmap.put("user_id", SharePreference.getStringPref(this, SharePreference.userId));
        Call<WalletResponse> call = ApiClient.getClient().getWallet(Integer.toString(currentPage), hasmap);
        call.enqueue(new Callback<WalletResponse>() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onResponse(Call<WalletResponse> call, Response<WalletResponse> response) {
                if (response.code() == 200) {
                    WalletResponse restResponce = response.body();
                    if (restResponce.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        if (restResponce.getData().getData() != null && restResponce.getData().getData().size() > 0) {
                            actWalletBinding.tvwalletamount.setText(Common.getPrice(currencyPosition, currency, restResponce.getWalletamount()));
                            actWalletBinding.rvtransactionhistory.setVisibility(View.VISIBLE);
                            actWalletBinding.tvNoDataFound.setVisibility(View.GONE);
                            currentPage = restResponce.getData().getCurrentPage();
                            total_pages = restResponce.getData().getLastPage() != null ? restResponce.getData().getLastPage() : 0;
                            walletList.addAll(restResponce.getData().getData());
                        } else {
                            actWalletBinding.rvtransactionhistory.setVisibility(View.GONE);
                            actWalletBinding.tvNoDataFound.setVisibility(View.VISIBLE);
                        }
                        walletAdapter.notifyDataSetChanged();
                    } else if (restResponce.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(ActWallet.this, restResponce.getMessage());
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(ActWallet.this, getResources().getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(Call<WalletResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActWallet.this, getResources().getString(R.string.error_msg));
            }
        });
    }

    // TODO WALLET DATA SET
    private void loadWalletDetails(ArrayList<WalletDataItem> walletList) {
        final com.webmintinfotech.ecom.databinding.RowTransactionhistoryBinding[] binding = {null};
        walletAdapter = new BaseAdaptor<WalletDataItem, RowTransactionhistoryBinding>(this, walletList) {
            @SuppressLint({"NewApi", "ResourceType", "SetTextI18n", "UseCompatLoadingForDrawables"})
            @Override
            public void onBindData(RecyclerView.ViewHolder holder, WalletDataItem val, int position) {
                switch (walletList.get(position).getTransactionType()) {
                    case "1":
                        binding[0].tvwalletDetails.setText(getString(R.string.cancelled));
                        binding[0].ivWallet.setImageResource(R.drawable.ordercancelledpackage);
                        binding[0].clorder.setBackground(getDrawable(R.drawable.round_red));
                        binding[0].tvwalletName.setText("Order id: " + walletList.get(position).getOrderNumber());
                        binding[0].tvDeliveryprice.setText("-" + Common.getPrice(currencyPosition, currency, walletList.get(position).getWallet()));
                        break;
                    case "2":
                        binding[0].tvwalletDetails.setText(getString(R.string.order_confirmed));
                        binding[0].ivWallet.setImageResource(R.drawable.ic_orderconfirmed);
                        binding[0].clorder.setBackground(getDrawable(R.drawable.round_green));
                        binding[0].tvwalletName.setText("Order id: " + walletList.get(position).getOrderNumber());
                        binding[0].tvDeliveryprice.setText(Common.getPrice(currencyPosition, currency, walletList.get(position).getWallet()));
                        break;
                    case "3":
                        binding[0].tvwalletDetails.setText(getString(R.string.order_Referral_amount));
                        binding[0].ivWallet.setImageResource(R.drawable.ic_wallet);
                        binding[0].clorder.setBackground(getDrawable(R.drawable.round_darkpink));
                        binding[0].tvwalletName.setText("User Name: " + walletList.get(position).getUsername());
                        binding[0].tvDeliveryprice.setText(Common.getPrice(currencyPosition, currency, walletList.get(position).getWallet()));
                        break;
                    case "4":
                        binding[0].tvwalletName.setText(getString(R.string.waller_recharge));
                        binding[0].tvwalletDetails.setText(getString(R.string.order_cancelled));
                        binding[0].ivWallet.setImageResource(R.drawable.ic_wallet);
                        binding[0].clorder.setBackground(getDrawable(R.drawable.round_darkpink));
                        binding[0].tvDeliveryprice.setText(Common.getPrice(currencyPosition, currency, walletList.get(position).getWallet()));
                        break;
                }

                switch (walletList.get(position).getType()) {
                    case "3":
                        binding[0].tvwalletDetails.setText(getString(R.string.razorpay));
                        break;
                    case "4":
                        binding[0].tvwalletDetails.setText(getString(R.string.stirpe));
                        break;
                    case "5":
                        binding[0].tvwalletDetails.setText(getString(R.string.flutterwave));
                        break;
                    case "6":
                        binding[0].tvwalletDetails.setText(getString(R.string.paystack));
                        break;
                }
                binding[0].tvwalletdate.setText(walletList.get(position).getDate() != null ? Common.getDate(walletList.get(position).getDate()) : "");
            }

            @Override
            public int setItemLayout() {
                return R.layout.row_notification;
            }

            @Override
            public RowTransactionhistoryBinding getBinding(ViewGroup parent) {
                binding[0] = RowTransactionhistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return binding[0];
            }
        };
        actWalletBinding.rvtransactionhistory.setLayoutManager(manager);
        actWalletBinding.rvtransactionhistory.setItemAnimator(new DefaultItemAnimator());
        actWalletBinding.rvtransactionhistory.setAdapter(walletAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Common.getCurrentLanguage(this, false);
        currentPage = 1;
        walletList.clear();
        loadWalletDetails(walletList);
        if (Common.isCheckNetwork(this)) {
            if (SharePreference.getBooleanPref(this, SharePreference.isLogin)) {
                callApiWallet();
            } else {
                openActivity(ActLogin.class);
                finish();
            }
        } else {
            Common.alertErrorOrValidationDialog(this, getResources().getString(R.string.no_internet));
        }
    }
}