package com.travelappproject.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.travelappproject.R;
import com.travelappproject.model.hotel.room.Room;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomDetailActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String uid;
    int idHotel;
    String hotelName;
    Room room;
    TextView txtDateCheckIn,txtDateCheckOut,txtTimeCheckIn,txtTimeCheckOut,txtFacility;
    String timeCheckIn, timeCheckOut;
    String addressHotel;
    Toolbar toolbar;
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);

        toolbar = findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.app_bar);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);

        if(mAuth.getCurrentUser() != null)
            uid = mAuth.getCurrentUser().getUid();

        Bundle bundle = getIntent().getExtras();

        if (bundle == null)
            return;

        timeCheckIn = bundle.getString("timeCheckIn");
        timeCheckOut = bundle.getString("timeCheckOut");
        addressHotel = bundle.getString("addressHotel");
        hotelName = bundle.getString("hotelName");
        idHotel = bundle.getInt("idHotel");
        room = (Room) bundle.get("room");

        txtDateCheckIn = findViewById(R.id.txtDateCheckIn);
        txtDateCheckOut = findViewById(R.id.txtDateCheckOut);
        txtTimeCheckIn = findViewById(R.id.txtTimeCheckIn);
        txtTimeCheckOut = findViewById(R.id.txtTimeCheckOut);
        txtFacility = findViewById(R.id.txtFacility);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();

        txtDateCheckIn.setText(now.format(dtf));
        txtTimeCheckIn.setText(timeCheckIn);

        txtDateCheckOut.setText(now.plusDays(1).format(dtf));
        txtTimeCheckOut.setText(timeCheckOut);

        if(room.getFacilities() != null)
            txtFacility.setText(room.getFacilities());

        TextView txtName = findViewById(R.id.txtName);
        ImageSlider imageSlider = findViewById(R.id.image_slider);

        txtName.setText(room.getName());
        List<SlideModel> slideModelList = new ArrayList<>();

        for (int i = 0; i < room.getPhotos().size(); i++) {
            SlideModel slideModel = new SlideModel(room.getPhotos().get(i).getRoomImage(), null, ScaleTypes.CENTER_CROP);
            slideModelList.add(slideModel);
        }

        TextView txtPrice = findViewById(R.id.txtPrice);
        txtPrice.setText(String.valueOf(room.getPrice()));
        imageSlider.setImageList(slideModelList);

        initToolBar();

        Button btnBookNow = findViewById(R.id.btnBook);
        btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoomDetailActivity.this,ConfirmActivity.class);

                Bundle args = new Bundle();

                args.putString("hotelName", hotelName);
                args.putSerializable("room", (Serializable) room);
                intent.putExtras(args);

                startActivity(intent);
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
                    collapsingToolbarLayout.setTitle(room.getName());
                    collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
                } else {
                    collapsingToolbarLayout.setTitle("");
                }
            }
        });
    }

}