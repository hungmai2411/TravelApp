package com.travelappproject.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.travelappproject.R;
import com.travelappproject.adapter.HotelAdapter1;
import com.travelappproject.fragments.SortBottomSheetFragment;
import com.travelappproject.model.hotel.Hotel;
import com.travelappproject.model.hotel.room.Room;
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
    TextView txtTitle;
    ShimmerFrameLayout shimmerFrameLayout;
    TableLayout table;
    LinearLayout btnSort, btnFilter;
    SortBottomSheetFragment sortBottomSheetFragment = new SortBottomSheetFragment();
    int indexSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_hotel);

        table = findViewById(R.id.table);
        btnSort = findViewById(R.id.btnSort);
        btnFilter = findViewById(R.id.btnFilter);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        shimmerFrameLayout.startShimmer();
        executorService = Executors.newSingleThreadExecutor();
        txtTitle = findViewById(R.id.txtTitle);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();

        if (intent != null) {
            indexSort = intent.getIntExtra("indexSort",0);

            if(indexSort != 0){
                btnSort.setBackground(getResources().getDrawable(R.drawable.custom_btn_sort));
            }

            destination = intent.getStringExtra("destination");

            if(destination.equals("Kiên Giang"))
                txtTitle.setText("Phú Quốc");
            else if (destination.equals("Khánh Hòa"))
                txtTitle.setText("Nha Trang");
            else if(destination.equals("Lâm Đồng"))
                txtTitle.setText("Đà Lạt");
            else
                txtTitle.setText(destination);
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
                intent1.putExtra("id",hotel.getId());
                startActivity(intent1);
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortBottomSheetFragment.setState(destination);
                sortBottomSheetFragment.show(getSupportFragmentManager(),"");
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
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    rcvListHotel.setVisibility(View.VISIBLE);
                    table.setVisibility(View.VISIBLE);
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

                            if (isScrolling && (firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReached) {
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


                                                        hotelList.add(hotelModel);
                                                    }
                                                    hotelAdapter.removeFooterLoading();
                                                    hotelAdapter.notifyDataSetChanged();

                                                    if(t.getResult().size() > 1)
                                                        lastVisible = t.getResult().getDocuments().get(t.getResult().size() - 1);

                                                    if (t.getResult().size() < limit) {
                                                        isLastItemReached = true;
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
}