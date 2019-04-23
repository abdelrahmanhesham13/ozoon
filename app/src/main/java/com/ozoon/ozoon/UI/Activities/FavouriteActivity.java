package com.ozoon.ozoon.UI.Activities;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.ozoon.ozoon.Adapters.CategoryAdsAdapter;
import com.ozoon.ozoon.Adapters.GridSpacingItemDecoration;
import com.ozoon.ozoon.CallBacks.AllProductsCallBack;
import com.ozoon.ozoon.Model.Objects.Product;
import com.ozoon.ozoon.Presenters.MainPresenter;
import com.ozoon.ozoon.R;
import com.ozoon.ozoon.Utils.GMethods;
import com.ozoon.ozoon.Utils.GPSTracker;
import com.ozoon.ozoon.Utils.Helper;
import com.ozoon.ozoon.Utils.StorageUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavouriteActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MainPresenter presenter;
    ProgressDialog progressDialog;
    CategoryAdsAdapter adapter;
    StorageUtil util;
    @BindView(R.id.swipyrefreshlayout)
    SwipeRefreshLayout mSwipyRefreshLayout;
    @BindView(R.id.back_btn)
    ImageView back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        ButterKnife.bind(this);
        InitViews();
        util = StorageUtil.getInstance().doStuff(this);
        presenter = new MainPresenter();
        GetData();

        mSwipyRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetData();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void InitViews() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("جاري جلب البيانات");

        recyclerView = findViewById(R.id.recyclerView);

    }

    private void GetData() {
        progressDialog.show();
        presenter.GetFavourite(util.GetCurrentUser().getId(), new AllProductsCallBack() {
            @Override
            public void OnSuccess(ArrayList<Product> products) {
                mSwipyRefreshLayout.setRefreshing(false);
                progressDialog.dismiss();
                SetData(products);
            }

            @Override
            public void OnFailure(String message) {
                mSwipyRefreshLayout.setRefreshing(false);
                progressDialog.dismiss();
                GMethods.show_alert_dialoug(FavouriteActivity.this,
                        "لا يوجد اعلانات",
                        getString(R.string.app_name),
                        true,
                        "",
                        "",
                        null,
                        null);
            }

            @Override
            public void OnServerError() {
                mSwipyRefreshLayout.setRefreshing(false);
                progressDialog.dismiss();
                GMethods.show_alert_dialoug(FavouriteActivity.this,
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

    private void SetData(ArrayList<Product> products) {
        adapter = new CategoryAdsAdapter(products,this,20);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }
}
