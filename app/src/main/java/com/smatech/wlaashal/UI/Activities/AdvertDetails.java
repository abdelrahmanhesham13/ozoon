package com.smatech.wlaashal.UI.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.util.Strings;
import com.smatech.wlaashal.CallBacks.GetProductCallBack;
import com.smatech.wlaashal.Model.Objects.ChatModel;
import com.smatech.wlaashal.Model.Objects.Product;
import com.smatech.wlaashal.Presenters.MainPresenter;
import com.smatech.wlaashal.ProfileActivity;
import com.smatech.wlaashal.R;
import com.smatech.wlaashal.UI.Fragments.ChatFragment;
import com.smatech.wlaashal.UI.Fragments.Home;
import com.smatech.wlaashal.Utils.Connector;
import com.smatech.wlaashal.Utils.GMethods;
import com.smatech.wlaashal.Utils.Helper;
import com.smatech.wlaashal.Utils.StorageUtil;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.myinnos.imagesliderwithswipeslibrary.SliderLayout;
import in.myinnos.imagesliderwithswipeslibrary.SliderTypes.BaseSliderView;
import in.myinnos.imagesliderwithswipeslibrary.SliderTypes.TextSliderView;
import me.leolin.shortcutbadger.ShortcutBadger;

public class AdvertDetails extends AppCompatActivity implements View.OnClickListener {
    private static final Object TAG = "test";
    TextView tv_address, tv_category_name, tv_description,num_of_watching,date,price;
    ImageView ic_back, ic_location, ic_call, ic_chat, ic_profile,notification_btn,whatsapp;
    SliderLayout sliderLayout;
    String productId = "";
    MainPresenter presenter;
    ProgressDialog progressDialog;
    Product current_product;
    StorageUtil util;

    public FloatingActionMenu home_menu;
    private FloatingActionButton fab_delivery;
    private FloatingActionButton fab_taxi;
    private FloatingActionButton fab_advert;
    @BindView(R.id.chat_floating_btn)
    FloatingActionButton chatBtn;

    ChatModel mChatModel;

    ArrayList<String> realImages;

