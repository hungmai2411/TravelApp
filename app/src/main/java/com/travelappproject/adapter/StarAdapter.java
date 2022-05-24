package com.travelappproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.travelappproject.R;

public class StarAdapter extends RecyclerView.Adapter<StarAdapter.StarViewHolder> {
    @NonNull
    @Override
    public StarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_star,parent,false);
        return new StarViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull StarViewHolder holder, int position) {
        if(position == t){
            holder.imgStar.setImageResource(R.drawable.ic_half_star);
            return;
        }

        holder.imgStar.setImageResource(R.drawable.ic_star);
    }

    @Override
    public int getItemCount() {
        double tmp = count;
        if(String.valueOf(count).length() > 2){
            tmp  = tmp + 0.5;
        }
        return (int) tmp;
    }

    public class StarViewHolder extends RecyclerView.ViewHolder{
        ImageView imgStar;

        public StarViewHolder(@NonNull View itemView) {
            super(itemView);
            imgStar = itemView.findViewById(R.id.imgStar);
        }
    }

    public StarAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setCount(double count){
        if(String.valueOf(count).length() > 2){
            t = count - 0.5;
        }
        this.count = count;
        notifyDataSetChanged();
    }

    Context mContext;
    double count;
    double t;
}