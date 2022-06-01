package com.travelappproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.travelappproject.R;
import com.travelappproject.model.hotel.Search;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchAdapterViewHolder> {
    private Context mContext;
    private List<Search> mSearchList;

    public SearchAdapter(Context mContext, SearchAdapter.IClickItemListener listener) {
        this.mContext = mContext;
        this.mIClickItemListener = listener;
    }

    private SearchAdapter.IClickItemListener mIClickItemListener;

    public interface IClickItemListener{
        public void onClickItem(Search search);
    }

    public void setData(List<Search> list){
        this.mSearchList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search,parent,false);
        return new SearchAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapterViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(mSearchList == null)
            return;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIClickItemListener.onClickItem(mSearchList.get(position));
            }
        });

        holder.txtNameHotel.setText(mSearchList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mSearchList.size();
    }

    public class SearchAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView txtNameHotel;

        public SearchAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNameHotel = itemView.findViewById(R.id.txtName);
        }
    }
}


