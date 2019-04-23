package com.ozoon.ozoon;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ozoon.ozoon.Utils.GMethods;
import com.ozoon.ozoon.Utils.Helper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class RestRateAdapter extends RecyclerView.Adapter<RestRateAdapter.RateAdapter> {
    private Context mContext;
    private ArrayList<Review> data;
    int flag = 0;
    public RestRateAdapter(Context context) {
        mContext = context;
//        this.data=data;

    }

    public void updateData(ArrayList<Review> data){
        this.data=data;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public RestRateAdapter.RateAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RestRateAdapter.RateAdapter(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RestRateAdapter.RateAdapter holder, final int position) {
//        if (data==null || data.size()==0)
//            return;
        try{
            final Review review = data.get(position);
            holder.name.setText(review.getUser());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext,ProfileActivity.class).putExtra("user_id",review.getUserId()));
                }
            });
            holder.rating_bar.setRating(Float.parseFloat(review.getReview()));
            holder.desc.setText(review.getComment());
        }catch (Exception e){

        }

        if (flag == 1){
            holder.rating_bar.setVisibility(View.GONE);
            holder.desc.setVisibility(View.GONE);
        } else {
            holder.rating_bar.setVisibility(View.VISIBLE);
            holder.desc.setVisibility(View.VISIBLE);
        }

        Helper.writeToLog(data.get(position).getImage());

        if (!TextUtils.isEmpty(data.get(position).getImage())) {
            Picasso.with(mContext).load(GMethods.IMAGE_URL + data.get(position).getImage()).error(R.drawable.ic_person_grey_24dp).into(holder.imageView);
//
        } else {
            holder.imageView.setImageResource(R.drawable.ic_person_grey_24dp);
        }

    }

    @Override
    public int getItemCount() {
        if (data!=null)
            return data.size();

        return 0;
    }



    static class RateAdapter extends RecyclerView.ViewHolder {
        public TextView name;
        public RatingBar rating_bar;
        public ImageView imageView;
        public TextView desc;

        public RateAdapter(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_list_rate, parent, false));
            name = (TextView) itemView.findViewById(R.id.tv_client_name);
            rating_bar = (RatingBar) itemView.findViewById(R.id.rb_rate);
            desc = (TextView) itemView.findViewById(R.id.tv_rate);
            imageView = itemView.findViewById(R.id.imageView7);

        }

    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
