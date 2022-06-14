package com.travelappproject.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.travelappproject.R;
import com.travelappproject.activities.BookingDetailActivity;
import com.travelappproject.activities.HotelDetailActivity;
import com.travelappproject.activities.SignInActivity;
import com.travelappproject.adapter.BookingAdapter;
import com.travelappproject.adapter.FavoriteAdapter;
import com.travelappproject.model.hotel.Booking;
import com.travelappproject.model.hotel.Hotel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookingFragment extends Fragment {
    LinearLayout layoutSignIn;
    Button btnLogin;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    RecyclerView rcvBookings;
    BookingAdapter bookingAdapter;
    String uid;
    List<Booking> listTmp = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Toolbar toolbar;

    public BookingFragment() {
        // Required empty public constructor
    }

    public static BookingFragment newInstance(String param1, String param2) {
        BookingFragment fragment = new BookingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_booking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar);
        layoutSignIn = view.findViewById(R.id.layoutSignIn);
        btnLogin = view.findViewById(R.id.btnLogin);
        rcvBookings = view.findViewById(R.id.rcvBookings);

        if (mAuth.getCurrentUser() == null) {
            layoutSignIn.setVisibility(View.VISIBLE);
        } else {
            uid = mAuth.getUid();
            layoutSignIn.setVisibility(View.GONE);
            rcvBookings.setVisibility(View.VISIBLE);

            bookingAdapter = new BookingAdapter(getActivity(), new BookingAdapter.IClickBookingListener() {
                @Override
                public void onCallBack(Booking booking) {
                    Intent intent = new Intent(getContext(), BookingDetailActivity.class);
                    intent.putExtra("fromList",true);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("booking", booking);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
            rcvBookings.setLayoutManager(linearLayoutManager);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rcvBookings.getContext(), DividerItemDecoration.VERTICAL);
            dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
            rcvBookings.addItemDecoration(dividerItemDecoration);

            bookingAdapter.addData(listTmp);
            rcvBookings.setAdapter(bookingAdapter);

            db.collection("users/" + uid + "/booked")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for(DocumentSnapshot doc : task.getResult()){
                                Booking booking = doc.toObject(Booking.class);
                                Timestamp timestamp = (Timestamp) doc.get("timestamp");
                                Date date = timestamp.toDate();
                                booking.setDate(date);
                                booking.setIdBooking(doc.getId());
                                listTmp.add(booking);
                            }
                            bookingAdapter.notifyDataSetChanged();
                        }
                    });
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SignInActivity.class));
            }
        });
    }

    private void removeBooking(String id) {
        List<Booking> list = new ArrayList<>();

        for (Booking booking : listTmp) {
            if (booking.getIdBooking().equals(id)) {
                list.add(booking);
            }
        }

        listTmp.removeAll(list);
    }
}