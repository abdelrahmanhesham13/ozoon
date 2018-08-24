package com.smatech.wlaashal.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.smatech.wlaashal.R;
import com.thefinestartist.finestwebview.FinestWebView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.anwarshahriar.calligrapher.Calligrapher;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

/**
 * Created by NaderNabil216@gmail.com on 5/8/2018.
 */

public class GMethods {

    /**
     * Service Urls
     */

    public static String Base_Url = "http://wla-ashl.com/panel/api/";
    public static String Privacy_Url = "http://www.wla-ashl.com/panel/api/webview?type=1";
    public static String About_Url = "http://www.wla-ashl.com/panel/api/webview?type=0";
    public static String IMAGE_URL = "http://wla-ashl.com/panel/prod_img/";

    /*** shared pref tags*/
    public static String isLogged = "isLogged";
    public static String FCM_TOKEN = "FCM_TOKEN"; //used for phone validation
    public static String saved_user = "saved_user";
    public static String UID = "UID";
    public static String LATLNG = "LATLNG";
    public static String IsTaxi = "IsTaxi";
    public static String IsDelivery = "IsDelivery";

    /**
     * constants strings
     */
    public static String Lat = "Lat";
    public static String Lng = "Lng";
    public static String CATEGORY_ID="CATEGORY_ID";
    public static String ADVERT_TYPE = "ADVERT_TYPE";
    public static String PERFORME_NEAREST="PERFORME_NEAREST";
    public static String PERFORME_HIGHEST_RATE="PERFORME_HIGHEST_RATE";
    public static String PERFORME_SUBCATEGORY="PERFORME_SUBCATEGORY";
    public static String ACTION_UPDATE_DATA="ACTION_UPDATE_DATA";
    public static String ACTION_OLDEST="ACTION_OLDEST";
    public static String ACTION_TYPE="ACTION_TYPE";
    public static String PRODUCT_ID="PRODUCT_ID";


    /**
     * for FCM and Handle notification
     */
    public static String FCM_RECEIVED = "FCM_RECEIVED";
    public static String FCM_ACTION = "FCM_ACTION";
    public static String FCM_MESSAGE = "FCM_MESSAGE";
    public static String FCM_TITLE = "FCM_TITLE";


