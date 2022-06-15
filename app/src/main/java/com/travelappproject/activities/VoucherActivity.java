package com.travelappproject.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.travelappproject.R;
import com.travelappproject.adapter.VouchersAdapter;
import com.travelappproject.model.hotel.Voucher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VoucherActivity extends AppCompatActivity {
    RecyclerView rcvVoucher;
    VouchersAdapter vouchersAdapter;
    List<Voucher> voucherList;
    FirebaseFirestore firestore;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);
        voucherList = new ArrayList<>();

        rcvVoucher = (RecyclerView) findViewById(R.id.rcvVoucher);
        toolbar = findViewById(R.id.toolbar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvVoucher.setLayoutManager(linearLayoutManager);
        getListVoucher(voucherList);

        vouchersAdapter = new VouchersAdapter(voucherList, getApplicationContext());
        rcvVoucher.setAdapter(vouchersAdapter);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getListVoucher(List<Voucher> voucherList){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        firestore = FirebaseFirestore.getInstance();

        firestore.collection("users/" + userID + "/vouchers")
                .orderBy("endDate", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot snapshot : task.getResult()){
                        Date date = new Date();
                        String endDate = snapshot.getString("endDate");

                        Date end = null;

                        try {
                            end = new SimpleDateFormat("dd-MM-yyyy").parse(endDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if(end.getYear() >= date.getYear()){
                            if(end.getMonth() >= date.getMonth()){
                                if(end.getDay() >= date.getDay()){
                                    Voucher voucher = snapshot.toObject(Voucher.class);
                                    voucherList.add(voucher);
                                }
                            }
                        }
                    }
                }
                vouchersAdapter.notifyDataSetChanged();
            }
        });
    }
}