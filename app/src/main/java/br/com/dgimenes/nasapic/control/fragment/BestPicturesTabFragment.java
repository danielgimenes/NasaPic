package br.com.dgimenes.nasapic.control.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import br.com.dgimenes.nasapic.R;
import br.com.dgimenes.nasapic.control.adapter.BestPicturesAdapter;
import br.com.dgimenes.nasapic.service.interactor.ApodInteractor;
import br.com.dgimenes.nasapic.service.interactor.OnFinishListener;
import br.com.dgimenes.nasapic.view.LoadingDialog;
import butterknife.Bind;
import butterknife.ButterKnife;

public class BestPicturesTabFragment extends Fragment {

    @Bind(R.id.best_pics_set_wallpaper_button)
    Button setWallpaperButton;

    @Bind(R.id.best_pics_pager)
    ViewPager bestPicsPager;

    private BestPicturesAdapter bestPicsPagerAdapter;

    private LoadingDialog loadingDialog;

    private static final Date[] bestPicsDates = {
            date(2014, 5, 19),
            date(2012, 3, 25),
            date(2011, 5, 17),
            date(2015, 7, 24),
            date(2007, 10, 18),
    };

    private ApodInteractor apodInteractor;

    private static Date date(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day); // month is zero-based
        return cal.getTime();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_best_pics_tab, container, false);
        ButterKnife.bind(this, rootView);
        apodInteractor = new ApodInteractor(getActivity());
        setupUI();
        return rootView;
    }

    private void setupUI() {
        loadingDialog = new LoadingDialog(getActivity());
        bestPicsPagerAdapter = new BestPicturesAdapter(getFragmentManager(), Arrays.asList(bestPicsDates));
        bestPicsPager.setAdapter(bestPicsPagerAdapter);
        bestPicsPager.setClipToPadding(false);
        bestPicsPager.setPageMargin(-80);
        bestPicsPager.setPadding(20, 0, 20, 0);
        bestPicsPager.setOffscreenPageLimit(4);
        setWallpaperButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadingDialog.show();
                        String pictureUrl = bestPicsPagerAdapter
                                .getFragment(bestPicsPager.getCurrentItem()).getPictureUrl();
                        apodInteractor.setWallpaper(pictureUrl, new OnFinishListener<Void>() {
                            @Override
                            public void onSuccess(Void none) {
                                displayToastMessage(getString(R.string.wallpaper_set));
                                loadingDialog.dismiss();
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                loadingDialog.dismiss();
                                displayToastMessage(
                                        getString(R.string.error_setting_wallpaper));
                            }
                        });
                    }
                }
        );
    }

    private void displayToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

}
