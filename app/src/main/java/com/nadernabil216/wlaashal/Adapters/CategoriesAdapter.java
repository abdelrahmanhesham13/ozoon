package com.nadernabil216.wlaashal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nadernabil216.wlaashal.Model.Objects.Category;
import com.nadernabil216.wlaashal.R;
import com.nadernabil216.wlaashal.UI.Activities.AdvertsActivity;
import com.nadernabil216.wlaashal.Utils.GMethods;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by NaderNabil216@gmail.com on 5/9/2018.
 */
public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.Viewholder> {

    private Context context;
    private ArrayList<Category> categories;

    public CategoriesAdapter(Context context, ArrayList<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        GMethods.ChangeViewFont(view);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(final Viewholder holder, int position) {
        final Category category = categories.get(position);
        if (position == 0) {
            holder.progressBar.setVisibility(View.GONE);
            holder.cat_image.setImageResource(R.drawable.ic_category_taxi);
            holder.cat_title.setText("تاكسي");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        } else if (position == 1) {
            holder.progressBar.setVisibility(View.GONE);
            holder.cat_image.setImageResource(R.drawable.ic_category_delivery);
            holder.cat_title.setText("التوصيل");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else {
            Picasso.with(context).load(category.getImage()).into(holder.cat_image, new Callback() {
                @Override
                public void onSuccess() {
                    holder.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {

                }
            });
            holder.cat_title.setText(category.getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AdvertsActivity.class);
                    intent.putExtra(GMethods.CATEGORY_ID,category.getId());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {

        ImageView cat_image;
        TextView cat_title;
        ProgressBar progressBar;

        public Viewholder(View itemView) {
            super(itemView);
            cat_image = itemView.findViewById(R.id.category_image);
            cat_title = itemView.findViewById(R.id.category_title);
            progressBar = itemView.findViewById(R.id.progressbar);
        }
    }
}
