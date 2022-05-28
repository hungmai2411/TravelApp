package com.travelappproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.travelappproject.R;

import java.util.List;

public class SortAdapter extends RecyclerView.Adapter<SortAdapter.SortViewHolder> {
    private Context mContext;
    private int checkedPosition = 0;
    private List<String> mList;
    private IClickSortItem iClickSortItem;

    public SortAdapter(Context mContext,IClickSortItem iClickSortItem, int index) {
        this.mContext = mContext;
        this.iClickSortItem = iClickSortItem;
        checkedPosition = index;
    }

    public void setData(List<String> list){
        mList = list;
        notifyDataSetChanged();
    }

    public String getItemSelected(){
        if(checkedPosition != -1){
            return mList.get(checkedPosition);
        }
        return null;
    }
    @NonNull
    @Override
    public SortViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_sort,parent,false);
        return new SortViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SortViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class SortViewHolder extends RecyclerView.ViewHolder{

        TextView txtName;
        ImageView btnCheck;
        RelativeLayout item;

        public SortViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            btnCheck = itemView.findViewById(R.id.btnCheck);
            item = itemView.findViewById(R.id.item);
        }

        void bind(int pos){
            if(checkedPosition == -1){
                btnCheck.setVisibility(View.GONE);
                item.setBackgroundResource(R.drawable.custom_linear_1);
            }else{
                if(checkedPosition == getLayoutPosition()){
                    btnCheck.setVisibility(View.VISIBLE);
                    item.setBackgroundResource(R.drawable.custom_linear);
                }else{
                    btnCheck.setVisibility(View.GONE);
                    item.setBackgroundResource(R.drawable.custom_linear_1);
                }
            }

            txtName.setText(mList.get(pos));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnCheck.setVisibility(View.VISIBLE);
                    item.setBackgroundResource(R.drawable.custom_linear);
                    iClickSortItem.onCallBack(txtName.getText().toString());

                    if(checkedPosition != getLayoutPosition()){
                        notifyItemChanged(checkedPosition);
                        checkedPosition = getAdapterPosition();
                    }
                }
            });
        }
    }

    public interface IClickSortItem{
        void onCallBack(String choice);
    }
}

