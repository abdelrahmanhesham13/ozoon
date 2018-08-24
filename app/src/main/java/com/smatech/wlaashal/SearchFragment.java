package com.smatech.wlaashal;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.smatech.wlaashal.Adapters.CategoryAdsAdapter;
import com.smatech.wlaashal.Adapters.GridSpacingItemDecoration;
import com.smatech.wlaashal.CallBacks.AllProductsCallBack;
import com.smatech.wlaashal.Model.Objects.Product;
import com.smatech.wlaashal.Presenters.MainPresenter;
import com.smatech.wlaashal.UI.Activities.AdvertDetails;
import com.smatech.wlaashal.Utils.Connector;
import com.smatech.wlaashal.Utils.GMethods;
import com.smatech.wlaashal.Utils.GPSTracker;
import com.smatech.wlaashal.Utils.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    @BindView(R.id.search_text)
    EditText mSearchText;
    @BindView(R.id.cancel)
    TextView mCancelButton;
    @BindView(R.id.progress_indicator)
    ProgressBar mProgressBar;
    @BindView(R.id.products)
    RecyclerView mProducts;
    @BindView(R.id.radio_group)
    RadioRealButtonGroup mRadioGroup;
    @BindView(R.id.tv_nearest)
    TextView mNearest;
    @BindView(R.id.filterParent)
    View mFilterParent;
    @BindView(R.id.tc_oldest)
    TextView mOldest;

    Connector mSearchUsersConnector;
    Connector searchProducts;

    ArrayList<UserModelSearch> mUsers;
    UserSearchAdapter mUserSearchAdapter;
    GPSTracker gps;
    ProgressDialog progressDialog;

    ArrayList<Product> productsGlobal = null;

    int mType = 0;

    double lat,lon;

    CategoryAdsAdapter adapter;
    MainPresenter presenter;

    boolean newest = true;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, v);
        GMethods.ChangeViewFont(v);
        presenter = new MainPresenter();

        mUsers = new ArrayList<>();

        mUserSearchAdapter = new UserSearchAdapter(getActivity(), mUsers, new UserSearchAdapter.OnItemClick() {
            @Override
            public void setOnItemClick(int position) {
                startActivity(new Intent(getContext(), ProfileActivity.class).putExtra("user_id",mUsers.get(position).getId()));
            }
        });


        searchProducts = new Connector(getActivity(), new Connector.LoadCallback() {
            @Override
            public void onComplete(String tag, String response) {
                if (Connector.checkStatus(response)) {
                    if (productsGlobal != null){
                        productsGlobal.clear();
                        adapter.notifyDataSetChanged();
                    }

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray products = jsonObject.getJSONArray("products");
                        for (int i = 0 ;i<products.length();i++){
                            JSONObject product = products.getJSONObject(i);
                            String id = product.getString("id");
                            String name = product.getString("name");
                            String user_id = product.getString("user_id");
                            String user = product.getString("user");
                            String rate = product.getString("rate");
                            String latitude = product.getString("latitude");
                            String longtide =product.getString("longtide");
                            String address = product.getString("address");
                            String city = product.getString("city");
                            String image = product.getString("image");
                            productsGlobal.add(new Product(name,id,"","",longtide,latitude,"","",user,user_id,"",city,null,image,rate,address,""));
                        }
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
                            gps.showSettingsAlert();
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else {
                    if (productsGlobal != null){
                        productsGlobal.clear();
                        adapter.notifyDataSetChanged();
                    }
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
        }, new Connector.ErrorCallback() {
            @Override
            public void onError(VolleyError error) {
                if (productsGlobal != null){
                    productsGlobal.clear();
                    adapter.notifyDataSetChanged();
                }
                GMethods.show_alert_dialoug(getActivity(),
                        "خطأ بالسيرفر",
                        getString(R.string.app_name),
                        true,
                        "",
                        "",
                        null,
                        null);
            }
        });

        mSearchUsersConnector = new Connector(getActivity(), new Connector.LoadCallback() {
            @Override
            public void onComplete(String tag, String response) {
                if (mUsers != null) {
                    mUsers.clear();
                    mUserSearchAdapter.notifyDataSetChanged();
                }
                if (productsGlobal != null){
                    productsGlobal.clear();
                    adapter.notifyDataSetChanged();
                }
                if (Connector.checkStatus(response)){
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray users = jsonObject.getJSONArray("users");
                        for (int i = 0;i<users.length();i++){
                            JSONObject user = users.getJSONObject(i);
                            String id = user.getString("id");
                            String name = user.getString("name");
                            String image = user.getString("image");
                            String rate = user.getString("rate");
                            mUsers.add(new UserModelSearch(id,name,rate,image));
                        }
                        mProducts.setAdapter(mUserSearchAdapter);
                        mUserSearchAdapter.notifyDataSetChanged();
                        mProducts.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    GMethods.show_alert_dialoug(getActivity(),
                            "لا يوجد مستخدمين",
                            getString(R.string.app_name),
                            true,
                            "",
                            "",
                            null,
                            null);
                }
            }
        }, new Connector.ErrorCallback() {
            @Override
            public void onError(VolleyError error) {
                GMethods.show_alert_dialoug(getActivity(),
                        "خطأ بالسيرفر",
                        getString(R.string.app_name),
                        true,
                        "",
                        "",
                        null,
                        null);
            }
        });

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (getActivity() != null) {
                ViewCompat.setLayoutDirection(getActivity().findViewById(R.id.parent_layout), ViewCompat.LAYOUT_DIRECTION_RTL);
            }
        }


        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mFilterParent.setVisibility(View.GONE);
                if (TextUtils.isEmpty(mSearchText.getText().toString())){
                    //Toast.makeText(getContext(), "ادخل نص البحث", Toast.LENGTH_SHORT).show();
                    Helper.showSnackBarMessage("ادخل نص البحث",(AppCompatActivity) getContext());
                } else {
                    newest = true;
                    mOldest.setText("الاقدم");
                    if (mType == 1) {
                        GetData(3, mSearchText.getText().toString(), "");
                        Helper.hideKeyboard((AppCompatActivity) getActivity(), v);
                    } else {
                        Helper.hideKeyboard((AppCompatActivity) getActivity(), v);
                        mSearchUsersConnector.getRequest("SearchFragment","http://wla-ashl.com/panel/api/get_users?search="+mSearchText.getText().toString().replaceAll(" ","%20"));
                    }
                }
            }
        });


        /*RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mProducts.addItemDecoration(new GridSpacingItemDecoration(getActivity(), 2, true));
        mProducts.setLayoutManager(mLayoutManager);
        mProducts.setItemAnimator(new DefaultItemAnimator());*/

        mRadioGroup.setOnClickedButtonListener(new RadioRealButtonGroup.OnClickedButtonListener() {
            @Override
            public void onClickedButton(RadioRealButton button, int position) {
                if (position == 0){
                    mType = 0;
                } else {
                    mType = 1;
                }
            }
        });

        mNearest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        if (gps.getLatitude() != 0 && gps.getLongitude() != 0) {
                            progressDialog.dismiss();
                            gps.stopUsingGPS();
                            lat = gps.getLatitude();
                            lon = gps.getLongitude();
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
        });

        mOldest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newest) {
                    if (!TextUtils.isEmpty(mSearchText.getText().toString())){
                        searchProducts.getRequest("SearchFragment","http://wla-ashl.com/panel/api/get_products?search="+mSearchText.getText().toString().replaceAll(" ","%20")+"&sort=1");
                        ((TextView)v).setText("الاحدث");
                        newest = false;
                    }
                } else {
                    if (!TextUtils.isEmpty(mSearchText.getText().toString())) {
                        searchProducts.getRequest("SearchFragment", "http://wla-ashl.com/panel/api/get_products?search=" + mSearchText.getText().toString().replaceAll(" ","%20") + "&sort=0");
                        ((TextView) v).setText("الاقدم");
                        newest = true;
                    }
                }
            }
        });

        return v;
    }


    /**
     * @param param action whether be category id or sub category id
     * @param type  1 = category_id
     *              2 = sub category id
     */
    private void GetData(int type, String param,String userId) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("جاري جلب البيانات");
        progressDialog.show();
        presenter.GetAllProducts(type, param, new AllProductsCallBack() {
            @Override
            public void OnSuccess(ArrayList<Product> products) {
                if (productsGlobal != null){
                    productsGlobal.clear();
                    adapter.notifyDataSetChanged();
                }
                if (mUsers != null) {
                    mUsers.clear();
                    mUserSearchAdapter.notifyDataSetChanged();
                }
                progressDialog.dismiss();
                SetData(products);
            }

            @Override
            public void OnFailure(String message) {
                progressDialog.dismiss();
                if (productsGlobal != null){
                    productsGlobal.clear();
                    adapter.notifyDataSetChanged();
                }
                if (mUsers != null) {
                    mUsers.clear();
                    mUserSearchAdapter.notifyDataSetChanged();
                }
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
                if (productsGlobal != null){
                    productsGlobal.clear();
                    adapter.notifyDataSetChanged();
                }
                if (mUsers != null) {
                    mUsers.clear();
                    mUserSearchAdapter.notifyDataSetChanged();
                }
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


    private void SetData(final ArrayList<Product> products) {
        if (products.size() != 0 && products != null){
            mFilterParent.setVisibility(View.VISIBLE);
        } else {
            mFilterParent.setVisibility(View.GONE);
        }
        productsGlobal = products;
        adapter = new CategoryAdsAdapter(productsGlobal, getActivity(),10);
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
                for (int i = 0; i < products.size(); i++) {
                    Product product = products.get(i);
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
            gps.showSettingsAlert();
        }
        mProducts.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mProducts.setAdapter(adapter);
    }


    @Override
    public void onStop() {
        super.onStop();
        mSearchUsersConnector.cancelAllRequests("SearchFragment");
        searchProducts.cancelAllRequests("SearchFragment");
    }
}
