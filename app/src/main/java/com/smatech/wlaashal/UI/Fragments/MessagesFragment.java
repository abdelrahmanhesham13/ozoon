package com.smatech.wlaashal.UI.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.smatech.wlaashal.Adapters.ConversationMessagesAdapter;
import com.smatech.wlaashal.Model.Objects.ChatModel;
import com.smatech.wlaashal.Model.Objects.MessageModel;
import com.smatech.wlaashal.Model.Objects.User;
import com.smatech.wlaashal.ProfileActivity;
import com.smatech.wlaashal.R;
import com.smatech.wlaashal.UI.Activities.HomeActivity;
import com.smatech.wlaashal.Utils.Connector;
import com.smatech.wlaashal.Utils.GMethods;
import com.smatech.wlaashal.Utils.Helper;
import com.smatech.wlaashal.Utils.StorageUtil;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment {


    @BindView(R.id.messages)
    RecyclerView mMessages;
    @BindView(R.id.progress_indicator)
    ProgressBar mProgressBar;
    @BindView(R.id.send_message_parent)
    View mSendParent;
    @BindView(R.id.send_message)
    TextView mSendMessageButton;
    @BindView(R.id.message_text)
    EditText mMessageText;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.receiver_name)
    TextView receiverName;
    @BindView(R.id.refrest_button)
    ImageView refresh;

    ArrayList<MessageModel> mMessageModels;
    ChatModel mChatModel;

    User mUserModel;

    Connector mConnector;
    Connector mConnectorSendMessage;

    Map<String, String> mMap;
    Map<String,String> mMapSendMessage;

    String message;

    StorageUtil util;

    private static final String TAG = "MessagesFragment";

    public MessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_messages, container, false);

        GMethods.ChangeViewFont(v);

        ButterKnife.bind(this,v);

        util = StorageUtil.getInstance().doStuff(getContext());

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            ViewCompat.setLayoutDirection(v.findViewById(R.id.parent_layout), ViewCompat.LAYOUT_DIRECTION_RTL);
        }

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("chat")){
            mChatModel = (ChatModel) bundle.getSerializable("chat");
            receiverName.setText(mChatModel.getName());
            receiverName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().startActivityForResult(new Intent(getActivity(), ProfileActivity.class).putExtra("user_id",mChatModel.getToId()).putExtra("chat","chat"),50);
                }
            });
        }

        if (util.IsLogged()){
            mUserModel = util.GetCurrentUser();
        }


        ((HomeActivity)getActivity()).home_menu.setVisibility(View.GONE);

        mMessageModels = new ArrayList<>();
        mMapSendMessage = new HashMap<>();


        final ConversationMessagesAdapter adapter = new ConversationMessagesAdapter(getActivity(), mMessageModels, new ConversationMessagesAdapter.OnItemClick() {
            @Override
            public void setOnItemClick(int position) {

            }
        });

        mMessages.setAdapter(adapter);


        mMessages.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        mMap = new HashMap<>();

        mConnector = new Connector(getContext(), new Connector.LoadCallback() {
            @Override
            public void onComplete(String tag, String response) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (Connector.checkStatus(response)) {
                    mMessageModels.clear();
                    mMessageModels.addAll(Connector.getChatMessagesJson(response,mUserModel));
                    adapter.notifyDataSetChanged();
                    mMessages.scrollToPosition(mMessageModels.size() - 1);
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mMessages.setVisibility(View.VISIBLE);
                    mSendParent.setVisibility(View.VISIBLE);
                } else {
                    if (getActivity() != null) {
                        Helper.showSnackBarMessage("لا يوجد رسائل", ((AppCompatActivity) getActivity()));
                    }
                    mMessages.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mSendParent.setVisibility(View.VISIBLE);
                }
            }
        }, new Connector.ErrorCallback() {
            @Override
            public void onError(VolleyError error) {
                mSwipeRefreshLayout.setRefreshing(false);
                mMessages.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
                mSendParent.setVisibility(View.VISIBLE);
                if (getActivity() != null) {
                    Helper.showSnackBarMessage("خطأ من فضلك اعد المحاوله", ((AppCompatActivity) getActivity()));
                }
            }
        });

        mSendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = mMessageText.getText().toString();
                if (!Helper.validateFields(message)){
                    if (getActivity() != null) {
                        Helper.showSnackBarMessage("ادخل الرساله", ((AppCompatActivity) getActivity()));
                    }
                } else {
                    Helper.hideKeyboard(((AppCompatActivity) getActivity()),v);
                    mMapSendMessage.put("chat_id", mChatModel.getChatId());
                    mMapSendMessage.put("message", message);
                    mMapSendMessage.put("user_id", mUserModel.getId());
                    mMapSendMessage.put("to_id", mChatModel.getToId());
                    mConnectorSendMessage.setMap(mMapSendMessage);
                    mMessages.setVisibility(View.INVISIBLE);
                    mSendParent.setVisibility(View.INVISIBLE);
                    mProgressBar.setVisibility(View.VISIBLE);
                    String url = Connector.createSendMessageUrl() + "?chat_id=" + mChatModel.getChatId() +
                             "&user_id=" + mUserModel.getId() + "&to_id=" + mChatModel.getToId();
                    Uri  builder = Uri.parse(url.toString())
                            .buildUpon()
                            .appendQueryParameter("message",message).build();


                    mConnectorSendMessage.getRequest(TAG, builder.toString());
                }
            }
        });

        mMap.put("chat_id",mChatModel.getChatId());
        mConnector.setMap(mMap);
        mMessages.setVisibility(View.INVISIBLE);
        mSendParent.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        String url = Connector.createGetChatMessagesUrl() + "?chat_id=" + mChatModel.getChatId();
        mConnector.getRequest(TAG, url);


        mConnectorSendMessage = new Connector(getContext(), new Connector.LoadCallback() {
            @Override
            public void onComplete(String tag, String response) {
                if (Connector.checkStatus(response)) {
                    mMessageText.setText("");
                    String url = Connector.createGetChatMessagesUrl() + "?chat_id=" + mChatModel.getChatId();
                    mConnector.getRequest(TAG, url);
                } else {
                    if (getActivity() != null) {
                        Helper.showSnackBarMessage("خطأ من فضلك اعد المحاوله", ((AppCompatActivity) getActivity()));
                    }
                    mMessages.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mSendParent.setVisibility(View.VISIBLE);
                }
            }
        }, new Connector.ErrorCallback() {
            @Override
            public void onError(VolleyError error) {
                mMessages.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
                mSendParent.setVisibility(View.VISIBLE);
                if (getActivity() != null) {
                    Helper.showSnackBarMessage("خطأ من فضلك اعد المحاوله", ((AppCompatActivity) getActivity()));
                }
            }
        });


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mMap.put("chat_id",mChatModel.getChatId());
                mConnector.setMap(mMap);
                mMessages.setVisibility(View.INVISIBLE);
                mSendParent.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                String url = Connector.createGetChatMessagesUrl() + "?chat_id=" + mChatModel.getChatId();
                mConnector.getRequest(TAG, url);
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.put("chat_id",mChatModel.getChatId());
                mConnector.setMap(mMap);
                mMessages.setVisibility(View.INVISIBLE);
                mSendParent.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                String url = Connector.createGetChatMessagesUrl() + "?chat_id=" + mChatModel.getChatId();
                mConnector.getRequest(TAG, url);
            }
        });

        return v;
    }




    @Override
    public void onStop() {
        super.onStop();
        mConnectorSendMessage.cancelAllRequests(TAG);
        mConnector.cancelAllRequests(TAG);
    }
}
