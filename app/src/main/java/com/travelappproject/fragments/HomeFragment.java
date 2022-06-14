package com.travelappproject.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.travelappproject.LanguageManager;
import com.travelappproject.SharedPreferences.LocalDataManager;
import com.travelappproject.activities.ChooseLocationActivity;
import com.travelappproject.R;
import com.travelappproject.activities.HotelDetailActivity;
import com.travelappproject.activities.ListHotelActivity;
import com.travelappproject.activities.NotificationActivity;
import com.travelappproject.activities.SearchActivity;
import com.travelappproject.adapter.HotelAdapter;
import com.travelappproject.adapter.ThumbnailAdapter;
import com.travelappproject.model.hotel.Banner;
import com.travelappproject.model.hotel.Hotel;
import com.travelappproject.model.hotel.Image;
import com.travelappproject.viewmodel.HotelViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {
    private HotelViewModel hotelViewModel;
    String uid;
    String state = "";
    HotelAdapter hotHotelAdapter;
    ThumbnailAdapter thumbnailAdapter;
    TextView txtCurrentLocation;
    RecyclerView rcvHotHotel;
    RecyclerView rcvNewHotel;
    LinearLayout btnChooseLocation;
    List<Hotel> listHotHotel;
    ImageButton btnSearch,btnNotification;
    LanguageManager languageManager;
    ShimmerFrameLayout shimmerFrameLayout;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    CardView redDot;
    ExecutorService executorService;
    ImageSlider imageSlider;
    Banner banner;

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

        executorService = Executors.newSingleThreadExecutor();
        hotelViewModel = new ViewModelProvider(getActivity()).get(HotelViewModel.class);

        languageManager = new LanguageManager(getContext());
        String language = LocalDataManager.getLanguage();
        languageManager.updateResource(language);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(mAuth.getCurrentUser() != null) {
            uid = mAuth.getUid();
        }

        imageSlider = view.findViewById(R.id.image_slider);

        observedBanners();
        redDot = view.findViewById(R.id.redDot);
        shimmerFrameLayout = view.findViewById(R.id.shimmer);
        shimmerFrameLayout.startShimmer();

        listHotHotel = new ArrayList<>();
        txtCurrentLocation = view.findViewById(R.id.txtCurrentLocation);
        txtCurrentLocation.setText(state);
        rcvHotHotel = view.findViewById(R.id.rcvHotHotel);
        rcvHotHotel.setHasFixedSize(true);
        btnNotification = view.findViewById(R.id.btnNotification);
        rcvNewHotel = view.findViewById(R.id.rcvNewHotel);
        btnChooseLocation = view.findViewById(R.id.btnChooseLocation);
        btnSearch = view.findViewById(R.id.btnSearch);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        rcvHotHotel.setLayoutManager(linearLayoutManager);

        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        db.collection("users/" + uid + "/notifications")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for(DocumentSnapshot doc: task.getResult()){
                                            HashMap<String,Object> hashMap = new HashMap<>();
                                            hashMap.put("hasSeen",true);
                                            db.collection("users/" + uid + "/notifications")
                                                    .document(doc.getId()).update(hashMap)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                        }
                                                    });
                                        }
                                    }
                                });
                    }
                });

                startActivity(new Intent(getContext(), NotificationActivity.class));
                redDot.setVisibility(View.GONE);
            }
        });

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

        mListLocation.add(getString(R.string.ha_noi));
        mListLocation.add(getString(R.string.da_nang));
        mListLocation.add(getString(R.string.sai_gon));
        mListLocation.add(getString(R.string.phu_quoc));
        mListLocation.add(getString(R.string.nha_trang));
        mListLocation.add(getString(R.string.da_lat));

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
                if(destination.equals(getString(R.string.sai_gon)))
                    destination = "Sài Gòn";
                else if(destination.equals(getString(R.string.da_nang)))
                    destination = "Đà Nẵng";
                else if(destination.equals(getString(R.string.ha_noi)))
                    destination = "Hà Nội";
                else if(destination.equals(getString(R.string.phu_quoc)))
                    destination = "Phú Quốc";
                else if(destination.equals(getString(R.string.nha_trang)))
                    destination = "Nha Trang";
                else if(destination.equals(getString(R.string.da_lat)))
                    destination = "Đà Lạt";

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



        db.collection("users/" + uid + "/notifications")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("Notification Activity", "listen:error", e);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            DocumentSnapshot documentSnapshot = dc.getDocument();
                            switch (dc.getType()) {
                                case ADDED:
                                    if(documentSnapshot.getBoolean("hasSeen") == false)
                                        redDot.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }
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

    private void observedBanners() {
        hotelViewModel.observedBannerLiveData().observe(getViewLifecycleOwner(), new Observer<Banner>() {
            @Override
            public void onChanged(Banner banner) {
                imageSlider.setVisibility(View.VISIBLE);
                List<SlideModel> slideModelList = new ArrayList<>();

                
                for(String s : banner.getImages()){
                    SlideModel slideModel = new SlideModel(s, null, ScaleTypes.CENTER_CROP);
                    slideModelList.add(slideModel);
                }

                imageSlider.setImageList(slideModelList);
            }
        });
    }
}