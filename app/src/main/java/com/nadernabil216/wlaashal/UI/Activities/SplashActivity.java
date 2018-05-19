package com.nadernabil216.wlaashal.UI.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.nadernabil216.wlaashal.R;
import com.nadernabil216.wlaashal.Utils.GMethods;
import com.nadernabil216.wlaashal.Utils.StorageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NaderNabil216@gmail.com on 5/8/2018.
 */

public class SplashActivity extends AppCompatActivity {
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;
    private StorageUtil util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        GMethods.ChangeFont(this);
        util = StorageUtil.getInstance().doStuff(SplashActivity.this);
        Bundle bundle = getIntent().getExtras();
        /**
         * to check whether there is util from fcm or not
         */
        if (bundle != null) {
            setNotificationRoute(getIntent().getExtras());
            Log.e("nader", "bundle not null");
        } else {
            checkPermissions();
        }
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
}
