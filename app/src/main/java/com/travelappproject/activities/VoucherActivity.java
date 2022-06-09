package com.travelappproject.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.travelappproject.R;
import com.travelappproject.adapter.VouchersAdapter;
import com.travelappproject.model.hotel.Voucher;

import java.util.ArrayList;
import java.util.List;

public class VoucherActivity extends AppCompatActivity {
    RecyclerView rcvVoucher;
    VouchersAdapter vouchersAdapter;
    List<Voucher> voucherList;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);
        voucherList = new ArrayList<>();

        rcvVoucher = (RecyclerView) findViewById(R.id.rcvVoucher);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvVoucher.setLayoutManager(linearLayoutManager);
        getListVoucher(voucherList);

        vouchersAdapter = new VouchersAdapter(voucherList, getApplicationContext());
        rcvVoucher.setAdapter(vouchersAdapter);
    }

    private void getListVoucher(List<Voucher> voucherList){
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("vouchers").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot snapshot : task.getResult()){
                        Voucher voucher = snapshot.toObject(Voucher.class);
                        voucherList.add(voucher);
                    }
                }
                vouchersAdapter.notifyDataSetChanged();
            }
        });
    }
}