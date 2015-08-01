package br.com.dgimenes.nasapic.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.HashMap;
import java.util.Map;

import br.com.dgimenes.nasapic.fragment.APODPageFragment;

public class APODPagerAdapter extends FragmentStatePagerAdapter {

    private final int numOfDaysToShow;

    private Map<Integer, APODPageFragment> fragmentPerPosition;

    public APODPagerAdapter(FragmentManager supportFragmentManager, int numOfDaysToShow) {
        super(supportFragmentManager);
        this.numOfDaysToShow = numOfDaysToShow;
        fragmentPerPosition = new HashMap<>();
    }

    @Override
    public Fragment getItem(int position) {
        int dateOffset = (position + 1) - numOfDaysToShow;
        APODPageFragment fragment = new APODPageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(APODPageFragment.DATE_OFFSET_PARAM, dateOffset);
        fragment.setArguments(bundle);
        fragmentPerPosition.put(position, fragment);
        return fragment;
    }

    public APODPageFragment getFragment(int position) {
        return fragmentPerPosition.get(position);
    }

    @Override
    public int getCount() {
        return numOfDaysToShow;
    }

}
