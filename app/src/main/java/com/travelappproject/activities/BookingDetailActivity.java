package com.travelappproject.activities;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import com.travelappproject.R;
import com.travelappproject.model.hotel.Booking;

public class BookingDetailActivity extends AppCompatActivity {

    TextView txtNameHotel,txtAddress,idBooking,dateBooking,txtDateCheckIn,txtTimeCheckIn,txtDateCheckOut,txtTimeCheckOut,nameHotel,txtTypeRoom;
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
        txtTimeCheckIn = findViewById(R.id.txtTimeCheckIn);
        txtDateCheckOut = findViewById(R.id.txtDateCheckOut);
        txtTimeCheckOut = findViewById(R.id.txtTimeCheckOut);
        nameHotel = findViewById(R.id.nameHotel);
        txtTypeRoom = findViewById(R.id.txtTypeRoom);

        Intent intent = getIntent();

        if (intent != null){
//            booking = (Booking) intent.getSerializableExtra("booking");
//            txtNameHotel.setText(booking.getHotelName());
//            nameHotel.setText(txtNameHotel.getText());
//            txtAddress.setText(booking.getAddressHotel());
//            txtDateCheckIn.setText(booking.getDateCheckIn());
//            txtDateCheckOut.setText(booking.getDateCheckOut());
//            txtTimeCheckIn.setText(booking.getTimeCheckIn());
//            txtTimeCheckOut.setText(booking.getTimeCheckOut());
//            idBooking.setText(booking.getIdBooking());
//            txtTypeRoom.setText(booking.getRoomName());
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BookingDetailActivity.this,MainActivity.class));
                //BookingDetailActivity.super.onBackPressed();
            }
        });
    }
}