package com.ozoon.ozoon.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ozoon.ozoon.R;
import com.ozoon.ozoon.UI.Activities.HomeActivity;
import com.ozoon.ozoon.UI.Activities.SplashActivity;

import java.util.Map;

import me.leolin.shortcutbadger.ShortcutBadger;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

            Map<String,String> notificationMessage = remoteMessage.getData();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            int badgeCount = Integer.valueOf(preferences.getString("appNotifications","0"));
            badgeCount++;
            preferences.edit().putString("appNotifications",String.valueOf(badgeCount)).apply();
            ShortcutBadger.applyCount(this, badgeCount); //for 1.1.4+

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "0");
            mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            //mBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
            //   R.drawable.im_logo));
            mBuilder.setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(notificationMessage.get("body")));
            mBuilder.setContentTitle(notificationMessage.get("title"));
            mBuilder.setContentText(notificationMessage.get("body"));
            mBuilder.setChannelId("0");
            mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
            int color = ContextCompat.getColor(this, android.R.color.white);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBuilder.setColor(color);
                mBuilder.setSmallIcon(R.drawable.notification_image);
            }else {
                mBuilder.setSmallIcon(R.drawable.notification_image);
            }
            Intent resultIntent = new Intent(this, HomeActivity.class);
            if (notificationMessage.containsKey("targetScreen")) {
                if (notificationMessage.get("targetScreen").equals("message")) {
                    resultIntent = new Intent(this, HomeActivity.class).putExtra("goToChat", true);
                } else if (notificationMessage.get("targetScreen").equals("notification")) {
                    SharedPreferences preferencesLocal = PreferenceManager.getDefaultSharedPreferences(this);
                    int notificationCount = Integer.valueOf(preferences.getString("notificationsCount","0"));
                    notificationCount++;
                    preferences.edit().putString("notificationsCount",String.valueOf(badgeCount)).apply();
                    resultIntent = new Intent(this, HomeActivity.class).putExtra("goToNotification", true);
                } else {
                    resultIntent = new Intent(this, SplashActivity.class);
                }
            }
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);



            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(HomeActivity.class);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel("0", "test", NotificationManager.IMPORTANCE_HIGH);
                if (mNotificationManager != null) {
                    mNotificationManager.createNotificationChannel(mChannel);
                }
            }

        stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            mBuilder.setAutoCancel(true);


            int random = (int)System.currentTimeMillis();
            if (mNotificationManager != null) {
                mNotificationManager.notify(random, mBuilder.build());
            }

    }
}
