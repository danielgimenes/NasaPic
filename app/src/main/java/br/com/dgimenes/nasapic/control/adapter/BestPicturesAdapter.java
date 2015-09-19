package br.com.dgimenes.nasapic.control.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.dgimenes.nasapic.control.fragment.SinglePictureFragment;


public class BestPicturesAdapter extends FragmentStatePagerAdapter {

    private final List<Date> bestPicsDates;

    private Map<Integer, SinglePictureFragment> fragmentPerPosition;

    public BestPicturesAdapter(FragmentManager supportFragmentManager, List<Date> bestPicsDates) {
        super(supportFragmentManager);
        this.bestPicsDates = bestPicsDates;
        fragmentPerPosition = new HashMap<>();
    }

    @Override
    public Fragment getItem(int position) {
        SinglePictureFragment fragment = new SinglePictureFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(SinglePictureFragment.DATE_PARAM, bestPicsDates.get(position).getTime());
        fragment.setArguments(bundle);
        fragmentPerPosition.put(position, fragment);
        return fragment;
    }

    public SinglePictureFragment getFragment(int position) {
        return fragmentPerPosition.get(position);
    }

    @Override
    public int getCount() {
        return bestPicsDates.size();
    }

}
