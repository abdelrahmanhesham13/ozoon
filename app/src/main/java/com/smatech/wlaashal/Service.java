package com.smatech.wlaashal;

import android.content.Context;

public class Service {



    static String GET_RAting_WEBPAGE="get_reviews?";
    public static void getReviews(String product_id, Context context , RequestCallBack callBack){
        BaseRequest.doGet("http://wla-ashl.com/panel/api/"+GET_RAting_WEBPAGE+"user_id="+product_id,context  ,callBack);
    }


    static String GET_FOLLWERS_WEBPAGE="get_followers?";
    public static void getFollowers(String product_id, Context context , RequestCallBack callBack){
        BaseRequest.doGet("http://wla-ashl.com/panel/api/"+GET_FOLLWERS_WEBPAGE+"user_id="+product_id,context  ,callBack);
    }


    static String GET_FOLLOWING_WEBPAGE="get_following?";
    public static void getFollowing(String product_id, Context context , RequestCallBack callBack){
        BaseRequest.doGet("http://wla-ashl.com/panel/api/"+GET_FOLLOWING_WEBPAGE+"user_id="+product_id,context  ,callBack);
    }

}
