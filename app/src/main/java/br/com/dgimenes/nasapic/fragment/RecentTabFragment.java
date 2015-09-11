package br.com.dgimenes.nasapic.fragment;

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

import br.com.dgimenes.nasapic.R;
import br.com.dgimenes.nasapic.adapter.APODPictureAdapter;
import br.com.dgimenes.nasapic.interactor.OnFinishListener;
import br.com.dgimenes.nasapic.view.LoadingDialog;

public class RecentTabFragment extends Fragment {

    public static final int DAYS_TO_DISPLAY = 3;

    Button setWallpaperButton;

    ViewPager apodPager;

    private APODPictureAdapter apodPagerAdapter;

    private LoadingDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_recent_tab, container, false);
        setWallpaperButton = (Button) rootView.findViewById(R.id.set_wallpaper_button);
        apodPager = (ViewPager) rootView.findViewById(R.id.apod_pager);
        setupUI();
        return rootView;
    }

    private void setupUI() {
        loadingDialog = new LoadingDialog(getActivity());
        apodPagerAdapter = new APODPictureAdapter(getFragmentManager(), DAYS_TO_DISPLAY);
        apodPager.setAdapter(apodPagerAdapter);
        apodPager.setClipToPadding(false);
        apodPager.setPageMargin(-80);
        apodPager.setPadding(20, 0, 20, 0);
        apodPager.setOffscreenPageLimit(4);
        setWallpaperButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadingDialog.show();
                        apodPagerAdapter.getFragment(apodPager.getCurrentItem())
                                .getBitmap(new OnFinishListener<Bitmap>() {
                                    @Override
                                    public void onSuccess(Bitmap bmp) {
                                        new SetWallpaperAsyncTask().execute(bmp);
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        displayToastMessage(
                                                getString(R.string.error_setting_wallpaper));
                                    }
                                });
                    }
                }
        );
    }

    // TODO refactor duplicated code
    class SetWallpaperAsyncTask extends AsyncTask<Bitmap, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Bitmap... params) {
            try {
                WallpaperManager wallpaperMgr = WallpaperManager.getInstance(RecentTabFragment.this.getActivity());
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
            loadingDialog.dismiss();
        }
    }

    private void displayToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

}
