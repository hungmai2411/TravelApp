package com.travelappproject.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.shuhart.stepview.StepView;
import com.travelappproject.R;
import com.travelappproject.activities.BookingDetailActivity;
import com.travelappproject.activities.ConfirmActivity;

public class ConfirmFragment2 extends Fragment {

    AppBarLayout appBarLayout;
    ConfirmActivity confirmActivity;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;

    public ConfirmFragment2() {
        // Required empty public constructor
    }

    public static ConfirmFragment2 newInstance(String param1, String param2) {
        ConfirmFragment2 fragment = new ConfirmFragment2();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        confirmActivity = (ConfirmActivity) getActivity();
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appBarLayout = view.findViewById(R.id.app_bar);
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        toolbar = view.findViewById(R.id.toolbar);

        initToolBar();

        StepView stepView = view.findViewById(R.id.step_view);
        stepView.setStepsNumber(3);
        stepView.done(true);
        stepView.go(2,true);

        Button btnShowDetail = view.findViewById(R.id.btnShowDetail);
        btnShowDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), BookingDetailActivity.class));
            }
        });

        TextView txtNameHotel;
        txtNameHotel = view.findViewById(R.id.txtNameHotel);
        txtNameHotel.setText(confirmActivity.hotelName);
    }

    private void initToolBar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }
}