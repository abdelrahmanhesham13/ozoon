package com.nadernabil216.wlaashal.Utils;

import android.content.Context;
import android.content.SharedPreferences;

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
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        editor = preferences.edit();
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

    public String getUID() {
        return preferences.getString(GMethods.UID, "0");
    }

    public void setUID(String uid) {
        editor.putString(GMethods.UID, uid);
        editor.apply();
    }

}
