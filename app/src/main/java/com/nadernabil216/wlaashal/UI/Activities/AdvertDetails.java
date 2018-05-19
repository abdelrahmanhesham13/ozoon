package com.nadernabil216.wlaashal.UI.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nadernabil216.wlaashal.CallBacks.GetProductCallBack;
import com.nadernabil216.wlaashal.Model.Objects.Product;
import com.nadernabil216.wlaashal.Presenters.MainPresenter;
import com.nadernabil216.wlaashal.R;
import com.nadernabil216.wlaashal.Utils.GMethods;

import in.myinnos.imagesliderwithswipeslibrary.SliderLayout;
import in.myinnos.imagesliderwithswipeslibrary.SliderTypes.BaseSliderView;
import in.myinnos.imagesliderwithswipeslibrary.SliderTypes.TextSliderView;

public class AdvertDetails extends AppCompatActivity implements View.OnClickListener {
    TextView tv_address, tv_category_name, tv_description;
    ImageView ic_back, ic_location, ic_call, ic_chat, ic_profile;
    SliderLayout sliderLayout;
    String productId = "";
    MainPresenter presenter;
    ProgressDialog progressDialog;
    Product current_product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_details);
        GMethods.ChangeFont(this);
        Intent intent = getIntent();
        if (intent != null) {
            productId = intent.getStringExtra(GMethods.PRODUCT_ID);
        }
        presenter = new MainPresenter();
        initViews();
        GetData();
    }

    private void initViews() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("جاري جلب البيانات");

        tv_address = findViewById(R.id.tv_advert_address);
        tv_category_name = findViewById(R.id.tv_category_name);
        tv_description = findViewById(R.id.tv_description);
        ic_back = findViewById(R.id.back_btn);
        ic_location = findViewById(R.id.ic_location);
        ic_call = findViewById(R.id.ic_call);
        ic_chat = findViewById(R.id.ic_chat);
        ic_profile = findViewById(R.id.ic_profile);
        sliderLayout = findViewById(R.id.slider);

        ic_back.setOnClickListener(this);
        ic_location.setOnClickListener(this);
        ic_call.setOnClickListener(this);
        ic_chat.setOnClickListener(this);
        ic_profile.setOnClickListener(this);

        sliderLayout = findViewById(R.id.slider);
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Stack);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setDuration(4000);

    }

    private void GetData() {
        progressDialog.show();
        presenter.GetProduct(productId, new GetProductCallBack() {
            @Override
            public void OnSuccess(Product product) {
                progressDialog.dismiss();
                SetData(product);
            }

            @Override
            public void OnFailure(String message) {
                progressDialog.dismiss();
                GMethods.show_alert_dialoug(AdvertDetails.this,
                        message,
                        getString(R.string.app_name),
                        true,
                        "",
                        "",
                        null,
                        null);
            }

            @Override
            public void OnServerError() {
                progressDialog.dismiss();
                GMethods.show_alert_dialoug(AdvertDetails.this,
                        getString(R.string.server_error),
                        getString(R.string.app_name),
                        true,
                        "",
                        "",
                        null,
                        null);
            }
        });
    }

    private void SetData(Product product) {
        current_product = product;

        if (product.getImages().isEmpty()) {
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView
                    .image("http://dbabapp.com/api-dbab/public/uploads/shops/default2.png")
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop);
            sliderLayout.addSlider(textSliderView);
            sliderLayout.stopAutoCycle();
        } else {
            for (int i = 0; i < product.getImages().size(); i++) {
                TextSliderView textSliderView = new TextSliderView(this);
                textSliderView
                        .image(product.getImages().get(i))
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop);
                sliderLayout.addSlider(textSliderView);
            }
        }

        tv_address.setText(product.getName());
        tv_category_name.setText(product.getCategory_name());
        tv_description.setText(product.getBody());

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void Make_Phone_Call(final String phone_number) {
        GMethods.show_alert_dialoug(this, phone_number, "هل تريد الإتصال بهذا الرقم ؟", false, "إتصل", "رجوع", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ActivityCompat.checkSelfPermission(AdvertDetails.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    GMethods.show_alert_dialoug(AdvertDetails.this, "برجاء السماح بعمل إتصال", getString(R.string.app_name), true, "حسنا", "", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", getPackageName(), null));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }, null);
                    return;
                }
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel: " + phone_number));
                startActivity(callIntent);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                onBackPressed();
                break;
            case R.id.ic_location:
                GMethods.OpenIntentToGoogleMapsDirection(AdvertDetails.this, current_product.getLatitude(), current_product.getLongtide());
                break;
            case R.id.ic_call:
                Make_Phone_Call(current_product.getMobile());
                break;
            case R.id.ic_chat:
                break;
            case R.id.ic_profile:
                break;
        }
    }
}
