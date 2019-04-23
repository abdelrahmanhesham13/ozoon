package com.ozoon.ozoon.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ozoon.ozoon.Model.Objects.ResultItemModel;
import com.ozoon.ozoon.R;
import com.ozoon.ozoon.Utils.GMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultItemAdapter extends RecyclerView.Adapter<ResultItemAdapter.ResultItemHolder> {

    private Context mContext;

    private ArrayList<ResultItemModel> items;

    private OnItemClick onItemClick;

    public ResultItemAdapter(Context mContext, ArrayList<ResultItemModel> items, OnItemClick onItemClick) {
        this.mContext = mContext;
        this.items = items;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public ResultItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_result_item, parent, false);
        GMethods.ChangeViewFont(view);
        return new ResultItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultItemHolder holder, int position) {
        holder.fromCity.setText(items.get(position).getFromCity());
        holder.toCity.setText(items.get(position).getToCity());
        if (items.get(position).getPrice().isEmpty()) {
            holder.price.setText("");
        } else {
            holder.price.setText(items.get(position).getPrice() + " ريال");
        }
        holder.time.setText(items.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ResultItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.from_city)
        TextView fromCity;
        @BindView(R.id.to_city)
        TextView toCity;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.time)
        TextView time;

        public ResultItemHolder(View itemView) {
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


    public interface OnItemClick {
        void setOnItemClick(int position);
    }

}
