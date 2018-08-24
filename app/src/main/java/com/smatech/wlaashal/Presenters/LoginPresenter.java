package com.smatech.wlaashal.Presenters;


import android.location.GnssMeasurementsEvent;

import com.smatech.wlaashal.CallBacks.LoginCallBack;
import com.smatech.wlaashal.CallBacks.UploadImageCallBack;
import com.smatech.wlaashal.Model.Responses.LoginResponse;
import com.smatech.wlaashal.Model.Responses.UploadImageResponse;
import com.smatech.wlaashal.Rest.APIService;
import com.smatech.wlaashal.Rest.RetrofitClient;
import com.smatech.wlaashal.Utils.GMethods;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NaderNabil216@gmail.com on 5/9/2018.
 */
public class LoginPresenter {

    public void Login(String user_name, String password,String token, final LoginCallBack callBack) {
        APIService apiService = RetrofitClient.getService();
        Call<LoginResponse> call = apiService.Login(user_name, password,token);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        callBack.OnSuccess(response.body().getUser());
                    } else {
                        callBack.OnFailure(response.body().getMessage());
                    }
                } else {
                    callBack.OnServerError();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callBack.OnFailure(t.getMessage());
            }
        });
    }


    public void SignUp(String email, String password, String name, String mobile, String Image,String code,String token, final LoginCallBack callBack) {
        APIService apiService = RetrofitClient.getService();
        Call<LoginResponse> call = apiService.SignUp(email, password, name, mobile, Image,code,token);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        callBack.OnSuccess(response.body().getUser());
                    } else {
                        callBack.OnFailure(response.body().getMessage());
                    }
                } else {
                    callBack.OnServerError();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callBack.OnFailure(t.getMessage());
            }
        });
    }

    public void AddSignUpImage(File Image, final UploadImageCallBack callBack) {
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


}
