package com.koopers.techservice.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.koopers.techservice.ui.fragments.device.Device_Fragment;

import java.util.List;

public class MyEstado extends FragmentStatePagerAdapter {
    private Device_Fragment[] fragments;
    private final int numOfTabs;

    public MyEstado(FragmentManager fm, List<String> filterby) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        this.numOfTabs = filterby.size();

        //Genera los fragments que se necesitan
        fragments = null;
        fragments = new Device_Fragment[filterby.size()];
        for(int i = 0; i< numOfTabs; i++){
            fragments[i] = Device_Fragment.newInstance(filterby.get(i));
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

    public TabLayout.OnTabSelectedListener SelectedListener(ViewPager viewPager) {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // nothing now
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // nothing now
            }
        };
    }
}