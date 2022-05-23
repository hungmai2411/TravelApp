package com.travelappproject.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.travelappproject.R;
import com.travelappproject.adapter.FacilityAdapter;
import com.travelappproject.model.hotel.Hotel;
import com.travelappproject.viewmodel.HotelViewModel;

public class AboutFragment extends Fragment {
    Hotel mHotel;
    RecyclerView rcvFacilities;
    FacilityAdapter facilityAdapter;
    TextView txtDetail;

    public AboutFragment() {
        // Required empty public constructor
    }

    public static AboutFragment newInstance(Hotel hotel) {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        args.putSerializable("hotel",hotel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mHotel = (Hotel) getArguments().getSerializable("hotel");
            Log.d("name",mHotel.getName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtDetail = view.findViewById(R.id.txtDetail);

        if(mHotel.getDesContent() != null)
            txtDetail.setText(Html.fromHtml(Html.fromHtml((String) mHotel.getDesContent()).toString()));
        else
            txtDetail.setText("Đang cập nhập thông tin về khách sạn");
    }
}