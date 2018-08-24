package com.smatech.wlaashal;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//import tech.android.volley.VolleyError;
//import tech.android.volley.request.SimpleMultiPartRequest;
//import tech.android.volley.toolbox.JsonObjectRequest;
//import tech.android.volley.toolbox.StringRequest;

/**
 * Created by mohanad on 31/07/17.
 */

public class BaseRequest {

    private static final String LOG_TAG = "BaseRequest";

    /**
     * request tha api with GET method
     *
     * @param url      : API URL
     * @param context  :  application context
     * @param callBack : the interface callback to notify
     */
    static void doGet(String url, final Context context, final RequestCallBack callBack) {

        url = url.replaceAll(" ","%20");


        Log.i("URL : " , url);
        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //notify callback for success
                        callBack.success(response);
                        Log.v(LOG_TAG,response+"jjj");

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        callBack.error(error);
                        showSnackBarMessage("خطأ بالسيرفر من فضلك اعد المحاوله",(AppCompatActivity) context);
                    }
                }
        );

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        stringRequest.setShouldCache(false);


        // Add the request to the queue
        Volley.newRequestQueue(context).add(stringRequest);
    }



    static void doGet(String url, final Context context, final RequestCallBack callBack,boolean x) {

        //url = url.replaceAll(" ","%20");

        // Request a string response

        Log.i("URL : " , url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //notify callback for success
                        callBack.success(response);
                        Log.v(LOG_TAG,response+"jjj");

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        callBack.error(error);
                        showSnackBarMessage("خطأ بالسيرفر من فضلك اعد المحاوله",(AppCompatActivity) context);
                    }
                }
        );

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        stringRequest.setShouldCache(false);


        // Add the request to the queue
        Volley.newRequestQueue(context).add(stringRequest);
    }

    /**
     * request tha api with Post method
     *
     * @param url      : API URL
     * @param context  :  application context
     * @param paramas  : the POST method paramas
     * @param callBack : the interface callback to notify
     */
    static void doPost(String url, Context context, final HashMap<String, String> paramas, final RequestCallBack callBack) {




        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        callBack.success(response);
                        Log.i("Response", response);
                        //dismiss the progress dialog
//                        pDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        callBack.error(error);
                        Log.e("Error.Response", error + "");
                        //dismiss the progress dialog
//                        pDialog.dismiss();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() {
                return paramas;
            }
        };


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache(false);

// Add the request to the queue
        Volley.newRequestQueue(context).add(stringRequest);
    }

    /**
     * request tha api with Post method
     *
     * @param url      : API URL
     * @param context  :  application context
     * @param callBack : the interface callback to notify
     */
    static void doPost(String url, Context context, JSONObject jsonObject, final RequestCallBack callBack) {

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jobReq = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        callBack.success(jsonObject.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callBack.error(error);
                    }
                });

        queue.add(jobReq);
    }



    public static void showSnackBarMessage(String message, AppCompatActivity activity) {

        if (activity.findViewById(android.R.id.content) != null) {

            Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
        }
    }


}
