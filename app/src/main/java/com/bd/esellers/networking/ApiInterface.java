package com.bd.esellers.networking;

import com.bd.esellers.cart.cartModel.CartRes;
import com.bd.esellers.cart.cartModel.Order;
import com.bd.esellers.category.categoryModel.CategoryResponse;
import com.bd.esellers.order.orderModel.OrderDetailsRes;
import com.bd.esellers.order.orderModel.OrderHistoryResponse;
import com.bd.esellers.order.reviewModel.ReviewGetResponse;
import com.bd.esellers.order.reviewModel.ReviewPostResponse;
import com.bd.esellers.userProfile.userModel.ProfileUpdateRes;
import com.bd.esellers.userProfile.userModel.Users;
import com.bd.esellers.mainApp.dataModel.SliderResponse;
import com.bd.esellers.product.productModel.ProductResponse;
import com.bd.esellers.userAuth.userAuthModels.AuthResponse;
import com.bd.esellers.userAuth.userAuthModels.LogoutResponse;
import com.bd.esellers.vendor.VendorResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {


    @FormUrlEncoded
    @POST("api/auth/user_register")
    Call<AuthResponse> createUser(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("api/auth/user_login")
    Call<AuthResponse> userSignin(@Field("email") String email, @Field("password") String password);


    @FormUrlEncoded
    @POST("api/auth/profile_update/{uid}")                  //uid=user id
    Call<ProfileUpdateRes> updateProfile(@Field("name") String name, @Field("email") String email,
                                         @Field("mobile") String mobile, @Field("user_profile_photo") String photo,
                                         @Field("address") String address, @Path("uid")  int id);    //token required


    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("api/auth/user_profile")
    Call<Users> getUserProfile();

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("api/auth/logout")
    Call<LogoutResponse> userLogout();


    @FormUrlEncoded
    @POST("api/review/save_review/{uid}/{pid}")
    Call<ReviewPostResponse> postReview(@Field("rating") int rating, @Field("review") String review,
                                        @Path("uid")  int uid, @Path("pid") int pid);

    @GET("api/review/get_review/{pid}")
    Call<ReviewGetResponse> getAllReviews(@Path("pid") int pid, @Query("page") int pageIndex);


    @GET("api/product/show_product")
    Call<ProductResponse> getAllProducts();


    @GET("api/order/orderDetails/{oid}")                            //oid=order id
    Call<OrderDetailsRes> getOrderDetails(@Path("oid") int id);


    @GET("api/order/customer_order/{cid}")                          //cid=customer id
    Call<OrderHistoryResponse> getOrder(@Path("cid") int id);


    @GET("api/product/category_product/{cid}")                      //cid=category id
    Call<ProductResponse> getProductsByCat(@Path("cid") int id);


    @GET("api/product/category_product/{cid}")                      //cid=category id
    Call<ProductResponse> getProductsByCat(@Path("cid") int id, @Query("page") int pageIndex);


    @GET("api/category/show_category")
    Call<CategoryResponse> getAllCategories();


    @GET("api/slider/show")
    Call<SliderResponse> getSliders();


    @FormUrlEncoded
    @POST("api/vendor/details")                                     // vid = vendor id
    Call<VendorResponse> getVendor(@Field("vendorId") int vid);


    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @POST("api/checkout/checkout")
    Call<CartRes> sendOrder(@Body Order order);







}
