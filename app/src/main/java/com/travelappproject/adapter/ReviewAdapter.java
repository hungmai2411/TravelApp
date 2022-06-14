package com.travelappproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.travelappproject.R;
import com.travelappproject.model.hotel.Review;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {
    private Context mContext;
    private List<Review> mListReview;

    public ReviewAdapter(Context context){
        this.mContext = context;
    }

    public void addList(List<Review> listReview){
        this.mListReview = listReview;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_review,parent,false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {
        holder.txtName.setText(mListReview.get(position).getNameUser());
        holder.txtNumStar.setText(String.valueOf(mListReview.get(position).getRate()));
        holder.txtReview.setText(mListReview.get(position).getReview());

        PhotoReviewAdapter photoAdapter;

        photoAdapter = new PhotoReviewAdapter(mContext);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        holder.rcvImages.setLayoutManager(linearLayoutManager);
        photoAdapter.addData(mListReview.get(position).getImages());
        holder.rcvImages.setAdapter(photoAdapter);
        Glide.with(mContext).load(mListReview.get(position).getImgUser()).error(R.drawable.profile).into(holder.imgUser);
    }

    @Override
    public int getItemCount() {
        if(mListReview == null)
            return 0;

        return mListReview.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder{

        RecyclerView rcvImages;
        TextView txtReview;
        TextView txtName,txtNumStar;
        CircleImageView imgUser;

        public ReviewHolder(@NonNull View itemView) {
            super(itemView);

            rcvImages = itemView.findViewById(R.id.rcvImages);
            txtReview = itemView.findViewById(R.id.txtReview);
            txtName = itemView.findViewById(R.id.txtName);
            txtNumStar = itemView.findViewById(R.id.txtNumStar);
            imgUser = itemView.findViewById(R.id.imgUser);
        }
    }
}
