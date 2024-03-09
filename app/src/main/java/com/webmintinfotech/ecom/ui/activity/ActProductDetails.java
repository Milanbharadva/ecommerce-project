package com.webmintinfotech.ecom.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.base.BaseAdaptor;
import com.webmintinfotech.ecom.adapter.RelatedProductsAdapter;
import com.webmintinfotech.ecom.adapter.VariationAdapter;
import com.webmintinfotech.ecom.api.ApiClient;
import com.webmintinfotech.ecom.api.SingleResponse;
import com.webmintinfotech.ecom.base.BaseActivity;
import com.webmintinfotech.ecom.databinding.ActProductDetailsBinding;
import com.webmintinfotech.ecom.databinding.RowFeaturedproductBinding;
import com.webmintinfotech.ecom.databinding.RowProductviewpagerBinding;
import com.webmintinfotech.ecom.databinding.SuccessBottomsheetDialogBinding;
import com.webmintinfotech.ecom.model.GetProductDetailsResponse;
import com.webmintinfotech.ecom.model.ProductDetailsData;
import com.webmintinfotech.ecom.model.ProductimagesItem;
import com.webmintinfotech.ecom.model.RelatedProductsItem;
import com.webmintinfotech.ecom.model.VariationsItem;
import com.webmintinfotech.ecom.model.Vendors;
import com.webmintinfotech.ecom.ui.authentication.ActLogin;
import com.webmintinfotech.ecom.utils.Common;
import com.webmintinfotech.ecom.utils.SharePreference;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.HashMap;

public class ActProductDetails extends BaseActivity {

    private ActProductDetailsBinding productDetailsBinding;
    private ProductDetailsData productDetailsList;
    private ArrayList<RelatedProductsItem> relatedProducts = new ArrayList<>();
    private String currency = "";
    private String currencyPosition = "";
    private boolean isAPICalling = false;
    private String taxpercent = "";
    private double productprice = 0.0;
    private String addtax = "";
    private String price = "";
    private String variation = "";
    private int pos = 0;
    private String[] colorArray = {
            "#FDF7FF", "#FDF3F0", "#EDF7FD", "#FFFAEA", "#F1FFF6", "#FFF5EC"
    };
    private BaseAdaptor<RelatedProductsItem, RowFeaturedproductBinding> viewAllDataAdapter;
    private BaseAdaptor<ProductimagesItem, RowProductviewpagerBinding> productimageDataAdapter;
    private VariationAdapter variationAdaper;
    private ArrayList<VariationsItem> variationList = new ArrayList<>();
    private String paymenttype;

