package com.ozoon.ozoon.UI.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ozoon.ozoon.CallBacks.SuccessCallBack;
import com.ozoon.ozoon.Presenters.MainPresenter;
import com.ozoon.ozoon.R;
import com.ozoon.ozoon.Utils.GMethods;
import com.ozoon.ozoon.Utils.Helper;
import com.ozoon.ozoon.Utils.StorageUtil;

public class ContactUsActivity extends AppCompatActivity {

    ImageView back_btn;
    TextInputLayout ed_user_name_layout, ed_email_layout, ed_subject_layout, ed_message_layout;
    String user_name, email, subject, message;
    EditText ed_user_name, ed_email, ed_subject, ed_message;
    Button btn_send;
    MainPresenter presenter ;
    StorageUtil util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        GMethods.ChangeFont(this);
        presenter=new MainPresenter();
        util=StorageUtil.getInstance().doStuff(this);
        InitViews();
    }

    private void InitViews() {
        ed_user_name_layout = findViewById(R.id.ed_user_name_layout);
        ed_email_layout = findViewById(R.id.ed_email_layout);
        ed_subject_layout = findViewById(R.id.ed_subject_layout);
        ed_message_layout = findViewById(R.id.ed_message_layout);

        ed_user_name = findViewById(R.id.ed_user_name);
        ed_email = findViewById(R.id.ed_email);
        ed_subject = findViewById(R.id.ed_subject);
        ed_message = findViewById(R.id.ed_message);

        back_btn = findViewById(R.id.back_btn);
        btn_send = findViewById(R.id.btn_send);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Checker();
            }
        });

    }

    private void Checker() {
        user_name = ed_user_name.getText().toString().trim();
        email = ed_email.getText().toString().trim();
        subject = ed_subject.getText().toString().trim();
        message = ed_message.getText().toString().trim();

        if(user_name.isEmpty()){
            //Toast.makeText(this, getString(R.string.please_enter_username), Toast.LENGTH_SHORT).show();
            Helper.showSnackBarMessage("برجاء ادخل اسم المستخدم",ContactUsActivity.this);
        }else if(email.isEmpty()){
            //Toast.makeText(this, getString(R.string.please_enter_email), Toast.LENGTH_SHORT).show();
            Helper.showSnackBarMessage("برجاء ادخل البريد الالكتروني",ContactUsActivity.this);
        }else if(subject.isEmpty()){
            //Toast.makeText(this, getString(R.string.please_enter_subject), Toast.LENGTH_SHORT).show();
            Helper.showSnackBarMessage("برجاء ادخل الموضوع",ContactUsActivity.this);
        }else if(message.isEmpty()){
            //Toast.makeText(this, getString(R.string.please_enter_message), Toast.LENGTH_SHORT).show();
            Helper.showSnackBarMessage("برجاء ادخال رسالتك",ContactUsActivity.this);
        }else{
            SendEmail();
        }
    }

    private void SendEmail() {
        final ProgressDialog progressDialog = GMethods.show_progress_dialoug(this,"جاري الإرسال",false);
        presenter.SendFeedBack(util.GetCurrentUser().getId(), user_name, email, subject, message, new SuccessCallBack() {
            @Override
            public void OnSuccess() {
                progressDialog.dismiss();
                //Toast.makeText(ContactUsActivity.this, "تم إرسال رسالتك بنجاح", Toast.LENGTH_SHORT).show();
                Helper.showSnackBarMessage("تم ارسال رسالتك بنجاح",ContactUsActivity.this);
                ContactUsActivity.this.finish();
            }

            @Override
            public void OnFailure(String message) {
                progressDialog.dismiss();
                GMethods.show_alert_dialoug(ContactUsActivity.this,
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
                GMethods.show_alert_dialoug(ContactUsActivity.this,
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
    public void onBackPressed() {
        finish();
    }

}
