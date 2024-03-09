package com.webmintinfotech.ecom.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.ui.authentication.ActLogin;
import com.webmintinfotech.ecom.ui.activity.ActProductDetails;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.api.SingleResponse;
import com.webmintinfotech.ecom.model.ViewAllDataItem;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.SharePreference;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewAllAdapter extends RecyclerView.Adapter<ViewAllAdapter.ViewHolder> {
    private Activity context;
    private ArrayList<ViewAllDataItem> mList;
    private String price = "";
    private String currency = "";
    private String currencyPosition = "";

    public ViewAllAdapter(Activity context, ArrayList<ViewAllDataItem> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_featuredproduct, parent, false);
        currency = SharePreference.getStringPref(context, SharePreference.Currency);
        currencyPosition = SharePreference.getStringPref(context, SharePreference.CurrencyPosition);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final ViewAllDataItem viewAll = mList.get(position);

        Glide.with(context).load(viewAll.getProductimage().getImageUrl()).into(holder.ivProduct);

        holder.tvProductName.setText(viewAll.getProductName());

        holder.tvProductPrice.setText(Common.getPrice(currencyPosition, currency, viewAll.getProductPrice().toString()));

        if (viewAll.getDiscountedPrice() == null || viewAll.getDiscountedPrice().equals("0")) {
            holder.tvProductDisprice.setVisibility(View.GONE);
        } else {
            holder.tvProductDisprice.setText(Common.getPrice(currencyPosition, currency, viewAll.getDiscountedPrice()));
        }

        if (viewAll.getIsWishlist() == 0) {
            holder.ivwishlist.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_dislike, null));
        } else {
            holder.ivwishlist.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_like, null));
        }

        holder.ivwishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharePreference.getBooleanPref(context, SharePreference.isLogin)) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("product_id", viewAll.getId().toString());
                    map.put("user_id", SharePreference.getStringPref(context, SharePreference.userId));

                    if (Common.isCheckNetwork(context)) {
                        if (viewAll.getIsWishlist() == 0) {
                            callApiFavourite(viewAll, map, position);
                        } else if (viewAll.getIsWishlist() == 1) {
                            callApiRemoveFavourite(viewAll, map, position);
                        }
                    } else {
                        Common.alertErrorOrValidationDialog(context, context.getResources().getString(R.string.no_internet));
                    }
                } else {
                    context.startActivity(new Intent(context, ActLogin.class));
                    context.finish();
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActProductDetails.class);
                intent.putExtra("product_id", viewAll.getProductimage().getProductId().toString());
                context.startActivity(intent);
            }
        });

        if (viewAll.getRattings().size() == 0) {
            holder.tvRatePro.setText("0.0");
        } else {
            holder.tvRatePro.setText(viewAll.getRattings().get(0).getAvgRatting().toString());
        }
    }

    private void callApiRemoveFavourite(final ViewAllDataItem viewAll, HashMap<String, String> map, final int position) {
        Common.showLoadingProgress(context);
        Call<SingleResponse> call = ApiClient.getClient().setRemoveFromWishList(map);
        Log.e("remove-->", new Gson().toJson(map));
        call.enqueue(new Callback<SingleResponse>() {
            @Override
            public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {
                if (response.code() == 200) {
                    SingleResponse restResponse = response.body();
                    if (restResponse.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        viewAll.setIsWishlist(0);
                        notifyItemChanged(position);
                    } else if (restResponse.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(context, restResponse.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<SingleResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(context, context.getResources().getString(R.string.error_msg));
            }
        });
    }

    private void callApiFavourite(final ViewAllDataItem viewAll, HashMap<String, String> map, final int position) {
        Common.showLoadingProgress(context);
        Call<SingleResponse> call = ApiClient.getClient().setAddToWishList(map);
        call.enqueue(new Callback<SingleResponse>() {
            @Override
            public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {
                if (response.code() == 200) {
                    SingleResponse restResponse = response.body();
                    if (restResponse.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        viewAll.setIsWishlist(1);
                        notifyItemChanged(position);
                    } else if (restResponse.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(context, restResponse.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<SingleResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(context, context.getResources().getString(R.string.error_msg));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvRatePro, tvProductPrice, tvProductDisprice;
        ImageView ivProduct, ivwishlist;

        public ViewHolder(View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvRatePro = itemView.findViewById(R.id.tvRatePro);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductDisprice = itemView.findViewById(R.id.tvProductDisprice);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            ivwishlist = itemView.findViewById(R.id.ivwishlist);
        }
    }
}
