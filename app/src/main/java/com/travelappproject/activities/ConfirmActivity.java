package com.travelappproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.travelappproject.R;
import com.travelappproject.fragments.ConfirmFragment0;
import com.travelappproject.model.hotel.room.Room;

public class ConfirmActivity extends AppCompatActivity {
    public Room room;
    public String hotelName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        Intent intent = getIntent();

        if(intent != null){
            room = (Room) intent.getSerializableExtra("room");
            hotelName = intent.getStringExtra("hotelName");
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.confirm_container, new ConfirmFragment0()).commit();
    }
}