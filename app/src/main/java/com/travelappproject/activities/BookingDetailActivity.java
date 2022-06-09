package com.travelappproject.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.travelappproject.HandleCurrency;
import com.travelappproject.QrCodeActivity;
import com.travelappproject.R;
import com.travelappproject.model.hotel.Booking;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class BookingDetailActivity extends AppCompatActivity {

    TextView txtStatusPayment,txtStatus,txtNameHotel, txtAddress, idBooking, dateBooking, txtDateCheckIn, txtChoice, txtDateCheckOut, txtPrice, nameHotel, txtTypeRoom;
    Booking booking;
    Toolbar toolbar;
    RelativeLayout relative;
    Dialog dialog;
    Button btnCancel,btnGetQr;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);

        if(auth.getCurrentUser() != null){
            uid = auth.getUid();
        }

        btnGetQr = findViewById(R.id.btnGetQr);
        btnCancel = findViewById(R.id.btnCancel);
        relative = findViewById(R.id.relative);
        txtStatusPayment = findViewById(R.id.txtStatusPayment);
        txtStatus = findViewById(R.id.txtStatus);
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

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_cancel);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
        }

        dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels*0.90), ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false); //Optional
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

        if (intent != null) {
            booking = (Booking) intent.getSerializableExtra("booking");

            if (booking != null) {
                txtStatus.setText(booking.getStatus());

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

        if(txtStatus.getText().toString().equals("Booked")){
            txtStatus.setTextColor(getResources().getColor(R.color.booked_text));
            txtStatus.setBackgroundColor(getResources().getColor(R.color.booked_color));
        }else{
            relative.setVisibility(View.GONE);
            txtStatus.setTextColor(getResources().getColor(R.color.cancelled_text));
            txtStatus.setBackgroundColor(getResources().getColor(R.color.cancelled_color));
        }

        btnGetQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(BookingDetailActivity.this, QrCodeActivity.class);
                intent1.putExtra("qr",idBooking.getText());
                intent1.putExtra("nameHotel", txtNameHotel.getText());
                intent1.putExtra("dateBooking", dateBooking.getText());
                intent1.putExtra("dateCheckIn", txtDateCheckIn.getText());
                intent1.putExtra("dateCheckOut", txtDateCheckOut.getText());
                intent1.putExtra("roomType", txtTypeRoom.getText());
                startActivity(intent1);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        Button btnNo = dialog.findViewById(R.id.btn_cancel);
        Button btnYes = dialog.findViewById(R.id.btn_okey);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, Object> map = new HashMap<>();
                map.put("status", "Cancelled");

                db.collection("users/" + uid + "/booked")
                        .document(booking.getIdBooking())
                        .update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dialog.dismiss();
                        txtStatus.setText("Cancelled");
                        relative.setVisibility(View.GONE);
                        txtStatus.setTextColor(getResources().getColor(R.color.cancelled_text));
                        txtStatus.setBackgroundColor(getResources().getColor(R.color.cancelled_color));
                        Toast.makeText(BookingDetailActivity.this, "BOOKING CANCELLED", Toast.LENGTH_SHORT).show();
                    }
                });

                db.collection("Hotels/" + booking.getIdHotel() + "/booked")
                        .document(booking.getIdBooking())
                        .update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BookingDetailActivity.this, MainActivity.class));
            }
        });
    }
}