package com.ozoon.ozoon.UI.Fragments;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ozoon.ozoon.Adapters.CategoriesAdapter;
import com.ozoon.ozoon.Adapters.GridSpacingItemDecoration;
import com.ozoon.ozoon.CallBacks.AllCategoriesCallBack;
import com.ozoon.ozoon.CallBacks.LoginCallBack;
import com.ozoon.ozoon.Model.Objects.Category;
import com.ozoon.ozoon.Model.Objects.User;
import com.ozoon.ozoon.NetworkUtils;
import com.ozoon.ozoon.Presenters.MainPresenter;
import com.ozoon.ozoon.ProfileActivity;
import com.ozoon.ozoon.R;
import com.ozoon.ozoon.ShowFollowersDialog;
import com.ozoon.ozoon.ShowRatingDialog;
import com.ozoon.ozoon.UI.Activities.HomeActivity;
import com.ozoon.ozoon.UI.Activities.LoginActivity;
import com.ozoon.ozoon.UI.Activities.SettingsActivity;
import com.ozoon.ozoon.Utils.GMethods;
import com.ozoon.ozoon.Utils.Helper;
import com.ozoon.ozoon.Utils.StorageUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NaderNabil216@gmail.com on 5/9/2018.
 */
public class Home extends Fragment {
    ProgressBar progressbar;
    RecyclerView recyclerView;
    MainPresenter presenter;
    CategoriesAdapter adapter;
    StorageUtil util;
    RelativeLayout fake_toolbar_layout, user_info_layout;
    ImageView user_profile_image;
    TextView user_name, user_ads, user_points,my_followers,chat;
    RatingBar user_rating;
    View numOfAds,numOfPoints,numOfFollowers;
    @BindView(R.id.swipyrefreshlayout)
    SwipeRefreshLayout mSwipyRefreshLayout;
    SharedPreferences preferences;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);
        ButterKnife.bind(this,view);
        presenter = new MainPresenter();
        util = StorageUtil.getInstance().doStuff(getContext());
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        Helper.writeToLog(util.getToken());
        GMethods.ChangeViewFont(view);
        InitViews(view);
        if (util.IsLogged()) {
            fake_toolbar_layout.setVisibility(View.GONE);
            user_info_layout.setVisibility(View.VISIBLE);
            presenter.getUser(util.GetCurrentUser().getId(), util.GetCurrentUser().getId(), new LoginCallBack() {
                @Override
                public void OnSuccess(User user) {
                    my_followers.setText(user.getFollower()+"");
                    user_rating.setRating(Float.parseFloat(user.getRate()));
                    user_points.setText(user.getPoints());
                }

                @Override
                public void OnFailure(String message) {

                }

                @Override
                public void OnServerError() {

                }
            });
        } else {
            fake_toolbar_layout.setVisibility(View.VISIBLE);
            user_info_layout.setVisibility(View.GONE);
        }

        GetData();


        mSwipyRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (util.IsLogged()) {
                    fake_toolbar_layout.setVisibility(View.GONE);
                    user_info_layout.setVisibility(View.VISIBLE);
                    presenter.getUser(util.GetCurrentUser().getId(), util.GetCurrentUser().getId(), new LoginCallBack() {
                        @Override
                        public void OnSuccess(User user) {
                            my_followers.setText(user.getFollower()+"");
                            user_rating.setRating(Float.parseFloat(user.getRate()));
                            user_points.setText(user.getPoints());
                            util.SetCurrentUser(user);
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                            SharedPreferences.Editor editor ;
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
                        }

                        @Override
                        public void OnFailure(String message) {

                        }

                        @Override
                        public void OnServerError() {

                        }
                    });
                } else {
                    fake_toolbar_layout.setVisibility(View.VISIBLE);
                    user_info_layout.setVisibility(View.GONE);
                }

                GetData();
            }
        });



        return view;
    }

    private void InitViews(View view) {
        fake_toolbar_layout = view.findViewById(R.id.fake_toolbar_layout);
        user_info_layout = view.findViewById(R.id.user_info_layout);
        user_profile_image = view.findViewById(R.id.user_profile_image);
        user_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null)
                    getActivity().startActivity(new Intent(getActivity(), SettingsActivity.class));
            }
        });
        user_name = view.findViewById(R.id.user_name);
        user_ads = view.findViewById(R.id.tv_my_ads);
        user_points = view.findViewById(R.id.tv_my_points);
        user_rating = view.findViewById(R.id.rating_bar);
        chat = view.findViewById(R.id.txt_title);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!util.IsLogged()) {

                    //Toast.makeText(getActivity(),"انت غير مسجل",Toast.LENGTH_LONG).show();
                    if (getContext() != null) {
                        Helper.showSnackBarMessage("انت غير مسجل", (AppCompatActivity) getContext());
                    }
                } else {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_frame, new ChatFragment()).commit();
                    ((HomeActivity)getActivity()).home = false;
                }
            }
        });
        user_rating.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (util.GetCurrentUser() != null)
                    showClientsRatingDialog(util.GetCurrentUser().getId());
                return false;
            }
        });
        numOfFollowers = view.findViewById(R.id.num_of_followers_home_parent);
        my_followers = view.findViewById(R.id.my_followers_home);
        numOfFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (util.IsLogged()) {
                    startActivity(new Intent(getActivity(), ProfileActivity.class).putExtra("user_id",util.GetCurrentUser().getId()));
                } else {
                    //Toast.makeText(getActivity(), "من فضلك قم بتسجيل الدخول", Toast.LENGTH_LONG).show();
                    if (getContext() != null) {
                        Helper.showSnackBarMessage("من فضلك قم بتسجيل الدخول", (AppCompatActivity) getContext());
                    }
                }*/
                if (util.GetCurrentUser() != null)
                    showFollowersDialog(util.GetCurrentUser().getId());
            }
        });
        numOfAds = view.findViewById(R.id.num_of_ads_parent);
        numOfPoints  = view.findViewById(R.id.num_of_points_parent);
        numOfPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (util.IsLogged()) {
                    startActivity(new Intent(getActivity(), ProfileActivity.class).putExtra("user_id",util.GetCurrentUser().getId()));
                } else {
                    //Toast.makeText(getActivity(), "من فضلك قم بتسجيل الدخول", Toast.LENGTH_LONG).show();
                    if (getContext() != null) {
                        Helper.showSnackBarMessage("من فضلك قم بتسجيل الدخول", (AppCompatActivity) getContext());
                    }
                }
            }
        });

        numOfAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (util.IsLogged()) {
                    startActivity(new Intent(getActivity(), ProfileActivity.class).putExtra("user_id",util.GetCurrentUser().getId()));
                } else {
                    //Toast.makeText(getActivity(), "من فضلك قم بتسجيل الدخول", Toast.LENGTH_LONG).show();
                    if (getContext() != null) {
                        Helper.showSnackBarMessage("من فضلك قم بتسجيل الدخول", (AppCompatActivity) getContext());
                    }
                }
            }
        });

        progressbar = view.findViewById(R.id.progressbar);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(getActivity(), 3, true));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        fake_toolbar_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
    }

    private void GetData() {
        String str_id="";
        if(util.IsLogged()){
            str_id =  util.GetCurrentUser().getId() ;
        }
        presenter.GetAllCategories(str_id, new AllCategoriesCallBack() {
            @Override
            public void OnSuccess(ArrayList<Category> categories) {
                Helper.writeToLog("Refreshed");
                mSwipyRefreshLayout.setRefreshing(false);
                ArrayList<Category> categories_list = new ArrayList<>();
                //add two items of taxi and delivery
                categories_list.add(new Category());
                categories_list.add(new Category());
                categories_list.add(new Category());
                categories_list.addAll(categories);
                SetData(categories_list);
                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void OnFailure(String message) {
                mSwipyRefreshLayout.setRefreshing(false);
                GMethods.show_alert_dialoug(getActivity(),
                        "خطأ من فضلك اعد المحاوله",
                        getString(R.string.app_name),
                        true,
                        "",
                        "",
                        null,
                        null);
                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void OnServerError() {
                mSwipyRefreshLayout.setRefreshing(false);
                GMethods.show_alert_dialoug(getActivity(),
                        getString(R.string.server_error),
                        getString(R.string.app_name),
                        true,
                        "",
                        "",
                        null,
                        null);
                progressbar.setVisibility(View.GONE);
            }
        });
    }

    private void SetData(ArrayList<Category> categories) {
        adapter = new CategoriesAdapter(getActivity(), categories);
        recyclerView.setAdapter(adapter);
        if(util.IsLogged()){
            User user = util.GetCurrentUser();
            GMethods.writeToLog(user.getImage());
            Picasso.with(getActivity()).load(GMethods.IMAGE_URL + user.getImage()).placeholder(R.drawable.ic_dummy_person).error(R.drawable.ic_dummy_person).into(user_profile_image);
            user_name.setText(user.getName());
            //user_rating.setRating(Float.valueOf(user.getRate()));
            if (getActivity() != null) {
                user_ads.setText(StorageUtil.getAdsCount(getActivity()));
            }
            //user_points.setText(user.getPoints());
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        if(util.IsLogged()){
            User user = util.GetCurrentUser();
            GMethods.writeToLog(user.getImage());
            Picasso.with(getActivity()).load(GMethods.IMAGE_URL + user.getImage()).placeholder(R.drawable.ic_dummy_person).error(R.drawable.ic_dummy_person).into(user_profile_image);
            user_name.setText(user.getName());
            //user_rating.setRating(Float.valueOf(user.getRate()));
            user_ads.setText(StorageUtil.getAdsCount(getActivity()));
            //user_points.setText(user.getPoints());
            Helper.writeToLog(util.GetCurrentUser().getAds_count()+"");
        }

        if (util.IsLogged()) {
            fake_toolbar_layout.setVisibility(View.GONE);
            user_info_layout.setVisibility(View.VISIBLE);
            presenter.getUser(util.GetCurrentUser().getId(), util.GetCurrentUser().getId(), new LoginCallBack() {
                @Override
                public void OnSuccess(User user) {
                    my_followers.setText(user.getFollower()+"");
                    user_rating.setRating(Float.parseFloat(user.getRate()));
                    user_points.setText(user.getPoints());
                    util.SetCurrentUser(user);
                   SharedPreferences.Editor editor ;
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
                }

                @Override
                public void OnFailure(String message) {

                }

                @Override
                public void OnServerError() {

                }
            });
        } else {
            fake_toolbar_layout.setVisibility(View.VISIBLE);
            user_info_layout.setVisibility(View.GONE);
        }

        GetData();


        if (util.getDelivery()) {
            HomeActivity.fab_delivery.setVisibility(View.GONE);
        } else {
            //fab_delivery.setVisibility(View.VISIBLE);
        }


        if (util.getTaxi()) {
            HomeActivity.fab_taxi.setVisibility(View.GONE);
        } else {
            //fab_taxi.setVisibility(View.VISIBLE);
        }
    }


    public void showClientsRatingDialog(String id) {
        if (getContext() != null) {
            if (!NetworkUtils.isNetworkConnected(getContext())) {
                GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا واعد المحاولة", (AppCompatActivity) getContext());
                return;
            }
        }

        if (getActivity() != null) {
            FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
            android.app.Fragment prev = getActivity().getFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            // Create and show the dialog.
            ShowRatingDialog newFragment = new ShowRatingDialog();
            newFragment.setProduct_id(id);

            newFragment.show(getActivity().getSupportFragmentManager(), "");
        }
    }


    public void showFollowersDialog(String id) {

        if (!NetworkUtils.isNetworkConnected(getContext())) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا واعد المحاولة",(AppCompatActivity)getActivity());
            return ;
        }

        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        android.app.Fragment prev = getActivity().getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        ShowFollowersDialog newFragment = new ShowFollowersDialog();
        newFragment.setProduct_id(id);

        newFragment.show(getActivity().getSupportFragmentManager(), "");
    }




}
