package com.ozoon.ozoon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ozoon.ozoon.Utils.GMethods;
import com.ozoon.ozoon.Utils.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchForDeliveryActivity extends AppCompatActivity {

    @BindView(R.id.from)
    AutoCompleteTextView mFrom;
    @BindView(R.id.to)
    AutoCompleteTextView mTo;
    @BindView(R.id.car_type)
    Spinner mCarType;
    @BindView(R.id.search)
    Button mSearch;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.back_btn)
    ImageView mBackBtn;

    String mType;
    String mFromText = "";
    String mToText = "";
    String mCarTypeText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_delivery);
        ButterKnife.bind(this);
        GMethods.ChangeFont(this);

        Intent intent = getIntent();
        if (intent.hasExtra("type")){
            mType = intent.getStringExtra("type");
        }

        if (mType.equals("trip")){
            mToolbarTitle.setText("البحث عن الشحنات");
        } else {
            mToolbarTitle.setText("البحث عن الرحلات");
        }

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

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.cars_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCarType.setAdapter(spinnerAdapter);

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCarTypeText = mCarType.getSelectedItem().toString();
                Helper.writeToLog(mFromText + " " + mToText + " " + mCarTypeText);
                if (mFromText.equals("") || !mFromText.equals(mFrom.getText().toString())) {
                    Helper.showSnackBarMessage("ادخل مكان الاقلاع", SearchForDeliveryActivity.this);
                } else if (mToText.equals("") || !mToText.equals(mTo.getText().toString())) {
                    Helper.showSnackBarMessage("ادخل مكان الوصول", SearchForDeliveryActivity.this);
                } else if (mCarTypeText.equals("")){
                    Helper.showSnackBarMessage("ادخل نوع السياره", SearchForDeliveryActivity.this);
                } else {
                    startActivity(new Intent(SearchForDeliveryActivity.this, DeliverySearchResultsActivity.class).putExtra("type", mType).putExtra("fromCity",mFromText).putExtra("toCity",mToText).putExtra("carType",mCarTypeText));
                }
            }
        });

    }
}
