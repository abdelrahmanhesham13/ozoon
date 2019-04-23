package com.ozoon.ozoon.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ozoon.ozoon.CallBacks.SuccessCallBack;
import com.ozoon.ozoon.Model.Objects.Product;
import com.ozoon.ozoon.Presenters.MainPresenter;
import com.ozoon.ozoon.ProfileActivity;
import com.ozoon.ozoon.R;
import com.ozoon.ozoon.UI.Activities.AddNewAdvertActivity;
import com.ozoon.ozoon.UI.Activities.AdvertDetails;
import com.ozoon.ozoon.Utils.GMethods;
import com.ozoon.ozoon.Utils.Helper;
import com.ozoon.ozoon.Utils.StorageUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by NaderNabil216@gmail.com on 5/9/2018.
 */
public class CategoryAdsAdapter extends RecyclerView.Adapter<CategoryAdsAdapter.ViewHolder> {

    ArrayList<Product> products;
    Context context;
    int flag;
    StorageUtil util;
    MainPresenter presenter;

    public CategoryAdsAdapter(ArrayList<Product> products, Context context, int flag) {
        this.products = products;
        this.context = context;
        this.flag = flag;
        presenter = new MainPresenter();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (flag == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
            GMethods.ChangeViewFont(view);
            return new ViewHolder(view);
        } else if (flag == 10) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_item_client_rest, parent, false);
            GMethods.ChangeViewFont(view);
            return new ViewHolder(view);
        } else if (flag == 20) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_item_client_rest, parent, false);
            GMethods.ChangeViewFont(view);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_product_mine, parent, false);
            GMethods.ChangeViewFont(view);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Product product = products.get(position);
        if (!product.getImage().isEmpty()) {
            Picasso.with(context).load(product.getImage()).fit().into(holder.imageView, new Callback() {
                @Override
                public void onSuccess() {
                    if (flag != 10 && flag != 20) {
                        holder.progressbar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onError() {

                }
            });
        }
        holder.title.setText(product.getName());
        if (product.getRate() != null) {
            try {
                holder.ratingBar.setRating(Float.parseFloat(product.getRate()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AdvertDetails.class);
                intent.putExtra(GMethods.PRODUCT_ID, product.getId());
                context.startActivity(intent);
            }
        });

        if (flag == 10) {
            if (product.getPrice() != null) {
                if (!product.getPrice().equals("0") && !product.getPrice().equals("٠")) {
                    holder.price.setVisibility(View.VISIBLE);
                    holder.price.setText("السعر : " + product.getPrice() + " ريال");
                } else {
                    holder.price.setVisibility(View.GONE);
                }
            }
            if (product.getDistance() != null) {
                float distance = Float.parseFloat(product.getDistance()) / 1000;
                String dist = String.format(Locale.getDefault(), "%.02f", distance);
                holder.distance.setText(dist + " كيلو متر");
                if (distance < 0.03) {
                    holder.distance.setText("قريب جدا");
                }
            } else {
                holder.distance.setText("خدمة تحديد الموقع غير مفعله");
            }
        }

        if (flag == 20) {
            if (product.getPrice() != null) {
                if (!product.getPrice().equals("0") && !product.getPrice().equals("٠")) {
                    holder.price.setVisibility(View.VISIBLE);
                    holder.price.setText("السعر : " + product.getPrice() + " ريال");
                } else {
                    holder.price.setVisibility(View.GONE);
                }
            }
            if (product.getDistance() != null) {
                float distance = Float.parseFloat(product.getDistance()) / 1000;
                String dist = String.format(Locale.getDefault(), "%.02f", distance);
                holder.distance.setText(dist + " كيلو متر");
                if (distance < 0.03) {
                    holder.distance.setText("قريب جدا");
                }
            } else {
                holder.distance.setText("خدمة تحديد الموقع غير مفعله");
            }

            holder.distance.setVisibility(View.GONE);
            holder.location.setVisibility(View.GONE);
        }

        if (flag == 1) {
            holder.delete_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    util = StorageUtil.getInstance().doStuff(context);
                    String productId = products.get(position).getId();
                    String userId = util.GetCurrentUser().getId();
                    presenter.deleteProduct(productId, userId, new SuccessCallBack() {
                        @Override
                        public void OnSuccess() {


                        }

                        @Override
                        public void OnFailure(String message) {
                            //Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                            Helper.showSnackBarMessage(message, (AppCompatActivity) context);
                            products.remove(position);
                            notifyDataSetChanged();
                            if (products.isEmpty()) {
                                ((ProfileActivity) context).deleteAll.setVisibility(View.GONE);
                            }
                            String ads = StorageUtil.getAdsCount(context);
                            int ad = Integer.valueOf(ads);
                            ad--;

                            StorageUtil.setAdsCount(context, String.valueOf(ad));
                            Helper.writeToLog(StorageUtil.getAdsCount(context));


                            int num = Integer.parseInt(((ProfileActivity) context).numOfPoints.getText().toString());
                            ((ProfileActivity) context).numOfPoints.setText((num - 10) + "");
                            ((ProfileActivity) context).numOfAds.setText(ad + "");
                        }

                        @Override
                        public void OnServerError() {

                        }
                    });
                }
            });

            holder.edit_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, AddNewAdvertActivity.class).putExtra("product", products.get(position)));
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        TextView price;
        RatingBar ratingBar;
        ProgressBar progressbar;
        TextView delete_product;
        TextView edit_product;
        TextView distance;
        ImageView location;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.product_img);
            title = itemView.findViewById(R.id.product_name);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            if (flag != 10 && flag != 20) {
                progressbar = itemView.findViewById(R.id.progressbar);
            }

            if (flag == 10) {
                distance = itemView.findViewById(R.id.distance);
                price = itemView.findViewById(R.id.price);
            }

            if (flag == 20) {
                distance = itemView.findViewById(R.id.distance);
                price = itemView.findViewById(R.id.price);
                location = itemView.findViewById(R.id.location);
            }

            if (flag == 1) {
                delete_product = itemView.findViewById(R.id.delete_product);
                edit_product = itemView.findViewById(R.id.edit_product);
            }
        }
    }
}
