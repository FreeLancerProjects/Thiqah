package com.creative.share.apps.thiqah.services;


import com.creative.share.apps.thiqah.models.AboutAppModel;
import com.creative.share.apps.thiqah.models.Cities_Payment_Bank_Model;
import com.creative.share.apps.thiqah.models.CommentDataModel;
import com.creative.share.apps.thiqah.models.GuideModel;
import com.creative.share.apps.thiqah.models.NotificationDataModel;
import com.creative.share.apps.thiqah.models.OrderDataModel;
import com.creative.share.apps.thiqah.models.OrderIdModel;
import com.creative.share.apps.thiqah.models.UserModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Service {

    @GET("api/cities-shipping")
    Call<Cities_Payment_Bank_Model> getCity_Payment_Bank();

    @FormUrlEncoded
    @POST("/api/login")
    Call<UserModel> login(@Field("mobile_code") String mobile_code,
                          @Field("mobile_number") String mobile_number,
                          @Field("software_type") String software_type,
                          @Field("password") String password
    );


    @GET("/api/logout")
    Call<ResponseBody> logout(@Header("Authorization") String header);

    @FormUrlEncoded
    @POST("/api/register")
    Call<ResponseBody> sign_up(@Field("mobile_code") String mobile_code,
                               @Field("mobile_number") String mobile_number
    );

    @FormUrlEncoded
    @POST("/api/verify")
    Call<UserModel> verifyCode(@Field("mobile_code") String mobile_code,
                               @Field("mobile_number") String mobile_number,
                               @Field("software_type") String software_type,
                               @Field("password") String password,
                               @Field("verification") String code,
                               @Field("full_name") String full_name,
                               @Field("email") String email,
                               @Field("city_id") String city_id

    );


    @FormUrlEncoded
    @POST("/api/verify")
    Call<ResponseBody> reSendCode(@Field("mobile_code") String mobile_code,
                                  @Field("mobile_number") String mobile_number
    );

    @GET("/api/user-guides")
    Call<GuideModel> guide();

    @GET("/api/settings")
    Call<AboutAppModel> appData();

    @FormUrlEncoded
    @POST("/api/new-ticket")
    Call<ResponseBody> contact(@Field("name") String name,
                               @Field("phone_number") String mobile_number,
                               @Field("email") String email,
                               @Field("desc") String desc
    );


    @FormUrlEncoded
    @POST("/api/firebase-token")
    Call<ResponseBody> update_token(@Header("Authorization") String header,
                                    @Field("number_token") String number_token
    );

    @FormUrlEncoded
    @POST("api/visits-count")
    Call<ResponseBody> updateVisit(@Field("date") String date,
                                   @Field("type") int software_type

    );

    @Multipart
    @POST("api/buyer-create-order")
    Call<OrderIdModel> buyer_order1(@Header("Authorization") String header,
                                    @Part("seller_phone") RequestBody seller_phone,
                                    @Part("price") RequestBody price,
                                    @Part("reason") RequestBody reason,
                                    @Part("days_left") RequestBody days_left,
                                    @Part("conditions") RequestBody conditions,
                                    @Part("bank_account_id") RequestBody bank_account_id,
                                    @Part("shipping_type_id") RequestBody shipping_type_id,
                                    @Part MultipartBody.Part image

    );

    @Multipart
    @POST("api/seller-create-order")
    Call<OrderIdModel> seller_order1(@Header("Authorization") String header,
                                     @Part("buyer_phone") RequestBody buyer_phone,
                                     @Part("price") RequestBody price,
                                     @Part("reason") RequestBody reason,
                                     @Part("days_left") RequestBody days_left,
                                     @Part("conditions") RequestBody conditions,
                                     @Part("seller_bank_name") RequestBody seller_bank_name,
                                     @Part("seller_bank_account") RequestBody seller_bank_account,
                                     @Part("seller_bank_iban") RequestBody seller_bank_iban,
                                     @Part MultipartBody.Part image

    );

    @GET("api/all-orders")
    Call<OrderDataModel> getOrder(@Header("Authorization") String header,
                                  @Query("page") int page
    );

    @GET("api/notifications")
    Call<NotificationDataModel> getNotifications(@Header("Authorization") String header,
                                                 @Query("page") int page
    );


    @FormUrlEncoded
    @POST("/api/order-details")
    Call<OrderDataModel.OrderModel> getOrderDetails(@Header("Authorization") String header,
                                                    @Field("order_id") int order_id
    );

    @Multipart
    @POST("/api/seller-update-order")
    Call<ResponseBody> sellerUpdateOrder(@Header("Authorization") String header,
                                         @Part("seller_bank_name") RequestBody seller_bank_name,
                                         @Part("seller_bank_account") RequestBody seller_bank_account,
                                         @Part("seller_bank_iban") RequestBody seller_bank_iban,
                                         @Part("order_id") RequestBody order_id,
                                         @Part("notification_id") RequestBody notification_id,
                                         @Part MultipartBody.Part image
    );

    @Multipart
    @POST("/api/buyer-update-order")
    Call<ResponseBody> buyerUpdateOrder(@Header("Authorization") String header,
                                        @Part("bank_account_id") RequestBody bank_account_id,
                                        @Part("shipping_type_id") RequestBody shipping_type_id,
                                        @Part("order_id") RequestBody order_id,
                                        @Part("notification_id") RequestBody notification_id,
                                        @Part MultipartBody.Part image
    );

    @GET("api/last-order")
    Call<Integer> getOrderNumber(@Header("Authorization") String header
    );


    @GET("api/testimonials")
    Call<CommentDataModel> getAllComments(@Query("page") int page);


    @FormUrlEncoded
    @POST("/api/seller-finish-order")
    Call<ResponseBody> sellerFinishOrder(@Header("Authorization") String header,
                                         @Field("order_id") int order_id,
                                         @Field("comment") String comment,
                                         @Field("rate") int rate
    );

    @FormUrlEncoded
    @POST("/api/buyer-finish-order")
    Call<ResponseBody> buyerFinishOrder(@Header("Authorization") String header,
                                        @Field("order_id") int order_id,
                                        @Field("comment") String comment,
                                        @Field("rate") int rate
    );

    @FormUrlEncoded
    @POST("/api/confirm-money-received")
    Call<ResponseBody> confirmMoney(@Header("Authorization") String header,
                                   @Field("order_id") int order_id,
                                   @Field("notification_id") int notification_id
    );


}


