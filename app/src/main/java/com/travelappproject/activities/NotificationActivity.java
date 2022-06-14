package com.travelappproject.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.travelappproject.R;
import com.travelappproject.adapter.NotificationAdapter;
import com.travelappproject.fragments.BookingFragment;
import com.travelappproject.model.hotel.Notification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rcvNotifications;
    NotificationAdapter notificationAdapter;
    List<Notification> notificationList = new ArrayList<>();
    FirebaseFirestore db;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        db = FirebaseFirestore.getInstance();
        toolbar = findViewById(R.id.toolbar);
        rcvNotifications = findViewById(R.id.rcvNotifications);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        notificationAdapter = new NotificationAdapter(this, new NotificationAdapter.ICLickNotification() {
            @Override
            public void onCallBack(String status) {
                if (status.equals("voucher")) {
                    startActivity(new Intent(NotificationActivity.this, VoucherActivity.class));
                }
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvNotifications.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rcvNotifications.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        rcvNotifications.addItemDecoration(dividerItemDecoration);

        if (mAuth.getCurrentUser() != null) {
            uid = mAuth.getUid();
        }

        db.collection("users/" + uid + "/notifications")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            Timestamp timestamp = (Timestamp) doc.get("timestamp");
                            Date date = timestamp.toDate();
                            Notification notification = new Notification();
                            notification.setDate(date);
                            notification.setType(doc.getString("type"));

                            notificationList.add(notification);

                        }
                        notificationAdapter.notifyDataSetChanged();
                        notificationAdapter.setDate(notificationList);
                        rcvNotifications.setAdapter(notificationAdapter);
                    }
                });

    }
}