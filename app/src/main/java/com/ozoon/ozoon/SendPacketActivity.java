package com.ozoon.ozoon;

import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.ozoon.ozoon.Model.Objects.User;
import com.ozoon.ozoon.Utils.Connector;
import com.ozoon.ozoon.Utils.GMethods;
import com.ozoon.ozoon.Utils.GPSTracker;
import com.ozoon.ozoon.Utils.Helper;
import com.ozoon.ozoon.Utils.StorageUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SendPacketActivity extends AppCompatActivity {

    @BindView(R.id.back_btn)
    ImageView mBackButton;
    @BindView(R.id.from)
    AutoCompleteTextView mFrom;
    @BindView(R.id.to)
    AutoCompleteTextView mTo;
    @BindView(R.id.description)
    EditText mDescriptionEditText;
    @BindView(R.id.documents)
    CheckBox mDocuments;
    @BindView(R.id.receiver)
    CheckBox mReceiver;
    @BindView(R.id.place)
    EditText mPlaceEditText;
    @BindView(R.id.name)
    EditText mNameEditText;
    @BindView(R.id.mobile)
    EditText mMobileEditText;
    @BindView(R.id.save)
    Button mSaveButton;


    String mFromText = "";
    String mToText = "";
    String mDescription = "";
    String mPlace = "";
    String mName = "";
    String mMobile = "";

    Connector mConnector;
    ProgressDialog mProgressDialog;
    User user;
    StorageUtil storageUtil;

    GPSTracker mGpsTracker;

    private static final String TAG = SendPacketActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_packet);
        ButterKnife.bind(this);
        GMethods.ChangeFont(this);
        storageUtil = StorageUtil.getInstance().doStuff(this);
        user = storageUtil.GetCurrentUser();

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mReceiver.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mNameEditText.setVisibility(View.GONE);
                    mPlaceEditText.setVisibility(View.GONE);
                    mMobileEditText.setVisibility(View.GONE);
                } else {
                    mNameEditText.setVisibility(View.VISIBLE);
                    mPlaceEditText.setVisibility(View.VISIBLE);
                    mMobileEditText.setVisibility(View.VISIBLE);
                }
            }
        });

        mConnector = new Connector(this, new Connector.LoadCallback() {
            @Override
            public void onComplete(String tag, String response) {
                mProgressDialog.dismiss();
                if (Connector.checkStatus(response)){
                    finish();
                } else {
                    Helper.showSnackBarMessage("خطأ من فضلك اعد المحاوله", SendPacketActivity.this);
                }
            }
        }, new Connector.ErrorCallback() {
            @Override
            public void onError(VolleyError error) {
                mProgressDialog.dismiss();
                Helper.showSnackBarMessage("خطأ من فضلك اعد المحاوله", SendPacketActivity.this);
            }
        });

        final ArrayList<String> searchArrayList = new ArrayList<>();

        String jsonString = "{\"cities\":[{\"id\":\"1\",\"name\":\"\\u062c\\u062f\\u0647\"},{\"id\":\"2\",\"name\":\"\\u0627\\u0644\\u0631\\u064a\\u0627\\u0636\"},{\"id\":\"4\",\"name\":\"\\u0627\\u0644\\u062e\\u0628\\u0631\"},{\"id\":\"5\",\"name\":\"\\u0645\\u0643\\u0647\"},{\"id\":\"6\",\"name\":\"\\u0627\\u0644\\u0645\\u062f\\u064a\\u0646\\u0629 \\u0627\\u0644\\u0645\\u0646\\u0648\\u0631\\u0629\"},{\"id\":\"7\",\"name\":\"\\u062a\\u0628\\u0648\\u0643\"},{\"id\":\"8\",\"name\":\"\\u064a\\u0646\\u0628\\u0639\"},{\"id\":\"9\",\"name\":\"\\u0631\\u0627\\u0628\\u063a\"},{\"id\":\"10\",\"name\":\"\\u0627\\u0644\\u062f\\u0645\\u0627\\u0645\"},{\"id\":\"11\",\"name\":\"\\u0627\\u0644\\u0638\\u0647\\u0631\\u0627\\u0646\"},{\"id\":\"12\",\"name\":\"\\u062d\\u0627\\u0626\\u0644\"},{\"id\":\"13\",\"name\":\"\\u0627\\u0644\\u0637\\u0627\\u0626\\u0641\"},{\"id\":\"14\",\"name\":\"\\u0623\\u0628\\u0647\\u0627\"},{\"id\":\"15\",\"name\":\"\\u0627\\u0644\\u0642\\u0635\\u064a\\u0645\"},{\"id\":\"16\",\"name\":\"\\u062d\\u0641\\u0631 \\u0627\\u0644\\u0628\\u0627\\u0637\\u0646\"},{\"id\":\"17\",\"name\":\"\\u0627\\u0644\\u0628\\u0627\\u062d\\u0629\"},{\"id\":\"18\",\"name\":\"\\u0646\\u062c\\u0631\\u0627\\u0646\"},{\"id\":\"19\",\"name\":\"\\u062c\\u064a\\u0632\\u0627\\u0646\"},{\"id\":\"20\",\"name\":\"\\u0639\\u0631\\u0639\\u0631\"},{\"id\":\"21\",\"name\":\"\\u0627\\u0644\\u062c\\u0648\\u0641\"},{\"id\":\"22\",\"name\":\"\\u0627\\u0644\\u0644\\u064a\\u062b\"},{\"id\":\"23\",\"name\":\"\\u0627\\u0644\\u0642\\u0646\\u0641\\u0630\\u0629\"},{\"id\":\"24\",\"name\":\"\\u0634\\u0631\\u0648\\u0631\\u0629\"},{\"id\":\"25\",\"name\":\"\\u0648\\u0627\\u062f\\u064a \\u0627\\u0644\\u062f\\u0648\\u0627\\u0633\\u0631\"},{\"id\":\"27\",\"name\":\"\\u0627\\u0644\\u0625\\u062d\\u0633\\u0627\\u0621\"},{\"id\":\"28\",\"name\":\"\\u062e\\u0645\\u064a\\u0633 \\u0645\\u0634\\u064a\\u0637\"},{\"id\":\"29\",\"name\":\"\\u0627\\u0644\\u0632\\u0644\\u0641\\u064a\"},{\"id\":\"30\",\"name\":\"\\u0627\\u0644\\u0631\\u0633\"},{\"id\":\"31\",\"name\":\"\\u0628\\u064a\\u0634\\u0647\"},{\"id\":\"32\",\"name\":\"\\u0627\\u0644\\u062f\\u0648\\u0627\\u062f\\u0645\\u064a\"},{\"id\":\"33\",\"name\":\"\\u0635\\u0628\\u064a\\u0627\"},{\"id\":\"34\",\"name\":\"\\u0628\\u064a\\u0634\"},{\"id\":\"35\",\"name\":\"\\u0627\\u0644\\u0623\\u0641\\u0644\\u0627\\u062c\"},{\"id\":\"36\",\"name\":\"\\u0627\\u0644\\u0642\\u0631\\u064a\\u0627\\u062a\"}],\"status\":true}";
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray cities = jsonObject.getJSONArray("cities");
            for (int i = 0;i<cities.length();i++) {
                JSONObject city = cities.getJSONObject(i);
                String name = city.getString("name");
                searchArrayList.add(name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this, android.R.layout.select_dialog_item, searchArrayList);

        mFrom.setAdapter(adapter);
        mTo.setAdapter(adapter);

        mFrom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mFromText = (String)parent.getItemAtPosition(position);
            }
        });

        mFrom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mFrom.showDropDown();
                return false;
            }
        });

        mTo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mTo.showDropDown();
                return false;
            }
        });

        mTo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mToText = (String)parent.getItemAtPosition(position);
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFromText.equals("") || !mFromText.equals(mFrom.getText().toString())) {
                    Helper.showSnackBarMessage("ادخل مكان الاقلاع", SendPacketActivity.this);
                } else if (mToText.equals("") || !mToText.equals(mTo.getText().toString())) {
                    Helper.showSnackBarMessage("ادخل مكان الوصول", SendPacketActivity.this);
                } else if (mDescriptionEditText.getText().toString().isEmpty()){
                    Helper.showSnackBarMessage("ادخل الوصف", SendPacketActivity.this);
                } else if (!mReceiver.isChecked() && mPlaceEditText.getText().toString().isEmpty()){
                    Helper.showSnackBarMessage("ادخل عنوان المستلم", SendPacketActivity.this);
                } else if (!mReceiver.isChecked() && mMobileEditText.getText().toString().isEmpty()){
                    Helper.showSnackBarMessage("ادخل رقم المستلم", SendPacketActivity.this);
                } else if (!mReceiver.isChecked() && mNameEditText.getText().toString().isEmpty()){
                    Helper.showSnackBarMessage("ادخل اسم المستلم", SendPacketActivity.this);
                } else {
                    mProgressDialog = GMethods.show_progress_dialoug(SendPacketActivity.this,"جاري التحميل",false);
                    if (mReceiver.isChecked()){
                        getLocation();
                    } else {
                        mConnector.getRequest(TAG,"http://cta3.com/ozoon/api/add_item?city_from="+Uri.encode(mFromText)+"&city_to="+Uri.encode(mToText)+"&user_id="+user.getId()+"&description="+Uri.encode(mDescriptionEditText.getText().toString())+"&documents="+String.valueOf(mDocuments.isChecked() ? 1:0)+"&name="+Uri.encode(mNameEditText.getText().toString())+"&mobile="+mMobileEditText.getText().toString()+"&address="+ Uri.encode(mPlaceEditText.getText().toString()));
                    }
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mConnector != null)
            mConnector.cancelAllRequests(TAG);
    }


    public void getLocation(){
        mGpsTracker = new GPSTracker(this, new GPSTracker.OnGetLocation() {
            @Override
            public void onGetLocation(double longtiude, double lantitude) {
                if (longtiude != 0 && lantitude != 0) {
                    mGpsTracker.stopUsingGPS();
                    Geocoder geocoder = new Geocoder(SendPacketActivity.this, Locale.forLanguageTag("ar"));
                    List<Address> toAddresses = null;
                    try {
                        toAddresses = geocoder.getFromLocation(lantitude, longtiude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                    if (toAddresses != null) {
                        String toAddress = toAddresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String toCity = toAddresses.get(0).getAdminArea();
                        mConnector.getRequest(TAG,"http://cta3.com/ozoon/api/add_item?city_from="+Uri.encode(mFromText)+"&city_to="+Uri.encode(mToText)+"&user_id="+user.getId()+"&description="+Uri.encode(mDescriptionEditText.getText().toString())+"&documents="+String.valueOf(mDocuments.isChecked() ? 1:0)+"&name="+Uri.encode(user.getName())+"&mobile="+user.getMobile()+"&address="+toAddress);
                    }
                }
            }
        });

        if (mGpsTracker.canGetLocation()){
            Location location = mGpsTracker.getLocation();
            if (location != null)
                if (location.getLongitude() != 0 && location.getLatitude() != 0) {
                    mGpsTracker.stopUsingGPS();
                    Geocoder geocoder = new Geocoder(SendPacketActivity.this, Locale.forLanguageTag("ar"));
                    List<Address> toAddresses = null;
                    try {
                        toAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                    if (toAddresses != null) {
                        String toAddress = toAddresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String toCity = toAddresses.get(0).getAdminArea();
                        mConnector.getRequest(TAG,"http://cta3.com/ozoon/api/add_item?city_from="+Uri.encode(mFromText)+"&city_to="+Uri.encode(mToText)+"&user_id="+user.getId()+"&description="+Uri.encode(mDescriptionEditText.getText().toString())+"&documents="+String.valueOf(mDocuments.isChecked() ? 1:0)+"&name="+Uri.encode(user.getName())+"&mobile="+user.getMobile()+"&address="+Uri.encode(toAddress));
                    }
                }
        }
    }

}
