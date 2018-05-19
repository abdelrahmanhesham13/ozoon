package com.nadernabil216.wlaashal.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

import com.nadernabil216.wlaashal.R;
import com.nadernabil216.wlaashal.UI.Fragments.AdvertsFragment;
import com.nadernabil216.wlaashal.Utils.GMethods;

public class AdvertsActivity extends AppCompatActivity {
    private FragmentManager fm;
    CardView grid_card, map_card;
    ImageView back_btn;
    String Category_Id ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adverts);
        GMethods.ChangeFont(this);
        Intent intent = getIntent();
        if (intent!=null){
            Category_Id=intent.getStringExtra(GMethods.CATEGORY_ID);
        }

        fm = getSupportFragmentManager();
        fm.findFragmentById(R.id.frame_layout);

        grid_card = findViewById(R.id.grid_card);
        grid_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switch_to_ads_list();
            }
        });
        map_card = findViewById(R.id.map_card);
        map_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switch_to_map();
            }
        });

        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Switch_to_ads_list();
    }

    public void Switch_to_ads_list() {
        Bundle bundle = new Bundle();
        bundle.putString(GMethods.CATEGORY_ID,Category_Id);
        AdvertsFragment advertsFragment = new AdvertsFragment();
        advertsFragment.setArguments(bundle);

        fm.beginTransaction().replace(R.id.frame_layout, advertsFragment).commit();

        grid_card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
        map_card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
    }

    public void Switch_to_map() {
        grid_card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
        map_card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
    }

    public void performNearest(View view) {
        Intent intent = new Intent(GMethods.ACTION_UPDATE_DATA);
        intent.putExtra(GMethods.ADVERT_TYPE, GMethods.PERFORME_NEAREST);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void performHighestRate(View view) {
        Intent intent = new Intent(GMethods.ACTION_UPDATE_DATA);
        intent.putExtra(GMethods.ADVERT_TYPE, GMethods.PERFORME_HIGHEST_RATE);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void performSubCategory(View view) {
        Intent intent = new Intent(GMethods.ACTION_UPDATE_DATA);
        intent.putExtra(GMethods.ACTION_TYPE, GMethods.PERFORME_SUBCATEGORY);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
