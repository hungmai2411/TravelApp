package com.travelappproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.travelappproject.HandleCurrency;
import com.travelappproject.R;
import com.travelappproject.model.hotel.Hotel;

import java.util.List;

public class HotelAdapter1 extends RecyclerView.Adapter<HotelAdapter1.Hotel1ViewHolder> {
    private Context mContext;
    private List<Hotel> mHotelList;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 2;
    private boolean isLoadingAdd;

    public HotelAdapter1(Context mContext, HotelAdapter1.IClickItemListener listener) {
        this.mContext = mContext;
        this.mIClickItemListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mHotelList.size() - 1 && mHotelList != null && isLoadingAdd == true) {
            return TYPE_LOADING;
        }
        return TYPE_ITEM;
    }

    private HotelAdapter1.IClickItemListener mIClickItemListener;

    public interface IClickItemListener {
        public void onClickItem(Hotel hotel);
    }

    public void setData(List<Hotel> list) {
        this.mHotelList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Hotel1ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (TYPE_ITEM == viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hotel_2, parent, false);
            return new HotelAdapter1.Hotel1ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new HotelAdapter1.Hotel1ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Hotel1ViewHolder hotelViewHolder, @SuppressLint("RecyclerView") int position) {
        if (mHotelList == null)
            return;

        if (hotelViewHolder.getItemViewType() == TYPE_ITEM) {
            Hotel1ViewHolder hotelHolder = (Hotel1ViewHolder) hotelViewHolder;

            hotelHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mIClickItemListener.onClickItem(mHotelList.get(position));
                }
            });

            double count = mHotelList.get(position).getStarRate();

            StarAdapter starAdapter = new StarAdapter(mContext);

            starAdapter.setCount(count);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            hotelHolder.rcvStar.setLayoutManager(linearLayoutManager);
            hotelHolder.rcvStar.setAdapter(starAdapter);
            hotelHolder.txtNameHotel.setText(mHotelList.get(position).getName());
            hotelHolder.txtLocation.setText(mHotelList.get(position).getFullAddress());

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);

            String tmp = mHotelList.get(position).getThumbImage();
            String path = "https://statics.vntrip.vn/data-v2/hotels/" + mHotelList.get(position).getId() + "/img_max/" + tmp;
            hotelHolder.txtAmount.setText(new HandleCurrency().handle(mHotelList.get(position).getPrice()));
            Glide.with(mContext).load(path).apply(options).into(hotelViewHolder.imgHotel);
        }
    }

    @Override
    public int getItemCount() {
        return mHotelList.size();
    }

    public class Hotel1ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgHotel;
        TextView txtNameHotel;
        TextView txtLocation;
        RecyclerView rcvStar;
        TextView txtAmount;

        public Hotel1ViewHolder(@NonNull View itemView) {
            super(itemView);

            rcvStar = itemView.findViewById(R.id.rcvStar);
            imgHotel = itemView.findViewById(R.id.imgHotel);
            txtNameHotel = itemView.findViewById(R.id.txtNameHotel);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtLocation = itemView.findViewById(R.id.txtLocation);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);

            progressBar = itemView.findViewById(R.id.progress_bar);
        }
    }

    public void addFooterLoading(){
        isLoadingAdd = true;
    }

    public void removeFooterLoading(){
        isLoadingAdd = false;
    }
}
