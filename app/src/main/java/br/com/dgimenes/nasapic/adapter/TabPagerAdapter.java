package br.com.dgimenes.nasapic.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import br.com.dgimenes.nasapic.R;
import br.com.dgimenes.nasapic.fragment.BestPicturesTabFragment;
import br.com.dgimenes.nasapic.fragment.RecentTabFragment;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    public TabPagerAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new RecentTabFragment();
                break;
            case 1:
                fragment = new BestPicturesTabFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return " Recent";
            case 1:
                return " Best";
        }
        return null;
    }

    public Drawable getPageIcon(Context context, int position) {
        switch (position) {
            case 0:
                return context.getResources().getDrawable(R.drawable.ic_recent_tab);
            case 1:
                return context.getResources().getDrawable(R.drawable.ic_best_pics_tab);
        }
        return null;
    }
}
