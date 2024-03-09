package com.webmintinfotech.ecom.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.base.BaseAdaptor;
import com.webmintinfotech.ecom.databinding.ActSearchBinding;
import com.webmintinfotech.ecom.databinding.RowSearchBinding;
import com.webmintinfotech.ecom.model.SearchDataItem;
import com.webmintinfotech.ecom.model.SearchProductResponse;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.SharePreference;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActSearch extends BaseActivity {
    private ActSearchBinding searchBinding;
    private ArrayList<SearchDataItem> searchList = new ArrayList<>();
    private ArrayList<SearchDataItem> tempsearchList;
    private BaseAdaptor<SearchDataItem, RowSearchBinding> searchAdapter;
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
        return searchBinding.getRoot();
    }

    @Override
    public void initView() {
        tempsearchList = new ArrayList<>();
        searchBinding = ActSearchBinding.inflate(LayoutInflater.from(this));
        searchBinding.ivBack.setOnClickListener(view -> {
            finish();
            setResult(RESULT_OK);
        });

        if (Common.isCheckNetwork(this)) {
            callApiSearchList();
        } else {
            Common.alertErrorOrValidationDialog(this, getResources().getString(R.string.no_internet));
        }


        searchBinding.edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is called before the text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // This method is called when the text is changed
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This method is called after the text is changed
                searchList.clear();
                String searchText = s.toString().toLowerCase();
                if (!searchText.isEmpty()) {
                    searchBinding.rvsearch.setVisibility(View.VISIBLE);
                    searchBinding.tvNoDataFound.setVisibility(View.GONE);
                    for (SearchDataItem item : tempsearchList) {
                        if (item.getProductName().toLowerCase().contains(searchText)) {
                            searchList.add(item);
                        }
                    }
                } else {
                    searchBinding.rvsearch.setVisibility(View.GONE);
                    searchBinding.tvNoDataFound.setVisibility(View.VISIBLE);
                    searchList.addAll(tempsearchList);
                }
                new Handler(Looper.getMainLooper()).postDelayed(() -> loadSearchList(searchList), 100L);
            }
        });

    }

    //TODO CALL SEARCH API
    private void callApiSearchList() {
        Common.showLoadingProgress(this);
        HashMap<String, String> hasmap = new HashMap<>();
        if (SharePreference.getBooleanPref(this, SharePreference.isLogin)) {
            hasmap.put("user_id", SharePreference.getStringPref(this, SharePreference.userId));
        } else {
            hasmap.put("user_id", "");
        }
        Call<SearchProductResponse> call = ApiClient.getClient().getSearchProducts(hasmap);
        call.enqueue(new Callback<SearchProductResponse>() {
            @Override
            public void onResponse(Call<SearchProductResponse> call, Response<SearchProductResponse> response) {
                if (response.code() == 200) {
                    SearchProductResponse restResponse = response.body();
                    if (restResponse.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        if (restResponse.getData() != null) {
                            searchList.addAll(restResponse.getData());
                            tempsearchList.addAll(restResponse.getData());
                        }
                        loadSearchList(searchList);
                    } else if (restResponse.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(ActSearch.this, restResponse.getMessage());
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(ActSearch.this, getResources().getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(Call<SearchProductResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActSearch.this, getResources().getString(R.string.error_msg));
            }
        });
    }

    //TODO SEARCH DATA SET
    private void loadSearchList(ArrayList<SearchDataItem> tempsearchList) {
        searchAdapter = new BaseAdaptor<SearchDataItem, RowSearchBinding>(this, tempsearchList) {
            @Override
            public void onBindData(RecyclerView.ViewHolder holder, SearchDataItem val, int position) {
                RowSearchBinding binding = ((BaseViewHolder) holder).binding;
                binding.tvsearch.setText(tempsearchList.get(position).getProductName());
                Glide.with(ActSearch.this)
                        .load(tempsearchList.get(position).getProductimage().getImageUrl())
                        .into(binding.ivsearchproduct);
                binding.ivsearchproduct.setBackgroundColor(Color.parseColor(colorArray[position % 6]));

                holder.itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(ActSearch.this, ActProductDetails.class);
                    intent.putExtra("product_id", tempsearchList.get(position).getProductimage().getProductId().toString());
                    startActivity(intent);
                });
            }

            @Override
            public int setItemLayout() {
                return R.layout.row_gravity;
            }

            @Override
            public RowSearchBinding getBinding(ViewGroup parent) {
                return RowSearchBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            }
        };

        searchBinding.rvsearch.setLayoutManager(new LinearLayoutManager(ActSearch.this, LinearLayoutManager.VERTICAL, false));
        searchBinding.rvsearch.setItemAnimator(new DefaultItemAnimator());
        searchBinding.rvsearch.setAdapter(searchAdapter);
    }

    // Define BaseViewHolder as a separate class within ActSearch
    private static class BaseViewHolder extends RecyclerView.ViewHolder {
        public final RowSearchBinding binding;

        public BaseViewHolder(RowSearchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
