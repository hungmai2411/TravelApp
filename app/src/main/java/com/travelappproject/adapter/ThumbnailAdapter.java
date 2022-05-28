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

import java.util.List;

public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.ThumbnailViewHolder> {
    List<String> mListLocation;
    List<Integer> mListImages;
    Context mContext;
    IClickDestinationListener mIClickDestinationListener;

    public ThumbnailAdapter(List<String> mListLocation, List<Integer> mListImages, Context mContext,IClickDestinationListener iClickDestinationListener) {
        this.mListLocation = mListLocation;
        this.mListImages = mListImages;
        this.mContext = mContext;
        this.mIClickDestinationListener = iClickDestinationListener;
    }

    @NonNull
    @Override
    public ThumbnailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_thumbnail,parent,false);
        return new ThumbnailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThumbnailViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtLocation.setText(mListLocation.get(position));

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);

        holder.imgLocation.setImageResource(mListImages.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIClickDestinationListener.onCallBack(mListLocation.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class ThumbnailViewHolder extends RecyclerView.ViewHolder{

        TextView txtLocation;
        ImageView imgLocation;

        public ThumbnailViewHolder(@NonNull View itemView) {
            super(itemView);

            txtLocation = itemView.findViewById(R.id.textTitle);
            imgLocation = itemView.findViewById(R.id.imgLocation);
        }
    }

    public interface IClickDestinationListener{
        public void onCallBack(String destination);
    }
}
