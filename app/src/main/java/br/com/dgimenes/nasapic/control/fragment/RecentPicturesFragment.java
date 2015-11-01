package br.com.dgimenes.nasapic.control.fragment;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;

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

    private static final int LIST_PAGE_SIZE = 10;
    private static final String LOG_TAG = RecentPicturesFragment.class.getName();

    @Bind(R.id.recent_pics_recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.list_loading_indicator)
    ProgressBar listLoadingIndicator;

    private RecyclerView.Adapter recyclerViewAdapter;

    private RecyclerView.LayoutManager recyclerViewLayoutManager;

    private List<APOD> apods;
    private Date nextDateToLoad;
    private boolean loadingAPODs = false;

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

        nextDateToLoad = Calendar.getInstance().getTime();
        loadAPOD(LIST_PAGE_SIZE);
    }

    private void loadAPOD(final int daysToLoad) {
        if (!loadingAPODs) {
            loadingAPODs = true;
        }
        if (nextDateToLoad == null) {
            return;
        }
        if (daysToLoad == 0) {
            loadingAPODs = false;
            setupInfiniteScroll();
            return;
        }
        ApodInteractor apodInteractor = new ApodInteractor(getActivity());
        apodInteractor.getNasaApod(nextDateToLoad, new OnFinishListener<APOD>() {
            @Override
            public void onSuccess(APOD apod) {
                apods.add(apod);
                recyclerViewAdapter.notifyDataSetChanged();
                loadNextAPOD();
            }

            @Override
            public void onError(Throwable throwable) {
                error("Error loading APOD of day " +
                        new SimpleDateFormat("yyyy-MM-dd").format(nextDateToLoad));
                loadNextAPOD();
            }

            private void loadNextAPOD() {
                if (recyclerView.getVisibility() == View.GONE) {
                    recyclerView.setVisibility(View.VISIBLE);
                    listLoadingIndicator.setVisibility(View.GONE);

                }
                advanceNextDateToLoad();
                loadAPOD(daysToLoad - 1);
            }
        });
    }

    private void advanceNextDateToLoad() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(nextDateToLoad);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        nextDateToLoad = cal.getTime();
    }

    private void setupInfiniteScroll() {
        recyclerView.clearOnScrollListeners();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                boolean canScrollDown = recyclerView.canScrollVertically(1);
                synchronized (this) {
                    if (!canScrollDown && !loadingAPODs) {
                        String loadingMessage =
                                getResources().getString(R.string.loading_more_apods);
                        Snackbar.make(recyclerView, loadingMessage, Snackbar.LENGTH_SHORT).show();
                        loadAPOD(LIST_PAGE_SIZE);
                    }
                }
            }
        });
    }

    @Override
    public void error(String errorMessage) {
        // Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
        Log.d(LOG_TAG, errorMessage);
    }

    public int getDisplayWidth() {
        Display display = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }
}
