package com.travelappproject.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shuhart.stepview.StepView;
import com.travelappproject.R;
import com.travelappproject.model.hotel.Hotel;
import com.travelappproject.model.hotel.User;
import com.travelappproject.model.hotel.room.Room;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ConfirmActivity extends AppCompatActivity {
    public Room room;
    public Long startDate, endDate, msDiff, daysDiff;
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    Hotel mHotel;
    RelativeLayout relative;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Button btnNext,btnLogin,btnBack;
    LinearLayout layoutSignIn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        btnLogin = findViewById(R.id.btnLogin);
        btnBack = findViewById(R.id.btnBack);

        relative = findViewById(R.id.relative);
        btnNext = findViewById(R.id.btnNext);
        layoutSignIn = findViewById(R.id.layoutSignIn);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ConfirmActivity.this, SignInActivity.class));
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (mAuth.getCurrentUser() != null) {
            layoutSignIn.setVisibility(View.GONE);
            btnNext.setVisibility(View.VISIBLE);
            relative.setVisibility(View.VISIBLE);

            db.collection("users").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot doc = task.getResult();

                    user = new User();
                    user.setName(doc.getString("name"));
                    user.setToken(doc.getString("token"));
                    user.setPhoneNumber(doc.getString("phonenumber"));

                    Intent intent = getIntent();
                    if (intent != null) {
                        mHotel = (Hotel) intent.getSerializableExtra("hotel");
                        startDate = intent.getLongExtra("startDate", 1);
                        endDate = intent.getLongExtra("endDate", 1);
                        room = (Room) intent.getSerializableExtra("room");
                    }

                    msDiff = endDate - startDate;
                    daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);

                    StepView stepView = findViewById(R.id.step_view);
                    stepView.setStepsNumber(3);
                    stepView.done(true);

                    appBarLayout = findViewById(R.id.app_bar);
                    collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
                    toolbar = findViewById(R.id.toolbar);

                    initToolBar();

                    btnNext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent1 = new Intent(ConfirmActivity.this, Confirm1Activity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("room", room);
                            bundle.putSerializable("user",user);
                            bundle.putSerializable("hotel", mHotel);
                            intent1.putExtras(bundle);
                            intent1.putExtra("startDate", startDate);
                            intent1.putExtra("endDate", endDate);
                            intent1.putExtra("daysdiff", daysDiff);
                            startActivity(intent1);
                        }
                    });

                    TextView txtBookingType, txtHotelName, txtRoomType, txtDateCheckIn, txtDateCheckOut,txtName,txtNumber;


                    txtBookingType = findViewById(R.id.txtBookingType);
                    txtHotelName = findViewById(R.id.nameHotel);
                    txtName = findViewById(R.id.txtName);
                    txtNumber = findViewById(R.id.txtNumber);
                    txtRoomType = findViewById(R.id.txtTypeRoom);
                    txtDateCheckIn = findViewById(R.id.txtDateCheckIn);
                    txtDateCheckOut = findViewById(R.id.txtDateCheckOut);

                    txtName.setText(user.getName());
                    txtNumber.setText(user.getPhoneNumber());
                    txtDateCheckOut.setText(DateFormat.format("dd/MM/yyyy", new Date(endDate)).toString());
                    txtDateCheckIn.setText(DateFormat.format("dd/MM/yyyy", new Date(startDate)).toString());
                    txtHotelName.setText(mHotel.getName());
                    txtRoomType.setText(room.getName());
                    txtBookingType.setText(String.valueOf(daysDiff) + " night");
                }
            });

        }
    }

    private void initToolBar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}