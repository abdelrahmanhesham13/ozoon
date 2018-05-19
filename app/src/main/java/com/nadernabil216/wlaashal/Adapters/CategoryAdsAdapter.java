package com.nadernabil216.wlaashal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nadernabil216.wlaashal.Model.Objects.Product;
import com.nadernabil216.wlaashal.R;
import com.nadernabil216.wlaashal.UI.Activities.AdvertDetails;
import com.nadernabil216.wlaashal.Utils.GMethods;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by NaderNabil216@gmail.com on 5/9/2018.
 */
public class CategoryAdsAdapter extends RecyclerView.Adapter<CategoryAdsAdapter.ViewHolder> {

    ArrayList<Product> products ;
    Context context ;

    public CategoryAdsAdapter(ArrayList<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product,parent,false) ;
        GMethods.ChangeViewFont(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Product product = products.get(position);
        Picasso.with(context).load(product.getImage()).into(holder.imageView, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });
        holder.title.setText(product.getName());
//        holder.ratingBar.setRating(Float.parseFloat(product.getRate()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AdvertDetails.class);
                intent.putExtra(GMethods.PRODUCT_ID,product.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView ;
        TextView title ;
        RatingBar ratingBar;
        ProgressBar progressbar ;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.product_img);
            title=itemView.findViewById(R.id.product_name);
            ratingBar=itemView.findViewById(R.id.rating_bar);
            progressbar = itemView.findViewById(R.id.progressbar);
        }
    }
}
