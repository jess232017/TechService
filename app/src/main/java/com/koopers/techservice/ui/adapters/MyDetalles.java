package com.koopers.techservice.ui.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.koopers.techservice.App;
import com.koopers.techservice.R;
import com.koopers.techservice.data.local.entity.Equipo;
import com.koopers.techservice.ui.fragments.device.Component_Fragment;
import com.koopers.techservice.ui.fragments.device.Detail_Fragment;

public class MyDetalles extends FragmentPagerAdapter {
    private String[] tabTitles = new String[]{getAppContext().getString(R.string.detail_screen), getAppContext().getString(R.string.Componentes)};
    private final Detail_Fragment detail1;
    private final Component_Fragment detail2;

    private final int numOfTabs;

    public MyDetalles(FragmentManager fm, Equipo equipo) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        this.numOfTabs = 2;

        detail1 = Detail_Fragment.newInstance(equipo);
        detail2 = Component_Fragment.newInstance(equipo);

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return  detail1;
            case 1:
                return detail2;
        }

        return detail2;
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

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    private Context getAppContext(){
        return App.instance.getApplicationContext();
    }
}