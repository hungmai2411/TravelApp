package com.travelappproject.activities;

import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.travelappproject.HandleCurrency;
import com.travelappproject.R;
import com.travelappproject.model.hotel.Booking;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BookingDetailActivity extends AppCompatActivity {

    TextView txtNameHotel, txtAddress, idBooking, dateBooking, txtDateCheckIn, txtChoice, txtDateCheckOut, txtPrice, nameHotel, txtTypeRoom;
    Booking booking;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);

        toolbar = findViewById(R.id.toolbar);
        txtNameHotel = findViewById(R.id.txtNameHotel);
        txtAddress = findViewById(R.id.txtAddress);
        idBooking = findViewById(R.id.idBooking);
        dateBooking = findViewById(R.id.dateBooking);
        txtDateCheckIn = findViewById(R.id.txtDateCheckIn);
        txtDateCheckOut = findViewById(R.id.txtDateCheckOut);
        nameHotel = findViewById(R.id.nameHotel);
        txtTypeRoom = findViewById(R.id.txtTypeRoom);
        txtChoice = findViewById(R.id.txtChoice);
        txtPrice = findViewById(R.id.txtPrice);

        Intent intent = getIntent();

        if (intent != null) {
            booking = (Booking) intent.getSerializableExtra("booking");

            if (booking != null) {
                txtPrice.setText(new HandleCurrency().handle(booking.getPrice()));
                txtChoice.setText(booking.getChoice());
                txtNameHotel.setText(booking.getHotelName());
                nameHotel.setText(txtNameHotel.getText());
                txtAddress.setText(booking.getAddressHotel());
                txtDateCheckIn.setText(android.text.format.DateFormat.format("dd/MM", new Date(booking.getStartDate())));
                txtDateCheckOut.setText(android.text.format.DateFormat.format("dd/MM", new Date(booking.getEndDate())));
                idBooking.setText(booking.getIdBooking());
                Date date = booking.getDate();
                DateFormat dateFormat = new SimpleDateFormat("hh:mm dd-MM-yyyy");
                String strDate = dateFormat.format(date);

                dateBooking.setText(strDate);
                txtTypeRoom.setText(booking.getNameRoom());
            }
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BookingDetailActivity.this, MainActivity.class));
            }
        });
    }
}