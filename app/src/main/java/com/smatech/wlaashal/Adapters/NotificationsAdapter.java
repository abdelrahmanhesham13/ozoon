package com.smatech.wlaashal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smatech.wlaashal.Model.Objects.Notification;
import com.smatech.wlaashal.Presenters.NotificationsPresenter;
import com.smatech.wlaashal.ProfileActivity;
import com.smatech.wlaashal.R;
import com.smatech.wlaashal.Utils.GMethods;
import com.smatech.wlaashal.Utils.StorageUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by NaderNabil216@gmail.com on 5/12/2018.
 */
public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    ArrayList<Notification> notifications;
    Context context;
    NotificationsPresenter presenter;
    OnItemClick onItemClick;
    StorageUtil util;
    int flag ;

    public NotificationsAdapter(ArrayList<Notification> notifications, Context context,OnItemClick onItemClick,int flag) {
        this.notifications = notifications;
        this.context = context;
        this.onItemClick = onItemClick;
        this.flag = flag;
        util = StorageUtil.getInstance().doStuff(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
        GMethods.ChangeViewFont(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        /*final Notification notification = notifications.get(position);
        Picasso.with(context).load(notification.getUser_image()).into(holder.profile_pic);
        holder.user_name.setText(notification.getUser_name());
        holder.body.setText(notification.getBody());
        holder.date.setText(notification.getDate());*/
        holder.date.setText(notifications.get(position).getDate());
        holder.user_name.setText(notifications.get(position).getUser());
        holder.itemView.setTag(position);
        holder.profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ProfileActivity.class).putExtra("user_id",notifications.get(position).getUser_id()));
            }
        });
        if (notifications.get(position).getMessage().trim().equals("تم قبول الطلب")){
            holder.body.setText(notifications.get(position).getMessage());
            holder.body.setTextColor(Color.parseColor("#228B22"));
        } else if (notifications.get(position).getMessage().trim().equals("لم يتم قبول الطلب بعد")){
            holder.body.setText(notifications.get(position).getMessage());
            holder.body.setTextColor(Color.RED);
        } else {
            holder.body.setText(notifications.get(position).getMessage());
            holder.body.setTextColor(Color.GRAY);
        }

        if (flag == 1) {
            holder.request_type.setVisibility(View.VISIBLE);
            if (notifications.get(position).getType().equals("taxi")) {

                if (notifications.get(position).getUser_id().equals(util.GetCurrentUser().getId())){
                    holder.request_type.setText("تاكسي - طلب صادر");
                } else {
                    holder.request_type.setText("تاكسي - طلب وارد");
                }
            } else {
                if (notifications.get(position).getUser_id().equals(util.GetCurrentUser().getId())){
                    holder.request_type.setText("توصيل - طلب صادر");
                } else {
                    holder.request_type.setText("توصيل - طلب وارد");
                }
            }
        } else {
            holder.request_type.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(notifications.get(position).getImage())) {
            Picasso.with(context).load(notifications.get(position).getImage()).placeholder(R.drawable.ic_dummy_person).error(R.drawable.ic_dummy_person).into(holder.profile_pic);
        } else {
            holder.profile_pic.setImageResource(R.drawable.ic_dummy_person);
        }
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView profile_pic;
        TextView body,user_name, date,request_type;

        public ViewHolder(View itemView) {
            super(itemView);
            body = itemView.findViewById(R.id.body);
            profile_pic = itemView.findViewById(R.id.profile_image);
            user_name = itemView.findViewById(R.id.person_name);
            date = itemView.findViewById(R.id.date);
            request_type = itemView.findViewById(R.id.request_type);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClick.setOnItemClick(getAdapterPosition());
        }
    }

    public interface OnItemClick{
        void setOnItemClick(int position);
    }


    public void setNotifications(ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }
}
