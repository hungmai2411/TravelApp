package com.travelappproject.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.travelappproject.fragments.OnBoarding1Fragment;
import com.travelappproject.fragments.OnBoarding2Fragment;
import com.travelappproject.fragments.OnBoarding3Fragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter{
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new OnBoarding1Fragment();
            case 1:
                return new OnBoarding2Fragment();
            case 2:
                return new OnBoarding3Fragment();
            default:
                return new OnBoarding1Fragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
