package com.travelappproject.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.travelappproject.R;
import com.travelappproject.activities.VoucherActivity;
import com.travelappproject.model.hotel.Voucher;

import java.util.List;

public class VouchersAdapter extends RecyclerView.Adapter<VouchersAdapter.VouchersViewHolder> {
    private List<Voucher> VoucherList;
    Context context;

    public VouchersAdapter(List<Voucher> voucherList, Context context) {
        VoucherList = voucherList;
        this.context = context;
    }

    @NonNull
    @Override
    public VouchersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voucher, parent, false);
        return new VouchersViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VouchersViewHolder holder, int position) {
        Voucher voucher = VoucherList.get(position);
        if(voucher == null)
            return;
        holder.txtTitle.setText(context.getString(R.string.discount) + " " + voucher.getNumber() + "%");
        holder.txtDescription.setText(voucher.getDescription());
        holder.txtEndDate.setText(voucher.getEndDate());
        holder.txtCode.setText(voucher.getCode());
    }

    @Override
    public int getItemCount() {
        if(VoucherList != null)
            return VoucherList.size();
        return 0;
    }

    public class VouchersViewHolder extends RecyclerView.ViewHolder{
        TextView txtTitle, txtDescription, txtEndDate, txtCode;
        TextView btnCopy;
        ImageView imgTick;
        public VouchersViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtEndDate = itemView.findViewById(R.id.txtEndDate);
            txtCode = itemView.findViewById(R.id.txtCode);
            btnCopy = itemView.findViewById(R.id.txtCopy);
            imgTick = itemView.findViewById(R.id.imgTick);

            btnCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("code", txtCode.getText().toString());
                    clipboardManager.setPrimaryClip(clip);

                    btnCopy.setText(context.getString(R.string.copied));
                    btnCopy.setTextColor(Color.LTGRAY);
                    imgTick.setVisibility(View.VISIBLE);

                }
            });

        }
    }
}
