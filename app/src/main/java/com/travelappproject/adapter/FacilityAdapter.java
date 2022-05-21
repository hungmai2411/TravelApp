package com.travelappproject.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.travelappproject.R;
import com.travelappproject.model.hotel.Facility;

import java.util.List;

public class FacilityAdapter extends RecyclerView.Adapter<FacilityAdapter.FacilityViewHolder> {
    private Context mContext;
    private List<Facility> mList;

    public void setData(List<Facility> list){
        mList = list;
        notifyDataSetChanged();
    }

    public FacilityAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public FacilityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_facilities,parent,false);
        return new FacilityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacilityViewHolder holder, int position) {
        if (mList == null)
            return;

        holder.txtFacility.setText(mList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class FacilityViewHolder extends RecyclerView.ViewHolder{
        TextView txtFacility;

        public FacilityViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFacility = itemView.findViewById(R.id.txtFacility);
        }
    }
}

