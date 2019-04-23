package com.ozoon.ozoon.Presenters;

import com.ozoon.ozoon.CallBacks.SuccessCallBack;
import com.ozoon.ozoon.Model.Responses.SuccessResponse;
import com.ozoon.ozoon.Rest.APIService;
import com.ozoon.ozoon.Rest.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestTaxiPresenter {


    public void RequestTaxi(String uid,String long_to,String lat_to,String address_to,String long_from,String lat_from,String address_from,String city_to,String city_from,String price, final SuccessCallBack callBack) {
        APIService apiService = RetrofitClient.getService();
        Call<SuccessResponse> call = apiService.TaxiRequest(uid, long_to, lat_to, address_to, long_from,lat_from,address_from,city_to,city_from,price);
        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        callBack.OnSuccess();
                    }
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

}
