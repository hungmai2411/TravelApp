package com.travelappproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QrCodeActivity extends AppCompatActivity {

    AppCompatImageView imgQr;
    String strQr, nameHotel, time, dateStart, dateEnd, roomType;
    Toolbar toolbar;
    AppCompatTextView txtNameHotel, txtTime, txtDateStart, txtDateEnd, txtRoomType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        toolbar = findViewById(R.id.toolbar);
        imgQr = findViewById(R.id.imgQr);
        txtNameHotel = findViewById(R.id.txtNameHotel);
        txtTime = findViewById(R.id.txtTime);
        txtDateStart = findViewById(R.id.txtDateStart);
        txtDateEnd = findViewById(R.id.txtDateEnd);
        txtRoomType = findViewById(R.id.txtRoomType);

        if (getIntent() != null) {
            strQr = getIntent().getStringExtra("qr");
            nameHotel = getIntent().getStringExtra("nameHotel");
            time = getIntent().getStringExtra("dateBooking");
            dateStart = getIntent().getStringExtra("dateCheckIn");
            dateEnd = getIntent().getStringExtra("dateCheckOut");
            roomType = getIntent().getStringExtra("roomType");
        }

        txtNameHotel.setText(nameHotel);
        txtTime.setText(time);
        txtDateStart.setText(dateStart);
        txtDateEnd.setText(dateEnd);
        txtRoomType.setText(roomType);

        if (!strQr.equals("")) {
            MultiFormatWriter writer = new MultiFormatWriter();
            try {
                // Initialize bit matrix
                BitMatrix matrix = writer.encode(strQr, BarcodeFormat.QR_CODE
                        , 500, 500);
                // Initialize barcode encoder
                BarcodeEncoder encoder = new BarcodeEncoder();
                // Initialize bitmap
                Bitmap bitmap = encoder.createBitmap(matrix);
                // Set bitmap on image view
                imgQr.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}