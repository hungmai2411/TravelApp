package com.travelappproject.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.travelappproject.R;
import com.travelappproject.activities.ListHotelActivity;
import com.travelappproject.adapter.SortAdapter;

import java.util.ArrayList;
import java.util.List;

public class SortBottomSheetFragment extends BottomSheetDialogFragment {
    RecyclerView rcvSort;
    SortAdapter sortAdapter;
    int index;
    String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    //create custom theme for your bottom sheet modal
    @Override
    public int getTheme() {
        //return super.getTheme();
        return R.style.AppBottomSheetDialogTheme;
    }

    public SortBottomSheetFragment() {
        // Required empty public constructor
    }

    public static SortBottomSheetFragment newInstance() {
        return new SortBottomSheetFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sort_bottom_sheet, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rcvSort = view.findViewById(R.id.rcvSort);
        LinearLayoutManager linearLayoutPaymentManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        sortAdapter = new SortAdapter(getContext(), new SortAdapter.IClickSortItem() {
            @Override
            public void onCallBack(String choice) {
                if(choice.equals("Giá tăng dần")){
                    setIndex(0);
                    dismiss();
                    Intent intent = new Intent(getContext(),ListHotelActivity.class);
                    intent.putExtra("indexSort",0);
                    intent.putExtra("destination",getState());
                    startActivity(intent);
                }else{
                    setIndex(1);
                    dismiss();
                    Intent intent = new Intent(getContext(),ListHotelActivity.class);
                    intent.putExtra("destination",getState());
                    intent.putExtra("indexSort",1);
                    startActivity(intent);
                }
            }
        }, getIndex());

        List<String> listSort = new ArrayList<>();
        listSort.add("Giá tăng dần");
        listSort.add("Giá giảm dần");

        sortAdapter.setData(listSort);
        rcvSort.setLayoutManager(linearLayoutPaymentManager);
        rcvSort.setAdapter(sortAdapter);
    }
}