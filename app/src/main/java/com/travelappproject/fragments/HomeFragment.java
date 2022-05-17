package com.travelappproject.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.travelappproject.activities.ChooseLocationActivity;
import com.travelappproject.R;
import com.travelappproject.adapter.HotelAdapter;
import com.travelappproject.model.Hotel;
import com.travelappproject.viewmodel.HotelViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private HotelViewModel hotelViewModel;
    FirebaseAuth firebaseAuth;
    String uid;
    String state = "";
    HotelAdapter hotHotelAdapter;
    HotelAdapter newHotelAdapter;
    TextView txtCurrentLocation;
    RecyclerView rcvHotHotel;
    RecyclerView rcvNewHotel;
    LinearLayout btnChooseLocation;
    List<Hotel> listHotHotel,listNewHotel;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String state) {
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("state", state);
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            state = getArguments().getString("state");
        }

        hotelViewModel = new ViewModelProvider(getActivity()).get(HotelViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listHotHotel = new ArrayList<>();
        listNewHotel = new ArrayList<>();
        txtCurrentLocation = view.findViewById(R.id.txtCurrentLocation);
        txtCurrentLocation.setText(state);
        rcvHotHotel = view.findViewById(R.id.rcvHotHotel);
        rcvNewHotel = view.findViewById(R.id.rcvNewHotel);
        btnChooseLocation = view.findViewById(R.id.btnChooseLocation);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        rcvHotHotel.setLayoutManager(linearLayoutManager);
        hotHotelAdapter = new HotelAdapter(getContext(), new HotelAdapter.IClickItemListener() {
            @Override
            public void onClickItem(Hotel data) {

            }
        });

        hotelViewModel.getList((String) txtCurrentLocation.getText());
        observedHotels();

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        rcvNewHotel.setLayoutManager(linearLayoutManager1);
        newHotelAdapter = new HotelAdapter(getContext(), new HotelAdapter.IClickItemListener() {
            @Override
            public void onClickItem(Hotel data) {

            }
        });

        btnChooseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ChooseLocationActivity.class));
            }
        });

    }

    private boolean checkExist(List<Hotel> list, Hotel hotel){
        for(Hotel hotel1 : list){
            if(hotel1.getName() == hotel.getName())
                return true;
        }

        return false;
    }

    private void observedHotels() {
        hotelViewModel.observedHotelLiveData().observe(getViewLifecycleOwner(), new Observer<List<Hotel>>() {
            @Override
            public void onChanged(List<Hotel> listHotel) {
                Log.d("size", String.valueOf(listHotel.size()));

                for(Hotel hotel : listHotel){
                    if(hotel.getStarRating() >= 4 && checkExist(listHotHotel,hotel) == false){
                        Log.d("name", hotel.getName());
                        Log.d("existHot", String.valueOf(checkExist(listHotHotel,hotel)));
                        listHotHotel.add(hotel);
                    }
                    if(hotel.getNewHotel() && checkExist(listNewHotel,hotel) == false){
                        Log.d("name", hotel.getName());
                        Log.d("existNew", String.valueOf(checkExist(listNewHotel,hotel)));
                        listNewHotel.add(hotel);
                    }
                }
                newHotelAdapter.setData(listNewHotel);
                rcvNewHotel.setAdapter(newHotelAdapter);

                hotHotelAdapter.setData(listHotHotel);
                rcvHotHotel.setAdapter(hotHotelAdapter);
            }
        });
    }
}