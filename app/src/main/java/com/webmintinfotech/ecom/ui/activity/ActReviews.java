package com.webmintinfotech.ecom.ui.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.base.BaseAdaptor;
import com.webmintinfotech.ecom.databinding.ActReviewsBinding;
import com.webmintinfotech.ecom.databinding.RowReviewsBinding;
import com.webmintinfotech.ecom.model.ProductReviewResponse;
import com.webmintinfotech.ecom.model.ReviewDataItem;
import com.webmintinfotech.ecom.model.Reviews;
import com.webmintinfotech.ecom.utils.Common;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActReviews extends BaseActivity {
    private ActReviewsBinding reviewsBinding;
    private Reviews productDetailsReviews;

    @Override
    public View setLayout() {
        return reviewsBinding.getRoot();
    }

    @Override
    public void initView() {
        reviewsBinding = ActReviewsBinding.inflate(LayoutInflater.from(this));
        if (Common.isCheckNetwork(this)) {
            callApiProductReview(getIntent().getStringExtra("product_id"));
        } else {
            Common.alertErrorOrValidationDialog(
                    this,
                    getResources().getString(R.string.no_internet)
            );
        }
        reviewsBinding.ivreviews.setOnClickListener(v -> openActivity(ActWriteReview.class));

        reviewsBinding.ivBack.setOnClickListener(v -> {
            finish();
            setResult(RESULT_OK);
        });
    }

    private void callApiProductReview(String productId) {
        Common.showLoadingProgress(this);
        HashMap<String, String> hasmap = new HashMap<>();
        hasmap.put("product_id", productId);
        Call<ProductReviewResponse> call = ApiClient.getClient().getProductReview(hasmap);
        call.enqueue(new Callback<ProductReviewResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<ProductReviewResponse> call, Response<ProductReviewResponse> response) {
                if (response.code() == 200) {
                    ProductReviewResponse restResponse = response.body();
                    if (restResponse.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        productDetailsReviews = restResponse.getReviews();
                        loadProductReview(productDetailsReviews);
                        loadProductReviewData(restResponse.getAllReview().getData());
//                        restResponse.getAllReview().getData().let(data->loadProductReviewData(data));
                    } else if (restResponse.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(
                                ActReviews.this,
                                restResponse.getMessage().toString()
                        );
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(
                            ActReviews.this,
                            getResources().getString(R.string.error_msg)
                    );
                }
            }

            @Override
            public void onFailure(Call<ProductReviewResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(
                        ActReviews.this,
                        getResources().getString(R.string.error_msg)
                );
            }
        });
    }

    private void loadProductReviewData(ArrayList<ReviewDataItem> reviewsUserData) {
        final RowReviewsBinding[] binding = {null}; // Declare as final array to be modified inside the inner class
        BaseAdaptor<ReviewDataItem, RowReviewsBinding> viewAllUserReviewsAdapter = new BaseAdaptor<ReviewDataItem, RowReviewsBinding>(this, reviewsUserData) {
            @SuppressLint({"NewApi", "ResourceType", "SetTextI18n"})
            @Override
            public void onBindData(RecyclerView.ViewHolder holder, ReviewDataItem val, int position) {
                binding[0].tvreviewsname.setText(reviewsUserData.get(position).getUsers().getName());
                binding[0].tvreviewsdate.setText(Common.getDate(reviewsUserData.get(position).getDate()));
                binding[0].tvreviewsdesc.setText(reviewsUserData.get(position).getComment());
                switch (reviewsUserData.get(position).getRatting()) {
                    case "0":
                    case "0.5":
                        binding[0].ivRatting.setImageResource(R.drawable.ratting0);
                        break;
                    case "1":
                    case "1.5":
                        binding[0].ivRatting.setImageResource(R.drawable.ratting1);
                        binding[0].ivRatting.setColorFilter(getColor(R.color.darkyellow));
                        break;
                    case "2":
                    case "2.5":
                        binding[0].ivRatting.setImageResource(R.drawable.ratting2);
                        binding[0].ivRatting.setColorFilter(getColor(R.color.darkyellow));
                        break;
                    case "3":
                    case "3.5":
                        binding[0].ivRatting.setImageResource(R.drawable.ratting3);
                        binding[0].ivRatting.setColorFilter(getColor(R.color.darkyellow));
                        break;
                    case "4":
                    case "4.5":
                        binding[0].ivRatting.setImageResource(R.drawable.ratting4);
                        binding[0].ivRatting.setColorFilter(getColor(R.color.darkyellow));
                        break;
                    case "5":
                        binding[0].ivRatting.setImageResource(R.drawable.ratting5);
                        binding[0].ivRatting.setColorFilter(getColor(R.color.darkyellow));
                        break;
                    default:
                        binding[0].ivRatting.setImageResource(R.drawable.ratting0);
                        break;
                }
                Glide.with(ActReviews.this)
                        .load(reviewsUserData.get(position).getUsers().getImageUrl())
                        .placeholder(ResourcesCompat.getDrawable(getResources(), R.drawable.profile, null))
                        .into(binding[0].ivusershop);
            }

            @Override
            public int setItemLayout() {
                return R.layout.row_reviews;
            }

            @Override
            public RowReviewsBinding getBinding(ViewGroup parent) {
                binding[0] = RowReviewsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return binding[0];
            }
        };

        reviewsBinding.rvReviewUserData.setLayoutManager(new LinearLayoutManager(ActReviews.this, LinearLayoutManager.HORIZONTAL, false));
        reviewsBinding.rvReviewUserData.setItemAnimator(new DefaultItemAnimator());
        reviewsBinding.rvReviewUserData.setAdapter(viewAllUserReviewsAdapter);

        if (reviewsUserData.size() > 0) {
            reviewsBinding.rvReviewUserData.setVisibility(View.VISIBLE);
            reviewsBinding.tvNoDataFound.setVisibility(View.GONE);
            reviewsBinding.cvReviews.setVisibility(View.VISIBLE);
        } else {
            reviewsBinding.rvReviewUserData.setVisibility(View.GONE);
            reviewsBinding.tvNoDataFound.setVisibility(View.VISIBLE);
            reviewsBinding.cvReviews.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    private void loadProductReview(Reviews productDetailsReviews) {
        reviewsBinding.tvrate.setText(productDetailsReviews.getAvgRatting() + " /" + " 5");
        switch (productDetailsReviews.getAvgRatting()) {
            case "0.0":
            case "0.5":
                reviewsBinding.ivRatting.setImageResource(R.drawable.ratting0);
                break;
            case "1.0":
            case "1.5":
                reviewsBinding.ivRatting.setImageResource(R.drawable.ratting1);
                reviewsBinding.ivRatting.setColorFilter(getColor(R.color.darkyellow));
                break;
            case "2.0":
            case "2.5":
                reviewsBinding.ivRatting.setImageResource(R.drawable.ratting2);
                reviewsBinding.ivRatting.setColorFilter(getColor(R.color.darkyellow));
                break;
            case "3.0":
            case "3.5":
                reviewsBinding.ivRatting.setImageResource(R.drawable.ratting3);
                reviewsBinding.ivRatting.setColorFilter(getColor(R.color.darkyellow));
                break;
            case "4.0":
            case "4.5":
                reviewsBinding.ivRatting.setImageResource(R.drawable.ratting4);
                reviewsBinding.ivRatting.setColorFilter(getColor(R.color.darkyellow));
                break;
            case "5.0":
                reviewsBinding.ivRatting.setImageResource(R.drawable.ratting5);
                reviewsBinding.ivRatting.setColorFilter(getColor(R.color.darkyellow));
                break;
            default:
                reviewsBinding.ivRatting.setImageResource(R.drawable.ratting0);
                break;
        }
        reviewsBinding.tvReviews.setText(getString(R.string.basedon) + " " + productDetailsReviews.getTotal() + " " + getString(R.string.ratings) + "\n" + getString(R.string.review));
        reviewsBinding.progress1.setMax(productDetailsReviews.getOneRatting());
        reviewsBinding.progress2.setMax(productDetailsReviews.getTwoRatting());
        reviewsBinding.progress3.setMax(productDetailsReviews.getThreeRatting());
        reviewsBinding.progress4.setMax(productDetailsReviews.getFourRatting());
        reviewsBinding.progress5.setMax(productDetailsReviews.getFiveRatting());
    }
}
