package com.travelappproject.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.travelappproject.fragments.AboutFragment;
import com.travelappproject.fragments.ReviewFragment;

public class ViewPageAdapter extends FragmentStateAdapter {
    public ViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new AboutFragment();
            case 1:
                return new ReviewFragment();
        }
        return new AboutFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}

