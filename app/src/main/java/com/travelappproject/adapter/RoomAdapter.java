package com.travelappproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
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

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private Context mContext;
    private List<Room> mList;

    public interface IClickRoomListener{
        void onCallBack(Room room);
    }

    private IClickRoomListener mIClickRoomListener;

    public RoomAdapter(Context mContext,IClickRoomListener iClickRoomListener) {
        this.mContext = mContext;
        this.mIClickRoomListener = iClickRoomListener;
    }

    public void setData(List<Room> list){
        mList = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room,parent,false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(mList == null)
            return;

        holder.txtTypeRoom.setText(mList.get(position).getName());
        Glide.with(mContext).load(mList.get(position).getPhotos().get(0).getRoomImage()).into(holder.imgRoom);
        holder.btnSelectRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIClickRoomListener.onCallBack(mList.get(position));
            }
        });

        if(mList.get(position).isAvailable() == false){
            Log.d("availabe",mList.get(position).getName());
            holder.btnSelectRoom.setEnabled(false);
            holder.imgRoom.setAlpha(100);
            holder.btnSelectRoom.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DADADA")));
        }

        holder.txtPrice.setText(new HandleCurrency().handle(mList.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        if(mList == null)
            return 0;
        return mList.size();
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder{
        TextView txtTypeRoom;
        TextView txtPrice;
        ShapeableImageView imgRoom;
        Button btnSelectRoom;
        LinearLayout layout;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTypeRoom = itemView.findViewById(R.id.txtType);
            imgRoom = itemView.findViewById(R.id.imgRoom);
            btnSelectRoom = itemView.findViewById(R.id.btnSelectRoom);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}

