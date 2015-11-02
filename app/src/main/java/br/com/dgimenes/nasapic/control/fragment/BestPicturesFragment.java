package br.com.dgimenes.nasapic.control.fragment;

import br.com.dgimenes.nasapic.exception.NoMoreAPODsToLoadException;
import br.com.dgimenes.nasapic.service.BestPicturesConfig;

public class BestPicturesFragment extends RecentPicturesFragment {

    private int dateIndexToLoad = 0;

    @Override
    protected void advanceNextDateToLoad() throws NoMoreAPODsToLoadException {
        if (dateIndexToLoad == BestPicturesConfig.bestPicsDates.length) {
            throw new NoMoreAPODsToLoadException();
        }
        nextDateToLoad = BestPicturesConfig.bestPicsDates[dateIndexToLoad++];
    }


}
