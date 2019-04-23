package com.ozoon.ozoon.Presenters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.ozoon.ozoon.CallBacks.AllCategoriesCallBack;
import com.ozoon.ozoon.CallBacks.AllNotificationsCallBack;
import com.ozoon.ozoon.CallBacks.AllProductsCallBack;
import com.ozoon.ozoon.CallBacks.GetProductCallBack;
import com.ozoon.ozoon.CallBacks.GetSubCategoriesCallBack;
import com.ozoon.ozoon.CallBacks.LoginCallBack;
import com.ozoon.ozoon.CallBacks.NotificationDetailCallBack;
import com.ozoon.ozoon.CallBacks.SuccessCallBack;
import com.ozoon.ozoon.CallBacks.UploadImageCallBack;
import com.ozoon.ozoon.Model.Objects.Product;
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
import com.ozoon.ozoon.Rest.APIService;
import com.ozoon.ozoon.Rest.RetrofitClient;
import com.ozoon.ozoon.UI.Activities.AdvertDetails;
import com.ozoon.ozoon.Utils.GMethods;
import com.ozoon.ozoon.Utils.Helper;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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


    public void AddFavorite(final Context context, String uid, String productId, final SuccessCallBack callBack) {
        APIService apiService = RetrofitClient.getService();
        Call<SuccessResponse> call = apiService.AddFavourite(uid,productId);
        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        Toast.makeText(context,response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        callBack.OnSuccess();
                    } else {
                        Helper.writeToLog(String.valueOf(response.message()));
                        callBack.OnFailure(response.body().getMessage());
                    }
                } else {
                    Helper.writeToLog(String.valueOf(response.raw()));
                    callBack.OnServerError();
                }

            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                t.printStackTrace();
                callBack.OnFailure(t.getLocalizedMessage());
            }
        });
    }

    public void updateTaxi(String uid, String status, final SuccessCallBack callBack) {
        APIService apiService = RetrofitClient.getService();
        Call<SuccessResponse> call = apiService.UpdateTaxi(uid, status);
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


    public void getTaxiCount(final SuccessCallBack callBack) {
        APIService apiService = RetrofitClient.getService();
        Call<CountResponse> call = apiService.GetTaxiCount();
        call.enqueue(new Callback<CountResponse>() {
            @Override
            public void onResponse(Call<CountResponse> call, Response<CountResponse> response) {
                if (response.code() == 200) {
                    if (!response.body().isStatus()) {
                        callBack.OnSuccess();
                    } else {
                        callBack.OnFailure(response.body().getCount()+"");
                    }
                } else {
                    callBack.OnServerError();
                }
            }

            @Override
            public void onFailure(Call<CountResponse> call, Throwable t) {
                callBack.OnFailure(t.getLocalizedMessage());
            }
        });
    }

    public void getDeliveryCount(final SuccessCallBack callBack) {
        APIService apiService = RetrofitClient.getService();
        Call<CountResponse> call = apiService.GetDeliveryCount();
        call.enqueue(new Callback<CountResponse>() {
            @Override
            public void onResponse(Call<CountResponse> call, Response<CountResponse> response) {
                if (response.code() == 200) {
                    if (!response.body().isStatus()) {
                        callBack.OnSuccess();
                    } else {
                        callBack.OnFailure(response.body().getCount()+"");
                    }
                } else {
                    callBack.OnServerError();
                }
            }

            @Override
            public void onFailure(Call<CountResponse> call, Throwable t) {
                callBack.OnFailure(t.getLocalizedMessage());
            }
        });
    }

    public void updateDelivery(String uid, String status, final SuccessCallBack callBack) {
        APIService apiService = RetrofitClient.getService();
        Call<SuccessResponse> call = apiService.UpdateDelivery(uid, status);
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


    public void deleteRequest(String type, String id, final SuccessCallBack callBack) {
        APIService apiService = RetrofitClient.getService();
        Call<SuccessResponse> call = apiService.DeleteRequest(type, id);
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


    public void deliveryRequest(String uid,String lng,String lat,String address,String city,String price,String title,final SuccessCallBack callBack){
        APIService apiService = RetrofitClient.getService();
        Call<SuccessResponse> call = apiService.DeliveryRequest(uid,lng,lat,address,city,price,title);
        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                if (response.code() == 200) {
                    GMethods.writeToLog(response.body().isStatus()+"");
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


    public void editProfile(String uid, String user_name, String email, String image, String mobile,String password, final SuccessCallBack callBack) {
        APIService apiService = RetrofitClient.getService();
        Call<SuccessResponse> call = apiService.EditProfile(email,password,user_name,mobile,image,uid);
        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                if (response.code() == 200) {
                    GMethods.writeToLog(response.body().isStatus()+"");
                    if (response.body().isStatus()) {
                        callBack.OnSuccess();
                    } else {
                        callBack.OnFailure("رقم الجوال او البريد موجود مسبقا");
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
        String user_id = "";
        String category_id = "";
        String subcategory_id = "";
        String search = "";
        String rate = "";
        String nearest = "";
        String sort = "";
        switch (type) {
            case 0:
                builder.addFormDataPart("user_id", query);
                user_id = query;
                break;
            case 1:
                builder.addFormDataPart("category_id", query);
                category_id = query;
                break;
            case 2:
                builder.addFormDataPart("subcategory_id", query);
                subcategory_id = query;
                break;
            case 3:
                builder.addFormDataPart("search", query);
                search = query;
                break;
            case 4:
                rate = "true";
            case 5:
                nearest = "true";
            case 6:
                builder.addFormDataPart("search", query);
                sort = query;
        }
        MultipartBody body = builder.build();
        APIService service = RetrofitClient.getService();
        service.GetAllProducts(user_id,category_id,subcategory_id,search,rate,nearest,"0","0",sort).enqueue(new Callback<AllProductsResponse>() {
            @Override
            public void onResponse(Call<AllProductsResponse> call, Response<AllProductsResponse> response) {
                if (response.code() == 200) {
                    GMethods.writeToLog(response.body().isStatus()+"");
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


    public void GetAllProductsCategory(int type, String query,String category, final AllProductsCallBack callBack) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        String user_id = "";
        String category_id = category;
        String subcategory_id = "";
        String search = "";
        String rate = "";
        String nearest = "";
        String sort = "";
        switch (type) {
            case 0:
                builder.addFormDataPart("user_id", query);
                user_id = query;
                break;
            case 1:
                builder.addFormDataPart("category_id", query);
                category_id = query;
                break;
            case 2:
                builder.addFormDataPart("subcategory_id", query);
                subcategory_id = query;
                break;
            case 3:
                builder.addFormDataPart("search", query);
                search = query;
                break;
            case 4:
                rate = "true";
            case 5:
                nearest = "true";
            case 6:
                builder.addFormDataPart("search", query);
                sort = query;
        }
        MultipartBody body = builder.build();
        APIService service = RetrofitClient.getService();
        service.GetAllProducts(user_id,category_id,subcategory_id,search,rate,nearest,"0","0",sort).enqueue(new Callback<AllProductsResponse>() {
            @Override
            public void onResponse(Call<AllProductsResponse> call, Response<AllProductsResponse> response) {
                if (response.code() == 200) {
                    GMethods.writeToLog(response.body().isStatus()+"");
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


    public void GetFavourite(String userId, final AllProductsCallBack callBack) {
        APIService service = RetrofitClient.getService();
        service.GetFavourite("1",userId).enqueue(new Callback<AllProductsResponse>() {
            @Override
            public void onResponse(Call<AllProductsResponse> call, Response<AllProductsResponse> response) {
                if (response.code() == 200) {
                    GMethods.writeToLog(response.body().isStatus()+"");
                    Helper.writeToLog(String.valueOf(response.raw()));
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


    public void GetProduct(String product_id,String userId, final GetProductCallBack callBack) {
        APIService apiService = RetrofitClient.getService();
        apiService.GetProduct(product_id,userId).enqueue(new Callback<GetProductReponse>() {
            @Override
            public void onResponse(Call<GetProductReponse> call, Response<GetProductReponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        GMethods.writeToLog(GetFormatedProcuct(response).getBody());
                        Helper.writeToLog("product response : " + response.toString());
                        Helper.writeToLog(response.body().getProduct().getCategory_name());
                        callBack.OnSuccess(GetFormatedProcuct(response));
                    } else {

                        callBack.OnFailure(response.body().getMessage());
                    }
                } else {
                    Helper.writeToLog(response.toString());
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
        /*for (int i = 0; i < imgs.size(); i++) {
            imgs.set(i, GMethods.IMAGE_URL + imgs.get(i));
        }*/
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
                    GMethods.writeToLog(response.body().isStatus()+"");
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


    public void getUser(String id,String user_id,final LoginCallBack callBack) {
        APIService apiService = RetrofitClient.getService();
        Call<LoginResponse> call = apiService.GetUser(id,user_id);
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


    public void followUser(String user_id,String to_id,final SuccessCallBack callBack) {
        APIService apiService = RetrofitClient.getService();
        Call<SuccessResponse> call = apiService.FollowUser(user_id,to_id);
        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        callBack.OnFailure(response.body().getMessage());
                    } else {
                        callBack.OnSuccess();
                    }
                } else {
                    callBack.OnServerError();
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                callBack.OnFailure(t.getMessage());
            }
        });
    }


    public void addReview(String rate,String comment,String user_id,String from_id,final SuccessCallBack callBack) {
        APIService apiService = RetrofitClient.getService();
        Call<SuccessResponse> call = apiService.AddReview(rate,comment,user_id,from_id);
        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                if (response.code() == 200) {
                    Helper.writeToLog(response.body().isStatus()+"");
                    Helper.writeToLog(response.body().getMessage()+ " ");
                    if (response.body().isStatus()) {
                        callBack.OnFailure(response.body().getMessage());
                    } else {
                        callBack.OnFailure(response.body().getMessage());
                    }
                } else {
                    callBack.OnSuccess();
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                callBack.OnFailure(t.getMessage());
            }
        });
    }

    public void deleteProduct(String product_id,String user_id,final SuccessCallBack callBack) {
        APIService apiService = RetrofitClient.getService();
        Call<SuccessResponse> call = apiService.DeleteProduct(product_id,user_id);
        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        callBack.OnFailure(response.body().getMessage());
                    } else {
                        callBack.OnSuccess();
                    }
                } else {
                    callBack.OnServerError();
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                callBack.OnFailure(t.getMessage());
            }
        });
    }


    public void getNotifications (String taxi, String delivery, String type, String user_id, final AllNotificationsCallBack callBack){
        APIService apiService = RetrofitClient.getService();
        Call<NotificationResponse> call = apiService.GetNotifications(taxi,delivery,type,user_id);
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if (response.code() == 200) {
                    Helper.writeToLog(response.body().isStatus()+"");
                    if (response.body().isStatus()) {
                        callBack.OnSuccess(response.body().getRequests());
                    } else {
                        callBack.OnFailure("Fail");
                        Helper.writeToLog("Fail");
                    }
                } else {
                    callBack.OnServerError();
                    Helper.writeToLog("Server Error");
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                callBack.OnFailure(t.getMessage());
            }
        });
    }



    public void getNotificationDetail (String id, final NotificationDetailCallBack callBack){
        APIService apiService = RetrofitClient.getService();
        Call<NotificationDetailResponse> call = apiService.GetNotificationDetail(id);
        call.enqueue(new Callback<NotificationDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<NotificationDetailResponse> call, @NonNull Response<NotificationDetailResponse> response) {
                if (response.code() == 200) {
                    Helper.writeToLog(response.body().isStatus()+"");
                    if (response.body().isStatus()) {
                        callBack.OnSuccess(response.body());
                    } else {
                        callBack.OnFailure("Fail");
                        Helper.writeToLog("Fail");
                    }
                } else {
                    callBack.OnServerError();
                    Helper.writeToLog("Server Error");
                }
            }

            @Override
            public void onFailure(Call<NotificationDetailResponse> call, Throwable t) {
                callBack.OnFailure(t.getMessage());
            }
        });
    }


    public void getDeliverDetail (String id, final NotificationDetailCallBack callBack){
        APIService apiService = RetrofitClient.getService();
        Call<NotificationDetailResponse> call = apiService.GetDeliveryRequest(id);
        call.enqueue(new Callback<NotificationDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<NotificationDetailResponse> call, @NonNull Response<NotificationDetailResponse> response) {
                if (response.code() == 200) {
                    Helper.writeToLog(response.body().isStatus()+"");
                    if (response.body().isStatus()) {
                        callBack.OnSuccess(response.body());
                    } else {
                        callBack.OnFailure("Fail");
                        Helper.writeToLog("Fail");
                    }
                } else {
                    callBack.OnServerError();
                    Helper.writeToLog("Server Error");
                }
            }

            @Override
            public void onFailure(Call<NotificationDetailResponse> call, Throwable t) {
                callBack.OnFailure(t.getMessage());
            }
        });
    }


    public void acceptTaxi(String id,String user_id,final SuccessCallBack callBack) {
        APIService apiService = RetrofitClient.getService();
        Call<SuccessResponse> call = apiService.AcceptTaxi(id,user_id);
        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        callBack.OnFailure(response.body().getMessage());
                    } else {
                        callBack.OnSuccess();
                    }
                } else {
                    callBack.OnServerError();
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                callBack.OnFailure(t.getMessage());
            }
        });
    }


    public void acceptDelivery(String id,String user_id,final SuccessCallBack callBack) {
        APIService apiService = RetrofitClient.getService();
        Call<SuccessResponse> call = apiService.AcceptDelivery(id,user_id);
        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        callBack.OnFailure(response.body().getMessage());
                    } else {
                        callBack.OnSuccess();
                    }
                } else {
                    callBack.OnServerError();
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                callBack.OnFailure(t.getMessage());
            }
        });
    }


    public void deleteAllProducts(String user_id,final SuccessCallBack callBack) {
        APIService apiService = RetrofitClient.getService();
        Call<SuccessResponse> call = apiService.DeleteAllProducts(user_id);
        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        callBack.OnFailure(response.body().getMessage());
                    } else {
                        callBack.OnSuccess();
                    }
                } else {
                    callBack.OnServerError();
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                callBack.OnFailure(t.getMessage());
            }
        });
    }




}
