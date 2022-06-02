package com.travelappproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.travelappproject.R;
import com.travelappproject.model.hotel.Payment;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {
    private Context mContext;
    private int checkedPosition = 0;
    private List<Payment> mList;

    public PaymentAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<Payment> list){
        mList = list;
        notifyDataSetChanged();
    }

    public Payment getItemSelected(){
        if(checkedPosition != -1){
            return mList.get(checkedPosition);
        }
        return null;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_payment,parent,false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class PaymentViewHolder extends RecyclerView.ViewHolder{
        TextView txtPaymentMethod;
        ImageView imgIcon;
        RadioButton radioButton;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);

            txtPaymentMethod = itemView.findViewById(R.id.txtPaymentMethod);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            radioButton = itemView.findViewById(R.id.radioButton);
        }

        void bind(int pos){
            if(checkedPosition == -1){
                radioButton.setChecked(false);
            }else{
                if(checkedPosition == getLayoutPosition()){
                    radioButton.setChecked(true);
                }else{
                    radioButton.setChecked(false);
                }
            }

            txtPaymentMethod.setText(mList.get(pos).getPaymentMethod());
            imgIcon.setImageResource(mList.get(pos).getPaymentIcon());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    radioButton.setChecked(true);

                    if(checkedPosition != getLayoutPosition()){
                        notifyItemChanged(checkedPosition);
                        checkedPosition = getLayoutPosition();
                    }
                }
            });

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    radioButton.setChecked(true);

                    if(checkedPosition != getLayoutPosition()){
                        notifyItemChanged(checkedPosition);
                        checkedPosition = getLayoutPosition();
                    }
                }
            });
        }

    }

}


