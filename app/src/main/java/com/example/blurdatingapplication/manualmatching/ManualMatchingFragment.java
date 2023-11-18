package com.example.blurdatingapplication.manualmatching;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.example.blurdatingapplication.R;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

public class ManualMatchingFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ViewPaperAdapter viewPaperAdapter;


    public ManualMatchingFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manual_matching, container, false);

        tabLayout = view.findViewById(R.id.tab);
        viewPager2 = view.findViewById(R.id.viewpaper2);
        viewPaperAdapter = new ViewPaperAdapter(requireActivity());
        viewPager2.setAdapter(viewPaperAdapter);
        viewPager2.setUserInputEnabled(false);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                  viewPager2.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }
}