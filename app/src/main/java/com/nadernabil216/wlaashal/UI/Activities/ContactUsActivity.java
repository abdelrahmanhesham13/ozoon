package com.nadernabil216.wlaashal.UI.Activities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.nadernabil216.wlaashal.R;
import com.nadernabil216.wlaashal.Utils.GMethods;

public class ContactUsActivity extends AppCompatActivity {

    ImageView back_btn;
    TextInputLayout ed_user_name_layout, ed_email_layout, ed_subject_layout, ed_message_layout;
    String user_name, email, subject, message;
    EditText ed_user_name, ed_email, ed_subject, ed_message;
    Button btn_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        GMethods.ChangeFont(this);
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

        ed_user_name_layout.setError("");
        ed_email_layout.setError("");
        ed_subject_layout.setError("");
        ed_message_layout.setError("");

        if(user_name.isEmpty()){
            ed_user_name_layout.setError(getString(R.string.please_enter_username));
        }else if(email.isEmpty()){
            ed_email_layout.setError(getString(R.string.please_enter_email));
        }else if(subject.isEmpty()){
            ed_subject_layout.setError(getString(R.string.please_enter_subject));
        }else if(message.isEmpty()){
            ed_message_layout.setError(getString(R.string.please_enter_message));
        }else{
            SendEmail();
        }
    }

    private void SendEmail() {

    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
