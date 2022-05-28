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
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.travelappproject.R;
import com.travelappproject.model.hotel.Hotel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>{
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private Context mContext;
    private List<Hotel> mList;
    private String mIdUser;
    boolean isFavorite = false;

    public FavoriteAdapter(Context mContext,String idUser,IClickItemListener iClickItemListener) {
        this.mContext = mContext;
        this.mIdUser = idUser;
        this.mIClickItemListener = iClickItemListener;
    }

    public void setData(List<Hotel> list){
        mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite,parent,false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Hotel hotel = mList.get(position);
        String id = String.valueOf(hotel.getId());

        if(hotel == null)
            return;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIClickItemListener.onClickItem(mList.get(position));
            }
        });

        firestore.collection("users/" + mIdUser + "/favorites").document(String.valueOf(id)).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null){
                    if (value.exists()){
                        holder.btnFav.setImageResource(R.drawable.ic_action_favorite);
                    }else{
                        holder.btnFav.setImageResource(R.drawable.ic_favorite_border);
                    }
                }
            }
        });


        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);

        String tmp = mList.get(position).getThumbImage();
        String path = "https://statics.vntrip.vn/data-v2/hotels/" + mList.get(position).getId() + "/img_max/" + tmp;
        Glide.with(mContext).load(path).apply(options).into(holder.img);
        holder.name.setText(hotel.getName());
        holder.txtNumStar.setText(String.valueOf(mList.get(position).getStarRate()));

        double count = mList.get(position).getStarRate();

        StarAdapter starAdapter = new StarAdapter(mContext);
        starAdapter.setCount(count);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,false);
        holder.rcStar.setLayoutManager(linearLayoutManager);
        holder.rcStar.setAdapter(starAdapter);

        holder.btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("del","222");

                firestore.collection("users/" + mIdUser + "/favorites").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.getResult().exists()){
                            Map<String , Object> likesMap = new HashMap<>();
                            likesMap.put("timestamp" , FieldValue.serverTimestamp());
                            firestore.collection("users/" + mIdUser + "/favorites").document(id).set(likesMap);
                        }else{
                            firestore.collection("users/" + mIdUser + "/favorites").document(id).delete();
                        }
                    }
                });

                mList.remove(holder.getBindingAdapterPosition());
                notifyItemRemoved(holder.getBindingAdapterPosition());
                holder.btnFav.setImageResource(R.drawable.ic_favorite_border);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mList == null)
            return  0;
        Log.d("teo", String.valueOf(mList.size()));
        return mList.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {

        private ImageView img;
        private TextView name;
        private RecyclerView rcStar;
        private TextView txtNumStar;
        private FloatingActionButton btnFav;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.imgLocation);
            name = itemView.findViewById(R.id.txtName);
            rcStar = itemView.findViewById(R.id.rcvStar);
            txtNumStar = itemView.findViewById(R.id.txtNumStar);
            btnFav = itemView.findViewById(R.id.btnFav);
        }
    }

    private IClickItemListener mIClickItemListener;

    public interface IClickItemListener{
        public void onClickItem(Hotel data);
    }

    boolean checkFavorite(long id){
        firestore.collection("users/" + mIdUser + "/favorites").document(String.valueOf(id)).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null){
                    if (value.exists()){
                        isFavorite = true;
                    }
                }
            }
        });

        return isFavorite;
    }
}

