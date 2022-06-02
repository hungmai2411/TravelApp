package com.travelappproject.fragments;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.travelappproject.LanguageManager;
import com.travelappproject.activities.ChooseLocationActivity;
import com.travelappproject.R;
import com.travelappproject.activities.HotelDetailActivity;
import com.travelappproject.activities.ListHotelActivity;
import com.travelappproject.activities.SearchActivity;
import com.travelappproject.adapter.HotelAdapter;
import com.travelappproject.adapter.HotelAdapter1;
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
    LanguageManager languageManager;
    ShimmerFrameLayout shimmerFrameLayout;

    public HomeFragment() {
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

        languageManager = new LanguageManager(getContext());
        languageManager.updateResource("en");
        getFragmentManager().beginTransaction().detach(HomeFragment.this).attach(HomeFragment.this).commit();

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

        shimmerFrameLayout = view.findViewById(R.id.shimmer);
        shimmerFrameLayout.startShimmer();

        listHotHotel = new ArrayList<>();
        txtCurrentLocation = view.findViewById(R.id.txtCurrentLocation);
        txtCurrentLocation.setText(state);
        rcvHotHotel = view.findViewById(R.id.rcvHotHotel);
        rcvHotHotel.setHasFixedSize(true);

        rcvNewHotel = view.findViewById(R.id.rcvNewHotel);
        btnChooseLocation = view.findViewById(R.id.btnChooseLocation);
        btnSearch = view.findViewById(R.id.btnSearch);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        rcvHotHotel.setLayoutManager(linearLayoutManager);

        hotHotelAdapter = new HotelAdapter(getContext(), new HotelAdapter.IClickItemListener() {
            @Override
            public void onClickItem(Hotel hotel) {
                Intent intent1 = new Intent(getActivity(), HotelDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("hotel", hotel);
                intent1.putExtras(bundle);
                intent1.putExtra("id",hotel.getId());
                startActivity(intent1);
            }
        });

        hotHotelAdapter.setData(listHotHotel);
        //rcvHotHotel.setAdapter(hotHotelAdapter);
        observedHotels();

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        rcvNewHotel.setLayoutManager(linearLayoutManager1);
        List<String> mListLocation = new ArrayList<String>();

        mListLocation.add("Hà Nội");
        mListLocation.add("Đà Nẵng");
        mListLocation.add("Sài Gòn");
        mListLocation.add("Phú Quốc");
        mListLocation.add("Nha Trang");
        mListLocation.add("Đà Lạt");

        List<Integer> mListImages = new ArrayList<>();

        mListImages.add(R.drawable.hn_thumbnail);
        mListImages.add(R.drawable.dn_thumbnail);
        mListImages.add(R.drawable.hcm_thumbnail);
        mListImages.add(R.drawable.pq_thumbnail);
        mListImages.add(R.drawable.nt_thumbnail);
        mListImages.add(R.drawable.dl_thumbnail);

        thumbnailAdapter = new ThumbnailAdapter(mListLocation, mListImages, getContext(), new ThumbnailAdapter.IClickDestinationListener() {
            @Override
            public void onCallBack(String destination) {
                if (destination == "Sài Gòn") {
                    Intent intent = new Intent(getContext(), ListHotelActivity.class);
                    intent.putExtra("destination", "TP Hồ Chí Minh");
                    startActivity(intent);
                } else if (destination == "Phú Quốc") {
                    Intent intent = new Intent(getContext(), ListHotelActivity.class);
                    intent.putExtra("destination", "Kiên Giang");
                    startActivity(intent);
                } else if (destination == "Nha Trang") {
                    Intent intent = new Intent(getContext(), ListHotelActivity.class);
                    intent.putExtra("destination", "Khánh Hòa");
                    startActivity(intent);
                }else if (destination == "Đà Lạt") {
                    Intent intent = new Intent(getContext(), ListHotelActivity.class);
                    intent.putExtra("destination", "Lâm Đồng");
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getContext(), ListHotelActivity.class);
                    intent.putExtra("destination", destination);
                    startActivity(intent);
                }

            }
        });

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
                startActivity(new Intent(getContext(), SearchActivity.class));
            }
        });
    }

    private void observedHotels() {
        hotelViewModel.observedHotelLiveData().observe(getViewLifecycleOwner(), new Observer<List<Hotel>>() {
            @Override
            public void onChanged(List<Hotel> listHotel) {
                for (Hotel hotel : listHotel) {
                    if (hotel.getStarRate() >= 4) {
                        listHotHotel.add(hotel);
                    }
                }

                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                rcvHotHotel.setVisibility(View.VISIBLE);
                hotHotelAdapter.notifyDataSetChanged();
                rcvHotHotel.setAdapter(hotHotelAdapter);
            }
        });
    }
}