package com.ozoon.ozoon.UI.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
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
import com.ozoon.ozoon.CallBacks.SuccessCallBack;
import com.ozoon.ozoon.Presenters.MainPresenter;
import com.ozoon.ozoon.Presenters.RequestTaxiPresenter;
import com.ozoon.ozoon.R;
import com.ozoon.ozoon.Utils.GMethods;
import com.ozoon.ozoon.Utils.GPSTracker;
import com.ozoon.ozoon.Utils.Helper;
import com.ozoon.ozoon.Utils.StorageUtil;
import com.ozoon.ozoon.Utils.WorkaroundMapFragment;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AskForTaxiActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.num_of_drivers)
    TextView numOfDrivers;
    private static final String TAG = AskForTaxiActivity.class.getName();
    MarkerOptions markerOptions;
    GPSTracker gps;
    MainPresenter mainPresenter;

    Marker marker;

    RequestTaxiPresenter presenter;
    StorageUtil util;

    ProgressDialog progressDialog;

    Button nextButton;
    Button lastButton;
    TextView addressEditText;
    TextView textView;
    TextView toolbarTitle;

    EditText priceEdit;

    ImageView imageView;

    ImageView chat;

    ImageView back_btn,notification_btn;
    View view;
    ImageView image2;
    String addressManual = "";
    String price;


    String address;
    String toAddress;

    String city;
    String toCity;


    View priceParent;

    boolean secondClick = false;

    double fromLat;
    double fromLot;

    Button btn_last;

    GoogleMap googleMap;

    boolean located = false;


    double toLat;
    double toLot;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_for_taxi);
        ButterKnife.bind(this);
        WorkaroundMapFragment mapFragment = (WorkaroundMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mainPresenter = new MainPresenter();
        nextButton = findViewById(R.id.btn_nex);
        addressEditText = findViewById(R.id.address);
        textView = findViewById(R.id.text);
        toolbarTitle = findViewById(R.id.toolbar_title);
        lastButton = findViewById(R.id.btn_nex_last);
        imageView = findViewById(R.id.image);
        image2 = findViewById(R.id.dollar);
        view = findViewById(R.id.map);
        back_btn = findViewById(R.id.back_btn);
        priceEdit = findViewById(R.id.price);
        priceParent = findViewById(R.id.price_parent);
        btn_last = findViewById(R.id.btn_last);
        chat = findViewById(R.id.chat);
        notification_btn = findViewById(R.id.notification_btn);

        notification_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AskForTaxiActivity.this,HomeActivity.class).putExtra("goToNotification",true));
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AskForTaxiActivity.this, HomeActivity.class).putExtra("goToChat",true));
                finish();
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        util = StorageUtil.getInstance().doStuff(this);

        presenter = new RequestTaxiPresenter();

        mainPresenter.getTaxiCount(new SuccessCallBack() {
            @Override
            public void OnSuccess() {
                numOfDrivers.setText("خطا في معرفة عدد السائقين");
            }

            @Override
            public void OnFailure(String message) {
                numOfDrivers.setText("عدد السائقين المتوفرين حاليا : "+ message);
            }

            @Override
            public void OnServerError() {
                numOfDrivers.setText("خطا في معرفة عدد السائقين");
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (secondClick) {
                    addressManual = addressEditText.getText().toString();
                    addressEditText.setText("");
                    view.setVisibility(View.GONE);
                    nextButton.setVisibility(View.GONE);
                    lastButton.setVisibility(View.GONE);
                    addressEditText.setVisibility(View.GONE);
                    priceParent.setVisibility(View.VISIBLE);
                    numOfDrivers.setVisibility(View.GONE);
                    toolbarTitle.setText("تحديد تكلفة الخدمة");
                } else {
                    secondClick = true;
                    addressEditText.setVisibility(View.VISIBLE);
                    addressEditText.setText("");
                    marker.setTitle("الواجهة");
                    marker.showInfoWindow();
                    toolbarTitle.setText("تحديد واجهة المستخدم");
                }
            }
        });


        addressEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(AskForTaxiActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }

            }
        });

        btn_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price = priceEdit.getText().toString();
                Geocoder geocoder;
                List<Address> fromAddresses;
                List<Address> toAddresses = null;
                geocoder = new Geocoder(AskForTaxiActivity.this, Locale.getDefault());

                try {
                    fromAddresses = geocoder.getFromLocation(fromLat, fromLot, 1);
                    address = fromAddresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    city = fromAddresses.get(0).getAdminArea();
                    String country = fromAddresses.get(0).getCountryName();

                    toAddresses = geocoder.getFromLocation(toLat, toLot, 1);
                    toAddress = toAddresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    toCity = toAddresses.get(0).getAdminArea();
                    String toCountry = toAddresses.get(0).getCountryName();
                    GMethods.writeToLog("From Address " + address + " From City " + city + " From Country " + country);
                    GMethods.writeToLog("To Address " + toAddress + " To City " + toCity + " To Country " + toCountry);
                } catch (IOException e) {
                    e.printStackTrace();

                }
                if (toAddresses == null){
                    //Toast.makeText(AskForTaxiActivity.this,"لا يوجد انترنت",Toast.LENGTH_LONG).show();
                    Helper.showSnackBarMessage("لا يوجد انترنت",AskForTaxiActivity.this);
                    finish();
                }
                if (TextUtils.isEmpty(price)) {
                    //Toast.makeText(AskForTaxiActivity.this, "من فضلك ادخل السعر", Toast.LENGTH_LONG).show();
                    Helper.showSnackBarMessage("من فضلك ادخل السعر",AskForTaxiActivity.this);
                } else {
                    sendRequestTaxi();
                }
            }
        });


        if (ContextCompat.checkSelfPermission(AskForTaxiActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AskForTaxiActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
        } else {
            progressDialog = GMethods.show_progress_dialoug(this,"جاري تحديد موقعك",false);
            gps = new GPSTracker(AskForTaxiActivity.this, new GPSTracker.OnGetLocation() {
                @Override
                public void onGetLocation(double longtiude, double lantitude) {
                    if (googleMap != null && !located) {
                        gps.stopUsingGPS();
                        located = true;
                        progressDialog.dismiss();
                        GMethods.writeToLog("Interface 1 : " + longtiude + " " + lantitude);
                        LatLng sydney = new LatLng(longtiude, lantitude);
                        fromLot = lantitude;
                        fromLat = longtiude;
                        toLat = fromLat;
                        toLot = fromLot;

                        marker.setPosition(sydney);
                        marker.showInfoWindow();
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        zoomIn(sydney);
                    }
                }
            });

            // check if GPS enabled
            if (gps.canGetLocation()) {
                if (gps.getLongitude() != 0 && gps.getLatitude() != 0) {
                    if (googleMap != null) {
                        gps.stopUsingGPS();
                        progressDialog.dismiss();
                        located = true;
                        fromLot = gps.getLatitude();
                        fromLat = gps.getLongitude();
                        LatLng sydney = new LatLng(fromLot, fromLat);
                        toLat = fromLat;
                        toLot = fromLot;

                        marker.setPosition(sydney);
                        marker.showInfoWindow();
                        GMethods.writeToLog("Interface : " + fromLat + " " + fromLot);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        zoomIn(sydney);
                    }
                }
            } else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                progressDialog.dismiss();
                gps.showSettingsAlert();
            }
        }


    }
    public void onMapReady(final GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
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

        final LatLng latLng = new LatLng(31.203156318491384, 29.915557913482193);
        fromLot = 29.915557913482193;
        fromLat = 31.203156318491384;
        toLat = fromLat;
        toLot = fromLot;
        markerOptions = new MarkerOptions().position(latLng)
                .title("مكان الاقلاع").draggable(true);


        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        zoomIn(latLng);

        marker = googleMap.addMarker(markerOptions);
        marker.showInfoWindow();


        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                if (secondClick) {
                    toLat = marker.getPosition().latitude;
                    toLot = marker.getPosition().longitude;


                    Geocoder geocoder = new Geocoder(AskForTaxiActivity.this, Locale.getDefault());
                    List<Address> toAddresses = null;
                    try {
                        toAddresses = geocoder.getFromLocation(toLat, toLot, 1);
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                    if (toAddresses != null) {
                        toAddress = toAddresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        toCity = toAddresses.get(0).getAdminArea();
                    } else {
                        //Toast.makeText(AskForTaxiActivity.this,"لا يوجد انترنت",Toast.LENGTH_LONG).show();
                        Helper.showSnackBarMessage("لا يوجد انترنت",AskForTaxiActivity.this);
                        finish();
                    }
                    addressEditText.setText(toAddress);

                    GMethods.writeToLog("To lat" + toLat + " " + " to lot" + toLot);
                } else {
                    fromLat = marker.getPosition().latitude;
                    fromLot = marker.getPosition().longitude;

                    Geocoder geocoder = new Geocoder(AskForTaxiActivity.this, Locale.getDefault());
                    List<Address> toAddresses = null;
                    try {
                        toAddresses = geocoder.getFromLocation(fromLat, fromLot, 1);
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                    if (toAddresses!= null) {
                        toAddress = toAddresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        toCity = toAddresses.get(0).getAdminArea();

                    } else {
                        //Toast.makeText(AskForTaxiActivity.this,"لا يوجد انترنت",Toast.LENGTH_LONG).show();
                        Helper.showSnackBarMessage("لا يوجد انترنت",AskForTaxiActivity.this);
                        finish();
                    }
                    addressEditText.setText(toAddress);

                    GMethods.writeToLog("from lat " + fromLat + " " + "from lot" + fromLot);
                }
            }
        });


        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                marker.setPosition(latLng);
                marker.showInfoWindow();
                if (secondClick) {
                    toLat = marker.getPosition().latitude;
                    toLot = marker.getPosition().longitude;


                    Geocoder geocoder = new Geocoder(AskForTaxiActivity.this, Locale.getDefault());
                    List<Address> toAddresses = null;
                    try {
                        toAddresses = geocoder.getFromLocation(toLat, toLot, 1);
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                    if (toAddresses != null) {
                        toAddress = toAddresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        toCity = toAddresses.get(0).getAdminArea();
                    } else {
                        //Toast.makeText(AskForTaxiActivity.this,"لا يوجد انترنت",Toast.LENGTH_LONG).show();
                        Helper.showSnackBarMessage("لا يوجد انترنت",AskForTaxiActivity.this);
                        finish();
                    }
                    addressEditText.setText(toAddress);

                    GMethods.writeToLog("To lat" + toLat + " " + " to lot" + toLot);
                } else {
                    fromLat = marker.getPosition().latitude;
                    fromLot = marker.getPosition().longitude;

                    Geocoder geocoder = new Geocoder(AskForTaxiActivity.this, Locale.getDefault());
                    List<Address> toAddresses = null;
                    try {
                        toAddresses = geocoder.getFromLocation(fromLat, fromLot, 1);
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                    if (toAddresses!= null) {
                        toAddress = toAddresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        toCity = toAddresses.get(0).getAdminArea();

                    } else {
                        //Toast.makeText(AskForTaxiActivity.this,"لا يوجد انترنت",Toast.LENGTH_LONG).show();
                        Helper.showSnackBarMessage("لا يوجد انترنت",AskForTaxiActivity.this);
                        finish();
                    }
                    addressEditText.setText(toAddress);

                    GMethods.writeToLog("from lat " + fromLat + " " + "from lot" + fromLot);
                }
            }
        });


    }


    private void sendRequestTaxi() {
        final ProgressDialog progressDialog = GMethods.show_progress_dialoug(this, "جاري طلب التاكسي", true);


        presenter.RequestTaxi(util.GetCurrentUser().getId(), String.valueOf(toLot), String.valueOf(toLat), toAddress, String.valueOf(fromLot), String.valueOf(fromLat), address, toCity, city, price, new SuccessCallBack() {
            @Override
            public void OnSuccess() {
                progressDialog.dismiss();
                //Toast.makeText(AskForTaxiActivity.this, "تم طلب التاكسي بنجاح", Toast.LENGTH_LONG).show();
                //Helper.showSnackBarMessage("تم طلب التاكسي بنجاح",AskForTaxiActivity.this);
                addressEditText.setText("");
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result","done");
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }

            @Override
            public void OnFailure(String message) {

            }

            @Override
            public void OnServerError() {

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName());
                marker.setPosition(place.getLatLng());
                zoomIn(place.getLatLng());
                marker.showInfoWindow();
                addressEditText.setText(place.getName());
                if (secondClick) {
                    toLat = place.getLatLng().latitude;
                    toLot = place.getLatLng().longitude;
                } else {
                    fromLat = place.getLatLng().latitude;
                    fromLot = place.getLatLng().longitude;
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
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


    @Override
    protected void onStart() {
        super.onStart();
        if (!located) {
            if (gps.providerAndNetworkEnabled()) {
                progressDialog.show();
            }

            gps = new GPSTracker(AskForTaxiActivity.this, new GPSTracker.OnGetLocation() {
                @Override
                public void onGetLocation(double longtiude, double lantitude) {
                    if (googleMap != null && gps.providerAndNetworkEnabled()) {
                        gps.stopUsingGPS();
                        progressDialog.dismiss();
                        GMethods.writeToLog("Interface 2 : " + longtiude + " " + lantitude);
                        LatLng sydney = new LatLng(longtiude, lantitude);
                        fromLot = lantitude;
                        fromLat = longtiude;
                        located = true;
                        toLat = fromLat;
                        toLot = fromLot;

                        marker.setPosition(sydney);
                        marker.showInfoWindow();
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        zoomIn(sydney);
                    }
                }
            });

            // check if GPS enabled
            if (gps.canGetLocation() && gps.providerAndNetworkEnabled()) {
                if (gps.getLongitude() != 0 && gps.getLatitude() != 0) {
                    if (googleMap != null) {
                        gps.stopUsingGPS();
                        progressDialog.dismiss();
                        located = true;
                        fromLot = gps.getLatitude();
                        fromLat = gps.getLongitude();
                        LatLng sydney = new LatLng(fromLot, fromLat);
                        toLat = fromLat;
                        toLot = fromLot;
                        marker.setPosition(sydney);
                        marker.showInfoWindow();
                        GMethods.writeToLog("Interface 2 : " + fromLat + " " + fromLot);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        zoomIn(sydney);
                    }
                }
            }
        }
    }

}
