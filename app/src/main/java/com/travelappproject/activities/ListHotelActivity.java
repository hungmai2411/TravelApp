package com.travelappproject.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.travelappproject.R;
import com.travelappproject.adapter.HotelAdapter1;
import com.travelappproject.model.hotel.Hotel;
import com.travelappproject.viewmodel.HotelViewModel;

import java.util.List;

public class ListHotelActivity extends AppCompatActivity {
    String destination;
    HotelViewModel hotelViewModel;
    RecyclerView rcvListHotel;
    HotelAdapter1 hotelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_hotel);

        hotelViewModel = new ViewModelProvider(this).get(HotelViewModel.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();

        if (intent != null) {
            destination = intent.getStringExtra("destination");
            toolbar.setTitle(destination);
            hotelViewModel.getList(destination);
            observeListHotel();
        }

        rcvListHotel = findViewById(R.id.rcvListHotel);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvListHotel.setLayoutManager(linearLayoutManager);
        hotelAdapter = new HotelAdapter1(getApplicationContext(), new HotelAdapter1.IClickItemListener() {
            @Override
            public void onClickItem(Hotel hotel) {
                Intent intent1 = new Intent(ListHotelActivity.this,HotelDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("hotel", hotel);
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        });
    }

    private void observeListHotel() {
        hotelViewModel.observedHotelLiveData().observe(this, new Observer<List<Hotel>>() {
            @Override
            public void onChanged(List<Hotel> listHotel) {
                Log.d("size", String.valueOf(listHotel.size()));
                hotelAdapter.setData(listHotel);
                hotelAdapter.notifyDataSetChanged();
                rcvListHotel.setAdapter(hotelAdapter);
            }
        });
    }
}