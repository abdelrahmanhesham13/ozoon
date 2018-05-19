package com.nadernabil216.wlaashal.Presenters;

import com.nadernabil216.wlaashal.CallBacks.AllCategoriesCallBack;
import com.nadernabil216.wlaashal.CallBacks.AllProductsCallBack;
import com.nadernabil216.wlaashal.CallBacks.GetProductCallBack;
import com.nadernabil216.wlaashal.CallBacks.GetSubCategoriesCallBack;
import com.nadernabil216.wlaashal.CallBacks.SuccessCallBack;
import com.nadernabil216.wlaashal.Model.Objects.Product;
import com.nadernabil216.wlaashal.Model.Responses.AllCategoriesResponse;
import com.nadernabil216.wlaashal.Model.Responses.AllProductsResponse;
import com.nadernabil216.wlaashal.Model.Responses.GetProductReponse;
import com.nadernabil216.wlaashal.Model.Responses.SubCategoriesResponse;
import com.nadernabil216.wlaashal.Model.Responses.SuccessResponse;
import com.nadernabil216.wlaashal.Rest.APIService;
import com.nadernabil216.wlaashal.Rest.RetrofitClient;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NaderNabil216@gmail.com on 5/9/2018.
 */
public class MainPresenter {

    public void SendFeedBack(String uid, String user_name, String email, String title, String body, final SuccessCallBack callBack) {
        APIService apiService = RetrofitClient.getService();
        Call<SuccessResponse> call = apiService.SendFeedback(uid, user_name, email, title, body);
        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        callBack.OnSuccess();
                    } else {
                        callBack.OnFailure(response.body().getMessage());
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

    /**
     * @param type 0 = profile ads -> user_id
     *             1 = category items -> category_id
     *             2 = sub category items -> subcategory_id
     *             3 = search items -> search
     */
    public void GetAllProducts(int type, String query, final AllProductsCallBack callBack) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        switch (type) {
            case 0:
                builder.addFormDataPart("user_id", query);
                break;
            case 1:
                builder.addFormDataPart("category_id", query);
                break;
            case 2:
                builder.addFormDataPart("subcategory_id", query);
                break;
            case 3:
                builder.addFormDataPart("search", query);
                break;
        }
        MultipartBody body = builder.build();
        APIService service = RetrofitClient.getService();
        service.GetAllProducts(body).enqueue(new Callback<AllProductsResponse>() {
            @Override
            public void onResponse(Call<AllProductsResponse> call, Response<AllProductsResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        callBack.OnSuccess(response.body().getProducts());
                    } else {
                        callBack.OnFailure(response.body().getMessage());
                    }
                } else {
                    callBack.OnServerError();
                }
            }

            @Override
            public void onFailure(Call<AllProductsResponse> call, Throwable t) {
                callBack.OnFailure(t.getLocalizedMessage());
            }
        });
    }

    public void GetProduct(String product_id, final GetProductCallBack callBack) {
        APIService apiService = RetrofitClient.getService();
        apiService.GetProduct(product_id).enqueue(new Callback<GetProductReponse>() {
            @Override
            public void onResponse(Call<GetProductReponse> call, Response<GetProductReponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        callBack.OnSuccess(GetFormatedProcuct(response));
                    } else {
                        callBack.OnFailure(response.body().getMessage());
                    }
                } else {
                    callBack.OnServerError();
                }
            }

            @Override
            public void onFailure(Call<GetProductReponse> call, Throwable t) {
                callBack.OnFailure(t.getLocalizedMessage());
            }
        });
    }

    private Product GetFormatedProcuct(Response<GetProductReponse> response) {
        ArrayList<String> imgs = response.body().getProduct().getImages();
        for (int i = 0; i < imgs.size(); i++) {
            imgs.set(i, response.body().getBase_url() + imgs.get(i));
        }
        Product product = response.body().getProduct();
        product.setImages(imgs);
        return product;
    }

    public void GetSubCategories(String category_id , final GetSubCategoriesCallBack callBack){
        APIService apiService = RetrofitClient.getService();
        apiService.GetSubCategories(category_id).enqueue(new Callback<SubCategoriesResponse>() {
            @Override
            public void onResponse(Call<SubCategoriesResponse> call, Response<SubCategoriesResponse> response) {
                if(response.code()==200){
                    if(response.body().isStatus()){
                        callBack.OnSuccess(response.body().getSubcategories());
                    }else {
                        callBack.OnFailure(response.body().getMessage());
                    }
                }else {
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
