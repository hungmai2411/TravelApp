package com.travelappproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.travelappproject.R;
import com.travelappproject.model.hotel.Hotel;

import java.util.List;

public class HotelAdapter1 extends RecyclerView.Adapter<HotelAdapter1.HotelAdapter1ViewHolder> {
    private Context mContext;
    private List<Hotel> mHotelList;

    public HotelAdapter1(Context mContext, HotelAdapter1.IClickItemListener listener) {
        this.mContext = mContext;
        this.mIClickItemListener = listener;
    }

    private HotelAdapter1.IClickItemListener mIClickItemListener;

    public interface IClickItemListener{
        public void onClickItem(Hotel hotel);
    }

    public void setData(List<Hotel> list){
        this.mHotelList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HotelAdapter1ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hotel_2,parent,false);
        return new HotelAdapter1.HotelAdapter1ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelAdapter1ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(mHotelList == null)
            return;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIClickItemListener.onClickItem(mHotelList.get(position));
            }
        });

        double count = mHotelList.get(position).getStarRate();

        StarAdapter starAdapter = new StarAdapter(mContext);

        starAdapter.setCount(count);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,false);
        holder.rcvStar.setLayoutManager(linearLayoutManager);
        holder.rcvStar.setAdapter(starAdapter);
        holder.txtNameHotel.setText(mHotelList.get(position).getName());
        holder.txtLocation.setText(mHotelList.get(position).getFullAddress());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);

        String tmp = mHotelList.get(position).getThumbImage();
        String path = "https://statics.vntrip.vn/data-v2/hotels/" + mHotelList.get(position).getId() + "/img_max/" + tmp;
        holder.txtAmount.setText(String.valueOf(mHotelList.get(position).getPrice()));
        Glide.with(mContext).load(path).apply(options).into(holder.imgHotel);
    }

    @Override
    public int getItemCount() {
        return mHotelList.size();
    }

    public class HotelAdapter1ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgHotel;
        TextView txtNameHotel;
        TextView txtLocation;
        RecyclerView rcvStar;
        TextView txtAmount;

        public HotelAdapter1ViewHolder(@NonNull View itemView) {
            super(itemView);

            rcvStar = itemView.findViewById(R.id.rcvStar);
            imgHotel = itemView.findViewById(R.id.imgHotel);
            txtNameHotel = itemView.findViewById(R.id.txtNameHotel);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtLocation = itemView.findViewById(R.id.txtLocation);
        }
    }
}
