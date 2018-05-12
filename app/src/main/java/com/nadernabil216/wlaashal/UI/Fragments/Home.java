package com.nadernabil216.wlaashal.UI.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nadernabil216.wlaashal.Adapters.CategoriesAdapter;
import com.nadernabil216.wlaashal.Adapters.GridSpacingItemDecoration;
import com.nadernabil216.wlaashal.Model.Objects.Category;
import com.nadernabil216.wlaashal.Presenters.HomePresenter;
import com.nadernabil216.wlaashal.R;
import com.nadernabil216.wlaashal.UI.Activities.LoginActivity;
import com.nadernabil216.wlaashal.Utils.GMethods;
import com.nadernabil216.wlaashal.Utils.StorageUtil;

import java.util.ArrayList;

/**
 * Created by NaderNabil216@gmail.com on 5/9/2018.
 */
public class Home extends Fragment {
    ProgressBar progressbar;
    RecyclerView recyclerView;
    HomePresenter presenter;
    CategoriesAdapter adapter;
    StorageUtil util;
    RelativeLayout fake_toolbar_layout, user_info_layout;
    ImageView user_profile_image;
    TextView user_name , user_ads , user_points ;
    RatingBar user_rating ;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);
        presenter = new HomePresenter();
        util = StorageUtil.getInstance().doStuff(getActivity());
        GMethods.ChangeViewFont(view);
        InitViews(view);
        if (util.IsLogged()) {
            fake_toolbar_layout.setVisibility(View.GONE);
            user_info_layout.setVisibility(View.VISIBLE);
        } else {
            fake_toolbar_layout.setVisibility(View.VISIBLE);
            user_info_layout.setVisibility(View.GONE);
        }

        GetData();

        return view;
    }

    private void InitViews(View view) {
        fake_toolbar_layout=view.findViewById(R.id.fake_toolbar_layout);
        user_info_layout=view.findViewById(R.id.user_info_layout);
        user_profile_image=view.findViewById(R.id.user_profile_image);
        user_name=view.findViewById(R.id.user_name);
        user_ads=view.findViewById(R.id.tv_my_ads);
        user_points=view.findViewById(R.id.tv_my_points);
        user_rating=view.findViewById(R.id.rating_bar);

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
        ArrayList<Category> categories = new ArrayList<>();
        for (int i = 0; i < 18; i++) {
            categories.add(new Category("1", "http://www.endlessicons.com/wp-content/uploads/2012/11/home-icon.png", "الرئيسية"));
        }
        progressbar.setVisibility(View.GONE);
        SetData(categories);
    }

    private void SetData(ArrayList<Category> categories) {
        adapter = new CategoriesAdapter(getActivity(), categories);
        recyclerView.setAdapter(adapter);
    }

}
