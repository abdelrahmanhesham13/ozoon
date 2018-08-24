package com.smatech.wlaashal.UI.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.smatech.wlaashal.CallBacks.LoginCallBack;
import com.smatech.wlaashal.ForgetPasswordActivity;
import com.smatech.wlaashal.Model.Objects.User;
import com.smatech.wlaashal.Presenters.LoginPresenter;
import com.smatech.wlaashal.R;
import com.smatech.wlaashal.UI.Activities.HomeActivity;
import com.smatech.wlaashal.UI.Activities.LoginActivity;
import com.smatech.wlaashal.Utils.AsteriskPasswordTransformationMethod;
import com.smatech.wlaashal.Utils.GMethods;
import com.smatech.wlaashal.Utils.StorageUtil;

/**
 * Created by NaderNabil216@gmail.com on 5/9/2018.
 */
public class SignIn extends Fragment implements View.OnClickListener {
    TextInputLayout username_layout, password_layout;
    EditText ed_username, ed_password;
    TextView tv_forget_password, tv_create_new_account;
    Button btn_login;
    LoginPresenter presenter;
    LoginActivity mActivity;
    String user_name ,password ;
    StorageUtil util ;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_sign_in, container, false);
        mActivity = (LoginActivity) getActivity();
        presenter = new LoginPresenter();
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        util = StorageUtil.getInstance().doStuff(getActivity());
        GMethods.ChangeViewFont(view);
        InitViews(view);
        return view;
    }

    private void InitViews(View view) {
        ed_username = view.findViewById(R.id.ed_user_name);
        ed_password = view.findViewById(R.id.ed_password);
        ed_password.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        username_layout = view.findViewById(R.id.ed_user_name_layout);
        password_layout = view.findViewById(R.id.ed_password_layout);
        tv_forget_password = view.findViewById(R.id.tv_forget_password);
        tv_create_new_account = view.findViewById(R.id.tv_create_new_account);
        btn_login = view.findViewById(R.id.btn_send);

        tv_forget_password.setOnClickListener(this);
        tv_create_new_account.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    private void Checker(View v) {
        user_name = ed_username.getText().toString().trim();
        password = ed_password.getText().toString().trim();
        if (getActivity() != null) {
            GMethods.hideKeyboard(((AppCompatActivity) getActivity()),v);
        }

        //username_layout.setError("");
        //password_layout.setError("");

        if (user_name.isEmpty()) {
            //username_layout.setError(getString(R.string.please_enter_username));
            if (getActivity() != null) {
                GMethods.showSnackBarMessage("من فضلك ادخل اسم المستخدم", ((AppCompatActivity) getActivity()));
            }
        } else if (password.isEmpty()) {
            //password_layout.setError(getString(R.string.please_enter_password));
            if (getActivity() != null) {
                GMethods.showSnackBarMessage("من فضلك ادخل كلمة المرور", ((AppCompatActivity) getActivity()));
            }
        } else {
            Login();
        }
    }

    private void Login() {
       final ProgressDialog progressDialog = GMethods.show_progress_dialoug(getActivity(),
                getString(R.string.logging_in),
                true);

        presenter.Login(user_name, password,util.getToken(), new LoginCallBack() {
            @Override
            public void OnSuccess(User user) {
                progressDialog.dismiss();
                util.setIsLogged(true);
                util.SetCurrentUser(user);
                util.putPassword(password);
                StorageUtil.setAdsCount(getContext(), String.valueOf(user.getAds_count()));
                if (user.getDelivery().equals("1")) {
                    util.isDelivery(true);
                    editor = preferences.edit().putBoolean("activateDelivery",true);
                    editor.apply();
                } else if (user.getDelivery().equals("2")){
                    util.isDelivery(true);
                    editor = preferences.edit().putBoolean("activateDelivery",false);
                    editor.apply();
                }
                if (user.getDriver().equals("1")){
                    util.isTaxi(true);
                    editor = preferences.edit().putBoolean("activateTaxi",true);
                    editor.apply();
                } else if (user.getDriver().equals("2")){
                    util.isTaxi(true);
                    editor = preferences.edit().putBoolean("activateTaxi",false);
                    editor.apply();
                }
                startActivity(new Intent(getActivity(), HomeActivity.class));
            }

            @Override
            public void OnFailure(String message) {
                progressDialog.dismiss();
                GMethods.show_alert_dialoug(getActivity(),
                        message,
                        getString(R.string.app_name),
                        true,
                        "",
                        "",
                        null,
                        null);
            }

            @Override
            public void OnServerError() {
                progressDialog.dismiss();
                GMethods.show_alert_dialoug(getActivity(),
                        getString(R.string.server_error),
                        getString(R.string.app_name),
                        true,
                        "",
                        "",
                        null,
                        null);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                Checker(v);
                break;
            case R.id.tv_create_new_account:
                mActivity.SwitchToSignUp();
                break;
            case R.id.tv_forget_password:
                startActivity(new Intent(getActivity(), ForgetPasswordActivity.class));
                break;
        }
    }
}
