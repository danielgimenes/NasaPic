package br.com.dgimenes.nasapic.control.fragment;

import android.view.View;

import java.util.List;

import br.com.dgimenes.nasapic.model.SpacePic;
import br.com.dgimenes.nasapic.service.GlobalLogger;
import br.com.dgimenes.nasapic.service.interactor.OnFinishListener;
import br.com.dgimenes.nasapic.service.interactor.SpacePicInteractor;

public class BestPicturesFragment extends RecentPicturesFragment {

    @Override
    protected void loadFeed() {
        synchronized (loadingFeed) {
            if (!loadingFeed) {
                loadingFeed = true;
            }
        }
        SpacePicInteractor spacePicInteractor = new SpacePicInteractor(getActivity());
        spacePicInteractor.getBest(nextPageToLoad, new OnFinishListener<List<SpacePic>>() {
            @Override
            public void onSuccess(List<SpacePic> loadedPics) {
                spacePics.addAll(loadedPics);
                recyclerViewAdapter.notifyDataSetChanged();
                if (recyclerView.getVisibility() == View.GONE) {
                    recyclerView.setVisibility(View.VISIBLE);
                }
                listLoadingIndicator.setVisibility(View.GONE);
                nextPageToLoad++;
                GlobalLogger.logEvent("Best pictures loaded");
                releaseLoadingFeed();
            }

            @Override
            public void onError(Throwable throwable) {
                error("Error loading feed (page " + nextPageToLoad + ")");
                throwable.printStackTrace();
                GlobalLogger.logEvent("Error loading recent pictures");
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
}
