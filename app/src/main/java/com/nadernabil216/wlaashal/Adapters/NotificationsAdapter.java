package com.nadernabil216.wlaashal.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nadernabil216.wlaashal.Model.Objects.Notification;
import com.nadernabil216.wlaashal.Presenters.NotificationsPresenter;
import com.nadernabil216.wlaashal.R;
import com.nadernabil216.wlaashal.Utils.GMethods;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by NaderNabil216@gmail.com on 5/12/2018.
 */
public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    ArrayList<Notification> notifications;
    Context context;
    NotificationsPresenter presenter;

    public NotificationsAdapter(ArrayList<Notification> notifications, Context context) {
        this.notifications = notifications;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        GMethods.ChangeViewFont(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Notification notification = notifications.get(position);
        Picasso.with(context).load(notification.getUser_image()).into(holder.profile_pic);
        holder.user_name.setText(notification.getUser_name());
        holder.body.setText(notification.getBody());
        holder.date.setText(notification.getDate());
        holder.ic_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifications.remove(position);
                notifyItemRemoved(position);

                // TODO: 5/12/2018 presenter.deleteNotification();
            }
        });

    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_pic, ic_delete;
        TextView body, user_name, date;

        public ViewHolder(View itemView) {
            super(itemView);
            profile_pic = itemView.findViewById(R.id.profile_image);
            ic_delete = itemView.findViewById(R.id.ic_delete);
            body = itemView.findViewById(R.id.notification_body);
            user_name = itemView.findViewById(R.id.tv_user_name);
            date = itemView.findViewById(R.id.tv_notification_date);

        }
    }
}
