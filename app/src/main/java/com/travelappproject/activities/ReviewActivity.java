package com.travelappproject.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.travelappproject.R;

public class ReviewActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        CardView review = (CardView) findViewById(R.id.review);
        TextView txtReview = findViewById(R.id.txtReview);
        EditText edtReview = findViewById(R.id.edtReview);

        ImageButton btnSend = findViewById(R.id.btnSend);

        review.setVisibility(View.GONE);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtReview.getText().equals("")) {
                    review.setVisibility(View.VISIBLE);
                    txtReview.setText(edtReview.getText());
                }
            }
        });

    }
}