    /**
     * progress dialog method
     */
    public static ProgressDialog show_progress_dialoug(Context context, String Body_text, boolean cancelable) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(cancelable);
        progressDialog.setMessage(Body_text);
        progressDialog.show();
        return progressDialog;
    }

    /**
     * alert dialog method
     */
    public static void show_alert_dialoug(Context context, String body, String positive_btn_txt, boolean cancelable, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setCancelable(cancelable)
                .setTitle(context.getString(R.string.app_name))
                .setMessage(body)
                .setPositiveButton(positive_btn_txt, listener);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void show_alert_dialoug(Context context, String body, String title, boolean cancelable, String positive_text, String negative_text, DialogInterface.OnClickListener yesListener, DialogInterface.OnClickListener NoListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setCancelable(cancelable).setMessage(body);
        if (!title.isEmpty()) {
            builder.setTitle(title);
        }
        if (!positive_text.isEmpty()) {
            builder.setPositiveButton(positive_text, yesListener);
        }
        if (!negative_text.isEmpty()) {
            builder.setNegativeButton(negative_text, NoListener);
        }

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    public static void showSnackBarMessage(String message, AppCompatActivity activity) {

        if (activity.findViewById(android.R.id.content) != null) {

            Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
        }
    }

    public static void hideKeyboard(AppCompatActivity activity,View v){
        View currentFocus = activity.getCurrentFocus();
        if (currentFocus != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
    }


    public static void writeToLog(String message) {

        Log.i("Helper Log", message);

    }

    /**
     * method for handle inputs
     * case 1 for name , 2 for email , 3 for phone number with code
     * case 4 for name and number only , 5 for number only
     * by sending regex and text
     * return true or false
     */
    public static boolean Checker(int code, String text) {
        String regex;
        Pattern p;
        Matcher m = null;
        switch (code) {
            case 1:
                regex = "^[a-zA-Z ]+$";
                p = Pattern.compile(regex);
                m = p.matcher(text);
                break;
            case 2:
                regex = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
                p = Pattern.compile(regex);
                m = p.matcher(text);
                break;
            case 3:
                regex = "^[0-9]+$";
                p = Pattern.compile(regex);
                m = p.matcher(text);
                break;
            case 4:
                regex = "^[a-zA-Z0-9 ]+$";
                p = Pattern.compile(regex);
                m = p.matcher(text);
                break;
            case 5:
                regex = "^[0-9]+$";
                p = Pattern.compile(regex);
                m = p.matcher(text);
                break;

        }

        return m.matches();
    }

    /**
     * method take lat and long fro location and get address name
     */

    public static String covertLatLng(Context context, double Lat, double lng) throws IOException {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(context, Locale.getDefault());
        addresses = geocoder.getFromLocation(Lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
        return address;
    }

//    public static LatLng getAddress(Context context, String sText) throws IOException {
//        Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
//        List<Address> addresses = geoCoder.getFromLocationName(sText, 5);
//        if (addresses.size() > 0) {
//            Double lat = addresses.get(0).getLatitude();
//            Double lng = addresses.get(0).getLongitude();
//            final LatLng postion = new LatLng(lat, lng);
//            return postion;
//        } else {
//            return null;
//        }
//    }

    public static List<Address> GetAddressList(Context context, String sText) throws IOException {
        Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = geoCoder.getFromLocationName(sText, 10);
        return addresses;
    }

    /**
     * this method for convert image to bitmap
     * this method call getResized bitmap and get bit map from path
     * and return bitmap
     */
    public static Bitmap GetBitmap(String path, int size) throws IOException {
        Bitmap bitmap = getBitmapFromPath(path, size);

        ExifInterface ei = new ExifInterface(path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }
        return rotatedBitmap;
    }

    private static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private static Bitmap getBitmapFromPath(String path, int size) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        return getResizedBitmap(bitmap, size);
    }

    public static String GetCurrentTime() {
        Locale locale = new Locale("en");
        long times = System.currentTimeMillis();
        SimpleDateFormat stf = new SimpleDateFormat("kk:mm", locale);
        return stf.format(times);
    }

    public static String GetCurrentDate() {
        Locale locale = new Locale("en");
        long dates = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", locale);
        return sdf.format(dates);
    }

    public static String GetFormatedDate(Date date) {
        Locale locale = new Locale("en");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", locale);
        return sdf.format(date);
    }

    public static String GetFormatedTime(Date date) {
        Locale locale = new Locale("en");
        SimpleDateFormat stf = new SimpleDateFormat("kk:mm", locale);
        return stf.format(date);
    }

    public static void ChangeViewFont(final View view) {
        Calligrapher calligrapher = new Calligrapher(view.getContext());
        calligrapher.setFont(view, "fonts/Droid-Arabic-Kufi.ttf");
    }

    public static void ChangeFont(final Context context) {
        Calligrapher calligrapher = new Calligrapher(context);
        calligrapher.setFont(((Activity) context), "fonts/Droid-Arabic-Kufi.ttf", true);
    }

    public static String covertLatLng2Address(Context context, double Lat, double lng) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        addresses = geocoder.getFromLocation(Lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
        return address;
    }


    public static void OpenPopUpBrowser(Context context, String url) {
        new FinestWebView.Builder(context)
                .setCustomAnimations(R.anim.activity_open_enter, R.anim.activity_open_exit, R.anim.activity_close_enter, R.anim.activity_close_exit)
                .backPressToClose(true)
                .toolbarScrollFlags(0)
                .webViewJavaScriptEnabled(true)
                .webViewUseWideViewPort(false)
                .webViewAllowContentAccess(true)
                .webViewSupportZoom(true)
                .webViewBuiltInZoomControls(true)
                .webViewDisplayZoomControls(true)
                .webViewDomStorageEnabled(true)
                .webViewDatabaseEnabled(true)
                .titleDefault(context.getString(R.string.app_name))
                .show(url);
    }

    public static void playNotificationSound_Default(Context context) {

        try {
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Date from_String_toDate(String dateTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        try {
            date = format.parse(dateTime);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        Log.e("nader216 before convert", date.toString());

        Calendar c = new GregorianCalendar();
        c.setTime(date);
        return c.getTime();
    }

    public static int Difference_between_to_dates_in_days(Date d1, Date d2) {
        //milliseconds
        long different = d2.getTime() - d1.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long number_of_days = different / daysInMilli;

        return (int) number_of_days;
    }


    public static String getDateCurrentTimeZone(long timestamp) {

        try {

            SimpleDateFormat sfd = new SimpleDateFormat("hh:mm aa");

            sfd.format(new Date(timestamp));

           /* Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm aa");
            Date currenTimeZone = calendar.getTime();*/

            return sfd.format(new Date(timestamp));

        } catch (Exception e) {

        }
        return "";

    }

    public static void ShareAppLink(Context context) {
        String app_url = "https://play.google.com/store/apps/details?id=com.nadernabil216.wlaashal";
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, app_url);
        context.startActivity(Intent.createChooser(sharingIntent, context.getResources().getString(R.string.share_using)));
    }

    public static void OpenIntentToGoogleMapsDirection(Context context, String lat, String lng) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + String.valueOf(lat) + "," + String.valueOf(lng));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        context.startActivity(mapIntent);
    }



}
