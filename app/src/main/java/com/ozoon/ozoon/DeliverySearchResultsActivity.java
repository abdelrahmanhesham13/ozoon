package com.ozoon.ozoon;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ozoon.ozoon.Adapters.ResultItemAdapter;
import com.ozoon.ozoon.Model.Objects.ResultItemModel;
import com.ozoon.ozoon.Utils.Connector;
import com.ozoon.ozoon.Utils.GMethods;
import com.ozoon.ozoon.Utils.Helper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeliverySearchResultsActivity extends AppCompatActivity {

    @BindView(R.id.searchResults)
    RecyclerView mSearchResultsRecycler;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.back_btn)
    ImageView mBackBtn;

    ResultItemAdapter mAdapter;
    ArrayList<ResultItemModel> mResults;

    Connector mConnector;

    String mType;
    String mFromText;
    String mToText;
    String mCarType;

    ProgressDialog mProgressDialog;

    private final String TAG = "DeliverySearchResultsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_search_results);
        ButterKnife.bind(this);
        GMethods.ChangeFont(this);

        Intent intent = getIntent();
        mType = intent.getStringExtra("type");
        mFromText = intent.getStringExtra("fromCity");
        mToText = intent.getStringExtra("toCity");
        mCarType = intent.getStringExtra("carType");

        if (mType.equals("trip")){
            mToolbarTitle.setText("نتائج البحث عن الشحنات");
        } else {
            mToolbarTitle.setText("نتائج البحث عن الرحلات");
        }

        mConnector = new Connector(this, new Connector.LoadCallback() {
            @Override
            public void onComplete(String tag, String response) {
                if (Connector.checkStatus(response)){
                    mProgressDialog.dismiss();
                    if (mType.equals("trip")){
                        mResults.addAll(Connector.getItems(response));
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mResults.addAll(Connector.getTrips(response));
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    mProgressDialog.dismiss();
                    Helper.showSnackBarMessage("لا توجد نتائج",DeliverySearchResultsActivity.this);
                }
            }
        }, new Connector.ErrorCallback() {
            @Override
            public void onError(VolleyError error) {
                mProgressDialog.dismiss();
                Helper.showSnackBarMessage("خطأ من فضلك اعد المحاوله",DeliverySearchResultsActivity.this);
            }
        });

        mResults = new ArrayList<>();

        mAdapter = new ResultItemAdapter(this, mResults, new ResultItemAdapter.OnItemClick() {
            @Override
            public void setOnItemClick(int position) {
                showDeliveryDetails(mResults.get(position).getId());
            }
        });


        mSearchResultsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mSearchResultsRecycler.setHasFixedSize(true);
        mSearchResultsRecycler.setAdapter(mAdapter);

        mProgressDialog = GMethods.show_progress_dialoug(this,"جاري التحميل",false);
        if (mType.equals("trip")){
            mConnector.getRequest(TAG,"http://cta3.com/ozoon/api/get_items?city_from="+Uri.encode(mFromText)+"&city_to="+ Uri.encode(mToText));
        } else {
            mConnector.getRequest(TAG,"http://cta3.com/ozoon/api/get_trips?city_from="+Uri.encode(mFromText)+"&city_to="+ Uri.encode(mToText));
        }

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    public void showDeliveryDetails(String position) {



        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DeliveryDetailsDialogFragment newFragment = new DeliveryDetailsDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id",position);
        bundle.putString("type",mType);
        newFragment.setArguments(bundle);

        newFragment.show(this.getSupportFragmentManager(), "");

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mConnector != null)
            mConnector.cancelAllRequests(TAG);
    }
}
