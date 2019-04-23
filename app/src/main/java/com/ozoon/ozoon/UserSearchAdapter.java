package com.ozoon.ozoon;

import android.content.Context;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchAdapter.MessageHolder> {

    private Context mContext;

    private ArrayList<UserModelSearch> items;

    private OnItemClick onItemClick;

    public UserSearchAdapter(Context mContext, ArrayList<UserModelSearch> items, OnItemClick onItemClick) {
        this.mContext = mContext;
        this.items = items;
        this.onItemClick = onItemClick;
    }


    public interface OnItemClick {
        void setOnItemClick(int position);
    }


    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        GMethods.ChangeViewFont(view);
        return new MessageHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MessageHolder holder, int position) {
        holder.ratingBar.setRating(Float.parseFloat(items.get(position).getRate()));
        holder.name.setText(items.get(position).getName());
        if (!TextUtils.isEmpty(items.get(position).getImage()))
            Picasso.with(mContext).load(items.get(position).getImage()).error(R.drawable.ic_dummy_person).fit().into(holder.image);
        else
            holder.image.setImageResource(R.drawable.ic_dummy_person);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }



    public class MessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.rating_bar)
        RatingBar ratingBar;
        @BindView(R.id.image)
        ImageView image;

        public MessageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            onItemClick.setOnItemClick(position);
        }
    }

}
