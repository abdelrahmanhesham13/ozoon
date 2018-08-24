package com.smatech.wlaashal.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.smatech.wlaashal.CallBacks.SuccessCallBack;
import com.smatech.wlaashal.Model.Objects.Category;
import com.smatech.wlaashal.Presenters.MainPresenter;
import com.smatech.wlaashal.R;
import com.smatech.wlaashal.UI.Activities.AdvertsActivity;
import com.smatech.wlaashal.UI.Activities.AskForTaxiActivity;
import com.smatech.wlaashal.UI.Activities.HomeActivity;
import com.smatech.wlaashal.Utils.GMethods;
import com.smatech.wlaashal.Utils.GPSTracker;
import com.smatech.wlaashal.Utils.Helper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.smatech.wlaashal.Utils.StorageUtil;

import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by NaderNabil216@gmail.com on 5/9/2018.
 */
public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.Viewholder> {

    private Context context;
    private ArrayList<Category> categories;
    private StorageUtil util;
    private MainPresenter presenter;
    private GPSTracker gps;
    double latitude;
    double longitude;
    String productString;
    String priceString;

    ProgressDialog progressDialog;

    AlertDialog alertDialog;

    public CategoriesAdapter(Context context, ArrayList<Category> categories) {
        this.context = context;
        this.categories = categories;
        util = StorageUtil.getInstance().doStuff(context);
        presenter = new MainPresenter();
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        GMethods.ChangeViewFont(view);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(final Viewholder holder, int position) {
        final Category category = categories.get(position);
        if (position == 0) {
            holder.progressBar.setVisibility(View.GONE);
            holder.cat_image.setImageResource(R.drawable.ic_category_taxi);
            holder.cat_title.setText("تاكسي");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (util.IsLogged()) {
                        ((HomeActivity)context).startActivityForResult(new Intent(context, AskForTaxiActivity.class),1);
                    } else {
                        //Toast.makeText(context, "انت غير مسجل من فضلك قم بتسجيل الدخول", Toast.LENGTH_LONG).show();
                        Helper.showSnackBarMessage("انت غير مسجل من فضلك قم بتسجيل الدخول", (AppCompatActivity) context);
                    }
                }
            });

        } else if (position == 1) {
            holder.progressBar.setVisibility(View.GONE);
            holder.cat_image.setImageResource(R.drawable.delivery_newest);
            holder.cat_title.setText("التوصيل");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (util.IsLogged()) {
                        show();
                    } else {
                        //Toast.makeText(context, "انت غير مسجل من فضلك قم بتسجيل الدخول", Toast.LENGTH_LONG).show();
                        Helper.showSnackBarMessage("انت غير مسجل من فضلك قم بتسجيل الدخول", (AppCompatActivity) context);
                    }
                }
            });
        } else if (position == 2) {
            holder.progressBar.setVisibility(View.GONE);
            holder.cat_image.setImageResource(R.drawable.all_ads);
            holder.cat_title.setText("احدث الاعلانات");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AdvertsActivity.class);
                    intent.putExtra(GMethods.CATEGORY_ID, "-1");
                    context.startActivity(intent);
                }
            });

        } else {
            Picasso.with(context).load(category.getImage()).into(holder.cat_image, new Callback() {
                @Override
                public void onSuccess() {
                    holder.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {

                }
            });
            holder.cat_title.setText(category.getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AdvertsActivity.class);
                    intent.putExtra(GMethods.CATEGORY_ID, category.getId());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {

        ImageView cat_image;
        TextView cat_title;
        ProgressBar progressBar;

        public Viewholder(View itemView) {
            super(itemView);
            cat_image = itemView.findViewById(R.id.category_image);
            cat_title = itemView.findViewById(R.id.category_title);
            progressBar = itemView.findViewById(R.id.progressbar);
        }
    }


    private void show() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);
        dialogBuilder.setView(dialogView);
        final EditText product = dialogView.findViewById(R.id.product_input);
        final EditText price = dialogView.findViewById(R.id.price_input);
        final TextView numOfDrivers = dialogView.findViewById(R.id.num_of_drivers);
        dialogView.findViewById(R.id.btn_last).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productString = product.getText().toString();
                priceString = price.getText().toString();
                if (TextUtils.isEmpty(productString)) {
                    //Toast.makeText(context,"من فضلك ادخل ماتريد شرائه",Toast.LENGTH_LONG).show();
                    Helper.showSnackBarMessage("من فضلك ادخل ماتريد شرائه", (AppCompatActivity) context);
                } else if (TextUtils.isEmpty(priceString)) {
                    //Toast.makeText(context,"من فضلك ادخل السعر",Toast.LENGTH_LONG).show();
                    Helper.showSnackBarMessage("من فضلك ادخل السعر", (AppCompatActivity) context);
                } else {
                    progressDialog = GMethods.show_progress_dialoug(context, "جاري اضافة طلبك...", false);
                    progressDialog.cancel();
                    getLocation();
                }
            }
        });

        presenter.getDeliveryCount(new SuccessCallBack() {
            @Override
            public void OnSuccess() {
                numOfDrivers.setText("خطا في معرفة عدد السائقين");
            }

            @Override
            public void OnFailure(String message) {
                numOfDrivers.setText("عدد السائقين المتوفرين حاليا : " + message);
            }

            @Override
            public void OnServerError() {
                numOfDrivers.setText("خطا في معرفة عدد السائقين");
            }
        });

        alertDialog = dialogBuilder.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        alertDialog.show();

    }


    public void sendDeliveryRequest() {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> toAddresses = null;
        String toAddress = null;
        String toCity = null;
        try {
            toAddresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();

        }
        if (toAddresses != null) {
            toAddress = toAddresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            toCity = toAddresses.get(0).getAdminArea();
        } else {
            alertDialog.dismiss();
            progressDialog.dismiss();
            //Toast.makeText(context,"لا يوجد انترنت",Toast.LENGTH_LONG).show();
            Helper.showSnackBarMessage("لا يوجد انترنت", (AppCompatActivity) context);
        }
        GMethods.writeToLog(util.GetCurrentUser().getId() + " " + longitude + " " + latitude + " " + toAddress + " " + toCity + " " + priceString + " " + productString);

        presenter.deliveryRequest(util.GetCurrentUser().getId(), String.valueOf(longitude), String.valueOf(latitude), toAddress, toCity, priceString, productString, new SuccessCallBack() {
            @Override
            public void OnSuccess() {
                gps.stopUsingGPS();
                progressDialog.dismiss();
                alertDialog.dismiss();
                //Toast.makeText(context, "تم اضافة طلبك بنجاح", Toast.LENGTH_LONG).show();
                Helper.showSnackBarMessage("تم اضافة طلبك بنجاح", (AppCompatActivity) context);
            }

            @Override
            public void OnFailure(String message) {

            }

            @Override
            public void OnServerError() {

            }
        });


    }


    private void getLocation() {
        gps = new GPSTracker(context, new GPSTracker.OnGetLocation() {
            @Override
            public void onGetLocation(double longtiude, double lantitude) {
                progressDialog.show();
                gps.stopUsingGPS();
                GMethods.writeToLog("Interface : " + longtiude + " " + lantitude);
                CategoriesAdapter.this.latitude = longtiude;
                CategoriesAdapter.this.longitude = lantitude;
                sendDeliveryRequest();

            }
        });

        // check if GPS enabled
        if (gps.canGetLocation()) {
            progressDialog.show();
            if (gps.getLatitude() != 0 && gps.getLongitude() != 0) {
                gps.stopUsingGPS();
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                sendDeliveryRequest();
            }
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

}
