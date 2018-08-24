package com.smatech.wlaashal;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.smatech.wlaashal.CallBacks.NotificationDetailCallBack;
import com.smatech.wlaashal.CallBacks.SuccessCallBack;
import com.smatech.wlaashal.Model.Objects.ChatModel;
import com.smatech.wlaashal.Model.Responses.NotificationDetailResponse;
import com.smatech.wlaashal.Presenters.MainPresenter;
import com.smatech.wlaashal.UI.Activities.AdvertDetails;
import com.smatech.wlaashal.UI.Activities.HomeActivity;
import com.smatech.wlaashal.Utils.Connector;
import com.smatech.wlaashal.Utils.GMethods;
import com.smatech.wlaashal.Utils.Helper;
import com.smatech.wlaashal.Utils.StorageUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequestDetailsActivity extends AppCompatActivity {

    @BindView(R.id.communication_1)
    View v1;
    @BindView(R.id.communication_2)
    View v2;
    Intent mIntent;
    String mType,mId;
    String mClass = "notMine";
    MainPresenter mPresenter;
    ImageView mPickupLocation,mDestinationLocation,mChatButton,mMobileButton,mBackButton,mChatButtonMenu,mProfileButton;
    TextView mRequestPrice,mClientName,mPickupLocationText,mDestinationLocationText;
    Button mAcceptButton,mRejectButton;
    View mPickupLocationFirst,mPickupLocationSecond;
    StorageUtil util;

    Connector mConnectorSendMessage;
    ChatModel mChatModel;

    NotificationDetailResponse responseRequest;

    private static final String TAG = RequestDetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);
        ButterKnife.bind(this);
        GMethods.ChangeFont(this);
        util = StorageUtil.getInstance().doStuff(this);

        mIntent = getIntent();
        mPresenter = new MainPresenter();

        initView();


        if (mIntent.hasExtra("type") && mIntent.hasExtra("id")){
            mType = mIntent.getStringExtra("type");
            mId = mIntent.getStringExtra("id");
            if (mIntent.hasExtra("mine")){
                mAcceptButton.setVisibility(View.GONE);
            }
        }

        if (mIntent.hasExtra("class")){
            mClass = mIntent.getStringExtra("class");
        }

        if (mType.equals("delivery")){
            mPickupLocationFirst.setVisibility(View.GONE);
            mPickupLocationSecond.setVisibility(View.VISIBLE);
            getDataDelivery();
        } else {
            getData();
        }

        if (mIntent.hasExtra("hideCommunication")){
            v1.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
        }


        mConnectorSendMessage = new Connector(RequestDetailsActivity.this, new Connector.LoadCallback() {
            @Override
            public void onComplete(String tag, String response) {
                if (Connector.checkStatus(response)) {
                    mChatModel = Connector.getChatModelJson(response,responseRequest.getUser().getName(),responseRequest.getUser().getId(),util.GetCurrentUser().getId());
                    //Intent returnIntent = new Intent();
                    //returnIntent.putExtra("chat",mChatModel);

                    startActivity(new Intent(RequestDetailsActivity.this,HomeActivity.class).putExtra("chat",mChatModel));
                    //setResult(Activity.RESULT_OK,returnIntent);
                    //finish();
                } else {
                    Helper.showSnackBarMessage("خطأ من فضلك اعد المحاوله", RequestDetailsActivity.this);
                }
            }
        }, new Connector.ErrorCallback() {
            @Override
            public void onError(VolleyError error) {
                Helper.showSnackBarMessage("خطأ من فضلك اعد المحاوله", RequestDetailsActivity.this);
            }
        });


    }


    public void initView(){
        mPickupLocation = findViewById(R.id.pickup_location);
        mDestinationLocation = findViewById(R.id.destination_location);
        mRequestPrice = findViewById(R.id.request_price);
        mClientName = findViewById(R.id.client_name);
        mChatButton = findViewById(R.id.chat_button);
        mMobileButton = findViewById(R.id.mobile_button);
        mAcceptButton = findViewById(R.id.accept_button);
        mProfileButton = findViewById(R.id.profile_button);
        //mRejectButton = findViewById(R.id.reject_button);
        mPickupLocationFirst = findViewById(R.id.pickup_location_parent_1);
        mPickupLocationSecond = findViewById(R.id.pickup_location_parent_2);
        mPickupLocationText = findViewById(R.id.pickup_location_text);
        mDestinationLocationText = findViewById(R.id.destination_location_text);
        mBackButton = findViewById(R.id.back_btn);
        mChatButtonMenu = findViewById(R.id.chat_btn);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mChatButtonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RequestDetailsActivity.this,HomeActivity.class).putExtra("goToChat",true));
                finish();
            }
        });



    }

    public void getData(){

        mPresenter.getNotificationDetail(mId, new NotificationDetailCallBack() {
            @Override
            public void OnSuccess(NotificationDetailResponse NotificationDetailResponse) {
                setData(NotificationDetailResponse);
                Helper.writeToLog(NotificationDetailResponse.getRequest().getId());
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



    public void setData(final NotificationDetailResponse notificationDetailResponse)
    {

        responseRequest = notificationDetailResponse;
        mClientName.setText(notificationDetailResponse.getUser().getName());
        mRequestPrice.setText(notificationDetailResponse.getRequest().getPrice() + " ريال");


        if (mType.equals("delivery")) {
            //mDestinationLocationText.setText(notificationDetailResponse.getRequest().getAddress());
            ((TextView)mPickupLocationSecond.findViewById(R.id.pickup_location_text)).setText(notificationDetailResponse.getRequest().getTitle());
            (mPickupLocationSecond.findViewById(R.id.pickup_location)).setVisibility(View.GONE);

        } else {
            //mDestinationLocationText.setText(notificationDetailResponse.getRequest().getAddress_to());
            //mPickupLocationText.setText(notificationDetailResponse.getRequest().getAddress_from());
        }

        mDestinationLocationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GMethods.OpenIntentToGoogleMapsDirection(RequestDetailsActivity.this,notificationDetailResponse.getRequest().getLat_to(),notificationDetailResponse.getRequest().getLong_to());
            }
        });

        mPickupLocationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GMethods.OpenIntentToGoogleMapsDirection(RequestDetailsActivity.this,notificationDetailResponse.getRequest().getLat_from(),notificationDetailResponse.getRequest().getLong_from());
            }
        });

        mPickupLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GMethods.OpenIntentToGoogleMapsDirection(RequestDetailsActivity.this,notificationDetailResponse.getRequest().getLat_from(),notificationDetailResponse.getRequest().getLong_from());
            }
        });

        mDestinationLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GMethods.OpenIntentToGoogleMapsDirection(RequestDetailsActivity.this,notificationDetailResponse.getRequest().getLat_to(),notificationDetailResponse.getRequest().getLong_to());
            }
        });

        mAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mType.equals("taxi")){
                    acceptTaxi();
                } else {
                    acceptDelivery();
                }
            }
        });


        /*mRejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/


        mMobileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialPhoneNumber(notificationDetailResponse.getUser().getMobile());
            }
        });


        mChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (util.IsLogged()) {
                    if (!util.GetCurrentUser().getId().equals(notificationDetailResponse.getUser().getId())) {
                        String url = Connector.createStartChatUrl()+"?message=&user_id="+util.GetCurrentUser().getId()+"&to_id="+notificationDetailResponse.getUser().getId();
                        Helper.writeToLog(url);
                        mConnectorSendMessage.getRequest((String) TAG, url);
                    } else {
                        Helper.showSnackBarMessage("لايمكنك مراسلة نفسك", RequestDetailsActivity.this);
                    }
                } else {
                    Helper.showSnackBarMessage("برجاء تسجيل الدخول", RequestDetailsActivity.this);
                }
            }
        });


        mProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RequestDetailsActivity.this,ProfileActivity.class).putExtra("user_id",notificationDetailResponse.getUser().getId()));
            }
        });



    }


    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    public void acceptTaxi(){
        mPresenter.acceptTaxi(responseRequest.getRequest().getId(), util.GetCurrentUser().getId(), new SuccessCallBack() {
            @Override
            public void OnSuccess() {
                //Toast.makeText(RequestDetailsActivity.this, "خطأ يرجي اعادة المحاوله", Toast.LENGTH_LONG).show();
                Helper.showSnackBarMessage("خطأ من فضلك اعد المحاوله",RequestDetailsActivity.this);
            }

            @Override
            public void OnFailure(String message) {
                //Toast.makeText(RequestDetailsActivity.this, message, Toast.LENGTH_LONG).show();
                Helper.showSnackBarMessage(message,RequestDetailsActivity.this);
                finish();
            }

            @Override
            public void OnServerError() {

            }
        });
    }


    public void acceptDelivery(){
        mPresenter.acceptDelivery(responseRequest.getRequest().getId(), util.GetCurrentUser().getId(), new SuccessCallBack() {
            @Override
            public void OnSuccess() {
                //Toast.makeText(RequestDetailsActivity.this, "خطأ يرجي اعادة المحاوله", Toast.LENGTH_LONG).show();
                Helper.showSnackBarMessage("خطأ من فضلك اعد المحاوله",RequestDetailsActivity.this);
            }

            @Override
            public void OnFailure(String message) {
                //Toast.makeText(RequestDetailsActivity.this, message, Toast.LENGTH_LONG).show();
                Helper.showSnackBarMessage(message,RequestDetailsActivity.this);
                finish();
            }

            @Override
            public void OnServerError() {

            }
        });
    }

}
