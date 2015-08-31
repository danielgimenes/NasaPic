package br.com.dgimenes.nasapic.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.dgimenes.nasapic.fragment.APODPictureFragment;

public class BestPicturesAdapter extends FragmentStatePagerAdapter {

    private final List<Date> bestPicsDates;

    private Map<Integer, APODPictureFragment> fragmentPerPosition;

    public BestPicturesAdapter(FragmentManager supportFragmentManager, List<Date> bestPicsDates) {
        super(supportFragmentManager);
        this.bestPicsDates = bestPicsDates;
        fragmentPerPosition = new HashMap<>();
    }

    @Override
    public Fragment getItem(int position) {
        int numOfDaysToShow = 3;
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
        return bestPicsDates.size();
    }

}