    Connector mConnectorSendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_details);
        Fresco.initialize(this);
        ButterKnife.bind(this);
        GMethods.ChangeFont(this);
        realImages = new ArrayList<>();
        home_menu = findViewById(R.id.home_menu);
        fab_delivery = findViewById(R.id.delivery_fab);
        util = StorageUtil.getInstance().doStuff(this);
        Intent intent = getIntent();
        if (intent != null) {
            productId = intent.getStringExtra(GMethods.PRODUCT_ID);
        }
        presenter = new MainPresenter();
        initViews();
        GetData();


        mConnectorSendMessage = new Connector(AdvertDetails.this, new Connector.LoadCallback() {
            @Override
            public void onComplete(String tag, String response) {
                if (Connector.checkStatus(response)) {
                    mChatModel = Connector.getChatModelJson(response,current_product.getUser(),current_product.getUser_id(),util.GetCurrentUser().getId());
                    //Intent returnIntent = new Intent();
                    //returnIntent.putExtra("chat",mChatModel);

                    startActivity(new Intent(AdvertDetails.this,HomeActivity.class).putExtra("chat",mChatModel));
                    //setResult(Activity.RESULT_OK,returnIntent);
                    //finish();
                } else {
                    Helper.showSnackBarMessage("خطأ من فضلك اعد المحاوله", AdvertDetails.this);
                }
            }
        }, new Connector.ErrorCallback() {
            @Override
            public void onError(VolleyError error) {
                Helper.showSnackBarMessage("خطأ من فضلك اعد المحاوله", AdvertDetails.this);
            }
        });



        fab_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_menu.close(false);
                if (util.IsLogged()) {
                    home_menu.close(true);
                    Intent intent = new Intent(AdvertDetails.this, AddNewDeliveryOrTaxiService.class);
                    intent.putExtra(GMethods.ADVERT_TYPE, 0);
                    startActivity(intent);
                } else {
                    //Toast.makeText(HomeActivity.this, "من فضلك قم بتسجيل الدخول", Toast.LENGTH_SHORT).show();
                    Helper.showSnackBarMessage("من فضلك قم بتسجيل الدخول",AdvertDetails.this);
                }
            }
        });

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_menu.close(false);
                if (!util.IsLogged()) {

                    //Toast.makeText(HomeActivity.this, "انت غير مسجل", Toast.LENGTH_LONG).show();
                    Helper.showSnackBarMessage("انت غير مسجل",AdvertDetails.this);
                } else {
                    home_menu.close(true);
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AdvertDetails.this);
                    preferences.edit().remove("appNotifications").apply();
                    ShortcutBadger.removeCount(AdvertDetails.this);
                    startActivity(new Intent(AdvertDetails.this,HomeActivity.class).putExtra("goToChat",true));
                    home_menu.setVisibility(View.GONE);
                }
            }
        });
        fab_taxi = findViewById(R.id.taxi_fab);
        fab_taxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_menu.close(false);
                if (util.IsLogged()) {
                    home_menu.close(true);
                    Intent intent = new Intent(AdvertDetails.this, AddNewDeliveryOrTaxiService.class);
                    intent.putExtra(GMethods.ADVERT_TYPE, 1);
                    startActivity(intent);
                } else {
                    //Toast.makeText(HomeActivity.this, "من فضلك قم بتسجيل الدخول", Toast.LENGTH_SHORT).show();
                    Helper.showSnackBarMessage("من فضلك قم بتسجيل الدخول",AdvertDetails.this);
                }
            }
        });
        fab_advert = findViewById(R.id.advert_fab);
        fab_advert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_menu.close(false);
                if (util.IsLogged()) {
                    home_menu.close(true);
                    Intent intent = new Intent(AdvertDetails.this, AddNewAdvertActivity.class);
                    startActivity(intent);
                } else {
                    //Toast.makeText(HomeActivity.this, "من فضلك قم بتسجيل الدخول", Toast.LENGTH_SHORT).show();
                    Helper.showSnackBarMessage("من فضلك قم بتسجيل الدخول",AdvertDetails.this);
                }
            }
        });


        if (!util.IsLogged()) {
            home_menu.setVisibility(View.GONE);
        }

        if (util.getDelivery()) {
            fab_delivery.setVisibility(View.GONE);
        } else {
            //fab_delivery.setVisibility(View.VISIBLE);
        }

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
        whatsapp = findViewById(R.id.whatsapp);
        num_of_watching = findViewById(R.id.num_of_watching);
        notification_btn = findViewById(R.id.notification_btn);
        price = findViewById(R.id.tv_price);
        date = findViewById(R.id.tv_date);

        ic_back.setOnClickListener(this);
        ic_location.setOnClickListener(this);
        ic_call.setOnClickListener(this);
        ic_chat.setOnClickListener(this);
        ic_profile.setOnClickListener(this);

        sliderLayout = findViewById(R.id.slider);
        //sliderLayout.setPresetTransformer(SliderLayout.Transformer.Stack);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setDuration(4000);


        notification_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdvertDetails.this,HomeActivity.class).putExtra("goToNotification",true));
            }
        });

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

    private void SetData(final Product product) {
        current_product = product;

        for (int j = 0; j < product.getImages().size(); j++){
            realImages.add(GMethods.IMAGE_URL + product.getImages().get(j));
        }

        if (product.getImages().isEmpty()) {
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView
                    .image("http://dbabapp.com/api-dbab/public/uploads/shops/default2.png")
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop);
            sliderLayout.addSlider(textSliderView);
            sliderLayout.stopAutoCycle();
        } else {
            for (int i = 0; i < product.getImages().size(); i++) {
                GMethods.writeToLog(product.getImages().get(i));
                final TextSliderView textSliderView = new TextSliderView(this);
                textSliderView
                        .image(GMethods.IMAGE_URL+product.getImages().get(i))
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop);
                sliderLayout.addSlider(textSliderView);
                sliderLayout.startAutoCycle();
                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView slider) {
                        final ImageViewer.Builder builder = new ImageViewer.Builder(AdvertDetails.this, realImages);
                        builder.setStartPosition(sliderLayout.getCurrentPosition());
                        builder.show();
                    }
                });
            }
        }

        tv_address.setText(product.getName());
        //Helper.writeToLog(product.getPrice());
        Helper.writeToLog(product.getId());
        if (!product.getPrice().equals("0") && !product.getPrice().equals("٠")) {
            price.setVisibility(View.VISIBLE);
            price.setText("السعر : " + product.getPrice() + " ريال");
        } else {
            price.setVisibility(View.GONE);
        }
        tv_category_name.setText( "القسم : "+product.getCategory_name());
        tv_description.setText(product.getBody());

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.writeToLog(current_product.getMobile());
                openWhatsappContact("+966"+current_product.getMobile());
            }
        });
        if (util.IsLogged()) {
            if (product.getUser_id().equals(util.GetCurrentUser().getId())) {
                whatsapp.setVisibility(View.INVISIBLE);
            } else {
                whatsapp.setVisibility(View.VISIBLE);
            }
        } else {
            whatsapp.setVisibility(View.VISIBLE);
        }

        Log.i("Number watchin ",current_product.getCount()+"");
        Log.i("Date",current_product.getDate()+"");
        num_of_watching.setText(product.getCount());
        date.setText(current_product.getDate());

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

                if (util.IsLogged()) {
                    if (!util.GetCurrentUser().getId().equals(current_product.getUser_id())) {
                        String url = Connector.createStartChatUrl()+"?message=&user_id="+util.GetCurrentUser().getId()+"&to_id="+current_product.getUser_id();
                        Helper.writeToLog(url);
                        mConnectorSendMessage.getRequest((String) TAG, url);
                    } else {
                        Helper.showSnackBarMessage("لايمكنك مراسلة نفسك", AdvertDetails.this);
                    }
                } else {
                    Helper.showSnackBarMessage("برجاء تسجيل الدخول", AdvertDetails.this);
                }

                break;
            case R.id.ic_profile:
                startActivity(new Intent(AdvertDetails.this, ProfileActivity.class).putExtra("user_id",current_product.getUser_id()));
                break;
        }
    }


    void openWhatsappContact(String number) {
        Uri uri = Uri.parse("smsto:" + number);
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.setPackage("com.whatsapp");
        startActivity(Intent.createChooser(i, ""));
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (util.getDelivery()) {
            fab_delivery.setVisibility(View.GONE);
        } else {
            //fab_delivery.setVisibility(View.VISIBLE);
        }


        home_menu.close(false);
        if (util.getDelivery()) {
            fab_delivery.setVisibility(View.GONE);
        } else {
            //fab_delivery.setVisibility(View.VISIBLE);
        }


        if (!util.IsLogged()) {
            home_menu.setVisibility(View.GONE);
        } else {
            home_menu.setVisibility(View.VISIBLE);
        }


        if (util.getTaxi()) {
            fab_taxi.setVisibility(View.GONE);
        } else {
            //fab_taxi.setVisibility(View.VISIBLE);
        }


        if (util.getTaxi()) {
            fab_taxi.setVisibility(View.GONE);
        } else {
            //fab_taxi.setVisibility(View.VISIBLE);
        }

    }
}
