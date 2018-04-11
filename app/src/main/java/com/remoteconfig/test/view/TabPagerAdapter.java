package com.remoteconfig.test.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.remoteconfig.test.model.TabConfig;

import java.util.List;


public class TabPagerAdapter extends FragmentStatePagerAdapter {

    private final List<TabConfig> tabs;

    public TabPagerAdapter(FragmentManager fm, List<TabConfig> tabs) {
        super(fm);

        this.tabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        return PagerFragment.newInstance(tabs.get(position).text);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }
}

