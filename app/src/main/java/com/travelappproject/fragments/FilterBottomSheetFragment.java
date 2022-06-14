package com.travelappproject.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.slider.RangeSlider;
import com.travelappproject.HandleCurrency;
import com.travelappproject.R;
import com.travelappproject.model.hotel.Common;

import java.util.List;

public class FilterBottomSheetFragment extends BottomSheetDialogFragment {

    RangeSlider rangeSlider;
    TextView txtStart, txtEnd;
    IClickItem iClickItem;

    public FilterBottomSheetFragment(IClickItem iClickItem) {
        this.iClickItem = iClickItem;
        // Required empty public constructor
    }

    //create custom theme for your bottom sheet modal
    @Override
    public int getTheme() {
        //return super.getTheme();
        return R.style.AppBottomSheetDialogTheme;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rangeSlider = view.findViewById(R.id.rangeSlider);
        txtStart = view.findViewById(R.id.txtStart);
        txtEnd = view.findViewById(R.id.txtEnd);

        rangeSlider.setValues(new Float[]{Common.start,Common.end});

        Long start = rangeSlider.getValues().get(0).longValue();
        Long end = rangeSlider.getValues().get(1).longValue();

        txtStart.setText(new HandleCurrency().handle(start));
        txtEnd.setText(new HandleCurrency().handle(end));

        rangeSlider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                iClickItem.onCallBack(slider.getValues());
            }
        });

        rangeSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                Common.start = slider.getValues().get(0);
                Common.end = slider.getValues().get(1);

                long start = slider.getValues().get(0).longValue();
                long end = slider.getValues().get(1).longValue();

                txtStart.setText(new HandleCurrency().handle(start));
                txtEnd.setText(new HandleCurrency().handle(end));
            }
        });
    }

    public interface IClickItem{
        void onCallBack(List<Float> values);
    }
}