    @Override
    public View setLayout() {
        return productDetailsBinding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initView() {
        productDetailsBinding = ActProductDetailsBinding.inflate(getLayoutInflater());
        setupProductVariationAdapter(variationList);
        if (Common.isCheckNetwork(ActProductDetails.this)) {
            callApiProductDetail(getIntent().getStringExtra("product_id"));
        } else {
            Common.alertErrorOrValidationDialog(
                    ActProductDetails.this,
                    getResources().getString(R.string.no_internet)
            );
        }
        currency = SharePreference.getStringPref(ActProductDetails.this, SharePreference.Currency);
        currencyPosition = SharePreference.getStringPref(ActProductDetails.this, SharePreference.CurrencyPosition);
        productDetailsBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                setResult(RESULT_OK);
            }
        });
        productDetailsBinding.btnaddtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharePreference.getBooleanPref(ActProductDetails.this, SharePreference.isLogin)) {
                    if (productDetailsList != null) {
                        apiaddtocart(productDetailsList, variation);
                    }
                } else {
                    openActivity(ActLogin.class);
                    finish();
                }
            }
        });
    }

    private void callApiProductDetail(String productId) {
        Common.showLoadingProgress(this);
        HashMap<String, String> hasmap = new HashMap<>();
        hasmap.put("user_id", SharePreference.getStringPref(this, SharePreference.userId));
        hasmap.put("product_id", productId);
        Call<GetProductDetailsResponse> call = ApiClient.getClient().getProductDetails(hasmap);
        call.enqueue(new Callback<GetProductDetailsResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<GetProductDetailsResponse> call, Response<GetProductDetailsResponse> response) {
                if (response.code() == 200) {
                    GetProductDetailsResponse restResponce = response.body();
                    if (restResponce.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        productDetailsList = restResponce.getData();
                        loadProductDetails(productDetailsList);
                        if (restResponce.getRelatedProducts() != null && restResponce.getRelatedProducts().size() > 0) {
                            productDetailsBinding.rvstorelist.setVisibility(View.VISIBLE);
                            relatedProducts = restResponce.getRelatedProducts();
                            RelatedProductsAdapter adapter = new RelatedProductsAdapter(ActProductDetails.this, relatedProducts);
                            productDetailsBinding.rvstorelist.setLayoutManager(new GridLayoutManager(ActProductDetails.this, 1, GridLayoutManager.HORIZONTAL, false));
                            productDetailsBinding.rvstorelist.setAdapter(adapter);
                        } else {
                            productDetailsBinding.rvstorelist.setVisibility(View.GONE);
                        }
                        if (restResponce.getVendors() != null) {
                            loadVendorsData(restResponce.getVendors());
                        }

                        productDetailsBinding.clReturnpolicy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ActProductDetails.this, ActReturnPolicy.class);
                                intent.putExtra("return_policies", restResponce.getReturnpolicy().getReturnPolicies().toString());
                                startActivity(intent);
                            }
                        });

                    } else if (restResponce.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(ActProductDetails.this, restResponce.getMessage().toString());
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(ActProductDetails.this, getResources().getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(Call<GetProductDetailsResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActProductDetails.this, getResources().getString(R.string.error_msg));
            }
        });
    }

    //TODO VENDORS DATA SET
    private void loadVendorsData(Vendors vendors) {
        productDetailsBinding.tvVendorsName.setText(vendors.getName());
        Glide.with(ActProductDetails.this)
                .load(vendors.getImageUrl()).into(productDetailsBinding.ivvendors);

        if (vendors.getRattings() == null) {
            productDetailsBinding.tvvendorsrate.setText("0.0");
        } else {
            if (vendors.getRattings().size() > 0) {
                productDetailsBinding.tvvendorsrate.setText(vendors.getRattings().get(0).getAvgRatting().toString());
            } else {
                productDetailsBinding.tvvendorsrate.setText("0.0");
            }
        }

        productDetailsBinding.tvvisitstore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActProductDetails.this, ActVendorsDetails.class);
                intent.putExtra("vendor_id", vendors.getId().toString());
                intent.putExtra("vendors_name", vendors.getName());
                intent.putExtra("vendors_iv", vendors.getImageUrl());
                if ((vendors.getRattings() != null ? vendors.getRattings().size() : 0) > 0) {
                    intent.putExtra("vendors_rate", vendors.getRattings().get(0).getAvgRatting().toString());
                } else {
                    intent.putExtra("vendors_rate", "0.0");
                }
                startActivity(intent);
            }
        });
    }

