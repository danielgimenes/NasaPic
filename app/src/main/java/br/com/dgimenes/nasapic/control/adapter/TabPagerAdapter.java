package br.com.dgimenes.nasapic.control.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import br.com.dgimenes.nasapic.R;
import br.com.dgimenes.nasapic.control.fragment.BestPicturesFragment;
import br.com.dgimenes.nasapic.control.fragment.RecentPicturesFragment;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    private final Context context;

    public TabPagerAdapter(FragmentManager supportFragmentManager, Context context) {
        super(supportFragmentManager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new RecentPicturesFragment();
                break;
            case 1:
                fragment = new BestPicturesFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return " " + context.getResources().getString(R.string.recent_tab_title);
            case 1:
                return " " + context.getResources().getString(R.string.best_pics_tab_title);
        }
        return null;
    }

    public Drawable getPageIcon(int position) {
        switch (position) {
            case 0:
                return context.getResources().getDrawable(R.drawable.ic_recent_tab);
            case 1:
                return context.getResources().getDrawable(R.drawable.ic_best_pics_tab);
        }
        return null;
    }
}
