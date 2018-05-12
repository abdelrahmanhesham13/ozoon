package com.nadernabil216.wlaashal.UI.Activities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.nadernabil216.wlaashal.Presenters.PublishAdsPresenter;
import com.nadernabil216.wlaashal.R;
import com.nadernabil216.wlaashal.Utils.GMethods;

import java.util.HashMap;

public class AddNewAdvertActivity extends AppCompatActivity {
    private PublishAdsPresenter presenter;
    private ImageView ic_back, ic_notification, img1, img2, img3, img4;
    private String current_key, img1_key = "img1_key", img2_key = "img2_key", img3_key = "img3_key", img4_key = "img4_key", address, car_type, car_number, phone_number, description;
    private EditText ed_service_address, ed_car_type, ed_car_number, ed_phone_number, ed_description;
    private TextInputLayout ed_service_address_layout, ed_car_type_layout, ed_car_number_layout, ed_phone_number_layout, ed_description_layout;
    private Button publish_btn;
    private Spinner categories_spinenr , sub_categories_spinner ;
    private HashMap<String, String> SelectedImages = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_advert);
        GMethods.ChangeFont(this);
        presenter = new PublishAdsPresenter();
        InitViews();
    }

    private void InitViews() {
        ic_back = findViewById(R.id.back_btn);

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
