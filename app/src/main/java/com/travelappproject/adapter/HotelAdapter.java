package com.travelappproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.travelappproject.model.hotel.Hotel;

import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelAdapterViewHolder> {
    private Context mContext;
    private List<Hotel> mHotelList;

    public HotelAdapter(Context mContext, HotelAdapter.IClickItemListener listener) {
        this.mContext = mContext;
        this.mIClickItemListener = listener;
    }

    private HotelAdapter.IClickItemListener mIClickItemListener;

    public interface IClickItemListener{
        public void onClickItem(Hotel data);
    }

    public void setData(List<Hotel> list){
        this.mHotelList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HotelAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hotel_1,parent,false);
        return new HotelAdapter.HotelAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelAdapterViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(mHotelList == null)
            return;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIClickItemListener.onClickItem(mHotelList.get(position));
            }
        });

        holder.txtNameHotel.setText(mHotelList.get(position).getName());
        holder.textStarRating.setText(String.valueOf(mHotelList.get(position).getStarRate()));
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);

        String tmp = mHotelList.get(position).getThumbImage();
        String path = "https://statics.vntrip.vn/data-v2/hotels/" + mHotelList.get(position).getId() + "/img_max/" + tmp;
        Glide.with(mContext).load(path).apply(options).into(holder.imgHotel);
    }

    @Override
    public int getItemCount() {
        return mHotelList.size();
    }

    public class HotelAdapterViewHolder extends RecyclerView.ViewHolder{

        ImageView imgHotel;
        TextView txtNameHotel;
        TextView textStarRating;

        public HotelAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            imgHotel = itemView.findViewById(R.id.imgHotel);
            txtNameHotel = itemView.findViewById(R.id.textTitle);
            textStarRating = itemView.findViewById(R.id.textStarRating);
        }
    }
}

