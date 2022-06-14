package com.travelappproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.travelappproject.HandleCurrency;
import com.travelappproject.R;
import com.travelappproject.model.hotel.Booking;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private List<Booking> bookingList;
    private IClickBookingListener iClickBookingListener;

    public BookingAdapter(Context mContext, IClickBookingListener iClickBookingListener) {
        this.mContext = mContext;
        this.iClickBookingListener = iClickBookingListener;
    }

    public void addData(List<Booking> list){
        bookingList = list;
        notifyDataSetChanged();
    }

    private Context mContext;

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking,parent,false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(bookingList == null)
            return;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickBookingListener.onCallBack(bookingList.get(position));
            }
        });

        Date date = bookingList.get(position).getDate();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);

        DateFormat dateFormat1 = new SimpleDateFormat("HH:mm");
        String strTime = dateFormat1.format(date);

        holder.txtNumber.setText(String.valueOf(bookingList.get(position).getDaysdiff()) + " night");
        holder.txtPrice.setText(new HandleCurrency().handle(bookingList.get(position).getPrice()));
        holder.txtNameHotel.setText(bookingList.get(position).getHotelName());
        holder.txtNameRoom.setText(bookingList.get(position).getNameRoom());
        holder.txtDate.setText(strDate);
        holder.txtTime.setText(strTime);

        if(bookingList.get(position).getStatus().equals("Cancelled")){
            holder.txtStatus.setTextColor(mContext.getResources().getColor(R.color.cancelled_text));
            holder.txtStatus.setBackgroundColor(mContext.getResources().getColor(R.color.cancelled_color));
        }else{
            holder.txtStatus.setTextColor(mContext.getResources().getColor(R.color.booked_text));
            holder.txtStatus.setBackgroundColor(mContext.getResources().getColor(R.color.booked_color));
        }
        holder.txtStatus.setText(bookingList.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        if(bookingList == null)
            return 0;

        return bookingList.size();
    }

    public class BookingViewHolder extends RecyclerView.ViewHolder{
        TextView txtDate,txtTime,txtNameHotel,txtNameRoom,txtPrice,txtNumber,txtStatus;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);

            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtNameHotel = itemView.findViewById(R.id.txtNameHotel);
            txtNameRoom = itemView.findViewById(R.id.txtNameRoom);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtNumber = itemView.findViewById(R.id.txtNumber);
        }
    }

    public interface IClickBookingListener{
        public void onCallBack(Booking booking);
    }
}
