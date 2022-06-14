package com.travelappproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.travelappproject.R;

import java.util.List;

public class PhotoReviewAdapter extends RecyclerView.Adapter<PhotoReviewAdapter.PhotoReviewViewHolder> {
    private Context mContext;
    private List<String> mListUri;

    public PhotoReviewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addData(List<String> list) {
        mListUri = list;
        notifyDataSetChanged();
    }
    public List<String> getData(){
        return mListUri;
    }

    @NonNull
    @Override
    public PhotoReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new PhotoReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoReviewViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            Glide.with(mContext).load(mListUri.get(position)).into(holder.imgPhoto);
            holder.btnDelete.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (mListUri == null)
            return 0;

        return mListUri.size();
    }


    public class PhotoReviewViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPhoto;
        ImageButton btnDelete;

        public PhotoReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            btnDelete = itemView.findViewById(R.id.btnDelete);
            imgPhoto = itemView.findViewById(R.id.img_photo);
        }
    }
}
