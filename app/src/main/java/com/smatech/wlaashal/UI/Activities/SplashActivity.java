package com.smatech.wlaashal.UI.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.smatech.wlaashal.CallBacks.LoginCallBack;
import com.smatech.wlaashal.Model.Objects.User;
import com.smatech.wlaashal.Presenters.LoginPresenter;
import com.smatech.wlaashal.R;
import com.smatech.wlaashal.Utils.GMethods;
import com.smatech.wlaashal.Utils.Helper;
import com.smatech.wlaashal.Utils.StorageUtil;

import java.util.ArrayList;
import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by NaderNabil216@gmail.com on 5/8/2018.
 */

public class SplashActivity extends AppCompatActivity {
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;
    private StorageUtil util;
    LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash);
        GMethods.ChangeFont(this);
        presenter = new LoginPresenter();
        util = StorageUtil.getInstance().doStuff(SplashActivity.this);
        Bundle bundle = getIntent().getExtras();
        /**
         * to check whether there is util from fcm or not
         */
        /*if (bundle != null) {
            setNotificationRoute(getIntent().getExtras());
            Log.e("nader", "bundle not null");
        } else {
           // checkPermissions();
        }*/

        if (util.IsLogged()){
            Login();
        } else {
            checkPermissions();
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().remove("appNotifications").apply();
        ShortcutBadger.removeCount(this);

    }

    private void setNotificationRoute(Bundle extras) {
        if (util.IsLogged()) {
//            String OrderID = extras.getString("fcm_type", "No Data Found");
//            Intent in;
//            if (OrderID.equals("1")) {
//                util.setIsLogged(false);
//                startActivity(new Intent(SplashActivity.this, SignInUpActivity.class));
//                finish();
//            } else if (OrderID.equals("2") || OrderID.equals("3") || OrderID.equals("4") || OrderID.equals("6")) {
//                if (util.getUTYPE().equals("1")) {
//                    in = new Intent(SplashActivity.this, UserMainActivity.class);
//                    in.putExtra("from", "0");
//                    startActivity(in);
//                    finish();
//                } else {
//                    in = new Intent(SplashActivity.this, DriverMainActivity.class);
//                    in.putExtra("from", "1");
//                    startActivity(in);
//                    finish();
//                }
//            } else if (OrderID.equals("5")) {
//                /**
//                 * rating
//                 */
//                in = new Intent(SplashActivity.this, UserMainActivity.class);
//                in.putExtra("shop_id", extras.getString("shop_id"));
//                in.putExtra("order_id", extras.getString("order_id"));
//                startActivity(in);
//                finish();
//            } else {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Intent in;
//                        if (util.IsLogged()) {
//                            if (util.getUTYPE().equals("1")) {
//                                in = new Intent(SplashActivity.this, UserMainActivity.class);
//                                startActivity(in);
//                                finish();
//                            } else {
//                                in = new Intent(SplashActivity.this, DriverMainActivity.class);
//                                startActivity(in);
//                                finish();
//                            }
//                        } else {
//                            startActivity(new Intent(SplashActivity.this, SignInUpActivity.class));
//                            finish();
//                        }
//                    }
//                }, 3000);
//            }



            checkPermissions();
        } else {
            checkPermissions();
        }
    }


    private void checkPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(SplashActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(SplashActivity.this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                }
            }, 3000);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        int permissionLocation = ContextCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            SplashActivity.this.finish();
        } else {
            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            finish();
        }
    }


    private void Login() {

        Helper.writeToLog(util.getToken());
        presenter.Login(util.GetCurrentUser().getUsername(), util.getPassword(),util.getToken(), new LoginCallBack() {
            @Override
            public void OnSuccess(User user) {
                Helper.writeToLog("Logged");
                util.setIsLogged(true);
                util.SetCurrentUser(user);
                util.putPassword(util.getPassword());
                if (user.getDelivery().equals("1")) {
                    util.isDelivery(true);
                }
                if (user.getDriver().equals("1")){
                    util.isTaxi(true);
                }
                checkPermissions();
            }

            @Override
            public void OnFailure(String message) {
                /*GMethods.show_alert_dialoug(SplashActivity.this,
                        message,
                        getString(R.string.app_name),
                        true,
                        "",
                        "",
                        null,
                        null);*/
                Helper.writeToLog(message);
            }

            @Override
            public void OnServerError() {
                /*GMethods.show_alert_dialoug(SplashActivity.this,
                        getString(R.string.server_error),
                        getString(R.string.app_name),
                        true,
                        "",
                        "",
                        null,
                        null);*/
                Helper.writeToLog("error");
            }
        });
    }

}
