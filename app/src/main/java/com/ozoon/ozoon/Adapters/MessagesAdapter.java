package com.ozoon.ozoon.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ozoon.ozoon.Model.Objects.ChatModel;
import com.ozoon.ozoon.R;
import com.ozoon.ozoon.Utils.GMethods;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageHolder> {


    private Context mContext;

    private ArrayList<ChatModel> items;

    private OnItemClick onItemClick;

    public MessagesAdapter(Context mContext, ArrayList<ChatModel> items, OnItemClick onItemClick) {
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
                .inflate(R.layout.chat_item, parent, false);
        GMethods.ChangeViewFont(view);
        return new MessageHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MessageHolder holder, int position) {
        holder.connect.setText(items.get(position).getEmail());
        holder.message.setText(items.get(position).getLastMessage());
        holder.name.setText(items.get(position).getName());
        holder.itemView.setTag(items.get(position).getChatId());
        if (!TextUtils.isEmpty(items.get(position).getImage()))
            Picasso.with(mContext).load(items.get(position).getImage()).error(R.drawable.ic_dummy_person).fit().into(holder.image);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    public class MessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.connect)
        TextView connect;
        @BindView(R.id.message)
        TextView message;
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
