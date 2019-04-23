package com.ozoon.ozoon.UI.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ozoon.ozoon.CallBacks.AllCategoriesCallBack;
import com.ozoon.ozoon.CallBacks.GetProductCallBack;
import com.ozoon.ozoon.CallBacks.GetSubCategoriesCallBack;
import com.ozoon.ozoon.CallBacks.SuccessCallBack;
import com.ozoon.ozoon.CallBacks.UploadImageCallBack;
import com.ozoon.ozoon.Model.Objects.Category;
import com.ozoon.ozoon.Model.Objects.Product;
import com.ozoon.ozoon.Model.Objects.SubCategory;
import com.ozoon.ozoon.Presenters.MainPresenter;
import com.ozoon.ozoon.Presenters.PublishAdsPresenter;
import com.ozoon.ozoon.R;
import com.ozoon.ozoon.Utils.GMethods;
import com.ozoon.ozoon.Utils.GPSTracker;
import com.ozoon.ozoon.Utils.Helper;
import com.ozoon.ozoon.Utils.StorageUtil;
import com.ozoon.ozoon.Utils.WorkaroundMapFragment;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddNewAdvertActivity extends AppCompatActivity implements View.OnClickListener,OnMapReadyCallback {
    private static final String TAG = AddNewAdvertActivity.class.getSimpleName();
    private PublishAdsPresenter presenter;
    StorageUtil util;
    private ImageView ic_back, ic_notification, img1, img2, img3, img4;
    private String current_key="0", img1_key = "images[0]", img2_key = "images[1]", img3_key = "images[2]", img4_key = "images[3]", title, address,price="0", description, category_id = "", subcategory_id = "";
    private EditText ed_service_title, ed_service_address, ed_description,ed_place,ed_price;
    private View mapParent;
    LinearLayout imagesParent;
    private TextView tolbarTitle;
    private Button publish_btn,place_choose,addMorePhotos;
    ProgressDialog progressDialog2;
    ProgressDialog progressDialog3;
    ArrayList<String> images;
    MainPresenter presenter2;
    View parentLayout;
    private Spinner categories_spinner, sub_categories_spinner;
    private HashMap<String, String> SelectedImages = new HashMap<>();

    ArrayList<String> str_listGlobal;
    ArrayList<String> str_list;

    GoogleMap googleMap;
    MarkerOptions markerOptions;
    Marker marker;

    String currentKey = "images[0]";

    ArrayList<Category> list;
    ArrayList<String> ids ;
    ArrayList<String> names;

    double lat,lng;

    Product product;

    boolean located = false;
    boolean mImageUploaded = false;
    boolean emptySubCategories = false;

    ImageView drawableImage;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    ArrayList<Category> categoriesGlobal;


    GPSTracker gps;

    String productId;

    double longitude,latitude;

    Product data = null;

    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_advert);
        GMethods.ChangeFont(this);
        presenter = new PublishAdsPresenter();
        presenter2 = new MainPresenter();
        images = new ArrayList<>();
        util = StorageUtil.getInstance().doStuff(this);
        InitViews();
        parentLayout = findViewById(R.id.parent_layout);
        GetCategories();
        Setup_SubCategory_Spinner(new ArrayList<SubCategory>());

        ids = new ArrayList<>();
        names = new ArrayList<>();

        WorkaroundMapFragment mapFragment = (WorkaroundMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        progressDialog3 = GMethods.show_progress_dialoug(this,"جاري تحديد موقعك",false);
        if (ContextCompat.checkSelfPermission(AddNewAdvertActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddNewAdvertActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
        } else {
            gps = new GPSTracker(AddNewAdvertActivity.this, new GPSTracker.OnGetLocation() {
                @Override
                public void onGetLocation(double longtiude, double lantitude) {
                    if (googleMap != null && !located) {
                        gps.stopUsingGPS();
                        progressDialog3.dismiss();
                        GMethods.writeToLog("Interface : " + longtiude + " " + lantitude);
                        LatLng sydney = new LatLng(longtiude, lantitude);
                        lat = longtiude;
                        located = true;
                        lng = lantitude;

                        marker.setPosition(sydney);
                        marker.showInfoWindow();
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        zoomIn(sydney);
                    }
                }
            });

            // check if GPS enabled
            if (gps.canGetLocation()) {

                //latitude = gps.getLatitude();
                //longitude = gps.getLongitude();
            } else {
                // can't get location
                progressDialog3.dismiss();
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                //gps.showSettingsAlert();
            }
        }





    }

    private void InitViews() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("جاري جلب البيانات");

        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        imagesParent = findViewById(R.id.images_parent);
        addMorePhotos = findViewById(R.id.btn_add_more_photos);
        addMorePhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImage();
            }
        });
        mapParent = findViewById(R.id.map_parent);
        ic_notification = findViewById(R.id.notification_btn);
        tolbarTitle = findViewById(R.id.toolbar_title);

        ic_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddNewAdvertActivity.this,HomeActivity.class).putExtra("goToNotification",true));
            }
        });

        drawableImage = findViewById(R.id.image_id);

        ed_service_address = findViewById(R.id.ed_user_address);

        ed_price = findViewById(R.id.ed_service_price);

        ed_service_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(AddNewAdvertActivity.this,v);
                parentLayout.setVisibility(View.GONE);
                mapParent.setVisibility(View.VISIBLE);
                ed_place.setVisibility(View.VISIBLE);
                place_choose.setVisibility(View.VISIBLE);
                if (!gps.canGetLocation()){
                    gps.showSettingsAlert();
                }
            }
        });

        ed_service_title = findViewById(R.id.ed_service_title);

        ed_place = findViewById(R.id.place);

        ed_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(AddNewAdvertActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }

            }
        });


        ed_description = findViewById(R.id.ed_description);

        publish_btn = findViewById(R.id.btn_publish);
        place_choose = findViewById(R.id.choose_location);


        place_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Geocoder geocoder = new Geocoder(AddNewAdvertActivity.this, Locale.getDefault());
                List<Address> toAddresses = null;
                try {
                    toAddresses = geocoder.getFromLocation(lat, lng, 1);
                } catch (IOException e) {
                    e.printStackTrace();

                }
                if (toAddresses != null) {
                    String toAddress = toAddresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String toCity = toAddresses.get(0).getAdminArea();
                    ed_service_address.setText(toAddress);
                } else {
                    //Toast.makeText(AddNewAdvertActivity.this,"لا يوجد انترنت",Toast.LENGTH_LONG).show();
                    Helper.showSnackBarMessage("لا يوجد انترنت",AddNewAdvertActivity.this);
                    finish();
                }
                GMethods.writeToLog("To lat" + lat + " " + " to lot" + lng);

                mapParent.setVisibility(View.GONE);
                ed_place.setVisibility(View.GONE);
                place_choose.setVisibility(View.GONE);
                parentLayout.setVisibility(View.VISIBLE);
            }
        });

        ic_back = findViewById(R.id.back_btn);

        categories_spinner = findViewById(R.id.main_category_spinner);
        sub_categories_spinner = findViewById(R.id.sub_category_spinner);

        ic_back.setOnClickListener(this);
        publish_btn.setOnClickListener(this);
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);
    }

    private void GetCategories() {
        progressDialog.show();
        presenter.GetAllCategories(util.GetCurrentUser().getId(), new AllCategoriesCallBack() {
            @Override
            public void OnSuccess(ArrayList<Category> categories) {
                progressDialog.dismiss();
                categoriesGlobal = categories;
                Setup_Category_Spinner(categories);
                if (getIntent().hasExtra("product")){
                    product = (Product) getIntent().getSerializableExtra("product");
                    mImageUploaded = true;
                    productId = product.getId();
                    tolbarTitle.setText("تعديل الاعلان");
                    GetData();
                }
            }

            @Override
            public void OnFailure(String message) {
                progressDialog.dismiss();
                GMethods.show_alert_dialoug(AddNewAdvertActivity.this,
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
                GMethods.show_alert_dialoug(AddNewAdvertActivity.this,
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

    private void GetSubCategories(String category_id) {
        progressDialog.show();
        presenter.GetSubCategories(category_id, new GetSubCategoriesCallBack() {
            @Override
            public void OnSuccess(ArrayList<SubCategory> subCategories) {
                progressDialog.dismiss();
                sub_categories_spinner.setVisibility(View.VISIBLE);
                emptySubCategories = false;
                for (int i = 0 ;i<subCategories.size();i++){
                    ids.add(subCategories.get(i).getId());
                    names.add(subCategories.get(i).getName());
                }
                Setup_SubCategory_Spinner(subCategories);
            }


            @Override
            public void OnFailure(String message) {
                progressDialog.dismiss();
                /*GMethods.show_alert_dialoug(AddNewAdvertActivity.this,
                        message,
                        getString(R.string.app_name),
                        true,
                        "",
                        "",
                        null,
                        null);*/
                emptySubCategories = true;
                sub_categories_spinner.setVisibility(View.GONE);
            }

            @Override
            public void OnServerError() {
                progressDialog.dismiss();
                GMethods.show_alert_dialoug(AddNewAdvertActivity.this,
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

    private void Setup_Category_Spinner(final ArrayList<Category> list) {
        this.list = list;
        // adding string list of categories names
        ArrayList<String> str_list = new ArrayList<>();
        str_list.add("القسم الرئيسي");
        for (int i = 0; i < list.size(); i++) {
            str_list.add(list.get(i).getName());
        }


        str_listGlobal = str_list;

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, str_list) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories_spinner.setAdapter(adapter);
        categories_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    category_id = list.get(position-1).getId();
                    GetSubCategories(category_id);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category_id = "";
            }
        });

    }

    private void Setup_SubCategory_Spinner(final ArrayList<SubCategory> list) {

        // adding string list of categories names
        str_list = new ArrayList<>();
        str_list.add("القسم الفرعي");
        for (int i = 0; i < list.size(); i++) {
            str_list.add(list.get(i).getName());
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, str_list) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sub_categories_spinner.setAdapter(adapter);
        sub_categories_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    subcategory_id = list.get(position-1).getId();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                subcategory_id = "";
            }
        });

        if (data != null) {
            Helper.writeToLog(data.getSubcategory_id() + "");
            if (!data.getSubcategory_id().equals("0")) {
                sub_categories_spinner.setSelection(str_list.indexOf(names.get(ids.indexOf(data.getSubcategory_id()))));
            }
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

    public void Checker() {

        title = ed_service_title.getText().toString().trim();
        address = ed_service_address.getText().toString().trim();
        description = ed_description.getText().toString().trim();
        if (!TextUtils.isEmpty(ed_price.getText())) {
            price = ed_price.getText().toString();
        }

        if (title.isEmpty()) {
            //Toast.makeText(this, "برجاء كتابة عنوان الخدمة", Toast.LENGTH_SHORT).show();
            Helper.showSnackBarMessage("برجاء كتابة عنوان الخدمة",AddNewAdvertActivity.this);
        } else if (address.isEmpty()) {
            //Toast.makeText(this, "برجاء كتابة عنوان المستخدم", Toast.LENGTH_SHORT).show();
            Helper.showSnackBarMessage("برجاء كتابة عنوان المستخدم",AddNewAdvertActivity.this);
        } else if (description.isEmpty()) {
            //Toast.makeText(this, "برجاء كتابة وصف العنوان", Toast.LENGTH_SHORT).show();
            Helper.showSnackBarMessage("برجاء كتابة وصف العنوان",AddNewAdvertActivity.this);
        } else if (category_id.isEmpty()) {
            //Toast.makeText(this, "برجاء اختيار القسم الرئيسي", Toast.LENGTH_SHORT).show();
            Helper.showSnackBarMessage("برجاء اختيار القسم الرئيسي",AddNewAdvertActivity.this);
        } else if (subcategory_id.isEmpty() && !emptySubCategories){
            //Toast.makeText(this, "برجاء اختيار القسم الفرعي", Toast.LENGTH_SHORT).show();
            Helper.showSnackBarMessage("برجاء اختيار القسم الفرعي",AddNewAdvertActivity.this);
        } else if (!util.IsLogged()){
            //Toast.makeText(this, "من فضلك قم بتسجيل الدخول", Toast.LENGTH_SHORT).show();
            Helper.showSnackBarMessage("من فضلك قم بتسجيل الدخول",AddNewAdvertActivity.this);
        } else if (!mImageUploaded){
            //Toast.makeText(this, "من فضلك قم بارفاق صوره واحده علي الاقل", Toast.LENGTH_SHORT).show();
            Helper.showSnackBarMessage("من فضلك قم بارفاق صوره واحده علي الاقل",AddNewAdvertActivity.this);
        }
        else {
            //getLocation();
            if (publish_btn.getText().equals("تعديل الاعلان")){
                editAdService();
            } else {
                sendAdService();
            }

        }
    }

    private void editAdService() {
        GMethods.writeToLog(String.valueOf(lat) + " " + String.valueOf(lng));

        progressDialog2 = GMethods.show_progress_dialoug(AddNewAdvertActivity.this,
                "جاري تعديل الاعلان",
                true);

        HashMap<String,String> finalImages = new HashMap<>();

        for (int i=0;i<images.size();i++){
            finalImages.put("images["+i+"]" ,images.get(i));
        }

        presenter.editProduct(price,title,description,category_id,util.GetCurrentUser().getId(),subcategory_id,address,String.valueOf(lng),String.valueOf(lat),finalImages,productId, new SuccessCallBack() {
            @Override
            public void OnSuccess() {
                progressDialog2.dismiss();
                clearData();
                //Toast.makeText(AddNewAdvertActivity.this, "تم تعديل الاعلان بنجاح", Toast.LENGTH_LONG).show();
                Helper.showSnackBarMessage("تم تعديل الاعلان بنجاح",AddNewAdvertActivity.this);
                finish();
            }

            @Override
            public void OnFailure(String message) {
                //Toast.makeText(AddNewAdvertActivity.this, message, Toast.LENGTH_LONG).show();
                Helper.showSnackBarMessage(message,AddNewAdvertActivity.this);
            }

            @Override
            public void OnServerError() {

            }
        });


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
                .start(); // start image picker activity with Request code
    }


    private void PickImage() {
        ImagePicker.with(this)
                .setFolderMode(true) // folder mode (false by default)
                .setFolderTitle(getString(R.string.folder)) // folder selection title
                .setImageTitle(getString(R.string.select_image)) // image selection title
                .setMaxSize(1) //  Max images can be selected
                .setMultipleMode(false) //single mode
                .setShowCamera(true) // show camera or not (true by default)
                .start(); // start image picker activity with Request code
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
                UploadImage(img.getName(), GMethods.GetBitmap(img.getPath(), 700));

                if (current_key.equals(img1_key)) {
                    img1.setImageBitmap(bitmapimage);
                } else if (current_key.equals(img2_key)) {
                    img2.setImageBitmap(bitmapimage);
                } else if (current_key.equals(img3_key)) {
                    img3.setImageBitmap(bitmapimage);
                } else if (current_key.equals(img4_key)) {
                    img4.setImageBitmap(bitmapimage);
                } else {
                    CircleImageView circleImageView = new CircleImageView(this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(120, 120);
                    lp.setMargins(0, 10, 0, 10);
                    circleImageView.setLayoutParams(lp);
                    circleImageView.setImageBitmap(bitmapimage);
                    imagesParent.addView(circleImageView);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName());
                marker.setPosition(place.getLatLng());
                zoomIn(place.getLatLng());
                marker.showInfoWindow();
                ed_place.setText(place.getName());
                lat = marker.getPosition().latitude;
                lng = marker.getPosition().longitude;
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

    }

    private void UploadImage(String name, Bitmap bitmap) {
        final ProgressDialog progressDialog = GMethods.show_progress_dialoug(this,
                "جاري رفع الصورة",
                false);
        try {
            final File image = BitmapToFile(name, bitmap);
            presenter.AddImage(image, new UploadImageCallBack() {
                @Override
                public void OnSuccess(String image_path) {
                    mImageUploaded = true;
                    SelectedImages.put(current_key, image_path);
                    images.add(image_path);
                    progressDialog.dismiss();
                }

                @Override
                public void OnFailure(String message) {

                }

                @Override
                public void OnServerError() {

                }
            });
        } catch (IOException e) {

        }
    }

    private File BitmapToFile(String name, Bitmap bmap) throws IOException {
        File f = new File(this.getExternalCacheDir().getAbsolutePath() + "/" + name);
        f.createNewFile();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
        byte[] bitmapdata = bos.toByteArray();

        FileOutputStream fos = new FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();
        return f;
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    public void sendAdService(){

        HashMap<String,String> finalImages = new HashMap<>();

        GMethods.writeToLog(String.valueOf(lat) + " " + String.valueOf(lng));

        progressDialog2 = GMethods.show_progress_dialoug(AddNewAdvertActivity.this,
                "جاري اضافة الاعلان",
                true);

        for (int i =0;i<images.size();i++){
            finalImages.put("images["+i+"]",images.get(i));
        }

        presenter.addProduct(price,title,description,category_id,util.GetCurrentUser().getId(),subcategory_id,address,String.valueOf(lng),String.valueOf(lat),finalImages, new SuccessCallBack() {
            @Override
            public void OnSuccess() {
                progressDialog2.dismiss();
                clearData();
                //Toast.makeText(AddNewAdvertActivity.this, "تم اضافة الاعلان بنجاح", Toast.LENGTH_LONG).show();
                Helper.showSnackBarMessage("تم اضافة الاعلان بنجاح",AddNewAdvertActivity.this);
                int ad_count= util.GetCurrentUser().getAds_count();
                ad_count=ad_count+ 1;

                String ads = StorageUtil.getAdsCount(AddNewAdvertActivity.this);
                int ad = Integer.valueOf(ads);
                ad++;
                util.GetCurrentUser().setAds_count(ad_count);
                Helper.writeToLog(util.GetCurrentUser().getAds_count()+"");
                StorageUtil.setAdsCount(AddNewAdvertActivity.this,String.valueOf(ad));
                finish();
            }

            @Override
            public void OnFailure(String message) {
                //Toast.makeText(AddNewAdvertActivity.this, message, Toast.LENGTH_LONG).show();
                Helper.showSnackBarMessage(message,AddNewAdvertActivity.this);
            }

            @Override
            public void OnServerError() {

            }
        });
    }


    private void clearData(){
        ed_service_title.setText("");
        ed_description.setText("");
        categories_spinner.setSelection(0);
        sub_categories_spinner.setSelection(0);
        ed_service_address.setText("");
        img1.setImageResource(R.drawable.ic_default_camera_pick);
        img2.setImageResource(R.drawable.ic_default_camera_pick);
        img3.setImageResource(R.drawable.ic_default_camera_pick);
        img4.setImageResource(R.drawable.ic_default_camera_pick);
    }


    private void getLocation(){

        if (ContextCompat.checkSelfPermission(AddNewAdvertActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddNewAdvertActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
        } else {
            gps = new GPSTracker(AddNewAdvertActivity.this, new GPSTracker.OnGetLocation() {
                @Override
                public void onGetLocation(double longtiude,double lantitude) {
                    gps.stopUsingGPS();
                    GMethods.writeToLog("Interface : " + longtiude + " " + lantitude);
                    longitude = longtiude;
                    latitude = lantitude;
                    GMethods.writeToLog("Global : " + longitude + " " + latitude);
                    sendAdService();
                }
            });

            // check if GPS enabled
            if (gps.canGetLocation()) {



                GMethods.writeToLog(latitude + " " + longitude);

                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
            } else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert();
            }
        }
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        final ScrollView mScrollView = (ScrollView) findViewById(R.id.scrollMap); //parent scrollview in xml, give your scrollview id value

        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .setListener(new WorkaroundMapFragment.OnTouchListener() {
                    @Override
                    public void onTouch() {
                        mScrollView.requestDisallowInterceptTouchEvent(true);
                    }
                });


        final LatLng latLng = new LatLng(31.1925456, 29.9113959);

        markerOptions = new MarkerOptions().position(latLng)
                .title("المكان").draggable(true);
        lat = 31.1925456;
        lng = 29.9113959;

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        zoomIn(latLng);

        marker = googleMap.addMarker(markerOptions);
        marker.showInfoWindow();

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                marker.setPosition(latLng);
                marker.showInfoWindow();
                lat = marker.getPosition().latitude;
                lng = marker.getPosition().longitude;


                Geocoder geocoder = new Geocoder(AddNewAdvertActivity.this, Locale.getDefault());
                List<Address> toAddresses = null;
                try {
                    toAddresses = geocoder.getFromLocation(lat, lng, 1);
                } catch (IOException e) {
                    e.printStackTrace();

                }
                if (toAddresses != null) {
                    String toAddress = toAddresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String toCity = toAddresses.get(0).getAdminArea();
                    ed_place.setText(toAddress);
                } else {
                    //Toast.makeText(AddNewAdvertActivity.this,"لا يوجد انترنت",Toast.LENGTH_LONG).show();
                    Helper.showSnackBarMessage("لا يوجد انترنت",AddNewAdvertActivity.this);
                    finish();
                }


                GMethods.writeToLog("To lat" + lat + " " + " to lot" + lng);
            }
        });



        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                lat = marker.getPosition().latitude;
                lng = marker.getPosition().longitude;


                Geocoder geocoder = new Geocoder(AddNewAdvertActivity.this, Locale.getDefault());
                List<Address> toAddresses = null;
                try {
                    toAddresses = geocoder.getFromLocation(lat, lng, 1);
                } catch (IOException e) {
                    e.printStackTrace();

                }
                if (toAddresses != null) {
                    String toAddress = toAddresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String toCity = toAddresses.get(0).getAdminArea();
                    ed_place.setText(toAddress);
                } else {
                    //Toast.makeText(AddNewAdvertActivity.this,"لا يوجد انترنت",Toast.LENGTH_LONG).show();
                    Helper.showSnackBarMessage("لا يوجد انترنت",AddNewAdvertActivity.this);
                    finish();
                }


                GMethods.writeToLog("To lat" + lat + " " + " to lot" + lng);
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!located) {
            if (gps.providerAndNetworkEnabled()) {
                progressDialog3.show();
            }

            if (ContextCompat.checkSelfPermission(AddNewAdvertActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AddNewAdvertActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
            } else {
                gps = new GPSTracker(AddNewAdvertActivity.this, new GPSTracker.OnGetLocation() {
                    @Override
                    public void onGetLocation(double longtiude, double lantitude) {
                        gps.stopUsingGPS();
                        progressDialog3.dismiss();
                        GMethods.writeToLog("Interface : " + longtiude + " " + lantitude);
                        LatLng sydney = new LatLng(longtiude, lantitude);
                        lat = longtiude;
                        lng = lantitude;
                        located = true;


                        marker.setPosition(sydney);
                        marker.showInfoWindow();
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        zoomIn(sydney);
                    }
                });

                // check if GPS enabled
                if (gps.canGetLocation()) {

                    //latitude = gps.getLatitude();
                    //longitude = gps.getLongitude();
                }
            }
        }
    }

    private void zoomIn(LatLng latLng) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
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


    private void GetData() {
        progressDialog.show();
        presenter2.GetProduct(productId,util.GetCurrentUser().getId(), new GetProductCallBack() {
            @Override
            public void OnSuccess(Product product) {
                progressDialog.dismiss();
                Helper.writeToLog(product.getCategory_name());
                SetData(product);
            }

            @Override
            public void OnFailure(String message) {
                progressDialog.dismiss();
                GMethods.show_alert_dialoug(AddNewAdvertActivity.this,
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
                GMethods.show_alert_dialoug(AddNewAdvertActivity.this,
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



    private void SetData(Product data){
        this.data = data;
        ed_description.setText(data.getBody());
        ed_service_address.setText(data.getAddress());
        ed_service_title.setText(data.getName());
        ed_price.setText(data.getPrice());
        categories_spinner.setSelection(str_listGlobal.indexOf(data.getCategory_name()));
        lng = Double.parseDouble(data.getLongtide());
        lat = Double.parseDouble(data.getLatitude());
        if (data.getImages().size()>4){
            for (int i=4;i<data.getImages().size();i++) {
                CircleImageView circleImageView = new CircleImageView(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(120, 120);
                lp.setMargins(0, 10, 0, 10);
                circleImageView.setLayoutParams(lp);
                Picasso.with(this).load(GMethods.IMAGE_URL + data.getImages().get(i)).fit().into(circleImageView);
                imagesParent.addView(circleImageView);
            }
        }
//        Helper.writeToLog(product.getCategory_name());
        //categories_spinner.setSelection(1);
        try {
            Picasso.with(this).load(GMethods.IMAGE_URL + data.getImages().get(0)).fit().into(img1);
            img1_key = data.getImages().get(0);
            Picasso.with(this).load(GMethods.IMAGE_URL + data.getImages().get(1)).fit().into(img2);
            img1_key = data.getImages().get(0);
            Picasso.with(this).load(GMethods.IMAGE_URL + data.getImages().get(2)).fit().into(img3);
            img1_key = data.getImages().get(0);
            Picasso.with(this).load(GMethods.IMAGE_URL + data.getImages().get(3)).fit().into(img4);
            img1_key = data.getImages().get(0);
        } catch (Exception e){

        }

        images.addAll(data.getImages());

        publish_btn.setText("تعديل الاعلان");
    }


    public void openMap(View view) {
        hideKeyboard(AddNewAdvertActivity.this,view);
        parentLayout.setVisibility(View.GONE);
        mapParent.setVisibility(View.VISIBLE);
        ed_place.setVisibility(View.VISIBLE);
        place_choose.setVisibility(View.VISIBLE);
    }


}
