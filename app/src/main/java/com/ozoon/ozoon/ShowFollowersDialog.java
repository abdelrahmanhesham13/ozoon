package com.ozoon.ozoon;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ozoon.ozoon.Utils.GMethods;

import java.util.ArrayList;

public class ShowFollowersDialog extends AppCompatDialogFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Button rate;
    String product_id;
    RestRateAdapter adapter ;
    ImageView close;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_clients_rating, container, false);
        adapter =new RestRateAdapter(getContext());
        adapter.setFlag(1);
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.recyclerview);
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        ((TextView)v.findViewById(R.id.title)).setText("المتابعين");
        progressDialog=new ProgressDialog(getContext());
        loadData();

        close = v.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowFollowersDialog.this.dismiss();
            }
        });


        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // Do all the stuff to initialize your custom view

        return v;
    }

    private void updateData(ArrayList<Review> data){
        adapter.updateData(data);
    }
    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    private void loadData(){


        if (!NetworkUtils.isNetworkConnected(getContext())) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا واعد المحاوله",(AppCompatActivity) getActivity());
            return ;
        }

        showLoading();

        try {

            Service.getFollowers(product_id,getContext(), new RequestCallBack() {
                @Override
                public void success(String response) {
                    try {
                        Gson gson = new Gson();
                        BaseReview baseReview = gson.fromJson(response,BaseReview.class);

                        Log.i("status", baseReview.getStatus().toString());
//                        updateData(clientProducts);

                        if (baseReview.getStatus()){
                            updateData((ArrayList<Review>)baseReview.getReviews());
                            dismissLoading();

                        }else {
                            GMethods.showSnackBarMessage("لا يوجد تقييمات بعد",(AppCompatActivity) getActivity());
                            dismissLoading();
                        }

                        dismissLoading();
                    }catch (Exception e){
                        Log.e("EX" ,e.toString());
                        dismissLoading();
//
                    }

                }

                @Override
                public void error(Exception exc) {
                    dismissLoading();
                }
            });

        }catch (Exception e){
            GMethods.showSnackBarMessage("حدث خطأ حاول مره اخري",(AppCompatActivity) getActivity());

        }

    }

    ProgressDialog progressDialog;
    private void showLoading() {
        progressDialog.setMessage("جاري التحميل");
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.setProgress(0);
//        progressDialog.setMax(100);
        progressDialog.show();
//        progressBar.setVisibility(View.VISIBLE);
    }

    private void dismissLoading() {
        progressDialog.dismiss();
//        progressBar.setVisibility(View.INVISIBLE);

    }







}