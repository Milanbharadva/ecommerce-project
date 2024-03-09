package com.webmintinfotech.ecom.api;

import com.webmintinfotech.ecom.model.BannerResponse;
import com.webmintinfotech.ecom.model.BrandDetailsResponse;
import com.webmintinfotech.ecom.model.BrandResponse;
import com.webmintinfotech.ecom.model.CategoriesResponse;
import com.webmintinfotech.ecom.model.CmsPageResponse;
import com.webmintinfotech.ecom.model.GetAddressResponse;
import com.webmintinfotech.ecom.model.GetCartResponse;
import com.webmintinfotech.ecom.model.GetCheckOutResponse;
import com.webmintinfotech.ecom.model.GetCouponResponse;
import com.webmintinfotech.ecom.model.GetFilterResponse;
import com.webmintinfotech.ecom.model.GetProductDetailsResponse;
import com.webmintinfotech.ecom.model.GetProfileResponse;
import com.webmintinfotech.ecom.model.GetVendorDetailsResponse;
import com.webmintinfotech.ecom.model.GetWishListResponse;
import com.webmintinfotech.ecom.model.HomefeedResponse;
import com.webmintinfotech.ecom.model.LoginModel;
import com.webmintinfotech.ecom.model.NotificationsResponse;
import com.webmintinfotech.ecom.model.OrderDetailsResponse;
import com.webmintinfotech.ecom.model.OrderHistoryResponse;
import com.webmintinfotech.ecom.model.OrderRetuenRequestResponse;
import com.webmintinfotech.ecom.model.PaymentListResponse;
import com.webmintinfotech.ecom.model.ProductResponse;
import com.webmintinfotech.ecom.model.ProductReviewResponse;
import com.webmintinfotech.ecom.model.RegistrationModel;
import com.webmintinfotech.ecom.model.SearchProductResponse;
import com.webmintinfotech.ecom.model.SubCategoriesResponse;
import com.webmintinfotech.ecom.model.TrackOrderResponse;
import com.webmintinfotech.ecom.model.VendorsDetailsResponse;
import com.webmintinfotech.ecom.model.VendorsResponse;
import com.webmintinfotech.ecom.model.ViewAllListResponse;
import com.webmintinfotech.ecom.model.WalletResponse;
import com.google.gson.JsonObject;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("login")
    Call<RestResponse<LoginModel>> getLogin(@Body HashMap<String, String> map);

    @POST("register")
    Call<RestResponse<RegistrationModel>> setRegistration(@Body HashMap<String, String> map);

    @POST("vendorsregister")
    Call<SingleResponse> setVendorsRegister(@Body HashMap<String, String> map);

    @POST("emailverify")
    Call<JsonObject> setEmailVerify(@Body HashMap<String, String> map);

    @POST("resendemailverification")
    Call<SingleResponse> setResendEmailVerification(@Body HashMap<String, String> map);

    @POST("forgotPassword")
    Call<SingleResponse> setforgotPassword(@Body HashMap<String, String> map);

    @POST("changepassword")
    Call<SingleResponse> setChangePassword(@Body HashMap<String, String> map);

    @POST("homefeeds")
    Call<HomefeedResponse> gethomefeeds(@Body HashMap<String, String> map);

    @GET("banner")
    Call<BannerResponse> getbanner();

    @GET("category")
    Call<CategoriesResponse> getcategory();

    @POST("subcategory")
    Call<SubCategoriesResponse> getSubCategoriesDetail(@Body HashMap<String, String> map);

    @POST("addtowishlist")
    Call<SingleResponse> setAddToWishList(@Body HashMap<String, String> map);

    @POST("removefromwishlist")
    Call<SingleResponse> setRemoveFromWishList(@Body HashMap<String, String> map);

    @POST("viewalllisting")
    Call<ViewAllListResponse> setViewAllListing(@Query("page") String page, @Body HashMap<String, String> map);

    @POST("getwishlist")
    Call<GetWishListResponse> getWishList(@Query("page") String page, @Body HashMap<String, String> map);

    @POST("getprofile")
    Call<GetProfileResponse> getProfile(@Body HashMap<String, String> map);

    @Multipart
    @POST("editprofile")
    Call<SingleResponse> setProfile(@Part("user_id") RequestBody userId,
                                    @Part("name") RequestBody name,
                                    @Part MultipartBody.Part profileimage);

    @POST("saveaddress")
    Call<SingleResponse> addAddress(@Body HashMap<String, String> map);

    @POST("getaddress")
    Call<GetAddressResponse> getAddress(@Body HashMap<String, String> map);

    @POST("deleteaddress")
    Call<SingleResponse> deleteAddress(@Body HashMap<String, String> map);

    @POST("editaddress")
    Call<SingleResponse> updateAddress(@Body HashMap<String, String> map);

    @POST("productdetails")
    Call<GetProductDetailsResponse> getProductDetails(@Body HashMap<String, String> map);

    @POST("vendorproducts")
    Call<GetVendorDetailsResponse> getVendorProducts(@Body HashMap<String, String> map);

    @POST("productreview")
    Call<ProductReviewResponse> getProductReview(@Body HashMap<String, String> map);

    @GET("brands")
    Call<BrandResponse> getBrands(@Query("page") String page);

    @POST("brandsproducts")
    Call<BrandDetailsResponse> getBrandDetails(@Query("page") String page, @Body HashMap<String, String> map);

    @GET("vendors")
    Call<VendorsResponse> getVendors(@Query("page") String page);

    @POST("vendorproducts")
    Call<VendorsDetailsResponse> getVendorsDetails(@Query("page") String page, @Body HashMap<String, String> map);

    @POST("notification")
    Call<NotificationsResponse> getNotificatios(@Query("page") String page, @Body HashMap<String, String> map);

    @POST("orderdetails")
    Call<OrderDetailsResponse> getOrderDetails(@Body HashMap<String, String> map);

    @POST("trackorder")
    Call<TrackOrderResponse> getTrackOrder(@Body HashMap<String, String> map);

    @GET("cmspages")
    Call<CmsPageResponse> getCmsData();

    @POST("orderhistory")
    Call<OrderHistoryResponse> getOrderHistory(@Query("page") String page, @Body HashMap<String, String> map);

    @POST("cancelorder")
    Call<SingleResponse> getCancelOrder(@Body HashMap<String, String> map);

    @POST("addtocart")
    Call<SingleResponse> getAddtocart(@Body HashMap<String, String> map);

    @POST("getcart")
    Call<GetCartResponse> getCartData(@Body HashMap<String, String> map);

    @POST("deleteproduct")
    Call<SingleResponse> deleteProduct(@Body HashMap<String, String> map);

    @POST("qtyupdate")
    Call<SingleResponse> qtyUpdate(@Body HashMap<String, String> map);

    @POST("checkout")
    Call<GetCheckOutResponse> getCheckOut(@Body HashMap<String, String> map);

    @POST("paymentlist")
    Call<PaymentListResponse> getPaymentList(@Body HashMap<String, String> map);

    @POST("order")
    Call<SingleResponse> setOrderPayment(@Body HashMap<String, String> map);

    @POST("searchproducts")
    Call<SearchProductResponse> getSearchProducts(@Body HashMap<String, String> map);

    @POST("filter")
    Call<GetFilterResponse> getFilter(@Query("page") String page, @Body HashMap<String, String> map);

    @POST("products")
    Call<ProductResponse> getProduct(@Query("page") String page, @Body HashMap<String, String> map);

    @GET("coupons")
    Call<GetCouponResponse> getCoupon(@Query("page") String page);

    @POST("wallet")
    Call<WalletResponse> getWallet(@Query("page") String page, @Body HashMap<String, String> map);

    @POST("returnconditions")
    Call<OrderRetuenRequestResponse> getOrderReturnRequest(@Body HashMap<String, String> map);

    @POST("returnrequest")
    Call<SingleResponse> returnRequest(@Body HashMap<String, String> map);

    @POST("addratting")
    Call<SingleResponse> addRatting(@Body HashMap<String, String> map);

    @POST("help")
    Call<SingleResponse> help(@Body HashMap<String, String> map);

    @POST("recharge")
    Call<SingleResponse> addMoney(@Body HashMap<String, String> map);

    @POST("changenotificationstatus")
    Call<SingleResponse> changeNotificationStatus(@Body HashMap<String, String> map);
}
