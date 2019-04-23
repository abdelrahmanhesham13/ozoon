package com.ozoon.ozoon;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ozoon.ozoon.Adapters.NotificationsAdapter;
import com.ozoon.ozoon.CallBacks.AllNotificationsCallBack;
import com.ozoon.ozoon.CallBacks.SuccessCallBack;
import com.ozoon.ozoon.Model.Objects.Notification;
import com.ozoon.ozoon.Presenters.MainPresenter;
import com.ozoon.ozoon.UI.Activities.HomeActivity;
import com.ozoon.ozoon.UI.Fragments.Home;
import com.ozoon.ozoon.Utils.GMethods;
import com.ozoon.ozoon.Utils.Helper;
import com.ozoon.ozoon.Utils.StorageUtil;

import java.util.ArrayList;

import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    ImageView back_btn,mChatButton ;
    RadioRealButtonGroup group;
    RadioRealButton clientRequest;
    RecyclerView myRequests;
    RecyclerView clientRequests;
    MainPresenter presenter;
    TextView mNoData;

    ArrayList<Notification> myNotifications;
    ArrayList<Notification> clientsNotification;
    StorageUtil util;
    SharedPreferences preferences;

    NotificationsAdapter notificationsAdapterMine;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_notification, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        GMethods.ChangeViewFont(v);
        util = StorageUtil.getInstance().doStuff(getContext());

        if (!util.IsLogged()){
            //Toast.makeText(getContext(), "من فضلك قم بتسجيل الدخول", Toast.LENGTH_LONG).show();
            Helper.showSnackBarMessage("من فضلك قم بتسجيل الدخول",(AppCompatActivity) getContext());

            //finish();
        } else {

            InitViews(v);

            presenter = new MainPresenter();

            getData(0);
            if (util.getDelivery() || util.getTaxi()) {
                getData(1);
            }
        }

        Helper.writeToLog("Taxi : " + util.getTaxi());
        Helper.writeToLog("Delivery : " + util.getDelivery());
        Helper.writeToLog("Profile Id : " + util.GetCurrentUser().getId() );


        if (util.getDelivery() || util.getTaxi()){
            clientRequest.setVisibility(View.VISIBLE);
        } else {
            clientRequest.setVisibility(View.GONE);
            group.setSelectorSize(0);
        }


        //Helper.showSnackBarMessage("لألغاء طلبكم اسحب علي اليمين",(AppCompatActivity)getActivity());


        return v;
    }


    private void InitViews(View v) {
        back_btn = v.findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity)getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.home_frame,new Home()).commit();
            }
        });
        group = v.findViewById(R.id.radio_group);
        mChatButton = v.findViewById(R.id.message_btn);
        myRequests = v.findViewById(R.id.recyclerViewMyRequests);
        clientRequests = v.findViewById(R.id.recyclerViewClientRequests);
        clientRequest = v.findViewById(R.id.client_request_button);
        mNoData = v.findViewById(R.id.no_data);
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
                startActivity(new Intent(getContext(), HomeActivity.class).putExtra("goToChat",true));
                //finish();
            }
        });

    }



    private void getData(final int flag){
        Helper.writeToLog(String.valueOf(util.getTaxi()) +" " +  String.valueOf(util.getDelivery())+" "+ String.valueOf(flag)+ " "+util.GetCurrentUser().getId());
        int delivery = 0;
        int taxi = 0;
        if (preferences.getBoolean("activateDelivery",false)){
            delivery = 1;
        }
        if (preferences.getBoolean("activateTaxi",false)){
            taxi = 1;
        }
        presenter.getNotifications(String.valueOf(taxi), String.valueOf(delivery), String.valueOf(flag), util.GetCurrentUser().getId(), new AllNotificationsCallBack() {
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


    @Override
    public void onStart() {
        super.onStart();
        getData(0);
        if (util.getDelivery() || util.getTaxi()) {
            getData(1);
        }
        Helper.writeToLog("on start");
    }


    @Override
    public void onResume() {
        super.onResume();
        getData(0);
        if (util.getDelivery() || util.getTaxi()) {
            getData(1);
        }
        Helper.writeToLog("on resume");
    }

    private void setData(int flag){
        if (flag == 0) {
            notificationsAdapterMine = new NotificationsAdapter(myNotifications, getContext(), new NotificationsAdapter.OnItemClick() {
                @Override
                public void setOnItemClick(int position) {
                    if (myNotifications.get(position).getStatus().equals("1")){
                        if (myNotifications.get(position).getUser_id().equals(util.GetCurrentUser().getId())) {
                            startActivity(new Intent(getActivity(), AcceptedRequestActivity.class).putExtra("type", myNotifications.get(position).getType()).putExtra("id", myNotifications.get(position).getId()).putExtra("class", "mine"));
                        } else {
                            startActivity(new Intent(getActivity(),RequestDetailsActivity.class).putExtra("type",myNotifications.get(position).getType()).putExtra("id",myNotifications.get(position).getId()).putExtra("mine",true));
                        }
                    } else {
                        startActivity(new Intent(getActivity(),RequestDetailsActivity.class).putExtra("type",myNotifications.get(position).getType()).putExtra("id",myNotifications.get(position).getId()).putExtra("mine",true).putExtra("hideCommunication",true));
                    }
                }
            },1);
            myRequests.setAdapter(notificationsAdapterMine);

            myRequests.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                    final int pos = (int) viewHolder.itemView.getTag();
                    Helper.writeToLog("Position " + pos);
                    presenter.deleteRequest(myNotifications.get(pos).getType(), myNotifications.get(pos).getId(), new SuccessCallBack() {
                        @Override
                        public void OnSuccess() {
                            //getData(0);
                            myNotifications.remove(viewHolder.getAdapterPosition());
                            notificationsAdapterMine.notifyDataSetChanged();
                        }

                        @Override
                        public void OnFailure(String message) {

                        }

                        @Override
                        public void OnServerError() {

                        }
                    });
                }

                @Override
                public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                    Paint p = new Paint();
                    Bitmap icon;
                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                        View itemView = viewHolder.itemView;
                        float height = (float) itemView.getBottom() - (float) itemView.getTop();
                        float width = height / 3;

                        if (dX < 0) {
                            p.setColor(Color.parseColor("#D32F2F"));
                            RectF background = new RectF((float) itemView.getRight() + dX/4, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                            c.drawRect(background, p);
                            icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_swipe);
                            RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                            p.setColor(Color.parseColor("#FFFFFF"));
                            c.drawBitmap(icon, null, icon_dest, p);
                        } else {
                            p.setColor(Color.parseColor("#D32F2F"));
                            RectF background = new RectF((float) itemView.getLeft() + dX/4, (float) itemView.getTop(), (float) itemView.getLeft(), (float) itemView.getBottom());
                            c.drawRect(background, p);
                            icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_swipe);
                            RectF icon_dest = new RectF((float) itemView.getLeft() + 2 * width, (float) itemView.getTop() + width, (float) itemView.getLeft() + width, (float) itemView.getBottom() - width);
                            p.setColor(Color.parseColor("#FFFFFF"));
                            c.drawBitmap(icon, null, icon_dest, p);
                        }
                    }
                    super.onChildDraw(c, recyclerView, viewHolder, dX/4, dY, actionState, isCurrentlyActive);
                }
            }).attachToRecyclerView(myRequests);
        } else {
            NotificationsAdapter notificationsAdapter = new NotificationsAdapter(clientsNotification, getContext(), new NotificationsAdapter.OnItemClick() {
                @Override
                public void setOnItemClick(int position) {
                    startActivity(new Intent(getActivity(),RequestDetailsActivity.class).putExtra("type",clientsNotification.get(position).getType()).putExtra("id",clientsNotification.get(position).getId()));
                }
            },0);
            clientRequests.setAdapter(notificationsAdapter);

            clientRequests.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        }
    }

}
