package br.com.dgimenes.nasapic.fragment;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import br.com.dgimenes.nasapic.R;
import br.com.dgimenes.nasapic.adapter.BestPicturesAdapter;

public class BestPicturesTabFragment extends Fragment {

    Button setWallpaperButton;

    ViewPager bestPicsPager;

    private BestPicturesAdapter bestPicsPagerAdapter;

    private ProgressDialog loadingDialog;

    private static final Date[] bestPicsDates = {
            date(2015, 1, 1),
            date(2015, 1, 2),
            date(2015, 1, 3),
            date(2015, 1, 4),
            date(2015, 1, 5),
    };

    private static Date date(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        return cal.getTime();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_best_pics_tab, container, false);
        setWallpaperButton = (Button) rootView.findViewById(R.id.best_pics_set_wallpaper_button);
        bestPicsPager = (ViewPager) rootView.findViewById(R.id.best_pics_pager);
        setupUI();
        return rootView;
    }

    private void setupUI() {
        bestPicsPagerAdapter = new BestPicturesAdapter(getFragmentManager(), Arrays.asList(bestPicsDates));
        bestPicsPager.setAdapter(bestPicsPagerAdapter);
        bestPicsPager.setClipToPadding(false);
        bestPicsPager.setPageMargin(-60);
        bestPicsPager.setPadding(50, 0, 50, 0);
        bestPicsPager.setOffscreenPageLimit(2);
        setWallpaperButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLoadingDialog();
                        try {
                            Bitmap bmp = bestPicsPagerAdapter.getFragment(bestPicsPager.getCurrentItem())
                                    .getBitmap();
                            new SetWallpaperAsyncTask().execute(bmp);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    private void hideLoadingDialog() {
        loadingDialog.dismiss();
    }

    private void showLoadingDialog() {
        loadingDialog = new ProgressDialog(getActivity());
        loadingDialog.setIndeterminate(false);
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingDialog.setMessage(getString(R.string.wait));
        loadingDialog.show();
    }

    class SetWallpaperAsyncTask extends AsyncTask<Bitmap, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Bitmap... params) {
            try {
                WallpaperManager wallpaperMgr = WallpaperManager.getInstance(BestPicturesTabFragment.this.getActivity());
                wallpaperMgr.setWallpaperOffsetSteps(0.5f, 1.0f);
                wallpaperMgr.setBitmap(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (!success) {
                displayToastMessage(getString(R.string.error_setting_wallpaper));
            } else {
                displayToastMessage(getString(R.string.wallpaper_set));
            }
            hideLoadingDialog();
        }
    }

    private void displayToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

}
