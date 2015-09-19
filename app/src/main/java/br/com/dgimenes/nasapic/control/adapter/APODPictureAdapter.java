package br.com.dgimenes.nasapic.control.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import br.com.dgimenes.nasapic.control.fragment.SinglePictureFragment;

public class APODPictureAdapter extends FragmentStatePagerAdapter {

    private final int numOfDaysToShow;

    private Map<Integer, SinglePictureFragment> fragmentPerPosition;

    public APODPictureAdapter(FragmentManager supportFragmentManager, int numOfDaysToShow) {
        super(supportFragmentManager);
        this.numOfDaysToShow = numOfDaysToShow;
        fragmentPerPosition = new HashMap<>();
    }

    @Override
    public Fragment getItem(int position) {
        int dateOffset = position * -1;
        SinglePictureFragment fragment = new SinglePictureFragment();
        Calendar calendar= Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, dateOffset);
        Bundle bundle = new Bundle();
        bundle.putLong(SinglePictureFragment.DATE_PARAM, calendar.getTime().getTime());
        fragment.setArguments(bundle);
        fragmentPerPosition.put(position, fragment);
        return fragment;
    }

    public SinglePictureFragment getFragment(int position) {
        return fragmentPerPosition.get(position);
    }

    @Override
    public int getCount() {
        return numOfDaysToShow;
    }

}