//    TODO RELATED PRODUCT DETAILS DATA SET
//    private void loadRelatedProducts(ArrayList<RelatedProductsItem> relatedProducts) {
//        BaseAdaptor<RelatedProductsItem, RowFeaturedproductBinding> viewAllDataAdapter = new BaseAdaptor<RelatedProductsItem, RowFeaturedproductBinding>(ActProductDetails.this, relatedProducts) {
//            @SuppressLint({"NewApi", "ResourceType", "SetTextI18n"})
//            @Override
//            public void onBindData(RecyclerView.ViewHolder holder, RelatedProductsItem val, int position) {
//                RowFeaturedproductBinding binding = DataBindingUtil.bind(holder.itemView);
//                if (binding != null) {
//                    if (relatedProducts.get(position).isWishlist() == 0) {
//                        binding.ivwishlist.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_dislike, null));
//                    } else {
//                        binding.ivwishlist.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_like, null));
//                    }
//                    binding.ivwishlist.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (SharePreference.getBooleanPref(ActProductDetails.this, SharePreference.isLogin)) {
//                                if (relatedProducts.get(position).isWishlist() == 0) {
//                                    HashMap<String, String> map = new HashMap<>();
//                                    map.put("product_id", relatedProducts.get(position).getId().toString());
//                                    map.put("user_id", SharePreference.getStringPref(ActProductDetails.this, SharePreference.userId));
//                                    if (Common.isCheckNetwork(ActProductDetails.this)) {
//                                        callApiFavourite(map, position, relatedProducts);
//                                    } else {
//                                        Common.alertErrorOrValidationDialog(ActProductDetails.this, getResources().getString(R.string.no_internet));
//                                    }
//                                }
//                                if (relatedProducts.get(position).isWishlist() == 1) {
//                                    HashMap<String, String> map = new HashMap<>();
//                                    map.put("product_id", relatedProducts.get(position).getId().toString());
//                                    map.put("user_id", SharePreference.getStringPref(ActProductDetails.this, SharePreference.userId));
//                                    if (Common.isCheckNetwork(ActProductDetails.this)) {
//                                        callApiRemoveFavourite(map, position, relatedProducts);
//                                    } else {
//                                        Common.alertErrorOrValidationDialog(ActProductDetails.this, getResources().getString(R.string.no_internet));
//                                    }
//                                }
//                            } else {
//                                openActivity(ActLogin.class);
//                                finish();
//                            }
//                        }
//                    });
//                    if (relatedProducts.get(position).getRattings().size() == 0) {
//                        binding.tvRatePro.setText("0.0");
//                    } else {
//                        binding.tvRatePro.setText(relatedProducts.get(position).getRattings().get(0).getAvgRatting().toString());
//                    }
//                    binding.tvProductName.setText(relatedProducts.get(position).getProductName());
//                    binding.tvProductPrice.setText(Common.getPrice(currencyPosition, currency, relatedProducts.get(position).getProductPrice().toString()));
//
//                    if (relatedProducts.get(position).getDiscountedPrice().equals("0") || relatedProducts.get(position).getDiscountedPrice() == null) {
//                        binding.tvProductDisprice.setVisibility(View.GONE);
//                    } else {
//                        binding.tvProductDisprice.setText(Common.getPrice(currencyPosition, currency, relatedProducts.get(position).getDiscountedPrice().toString()));
//                    }
//
//                    Glide.with(ActProductDetails.this)
//                            .load(relatedProducts.get(position).getProductimage().getImageUrl())
//                            .transition(DrawableTransitionOptions.withCrossFade(1000))
//                            .into(binding.ivProduct);
//                    binding.ivProduct.setBackgroundColor(Color.parseColor(colorArray[position % 6]));
//                    holder.itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Log.e("product_id--->", relatedProducts.get(position).getProductimage().getProductId().toString());
//                            Intent intent = new Intent(ActProductDetails.this, ActProductDetails.class);
//                            intent.putExtra("product_id", relatedProducts.get(position).getProductimage().getProductId().toString());
//                            startActivity(intent);
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public int setItemLayout() {
//                return R.layout.row_featuredproduct;
//            }
//
//            @Override
//            public RowFeaturedproductBinding getBinding(ViewGroup parent) {
//                return RowFeaturedproductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
//            }
//        };
//
//        productDetailsBinding.rvstorelist.setLayoutManager(new GridLayoutManager(ActProductDetails.this, 1, GridLayoutManager.HORIZONTAL, false));
//        productDetailsBinding.rvstorelist.setItemAnimator(new DefaultItemAnimator());
//        productDetailsBinding.rvstorelist.setAdapter(viewAllDataAdapter);
//    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"ResourceAsColor", "SetTextI18n", "NotifyDataSetChanged", "UseCompatLoadingForDrawables"})
    private void loadProductDetails(ProductDetailsData productDetailsList) {
        if (productDetailsList.getRattings() != null && productDetailsList.getRattings().size() == 0) {
            productDetailsBinding.tvRatePro.setText("0.0");
        } else {
            productDetailsBinding.tvRatePro.setText(productDetailsList.getRattings().get(0).getAvgRatting().toString());
        }
        paymenttype = productDetailsList.getTaxType() != null ? productDetailsList.getTaxType() : "";
        productDetailsBinding.tvproduct.setText(productDetailsList.getCategoryName() + " | " + productDetailsList.getSubcategoryName() + " | " + productDetailsList.getInnersubcategoryName());
        productDetailsBinding.tvproducttitle.setText(productDetailsList.getProductName());
        productDetailsBinding.tvBarProductTitle.setText(productDetailsList.getProductName());
        if (productDetailsList.getReturnDays().equals("0")) {
            productDetailsBinding.clReturnpolicy.setVisibility(View.GONE);
            productDetailsBinding.viewreturn.setVisibility(View.GONE);
        } else {
            productDetailsBinding.tvreturnpilicy.setText(productDetailsList.getReturnDays() + " " + getString(R.string.day) + " " + getString(R.string.return_policies));
        }
        productDetailsBinding.tvProductPrice.setText(Common.getPrice(currencyPosition, currency, productDetailsList.getProductPrice().toString()));
        if (productDetailsList.getDiscountedPrice() == null || productDetailsList.getDiscountedPrice().equals("0")) {
            productDetailsBinding.tvProductDisprice.setVisibility(View.GONE);
        } else {
            productDetailsBinding.tvProductDisprice.setText(Common.getPrice(currencyPosition, currency, productDetailsList.getDiscountedPrice().toString()));
        }

        if (productDetailsList.getFreeShipping() == 1) {
            productDetailsBinding.tvshoppingcharge.setText(currency + "0.00");
        } else if (productDetailsList.getFreeShipping() == 2) {
            productDetailsBinding.tvshoppingcharge.setText(currency + Common.getPrice(currencyPosition, currency, productDetailsList.getShippingCost().toString()));
        }

        Log.d("isVariation", String.valueOf(productDetailsList.getVariations()));
        Log.d("availableStock", productDetailsList.getAvailableStock());
        if (productDetailsList.getIsVariation() == 1) {
            if (productDetailsList.getVariations().get(0).getQty().equals("0")) {
                productDetailsBinding.tvInstock.setText(getString(R.string.outofstock));
                productDetailsBinding.tvInstock.setTextColor(getColor(R.color.red));
                productDetailsBinding.btnaddtocart.setBackground(getDrawable(R.drawable.round_gray_bg_9));
                productDetailsBinding.btnaddtocart.setClickable(false);
            } else {
                productDetailsBinding.tvInstock.setText(getString(R.string.in_stock));
                productDetailsBinding.tvInstock.setTextColor(getColor(R.color.green));
                productDetailsBinding.btnaddtocart.setBackground(getDrawable(R.drawable.round_blue_bg_9));
                productDetailsBinding.btnaddtocart.setClickable(true);
            }
        } else if (productDetailsList.getIsVariation() == 0) {
            if (productDetailsList.getAvailableStock().equals("0")) {
                productDetailsBinding.tvInstock.setText(getString(R.string.outofstock));
                productDetailsBinding.tvInstock.setTextColor(getColor(R.color.red));
                productDetailsBinding.btnaddtocart.setBackground(getDrawable(R.drawable.round_gray_bg_9));
                productDetailsBinding.btnaddtocart.setClickable(false);
            } else {
                productDetailsBinding.tvInstock.setText(getString(R.string.in_stock));
                productDetailsBinding.tvInstock.setTextColor(getColor(R.color.green));
                productDetailsBinding.btnaddtocart.setBackground(getDrawable(R.drawable.round_blue_bg_9));
                productDetailsBinding.btnaddtocart.setClickable(true);
            }
        }
        if (productDetailsList.getIsWishlist() == 0) {
            productDetailsBinding.ivwishlist.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_dislike, null));
        } else {
            productDetailsBinding.ivwishlist.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_like, null));
        }
        productDetailsBinding.ivwishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharePreference.getBooleanPref(ActProductDetails.this, SharePreference.isLogin)) {
                    if (productDetailsList.getIsWishlist() == 0) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("product_id", productDetailsList.getId().toString());
                        map.put("user_id", SharePreference.getStringPref(ActProductDetails.this, SharePreference.userId));
                        if (Common.isCheckNetwork(ActProductDetails.this)) {
                            callApiFavouritePro(map);
                        } else {
                            Common.alertErrorOrValidationDialog(ActProductDetails.this, getResources().getString(R.string.no_internet));
                        }
                    }
                    if (productDetailsList.getIsWishlist() == 1) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("product_id", productDetailsList.getId().toString());
                        map.put("user_id", SharePreference.getStringPref(ActProductDetails.this, SharePreference.userId));
                        if (Common.isCheckNetwork(ActProductDetails.this)) {
                            callApiRemoveFavouritePro(map);
                        } else {
                            Common.alertErrorOrValidationDialog(ActProductDetails.this, getResources().getString(R.string.no_internet));
                        }
                    }
                } else {
                    openActivity(ActLogin.class);
                    finish();
                }
            }
        });
        productDetailsBinding.tvcode.setText(productDetailsList.getSku());
        if (productDetailsList.getAttribute() == null) {
            productDetailsBinding.tvproductdesc.setVisibility(View.GONE);
            productDetailsBinding.rvproductSize.setVisibility(View.GONE);
        } else {
            productDetailsBinding.tvproductdesc.setVisibility(View.VISIBLE);
            productDetailsBinding.tvproductdesc.setText(productDetailsList.getAttribute());
            productDetailsBinding.rvproductSize.setVisibility(View.VISIBLE);
        }
        taxpercent = productDetailsList.getTax();
        Log.d("Taxper--->", taxpercent);
        productprice = Double.parseDouble(productDetailsList.getProductPrice());
        Log.d("Price--->", Double.toString(productprice));
        double tax = productprice * Double.parseDouble(taxpercent) / 100;
        Log.d("Tax--->", Double.toString(tax));
        addtax = (paymenttype.equals("amount")) ? productDetailsList.getTax() : Double.toString(tax);
        if (productDetailsList.getTax() != null && !productDetailsList.getTax().equals("0")) {
            Log.d("tax", productDetailsList.getTax());
            productDetailsBinding.tvaddtax.setText(Common.getPrice(currencyPosition, currency, addtax) + " " + getString(R.string.add_tax));
            productDetailsBinding.tvaddtax.setTextColor(getColor(R.color.red));
        } else {
            productDetailsBinding.tvaddtax.setTextColor(getColor(R.color.green));
            productDetailsBinding.tvaddtax.setText(getString(R.string.inclusive_all_taxes));
        }
        if (productDetailsList.getFreeShipping() == 1) {
            productDetailsBinding.tvshoppingcharge.setVisibility(View.GONE);
            productDetailsBinding.tvshoppingchargetitle.setText(getString(R.string.freeshipping));
        } else {
            productDetailsBinding.tvshoppingcharge.setVisibility(View.VISIBLE);
            productDetailsBinding.tvshoppingcharge.setText(Common.getPrice(currencyPosition, currency, productDetailsList.getShippingCost().toString()));
        }
        productDetailsBinding.tvshoppingday.setText(productDetailsList.getEstShippingDays() + " " + getString(R.string.day));
        productDetailsBinding.clDescripition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActProductDetails.this, ActProductDescription.class);
                intent.putExtra("description", productDetailsList.getDescription());
                startActivity(intent);
            }
        });
        if (productDetailsList.getVariations() != null) {
            if (productDetailsList.getVariations().size() > 0) {
                productDetailsList.getVariations().get(0).setSelect(true);
                setupPriceData(productDetailsList.getVariations().get(0));
            }
            variationList.clear();
            variationList.addAll(productDetailsList.getVariations());
            variationAdaper.notifyDataSetChanged();
        }
        if (variationList.size() > 0) {
            productDetailsBinding.rvproductSize.setVisibility(View.VISIBLE);
            productDetailsBinding.tvproductdesc.setVisibility(View.VISIBLE);
        } else {
            productDetailsBinding.rvproductSize.setVisibility(View.GONE);
            productDetailsBinding.tvproductdesc.setVisibility(View.GONE);
        }
        ArrayList<SlideModel> imageList = new ArrayList<>();
        for (int i = 0; i < productDetailsList.getProductimages().size(); i++) {
            SlideModel slideModel = new SlideModel(productDetailsList.getProductimages().get(i).getImageUrl(), ScaleTypes.CENTER_CROP);
            imageList.add(slideModel);
        }
        productDetailsBinding.imageSlider.setImageList(imageList);
        productDetailsBinding.imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int position) {
                Intent intent = new Intent(ActProductDetails.this, ActImageSlider.class);
                intent.putParcelableArrayListExtra("imageList", productDetailsList.getProductimages());
                startActivity(intent);
            }
        });
        productDetailsBinding.imageSlider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        productDetailsBinding.clReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActProductDetails.this, ActReviews.class);
                intent.putExtra("product_id", productDetailsList.getId().toString());
                startActivity(intent);
            }
        });
    }

    private void callApiRemoveFavouritePro(HashMap<String, String> map) {
        if (isAPICalling) {
            return;
        }
        isAPICalling = true;
        Common.showLoadingProgress(ActProductDetails.this);
        Call<SingleResponse> call = ApiClient.getClient().setRemoveFromWishList(map);
        Log.e("remove-->", new Gson().toJson(map));
        call.enqueue(new Callback<SingleResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {
                if (response.code() == 200) {
                    SingleResponse restResponse = response.body();
                    if (restResponse != null && restResponse.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        if (productDetailsList != null) {
                            productDetailsList.setIsWishlist(0);
                            productDetailsBinding.ivwishlist.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_dislike, null));
                        }
                    } else if (restResponse != null && restResponse.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(ActProductDetails.this, restResponse.getMessage());
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(ActProductDetails.this, getResources().getString(R.string.error_msg));
                }
                isAPICalling = false;
            }

            @Override
            public void onFailure(Call<SingleResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActProductDetails.this, getResources().getString(R.string.error_msg));
                isAPICalling = false;
            }
        });
    }


    private void callApiFavouritePro(HashMap<String, String> map) {
        if (isAPICalling) {
            return;
        }
        isAPICalling = true;
        Common.showLoadingProgress(ActProductDetails.this);
        Call<SingleResponse> call = ApiClient.getClient().setAddToWishList(map);
        call.enqueue(new Callback<SingleResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {
                if (response.code() == 200) {
                    SingleResponse restResponse = response.body();
                    if (restResponse != null && restResponse.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        if (productDetailsList != null) {
                            productDetailsList.setIsWishlist(1);
                            productDetailsBinding.ivwishlist.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_like, null));
                        }
                    } else if (restResponse != null && restResponse.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(ActProductDetails.this, restResponse.getMessage());
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(ActProductDetails.this, getResources().getString(R.string.error_msg));
                }
                isAPICalling = false;
            }

            @Override
            public void onFailure(Call<SingleResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActProductDetails.this, getResources().getString(R.string.error_msg));
                isAPICalling = false;
            }
        });
    }


    private void callApiRemoveFavourite(final HashMap<String, String> map, final int position, final ArrayList<RelatedProductsItem> relatedProducts) {
        if (isAPICalling) {
            return;
        }
        isAPICalling = true;
        Common.showLoadingProgress(ActProductDetails.this);
        Call<SingleResponse> call = ApiClient.getClient().setRemoveFromWishList(map);
        Log.e("remove-->", new Gson().toJson(map));
        call.enqueue(new Callback<SingleResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {
                if (response.code() == 200) {
                    SingleResponse restResponse = response.body();
                    if (restResponse.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        relatedProducts.get(position).setIsWishlist(0);
                        viewAllDataAdapter.notifyItemChanged(position);

                    } else if (restResponse.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(ActProductDetails.this, restResponse.getMessage());
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(ActProductDetails.this, getResources().getString(R.string.error_msg));
                }
                isAPICalling = false;
            }

            @Override
            public void onFailure(Call<SingleResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActProductDetails.this, getResources().getString(R.string.error_msg));
                isAPICalling = false;
            }
        });
    }

    private void callApiFavourite(final HashMap<String, String> map, final int position, final ArrayList<RelatedProductsItem> relatedProducts) {
        if (isAPICalling) {
            return;
        }
        isAPICalling = true;
        Common.showLoadingProgress(ActProductDetails.this);
        Call<SingleResponse> call = ApiClient.getClient().setAddToWishList(map);
        call.enqueue(new Callback<SingleResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {
                if (response.code() == 200) {
                    SingleResponse restResponse = response.body();
                    if (restResponse.getStatus() == 1) {
                        Common.dismissLoadingProgress();
                        relatedProducts.get(position).setIsWishlist(1);
                        viewAllDataAdapter.notifyItemChanged(position);
                    } else if (restResponse.getStatus() == 0) {
                        Common.dismissLoadingProgress();
                        Common.alertErrorOrValidationDialog(ActProductDetails.this, restResponse.getMessage());
                    }
                } else {
                    Common.dismissLoadingProgress();
                    Common.alertErrorOrValidationDialog(ActProductDetails.this, getResources().getString(R.string.error_msg));
                }
                isAPICalling = false;
            }

            @Override
            public void onFailure(Call<SingleResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActProductDetails.this, getResources().getString(R.string.error_msg));
                isAPICalling = false;
            }
        });
    }

