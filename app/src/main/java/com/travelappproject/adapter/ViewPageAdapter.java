package com.travelappproject.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.travelappproject.fragments.AboutFragment;
import com.travelappproject.fragments.ReviewFragment;
import com.travelappproject.model.hotel.Hotel;

public class ViewPageAdapter extends FragmentStateAdapter {
    Hotel hotel;
    public ViewPageAdapter(@NonNull FragmentActivity fragmentActivity, Hotel hotel) {
        super(fragmentActivity);
        this.hotel = hotel;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return AboutFragment.newInstance(hotel);
            case 1:
                return ReviewFragment.newInstance(hotel.getId());
        }
        return AboutFragment.newInstance(hotel);
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
