package com.travelappproject.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);

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
            //List<RoomSettingFormList> roomList = mHotel.getRoomSettingFormList();

            for (int i = 0; i < hotelImageList.size(); i++) {
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tool_bar, menu);
        MenuItem favorite = menu.findItem(R.id.ic_favorite);

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

        return super.onCreateOptionsMenu(menu);
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
