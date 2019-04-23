package com.ozoon.ozoon.Presenters;

import com.ozoon.ozoon.CallBacks.AllCategoriesCallBack;
import com.ozoon.ozoon.CallBacks.GetSubCategoriesCallBack;
import com.ozoon.ozoon.CallBacks.SuccessCallBack;
import com.ozoon.ozoon.CallBacks.UploadImageCallBack;
import com.ozoon.ozoon.Model.Responses.AllCategoriesResponse;
import com.ozoon.ozoon.Model.Responses.SubCategoriesResponse;
import com.ozoon.ozoon.Model.Responses.SuccessResponse;
import com.ozoon.ozoon.Model.Responses.UploadImageResponse;
import com.ozoon.ozoon.Rest.APIService;
import com.ozoon.ozoon.Rest.RetrofitClient;
import com.ozoon.ozoon.Utils.GMethods;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NaderNabil216@gmail.com on 5/10/2018.
 */
public class PublishAdsPresenter {
    public void AddImage(File Image, final UploadImageCallBack callBack) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("parameters[0]", Image.getName(), RequestBody.create(MediaType.parse("image/*"), Image));
        MultipartBody body = builder.build();
        APIService service = RetrofitClient.getService();
        Call<UploadImageResponse> call = service.UploadImage(body);
        call.enqueue(new Callback<UploadImageResponse>() {
            @Override
            public void onResponse(Call<UploadImageResponse> call, Response<UploadImageResponse> response) {
                if (response.code() == 200) {
                    callBack.OnSuccess(response.body().getImagePath());
                } else {
                    callBack.OnServerError();
                }
            }

            @Override
            public void onFailure(Call<UploadImageResponse> call, Throwable t) {
                callBack.OnFailure(t.getMessage());
            }
        });
    }



    public void deliverRegister(String name, String body, String userId, String mobile, String carNumber, Map<String,String> images, final SuccessCallBack callBack){
        APIService apiService = RetrofitClient.getService();
        Call<SuccessResponse> call = apiService.DeliveryRegister(name,body,userId,mobile,carNumber,images);
        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                if (response.code() == 200) {
                    GMethods.writeToLog(response.body().isStatus()+"");
                    callBack.OnSuccess();
                } else {
                    callBack.OnServerError();
                }

            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                callBack.OnFailure(t.getLocalizedMessage());

            }
        });
    }


    public void taxiRegister(String name, String body, String userId, String mobile, String carNumber, Map<String,String> images, final SuccessCallBack callBack){
        APIService apiService = RetrofitClient.getService();
        Call<SuccessResponse> call = apiService.TaxiRegister(name,body,userId,mobile,carNumber,images);
        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                if (response.code() == 200) {
                    GMethods.writeToLog(response.body().isStatus()+"");
                    callBack.OnSuccess();
                } else {
                    callBack.OnServerError();
                }

            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                callBack.OnFailure(t.getLocalizedMessage());

            }
        });
    }


    public void addProduct(String price,String name, String body,String categoryId, String userId,String subcategoryId,String address,String longtitde,String latitude, Map<String,String> images, final SuccessCallBack callBack){
        APIService apiService = RetrofitClient.getService();
        Call<SuccessResponse> call = apiService.AddProduct(price,name,body,categoryId,userId,subcategoryId,address,longtitde,latitude,images);
        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                if (response.code() == 200) {
                    GMethods.writeToLog(response.body().isStatus()+"");
                    callBack.OnSuccess();
                } else {
                    callBack.OnServerError();
                }

            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                callBack.OnFailure(t.getLocalizedMessage());

            }
        });
    }


    public void editProduct(String price,String name, String body,String categoryId, String userId,String subcategoryId,String address,String longtitde,String latitude, Map<String,String> images,String id, final SuccessCallBack callBack){
        APIService apiService = RetrofitClient.getService();
        Call<SuccessResponse> call = apiService.EditProduct(price,name,body,categoryId,userId,subcategoryId,address,longtitde,latitude,images,id);
        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                if (response.code() == 200) {
                    GMethods.writeToLog(response.body().isStatus()+"");
                    callBack.OnSuccess();
                } else {
                    callBack.OnServerError();
                }

            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                callBack.OnFailure(t.getLocalizedMessage());

            }
        });
    }



    public void GetAllCategories(String uid, final AllCategoriesCallBack callBack) {
        APIService apiService = RetrofitClient.getService();
        Call<AllCategoriesResponse> call = apiService.GetAllCategories(uid);
        call.enqueue(new Callback<AllCategoriesResponse>() {
            @Override
            public void onResponse(Call<AllCategoriesResponse> call, Response<AllCategoriesResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        callBack.OnSuccess(response.body().getCategories());
                    } else {
                        callBack.OnFailure(response.body().getMessage());
                    }
                } else {
                    callBack.OnServerError();
                }

            }

            @Override
            public void onFailure(Call<AllCategoriesResponse> call, Throwable t) {
                callBack.OnFailure(t.getLocalizedMessage());

            }
        });
    }

    public void GetSubCategories(String category_id, final GetSubCategoriesCallBack callBack) {
        APIService apiService = RetrofitClient.getService();
        apiService.GetSubCategories(category_id).enqueue(new Callback<SubCategoriesResponse>() {
            @Override
            public void onResponse(Call<SubCategoriesResponse> call, Response<SubCategoriesResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        callBack.OnSuccess(response.body().getSubcategories());
                    } else {
                        callBack.OnFailure(response.body().getMessage());
                    }
                } else {
                    callBack.OnServerError();
                }
            }

            @Override
            public void onFailure(Call<SubCategoriesResponse> call, Throwable t) {
                callBack.OnFailure(t.getLocalizedMessage());
            }
        });
    }


}
