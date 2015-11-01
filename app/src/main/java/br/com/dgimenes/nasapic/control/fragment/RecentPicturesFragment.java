package br.com.dgimenes.nasapic.control.fragment;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.dgimenes.nasapic.R;
import br.com.dgimenes.nasapic.control.adapter.APODListAdapter;
import br.com.dgimenes.nasapic.model.APOD;
import br.com.dgimenes.nasapic.service.interactor.ApodInteractor;
import br.com.dgimenes.nasapic.service.interactor.OnFinishListener;
import butterknife.Bind;
import butterknife.ButterKnife;

public class RecentPicturesFragment extends Fragment implements APODListAdapter.ErrorListener {

    @Bind(R.id.recent_pics_recycler_view)
    RecyclerView recyclerView;

    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;

    private List<APOD> apods;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_recent_tab, container, false);
        ButterKnife.bind(this, rootView);
        setupUI();
        return rootView;
    }

    private void setupUI() {
        recyclerView.setHasFixedSize(true);
        recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        apods = new ArrayList<>();
        recyclerViewAdapter = new APODListAdapter(getActivity(), apods, this, getDisplayWidth());
        recyclerView.setAdapter(recyclerViewAdapter);

        loadAPOD(Calendar.getInstance().getTime(), 10);

//        setWallpaperButton.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        loadingDialog.show();
//                        String pictureUrl = apodPagerAdapter.getFragment(apodPager.getCurrentItem())
//                                .getPictureUrl();
//                        apodInteractor.setWallpaper(pictureUrl, new OnFinishListener<Void>() {
//                            @Override
//                            public void onSuccess(Void none) {
//                                displayToastMessage(getString(R.string.wallpaper_set));
//                                loadingDialog.dismiss();
//                            }
//
//                            @Override
//                            public void onError(Throwable throwable) {
//                                loadingDialog.dismiss();
//                                displayToastMessage(
//                                        getString(R.string.error_setting_wallpaper));
//                            }
//                        });
//                    }
//                }
//        );
    }

    private void loadAPOD(final Date date, final int daysToLoad) {
        if (daysToLoad == 0) {
            return;
        }
        ApodInteractor apodInteractor = new ApodInteractor(getActivity());
        apodInteractor.getNasaApod(date, new OnFinishListener<APOD>() {
            @Override
            public void onSuccess(APOD apod) {
                apods.add(apod);
                recyclerViewAdapter.notifyDataSetChanged();
                loadNextAPOD();
            }

            @Override
            public void onError(Throwable throwable) {
                error("Error loading APOD of day " +
                        new SimpleDateFormat("yyyy-MM-dd").format(date));
                loadNextAPOD();
            }

            private void loadNextAPOD() {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.DAY_OF_MONTH, -1);
                loadAPOD(cal.getTime(), daysToLoad - 1);
            }
        });

    }

    @Override
    public void error(String errorMessage) {
        Snackbar.make(recyclerView, errorMessage, Snackbar.LENGTH_SHORT).show();
    }

    public int getDisplayWidth() {
        Display display = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }
}
