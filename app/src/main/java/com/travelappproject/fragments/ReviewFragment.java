package com.travelappproject.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.travelappproject.R;
import com.travelappproject.adapter.ReviewAdapter;
import com.travelappproject.model.hotel.Hotel;
import com.travelappproject.model.hotel.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewFragment extends Fragment {
    RecyclerView rcvReviews;
    ReviewAdapter reviewAdapter;
    List<Review> listReview;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Long idHotel;
    LinearLayout linear;
    ShimmerFrameLayout shimmer;

    public ReviewFragment() {
        // Required empty public constructor
    }

    public static ReviewFragment newInstance(Long idHotel) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle args = new Bundle();
        args.putLong("idHotel", idHotel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idHotel = getArguments().getLong("idHotel");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        shimmer = view.findViewById(R.id.shimmer);
        linear = view.findViewById(R.id.linear);
        rcvReviews = view.findViewById(R.id.rcvReviews);
        reviewAdapter = new ReviewAdapter(getContext());
        listReview = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcvReviews.setLayoutManager(linearLayoutManager);
        reviewAdapter.addList(listReview);
        rcvReviews.setAdapter(reviewAdapter);

        shimmer.startShimmer();

        db.collection("Hotels/" + idHotel + "/reviews/")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()){
                            Review review = doc.toObject(Review.class);

                            listReview.add(review);
                        }
                        reviewAdapter.notifyDataSetChanged();
                        shimmer.stopShimmer();
                        shimmer.setVisibility(View.GONE);
                        rcvReviews.setVisibility(View.VISIBLE);
                    }
                });

    }

    @Override
    public void onResume() {
        super.onResume();
        linear.requestLayout();
    }
}
