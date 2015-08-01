package br.com.dgimenes.nasapic.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import br.com.dgimenes.nasapic.fragment.APODPageFragment;

public class APODPagerAdapter extends FragmentStatePagerAdapter {

    private final int numOfDaysToShow;

    public APODPagerAdapter(FragmentManager supportFragmentManager, int numOfDaysToShow) {
        super(supportFragmentManager);
        this.numOfDaysToShow = numOfDaysToShow;
    }

    @Override
    public Fragment getItem(int position) {
        int dateOffset = (position + 1) - numOfDaysToShow;
        Fragment fragment = new APODPageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(APODPageFragment.DATE_OFFSET_PARAM, dateOffset);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return numOfDaysToShow;
    }

}
