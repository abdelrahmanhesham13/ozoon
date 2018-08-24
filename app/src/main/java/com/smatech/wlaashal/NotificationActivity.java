package com.smatech.wlaashal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.smatech.wlaashal.Adapters.GridSpacingItemDecoration;
import com.smatech.wlaashal.Adapters.NotificationsAdapter;
import com.smatech.wlaashal.CallBacks.AllNotificationsCallBack;
import com.smatech.wlaashal.Model.Objects.Notification;
import com.smatech.wlaashal.Presenters.MainPresenter;
import com.smatech.wlaashal.UI.Activities.HomeActivity;
import com.smatech.wlaashal.UI.Fragments.Home;
import com.smatech.wlaashal.Utils.GMethods;
import com.smatech.wlaashal.Utils.Helper;
import com.smatech.wlaashal.Utils.StorageUtil;

import java.util.ArrayList;

import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;

public class NotificationActivity extends AppCompatActivity {

    ImageView back_btn,mChatButton ;
    RadioRealButtonGroup group;
    RecyclerView myRequests;
    RecyclerView clientRequests;
    MainPresenter presenter;

    ArrayList<Notification> myNotifications;
    ArrayList<Notification> clientsNotification;
    StorageUtil util;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        GMethods.ChangeFont(this);
        util = StorageUtil.getInstance().doStuff(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (!util.IsLogged()){
            //Toast.makeText(this, "من فضلك قم بتسجيل الدخول", Toast.LENGTH_LONG).show();
            Helper.showSnackBarMessage("من فضلك قم بتسجيل الدخول",NotificationActivity.this);
            finish();
        } else {

            InitViews();

            presenter = new MainPresenter();

            getData(0);
            getData(1);
        }
    }


    private void InitViews() {
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        group = findViewById(R.id.radio_group);
        mChatButton = findViewById(R.id.message_btn);
        myRequests = findViewById(R.id.recyclerViewMyRequests);
        clientRequests = findViewById(R.id.recyclerViewClientRequests);
        group.setOnClickedButtonListener(new RadioRealButtonGroup.OnClickedButtonListener() {
            @Override
            public void onClickedButton(RadioRealButton button, int position) {
                if (position == 0){
                    myRequests.setVisibility(View.VISIBLE);
                    clientRequests.setVisibility(View.GONE);
                } else {
                    myRequests.setVisibility(View.GONE);
                    clientRequests.setVisibility(View.VISIBLE);
                }
            }
        });


        mChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotificationActivity.this, HomeActivity.class).putExtra("goToChat",true));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }


    private void getData(final int flag){
     presenter.getNotifications(String.valueOf(preferences.getBoolean("activateTaxi",true)), String.valueOf(preferences.getBoolean("activateDelivery",true)), String.valueOf(flag), util.GetCurrentUser().getId(), new AllNotificationsCallBack() {
         @Override
         public void OnSuccess(ArrayList<Notification> Notification) {
             if (flag == 0) {
                 myNotifications = Notification;
             } else {
                 clientsNotification = Notification;
             }
             setData(flag);
         }

         @Override
         public void OnFailure(String message) {

         }

         @Override
         public void OnServerError() {

         }
     });
    }



    private void setData(int flag){
        if (flag == 0) {
            NotificationsAdapter notificationsAdapter = new NotificationsAdapter(myNotifications, this, new NotificationsAdapter.OnItemClick() {
                @Override
                public void setOnItemClick(int position) {
                    if (myNotifications.get(position).getStatus().equals("1")){
                        startActivity(new Intent(NotificationActivity.this,AcceptedRequestActivity.class).putExtra("type",myNotifications.get(position).getType()).putExtra("id",myNotifications.get(position).getId()).putExtra("class","mine"));
                    }
                }
            },1);
            myRequests.setAdapter(notificationsAdapter);

            myRequests.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        } else {
            NotificationsAdapter notificationsAdapter = new NotificationsAdapter(clientsNotification, this, new NotificationsAdapter.OnItemClick() {
                @Override
                public void setOnItemClick(int position) {
                    startActivity(new Intent(NotificationActivity.this,RequestDetailsActivity.class).putExtra("type",clientsNotification.get(position).getType()).putExtra("id",clientsNotification.get(position).getId()));
                }
            },0);
            clientRequests.setAdapter(notificationsAdapter);

            clientRequests.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }
    }

}
