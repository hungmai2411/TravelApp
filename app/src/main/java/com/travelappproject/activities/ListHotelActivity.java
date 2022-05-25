package com.travelappproject.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.travelappproject.R;
import com.travelappproject.adapter.HotelAdapter1;
import com.travelappproject.model.hotel.Hotel;
import com.travelappproject.viewmodel.HotelViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListHotelActivity extends AppCompatActivity {
    String destination;
    //HotelViewModel hotelViewModel;
    RecyclerView rcvListHotel;
    HotelAdapter1 hotelAdapter;
    private int limit = 10;
    private DocumentSnapshot lastVisible;
    private boolean isScrolling = false;
    private boolean isLastItemReached = false;
    ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_hotel);

        executorService = Executors.newSingleThreadExecutor();
        //hotelViewModel = new ViewModelProvider(this).get(HotelViewModel.class);

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
            //hotelViewModel.getList(destination);
            //observeListHotel();
        }

        rcvListHotel = findViewById(R.id.rcvListHotel);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvListHotel.setLayoutManager(linearLayoutManager);
        hotelAdapter = new HotelAdapter1(getApplicationContext(), new HotelAdapter1.IClickItemListener() {
            @Override
            public void onClickItem(Hotel hotel) {
                Intent intent1 = new Intent(ListHotelActivity.this, HotelDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("hotel", hotel);
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        });

        List<Hotel> hotelList = new ArrayList<>();
        hotelAdapter.setData(hotelList);
        rcvListHotel.setAdapter(hotelAdapter);

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference productsRef = rootRef.collection("Hotels");
        Query query = productsRef.whereEqualTo("provinceName", destination)
                .limit(limit);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Hotel hotelModel = document.toObject(Hotel.class);
                        hotelList.add(hotelModel);
                    }
                    hotelAdapter.notifyDataSetChanged();
                    lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);

                    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                                isScrolling = true;
                            }
                        }

                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition(); //item dau tien
                            int visibleItemCount = linearLayoutManager.getChildCount(); //item co the nhin thay trong 1 trang
                            int totalItemCount = linearLayoutManager.getItemCount(); // so luong max item

                            if (isScrolling && (firstVisibleItemPosition + visibleItemCount >= totalItemCount) && !isLastItemReached) {
                                isScrolling = false;
                                hotelAdapter.addFooterLoading();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Query nextQuery = productsRef
                                                .whereEqualTo("provinceName", destination).startAfter(lastVisible).limit(limit);
                                        nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> t) {
                                                if (t.isSuccessful()) {
                                                    List<Hotel> hotelList2 = new ArrayList<>();
                                                    for (DocumentSnapshot d : t.getResult()) {
                                                        Hotel hotelModel = d.toObject(Hotel.class);
                                                        hotelList2.add(hotelModel);
                                                    }
                                                    hotelAdapter.removeFooterLoading();
                                                    hotelList.addAll(hotelList2);
                                                    hotelAdapter.notifyDataSetChanged();
                                                    lastVisible = t.getResult().getDocuments().get(t.getResult().size() - 1);

                                                    if (t.getResult().size() < limit) {
                                                        isLastItemReached = true;
                                                    } else {
                                                        //hotelAdapter.addFooterLoading();
                                                    }
                                                }
                                            }
                                        });
                                    }
                                },2000);
                            }
                        }
                    };
                    rcvListHotel.addOnScrollListener(onScrollListener);
                }
            }
        });
    }

    private void observeListHotel() {
//        hotelViewModel.observedHotelLiveData().observe(this, new Observer<List<Hotel>>() {
//            @Override
//            public void onChanged(List<Hotel> listHotel) {
//                Log.d("size", String.valueOf(listHotel.size()));
//                hotelAdapter.setData(listHotel);
//                hotelAdapter.notifyDataSetChanged();
//                rcvListHotel.setAdapter(hotelAdapter);
//            }
//        });
    }
}