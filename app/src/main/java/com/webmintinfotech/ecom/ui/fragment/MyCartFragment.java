package com.webmintinfotech.ecom.ui.fragment;

import static com.webmintinfotech.ecom.utils.Common.alertErrorOrValidationDialog;
import static com.webmintinfotech.ecom.utils.Common.dismissLoadingProgress;
import static com.webmintinfotech.ecom.utils.Common.showErrorFullMsg;
import static com.webmintinfotech.ecom.utils.Common.showLoadingProgress;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.ui.activity.ActCheckout;
import com.webmintinfotech.ecom.ui.authentication.ActLogin;
import com.webmintinfotech.ecom.ui.activity.ActProductDetails;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.api.SingleResponse;
import com.webmintinfotech.ecom.base.BaseAdaptor;
import com.webmintinfotech.ecom.base.BaseFragment;
import com.webmintinfotech.ecom.databinding.FragMyCartBinding;
import com.webmintinfotech.ecom.databinding.RemoveItemDialogBinding;
import com.webmintinfotech.ecom.databinding.RowMycartBinding;
import com.webmintinfotech.ecom.model.CartDataItem;
import com.webmintinfotech.ecom.model.GetCartResponse;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.SharePreference;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCartFragment extends BaseFragment<FragMyCartBinding> {
    private FragMyCartBinding fragMyCartBinding;
    private ArrayList<CartDataItem> cartDataList;
    private String currency = "";
    private String currencyPosition = "";
    private int count = 0;
    private BaseAdaptor<CartDataItem, RowMycartBinding> cartListDataAdapter;
    private String[] colorArray = {
            "#FDF7FF",
            "#FDF3F0",
            "#EDF7FD",
            "#FFFAEA",
            "#F1FFF6",
            "#FFF5EC"
    };

    @Override
    public void initView(View view) {
        fragMyCartBinding = FragMyCartBinding.bind(view);
        currency = SharePreference.getStringPref(requireActivity(), SharePreference.Currency);
        currencyPosition = SharePreference.getStringPref(requireActivity(), SharePreference.CurrencyPosition);
    }

    @Override
    public FragMyCartBinding getBinding() {
        fragMyCartBinding = FragMyCartBinding.inflate(LayoutInflater.from(requireActivity()));
        fragMyCartBinding.btncheckout.setOnClickListener(v -> openActivity(ActCheckout.class));
        return fragMyCartBinding;
    }

    // TODO API CART CALL
    private void callApiCartData(boolean isQty) {
        if (!isQty) {
            showLoadingProgress(requireActivity());
        }
        HashMap<String, String> hasmap = new HashMap<>();
        hasmap.put("user_id", SharePreference.getStringPref(requireActivity(), SharePreference.userId));
        Call<GetCartResponse> call = ApiClient.getClient().getCartData(hasmap);
        call.enqueue(new Callback<GetCartResponse>() {
            @Override
            public void onResponse(Call<GetCartResponse> call, Response<GetCartResponse> response) {
                if (response.code() == 200) {
                    GetCartResponse restResponce = response.body();
                    if (restResponce.getStatus() == 1) {
                        dismissLoadingProgress();
                        fragMyCartBinding.rvMycard.setVisibility(View.VISIBLE);
                        fragMyCartBinding.tvNoDataFound.setVisibility(View.GONE);
                        cartDataList = restResponce.getData();
                        if (isAdded()) {
                            loadCartData(cartDataList);
                        }
                    } else if (restResponce.getStatus() == 0) {
                        dismissLoadingProgress();
                        fragMyCartBinding.rvMycard.setVisibility(View.GONE);
                        fragMyCartBinding.tvNoDataFound.setVisibility(View.VISIBLE);
                        alertErrorOrValidationDialog(requireActivity(), restResponce.getMessage());
                    }
                } else {
                    dismissLoadingProgress();
                    alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(Call<GetCartResponse> call, Throwable t) {
                dismissLoadingProgress();
                alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.error_msg));
            }
        });
    }

    public void removeItemDialog(String strCartId, int pos) {
        RemoveItemDialogBinding removeDialogBinding = RemoveItemDialogBinding.inflate(LayoutInflater.from(requireActivity()));
        BottomSheetDialog dialog = new BottomSheetDialog(requireActivity());
        dialog.setContentView(removeDialogBinding.getRoot());
        removeDialogBinding.tvRemoveTitle.setText(getResources().getString(R.string.remove_product));
        removeDialogBinding.tvAlertMessage.setText(getResources().getString(R.string.remove_product_desc));
        removeDialogBinding.btnProceed.setOnClickListener(v -> {
            if (Common.isCheckNetwork(requireActivity())) {
                dialog.dismiss();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("user_id", SharePreference.getStringPref(requireActivity(), SharePreference.userId));
                hashMap.put("cart_id", strCartId);
                callDeleteApi(hashMap, pos);
            } else {
                alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.no_internet));
            }
        });

        removeDialogBinding.ivClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    // TODO SET CART DATA
    private void loadCartData(ArrayList<CartDataItem> cartDataList) {
        final com.webmintinfotech.ecom.databinding.RowMycartBinding[] binding = {null};
        cartListDataAdapter = new BaseAdaptor<CartDataItem, RowMycartBinding>(requireActivity(), cartDataList) {
            @SuppressLint({"NewApi", "ResourceType", "SetTextI18n"})
            @Override
            public void onBindData(RecyclerView.ViewHolder holder, CartDataItem val, int position) {
                double price = cartDataList.get(position).getQty().intValue() * Double.parseDouble(cartDataList.get(position).getPrice());
                binding[0].tvcartitemprice.setText(Common.getPrice(currencyPosition, currency, String.valueOf(price)));
                binding[0].tvorderitem.setText(cartDataList.get(position).getQty().toString());
                binding[0].tvcateitemname.setText(cartDataList.get(position).getProductName());

                Glide.with(requireActivity())
                        .load(cartDataList.get(position).getImageUrl())
                        .into(binding[0].ivCartitemm);
                binding[0].ivCartitemm.setBackgroundColor(Color.parseColor(colorArray[position % 6]));

                binding[0].tvDelete.setOnClickListener(v -> {
                    if (Common.isCheckNetwork(requireActivity())) {
                        removeItemDialog(cartDataList.get(position).getId().toString(), position);
                    } else {
                        alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.no_internet));
                    }
                });

                binding[0].ivMinus.setOnClickListener(v -> {
                    if (cartDataList.get(position).getQty().intValue() > 1) {
                        binding[0].ivMinus.setClickable(true);
                        Common.getLog("Qty>>", cartDataList.get(position).getQty().toString());
                        if (Common.isCheckNetwork(requireActivity())) {
                            callQtyUpdate(cartDataList.get(position), false);
                        } else {
                            alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.no_internet));
                        }
                    } else {
                        binding[0].ivMinus.setClickable(false);
                        Common.getLog("Qty1>>", cartDataList.get(position).getQty().toString());
                    }
                });

                binding[0].swipe.getSurfaceView().setOnClickListener(v -> {
                    Log.e("product_id--->", cartDataList.get(position).getProductId().toString());
                    Intent intent = new Intent(requireActivity(), ActProductDetails.class);
                    intent.putExtra("product_id", cartDataList.get(position).getProductId().toString());
                    startActivity(intent);
                });

                if (cartDataList.get(position).getVariation().isEmpty()) {
                    binding[0].tvcartitemsize.setText("-");
                } else {
                    if (cartDataList.get(position).getAttribute() == null || cartDataList.get(position).getVariation() == null) {
                        binding[0].tvcartitemsize.setText("-");
                    } else {
                        binding[0].tvcartitemsize.setText(cartDataList.get(position).getAttribute() + " : " + cartDataList.get(position).getVariation());
                    }
                }

                binding[0].ivPlus.setOnClickListener(v -> {
                    if (cartDataList.get(position).getQty().intValue() < 10) {
                        count++;
                        if (Common.isCheckNetwork(requireActivity())) {
                            callQtyUpdate(cartDataList.get(position), true);
                        } else {
                            alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.no_internet));
                        }
                    } else {
                        alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.max_qty));
                    }
                });
            }

            @Override
            public int setItemLayout() {
                return R.layout.row_mycart;
            }

            @Override
            public RowMycartBinding getBinding(ViewGroup parent) {
                binding[0] = RowMycartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return binding[0];
            }
        };

        if (isAdded()) {

                if (cartDataList.size() > 0) {
                    fragMyCartBinding.rvMycard.setVisibility(View.VISIBLE);
                    fragMyCartBinding.tvNoDataFound.setVisibility(View.GONE);
                    fragMyCartBinding.btncheckout.setVisibility(View.VISIBLE);

                    fragMyCartBinding.rvMycard.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    fragMyCartBinding.rvMycard.setItemAnimator(new DefaultItemAnimator());
                    fragMyCartBinding.rvMycard.setAdapter(cartListDataAdapter);
                } else {
                    fragMyCartBinding.rvMycard.setVisibility(View.GONE);
                    fragMyCartBinding.tvNoDataFound.setVisibility(View.VISIBLE);
                    fragMyCartBinding.btncheckout.setVisibility(View.GONE);
                }

        }
    }

    // TODO API CART ITEM DELETE CALL
    private void callDeleteApi(HashMap<String, String> hasmap, int pos) {
        showLoadingProgress(requireActivity());

        Call<SingleResponse> call = ApiClient.getClient().deleteProduct(hasmap);
        call.enqueue(new Callback<SingleResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {
                dismissLoadingProgress();
                if (response.code() == 200) {
                    if (response.body().getStatus() == 1) {
                        cartDataList.remove(pos);
                        callApiCartData(true);
                        if (cartDataList.size() > 0) {
                            fragMyCartBinding.tvNoDataFound.setVisibility(View.GONE);
                            fragMyCartBinding.rvMycard.setVisibility(View.VISIBLE);
                            fragMyCartBinding.btncheckout.setVisibility(View.VISIBLE);
                        } else {
                            fragMyCartBinding.tvNoDataFound.setVisibility(View.VISIBLE);
                            fragMyCartBinding.rvMycard.setVisibility(View.GONE);
                            fragMyCartBinding.btncheckout.setVisibility(View.GONE);
                        }
                    } else {
                        showErrorFullMsg(requireActivity(), response.body().getMessage());
                    }
                } else {
                    alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(Call<SingleResponse> call, Throwable t) {
                dismissLoadingProgress();
                alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.error_msg));
            }
        });
    }

    // TODO API QTY UPDATE CALL
    private void callQtyUpdate(CartDataItem cartModel, boolean isPlus) {
        int qty;
        if (isPlus) {
            qty = cartModel.getQty() + 1;
        } else {
            qty = cartModel.getQty() - 1;
        }
        showLoadingProgress(requireActivity());
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("cart_id", String.valueOf(cartModel.getId()));
        hashMap.put("qty", String.valueOf(qty));
        Call<SingleResponse> call = ApiClient.getClient().qtyUpdate(hashMap);
        call.enqueue(new Callback<SingleResponse>() {
            @Override
            public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {
                dismissLoadingProgress();
                if (response.code() == 200) {
                    if (response.body().getStatus() == 1) {
                        Common.isAddOrUpdated = true;
                        callApiCartData(true);
                    } else {
                        String message = response.body().getMessage();
                        if (message != null) {
                            showErrorFullMsg(requireActivity(), message);
                        }
                    }
                } else {
                    alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(Call<SingleResponse> call, Throwable t) {
                dismissLoadingProgress();
                alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.error_msg));
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        Common.getCurrentLanguage(requireActivity(), false);
        if (Common.isCheckNetwork(requireActivity())) {
            if (SharePreference.getBooleanPref(requireActivity(), SharePreference.isLogin)) {
                callApiCartData(false);
            } else {
                openActivity(ActLogin.class);
                requireActivity().finish();
            }
        } else {
            alertErrorOrValidationDialog(requireActivity(), getResources().getString(R.string.no_internet));
        }
    }
}