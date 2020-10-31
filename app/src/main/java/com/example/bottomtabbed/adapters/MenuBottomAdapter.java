package com.example.bottomtabbed.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MenuBottomAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> fragments;

    public MenuBottomAdapter (ArrayList<Fragment> fragments, FragmentManager fragmentManager) {
        // Required empty public constructor
        //FragmentStatePagerAdapter
        super(fragmentManager);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
