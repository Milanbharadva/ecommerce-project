package com.webmintinfotech.ecom.ui.activity;

import android.annotation.SuppressLint;
import android.view.View;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.adapter.ProductViewAdapter;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.databinding.ActSubCateProductBinding;
import com.webmintinfotech.ecom.model.ProductDataItem;
import com.webmintinfotech.ecom.model.ProductResponse;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.SharePreference;
import java.util.ArrayList;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActSubCateProduct extends BaseActivity {
    private ActSubCateProductBinding binding;
    private int currentPage = 1;
    private int total_pages = 0;
    private int visibleItemCount = 0;
    private int totalItemCount = 0;
    private int pastVisibleItems = 0;
    private ArrayList<ProductDataItem> productAllDataList = new ArrayList<>();
    private GridLayoutManager gridLayoutManagerProduct;
    private ProductViewAdapter productAllDataAdapter;

    @Override
    public View setLayout() {
        return binding.getRoot();
    }

    @Override
    public void initView() {
        binding = ActSubCateProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.ivBack.setOnClickListener(v -> finish());
        gridLayoutManagerProduct = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        String title = getIntent().getStringExtra("title");
        binding.tvviewall.setText(title);
        binding.rvProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = gridLayoutManagerProduct.getChildCount();
                    totalItemCount = gridLayoutManagerProduct.getItemCount();
                    pastVisibleItems = gridLayoutManagerProduct.findFirstVisibleItemPosition();
                    if (currentPage < total_pages) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            currentPage += 1;
                            productData();
                        }
                    }
                }
            }
        });
    }

    private void productData() {
        Common.showLoadingProgress(this);
        HashMap<String, String> hasmap = new HashMap<>();
        hasmap.put("user_id", SharePreference.getStringPref(this, SharePreference.userId));
        hasmap.put("innersubcategory_id", String.valueOf(getIntent().getSerializableExtra("innersubcategory_id")));
        Call<ProductResponse> call = ApiClient.getClient().getProduct(String.valueOf(currentPage), hasmap);
        call.enqueue(new Callback<ProductResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.code() == 200) {
                    ProductResponse restResponce = response.body();
                    if (restResponce.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        if (restResponce.getData().getData().size() > 0) {
                            binding.rvProduct.setVisibility(View.VISIBLE);
                            binding.tvNoDataFound.setVisibility(View.GONE);
                            currentPage = Integer.parseInt(String.valueOf(restResponce.getData().getCurrentPage()));
                            total_pages = Integer.parseInt(String.valueOf(restResponce.getData().getLastPage()));
                            productAllDataList.addAll(restResponce.getData().getData());
                        } else {
                            binding.rvProduct.setVisibility(View.GONE);
                            binding.tvNoDataFound.setVisibility(View.VISIBLE);
                        }
                        productAllDataAdapter.notifyDataSetChanged();
                    } else if (restResponce.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        if (restResponce.getMessage().equals("No data found")) {
                            binding.tvNoDataFound.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActSubCateProduct.this, getResources().getString(R.string.error_msg));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentPage = 1;
        productAllDataList.clear();
        loadAdapter(productAllDataList);
        productData();
        Common.getCurrentLanguage(this, false);
    }

    private void loadAdapter(ArrayList<ProductDataItem> productAllDataList) {
        productAllDataAdapter = new ProductViewAdapter(this, productAllDataList);
        binding.rvProduct.setLayoutManager(gridLayoutManagerProduct);
        binding.rvProduct.setAdapter(productAllDataAdapter);
    }
}
