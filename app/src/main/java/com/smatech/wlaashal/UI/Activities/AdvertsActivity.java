package com.smatech.wlaashal.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smatech.wlaashal.MapFragment;
import com.smatech.wlaashal.NotificationActivity;
import com.smatech.wlaashal.R;
import com.smatech.wlaashal.UI.Fragments.AdvertsFragment;
import com.smatech.wlaashal.Utils.GMethods;
import com.smatech.wlaashal.Utils.StorageUtil;


public class AdvertsActivity extends AppCompatActivity {
    private FragmentManager fm;
    CardView grid_card, map_card,list_card,add_card;
    ImageView back_btn,notification_btn;
    String Category_Id ;
    View filterParent,lineView;
    StorageUtil storageUtil;
    boolean newest = true;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adverts);
        GMethods.ChangeFont(this);
        storageUtil = StorageUtil.getInstance().doStuff(this);
        filterParent = findViewById(R.id.filterParent);
        lineView = findViewById(R.id.lineView);
        Intent intent = getIntent();
        if (intent!=null){
            Category_Id=intent.getStringExtra(GMethods.CATEGORY_ID);
            if (Category_Id.equals("-1")){
                filterParent.setVisibility(View.GONE);
                lineView.setVisibility(View.GONE);
            }
        }

        fm = getSupportFragmentManager();
        fm.findFragmentById(R.id.frame_layout);
        notification_btn = findViewById(R.id.notification_btn);

        notification_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdvertsActivity.this, HomeActivity.class).putExtra("goToNotification",true));
            }
        });

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

        list_card = findViewById(R.id.list_card);
        list_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switch_to_ads_list_listed();
            }
        });
        add_card = findViewById(R.id.add_button);

        if (storageUtil.IsLogged()){
            add_card.setVisibility(View.VISIBLE);
            add_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AdvertsActivity.this, AddNewAdvertActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            add_card.setVisibility(View.GONE);
        }




        Switch_to_ads_list_listed();
    }

    public void Switch_to_ads_list() {
        Bundle bundle = new Bundle();
        bundle.putString(GMethods.CATEGORY_ID,Category_Id);
        AdvertsFragment advertsFragment = new AdvertsFragment();
        advertsFragment.setArguments(bundle);

        fm.beginTransaction().replace(R.id.frame_layout, advertsFragment).commit();

        grid_card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
        map_card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
        list_card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
    }


    public void Switch_to_ads_list_listed() {
        Bundle bundle = new Bundle();
        bundle.putString(GMethods.CATEGORY_ID,Category_Id);
        bundle.putString("list","true");
        AdvertsFragment advertsFragment = new AdvertsFragment();
        advertsFragment.setArguments(bundle);

        fm.beginTransaction().replace(R.id.frame_layout, advertsFragment).commit();

        grid_card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
        map_card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
        list_card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
    }

    public void Switch_to_map() {
        grid_card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
        map_card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
        list_card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));

        MapFragment mapFragment = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putString(GMethods.CATEGORY_ID,Category_Id);
        mapFragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.frame_layout, mapFragment).commit();

    }

    public void performNearest(View view) {
        Intent intent = new Intent(GMethods.ACTION_UPDATE_DATA);
        intent.putExtra(GMethods.ADVERT_TYPE, GMethods.PERFORME_NEAREST);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    public void performSubCategory(View view) {
        Intent intent = new Intent(GMethods.ACTION_UPDATE_DATA);
        intent.putExtra(GMethods.ADVERT_TYPE, GMethods.PERFORME_SUBCATEGORY);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void performOldest(View view) {
        if (newest) {
            Intent intent = new Intent(GMethods.ACTION_UPDATE_DATA);
            intent.putExtra(GMethods.ADVERT_TYPE, GMethods.ACTION_OLDEST);

            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            ((TextView)view).setText("الاحدث");
            newest = false;
        } else {
            Intent intent = new Intent(GMethods.ACTION_UPDATE_DATA);
            intent.putExtra(GMethods.ADVERT_TYPE, GMethods.PERFORME_HIGHEST_RATE);

            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            ((TextView)view).setText("الاقدم");
            newest = true;
        }
    }
}
