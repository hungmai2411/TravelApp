package com.travelappproject.adapter;

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
import java.util.concurrent.ConcurrentHashMap;

public class HotelSearchAdapter extends RecyclerView.Adapter<HotelSearchAdapter.HotelViewHolder> {
    private List<Hotel> mListHotel;
    private Context mContext;

    public HotelSearchAdapter(List<Hotel> mListHotel, Context context) {
        this.mListHotel = mListHotel;
        mContext = context;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hotel_2, parent, false);

        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        Hotel hotel = mListHotel.get(position);
        if(hotel == null)
            return;

        holder.txtName.setText(hotel.getName());
        holder.txtLocation.setText(hotel.getFullAddress());
        holder.txtPrice.setText(String.valueOf(hotel.getPrice()));

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);
        String tmp = mListHotel.get(position).getThumbImage();
        String path = "https://statics.vntrip.vn/data-v2/hotels/" + mListHotel.get(position).getId() + "/img_max/" + tmp;
        Glide.with(mContext).load(path).apply(options).into(holder.imgHotel);
    }

    @Override
    public int getItemCount() {
        if(mListHotel != null)
            return mListHotel.size();

        return 0;
    }

    public class HotelViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgHotel;
        private TextView txtName;
        private TextView txtLocation;
        private TextView txtPrice;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHotel = itemView.findViewById(R.id.imgHotel);
            txtName = itemView.findViewById(R.id.txtName);
            txtLocation = itemView.findViewById(R.id.txtLocation);
            txtPrice = itemView.findViewById(R.id.txtAmount);
        }
    }
}
