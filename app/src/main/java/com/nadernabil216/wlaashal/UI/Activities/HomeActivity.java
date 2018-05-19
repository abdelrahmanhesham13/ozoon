package com.nadernabil216.wlaashal.UI.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nadernabil216.wlaashal.R;
import com.nadernabil216.wlaashal.UI.Fragments.Home;
import com.nadernabil216.wlaashal.UI.Fragments.MainMenuFragment;
import com.nadernabil216.wlaashal.Utils.GMethods;

public class HomeActivity extends AppCompatActivity {

    private FragmentManager fm;
    //  private BottomNavigationView navigation;
    private BottomNavigationViewEx bnve;
    private FloatingActionMenu home_menu;
    private FloatingActionButton fab_delivery;
    private FloatingActionButton fab_taxi;
    private FloatingActionButton fab_advert;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (!item.isChecked()) {
                        Home home = new Home();
                        fm.beginTransaction().replace(R.id.home_frame, home).commit();
                    }

                    return true;
//                case R.id.navigation_search:
//
                case R.id.navigation_menu:
                    MainMenuFragment menuFragment = new MainMenuFragment();
                    fm.beginTransaction().replace(R.id.home_frame, menuFragment).addToBackStack("menu").commit();
                    return true;
//                case R.id.navigation_chat:
//                    Cart cart = new Cart();
//                    ft = fm.beginTransaction();
//                    ft.replace(R.id.frame, cart);
//                    ft.addToBackStack("cart");
//                    ft.commit();
//                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        GMethods.ChangeFont(this);

//        navigation = findViewById(R.id.home_navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        BottomNavigationViewHelper.disableShiftMode(navigation);

        bnve = (BottomNavigationViewEx) findViewById(R.id.home_navigation);
        bnve.enableAnimation(false);
        bnve.enableShiftingMode(false);
        bnve.enableItemShiftingMode(false);
        bnve.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bnve.setTextVisibility(false);


        fm = getSupportFragmentManager();
        fm.findFragmentById(R.id.home_frame);

        final Home home = new Home();
        fm.beginTransaction().replace(R.id.home_frame, home).commit();

        home_menu = findViewById(R.id.home_menu);
        fab_delivery = findViewById(R.id.delivery_fab);
        fab_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_menu.close(true);
                Intent intent = new Intent(HomeActivity.this, AddNewDeliveryOrTaxiService.class);
                intent.putExtra(GMethods.ADVERT_TYPE, 0);
                startActivity(intent);
            }
        });
        fab_taxi = findViewById(R.id.taxi_fab);
        fab_taxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_menu.close(true);
                Intent intent = new Intent(HomeActivity.this, AddNewDeliveryOrTaxiService.class);
                intent.putExtra(GMethods.ADVERT_TYPE, 1);
                startActivity(intent);
            }
        });
        fab_advert = findViewById(R.id.advert_fab);
        fab_advert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_menu.close(true);
                Intent intent = new Intent(HomeActivity.this, AddNewAdvertActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (fm.getBackStackEntryCount() - 1 > 0) {
            String fragmentTag = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 2).getName();
            if (fragmentTag.equals("menu")) {
                // navigation.getMenu().getItem(2).setChecked(true);
            }
            fm.popBackStack();
        } else {
            GMethods.show_alert_dialoug(this,
                    "هل تريد الخروج من التطبيق ؟",
                    "",
                    true,
                    "حسنا",
                    "الرجوع",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finishAffinity();
                            System.exit(0);
                        }
                    },
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

        }
    }
}
