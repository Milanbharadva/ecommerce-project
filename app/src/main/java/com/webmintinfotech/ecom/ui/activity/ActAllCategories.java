package com.webmintinfotech.ecom.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.base.BaseAdaptor;
import com.webmintinfotech.ecom.databinding.ActAllCategoriesBinding;
import com.webmintinfotech.ecom.databinding.RowAllCategoriesBinding;
import com.webmintinfotech.ecom.model.CategoriesResponse;
import com.webmintinfotech.ecom.model.DataItem;
import com.webmintinfotech.ecom.utils.Common;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActAllCategories extends BaseActivity {
    private ActAllCategoriesBinding allCategoriesBinding;
    private ArrayList<DataItem> categoriesList;
    private BaseAdaptor<DataItem, RowAllCategoriesBinding> categoriesAdaptor;
    private LinearLayoutManager linearLayoutManagerCategories;
    private String[] colorArray = {
            "#FDF7FF", "#FDF3F0", "#EDF7FD", "#FFFAEA", "#F1FFF6", "#FFF5EC"
    };

    @Override
    public View setLayout() {
        return allCategoriesBinding.getRoot();
    }

    @Override
    public void initView() {
        allCategoriesBinding = ActAllCategoriesBinding.inflate(getLayoutInflater());
        linearLayoutManagerCategories = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        if (Common.isCheckNetwork(this)) {
            callCatgories();
        } else {
            Common.alertErrorOrValidationDialog(this, getString(R.string.no_internet));
        }

        allCategoriesBinding.ivBack.setOnClickListener(view -> {
            finish();
            setResult(RESULT_OK);
        });
    }

    private void callCatgories() {
        Common.showLoadingProgress(this);
        Call<CategoriesResponse> call = ApiClient.getClient().getcategory();
        call.enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                if (response.code() == 200) {
                    CategoriesResponse restResponce = response.body();
                    if (restResponce != null && restResponce.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        categoriesList = restResponce.getData();
                        if (categoriesList != null && categoriesList.size() > 0) {
                            loadCategoriesDeals(categoriesList);
                            allCategoriesBinding.rvCategories.setVisibility(View.VISIBLE);
                            allCategoriesBinding.tvNoDataFound.setVisibility(View.GONE);
                        } else {
                            allCategoriesBinding.rvCategories.setVisibility(View.GONE);
                            allCategoriesBinding.tvNoDataFound.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Common.alertErrorOrValidationDialog(ActAllCategories.this, getString(R.string.error_msg));
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(ActAllCategories.this, getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActAllCategories.this, getString(R.string.error_msg));
            }
        });
    }

    private void loadCategoriesDeals(ArrayList<DataItem> categoriesList) {
        categoriesAdaptor = new BaseAdaptor<DataItem, RowAllCategoriesBinding>(this, categoriesList) {
            @Override
            public void onBindData(@NonNull RecyclerView.ViewHolder holder, DataItem val, int position) {
                RowAllCategoriesBinding binding = (RowAllCategoriesBinding) ((ViewHolder) holder).getBinding();
                binding.tvcateitemname.setText(categoriesList.get(position).getCategoryName());
                Glide.with(ActAllCategories.this)
                        .load(categoriesList.get(position).getImageUrl())
                        .into(binding.ivCarttemm);
                binding.ivCarttemm.setBackgroundColor(Color.parseColor(colorArray[position % 6]));
                holder.itemView.setOnClickListener(view -> {
                    Log.e("cat_id--->", categoriesList.get(position).getId().toString());
                    Intent intent = new Intent(ActAllCategories.this, ActAllSubCategories.class);
                    Bundle extras = new Bundle();
                    extras.putString("cat_id", categoriesList.get(position).getId().toString());
                    extras.putString("categoryName", categoriesList.get(position).getCategoryName());
                    intent.putExtras(extras);
                    startActivity(intent);
                });
                binding.clmain.setBackgroundColor(Color.parseColor(colorArray[position % 6]));
                binding.card.setCardBackgroundColor(Color.parseColor(colorArray[position % 6]));
            }

            @Override
            public int setItemLayout() {
                return R.layout.row_all_categories;
            }

            @Override
            public RowAllCategoriesBinding getBinding(ViewGroup parent) {
                return RowAllCategoriesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            }
        };

        allCategoriesBinding.rvCategories.setLayoutManager(linearLayoutManagerCategories);
        allCategoriesBinding.rvCategories.setItemAnimator(new DefaultItemAnimator());
        allCategoriesBinding.rvCategories.setAdapter(categoriesAdaptor);
    }

    @Override
    public void onResume() {
        super.onResume();
        Common.getCurrentLanguage(this, false);
    }
}
