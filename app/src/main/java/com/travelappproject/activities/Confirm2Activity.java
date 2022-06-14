package com.travelappproject.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shuhart.stepview.StepView;
import com.travelappproject.R;
import com.travelappproject.model.hotel.Booking;
import com.travelappproject.model.hotel.Hotel;
import com.travelappproject.model.hotel.User;
import com.travelappproject.model.hotel.room.Room;

import java.util.Date;

public class Confirm2Activity extends AppCompatActivity {

    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    Hotel mHotel;
    String bookingID;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String uid;
    Booking booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm2);

        Intent intent = getIntent();

        if(mAuth.getCurrentUser() != null){
            uid = mAuth.getCurrentUser().getUid();
        }

        if (intent != null) {
            Bundle bundle = intent.getExtras();
            bookingID = intent.getStringExtra("idBooking");
            mHotel = (Hotel) bundle.getSerializable("hotel");
        }

        appBarLayout = findViewById(R.id.app_bar);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        toolbar = findViewById(R.id.toolbar);

        initToolBar();

        StepView stepView = findViewById(R.id.step_view);
        stepView.setStepsNumber(3);
        stepView.done(true);
        stepView.go(2, true);

        Button btnShowDetail = findViewById(R.id.btnShowDetail);
        btnShowDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Confirm2Activity.this, BookingDetailActivity.class);
                db.collection("users/" + uid + "/booked/").document(bookingID)
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        booking = documentSnapshot.toObject(Booking.class);
                        Timestamp timestamp = (Timestamp) documentSnapshot.get("timestamp");
                        Date date = timestamp.toDate();
                        booking.setDate(date);
                        booking.setIdBooking(documentSnapshot.getId());

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("booking", booking);
                        intent1.putExtras(bundle);

                        startActivity(intent1);
                    }
                });
            }
        });

        TextView txtNameHotel;
        txtNameHotel = findViewById(R.id.txtNameHotel);
        txtNameHotel.setText(mHotel.getName());
    }

    private void initToolBar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
