package com.ozoon.ozoon.Rest;

import com.ozoon.ozoon.Model.Responses.AllCategoriesResponse;
import com.ozoon.ozoon.Model.Responses.AllProductsResponse;
import com.ozoon.ozoon.Model.Responses.CountResponse;
import com.ozoon.ozoon.Model.Responses.GetProductReponse;
import com.ozoon.ozoon.Model.Responses.LoginResponse;
import com.ozoon.ozoon.Model.Responses.NotificationDetailResponse;
import com.ozoon.ozoon.Model.Responses.NotificationResponse;
import com.ozoon.ozoon.Model.Responses.SubCategoriesResponse;
import com.ozoon.ozoon.Model.Responses.SuccessResponse;
import com.ozoon.ozoon.Model.Responses.UploadImageResponse;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by NaderNabil216@gmail.com on 5/9/2018.
 */
public interface APIService {

    @GET("login")
    Call<LoginResponse> Login(@Query("username") String username,
                              @Query("password") String password,
                              @Query("token") String token);

    @GET("signup")
    Call<LoginResponse> SignUp(@Query("username") String email,
                               @Query("password") String password,
                               @Query("name") String name,
                               @Query("mobile") String mobile,
                               @Query("image") String image,
                               @Query("code") String code,
                               @Query("token") String token);

    @GET("delete_request")
    Call<SuccessResponse> DeleteRequest(@Query("type") String type,
                                        @Query("id") String id);

    @POST("upload_image")
    Call<UploadImageResponse> UploadImage(@Body RequestBody requestBody);

    @GET("send_feedback")
    Call<SuccessResponse> SendFeedback(@Query("id") String user_id,
                                       @Query("name") String user_name,
                                       @Query("email") String user_email,
                                       @Query("title") String title,
                                       @Query("comment") String body);

    @FormUrlEncoded
    @POST("get_category")
    Call<AllCategoriesResponse> GetAllCategories(@Field("user_id") String uid);

    @GET("get_products")
    Call<AllProductsResponse> GetAllProducts(@Query("user_id") String user_id,
                                             @Query("category_id") String category_id,
                                             @Query("subcategory_id") String subCategory_id,
                                             @Query("search") String search,
                                             @Query("rate") String rate,
                                             @Query("nearest") String nearest,
                                             @Query("lng") String lng,
                                             @Query("lat") String lat,
                                             @Query("sort") String sort);

    @GET("get_product")
    Call<GetProductReponse> GetProduct(@Query("id") String product_id,
                                       @Query("user_id") String userId);

    @GET("get_subcategory")
    Call<SubCategoriesResponse> GetSubCategories(@Query("category_id") String CategoryId);


    @GET("delivery_register")
    Call<SuccessResponse> DeliveryRegister(@Query("name") String name,
                                           @Query("body") String body,
                                           @Query("user_id") String userId,
                                           @Query("mobile") String mobile,
                                           @Query("car_number") String carNumber,
                                           @QueryMap Map<String, String> images);


    @GET("taxi_register")
    Call<SuccessResponse> TaxiRegister(@Query("name") String name,
                                       @Query("body") String body,
                                       @Query("user_id") String userId,
                                       @Query("mobile") String mobile,
                                       @Query("car_number") String carNumber,
                                       @QueryMap Map<String, String> images);


    @GET("add_product")
    Call<SuccessResponse> AddProduct(@Query("price") String price,
                                     @Query("name") String name,
                                     @Query("body") String body,
                                     @Query("category_id") String categoryId,
                                     @Query("user_id") String userId,
                                     @Query("subcategory_id") String subcategoryId,
                                     @Query("address") String address,
                                     @Query("longtide") String longtide,
                                     @Query("latitude") String latitude,
                                     @QueryMap Map<String, String> images);

    @GET("edit_product")
    Call<SuccessResponse> EditProduct(@Query("price") String price,
                                      @Query("name") String name,
                                      @Query("body") String body,
                                      @Query("category_id") String categoryId,
                                      @Query("user_id") String userId,
                                      @Query("subcategory_id") String subcategoryId,
                                      @Query("address") String address,
                                      @Query("longtide") String longtide,
                                      @Query("latitude") String latitude,
                                      @QueryMap Map<String, String> images,
                                      @Query("id") String id);

    @GET("taxi_request")
    Call<SuccessResponse> TaxiRequest(@Query("user_id") String user_id,
                                      @Query("long_to") String long_to,
                                      @Query("lat_to") String lat_to,
                                      @Query("address_to") String address_to,
                                      @Query("long_from") String long_from,
                                      @Query("lat_from") String lat_from,
                                      @Query("address_from") String address_from,
                                      @Query("city_to") String city_to,
                                      @Query("city_from") String city_from,
                                      @Query("price") String price);


    @GET("edit")
    Call<SuccessResponse> EditProfile(@Query("email") String email,
                                      @Query("password") String password,
                                      @Query("name") String name,
                                      @Query("mobile") String mobile,
                                      @Query("image") String image,
                                      @Query("id") String id);


    @GET("delivery_request")
    Call<SuccessResponse> DeliveryRequest(@Query("user_id") String user_id,
                                          @Query("long") String lng,
                                          @Query("lat") String lat,
                                          @Query("address") String address,
                                          @Query("city") String city,
                                          @Query("price") String price,
                                          @Query("title") String title);


    @GET("get_user")
    Call<LoginResponse> GetUser(@Query("id") String id, @Query("user_id") String user_id);

    @GET("follow")
    Call<SuccessResponse> FollowUser(@Query("user_id") String user_id,
                                     @Query("to_id") String to_id);


    @GET("add_review")
    Call<SuccessResponse> AddReview(@Query("rate") String rate,
                                    @Query("comment") String comment,
                                    @Query("user_id") String user_id,
                                    @Query("from_id") String from_id);

    @GET("delete_product")
    Call<SuccessResponse> DeleteProduct(@Query("id") String product_id,
                                        @Query("user_id") String user_id);


    @GET("notifications")
    Call<NotificationResponse> GetNotifications(@Query("taxi") String taxi,
                                                @Query("delivery") String delivery,
                                                @Query("type") String type,
                                                @Query("user_id") String user_id);

    @GET("get_taxi_request")
    Call<NotificationDetailResponse> GetNotificationDetail(@Query("id") String notificationId);


    @GET("get_delivery_request")
    Call<NotificationDetailResponse> GetDeliveryRequest(@Query("id") String id);


    @GET("accept_taxi")
    Call<SuccessResponse> AcceptTaxi(@Query("id") String id,
                                     @Query("user_id") String user_id);


    @GET("accept_delivery")
    Call<SuccessResponse> AcceptDelivery(@Query("id") String id,
                                         @Query("user_id") String user_id);

    @GET("delete_all_product")
    Call<SuccessResponse> DeleteAllProducts(@Query("user_id") String user_id);


    @GET("taxi_update")
    Call<SuccessResponse> UpdateTaxi(@Query("user_id") String id,
                                     @Query("status") String status);

    @GET("delivery_update")
    Call<SuccessResponse> UpdateDelivery(@Query("user_id") String id,
                                         @Query("status") String status);

    @GET("get_delivery_count")
    Call<CountResponse> GetDeliveryCount();


    @GET("get_taxi_count")
    Call<CountResponse> GetTaxiCount();

    @GET("add_favourite")
    Call<SuccessResponse> AddFavourite(@Query("user_id") String id,
                                       @Query("product_id") String productId);

    @GET("get_products")
    Call<AllProductsResponse> GetFavourite(@Query("favourite") String favourite,
                                         @Query("user_id") String userId);


}
