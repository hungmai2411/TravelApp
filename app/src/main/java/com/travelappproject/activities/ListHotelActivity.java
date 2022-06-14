package com.travelappproject.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
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
import com.travelappproject.fragments.FilterBottomSheetFragment;
import com.travelappproject.fragments.SortBottomSheetFragment;
import com.travelappproject.model.hotel.Common;
import com.travelappproject.model.hotel.Hotel;
import com.travelappproject.model.hotel.room.Room;
import com.travelappproject.viewmodel.HotelViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ListHotelActivity extends AppCompatActivity {
    String destination;
    HotelViewModel hotelViewModel;
    RecyclerView rcvListHotel;
    HotelAdapter1 hotelAdapter;
    private int limit = 10;
    private boolean isScrolling = false;
    private boolean isLastItemReached = false;
    ExecutorService executorService;
    TextView txtTitle;
    ShimmerFrameLayout shimmerFrameLayout;
    TableLayout table;
    LinearLayout btnSort, btnFilter;
    SortBottomSheetFragment sortBottomSheetFragment;
    FilterBottomSheetFragment filterBottomSheetFragment;
    List<Hotel> hotelList = new ArrayList<>();
    boolean hasFiltered = false;
    List<Hotel> tmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_hotel);

        hotelViewModel = new ViewModelProvider(ListHotelActivity.this).get(HotelViewModel.class);
        table = findViewById(R.id.table);
        btnSort = findViewById(R.id.btnSort);
        btnFilter = findViewById(R.id.btnFilter);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        shimmerFrameLayout.startShimmer();
        executorService = Executors.newSingleThreadExecutor();
        txtTitle = findViewById(R.id.txtTitle);

        Common.start = 0;
        Common.end = 3000000;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListHotelActivity.super.onBackPressed();
            }
        });

        Intent intent = getIntent();

        if (intent != null) {
            destination = intent.getStringExtra("destination");

            if (destination.equals("Kiên Giang"))
                txtTitle.setText(getString(R.string.phu_quoc));
            else if (destination.equals("Khánh Hòa"))
                txtTitle.setText(getString(R.string.nha_trang));
            else if (destination.equals("Lâm Đồng"))
                txtTitle.setText(getString(R.string.da_lat));
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
                intent1.putExtra("id", hotel.getId());
                startActivity(intent1);
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterBottomSheetFragment = new FilterBottomSheetFragment(new FilterBottomSheetFragment.IClickItem() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onCallBack(List<Float> values) {
                        if (values.get(0).longValue() == 0 && values.get(1).longValue() == 3000000) {
                            hotelAdapter.setData(hotelList);
                            hotelAdapter.notifyDataSetChanged();
                            btnFilter.setBackgroundResource(0);
                        } else {
                            hasFiltered = true;
                            Common.start = values.get(0).longValue();
                            Common.end = values.get(1).longValue();
                            tmp = new ArrayList<>();
                            tmp = hotelList.stream().filter(t -> t.getPrice() >= values.get(0).longValue() && t.getPrice() <= values.get(1).longValue()).collect(Collectors.toList());
                            hotelAdapter.setData(tmp);
                            hotelAdapter.notifyDataSetChanged();
                            btnFilter.setBackground(getResources().getDrawable(R.drawable.custom_btn_filter));
                        }
                    }
                });
                filterBottomSheetFragment.show(getSupportFragmentManager(), "");
            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortBottomSheetFragment = new SortBottomSheetFragment(new SortBottomSheetFragment.IClickItem() {
                    @Override
                    public void onCallBack(int index) {
                        if (index == 1) {
                            sortDescend();
                            sortBottomSheetFragment.setState(1);
                            hotelAdapter.notifyDataSetChanged();
                            btnSort.setBackground(getResources().getDrawable(R.drawable.custom_btn_sort));
                        } else {
                            sortAscend();
                            sortBottomSheetFragment.setState(0);
                            hotelAdapter.notifyDataSetChanged();
                            btnSort.setBackgroundResource(0);
                        }
                    }
                });
                sortBottomSheetFragment.show(getSupportFragmentManager(), "");
            }
        });

        hotelViewModel.getList(destination);
        observe();
    }

    private void observe() {
        hotelViewModel.observedHotelLiveData().observe(this, new Observer<List<Hotel>>() {
            @Override
            public void onChanged(List<Hotel> list) {
                hotelList = list;

                sortAscend();
                hotelAdapter.setData(hotelList);
                rcvListHotel.setAdapter(hotelAdapter);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                rcvListHotel.setVisibility(View.VISIBLE);
                table.setVisibility(View.VISIBLE);
            }
        });
    }

    private void sortAscend() {
        if(hasFiltered){
            Collections.sort(tmp, new Comparator<Hotel>() {
                public int compare(Hotel obj1, Hotel obj2) {
                    return Integer.valueOf((int) obj1.getPrice()).compareTo(Integer.valueOf((int) obj2.getPrice())); // To compare string values
                }
            });
        }else {
            Collections.sort(hotelList, new Comparator<Hotel>() {
                public int compare(Hotel obj1, Hotel obj2) {
                    return Integer.valueOf((int) obj1.getPrice()).compareTo(Integer.valueOf((int) obj2.getPrice())); // To compare string values
                }
            });
        }
    }

    private void sortDescend() {
        if(hasFiltered){
            Collections.sort(tmp, new Comparator<Hotel>() {
                public int compare(Hotel obj1, Hotel obj2) {
                    return Integer.valueOf((int) obj2.getPrice()).compareTo(Integer.valueOf((int) obj1.getPrice())); // To compare string values
                }
            });
        }else {
            Collections.sort(hotelList, new Comparator<Hotel>() {
                public int compare(Hotel obj1, Hotel obj2) {
                    return Integer.valueOf((int) obj2.getPrice()).compareTo(Integer.valueOf((int) obj1.getPrice())); // To compare string values
                }
            });
        }
    }
}