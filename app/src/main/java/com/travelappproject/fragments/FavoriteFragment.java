package com.travelappproject.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.travelappproject.R;
import com.travelappproject.activities.HotelDetailActivity;
import com.travelappproject.activities.ListHotelActivity;
import com.travelappproject.activities.SignInActivity;
import com.travelappproject.adapter.FavoriteAdapter;
import com.travelappproject.model.hotel.Hotel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FavoriteFragment extends Fragment {
    LinearLayout layoutSignIn;
    Button btnLogin;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    RecyclerView rcvFavorite;
    FavoriteAdapter favoriteAdapter;
    String uid;
    List<Hotel> listTmp = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ExecutorService executorService;
    Toolbar toolbar;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    public static FavoriteFragment newInstance(String param1, String param2) {
        return new FavoriteFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar);
        layoutSignIn = view.findViewById(R.id.layoutSignIn);
        btnLogin = view.findViewById(R.id.btnLogin);
        rcvFavorite = view.findViewById(R.id.rcvFavorite);

        if (mAuth.getCurrentUser() == null) {
            layoutSignIn.setVisibility(View.VISIBLE);
        } else {
            uid = mAuth.getUid();
            layoutSignIn.setVisibility(View.GONE);

            favoriteAdapter = new FavoriteAdapter(getActivity(), uid, new FavoriteAdapter.IClickItemListener() {
                @Override
                public void onClickItem(Hotel hotel) {
                    Intent intent1 = new Intent(getContext(), HotelDetailActivity.class);
                    intent1.putExtra("id",hotel.getId());
                    startActivity(intent1);
                }
            });

            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false);
            rcvFavorite.setLayoutManager(gridLayoutManager);

            getData();
            favoriteAdapter.setData(listTmp);
            rcvFavorite.setAdapter(favoriteAdapter);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SignInActivity.class));
            }
        });
    }

    void getData() {
        listTmp.clear();

        db.collection("users/" + uid + "/favorites")
                .orderBy("timestamp")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error == null) {
                            if (!value.isEmpty()) {
                                for (QueryDocumentSnapshot doc : value) {
                                    int sn = Integer.valueOf(doc.getId());

                                    executorService.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            db.collection("Hotels")
                                                    .document(String.valueOf(sn))
                                                    .get()
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                            Hotel hotel = documentSnapshot.toObject(Hotel.class);
                                                            if (!isExisted(hotel)) {
                                                                listTmp.add(hotel);
                                                            }
                                                            favoriteAdapter.notifyDataSetChanged();
                                                        }
                                                    });
                                        }
                                    });
                                }
                                favoriteAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    private boolean isExisted(Hotel hotel) {
        for (Hotel data : listTmp) {
            if (hotel.getId() == data.getId()) {
                return true;
            }
        }
        return false;
    }
}