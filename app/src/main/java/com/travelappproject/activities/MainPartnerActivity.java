package com.travelappproject.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.travelappproject.R;
import com.travelappproject.fragments.BookingFragment;
import com.travelappproject.fragments.FavoriteFragment;
import com.travelappproject.fragments.HomeFragment;
import com.travelappproject.fragments.ProfileFragment;
import com.travelappproject.fragments.RoomFragment;
import com.travelappproject.fragments.StatisticFragment;

public class MainPartnerActivity extends AppCompatActivity {
    BottomNavigationView bottom_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_partner);

        bottom_navigation = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_container, new RoomFragment(), null).commit();


        bottom_navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.pageRoom:
                        fragment = new RoomFragment();
                        break;
                    case R.id.pageStatistic:
                        fragment = new StatisticFragment();
                        break;
//                    case R.id.pageProfile:
//                        fragment = new ProfileFragment();
//                        break;
//                    case R.id.pageBooking:
//                        fragment = new BookingFragment();
//                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.content_container, fragment).commit();

                return true;
            }
        });

    }
}