//    private void loadProductImage(ArrayList<ProductimagesItem> productimages) {
//        RowProductviewpagerBinding binding;
//        productimageDataAdapter = new BaseAdaptor<ProductimagesItem, RowProductviewpagerBinding>(this, productimages) {
//            @SuppressLint({"NewApi", "ResourceType", "SetTextI18n"})
//            @Override
//            public void onBindData(RecyclerView.ViewHolder holder, ProductimagesItem val, int position) {
//                Glide.with(ActProductDetails.this)
//                        .load(productimages.get(position).getImageUrl())
//                        .into(binding.ivProduct);
//                binding.ivProduct.setBackgroundColor(Color.parseColor(colorArray[position % 6]));
//            }
//
//            @Override
//            public int setItemLayout() {
//                return R.layout.row_productviewpager;
//            }
//
//            @Override
//            public RowProductviewpagerBinding getBinding(ViewGroup parent) {
//                binding = RowProductviewpagerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
//                return binding;
//            }
//        };
//
//        productDetailsBinding.viewPager.setAdapter(new StartScreenAdapter(this, productimages));
//        productDetailsBinding.tabLayout.setupWithViewPager(productDetailsBinding.viewPager, true);
//    }

    class StartScreenAdapter extends PagerAdapter {
        private Context mContext;
        private ArrayList<ProductimagesItem> mImagelist;

        StartScreenAdapter(Context context, ArrayList<ProductimagesItem> imagelist) {
            mContext = context;
            mImagelist = imagelist;
        }


        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.row_productviewpager, collection, false);
            ImageView iv = layout.findViewById(R.id.ivProduct);
            Glide.with(mContext).load(mImagelist.get(position).getImageUrl()).into(iv);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ActImageSlider.class);
                    intent.putParcelableArrayListExtra("imageList", mImagelist);
                    mContext.startActivity(intent);
                }
            });
            collection.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return mImagelist.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }

    private void apiaddtocart(ProductDetailsData productDetailsList, String variation) {
        Common.showLoadingProgress(this);
        productprice = productprice;
        String attribute;
        double tax;
        String addtax = null;
        if ("null".equals(productDetailsList.getAttribute())) {
            attribute = "";
        } else {
            attribute = productDetailsList.getAttribute();
        }
        paymenttype = productDetailsList.getTaxType();
        if ("amount".equals(productDetailsList.getTaxType())) {
            tax = Double.parseDouble(productDetailsList.getTax());
        } else {
            tax = Double.parseDouble(addtax);
        }
        String shippingcost = "";
        if (productDetailsList.getFreeShipping() == 1) {
            shippingcost = "0.00";
        } else if (productDetailsList.getFreeShipping() == 2) {
            shippingcost = productDetailsList.getShippingCost().toString();
        }
        HashMap<String, String> hasmap = new HashMap<>();
        hasmap.put("product_id", productDetailsList.getId().toString());
        hasmap.put("user_id", SharePreference.getStringPref(this, SharePreference.userId));
        hasmap.put("vendor_id", productDetailsList.getVendorId().toString());
        hasmap.put("product_name", productDetailsList.getProductName());
        hasmap.put("qty", "1");
        hasmap.put("price", String.valueOf(productprice));
        hasmap.put("variation", variation);
        hasmap.put("shipping_cost", shippingcost);
        hasmap.put("image", productDetailsList.getProductimages().get(0).getImageName());
        hasmap.put("tax", String.valueOf(tax));
        hasmap.put("attribute", attribute);
        Call<SingleResponse> call = ApiClient.getClient().getAddtocart(hasmap);
        call.enqueue(new Callback<SingleResponse>() {
            @Override
            public void onResponse(Call<SingleResponse> call, Response<SingleResponse> response) {
                Common.dismissLoadingProgress();
                if (response.code() == 200) {
                    if (response.body().getStatus() == 1) {
                        Common.isAddOrUpdated=true;
                        if (Common.isCheckNetwork(getApplicationContext())) {
                            successDialogBottomSheet();
                        } else {
                            Common.alertErrorOrValidationDialog(ActProductDetails.this, getResources().getString(R.string.no_internet));
                        }
                    } else {
                        Common.showErrorFullMsg(ActProductDetails.this, response.body().getMessage());
                    }
                } else {
                    Common.alertErrorOrValidationDialog(ActProductDetails.this, getResources().getString(R.string.error_cart));
                }
            }

            @Override
            public void onFailure(Call<SingleResponse> call, Throwable t) {
                Common.dismissLoadingProgress();
                Common.alertErrorOrValidationDialog(ActProductDetails.this, getResources().getString(R.string.error_msg));
            }
        });
    }

    private void successDialogBottomSheet() {
        SuccessBottomsheetDialogBinding successDialogBinding = SuccessBottomsheetDialogBinding.inflate(getLayoutInflater());

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(successDialogBinding.getRoot());

        successDialogBinding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        successDialogBinding.btnGotoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(ActProductDetails.this, ActMain.class);
                intent.putExtra("pos", "3");
                startActivity(intent);
                finish();
            }
        });
        successDialogBinding.btncontinueshopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(ActProductDetails.this, ActMain.class);
                intent.putExtra("temp", 0);
                startActivity(intent);
                finish();
            }
        });
        dialog.show();
    }

    @SuppressLint("InflateParams")
    private void dlgAddtoCartConformationDialog(Activity act, String msg) {
        Dialog dialog = null;
        try {
            if (dialog != null) {
                dialog.dismiss();
            }
            dialog = new Dialog(act, R.style.AppCompatAlertDialogStyleBig);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            LayoutInflater mInflater = LayoutInflater.from(act);
            View mView = mInflater.inflate(R.layout.dlg_addtocart, null, false);
            TextView textDesc = mView.findViewById(R.id.tvDesc);
            textDesc.setText(msg);
            TextView tvContinuation = mView.findViewById(R.id.tvcontinueshooping);
            dialog.setContentView(mView);
            final Dialog finalDialog = dialog;
            tvContinuation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Common.isCheckNetwork(ActProductDetails.this)) {
                        finalDialog.dismiss();
                        Intent intent = new Intent(ActProductDetails.this, ActMain.class);
                        intent.putExtra("temp", 0);
                        startActivity(intent);
                    } else {
                        Common.alertErrorOrValidationDialog(ActProductDetails.this, getResources().getString(R.string.no_internet));
                    }
                }
            });
            TextView tvGoToCart = mView.findViewById(R.id.tvgotocart);
            tvGoToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActProductDetails.this, ActMain.class);
                    intent.putExtra("pos", "3");
                    startActivity(intent);
                }
            });
            dialog.setContentView(mView);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void setupPriceData(VariationsItem data) {
        variation = (data.getVariation() == null || data.getVariation().equals("")) ? "" : data.getVariation();
        productDetailsBinding.tvProductPrice.setText(
                Common.getPrice(currencyPosition, currency, String.valueOf(data.getPrice())));
        productDetailsBinding.tvProductDisprice.setText(
                Common.getPrice(currencyPosition, currency, String.valueOf(data.getDiscountedVariationPrice())));
        productprice = Double.parseDouble(data.getPrice());
        double tax = productprice * Double.parseDouble(taxpercent) / 100;
        addtax = String.valueOf(tax);
        if (!productDetailsList.getTax().equals("0")) {
            productDetailsBinding.tvaddtax.setText(
                    Common.getPrice(currencyPosition, currency, addtax) +
                            " " +
                            getString(R.string.additional) +
                            getString(R.string.tax));
            productDetailsBinding.tvaddtax.setTextColor(
                    ResourcesCompat.getColor(getResources(), R.color.red, null));
        } else {
            productDetailsBinding.tvaddtax.setTextColor(
                    ResourcesCompat.getColor(getResources(), R.color.green, null));
            productDetailsBinding.tvaddtax.setText(getString(R.string.inclusive_all_taxes));
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("NotifyDataSetChanged")
    private void setupProductVariationAdapter(ArrayList<VariationsItem> variationList) {
        variationAdaper = new VariationAdapter(ActProductDetails.this, variationList, taxpercent, productDetailsList != null ? productDetailsList.getTax() : "0.00", productDetailsBinding, (i, s) -> {
            if (s.equals("ItemClick")) {
                for (VariationsItem item : variationList) {
                    item.setSelect(false);
                    if (productDetailsList != null && productDetailsList.getIsVariation() == 1) {
                        if (variationList.get(i).getQty().equals("0")) {
                            Log.d("qty", variationList.get(pos).getQty());
                            productDetailsBinding.tvInstock.setText(getString(R.string.outofstock));
                            productDetailsBinding.tvInstock.setTextColor(getColor(R.color.red));
                            productDetailsBinding.btnaddtocart.setBackground(getDrawable(R.drawable.round_gray_bg_9));
                            productDetailsBinding.btnaddtocart.setClickable(false);
                        } else {
                            productDetailsBinding.tvInstock.setText(getString(R.string.in_stock));
                            productDetailsBinding.tvInstock.setTextColor(getColor(R.color.green));
                            productDetailsBinding.btnaddtocart.setBackground(getDrawable(R.drawable.round_blue_bg_9));
                            productDetailsBinding.btnaddtocart.setClickable(true);
                        }
                    }
                }
                setupPriceData(variationList.get(i));
                variationList.get(i).setSelect(true);
                if (variationAdaper != null) {
                    variationAdaper.notifyDataSetChanged();
                }
            }
        });
        productDetailsBinding.rvproductSize.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        productDetailsBinding.rvproductSize.setItemAnimator(new DefaultItemAnimator());
        productDetailsBinding.rvproductSize.setAdapter(variationAdaper);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Common.isCheckNetwork(ActProductDetails.this)) {
            callApiProductDetail(getIntent().getStringExtra("product_id"));
        } else {
            Common.alertErrorOrValidationDialog(ActProductDetails.this, getResources().getString(R.string.no_internet));
        }
    }





}
