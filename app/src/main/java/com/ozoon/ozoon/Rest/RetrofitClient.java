package com.ozoon.ozoon.Rest;

import com.ozoon.ozoon.Utils.GMethods;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by NaderNabil216@gmail.com on 5/9/2018.
 */
public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static String baseUrl = GMethods.Base_Url;

    private static Retrofit getClient() {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES).build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

    public static APIService getService() {
        return getClient().create(APIService.class);
    }

}