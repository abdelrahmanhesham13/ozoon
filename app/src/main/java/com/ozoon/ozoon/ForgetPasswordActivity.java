package com.ozoon.ozoon;

import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.ozoon.ozoon.Utils.AsteriskPasswordTransformationMethod;
import com.ozoon.ozoon.Utils.Connector;
import com.ozoon.ozoon.Utils.GMethods;
import com.ozoon.ozoon.Utils.Helper;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgetPasswordActivity extends AppCompatActivity {


    @BindView(R.id.login_button)
    Button mLoginButton;
    @BindView(R.id.user_name)
    EditText mEmailEditText;
    @BindView(R.id.password)
    EditText mPasswordEditText;
    @BindView(R.id.progress_indicator)
    ProgressBar mProgressBar;
    @BindView(R.id.code)
    EditText mCodeEditText;
    @BindView(R.id.root_layout)
    View mParentLayout;

    Connector mConnector;
    Connector mConnectorChangePassword;

    String mEmail;
    String mCode;
    String mPassword;

    Map<String,String> mMap;
    Map<String,String> mMapChangePassword;

    private final String TAG = "ForgetPasswordActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        ButterKnife.bind(this);

        GMethods.ChangeFont(this);

        mPasswordEditText.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            ViewCompat.setLayoutDirection(findViewById(R.id.parent_layout), ViewCompat.LAYOUT_DIRECTION_RTL);
        }



        mMap = new HashMap<>();
        mMapChangePassword = new HashMap<>();

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoginButton.getText().equals("استعاده")) {
                    mEmail = mEmailEditText.getText().toString();
                    if (!Helper.validateEmail(mEmail))
                        Helper.showSnackBarMessage("من فضلك. ادخل البريد الالكتروني بشكل صحيح", ForgetPasswordActivity.this);
                    else {
                        mMap.put("email", mEmail);
                        Helper.hideKeyboard(ForgetPasswordActivity.this, v);
                        mConnector.setMap(mMap);
                        mParentLayout.setVisibility(View.INVISIBLE);
                        mProgressBar.setVisibility(View.VISIBLE);
                        mConnector.getRequest(TAG, Connector.createForgetPasswordUrl());
                    }
                } else {
                    mCode = mCodeEditText.getText().toString();
                    mPassword = mPasswordEditText.getText().toString();
                    if (!Helper.validateFields(mCode)){
                        Helper.showSnackBarMessage("من فضلك. ادخل الرمز", ForgetPasswordActivity.this);
                    } else if (!Helper.validateFields(mPassword)){
                        Helper.showSnackBarMessage("من فضلك. ادخل كلمة المرور الجديده", ForgetPasswordActivity.this);
                    } else {
                        mMapChangePassword.put("verification",mCode);
                        mMapChangePassword.put("new_password",mPassword);
                        Helper.hideKeyboard(ForgetPasswordActivity.this, v);
                        mConnectorChangePassword.setMap(mMapChangePassword);
                        mParentLayout.setVisibility(View.INVISIBLE);
                        mProgressBar.setVisibility(View.VISIBLE);
                        mConnectorChangePassword.getRequest(TAG, Connector.createChangePasswordUrl());
                    }
                }
            }
        });


        mConnector = new Connector(ForgetPasswordActivity.this, new Connector.LoadCallback() {
            @Override
            public void onComplete(String tag, String response) {
                if (Connector.checkStatus(response)){
                    Helper.showSnackBarMessage("تم ارسال الرمز علي البريد الالكتروني",ForgetPasswordActivity.this);
                    mParentLayout.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mEmailEditText.setVisibility(View.INVISIBLE);
                    mCodeEditText.setVisibility(View.VISIBLE);
                    mPasswordEditText.setVisibility(View.VISIBLE);
                    mLoginButton.setText("تغيير");
                } else {
                    Helper.showSnackBarMessage("البريد الالكتروني غير مسجل لدينا",ForgetPasswordActivity.this);
                    mParentLayout.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        }, new Connector.ErrorCallback() {
            @Override
            public void onError(VolleyError error) {
                error.printStackTrace();
                mParentLayout.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
                Helper.showSnackBarMessage("خطأ. من فضلك اعد المحاوله",ForgetPasswordActivity.this);
            }
        });


        mConnectorChangePassword = new Connector(ForgetPasswordActivity.this, new Connector.LoadCallback() {
            @Override
            public void onComplete(String tag, String response) {
                if (Connector.checkStatus(response)){
                    Helper.showSnackBarMessage("تم تغيير كلمة المرور بنجاح",ForgetPasswordActivity.this);
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mParentLayout.setVisibility(View.VISIBLE);
                    mCodeEditText.setText("");
                    mPasswordEditText.setText("");
                } else {
                    Helper.showSnackBarMessage("خطأ في الرمز",ForgetPasswordActivity.this);
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mParentLayout.setVisibility(View.VISIBLE);
                }
            }
        }, new Connector.ErrorCallback() {
            @Override
            public void onError(VolleyError error) {
                error.printStackTrace();
                mParentLayout.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
                Helper.showSnackBarMessage("خطأ. من فضلك اعد المحاوله",ForgetPasswordActivity.this);
            }
        });

    }


    @Override
    protected void onStop() {
        super.onStop();
        mConnectorChangePassword.cancelAllRequests(TAG);
        mConnector.cancelAllRequests(TAG);
    }
}
