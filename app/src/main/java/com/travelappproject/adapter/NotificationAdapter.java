package com.travelappproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.travelappproject.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>{

    private Context mContext;
    private List<Date> mListDate;

    public NotificationAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setDate(List<Date> list){
        mListDate = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_notification,parent,false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm dd/MM/yyyy");
        String strDate = dateFormat.format(mListDate.get(position));

        holder.txtDate.setText(strDate);
    }

    @Override
    public int getItemCount() {
        if(mListDate == null){
            return 0;
        }

        return mListDate.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder{

        TextView txtDate;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDate = itemView.findViewById(R.id.txtDate);
        }
    }
}
