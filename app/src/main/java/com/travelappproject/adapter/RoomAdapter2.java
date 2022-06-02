package com.travelappproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.travelappproject.HandleCurrency;
import com.travelappproject.R;
import com.travelappproject.model.hotel.room.Room;

import java.util.List;

public class RoomAdapter2 extends RecyclerView.Adapter<RoomAdapter2.Room2ViewHolder> {
    private Context mContext;
    private List<Room> mList;


    public RoomAdapter2(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<Room> list){
        mList = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public Room2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_2,parent,false);
        return new Room2ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Room2ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(mList == null)
            return;
        holder.txtTypeRoom.setText(mList.get(position).getName());
        Glide.with(mContext).load(mList.get(position).getPhotos().get(0).getRoomImage()).into(holder.imgRoom);
        holder.txtPrice.setText(new HandleCurrency().handle(mList.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class Room2ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTypeRoom;
        TextView txtPrice;
        ShapeableImageView imgRoom;
        LinearLayout layout;

        public Room2ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTypeRoom = itemView.findViewById(R.id.txtType);
            imgRoom = itemView.findViewById(R.id.imgRoom);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
