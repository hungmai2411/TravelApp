package com.travelappproject.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.travelappproject.R;
import com.travelappproject.adapter.RoomAdapter;
import com.travelappproject.adapter.ViewPageAdapter;
import com.travelappproject.model.hotel.Hotel;
import com.travelappproject.model.hotel.Image;
import com.travelappproject.model.hotel.room.Room;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HotelDetailActivity extends AppCompatActivity {
    Hotel hotel;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();
    private String uidUser;
    private ImageSlider imageSlider;
    TextView txtNameHotel;
    TextView txtLocationAddress;
    TextView txtNumStar;
    TextView txtNumReviews;
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ViewPageAdapter viewPageAdapter;
    RecyclerView rcvRooms;
    RoomAdapter roomAdapter;
    long idHotel;
    ImageView btnViewMap;
    LinearLayout btnCheckOut,btnCheckIn;
    TextView txtDateCheckOut,txtDateCheckIn,txtNumber;
    Long startDate,endDate,numberNight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);

        startDate = MaterialDatePicker.todayInUtcMilliseconds();

        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.DATE, 1);
        endDate = gc.getTimeInMillis();

        long msDiff = endDate - startDate;
        numberNight = TimeUnit.MILLISECONDS.toDays(msDiff);

        txtDateCheckOut = findViewById(R.id.txtDateCheckOut);
        txtDateCheckIn = findViewById(R.id.txtDateCheckIn);

        txtDateCheckOut.setText(DateFormat.format("dd/MM", new Date(endDate)).toString());
        txtDateCheckIn.setText(DateFormat.format("dd/MM", new Date(startDate)).toString());

        txtNumber = findViewById(R.id.txtNumber);

        txtNumber.setText("(" + numberNight + " night)");

        btnCheckOut = findViewById(R.id.btnCheckOut);
        btnCheckIn = findViewById(R.id.btnCheckIn);
        btnViewMap = findViewById(R.id.btnViewMap);
        appBarLayout = findViewById(R.id.app_bar);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        toolbar = findViewById(R.id.toolbar);
        txtNameHotel = findViewById(R.id.txtNameHotel);
        txtLocationAddress = findViewById(R.id.txtLocationAddress);
        txtNumStar = findViewById(R.id.txtNumStar);
        txtNumReviews = findViewById(R.id.txtNumReviews);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager2);
        imageSlider = findViewById(R.id.image_slider);
        rcvRooms = findViewById(R.id.rcvRooms);

        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarConstraints.Builder calendarConstraints = new CalendarConstraints.Builder().setValidator(DateValidatorPointForward.from(MaterialDatePicker.todayInUtcMilliseconds()));

                MaterialDatePicker<Pair<Long, Long>> dateRangePicker =
                        MaterialDatePicker.Builder.dateRangePicker()
                                .setCalendarConstraints(calendarConstraints.build())
                                .setTitleText("Select dates")
                                .build();
                dateRangePicker.show(getSupportFragmentManager(),"11");
                dateRangePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long,Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long,Long> selection) {
                        startDate = selection.first;
                        endDate = selection.second;

                        long msDiff = endDate - startDate;
                        long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);

                        txtDateCheckOut.setText(DateFormat.format("dd/MM", new Date(endDate)).toString());
                        txtDateCheckIn.setText(DateFormat.format("dd/MM", new Date(startDate)).toString());
                        txtNumber.setText("(" + daysDiff + " night)");
                    }
                });
            }
        });

        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarConstraints.Builder calendarConstraints = new CalendarConstraints.Builder().setValidator(DateValidatorPointForward.from(MaterialDatePicker.todayInUtcMilliseconds()));

                MaterialDatePicker<Pair<Long, Long>> dateRangePicker =
                        MaterialDatePicker.Builder.dateRangePicker()
                                .setCalendarConstraints(calendarConstraints.build())
                                .setTitleText("Select dates")
                                .setTheme(R.style.ThemeOverlay_App_DatePicker)
                                .build();
                dateRangePicker.show(getSupportFragmentManager(),"11");
                dateRangePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long,Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long,Long> selection) {
                        startDate = selection.first;
                        endDate = selection.second;

                        long msDiff = endDate - startDate;
                        long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);

                        txtDateCheckOut.setText(DateFormat.format("dd/MM", new Date(endDate)).toString());
                        txtDateCheckIn.setText(DateFormat.format("dd/MM", new Date(startDate)).toString());
                        txtNumber.setText("(" + daysDiff + " night)");
                    }
                });
            }
        });

        initToolBar();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String lat = "";
        String lon = "";

        if (bundle != null) {
            hotel = (Hotel) bundle.getSerializable("hotel");

            viewPageAdapter = new ViewPageAdapter(this, hotel);
            viewPager2.setAdapter(viewPageAdapter);

            lat = String.valueOf(hotel.getLocation().getLat());
            lon = String.valueOf(hotel.getLocation().getLon());

            txtNameHotel.setText(hotel.getName());
            txtLocationAddress.setText(hotel.getFullAddress());
            txtNumStar.setText(String.valueOf(hotel.getStarRate()));
            txtNumReviews.setText("0");
            idHotel = hotel.getId();
            List<SlideModel> slideModelList = new ArrayList<>();
            List<Image> hotelImageList = hotel.getImages();

            for (int i = 0; i < 20; i++) {
                String path = "https://statics.vntrip.vn/data-v2/hotels/" + idHotel + "/img_max/" + hotelImageList.get(i).getSlug();
                SlideModel slideModel = new SlideModel(path, null, ScaleTypes.FIT);
                slideModelList.add(slideModel);
            }

            List<Room> listRoom = new ArrayList<>();

            roomAdapter = new RoomAdapter(this, new RoomAdapter.IClickRoomListener() {
                @Override
                public void onCallBack(Room room) {
                    DocumentReference docRef = mFireStore.collection("Hotels/" + idHotel + "/rooms").document(String.valueOf(room.getId()));
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null) {
                                    Intent intent = new Intent(HotelDetailActivity.this, RoomDetailActivity.class);
                                    Bundle args = new Bundle();
                                    args.putLong("startDate",startDate);
                                    args.putLong("endDate",endDate);
                                    args.putString("timeCheckIn", hotel.getCheckInTime());
                                    args.putString("timeCheckOut", hotel.getCheckOutTime());
                                    args.putString("hotelName", hotel.getName());
                                    args.putString("addressHotel", hotel.getFullAddress());
                                    args.putInt("idHotel", (int) hotel.getId());
                                    args.putSerializable("room", (Serializable) room);
                                    intent.putExtras(args);
                                    startActivity(intent);
                                } else {
                                    Log.d("LOGGER", "No such document");
                                }
                            } else {
                                Log.d("LOGGER", "get failed with ", task.getException());
                            }
                        }
                    });
                }
            });

            mFireStore.collection("Hotels/" + idHotel + "/rooms")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error == null) {
                                if (!value.isEmpty()) {
                                    for (QueryDocumentSnapshot doc : value) {
                                        Room room = doc.toObject(Room.class);
                                        listRoom.add(room);
                                    }
                                    roomAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });

            imageSlider.setImageList(slideModelList);
            roomAdapter.setData(listRoom);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
            rcvRooms.setLayoutManager(linearLayoutManager);
            rcvRooms.setAdapter(roomAdapter);
        }

        String finalLat = lat;
        String finalLon = lon;
        btnViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("geo:" + finalLat + "," + finalLon + "?q=" + Uri.parse(finalLat + "," + finalLon));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("About");
                    break;
                case 1:
                    tab.setText("Reviews");
                    break;
            }
        }).attach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tool_bar, menu);
        MenuItem favorite = menu.findItem(R.id.ic_favorite);

        return super.onCreateOptionsMenu(menu);
    }

    void initToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) > 200) {
                    collapsingToolbarLayout.setTitle(txtNameHotel.getText());
                    collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
                } else {
                    collapsingToolbarLayout.setTitle("");
                }
            }
        });
    }
}
