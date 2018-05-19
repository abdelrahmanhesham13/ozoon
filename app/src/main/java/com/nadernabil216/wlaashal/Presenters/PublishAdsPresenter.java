package com.nadernabil216.wlaashal.Presenters;

import com.nadernabil216.wlaashal.CallBacks.AllCategoriesCallBack;
import com.nadernabil216.wlaashal.CallBacks.GetSubCategoriesCallBack;
import com.nadernabil216.wlaashal.CallBacks.UploadImageCallBack;
import com.nadernabil216.wlaashal.Model.Responses.AllCategoriesResponse;
import com.nadernabil216.wlaashal.Model.Responses.SubCategoriesResponse;
import com.nadernabil216.wlaashal.Model.Responses.UploadImageResponse;
import com.nadernabil216.wlaashal.Rest.APIService;
import com.nadernabil216.wlaashal.Rest.RetrofitClient;

import java.io.File;

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
                    if (response.body().isStatus()) {
                        callBack.OnSuccess(response.body().getImagePath());
                    } else {
                        callBack.OnFailure(response.body().getMessage());
                    }
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
