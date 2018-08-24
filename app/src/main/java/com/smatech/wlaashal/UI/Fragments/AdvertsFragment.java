package com.smatech.wlaashal.UI.Fragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.smatech.wlaashal.Adapters.CategoryAdsAdapter;
import com.smatech.wlaashal.Adapters.GridSpacingItemDecoration;
import com.smatech.wlaashal.CallBacks.AllProductsCallBack;
import com.smatech.wlaashal.CallBacks.GetSubCategoriesCallBack;
import com.smatech.wlaashal.Model.Objects.Product;
import com.smatech.wlaashal.Model.Objects.SubCategory;
import com.smatech.wlaashal.Presenters.MainPresenter;
import com.smatech.wlaashal.R;
import com.smatech.wlaashal.UI.Activities.AskForTaxiActivity;
import com.smatech.wlaashal.Utils.Connector;
import com.smatech.wlaashal.Utils.GMethods;
import com.smatech.wlaashal.Utils.GPSTracker;
import com.smatech.wlaashal.Utils.Helper;
import com.smatech.wlaashal.Utils.StorageUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NaderNabil216@gmail.com on 5/16/2018.
 */
public class AdvertsFragment extends Fragment {
    String Category_id = "";
    BroadcastReceiver broadcastReceiver;
    RecyclerView recyclerView;
    MainPresenter presenter;
    ProgressDialog progressDialog;
    CategoryAdsAdapter adapter;
    StorageUtil util;
    ArrayList<Product> productsGlobal;
    double lat, lon;
    GPSTracker gps;
    @BindView(R.id.swipyrefreshlayout)
    SwipeRefreshLayout mSwipyRefreshLayout;

