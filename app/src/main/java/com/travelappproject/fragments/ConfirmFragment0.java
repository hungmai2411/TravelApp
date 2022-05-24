package com.travelappproject.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.shuhart.stepview.StepView;
import com.travelappproject.R;
import com.travelappproject.activities.ConfirmActivity;

public class ConfirmFragment0 extends Fragment {
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    ConfirmActivity confirmActivity;

    public ConfirmFragment0() {
        // Required empty public constructor
    }

    public static ConfirmFragment0 newInstance(String param1, String param2) {
        ConfirmFragment0 fragment = new ConfirmFragment0();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        confirmActivity = (ConfirmActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm0, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        StepView stepView = view.findViewById(R.id.step_view);
        stepView.setStepsNumber(3);
        stepView.done(true);

        appBarLayout = view.findViewById(R.id.app_bar);
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        toolbar = view.findViewById(R.id.toolbar);

        initToolBar();

        Button btnNext = view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.confirm_container, new ConfirmFragment1()).commit();
            }
        });

        TextView txtBookingType, txtHotelName, txtRoomType;

        txtBookingType = view.findViewById(R.id.txtBookingType);
        txtHotelName = view.findViewById(R.id.nameHotel);
        txtRoomType = view.findViewById(R.id.txtTypeRoom);

        txtHotelName.setText(confirmActivity.hotelName);
        txtRoomType.setText(confirmActivity.room.getName());
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