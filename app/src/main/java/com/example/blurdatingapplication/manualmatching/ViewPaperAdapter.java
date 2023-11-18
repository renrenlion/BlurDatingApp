package com.example.blurdatingapplication.manualmatching;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPaperAdapter extends FragmentStateAdapter {

    public ViewPaperAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new ExploreFragment();
            case 1: return new LikesYouFragment();
            default: return new ExploreFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
