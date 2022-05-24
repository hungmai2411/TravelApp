package com.travelappproject.fragments;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.travelappproject.activities.ChooseLocationActivity;
import com.travelappproject.R;
import com.travelappproject.adapter.HotelAdapter;
import com.travelappproject.adapter.ThumbnailAdapter;
import com.travelappproject.model.hotel.Hotel;
import com.travelappproject.viewmodel.HotelViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private HotelViewModel hotelViewModel;
    FirebaseAuth firebaseAuth;
    String uid;
    String state = "";
    HotelAdapter hotHotelAdapter;
    ThumbnailAdapter thumbnailAdapter;
    TextView txtCurrentLocation;
    RecyclerView rcvHotHotel;
    RecyclerView rcvNewHotel;
    LinearLayout btnChooseLocation;
    List<Hotel> listHotHotel;
    ImageButton btnSearch;

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
        txtCurrentLocation = view.findViewById(R.id.txtCurrentLocation);
        txtCurrentLocation.setText(state);
        rcvHotHotel = view.findViewById(R.id.rcvHotHotel);
        rcvNewHotel = view.findViewById(R.id.rcvNewHotel);
        btnChooseLocation = view.findViewById(R.id.btnChooseLocation);
        btnSearch = view.findViewById(R.id.btnSearch);

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
        List<String> mListLocation = new ArrayList<String>();

        mListLocation.add("Hà Nội");
        mListLocation.add("Đà Nẵng");
        mListLocation.add("Hội An");
        mListLocation.add("Sài Gòn");
        mListLocation.add("Phú Quốc");
        mListLocation.add("Nha Trang");
        mListLocation.add("Đà Lạt");

        List<Integer> mListImages = new ArrayList<>();

        mListImages.add(R.drawable.hn_thumbnail);
        mListImages.add(R.drawable.dn_thumbnail);
        mListImages.add(R.drawable.ha_thumbnail);
        mListImages.add(R.drawable.hcm_thumbnail);
        mListImages.add(R.drawable.pq_thumbnail);
        mListImages.add(R.drawable.nt_thumbnail);
        mListImages.add(R.drawable.dl_thumbnail);

        thumbnailAdapter = new ThumbnailAdapter(mListLocation, mListImages, getContext());
        rcvNewHotel.setAdapter(thumbnailAdapter);

        btnChooseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ChooseLocationActivity.class));
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchFragment searchFragment = new SearchFragment();
                //startActivity(new Intent(getContext(), SearchFragment.class));
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_container, searchFragment).commit();
            }
        });

    }

    private boolean checkExist(List<Hotel> list, Hotel hotel) {
        for (Hotel hotel1 : list) {
            if (hotel1.getName() == hotel.getName())
                return true;
        }

        return false;
    }

    private void observedHotels() {
        hotelViewModel.observedHotelLiveData().observe(getViewLifecycleOwner(), new Observer<List<Hotel>>() {
            @Override
            public void onChanged(List<Hotel> listHotel) {
                Log.d("size", String.valueOf(listHotel.size()));

                for (Hotel hotel : listHotel) {
                    if (hotel.getStarRate() >= 4 && checkExist(listHotHotel, hotel) == false) {
                        Log.d("name", hotel.getName());
                        Log.d("existHot", String.valueOf(checkExist(listHotHotel, hotel)));
                        listHotHotel.add(hotel);
                    }

                }

                hotHotelAdapter.setData(listHotHotel);
                rcvHotHotel.setAdapter(hotHotelAdapter);
            }
        });
    }
}