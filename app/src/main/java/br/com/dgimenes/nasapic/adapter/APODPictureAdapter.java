package br.com.dgimenes.nasapic.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.HashMap;
import java.util.Map;

import br.com.dgimenes.nasapic.fragment.APODPictureFragment;

public class APODPictureAdapter extends FragmentStatePagerAdapter {

    private final int numOfDaysToShow;

    private Map<Integer, APODPictureFragment> fragmentPerPosition;

    public APODPictureAdapter(FragmentManager supportFragmentManager, int numOfDaysToShow) {
        super(supportFragmentManager);
        this.numOfDaysToShow = numOfDaysToShow;
        fragmentPerPosition = new HashMap<>();
    }

    @Override
    public Fragment getItem(int position) {
        int dateOffset = (position + 1) - numOfDaysToShow;
        APODPictureFragment fragment = new APODPictureFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(APODPictureFragment.DATE_OFFSET_PARAM, dateOffset);
        fragment.setArguments(bundle);
        fragmentPerPosition.put(position, fragment);
        return fragment;
    }

    public APODPictureFragment getFragment(int position) {
        return fragmentPerPosition.get(position);
    }

    @Override
    public int getCount() {
        return numOfDaysToShow;
    }

}
