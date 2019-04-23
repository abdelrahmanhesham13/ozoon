package com.ozoon.ozoon.UI.Fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ozoon.ozoon.Adapters.MessagesAdapter;
import com.ozoon.ozoon.Model.Objects.ChatModel;
import com.ozoon.ozoon.Model.Objects.User;
import com.ozoon.ozoon.R;
import com.ozoon.ozoon.UI.Activities.HomeActivity;
import com.ozoon.ozoon.Utils.Connector;
import com.ozoon.ozoon.Utils.GMethods;
import com.ozoon.ozoon.Utils.Helper;
import com.ozoon.ozoon.Utils.StorageUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    @BindView(R.id.messages)
    RecyclerView mMessages;
    @BindView(R.id.progress_indicator)
    ProgressBar mProgressBar;
    @BindView(R.id.swipyrefreshlayout)
    SwipeRefreshLayout mSwipyRefreshLayout;

    Connector mConnector;

    Map<String, String> mMap;

    ArrayList<ChatModel> mChatModels;

    User mUserModel;

    Map<String,String> mMapChatDelete;

    Connector mConnectorDeleteChat;

    StorageUtil util;

    private static final String TAG = "ChatFragment";

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, v);


        GMethods.ChangeViewFont(v);

        util = StorageUtil.getInstance().doStuff(getContext());

        if (util.IsLogged()){
            if (getContext() != null)
                ((HomeActivity)getContext()).home_menu.setVisibility(View.GONE);
        }

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            ViewCompat.setLayoutDirection(v.findViewById(R.id.parent_layout), ViewCompat.LAYOUT_DIRECTION_RTL);
        }

        if (util.IsLogged()) {
                mUserModel = util.GetCurrentUser();
        }

        mChatModels = new ArrayList<>();
        mMapChatDelete = new HashMap<>();

        final MessagesAdapter adapter = new MessagesAdapter(getActivity(), mChatModels, new MessagesAdapter.OnItemClick() {
            @Override
            public void setOnItemClick(int position) {
                if (getActivity() != null) {
                    //getActivity().findViewById(R.id.toolbar_title).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.receive_name).setVisibility(View.VISIBLE);
                    ((TextView)(getActivity().findViewById(R.id.receiver_name))).setText(mChatModels.get(position).getName());
                    //getActivity().findViewById(R.id.back).setVisibility(View.VISIBLE);
                    MessagesFragment fragment = new MessagesFragment();
                    Bundle bundle = new Bundle();
                    ((HomeActivity)getActivity()).home = false;
                    bundle.putSerializable("chat",mChatModels.get(position));
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_frame, fragment).commit();
                }
            }
        });

        mMessages.setAdapter(adapter);


        mMessages.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        mMap = new HashMap<>();

        mConnector = new Connector(getContext(), new Connector.LoadCallback() {
            @Override
            public void onComplete(String tag, String response) {
                mSwipyRefreshLayout.setRefreshing(false);
                if (true) {
                    mChatModels.clear();
                    mChatModels.addAll(Connector.getChatJson(response));
                    adapter.notifyDataSetChanged();
                    if (mChatModels.size() == 0){
                        if (getActivity() != null) {
                            Helper.showSnackBarMessage("لا يوجد رسائل", ((AppCompatActivity) getActivity()));
                        }
                    }
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mMessages.setVisibility(View.VISIBLE);
                } else {
                    if (getActivity() != null) {
                        Helper.showSnackBarMessage("لا يوجد رسائل", ((AppCompatActivity) getActivity()));
                    }
                    mChatModels.clear();
                    adapter.notifyDataSetChanged();
                    mMessages.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        }, new Connector.ErrorCallback() {
            @Override
            public void onError(VolleyError error) {
                mSwipyRefreshLayout.setRefreshing(false);
                mMessages.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
                if (getActivity() != null) {
                    Helper.showSnackBarMessage("خطأ من فضلك اعد المحاوله", ((AppCompatActivity) getActivity()));
                }
            }
        });

        Helper.writeToLog(mUserModel.getId());
        mMap.put("user_id",mUserModel.getId());
        mConnector.setMap(mMap);
        mMessages.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        final String url = Connector.createGetChatUrl() + "?user_id=" + mUserModel.getId();
        mConnector.getRequest(TAG, url);


        mConnectorDeleteChat = new Connector(getActivity(), new Connector.LoadCallback() {
            @Override
            public void onComplete(String tag, String response) {
                if (Connector.checkStatus(response)) {
                    Helper.showSnackBarMessage("تم حذف الشات بنجاح", ((AppCompatActivity) getActivity()));
                    if (mUserModel != null) {
                        mMap.put("user_id", mUserModel.getId());
                        mConnector.setMap(mMap);
                        String url = Connector.createGetChatUrl() + "?user_id=" + mUserModel.getId();
                    }
                    mConnector.getRequest(TAG, url);
                } else {
                    Helper.showSnackBarMessage("خطأ. من فضلك اعد المحاوله", ((AppCompatActivity) getActivity()));
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        }, new Connector.ErrorCallback() {
            @Override
            public void onError(VolleyError error) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Helper.showSnackBarMessage("خطأ. من فضلك اعد المحاوله", ((AppCompatActivity) getActivity()));
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                mMessages.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                String chatId = String.valueOf(viewHolder.itemView.getTag());
                mMapChatDelete.put("chat_id",chatId);
                mMapChatDelete.put("user_id",mUserModel.getId());
                mConnectorDeleteChat.setMap(mMapChatDelete);
                String url = Connector.createDeleteChatUrl() + "?chat_id="+chatId+"&user_id="+mUserModel.getId();
                mConnectorDeleteChat.getRequest(TAG, url);
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
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.asset_23_3x);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        p.setColor(Color.parseColor("#FFFFFF"));
                        c.drawBitmap(icon, null, icon_dest, p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getLeft() + dX/4, (float) itemView.getTop(), (float) itemView.getLeft(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.asset_23_3x);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + 2 * width, (float) itemView.getTop() + width, (float) itemView.getLeft() + width, (float) itemView.getBottom() - width);
                        p.setColor(Color.parseColor("#FFFFFF"));
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX/4, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(mMessages);

        mSwipyRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mMap.put("user_id",mUserModel.getId());
                mConnector.setMap(mMap);
                mMessages.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                String url = Connector.createGetChatUrl() + "?user_id="+mUserModel.getId();
                mConnector.getRequest(TAG, url);
            }
        });

        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        mConnector.cancelAllRequests(TAG);
        mConnectorDeleteChat.cancelAllRequests(TAG);
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        /*if (util.IsLogged()){
            if (getContext() != null)
                ((HomeActivity)getContext()).home_menu.setVisibility(View.GONE);
        }*/
    }
}
