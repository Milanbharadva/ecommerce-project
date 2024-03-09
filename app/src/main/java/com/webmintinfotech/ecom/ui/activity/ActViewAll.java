package com.webmintinfotech.ecom.ui.activity;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;

import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.adapter.FilterViewAdapter;
import com.webmintinfotech.ecom.adapter.ViewAllAdapter;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.databinding.ActViewAllBinding;
import com.webmintinfotech.ecom.model.FilterDataItem;
import com.webmintinfotech.ecom.model.GetFilterResponse;
import com.webmintinfotech.ecom.model.ViewAllDataItem;
import com.webmintinfotech.ecom.model.ViewAllListResponse;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.PaginationScrollListener;
import com.webmintinfotech.ecom.utils.SharePreference;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActViewAll extends BaseActivity {
    private ActViewAllBinding viewAllBinding;
    private ArrayList<ViewAllDataItem> viewAllDataList = new ArrayList<>();
    private ArrayList<FilterDataItem> filterAllDataList = new ArrayList<>();
    private String currency = "";
    private String currencyPosition = "";
    private ViewAllAdapter viewAllDataAdapter;
    private FilterViewAdapter filterAllDataAdapter;
    private GridLayoutManager gridLayoutManager;
    private GridLayoutManager gridLayoutManagerFilter;
    private int currentPage = 1;
    private int currentPageFilter = 1;
    private boolean isLoadingFilter = false;
    private boolean isLastPageFilter = false;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int total_pages = 0;
    private String protitle = "";
    private String[] colorArray = {
            "#FDF7FF",
            "#FDF3F0",
            "#EDF7FD",
            "#FFFAEA",
            "#F1FFF6",
            "#FFF5EC"
    };
    private String title = "";
    private String type = "";

    @Override
    public View setLayout() {
        return viewAllBinding.getRoot();
    }

    @Override
    public void initView() {
        viewAllBinding = ActViewAllBinding.inflate(getLayoutInflater());
        gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        gridLayoutManagerFilter = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        currency = SharePreference.getStringPref(this, SharePreference.Currency);
        currencyPosition = SharePreference.getStringPref(this, SharePreference.CurrencyPosition);
        title = getIntent().getStringExtra("title");
        viewAllBinding.tvviewall.setText(title);

        if (Common.isCheckNetwork(this)) {
            switch (title) {
                case "Featured Products":
                    viewAllBinding.rvFilterall.setVisibility(View.GONE);
                    viewAllBinding.rvViewall.setVisibility(View.VISIBLE);
                    title = "featured_products";
                    protitle = "featured_products";
                    viewAllData(title);
                    break;
                case "New Arrivals":
                    viewAllBinding.rvFilterall.setVisibility(View.GONE);
                    viewAllBinding.rvViewall.setVisibility(View.VISIBLE);
                    title = "new_products";
                    protitle = "new_products";
                    viewAllData(title);
                    break;
                case "Hot Deals":
                    viewAllBinding.rvFilterall.setVisibility(View.GONE);
                    viewAllBinding.rvViewall.setVisibility(View.VISIBLE);
                    title = "hot_products";
                    protitle = "hot_products";
                    viewAllData(title);
                    break;
            }
        } else {
            Common.alertErrorOrValidationDialog(this, getResources().getString(R.string.no_internet));
        }
        loadViewAll(viewAllDataList);
        paginationViewAll();

        viewAllBinding.ivBack.setOnClickListener(v -> finish());
        viewAllBinding.ivFilter.setOnClickListener(v -> {
            isLoadingFilter = false;
            isLastPageFilter = false;
            BottomSheetDialog dialog = new BottomSheetDialog(ActViewAll.this);
            if (Common.isCheckNetwork(ActViewAll.this)) {
                View view = getLayoutInflater().inflate(R.layout.row_bottomsheetsortby, null);
                TextView latest = view.findViewById(R.id.tvlatest);
                TextView pricelowtohigh = view.findViewById(R.id.tvpricelowtohigh);
                TextView pricehightolow = view.findViewById(R.id.tvpricehightolow);
                TextView rattinglowtohigh = view.findViewById(R.id.tvrattinglowtohigh);
                TextView rattinghightolow = view.findViewById(R.id.tvrattinghightolow);
                ImageView close = view.findViewById(R.id.iv_close);

                latest.setOnClickListener(v1 -> {
                    type = "new";
                    viewAllBinding.rvFilterall.setVisibility(View.VISIBLE);
                    viewAllBinding.rvViewall.setVisibility(View.GONE);
                    currentPageFilter = 1;
                    filterAllDataList.clear();
                    loadFilterAdapter(filterAllDataList);
                    callApiFilter(type);
                    dialog.dismiss();
                });
                pricelowtohigh.setOnClickListener(v12 -> {
                    type = "price-low-to-high";
                    viewAllBinding.rvFilterall.setVisibility(View.VISIBLE);
                    viewAllBinding.rvViewall.setVisibility(View.GONE);
                    currentPageFilter = 1;
                    filterAllDataList.clear();
                    loadFilterAdapter(filterAllDataList);
                    callApiFilter(type);
                    dialog.dismiss();
                });
                pricehightolow.setOnClickListener(v13 -> {
                    type = "price-high-to-low";
                    viewAllBinding.rvFilterall.setVisibility(View.VISIBLE);
                    viewAllBinding.rvViewall.setVisibility(View.GONE);
                    currentPageFilter = 1;
                    filterAllDataList.clear();
                    loadFilterAdapter(filterAllDataList);
                    callApiFilter(type);
                    dialog.dismiss();
                });
                rattinglowtohigh.setOnClickListener(v14 -> {
                    type = "ratting-low-to-high";
                    viewAllBinding.rvFilterall.setVisibility(View.VISIBLE);
                    viewAllBinding.rvViewall.setVisibility(View.GONE);
                    currentPageFilter = 1;
                    filterAllDataList.clear();
                    loadFilterAdapter(filterAllDataList);
                    callApiFilter(type);
                    dialog.dismiss();
                });
                rattinghightolow.setOnClickListener(v15 -> {
                    type = "ratting-high-to-low";
                    viewAllBinding.rvFilterall.setVisibility(View.VISIBLE);
                    viewAllBinding.rvViewall.setVisibility(View.GONE);
                    currentPageFilter = 1;
                    filterAllDataList.clear();
                    loadFilterAdapter(filterAllDataList);
                    callApiFilter(type);
                    dialog.dismiss();
                });
                paginationFilter();
                close.setOnClickListener(v16 -> dialog.dismiss());
                dialog.setCancelable(false);
                dialog.setContentView(view);
                dialog.show();
            } else {
                Common.alertErrorOrValidationDialog(ActViewAll.this, getResources().getString(R.string.no_internet));
                dialog.dismiss();
            }
        });
    }

    private void paginationFilter() {
        PaginationScrollListener paginationListener = new PaginationScrollListener(gridLayoutManagerFilter) {
            @Override
            public boolean isLastPage() {
                return isLastPageFilter;
            }

            @Override
            public boolean isLoading() {
                return isLoadingFilter;
            }

            @Override
            public void loadMoreItems() {
                isLoadingFilter = true;
                currentPageFilter++;
                callApiFilter(type);
            }
        };
        viewAllBinding.rvFilterall.addOnScrollListener(paginationListener);
    }

    private void paginationViewAll() {
        PaginationScrollListener paginationListener = new PaginationScrollListener(gridLayoutManager) {
            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public void loadMoreItems() {
                isLoading = true;
                currentPage++;
                viewAllData(title);
            }
        };
        viewAllBinding.rvViewall.addOnScrollListener(paginationListener);
    }

    private void loadViewAll(ArrayList<ViewAllDataItem> viewAllDataList) {
        viewAllDataAdapter = new ViewAllAdapter(ActViewAll.this, viewAllDataList);
        viewAllBinding.rvViewall.setLayoutManager(gridLayoutManager);
        viewAllBinding.rvViewall.setAdapter(viewAllDataAdapter);
    }

    private void loadFilterAdapter(ArrayList<FilterDataItem> filterAllDataList) {
        filterAllDataAdapter = new FilterViewAdapter(ActViewAll.this, filterAllDataList);
        viewAllBinding.rvFilterall.setLayoutManager(gridLayoutManagerFilter);
        viewAllBinding.rvFilterall.setAdapter(filterAllDataAdapter);
    }

    private void callApiFilter(String type) {
        Common.showLoadingProgress(ActViewAll.this);
        HashMap<String, String> hasmap = new HashMap<>();
        hasmap.put("user_id", SharePreference.getStringPref(ActViewAll.this, SharePreference.userId));
        hasmap.put("type", type);
        hasmap.put("product", protitle);
        if (getIntent().getSerializableExtra("innersubcategory_id").toString().equals("null")) {
            hasmap.put("innersubcat_id", "");
        } else {
            hasmap.put("innersubcat_id", getIntent().getSerializableExtra("innersubcategory_id").toString());
        }
        Call<GetFilterResponse> call = ApiClient.getClient().getFilter(currentPageFilter + "", hasmap);
        call.enqueue(new Callback<GetFilterResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<GetFilterResponse> call, Response<GetFilterResponse> response) {
                if (response.code() == 200) {
                    GetFilterResponse restResponse = response.body();
                    if (restResponse.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        if (restResponse.getData().getData().size() > 0) {
                            viewAllBinding.rvFilterall.setVisibility(View.VISIBLE);
                            viewAllBinding.tvNoDataFound.setVisibility(View.GONE);
                            currentPageFilter = restResponse.getData().getCurrentPage();
                            total_pages = restResponse.getData().getLastPage();
                            filterAllDataList.addAll(restResponse.getData().getData());
                            if (currentPageFilter >= total_pages) {
                                isLastPageFilter = true;
                            }
                            isLoadingFilter = false;
                        } else {
                            viewAllBinding.rvFilterall.setVisibility(View.GONE);
                            viewAllBinding.tvNoDataFound.setVisibility(View.VISIBLE);
                        }
                        filterAllDataAdapter.notifyDataSetChanged();
                    } else if (restResponse.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        if (restResponse.getMessage().equals("No data found")) {
                            viewAllBinding.tvNoDataFound.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(ActViewAll.this, getResources().getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(Call<GetFilterResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActViewAll.this, getResources().getString(R.string.error_msg));
            }
        });
    }

    private void viewAllData(String title) {
        Common.showLoadingProgress(ActViewAll.this);
        HashMap<String, String> hasmap = new HashMap<>();
        hasmap.put("user_id", SharePreference.getStringPref(ActViewAll.this, SharePreference.userId));
        hasmap.put("type", title);
        Call<ViewAllListResponse> call = ApiClient.getClient().setViewAllListing(currentPage + "", hasmap);
        call.enqueue(new Callback<ViewAllListResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<ViewAllListResponse> call, Response<ViewAllListResponse> response) {
                if (response.code() == 200) {
                    ViewAllListResponse restResponse = response.body();
                    if (restResponse.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        if (restResponse.getAlldata().getData().size() > 0) {
                            viewAllBinding.rvViewall.setVisibility(View.VISIBLE);
                            viewAllBinding.tvNoDataFound.setVisibility(View.GONE);
                            currentPage = restResponse.getAlldata().getCurrentPage();
                            total_pages = restResponse.getAlldata().getLastPage();
                            viewAllDataList.addAll(restResponse.getAlldata().getData());
                            if (currentPage >= total_pages) {
                                isLastPage = true;
                            }
                            isLoading = false;
                        } else {
                            viewAllBinding.rvViewall.setVisibility(View.GONE);
                            viewAllBinding.tvNoDataFound.setVisibility(View.VISIBLE);
                        }
                        viewAllDataAdapter.notifyDataSetChanged();
                    } else if (restResponse.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        if (restResponse.getMessage().equals("No data found")) {
                            viewAllBinding.tvNoDataFound.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(ActViewAll.this, getResources().getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(Call<ViewAllListResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActViewAll.this, getResources().getString(R.string.error_msg));
            }
        });
    }
}
