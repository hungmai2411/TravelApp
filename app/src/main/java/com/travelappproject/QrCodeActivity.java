package com.travelappproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QrCodeActivity extends AppCompatActivity {

    ImageView imgQr;
    String strQr;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        toolbar = findViewById(R.id.toolbar);
        imgQr = findViewById(R.id.imgQr);

        if (getIntent() != null) {
            strQr = getIntent().getStringExtra("qr");
        }

        if (!strQr.equals("")) {
            MultiFormatWriter writer = new MultiFormatWriter();
            try {
                // Initialize bit matrix
                BitMatrix matrix = writer.encode(strQr, BarcodeFormat.QR_CODE
                        , 350, 350);
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