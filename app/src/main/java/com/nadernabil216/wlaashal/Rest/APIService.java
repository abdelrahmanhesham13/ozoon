package com.nadernabil216.wlaashal.Rest;

import com.nadernabil216.wlaashal.Model.Responses.AllCategoriesResponse;
import com.nadernabil216.wlaashal.Model.Responses.AllProductsResponse;
import com.nadernabil216.wlaashal.Model.Responses.GetProductReponse;
import com.nadernabil216.wlaashal.Model.Responses.LoginResponse;
import com.nadernabil216.wlaashal.Model.Responses.SubCategoriesResponse;
import com.nadernabil216.wlaashal.Model.Responses.SuccessResponse;
import com.nadernabil216.wlaashal.Model.Responses.UploadImageResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by NaderNabil216@gmail.com on 5/9/2018.
 */
public interface APIService {

    @GET("login")
    Call<LoginResponse> Login(@Query("username") String username,
                              @Query("password") String password);

    @GET("signup")
    Call<LoginResponse> SignUp(@Query("username") String email,
                               @Query("password") String password,
                               @Query("name") String name,
                               @Query("mobile") String mobile,
                               @Query("image") String image);

    @POST("upload_image")
    Call<UploadImageResponse> UploadImage(@Body RequestBody requestBody);

    @GET("send_feedback")
    Call<SuccessResponse> SendFeedback(@Query("user_id") String user_id,
                                       @Query("user_name") String user_name,
                                       @Query("user_email") String user_email,
                                       @Query("title") String title,
                                       @Query("body") String body);

    @FormUrlEncoded
    @POST("get_category")
    Call<AllCategoriesResponse> GetAllCategories(@Field("user_id") String uid );

    @POST("get_products")
    Call<AllProductsResponse> GetAllProducts (@Body RequestBody body);

    @FormUrlEncoded
    @POST("get_product")
    Call<GetProductReponse> GetProduct(@Field("id") String product_id);

    @FormUrlEncoded
    @POST("get_subcategory")
    Call<SubCategoriesResponse> GetSubCategories(@Field("category_id") String CategoryId);



}
