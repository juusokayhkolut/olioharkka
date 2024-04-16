package com.example.olioharkka;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class TabPagerAdapter extends FragmentStateAdapter {
    private final ViewPager2 viewPager;
    private String[] mData; // Data to be passed to fragments

    public TabPagerAdapter(@NonNull FragmentActivity fragmentActivity, String[] dataToPass) {
        super(fragmentActivity);
        viewPager = fragmentActivity.findViewById(R.id.view_pager);
        mData = dataToPass;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                MunicipalityDetailsFragment fragment = new MunicipalityDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("municipality", mData[0]); // Replace "key" with your key and "value" with the actual data
                fragment.setArguments(bundle);

                return fragment;
            case 1:
                return new CompareFragment();
            case 2:
                return new QuizFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Number of tabs
    }

    public void setSwipeEnabled(boolean enabled) {
        // Enable or disable swiping between tabs
        viewPager.setUserInputEnabled(enabled);
    }
}
