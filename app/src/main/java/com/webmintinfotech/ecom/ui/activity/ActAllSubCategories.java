package com.webmintinfotech.ecom.ui.activity;

import android.os.Bundle;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.adapter.SubCateAdapter;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.databinding.ActAllSubCategoriesBinding;
import com.webmintinfotech.ecom.model.SubCategoriesResponse;
import com.webmintinfotech.ecom.model.SubcategoryItem;
import com.webmintinfotech.ecom.utils.Common;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.HashMap;

public class ActAllSubCategories extends BaseActivity {
    private ActAllSubCategoriesBinding allSubCategoriesBinding;
    private String cateId = "";
    private ArrayList<SubcategoryItem> cartSubList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;

    @Override
    public View setLayout() {
        return allSubCategoriesBinding.getRoot();
    }

    @Override
    public void initView() {
        allSubCategoriesBinding = ActAllSubCategoriesBinding.inflate(getLayoutInflater());
        allSubCategoriesBinding.ivBack.setOnClickListener(view -> {
            finish();
            setResult(RESULT_OK);
        });
        Bundle extras = getIntent().getExtras();
        String cat_id = extras.getString("cat_id");
        String categoryName = extras.getString("categoryName");
        allSubCategoriesBinding.Subcategoriename.setText(categoryName);
        cateId = getIntent().getStringExtra("cat_id");
        if (Common.isCheckNetwork(this)) {
            if (cat_id != null) {
                callApiSubCategories(cat_id);
            }
        } else {
            Common.alertErrorOrValidationDialog(
                    this,
                    getResources().getString(R.string.no_internet)
            );
        }
    }

    // TODO API SUB CATEGORIES CALL
    private void callApiSubCategories(String cat_id) {
        Common.showLoadingProgress(this);
        HashMap<String, String> map = new HashMap<>();
        map.put("cat_id", cat_id);
        Call<SubCategoriesResponse> call = ApiClient.getClient().getSubCategoriesDetail(map);
        call.enqueue(new Callback<SubCategoriesResponse>() {
            @Override
            public void onResponse(Call<SubCategoriesResponse> call, Response<SubCategoriesResponse> response) {
                if (response.code() == 200) {
                    Common.dismissLoadingProgress();
                    SubCategoriesResponse restResponce = response.body();
                    if (restResponce.getStatus() == 1) {
                        if (restResponce.getData().getSubcategory().size() > 0) {
                            allSubCategoriesBinding.rvSubcate.setVisibility(View.VISIBLE);
                            allSubCategoriesBinding.tvNoDataFound.setVisibility(View.GONE);
                            cartSubList = restResponce.getData().getSubcategory();
                            SubCateAdapter adapter = new SubCateAdapter(cartSubList);
                            allSubCategoriesBinding.rvSubcate.setLayoutManager(new LinearLayoutManager(ActAllSubCategories.this));
                            allSubCategoriesBinding.rvSubcate.setAdapter(adapter);
                        } else {
                            allSubCategoriesBinding.rvSubcate.setVisibility(View.GONE);
                            allSubCategoriesBinding.tvNoDataFound.setVisibility(View.VISIBLE);
                            Common.dismissLoadingProgress();
                        }
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(
                            ActAllSubCategories.this,
                            getResources().getString(R.string.error_msg)
                    );
                }
            }

            @Override
            public void onFailure(Call<SubCategoriesResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(
                        ActAllSubCategories.this,
                        getResources().getString(R.string.error_msg)
                );
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Common.getCurrentLanguage(this, false);
    }
}
