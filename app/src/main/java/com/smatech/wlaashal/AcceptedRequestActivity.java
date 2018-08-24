package com.smatech.wlaashal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.smatech.wlaashal.CallBacks.NotificationDetailCallBack;
import com.smatech.wlaashal.Model.Objects.ChatModel;
import com.smatech.wlaashal.Model.Responses.NotificationDetailResponse;
import com.smatech.wlaashal.Presenters.MainPresenter;
import com.smatech.wlaashal.UI.Activities.HomeActivity;
import com.smatech.wlaashal.Utils.Connector;
import com.smatech.wlaashal.Utils.GMethods;
import com.smatech.wlaashal.Utils.Helper;
import com.smatech.wlaashal.Utils.StorageUtil;

public class AcceptedRequestActivity extends AppCompatActivity {


    Intent mIntent;
    String mType,mId;
    String mClass = "notMine";
    MainPresenter mPresenter;
    TextView mClientName;//mClientCar,mClientAddress;
    ImageView backBtn;
    View mPickupLocationFirst,mPickupLocationSecond;
    StorageUtil util;
    Button mProfileButton;

    Connector mConnectorSendMessage;
    ChatModel mChatModel;

    NotificationDetailResponse responseRequest;

    private static final String TAG = RequestDetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_request);

        GMethods.ChangeFont(this);
        util = StorageUtil.getInstance().doStuff(this);

        mIntent = getIntent();
        mPresenter = new MainPresenter();

        initView();



        if (mIntent.hasExtra("type") && mIntent.hasExtra("id")){
            mType = mIntent.getStringExtra("type");
            mId = mIntent.getStringExtra("id");
        }

        if (mIntent.hasExtra("class")){
            mClass = mIntent.getStringExtra("class");
        }

        if (mType.equals("delivery")){
//            mPickupLocationFirst.setVisibility(View.GONE);
//            mPickupLocationSecond.setVisibility(View.VISIBLE);
            getDataDelivery();
        } else {
            getData();
        }
    }

    private void initView() {

        //mClientAddress = findViewById(R.id.client_address);
        //mClientCar = findViewById(R.id.client_car);
        mClientName = findViewById(R.id.client_name);
        mProfileButton = findViewById(R.id.accept_button);
        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    public void getData(){
        mPresenter.getNotificationDetail(mId, new NotificationDetailCallBack() {
            @Override
            public void OnSuccess(NotificationDetailResponse NotificationDetailResponse) {
                setData(NotificationDetailResponse);
            }

            @Override
            public void OnFailure(String message) {

            }

            @Override
            public void OnServerError() {

            }
        });
    }



    public void getDataDelivery(){
        mPresenter.getDeliverDetail(mId, new NotificationDetailCallBack() {
            @Override
            public void OnSuccess(NotificationDetailResponse NotificationDetailResponse) {
                setData(NotificationDetailResponse);
            }

            @Override
            public void OnFailure(String message) {

            }

            @Override
            public void OnServerError() {

            }
        });
    }


    public void setData(final NotificationDetailResponse data){
        if (mType.equals("delivery")){
            mClientName.setText(data.getDelivery().getName());
            mProfileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(AcceptedRequestActivity.this, ProfileActivity.class).putExtra("user_id",data.getDelivery().getId()));
                }
            });
        } else {
            mClientName.setText(data.getTaxi().getName());
            mProfileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(AcceptedRequestActivity.this, ProfileActivity.class).putExtra("user_id",data.getTaxi().getId()));
                }
            });
        }

    }

}
