package com.nadernabil216.wlaashal.UI.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nadernabil216.wlaashal.Presenters.PublishAdsPresenter;
import com.nadernabil216.wlaashal.R;
import com.nadernabil216.wlaashal.Utils.GMethods;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AddNewDeliveryOrTaxiService extends AppCompatActivity implements View.OnClickListener {

    private int advertise_type = 0; // 0 is delivery , 1 is taxi
    private PublishAdsPresenter presenter;
    private TextView toolbar_title;
    private ImageView ic_back, ic_notification, service_logo, img1, img2, img3, img4;
    private String current_key, img1_key = "img1_key", img2_key = "img2_key", img3_key = "img3_key", img4_key = "img4_key", address, car_type, car_number, phone_number, description;
    private EditText ed_service_address, ed_car_type, ed_car_number, ed_phone_number, ed_description;
    private TextInputLayout ed_service_address_layout, ed_car_type_layout, ed_car_number_layout, ed_phone_number_layout, ed_description_layout;
    private Button publish_btn;
    private HashMap<String, String> SelectedImages = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_delivery_or_taxi_service);
        GMethods.ChangeFont(this);
        Intent intent = getIntent();
        if (intent != null) {
            advertise_type = intent.getIntExtra(GMethods.ADVERT_TYPE, 0);
        }

        presenter = new PublishAdsPresenter();
        InitViews();
    }

    private void InitViews() {
        ed_service_address_layout = findViewById(R.id.ed_service_address_layout);
        ed_car_type_layout = findViewById(R.id.ed_car_type_layout);
        ed_car_number_layout = findViewById(R.id.ed_car_number_layout);
        ed_phone_number_layout = findViewById(R.id.ed_phone_number_layout);
        ed_description_layout = findViewById(R.id.ed_description_layout);

        ed_service_address = findViewById(R.id.ed_service_address);
        ed_car_type = findViewById(R.id.ed_car_type);
        ed_car_number = findViewById(R.id.ed_car_number);
        ed_phone_number = findViewById(R.id.ed_phone_number);
        ed_description = findViewById(R.id.ed_description);

        publish_btn = findViewById(R.id.btn_publish);
        ic_back = findViewById(R.id.back_btn);
        ic_notification = findViewById(R.id.notification_btn);
        service_logo = findViewById(R.id.service_logo);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        toolbar_title = findViewById(R.id.toolbar_title);

        publish_btn.setOnClickListener(this);
        ic_back.setOnClickListener(this);
        ic_notification.setOnClickListener(this);
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);

        if (advertise_type == 0) {
            toolbar_title.setText(getString(R.string.add_delivery_service));
            service_logo.setImageResource(R.drawable.ic_add_delivery_service);
        } else {
            toolbar_title.setText(getString(R.string.add_taxi_service));
            service_logo.setImageResource(R.drawable.ic_add_taxi_service);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_publish:
                Checker();
                break;
            case R.id.back_btn:
                onBackPressed();
                break;
            case R.id.notification_btn:
                // TODO: 5/10/2018 go to notifications
                break;
            case R.id.img1:
                PickImage(img1_key);
                break;
            case R.id.img2:
                PickImage(img2_key);
                break;
            case R.id.img3:
                PickImage(img3_key);
                break;
            case R.id.img4:
                PickImage(img4_key);
                break;
        }
    }

    private void PickImage(String Key) {
        current_key = Key;
        ImagePicker.with(this)
                .setFolderMode(true) // folder mode (false by default)
                .setFolderTitle(getString(R.string.folder)) // folder selection title
                .setImageTitle(getString(R.string.select_image)) // image selection title
                .setMaxSize(1) //  Max images can be selected
                .setMultipleMode(false) //single mode
                .setShowCamera(true) // show camera or not (true by default)
                .start(); // start image picker activity with request code
    }

    /**
     * result of pick image will appear here where we start to use it
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            Image img = images.get(0);
            try {
                Bitmap bitmapimage = GMethods.GetBitmap(img.getPath(), 100);
                SelectedImages.put(current_key, img.getPath());

                if (current_key.equals(img1_key)) {
                    img1.setImageBitmap(bitmapimage);
                } else if (current_key.equals(img2_key)) {
                    img2.setImageBitmap(bitmapimage);
                } else if (current_key.equals(img3_key)) {
                    img3.setImageBitmap(bitmapimage);
                } else if (current_key.equals(img4_key)) {
                    img4.setImageBitmap(bitmapimage);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void Checker() {
        ed_service_address_layout.setError("");
        ed_car_type_layout.setError("");
        ed_car_number_layout.setError("");
        ed_phone_number_layout.setError("");
        ed_description_layout.setError("");

        address = ed_service_address.getText().toString().trim();
        car_type = ed_car_type.getText().toString().trim();
        car_number = ed_car_number.getText().toString().trim();
        phone_number = ed_phone_number.getText().toString().trim();
        description = ed_description.getText().toString().trim();

        if (address.isEmpty()) {
            ed_service_address_layout.setError("برجاء كتابة عنوان الخدمة");
        } else if (car_type.isEmpty()) {
            ed_car_type_layout.setError("برجاء كتابة نوع السيارة");
        } else if (car_number.isEmpty()) {
            ed_car_number_layout.setError("برجاء كتابة رقم لوحة السيارة");
        } else if (phone_number.isEmpty() || (phone_number.length() < 10) || (phone_number.length() > 10)) {
            ed_phone_number_layout.setError("برجاء كتابة رقم الهاتف");
        } else if (description.isEmpty()) {
            ed_description_layout.setError("برجاء كتابة وصف العنوان");
        } else {
            if (advertise_type == 0) {
                SendDeliveryService();
            } else {
                SendTaxiService();
            }
        }
    }

    private void SendDeliveryService() {

    }

    private void SendTaxiService() {

    }

    @Override
    public void onBackPressed() {
        finish();
    }


}
