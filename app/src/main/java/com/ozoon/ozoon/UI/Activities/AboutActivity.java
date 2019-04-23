package com.ozoon.ozoon.UI.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.ozoon.ozoon.R;
import com.ozoon.ozoon.Utils.GMethods;

public class AboutActivity extends AppCompatActivity {

    ImageView back_btn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        GMethods.ChangeFont(this);
        InitViews();
    }

    private void InitViews() {
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
