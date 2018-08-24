package com.smatech.wlaashal.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smatech.wlaashal.Model.Objects.User;

import java.lang.reflect.Type;

/**
 * Created by NaderNabil216@gmail.com on 5/8/2018.
 */
public class StorageUtil {

    private static final StorageUtil ourInstance = new StorageUtil();

    private final String STORAGE = "com.altatawwar.shaheen.Utils.STORAGE";
    SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private Context context;

    private StorageUtil() {
    }

    public static StorageUtil getInstance() {
        return ourInstance;
    }

    public StorageUtil doStuff(Context context) {
        this.context = context;
        if (context != null) {
            preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
            editor = preferences.edit();
        }
        return ourInstance;
    }

    public void setIsLogged(boolean state) {
        editor.putBoolean(GMethods.isLogged, state);
        editor.apply();
    }

    public boolean IsLogged() {
        return preferences.getBoolean(GMethods.isLogged, false);
    }


    public String getFCMtoken() {
        return preferences.getString(GMethods.FCM_TOKEN, "");
    }

    public void setFCMtoken(String token) {
        editor.putString(GMethods.FCM_TOKEN, token);
        editor.apply();
    }

    public void SetCurrentUser(User user){
        Gson gson = new Gson();
        editor.putString(GMethods.saved_user, gson.toJson(user));
        editor.apply();
    }

    public User GetCurrentUser(){
        Gson gson = new Gson();
        Type type = new TypeToken<User>() {
        }.getType();
        String json = preferences.getString(GMethods.saved_user, "");
        return gson.fromJson(json, type);

    }

    public void deleteCurrentUser(){
        editor.remove(GMethods.saved_user);
        editor.apply();
        setIsLogged(false);
    }


    public void isDelivery(boolean isDelivery){
        editor.putBoolean(GMethods.IsDelivery,isDelivery);
        editor.apply();
    }

    public boolean getDelivery(){
        return preferences.getBoolean(GMethods.IsDelivery,false);
    }


    public void isTaxi(boolean isTaxi){
        editor.putBoolean(GMethods.IsTaxi,isTaxi);
        editor.apply();
    }


    public void putPassword(String password){
        editor.putString("password",password);
        editor.apply();
    }

    public String getPassword(){
        return preferences.getString("password","null");
    }

    public void removePassword(){
        editor.remove("password");
        editor.apply();
    }


    public boolean getTaxi(){
        return preferences.getBoolean(GMethods.IsTaxi,false);
    }


    public void saveToken(String token){
        editor.putString("token",token);
        editor.apply();
    }

    public String getToken(){
        return preferences.getString("token","null");
    }


    public static String getAdsCount(Context context){
        SharedPreferences preferenceManager = PreferenceManager.getDefaultSharedPreferences(context);
        return preferenceManager.getString("ads_count","0");
    }

    public static void setAdsCount(Context context,String adsCount){
        SharedPreferences preferenceManager = PreferenceManager.getDefaultSharedPreferences(context);
        preferenceManager.edit().putString("ads_count",adsCount).apply();
    }

}
