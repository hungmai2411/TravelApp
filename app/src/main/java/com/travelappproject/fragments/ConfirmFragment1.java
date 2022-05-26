package com.travelappproject.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.travelappproject.R;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.shuhart.stepview.StepView;
import com.travelappproject.activities.ConfirmActivity;
import com.travelappproject.adapter.PaymentAdapter;
import com.travelappproject.model.hotel.Payment;

import java.util.ArrayList;
import java.util.List;

public class ConfirmFragment1 extends Fragment {
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    RecyclerView rcvPaymentMethod;
    ConfirmActivity confirmActivity;

    public ConfirmFragment1() {
        // Required empty public constructor
    }

    public static ConfirmFragment1 newInstance(String param1, String param2) {
        ConfirmFragment1 fragment = new ConfirmFragment1();
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
        return inflater.inflate(R.layout.fragment_confirm1, container, false);
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
        stepView.go(1,true);

        Button btnConfirm = view.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.confirm_container, new ConfirmFragment2()).commit();
            }
        });

        rcvPaymentMethod = view.findViewById(R.id.rcvPaymentMethod);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rcvPaymentMethod.setLayoutManager(linearLayoutManager1);
        PaymentAdapter paymentAdapter = new PaymentAdapter(getContext());
        List<Payment> paymentList = new ArrayList<>();
        paymentList.add(new Payment("Payment at hotel",R.drawable.review));
        paymentList.add(new Payment("Payment with Zalo Pay",R.drawable.zalopay));
        paymentAdapter.setData(paymentList);
        rcvPaymentMethod.setAdapter(paymentAdapter);

        TextView txtBookingType, txtHotelName, txtRoomType,txtPrice,txtCancelPolicy;

        txtBookingType = view.findViewById(R.id.txtBookingType);
        txtHotelName = view.findViewById(R.id.nameHotel);
        txtRoomType = view.findViewById(R.id.txtTypeRoom);
        txtPrice = view.findViewById(R.id.txtPrice);
        txtCancelPolicy = view.findViewById(R.id.txtCancelPolicy);

        txtCancelPolicy.setText(confirmActivity.room.getCancelPolicies());
        txtPrice.setText(String.valueOf(confirmActivity.room.getPrice() * confirmActivity.daysDiff));
        txtHotelName.setText(confirmActivity.hotelName);
        txtRoomType.setText(confirmActivity.room.getName());
        txtBookingType.setText(String.valueOf(confirmActivity.daysDiff) + " night");
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