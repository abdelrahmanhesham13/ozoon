package com.ozoon.ozoon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ozoon.ozoon.Model.Objects.ChatModel;
import com.ozoon.ozoon.Model.Objects.ResultItemModel;
import com.ozoon.ozoon.Model.Objects.User;
import com.ozoon.ozoon.UI.Activities.HomeActivity;
import com.ozoon.ozoon.Utils.Connector;
import com.ozoon.ozoon.Utils.GMethods;
import com.ozoon.ozoon.Utils.Helper;
import com.ozoon.ozoon.Utils.StorageUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeliveryDetailsDialogFragment extends AppCompatDialogFragment {

    private static final String TAG = DeliveryDetailsDialogFragment.class.getSimpleName();
    String id;
    String mType;
    ResultItemModel mResult;
    Connector mConnector;
    @BindView(R.id.progressIndicator)
    ProgressBar mProgressBar;
    @BindView(R.id.parent_layout)
    View mParentLayout;
    @BindView(R.id.user_name)
    TextView mUsername;
    @BindView(R.id.rating_bar)
    RatingBar mRatingBar;
    @BindView(R.id.destination)
    TextView mDestination;
    @BindView(R.id.price)
    TextView mPrice;
    @BindView(R.id.request)
    TextView mRequestBtn;

    Connector mConnectorSendMessage;

    StorageUtil util;
    User user;

    ChatModel mChatModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_delivery_details, container);
        ButterKnife.bind(this, v);

        util = StorageUtil.getInstance().doStuff(getContext());
        user = util.GetCurrentUser();

        GMethods.ChangeViewFont(v);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getString("id");
            mType = bundle.getString("type");
        }

        mConnector = new Connector(getContext(), new Connector.LoadCallback() {
            @Override
            public void onComplete(String tag, String response) {
                if (Connector.checkStatus(response)) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mParentLayout.setVisibility(View.VISIBLE);
                    if (mType.equals("trip")) {
                        mResult = Connector.getItems(response).get(0);
                        mUsername.setText(mResult.getUser().getName());
                        try {
                            mRatingBar.setRating(Float.parseFloat(mResult.getUser().getRate()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mDestination.setText(mResult.getFromCity() + "←" + mResult.getToCity());
                        mPrice.setText(mResult.getPrice() + " ريال");
                    } else {
                        mResult = Connector.getTrips(response).get(0);
                        mUsername.setText(mResult.getUser().getName());
                        try {
                            mRatingBar.setRating(Float.parseFloat(mResult.getUser().getRate()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mDestination.setText(mResult.getFromCity() + "←" + mResult.getToCity());
                        mPrice.setText(mResult.getPrice() + " ريال");
                    }
                } else {
                    dismiss();
                }
            }
        }, new Connector.ErrorCallback() {
            @Override
            public void onError(VolleyError error) {
                dismiss();
            }
        });

        mProgressBar.setVisibility(View.VISIBLE);
        mParentLayout.setVisibility(View.INVISIBLE);
        if (mType.equals("trip")) {
            mConnector.getRequest(TAG, "http://cta3.com/ozoon/api/get_items?id=" + id);
        } else {
            mConnector.getRequest(TAG, "http://cta3.com/ozoon/api/get_trips?id=" + id);
        }

        mConnectorSendMessage = new Connector(getContext(), new Connector.LoadCallback() {
            @Override
            public void onComplete(String tag, String response) {
                if (Connector.checkStatus(response)) {
                    mChatModel = Connector.getChatModelJson(response, user.getName(), user.getId(), util.GetCurrentUser().getId());
                    //Intent returnIntent = new Intent();
                    //returnIntent.putExtra("chat",mChatModel);

                    startActivity(new Intent(getContext(), HomeActivity.class).putExtra("chat", mChatModel));
                    //setResult(Activity.RESULT_OK,returnIntent);
                    //finish();
                } else {
                    if (getActivity() != null)
                    Helper.showSnackBarMessage("خطأ من فضلك اعد المحاوله", (AppCompatActivity)getActivity());
                }
            }
        }, new Connector.ErrorCallback() {
            @Override
            public void onError(VolleyError error) {
                if (getActivity() != null)
                Helper.showSnackBarMessage("خطأ من فضلك اعد المحاوله", (AppCompatActivity)getActivity());
            }
        });

        mRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (util.IsLogged()) {
                    if (!util.GetCurrentUser().getId().equals(user.getId())) {
                        String url = Connector.createStartChatUrl() + "?message=&user_id=" + util.GetCurrentUser().getId() + "&to_id=" + user.getId();
                        Helper.writeToLog(url);
                        mConnectorSendMessage.getRequest(TAG, url);
                    } else {
                        if (getActivity() != null)
                            Helper.showSnackBarMessage("لايمكنك مراسلة نفسك", (AppCompatActivity) getActivity());
                    }
                } else {
                    if (getActivity() != null)
                        Helper.showSnackBarMessage("برجاء تسجيل الدخول", (AppCompatActivity) getActivity());
                }
            }
        });

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

    }
}
