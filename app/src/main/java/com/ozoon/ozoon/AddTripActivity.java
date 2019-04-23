package com.ozoon.ozoon;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTripActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener {

    @BindView(R.id.back_btn)
    ImageView mBackButton;
    @BindView(R.id.from)
    AutoCompleteTextView mFrom;
    @BindView(R.id.to)
    AutoCompleteTextView mTo;
    @BindView(R.id.description)
    EditText mDescriptionEditText;
    @BindView(R.id.save)
    Button mSaveButton;
    @BindView(R.id.carType)
    EditText mCarTypeEditText;
    @BindView(R.id.time)
    EditText mTimeEdiText;
    @BindView(R.id.price)
    EditText mPriceEditText;
    @BindView(R.id.date)
    EditText mDateEditText;
    @BindView(R.id.car_type)
    Spinner mCarType;

    Connector mConnector;
    ProgressDialog mProgressDialog;
    User user;
    StorageUtil storageUtil;

    GPSTracker mGpsTracker;

    String mCarTypeText = "";
    String mFromText = "";
    String mToText = "";

    private static final String TAG = AddTripActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
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

        mConnector = new Connector(this, new Connector.LoadCallback() {
            @Override
            public void onComplete(String tag, String response) {
                mProgressDialog.dismiss();
                if (Connector.checkStatus(response)) {
                    finish();
                } else {
                    Helper.showSnackBarMessage("خطأ من فضلك اعد المحاوله", AddTripActivity.this);
                }
            }
        }, new Connector.ErrorCallback() {
            @Override
            public void onError(VolleyError error) {
                mProgressDialog.dismiss();
                Helper.showSnackBarMessage("خطأ من فضلك اعد المحاوله", AddTripActivity.this);
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
                mFromText = (String) parent.getItemAtPosition(position);
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
                mToText = (String) parent.getItemAtPosition(position);
            }
        });

        mDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTripActivity.this, AddTripActivity.this, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        mTimeEdiText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                // Create a new instance of TimePickerDialog and return it
                new TimePickerDialog(AddTripActivity.this, AddTripActivity.this, hour, minute,
                        DateFormat.is24HourFormat(AddTripActivity.this)).show();
            }
        });

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.cars_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCarType.setAdapter(spinnerAdapter);


        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCarTypeText = mCarType.getSelectedItem().toString();
                if (mFromText.equals("") || !mFromText.equals(mFrom.getText().toString())) {
                    Helper.showSnackBarMessage("ادخل مكان الاقلاع", AddTripActivity.this);
                } else if (mToText.equals("") || !mToText.equals(mTo.getText().toString())) {
                    Helper.showSnackBarMessage("ادخل مكان الوصول", AddTripActivity.this);
                } else if (mDescriptionEditText.getText().toString().isEmpty()) {
                    Helper.showSnackBarMessage("ادخل الوصف", AddTripActivity.this);
                } else if (mCarTypeEditText.getText().toString().isEmpty()) {
                    Helper.showSnackBarMessage("ادخل نوع المركبة", AddTripActivity.this);
                } else if (mPriceEditText.getText().toString().isEmpty()) {
                    Helper.showSnackBarMessage("ادخل سعر الشحنه", AddTripActivity.this);
                } else if (mTimeEdiText.getText().toString().isEmpty()) {
                    Helper.showSnackBarMessage("ادخل الوقت", AddTripActivity.this);
                } else if (mDateEditText.getText().toString().isEmpty()) {
                    Helper.showSnackBarMessage("ادخل التاريخ", AddTripActivity.this);
                } else if (mCarTypeText.equals("")){
                    Helper.showSnackBarMessage("ادخل نوع السياره", AddTripActivity.this);
                } else {
                    mProgressDialog = GMethods.show_progress_dialoug(AddTripActivity.this, "جاري التحميل", false);
                    getLocation();
                }
            }
        });

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        String hour= Integer.toString(hourOfDay) ;
        String min= Integer.toString(minute) ;

        if (hourOfDay<10)
            hour="0"+hour;
        if (minute<10)
            min="0"+min;



        mTimeEdiText.setText(hour+":"+min);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mConnector != null)
            mConnector.cancelAllRequests(TAG);
    }


    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user

        String monthString= Integer.toString(month);
        String dayString= Integer.toString(month);

        //because year added to 1900
        year-=1900;


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);


        Date date = new Date(year , month , day);


        long timeInMils = date.getTime();

        mDateEditText.setText(dateFormat.format(date));
    }


    public void getLocation(){
        mGpsTracker = new GPSTracker(this, new GPSTracker.OnGetLocation() {
            @Override
            public void onGetLocation(double longtiude, double lantitude) {
                if (longtiude != 0 && lantitude != 0) {
                    mGpsTracker.stopUsingGPS();
                    Geocoder geocoder = new Geocoder(AddTripActivity.this, Locale.forLanguageTag("ar"));
                    List<Address> toAddresses = null;
                    try {
                        toAddresses = geocoder.getFromLocation(lantitude, longtiude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                    if (toAddresses != null) {
                        String toAddress = toAddresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String toCity = toAddresses.get(0).getAdminArea();
                        mConnector.getRequest(TAG, "http://cta3.com/ozoon/api/add_trip?city_from="+Uri.encode(mFromText)+"&city_to="+Uri.encode(mToText)+"&user_id="+user.getId()+"&start_time="+mDateEditText.getText().toString()+"&start_date="+mDateEditText.getText().toString()+"&address_from="+Uri.encode(toAddress)+"&price="+mPriceEditText.getText().toString()+"&car_type="+Uri.encode(mCarTypeEditText.getText().toString())+"&car_name="+Uri.encode(mCarTypeEditText.getText().toString()));
                    }
                }
            }
        });

        if (mGpsTracker.canGetLocation()){
            Location location = mGpsTracker.getLocation();
            if (location != null)
                if (location.getLongitude() != 0 && location.getLatitude() != 0) {
                    mGpsTracker.stopUsingGPS();
                    Geocoder geocoder = new Geocoder(AddTripActivity.this, Locale.forLanguageTag("ar"));
                    List<Address> toAddresses = null;
                    try {
                        toAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                    if (toAddresses != null) {
                        String toAddress = toAddresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String toCity = toAddresses.get(0).getAdminArea();
                        mConnector.getRequest(TAG, "http://cta3.com/ozoon/api/add_trip?city_from="+Uri.encode(mFromText)+"&city_to="+Uri.encode(mToText)+"&user_id="+user.getId()+"&start_time="+mDateEditText.getText().toString()+"&start_date="+mDateEditText.getText().toString()+"&address_from="+Uri.encode(toAddress)+"&price="+mPriceEditText.getText().toString()+"&car_type="+Uri.encode(mCarTypeEditText.getText().toString())+"&car_name="+Uri.encode(mCarTypeEditText.getText().toString()));
                    }
                }
        }
    }

}
