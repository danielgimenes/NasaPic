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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.dgimenes.nasapic.R;
import br.com.dgimenes.nasapic.control.ErrorMessage;
import br.com.dgimenes.nasapic.control.adapter.SpacePicListAdapter;
import br.com.dgimenes.nasapic.model.SpacePic;
import br.com.dgimenes.nasapic.service.EventsLogger;
import br.com.dgimenes.nasapic.service.interactor.OnFinishListener;
import br.com.dgimenes.nasapic.service.interactor.SpacePicInteractor;
import butterknife.Bind;
import butterknife.ButterKnife;

public class RecentPicturesFragment extends Fragment implements SpacePicListAdapter.ErrorListener {

    private static final String LOG_TAG = RecentPicturesFragment.class.getName();

    @Bind(R.id.recent_pics_recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.list_loading_indicator)
    ProgressBar listLoadingIndicator;

    protected RecyclerView.Adapter recyclerViewAdapter;

    protected RecyclerView.LayoutManager recyclerViewLayoutManager;

    protected List<SpacePic> spacePics;
    protected int nextPageToLoad;
    protected Boolean loadingFeed = false;

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

    protected void setupUI() {
        recyclerView.setHasFixedSize(true);
        recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        spacePics = new ArrayList<>();
        recyclerViewAdapter = new SpacePicListAdapter(getActivity(),
                spacePics, this, getDisplayWidth());
        recyclerView.setAdapter(recyclerViewAdapter);

        nextPageToLoad = 1;
        loadFeed();
    }

    protected void loadFeed() {
        synchronized (loadingFeed) {
            if (!loadingFeed) {
                loadingFeed = true;
            }
        }
        SpacePicInteractor spacePicInteractor = new SpacePicInteractor(getActivity());
        spacePicInteractor.getFeed(nextPageToLoad, new OnFinishListener<List<SpacePic>>() {
            @Override
            public void onSuccess(List<SpacePic> loadedPics) {
                spacePics.addAll(loadedPics);
                recyclerViewAdapter.notifyDataSetChanged();
                if (recyclerView.getVisibility() == View.GONE) {
                    recyclerView.setVisibility(View.VISIBLE);
                }
                listLoadingIndicator.setVisibility(View.GONE);
                nextPageToLoad++;
                EventsLogger.logEvent("Recent pictures loaded");
                releaseLoadingFeed();
            }

            @Override
            public void onError(Throwable throwable) {
                ErrorMessage error = ErrorMessage.LOADING_RECENT_PICS;
                EventsLogger.logError(error, throwable);
                error(getResources().getString(error.userMessageRes));
                releaseLoadingFeed();
            }

            private void releaseLoadingFeed() {
                synchronized (loadingFeed) {
                    loadingFeed = false;
                    setupInfiniteScroll();
                }
            }
        });
    }

    protected void setupInfiniteScroll() {
        recyclerView.clearOnScrollListeners();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                boolean canScrollDown = recyclerView.canScrollVertically(1);
                synchronized (this) {
                    if (!canScrollDown && !loadingFeed) {
                        String loadingMessage =
                                getResources().getString(R.string.loading_more_apods);
                        Snackbar.make(recyclerView, loadingMessage, Snackbar.LENGTH_LONG).show();
                        listLoadingIndicator.setVisibility(View.VISIBLE);
                        listLoadingIndicator.bringToFront();
                        loadFeed();
                    }
                }
            }
        });
    }

    @Override
    public void error(String errorMessage) {
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        Log.d(LOG_TAG, errorMessage);
    }

    protected int getDisplayWidth() {
        Display display = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }
}
