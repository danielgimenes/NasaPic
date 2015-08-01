package br.com.dgimenes.nasapic.fragment;

import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import br.com.dgimenes.nasapic.R;
import br.com.dgimenes.nasapic.exception.APODIsNotAPictureException;
import br.com.dgimenes.nasapic.interactor.ApodInteractor;
import br.com.dgimenes.nasapic.interactor.OnFinishListener;

public class APODPageFragment extends Fragment {

    public static final String DATE_OFFSET_PARAM = "DATE_OFFSET";
    private ImageView previewImageView;
    private TextView errorMessageTextView;
    private Picasso picasso;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_apod_page, container, false);
        int dateOffset = getArguments().getInt(DATE_OFFSET_PARAM);
        previewImageView = (ImageView) rootView.findViewById(R.id.apod_preview_image);
        errorMessageTextView = (TextView) rootView.findViewById(R.id.error_message);
        setupPicasso();
        loadNasaAPOD(dateOffset);
        return rootView;
    }

    private void loadNasaAPOD(int dateOffset) {
        setLoadingImage();
        new ApodInteractor().getNasaApodPictureURI(dateOffset, new OnFinishListener<String>() {

            @Override
            public void onSuccess(String pictureUrl) {
                downloadAndSetPicture(pictureUrl);
            }

            @Override
            public void onError(Throwable throwable) {
                if (throwable instanceof APODIsNotAPictureException) {
                    displayErrorMessage(getString(R.string.apod_is_not_a_picture));
                    return;
                } else if (throwable != null) {
                    throwable.printStackTrace();
                }
                displayErrorMessage(getString(R.string.error_loading_apod));
            }
        });
    }

    private void setLoadingImage() {
        previewImageView.setImageDrawable(getResources().getDrawable(R.drawable.loading));
    }

    private void displayErrorMessage(String errorMessage) {
        previewImageView.setVisibility(View.GONE);
        errorMessageTextView.setText(errorMessage);
        errorMessageTextView.setVisibility(View.VISIBLE);
    }

    private void downloadAndSetPicture(String pictureUrl) {
        errorMessageTextView.setVisibility(View.GONE);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int ideal_height = size.y;
        try {
            picasso.load(pictureUrl).placeholder(R.drawable.loading).resize(0, ideal_height)
                    .into(previewImageView);
        } catch (RuntimeException e) {
            displayErrorMessage(getString(R.string.error_loading_apod));
        }
    }

    private void setupPicasso() {
        if (picasso == null) {
            picasso = new Picasso.Builder(getActivity())
                    .downloader(new OkHttpDownloader(getActivity(), Integer.MAX_VALUE))
                    .listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                            displayErrorMessage(getString(R.string.error_downloading_apod));
                        }
                    })
                    .build();
            picasso.setIndicatorsEnabled(true);
        }
    }
}
