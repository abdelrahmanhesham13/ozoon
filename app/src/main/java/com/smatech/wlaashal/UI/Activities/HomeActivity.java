package com.smatech.wlaashal.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.smatech.wlaashal.CallBacks.LoginCallBack;
import com.smatech.wlaashal.Model.Objects.ChatModel;
import com.smatech.wlaashal.Model.Objects.User;
import com.smatech.wlaashal.NotificationFragment;
import com.smatech.wlaashal.Presenters.LoginPresenter;
import com.smatech.wlaashal.R;
import com.smatech.wlaashal.SearchFragment;
import com.smatech.wlaashal.UI.Fragments.ChatFragment;
import com.smatech.wlaashal.UI.Fragments.Home;
import com.smatech.wlaashal.UI.Fragments.MainMenuFragment;
import com.smatech.wlaashal.UI.Fragments.MessagesFragment;
import com.smatech.wlaashal.Utils.GMethods;
import com.smatech.wlaashal.Utils.Helper;
import com.smatech.wlaashal.Utils.StorageUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.leolin.shortcutbadger.ShortcutBadger;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class HomeActivity extends AppCompatActivity {


    @BindView(R.id.chat_floating_btn)
    FloatingActionButton chatBtn;
    private FragmentManager fm;
    //  private BottomNavigationView navigation;
    public BottomNavigationViewEx bnve;
    public FloatingActionMenu home_menu;
    private FloatingActionButton fab_delivery;
    private FloatingActionButton fab_taxi;
    private FloatingActionButton fab_advert;
    StorageUtil util;
    public Badge badge;


    LoginPresenter presenter;
    public boolean home = true;

    ChatModel chatModel;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    if (true) {
                        home = true;
                        home_menu.setVisibility(View.VISIBLE);
                        Home home = new Home();
                        fm.beginTransaction().replace(R.id.home_frame, home).commit();
                        if (!util.IsLogged()) {
                            home_menu.setVisibility(View.GONE);
                        } else {
                            home_menu.setVisibility(View.VISIBLE);
                        }
                    }
                    return true;
                case R.id.navigation_search:

                    home_menu.setVisibility(View.GONE);
                    SearchFragment searchFragment = new SearchFragment();
                    fm.beginTransaction().replace(R.id.home_frame, searchFragment, "Menu").addToBackStack("menu").commit();
                    return true;
                case R.id.navigation_menu:
                    home_menu.setVisibility(View.GONE);
                    MainMenuFragment menuFragment = new MainMenuFragment();
                    fm.beginTransaction().replace(R.id.home_frame, menuFragment, "Menu").addToBackStack("menu").commit();
                    return true;
                case R.id.navigation_chat:
                    if (!util.IsLogged()) {

                        //Toast.makeText(HomeActivity.this, "انت غير مسجل", Toast.LENGTH_LONG).show();
                        Helper.showSnackBarMessage("انت غير مسجل",HomeActivity.this);
                    } else {
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
                        preferences.edit().remove("appNotifications").remove("notificationsCount").apply();
                        ShortcutBadger.removeCount(HomeActivity.this);
                        if (badge != null) {
                            badge.hide(false);
                        }
                        home_menu.setVisibility(View.GONE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.home_frame, new NotificationFragment(), "Notification").commit();
                        home = false;
                    }
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        GMethods.ChangeFont(this);
        presenter = new LoginPresenter();

        home_menu = findViewById(R.id.home_menu);
        fab_delivery = findViewById(R.id.delivery_fab);

        util = StorageUtil.getInstance().doStuff(HomeActivity.this);

        fm = getSupportFragmentManager();

//        navigation = findViewById(R.id.home_navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        BottomNavigationViewHelper.disableShiftMode(navigation);

        bnve = (BottomNavigationViewEx) findViewById(R.id.home_navigation);
        bnve.enableAnimation(false);
        bnve.enableShiftingMode(false);
        bnve.enableItemShiftingMode(false);
        bnve.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bnve.setTextVisibility(false);

        for (int i = 0; i < bnve.getItemCount(); i++) {
            bnve.setIconTintList(i, null);
        }

        if (getIntent().hasExtra("chat")) {
            chatModel = (ChatModel) getIntent().getSerializableExtra("chat");
            this.home = false;
            bnve.setCurrentItem(3);
            //findViewById(R.id.receive_name).setVisibility(View.VISIBLE);
            //mToggle.setDrawerIndicatorEnabled(false);
            MessagesFragment fragment = new MessagesFragment();
            Bundle bundle = new Bundle();
            home_menu.setVisibility(View.GONE);
            bundle.putSerializable("chat", chatModel);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.home_frame, fragment).commit();

        } else if (getIntent().hasExtra("goToChat")) {
            if (!util.IsLogged()) {

                //Toast.makeText(HomeActivity.this, "انت غير مسجل", Toast.LENGTH_LONG).show();
                Helper.showSnackBarMessage("انت غير مسجل",HomeActivity.this);
            } else {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
                preferences.edit().remove("appNotifications").apply();
                ShortcutBadger.removeCount(HomeActivity.this);
                home_menu.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.home_frame, new ChatFragment()).commit();
                home = false;
            }
        } else if (getIntent().hasExtra("goToNotification")) {
            if (!util.IsLogged()) {

                //Toast.makeText(HomeActivity.this, "انت غير مسجل", Toast.LENGTH_LONG).show();
                Helper.showSnackBarMessage("انت غير مسجل",HomeActivity.this);
            } else {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
                preferences.edit().remove("appNotifications").remove("notificationsCount").apply();
                ShortcutBadger.removeCount(HomeActivity.this);
                if (badge != null) {
                    badge.hide(false);
                }
                home_menu.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.home_frame, new NotificationFragment()).commit();
                home = false;
                bnve.setCurrentItem(3);
            }
        } else {


            fm = getSupportFragmentManager();
            fm.findFragmentById(R.id.home_frame);

            final Home home = new Home();
            fm.beginTransaction().replace(R.id.home_frame, home).commit();

        }

        fab_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_menu.close(false);
                if (util.IsLogged()) {
                    home_menu.close(true);
                    Intent intent = new Intent(HomeActivity.this, AddNewDeliveryOrTaxiService.class);
                    intent.putExtra(GMethods.ADVERT_TYPE, 0);
                    startActivity(intent);
                } else {
                    //Toast.makeText(HomeActivity.this, "من فضلك قم بتسجيل الدخول", Toast.LENGTH_SHORT).show();
                    Helper.showSnackBarMessage("من فضلك قم بتسجيل الدخول",HomeActivity.this);
                }
            }
        });

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_menu.close(false);
                if (!util.IsLogged()) {

                    //Toast.makeText(HomeActivity.this, "انت غير مسجل", Toast.LENGTH_LONG).show();
                    Helper.showSnackBarMessage("انت غير مسجل",HomeActivity.this);
                } else {
                    home_menu.close(true);
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
                    preferences.edit().remove("appNotifications").apply();
                    ShortcutBadger.removeCount(HomeActivity.this);
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_frame, new ChatFragment()).commit();
                    home = false;
                    home_menu.setVisibility(View.GONE);
                }
            }
        });
        fab_taxi = findViewById(R.id.taxi_fab);
        fab_taxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_menu.close(false);
                if (util.IsLogged()) {
                    home_menu.close(true);
                    Intent intent = new Intent(HomeActivity.this, AddNewDeliveryOrTaxiService.class);
                    intent.putExtra(GMethods.ADVERT_TYPE, 1);
                    startActivity(intent);
                } else {
                    //Toast.makeText(HomeActivity.this, "من فضلك قم بتسجيل الدخول", Toast.LENGTH_SHORT).show();
                    Helper.showSnackBarMessage("من فضلك قم بتسجيل الدخول",HomeActivity.this);
                }
            }
        });
        fab_advert = findViewById(R.id.advert_fab);
        fab_advert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_menu.close(false);
                if (util.IsLogged()) {
                    home_menu.close(true);
                    Intent intent = new Intent(HomeActivity.this, AddNewAdvertActivity.class);
                    startActivity(intent);
                } else {
                    //Toast.makeText(HomeActivity.this, "من فضلك قم بتسجيل الدخول", Toast.LENGTH_SHORT).show();
                    Helper.showSnackBarMessage("من فضلك قم بتسجيل الدخول",HomeActivity.this);
                }
            }
        });

        if (util.IsLogged() && !getIntent().hasExtra("chat")) {
            home_menu.setVisibility(View.VISIBLE);
        } else {
            home_menu.setVisibility(View.GONE);
        }


        if (!util.IsLogged()) {
            home_menu.setVisibility(View.GONE);
        }

        if (util.getDelivery()) {
            fab_delivery.setVisibility(View.GONE);
        } else {
            //fab_delivery.setVisibility(View.VISIBLE);
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().remove("appNotifications").apply();
        ShortcutBadger.removeCount(this);




        badge = addBadgeAt(3,Integer.valueOf(preferences.getString("notificationsCount","0")));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (util.getDelivery()) {
            fab_delivery.setVisibility(View.GONE);
        } else {
            //fab_delivery.setVisibility(View.VISIBLE);
        }


        if (util.getTaxi()) {
            fab_taxi.setVisibility(View.GONE);
        } else {
            //fab_taxi.setVisibility(View.VISIBLE);
        }

        if (!util.IsLogged()) {
            home_menu.setVisibility(View.GONE);
        } else {
            home_menu.setVisibility(View.VISIBLE);
        }

        Fragment fragment = getSupportFragmentManager().findFragmentByTag("Notification");
        if (fragment != null && fragment.isVisible()) {
            home_menu.setVisibility(View.GONE);
        }

        Fragment fragment1 = getSupportFragmentManager().findFragmentByTag("Menu");
        if (fragment1 != null && fragment1.isVisible()) {
            home_menu.setVisibility(View.GONE);
        }


        if (util.getTaxi()) {
            fab_taxi.setVisibility(View.GONE);
        } else {
            //fab_taxi.setVisibility(View.VISIBLE);
        }


        if (getIntent().hasExtra("goToNotification") || getIntent().hasExtra("chat")) {
            home_menu.setVisibility(View.GONE);
        }

        home_menu.close(false);

    }


    @Override
    protected void onStart() {
        super.onStart();
        home_menu.close(false);
        if (util.getDelivery()) {
            fab_delivery.setVisibility(View.GONE);
        } else {
            //fab_delivery.setVisibility(View.VISIBLE);
        }


        if (!util.IsLogged()) {
            home_menu.setVisibility(View.GONE);
        } else {
            home_menu.setVisibility(View.VISIBLE);
        }


        if (util.getTaxi()) {
            fab_taxi.setVisibility(View.GONE);
        } else {
            //fab_taxi.setVisibility(View.VISIBLE);
        }

        Fragment fragment = getSupportFragmentManager().findFragmentByTag("Notification");
        if (fragment != null && fragment.isVisible()) {
            home_menu.setVisibility(View.GONE);
        }

        Fragment fragment1 = getSupportFragmentManager().findFragmentByTag("Menu");
        if (fragment1 != null && fragment1.isVisible()) {
            home_menu.setVisibility(View.GONE);
        }


        if (getIntent().hasExtra("goToNotification") || getIntent().hasExtra("chat")) {
            home_menu.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (util.getDelivery()) {
            fab_delivery.setVisibility(View.GONE);
        } else {
            //fab_delivery.setVisibility(View.VISIBLE);
        }


        home_menu.close(false);
        if (util.getDelivery()) {
            fab_delivery.setVisibility(View.GONE);
        } else {
            //fab_delivery.setVisibility(View.VISIBLE);
        }


        if (!util.IsLogged()) {
            home_menu.setVisibility(View.GONE);
        } else {
            home_menu.setVisibility(View.VISIBLE);
        }


        if (util.getTaxi()) {
            fab_taxi.setVisibility(View.GONE);
        } else {
            //fab_taxi.setVisibility(View.VISIBLE);
        }


        if (getIntent().hasExtra("goToNotification") || getIntent().hasExtra("chat")) {
            home_menu.setVisibility(View.GONE);
        }


        if (util.getTaxi()) {
            fab_taxi.setVisibility(View.GONE);
        } else {
            //fab_taxi.setVisibility(View.VISIBLE);
        }


        Fragment fragment = getSupportFragmentManager().findFragmentByTag("Notification");
        if (fragment != null && fragment.isVisible()) {
            home_menu.setVisibility(View.GONE);
        }

        Fragment fragment1 = getSupportFragmentManager().findFragmentByTag("Menu");
        if (fragment1 != null && fragment1.isVisible()) {
            home_menu.setVisibility(View.GONE);
        }


        getIntent().removeExtra("goToChat");
        getIntent().removeExtra("goToNotification");
        getIntent().removeExtra("chat");

    }

    @Override
    public void onBackPressed() {
        if (!home) {
            if (!util.IsLogged()) {

                //Toast.makeText(HomeActivity.this, "انت غير مسجل", Toast.LENGTH_LONG).show();
                Helper.showSnackBarMessage("انت غير مسجل",HomeActivity.this);
            } else {
                home_menu.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.home_frame, new Home()).commit();
                bnve.setCurrentItem(0);
                home = true;
            }
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


    private Badge addBadgeAt(int position, int number) {
        // add badge
        return new QBadgeView(this)
                .setBadgeNumber(number)
                .setGravityOffset(19, 2, true)
                .bindTarget(bnve.getBottomNavigationItemView(position));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                if (result.equals("done")){
                    Helper.showSnackBarMessageTimed("تم الطلب ويمكن متابعة طلبكم عبر الاشعارات",HomeActivity.this);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        } else if (requestCode == 50){
            if (data.hasExtra("chat")){
                getIntent().putExtra("chat","chat");
            }
        }
    }
}
