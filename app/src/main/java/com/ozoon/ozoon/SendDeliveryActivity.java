package com.ozoon.ozoon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ozoon.ozoon.Utils.GMethods;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SendDeliveryActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.trip_searching)
    Button mTripSearching;
    @BindView(R.id.send_packet)
    Button mSendPacket;
    @BindView(R.id.back_btn)
    ImageView mBackButton;


    String mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_delivery);
        ButterKnife.bind(this);
        GMethods.ChangeFont(this);
        Intent intent = getIntent();
        if (intent.hasExtra("type")){
            mType = intent.getStringExtra("type");
            if (mType.equals("packet")){
                mToolbarTitle.setText("ارسال شحنة");
                mTripSearching.setText("البحث عن الرحلات المتوفرة");
                mSendPacket.setText("ارسال شحنة");
            } else {
                mToolbarTitle.setText("انشاء رحلة");
                mTripSearching.setText("البحث عن الشحنات القريبة");
                mSendPacket.setText("اضافة رحلة");
            }
        }

        mTripSearching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SendDeliveryActivity.this,SearchForDeliveryActivity.class).putExtra("type",mType));
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSendPacket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mType.equals("packet")){
                    startActivity(new Intent(SendDeliveryActivity.this,SendPacketActivity.class));
                } else {
                    startActivity(new Intent(SendDeliveryActivity.this,AddTripActivity.class));
                }
            }
        });

    }
}
