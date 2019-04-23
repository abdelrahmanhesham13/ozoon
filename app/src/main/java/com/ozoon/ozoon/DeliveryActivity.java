package com.ozoon.ozoon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ozoon.ozoon.Adapters.ResultItemAdapter;
import com.ozoon.ozoon.Model.Objects.ResultItemModel;
import com.ozoon.ozoon.Model.Objects.User;
import com.ozoon.ozoon.Utils.Connector;
import com.ozoon.ozoon.Utils.GMethods;
import com.ozoon.ozoon.Utils.GPSTracker;
import com.ozoon.ozoon.Utils.Helper;
import com.ozoon.ozoon.Utils.StorageUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeliveryActivity extends AppCompatActivity {

    @BindView(R.id.back_btn)
    ImageView mBackButton;
    @BindView(R.id.send_packet)
    Button mSendPacketButton;
    @BindView(R.id.user_name)
    TextView mUsername;
    @BindView(R.id.rating_bar)
    RatingBar mRatingBar;
    @BindView(R.id.phone)
    TextView mPhone;
    @BindView(R.id.location)
    TextView mLocation;
    @BindView(R.id.trips)
    TextView mTrips;
    @BindView(R.id.packets)
    TextView mPackets;
    @BindView(R.id.create_trip)
    Button mCreateTrip;
    @BindView(R.id.results)
    RecyclerView mResultsRecycler;
    @BindView(R.id.selector)
    View mSelector;

    ResultItemAdapter mAdapter;

    ArrayList<ResultItemModel> mResults;

    StorageUtil util;
    User mUser;

    Connector mTripsConnector;
    Connector mItemsConnector;

    ProgressDialog mProgressDialog;

    GPSTracker mGpsTracker;

    int mChosen = 1;

    private final String TAG = "DeliveryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        ButterKnife.bind(this);
        GMethods.ChangeFont(this);
        util = StorageUtil.getInstance().doStuff(this);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mGpsTracker = new GPSTracker(this, new GPSTracker.OnGetLocation() {
            @Override
            public void onGetLocation(double longtiude, double lantitude) {
                if (longtiude != 0 && lantitude != 0) {
                    Geocoder geocoder = new Geocoder(DeliveryActivity.this, Locale.forLanguageTag("ar"));
                    List<Address> toAddresses = null;
                    try {
                        toAddresses = geocoder.getFromLocation(lantitude, longtiude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                    if (toAddresses != null) {
                        String toAddress = toAddresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String toCity = toAddresses.get(0).getAdminArea();
                        mLocation.setText(toAddress);
                    }
                    mGpsTracker.stopUsingGPS();
                }
            }
        });

        if (mGpsTracker.canGetLocation()) {
            Location location = mGpsTracker.getLocation();
            if (location != null)
                if (location.getLongitude() != 0 && location.getLatitude() != 0) {
                    Geocoder geocoder = new Geocoder(DeliveryActivity.this, Locale.forLanguageTag("ar"));
                    List<Address> toAddresses = null;
                    try {
                        toAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                    if (toAddresses != null) {
                        String toAddress = toAddresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String toCity = toAddresses.get(0).getAdminArea();
                        mLocation.setText(toAddress);
                    }
                    mGpsTracker.stopUsingGPS();
                }
        }

        mTripsConnector = new Connector(this, new Connector.LoadCallback() {
            @Override
            public void onComplete(String tag, String response) {
                if (Connector.checkStatus(response)) {
                    mProgressDialog.dismiss();
                    mResults.clear();
                    mResults.addAll(Connector.getTrips(response));
                    mAdapter.notifyDataSetChanged();
                } else {
                    mProgressDialog.dismiss();
                    Helper.showSnackBarMessage("لا يوجد رحلات", DeliveryActivity.this);
                }
            }
        }, new Connector.ErrorCallback() {
            @Override
            public void onError(VolleyError error) {
                mProgressDialog.dismiss();
                Helper.showSnackBarMessage("خطأ من فضلك اعد المحاوله", DeliveryActivity.this);
            }
        });

        mItemsConnector = new Connector(this, new Connector.LoadCallback() {
            @Override
            public void onComplete(String tag, String response) {
                if (Connector.checkStatus(response)) {
                    mProgressDialog.dismiss();
                    mResults.clear();
                    mResults.addAll(Connector.getItems(response));
                    mAdapter.notifyDataSetChanged();
                } else {
                    mProgressDialog.dismiss();
                    Helper.showSnackBarMessage("لا يوجد شحنات", DeliveryActivity.this);
                }
            }
        }, new Connector.ErrorCallback() {
            @Override
            public void onError(VolleyError error) {
                mProgressDialog.dismiss();
                Helper.showSnackBarMessage("لا يوجد شحنات", DeliveryActivity.this);
            }
        });

        mSendPacketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeliveryActivity.this, SendDeliveryActivity.class).putExtra("type", "packet"));
            }
        });

        mCreateTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeliveryActivity.this, SendDeliveryActivity.class).putExtra("type", "trip"));
            }
        });

        if (util.IsLogged()) {
            mUser = util.GetCurrentUser();
            mUsername.setText(mUser.getName());
            mPhone.setText(mUser.getMobile());
            mRatingBar.setRating(Float.parseFloat(mUser.getRate()));
        }

        mTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChosen = 1;
                mTrips.setTextColor(Color.parseColor("#32B8DB"));
                mPackets.setTextColor(Color.parseColor("#696969"));
                v.animate().scaleX(1.1f).scaleY(1.1f).setDuration(100).start();
                mPackets.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                Drawable mDrawable = ContextCompat.getDrawable(DeliveryActivity.this, R.drawable.ic_directions_car_black_24dp_24);
                if (mDrawable != null) {
                    mDrawable.setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP));
                }
                Drawable mDrawable2 = ContextCompat.getDrawable(DeliveryActivity.this, R.drawable.ic_card_travel_black_24dp);
                if (mDrawable2 != null) {
                    mDrawable2.setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.Color_DimGray), PorterDuff.Mode.SRC_ATOP));
                }
                mTrips.setCompoundDrawablesWithIntrinsicBounds(null, mDrawable, null, null);
                mPackets.setCompoundDrawablesWithIntrinsicBounds(null, mDrawable2, null, null);
                mProgressDialog.show();
                mItemsConnector.cancelAllRequests(TAG);
                mTripsConnector.getRequest(TAG, "http://cta3.com/ozoon/api/get_trips?user_id=" + mUser.getId());
            }
        });


        mPackets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChosen = 2;
                mTrips.setTextColor(Color.parseColor("#696969"));
                mPackets.setTextColor(Color.parseColor("#32B8DB"));
                v.animate().scaleX(1.1f).scaleY(1.1f).setDuration(100).start();
                mTrips.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                Drawable mDrawable = ContextCompat.getDrawable(DeliveryActivity.this, R.drawable.ic_directions_car_black_24dp_24);
                if (mDrawable != null) {
                    mDrawable.setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.Color_DimGray), PorterDuff.Mode.SRC_ATOP));
                }
                Drawable mDrawable2 = ContextCompat.getDrawable(DeliveryActivity.this, R.drawable.ic_card_travel_black_24dp);
                if (mDrawable2 != null) {
                    mDrawable2.setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP));
                }
                mTrips.setCompoundDrawablesWithIntrinsicBounds(null, mDrawable, null, null);
                mPackets.setCompoundDrawablesWithIntrinsicBounds(null, mDrawable2, null, null);
                mProgressDialog.show();
                mTripsConnector.cancelAllRequests(TAG);
                mItemsConnector.getRequest(TAG, "http://cta3.com/ozoon/api/get_items?user_id=" + mUser.getId());
            }
        });

        mResults = new ArrayList<>();
        mAdapter = new ResultItemAdapter(this, mResults, new ResultItemAdapter.OnItemClick() {
            @Override
            public void setOnItemClick(int position) {
            }
        });


        mResultsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mResultsRecycler.setHasFixedSize(true);
        mResultsRecycler.setAdapter(mAdapter);

        mTrips.animate().scaleX(1.1f).scaleY(1.1f).setDuration(100).start();
        mProgressDialog = GMethods.show_progress_dialoug(this, "جاري التحميل", false);
        mItemsConnector.cancelAllRequests(TAG);
        mTripsConnector.getRequest(TAG, "http://cta3.com/ozoon/api/get_trips?user_id=" + mUser.getId());


    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mTripsConnector != null)
            mTripsConnector.cancelAllRequests(TAG);
        if (mItemsConnector != null)
            mItemsConnector.cancelAllRequests(TAG);
    }
}