    boolean done = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_ads, container, false);
        ButterKnife.bind(this,view);
        Category_id = getArguments().getString(GMethods.CATEGORY_ID);
        presenter = new MainPresenter();
        InitViews(view);
        InitBroadCast();
        util = StorageUtil.getInstance().doStuff(getContext());
        if (util.IsLogged()) {
            GetData(1, Category_id, util.GetCurrentUser().getId());
        } else {
            GetData(1, Category_id, "");
        }


        mSwipyRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (util.IsLogged()) {
                    GetData(1, Category_id, util.GetCurrentUser().getId());
                } else {
                    GetData(1, Category_id, "");
                }
            }
        });

        return view;
    }

    private void InitViews(View view) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("جاري جلب البيانات");

        recyclerView = view.findViewById(R.id.recyclerView);

    }

    /**
     * @param param action whether be category id or sub category id
     * @param type  1 = category_id
     *              2 = sub category id
     */
    private void GetData(int type, String param, String userId) {
        progressDialog.show();
        presenter.GetAllProducts(type, param, new AllProductsCallBack() {
            @Override
            public void OnSuccess(ArrayList<Product> products) {
                mSwipyRefreshLayout.setRefreshing(false);
                progressDialog.dismiss();
                SetData(products);
            }

            @Override
            public void OnFailure(String message) {
                mSwipyRefreshLayout.setRefreshing(false);
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
                mSwipyRefreshLayout.setRefreshing(false);
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

    private void GetDataCategory(int type, String param, String category, String userId) {
        progressDialog.show();
        presenter.GetAllProductsCategory(type, param, category, new AllProductsCallBack() {
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

    private void SetData(ArrayList<Product> products) {
        if (getArguments() != null) {
            if (getArguments().containsKey("list")) {
                productsGlobal = products;
                adapter = new CategoryAdsAdapter(products, getActivity(), 10);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(adapter);
                progressDialog = GMethods.show_progress_dialoug(getContext(), "جاري تحديد موقعك", false);
                gps = new GPSTracker(getContext(), new GPSTracker.OnGetLocation() {
                    @Override
                    public void onGetLocation(double longtiude, double lantitude) {
                        progressDialog.dismiss();
                        gps.stopUsingGPS();
                        lat = longtiude;
                        lon = lantitude;
                        for (int i = 0; i < productsGlobal.size(); i++) {
                            Product product = productsGlobal.get(i);
                            product.setDistance(String.valueOf(product.meterDistanceBetweenPoints((float) lat, (float) lon, Float.valueOf(product.getLatitude()), Float.valueOf(product.getLongtide()))));
                            Helper.writeToLog(product.getName() + " : " + product.getDistance() + "\n");
                        }
                        //Collections.reverse(productsGlobal);
                        adapter.notifyDataSetChanged();

                    }
                });
                if (gps.canGetLocation()) {

                    if (gps.getLatitude() != 0 && gps.getLongitude() != 0) {
                        progressDialog.dismiss();
                        lat = gps.getLatitude();
                        lon = gps.getLongitude();
                        Helper.writeToLog("Lat " + lat + " Lon " + lon);
                        gps.stopUsingGPS();
                        for (int i = 0; i < productsGlobal.size(); i++) {
                            Product product = productsGlobal.get(i);
                            product.setDistance(String.valueOf(product.meterDistanceBetweenPoints((float) lat, (float) lon, Float.valueOf(product.getLatitude()), Float.valueOf(product.getLongtide()))));
                            Helper.writeToLog(product.getName() + " : " + product.getDistance() + "\n");
                        }
                        //Collections.reverse(productsGlobal);
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    progressDialog.dismiss();
                    //gps.showSettingsAlert();
                }
            } else {
                productsGlobal = products;
                adapter = new CategoryAdsAdapter(products, getActivity(), 0);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(getActivity(), 2, true));
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
            }
        }
    }

    private void InitBroadCast() {
        IntentFilter intentFilter = new IntentFilter(GMethods.ACTION_UPDATE_DATA);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getStringExtra(GMethods.ADVERT_TYPE);
                if (action.equals(GMethods.PERFORME_SUBCATEGORY)) {
                    GetSubCategories();
                } else if (action.equals(GMethods.ACTION_OLDEST)) {
                    GetDataCategory(6, "1", Category_id, "");
                } else if (action.equals(GMethods.PERFORME_HIGHEST_RATE)) {
                    //Collections.sort(productsGlobal);
                    //Collections.reverse(productsGlobal);
                    //adapter.notifyDataSetChanged();
                    GetDataCategory(6, "0", Category_id, "");
                } else if (action.equals(GMethods.PERFORME_NEAREST)) {
                    if (productsGlobal != null) {
                        progressDialog = GMethods.show_progress_dialoug(getContext(), "جاري تحديد موقعك", false);
                        gps = new GPSTracker(getContext(), new GPSTracker.OnGetLocation() {
                            @Override
                            public void onGetLocation(double longtiude, double lantitude) {
                                progressDialog.dismiss();
                                gps.stopUsingGPS();
                                lat = longtiude;
                                lon = lantitude;
                                if (productsGlobal != null) {
                                    for (int i = 0; i < productsGlobal.size(); i++) {
                                        Product product = productsGlobal.get(i);
                                        product.setDistance(String.valueOf(product.meterDistanceBetweenPoints((float) lat, (float) lon, Float.valueOf(product.getLatitude()), Float.valueOf(product.getLongtide()))));
                                        Helper.writeToLog(product.getName() + " : " + product.getDistance() + "\n");
                                    }
                                    Collections.sort(productsGlobal, new Comparator<Product>() {
                                        @Override
                                        public int compare(Product o1, Product o2) {
                                            return Double.valueOf(o1.getDistance()).compareTo(Double.valueOf(o2.getDistance()));

                                        }
                                    });
                                }
                                //Collections.reverse(productsGlobal);
                                adapter.notifyDataSetChanged();

                            }
                        });
                        if (gps.canGetLocation()) {

                        } else {
                            // can't get location
                            // GPS or Network is not enabled
                            // Ask user to enable GPS/network in settings
                            progressDialog.dismiss();
                            gps.showSettingsAlert();
                        }

                    } else {
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
                }
            }
        };

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, intentFilter);
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
                        "لا يوجد اقسام فرعيه",
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
    }
}
