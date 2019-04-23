package com.ozoon.ozoon;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ozoon.ozoon.Adapters.CategoryAdsAdapter;
import com.ozoon.ozoon.CallBacks.AllProductsCallBack;
import com.ozoon.ozoon.CallBacks.GetSubCategoriesCallBack;
import com.ozoon.ozoon.Model.Objects.Product;
import com.ozoon.ozoon.Model.Objects.SubCategory;
import com.ozoon.ozoon.Presenters.MainPresenter;
import com.ozoon.ozoon.UI.Activities.AdvertDetails;
import com.ozoon.ozoon.Utils.GMethods;
import com.ozoon.ozoon.Utils.GPSTracker;
import com.ozoon.ozoon.Utils.Helper;
import com.ozoon.ozoon.Utils.StorageUtil;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    String Category_id = "";
    BroadcastReceiver broadcastReceiver;
    RecyclerView recyclerView;
    MainPresenter presenter;
    ProgressDialog progressDialog;
    CategoryAdsAdapter adapter;
    StorageUtil util;
    MapView mMapView;
    private GoogleMap googleMap;
    GPSTracker gps;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("جاري جلب البيانات");


        Category_id = getArguments().getString(GMethods.CATEGORY_ID);
        presenter = new MainPresenter();
        util = StorageUtil.getInstance().doStuff(getContext());
        InitBroadCast();
        if (util.IsLogged()) {
            GetData(1, Category_id, util.GetCurrentUser().getId());
        } else {
            GetData(1, Category_id, "");
        }
        return view;

    }


    private void GetSubCategories() {
        progressDialog.show();
        presenter.GetSubCategories(Category_id, new GetSubCategoriesCallBack() {
            @Override
            public void OnSuccess(ArrayList<SubCategory> subCategories) {
                progressDialog.dismiss();
                BuildSubCategoryDialog(subCategories);
            }

            @Override
            public void OnFailure(String message) {
                progressDialog.dismiss();
                GMethods.show_alert_dialoug(getActivity(),
                        "لا يوجد اعلانات",
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
                GMethods.show_alert_dialoug(getActivity(),
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

    private void BuildSubCategoryDialog(final ArrayList<SubCategory> subCategories) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());

        ArrayList<String> names = new ArrayList<>();
        for (SubCategory subCategory : subCategories) {
            names.add(subCategory.getName());
        }
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_singlechoice, names);

        builderSingle.setNegativeButton("رجوع", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SubCategory subCategory = subCategories.get(which);
                if (util.IsLogged()) {
                    Helper.writeToLog(subCategory.getId());
                    GetData(2, subCategory.getId(), util.GetCurrentUser().getId());
                } else {
                    GetData(2, subCategory.getId(), "");
                }
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }


    private void InitBroadCast() {
        IntentFilter intentFilter = new IntentFilter(GMethods.ACTION_UPDATE_DATA);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getStringExtra(GMethods.ACTION_TYPE);
                if (action.equals(GMethods.PERFORME_SUBCATEGORY)) {
                    GetSubCategories();
                } else if (action.equals(GMethods.PERFORME_HIGHEST_RATE)) {

                } else if (action.equals(GMethods.PERFORME_NEAREST)) {

                }
            }
        };

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, intentFilter);
    }


    private void GetData(int type, String param, String userId) {
        progressDialog.show();
        presenter.GetAllProducts(type, param, new AllProductsCallBack() {
            @Override
            public void OnSuccess(ArrayList<Product> products) {
                progressDialog.dismiss();
                SetData(products);
            }

            @Override
            public void OnFailure(String message) {
                progressDialog.dismiss();
                GMethods.show_alert_dialoug(getActivity(),
                        "لا يوجد اعلانات",
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
                GMethods.show_alert_dialoug(getActivity(),
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


    @Override
    public void onMapReady(final GoogleMap googleMap) {

        this.googleMap = googleMap;

        final ProgressDialog progressDialog2 = GMethods.show_progress_dialoug(getContext(), "جاري تحديد موقعك", false);
        gps = new GPSTracker(getContext(), new GPSTracker.OnGetLocation() {
            @Override
            public void onGetLocation(double longtiude, double lantitude) {
                progressDialog2.dismiss();
                gps.stopUsingGPS();
                Marker marker = MapFragment.this.googleMap.addMarker(new MarkerOptions().position(new LatLng(longtiude, lantitude)).title("موقعي"));
                marker.showInfoWindow();
                zoomIn(new LatLng(longtiude, lantitude));
            }
        });
        if (gps.canGetLocation()) {

            if (gps.getLongitude() != 0 && gps.getLatitude() != 0) {

                gps.stopUsingGPS();
                progressDialog2.dismiss();
                Marker marker = MapFragment.this.googleMap.addMarker(new MarkerOptions().position(new LatLng(gps.getLatitude(), gps.getLongitude())).title("موقعي"));
                marker.showInfoWindow();
                zoomIn(new LatLng(gps.getLatitude(), gps.getLongitude()));
            }

        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            progressDialog2.dismiss();
            gps.showSettingsAlert();
        }


        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (!marker.getTitle().equals("موقعي"))
                    startActivity(new Intent(getActivity(), AdvertDetails.class).putExtra(GMethods.PRODUCT_ID, ((Product) marker.getTag()).getId()));
                return true;
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    private void SetData(ArrayList<Product> products) {
        //googleMap.clear();
        for (int i = 0; i < products.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            double lat = 0;
            double lng = 0;
            if (products.get(i).getLatitude() != null && products.get(i).getLongtide() != null) {
                lat = Double.parseDouble(products.get(i).getLatitude());
                lng = Double.parseDouble(products.get(i).getLongtide());
            }

            LatLng latLng = new LatLng(lat, lng);

            markerOptions.position(latLng);
            markerOptions.title(products.get(i).getName());

            Marker marker = googleMap.addMarker(markerOptions);
            marker.setTag(products.get(i));

        }
    }


    private void zoomIn(LatLng latLng) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }

}
