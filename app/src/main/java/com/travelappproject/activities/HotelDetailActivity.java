package com.travelappproject.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.facebook.shimmer.ShimmerFrameLayout;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
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
import com.travelappproject.model.hotel.Booking;
import com.travelappproject.model.hotel.Hotel;
import com.travelappproject.model.hotel.Image;
import com.travelappproject.model.hotel.room.Photo;
import com.travelappproject.model.hotel.room.Room;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    LinearLayout btnCheckOut, btnCheckIn;
    TextView txtDateCheckOut, txtDateCheckIn, txtNumber;
    Long startDate, endDate, numberNight;
    String lat = "";
    NestedScrollView nestedScrollView;
    String lon = "";
    ShimmerFrameLayout shimmerFrameLayout;
    List<Room> listRoom = new ArrayList<>();
    int count = 0;
    ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);

        if (mAuth.getCurrentUser() != null) {
            uidUser = mAuth.getUid();
        }

        executorService = Executors.newSingleThreadExecutor();

        startDate = MaterialDatePicker.todayInUtcMilliseconds();

        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.DATE, 1);
        endDate = gc.getTimeInMillis();

        long msDiff = endDate - startDate;
        numberNight = TimeUnit.MILLISECONDS.toDays(msDiff);

        shimmerFrameLayout = findViewById(R.id.shimmer);
        shimmerFrameLayout.startShimmer();

        nestedScrollView = findViewById(R.id.nestedScrollView);
        txtDateCheckOut = findViewById(R.id.txtDateCheckOut);
        txtDateCheckIn = findViewById(R.id.txtDateCheckIn);

        txtDateCheckOut.setText(DateFormat.format("dd/MM", new Date(endDate)).toString());
        txtDateCheckIn.setText(DateFormat.format("dd/MM", new Date(startDate)).toString());

        txtNumber = findViewById(R.id.txtNumber);

        txtNumber.setText("(" + numberNight + " " + getString(R.string.night) + ")");

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
                                .setTitleText(R.string.selectdates)
                                .setTheme(R.style.ThemeOverlay_App_DatePicker)
                                .build();
                dateRangePicker.show(getSupportFragmentManager(), "11");
                dateRangePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long, Long> selection) {
                        startDate = selection.first;
                        endDate = selection.second;

                        long msDiff = endDate - startDate;
                        long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);

                        txtDateCheckOut.setText(DateFormat.format("dd/MM", new Date(endDate)).toString());
                        txtDateCheckIn.setText(DateFormat.format("dd/MM", new Date(startDate)).toString());
                        txtNumber.setText("(" + daysDiff + " night)");

                        count = 0;
                        listRoom.clear();

                        mFireStore.collection("Hotels/" + idHotel + "/rooms")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for (DocumentSnapshot document : task.getResult()) {
                                            Room room = new Room();

                                            List<Photo> list = (List<Photo>) document.get("photos");
                                            List<Photo> listTmp = new ArrayList<>();

                                            for (int i = 0; i < list.size(); i++) {
                                                final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
                                                final Photo pojo = mapper.convertValue(list.get(i), Photo.class);
                                                listTmp.add(pojo);
                                            }

                                            room.setRoomArea(document.getString("roomArea"));
                                            room.setId(document.getId());
                                            room.setName(document.getString("name"));
                                            room.setCancelPolicies(document.getString("cancelPolicies"));
                                            room.setFacilities(document.getString("facilities"));

                                            if (document.get("number") != null)
                                                room.setNumber((Long) document.get("number"));

                                            room.setPhotos(listTmp);
                                            room.setPrice((Long) document.get("price"));

                                            mFireStore.collection("Hotels/" + idHotel + "/booked")
                                                    .whereEqualTo("idRoom", room.getId())
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            for (DocumentSnapshot doc : task.getResult()) {
                                                                String end = DateFormat.format("dd/MM/yyyy", new Date(doc.getLong("endDate"))).toString();
                                                                String start = DateFormat.format("dd/MM/yyyy", new Date(doc.getLong("startDate"))).toString();
                                                                String startChoice = DateFormat.format("dd/MM/yyyy", new Date(startDate)).toString();

                                                                try {
                                                                    Date dateStart = new SimpleDateFormat("dd/MM/yyyy").parse(start);
                                                                    Date dateEnd = new SimpleDateFormat("dd/MM/yyyy").parse(end);
                                                                    Date dateStartChoice = new SimpleDateFormat("dd/MM/yyyy").parse(startChoice);

                                                                    if (dateStartChoice.compareTo(dateStart) == -1) {
                                                                        continue;
                                                                    } else if (dateStartChoice.compareTo(dateEnd) == -1) {
                                                                        count++;
                                                                    } else {
                                                                        room.setAvailable(true);
                                                                    }
                                                                    if (count == room.getNumber()) {
                                                                        room.setAvailable(false);
                                                                    }

                                                                } catch (ParseException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                            listRoom.add(room);
                                                            roomAdapter.notifyDataSetChanged();
                                                            count = 0;
                                                        }
                                                    });

                                            roomAdapter.notifyDataSetChanged();
                                            roomAdapter.setData(listRoom);
                                        }
                                    }
                                });
                        rcvRooms.setAdapter(roomAdapter);
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
                                .setTitleText(getString(R.string.selectdates))
                                .setTheme(R.style.ThemeOverlay_App_DatePicker)
                                .build();
                dateRangePicker.show(getSupportFragmentManager(), "11");
                dateRangePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long, Long> selection) {
                        startDate = selection.first;
                        endDate = selection.second;

                        long msDiff = endDate - startDate;
                        long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);

                        String end = DateFormat.format("dd/MM", new Date(endDate)).toString();
                        txtDateCheckOut.setText(end);
                        String start = DateFormat.format("dd/MM", new Date(startDate)).toString();
                        txtDateCheckIn.setText(start);
                        txtNumber.setText("(" + daysDiff + " night)");

                        count = 0;
                        listRoom.clear();

                        mFireStore.collection("Hotels/" + idHotel + "/rooms")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for (DocumentSnapshot document : task.getResult()) {
                                            Room room = new Room();

                                            List<Photo> list = (List<Photo>) document.get("photos");
                                            List<Photo> listTmp = new ArrayList<>();

                                            for (int i = 0; i < list.size(); i++) {
                                                final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
                                                final Photo pojo = mapper.convertValue(list.get(i), Photo.class);
                                                listTmp.add(pojo);
                                            }

                                            room.setRoomArea(document.getString("roomArea"));
                                            room.setId(document.getId());
                                            room.setName(document.getString("name"));
                                            room.setCancelPolicies(document.getString("cancelPolicies"));
                                            room.setFacilities(document.getString("facilities"));

                                            if (document.get("number") != null)
                                                room.setNumber((Long) document.get("number"));

                                            room.setPhotos(listTmp);
                                            room.setPrice((Long) document.get("price"));

                                            mFireStore.collection("Hotels/" + idHotel + "/booked")
                                                    .whereEqualTo("idRoom", room.getId())
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            for (DocumentSnapshot doc : task.getResult()) {
                                                                String end = DateFormat.format("dd/MM/yyyy", new Date(doc.getLong("endDate"))).toString();
                                                                String start = DateFormat.format("dd/MM/yyyy", new Date(doc.getLong("startDate"))).toString();
                                                                String startChoice = DateFormat.format("dd/MM/yyyy", new Date(startDate)).toString();

                                                                try {
                                                                    Date dateStart = new SimpleDateFormat("dd/MM/yyyy").parse(start);
                                                                    Date dateEnd = new SimpleDateFormat("dd/MM/yyyy").parse(end);
                                                                    Date dateStartChoice = new SimpleDateFormat("dd/MM/yyyy").parse(startChoice);

                                                                    if (dateStartChoice.compareTo(dateStart) == -1) {
                                                                        continue;
                                                                    } else if (dateStartChoice.compareTo(dateEnd) == -1) {
                                                                        count++;
                                                                    } else {
                                                                        room.setAvailable(true);
                                                                    }
                                                                    if (count == room.getNumber()) {
                                                                        room.setAvailable(false);
                                                                    }

                                                                } catch (ParseException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                            listRoom.add(room);
                                                            roomAdapter.notifyDataSetChanged();
                                                            count = 0;
                                                        }
                                                    });


                                            roomAdapter.notifyDataSetChanged();
                                            roomAdapter.setData(listRoom);
                                        }
                                    }
                                });
                        rcvRooms.setAdapter(roomAdapter);
                    }
                });
            }
        });

        initToolBar();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            idHotel = intent.getLongExtra("id", 0);
            Long id = intent.getLongExtra("id", 0);
            mFireStore.collection("Hotels/").document(String.valueOf(id))
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    hotel = documentSnapshot.toObject(Hotel.class);

                    viewPageAdapter = new ViewPageAdapter(HotelDetailActivity.this, hotel);
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

                    for (int i = 0; i < hotelImageList.size(); i++) {
                        String path = "https://statics.vntrip.vn/data-v2/hotels/" + idHotel + "/img_max/" + hotelImageList.get(i).getSlug();
                        SlideModel slideModel = new SlideModel(path, null, ScaleTypes.FIT);

                        if (i == 20) {
                            break;
                        }
                        slideModelList.add(slideModel);
                    }

                    roomAdapter = new RoomAdapter(HotelDetailActivity.this, new RoomAdapter.IClickRoomListener() {
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
                                            args.putLong("startDate", startDate);
                                            args.putLong("endDate", endDate);
                                            args.putSerializable("hotel", hotel);
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

                    listRoom = new ArrayList<>();

                    mFireStore.collection("Hotels/" + idHotel + "/rooms")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    for (DocumentSnapshot document : task.getResult()) {
                                        Room room = new Room();

                                        List<Photo> list = (List<Photo>) document.get("photos");
                                        List<Photo> listTmp = new ArrayList<>();

                                        for (int i = 0; i < list.size(); i++) {
                                            final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
                                            final Photo pojo = mapper.convertValue(list.get(i), Photo.class);
                                            listTmp.add(pojo);
                                        }

                                        room.setRoomArea(document.getString("roomArea"));
                                        room.setId(document.getId());
                                        room.setName(document.getString("name"));
                                        room.setCancelPolicies(document.getString("cancelPolicies"));
                                        room.setFacilities(document.getString("facilities"));

                                        if (document.get("number") != null)
                                            room.setNumber((Long) document.get("number"));

                                        room.setPhotos(listTmp);
                                        room.setPrice((Long) document.get("price"));

                                        mFireStore.collection("Hotels/" + idHotel + "/booked")
                                                .whereEqualTo("idRoom", room.getId())
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        for (DocumentSnapshot doc : task.getResult()) {
                                                            String end = DateFormat.format("dd/MM/yyyy", new Date(doc.getLong("endDate"))).toString();
                                                            String start = DateFormat.format("dd/MM/yyyy", new Date(doc.getLong("startDate"))).toString();
                                                            String startChoice = DateFormat.format("dd/MM/yyyy", new Date(startDate)).toString();

                                                            try {
                                                                Date dateStart = new SimpleDateFormat("dd/MM/yyyy").parse(start);
                                                                Date dateEnd = new SimpleDateFormat("dd/MM/yyyy").parse(end);
                                                                Date dateStartChoice = new SimpleDateFormat("dd/MM/yyyy").parse(startChoice);

                                                                if (dateStartChoice.compareTo(dateStart) == -1) {
                                                                    continue;
                                                                } else if (dateStartChoice.compareTo(dateEnd) == -1) {
                                                                    count++;
                                                                } else {
                                                                    room.setAvailable(true);
                                                                }
                                                                if (count == room.getNumber()) {
                                                                    room.setAvailable(false);
                                                                }

                                                            } catch (ParseException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                        listRoom.add(room);
                                                        roomAdapter.notifyDataSetChanged();
                                                        count = 0;
                                                    }
                                                });


                                        roomAdapter.notifyDataSetChanged();
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        appBarLayout.setVisibility(View.VISIBLE);
                                        nestedScrollView.setVisibility(View.VISIBLE);
                                    }
                                }
                            });

                    imageSlider.setImageList(slideModelList);
                    roomAdapter.setData(listRoom);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HotelDetailActivity.this, RecyclerView.VERTICAL, false);
                    rcvRooms.setLayoutManager(linearLayoutManager);
                    rcvRooms.setAdapter(roomAdapter);

                    viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                        @Override
                        public void onPageSelected(int position) {
                            super.onPageSelected(position);

                        }
                    });

                    new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
                        switch (position) {
                            case 0:
                                tab.setText(R.string.About);
                                break;
                            case 1:
                                tab.setText(R.string.reviews);
                                break;
                        }
                    }).attach();
                }
            });


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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tool_bar, menu);
        MenuItem favorite = menu.findItem(R.id.ic_favorite);

        if (uidUser != null) {
            mFireStore.collection("users/" + uidUser + "/favorites").document(String.valueOf(idHotel)).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error == null) {
                        if (value.exists()) {
                            favorite.setIcon(R.drawable.ic_favorite);
                        } else {
                            favorite.setIcon(R.drawable.ic_action_favorite);
                        }
                    }
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }

    private boolean isSelected = false;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ic_favorite:
                isSelected = !isSelected;
                if (isSelected) {
                    item.setIcon(R.drawable.ic_favorite);
                } else {
                    item.setIcon(R.drawable.ic_action_favorite);
                }
                addToFavorite();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void addToFavorite() {
        mFireStore.collection("users/" + uidUser + "/favorites").document(String.valueOf(hotel.getId())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.getResult().exists()) {
                    Map<String, Object> likesMap = new HashMap<>();
                    likesMap.put("timestamp", FieldValue.serverTimestamp());
                    mFireStore.collection("users/" + uidUser + "/favorites").document(String.valueOf(hotel.getId())).set(likesMap);
                } else {
                    mFireStore.collection("users/" + uidUser + "/favorites").document(String.valueOf(hotel.getId())).delete();
                }
            }
        });
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
