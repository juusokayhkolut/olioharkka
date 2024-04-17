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
        DataManager.getInstance().setData("municipality", mData[0]);

        switch (position) {
            case 0:
                return new MunicipalityDetailsFragment();
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
