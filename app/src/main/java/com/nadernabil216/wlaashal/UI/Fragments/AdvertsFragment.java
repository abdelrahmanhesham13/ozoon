package com.nadernabil216.wlaashal.UI.Fragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.nadernabil216.wlaashal.Adapters.CategoryAdsAdapter;
import com.nadernabil216.wlaashal.Adapters.GridSpacingItemDecoration;
import com.nadernabil216.wlaashal.CallBacks.AllProductsCallBack;
import com.nadernabil216.wlaashal.CallBacks.GetSubCategoriesCallBack;
import com.nadernabil216.wlaashal.Model.Objects.Product;
import com.nadernabil216.wlaashal.Model.Objects.SubCategory;
import com.nadernabil216.wlaashal.Presenters.MainPresenter;
import com.nadernabil216.wlaashal.R;
import com.nadernabil216.wlaashal.Utils.GMethods;

import java.util.ArrayList;

/**
 * Created by NaderNabil216@gmail.com on 5/16/2018.
 */
public class AdvertsFragment extends Fragment {
    String Category_id = "";
    BroadcastReceiver broadcastReceiver;
    RecyclerView recyclerView;
    MainPresenter presenter;
    ProgressDialog progressDialog;
    CategoryAdsAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_ads, container, false);
        Category_id = getArguments().getString(GMethods.CATEGORY_ID);
        presenter = new MainPresenter();
        InitViews(view);
        InitBroadCast();
        GetData(1, Category_id);
        return view;
    }

    private void InitViews(View view) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("جاري جلب البيانات");

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(getActivity(), 2, true));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    /**
     * @param param action whether be category id or sub category id
     * @param type  1 = category_id
     *              2 = sub category id
     */
    private void GetData(int type, String param) {
        progressDialog.show();
        presenter.GetAllProducts(type, param, new AllProductsCallBack() {
            @Override
            public void OnSuccess(ArrayList<Product> products) {
                progressDialog.dismiss();
                SetData(products);
            }

            @Override
            public void OnFailure(String message) {
                progressDialog.dismiss();
                GMethods.show_alert_dialoug(getActivity(),
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
                GMethods.show_alert_dialoug(getActivity(),
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
        adapter = new CategoryAdsAdapter(products, getActivity());
        recyclerView.setAdapter(adapter);
    }

    private void InitBroadCast() {
        IntentFilter intentFilter = new IntentFilter(GMethods.ACTION_UPDATE_DATA);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getStringExtra(GMethods.ACTION_TYPE);
                if (action == GMethods.PERFORME_SUBCATEGORY) {
                    GetSubCategories();
                } else if (action == GMethods.PERFORME_HIGHEST_RATE) {

                } else if (action == GMethods.PERFORME_NEAREST) {

                }
            }
        };

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, intentFilter);
    }

    private void GetSubCategories() {
        progressDialog.show();
        presenter.GetSubCategories(Category_id, new GetSubCategoriesCallBack() {
            @Override
            public void OnSuccess(ArrayList<SubCategory> subCategories) {
                progressDialog.dismiss();
                BuildSubCategoryDialog(subCategories);
            }

            @Override
            public void OnFailure(String message) {
                progressDialog.dismiss();
                GMethods.show_alert_dialoug(getActivity(),
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
                GMethods.show_alert_dialoug(getActivity(),
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

    private void BuildSubCategoryDialog(final ArrayList<SubCategory> subCategories) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());

        ArrayList<String> names = new ArrayList<>();
        for (SubCategory subCategory : subCategories) {
            names.add(subCategory.getName());
        }
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_singlechoice, names);

        builderSingle.setNegativeButton("رجوع", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SubCategory subCategory = subCategories.get(which);
                GetData(2, subCategory.getId());
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
    }
}
