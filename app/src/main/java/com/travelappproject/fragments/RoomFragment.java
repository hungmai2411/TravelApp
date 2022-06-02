package com.travelappproject.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.travelappproject.R;
import com.travelappproject.activities.AddRoomActivity;
import com.travelappproject.activities.HotelDetailActivity;
import com.travelappproject.activities.MainActivity;
import com.travelappproject.activities.MainPartnerActivity;
import com.travelappproject.activities.RoomDetailActivity;
import com.travelappproject.adapter.RoomAdapter;
import com.travelappproject.adapter.RoomAdapter2;
import com.travelappproject.adapter.ViewPageAdapter;
import com.travelappproject.model.hotel.Hotel;
import com.travelappproject.model.hotel.Image;
import com.travelappproject.model.hotel.room.Room;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoomFragment extends Fragment {
    Toolbar toolbar;
    RecyclerView rcvRooms;
    RoomAdapter2 roomAdapter;
    List<Room> roomList;
    FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();


    public RoomFragment() {
        // Required empty public constructor
    }

    public static RoomFragment newInstance() {
        return new RoomFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_room, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar);
        ((MainPartnerActivity) getActivity()).setSupportActionBar(toolbar);
        ((MainPartnerActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);

        rcvRooms = view.findViewById(R.id.rcvRooms);

        mFireStore.collection("Hotels/").document("1428")
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                roomList = new ArrayList<>();

                roomAdapter = new RoomAdapter2(getActivity());

                mFireStore.collection("Hotels/" + 1428 + "/rooms")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error == null) {
                                    if (!value.isEmpty()) {
                                        for (QueryDocumentSnapshot doc : value) {
                                            Room room = doc.toObject(Room.class);
                                            roomList.add(room);
                                        }
                                        roomAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        });

                roomAdapter.setData(roomList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                rcvRooms.setLayoutManager(linearLayoutManager);
                rcvRooms.setAdapter(roomAdapter);
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_room, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.ic_add) {
            startActivity(new Intent(getContext(), AddRoomActivity.class));
        }

        return true;
    }
}
