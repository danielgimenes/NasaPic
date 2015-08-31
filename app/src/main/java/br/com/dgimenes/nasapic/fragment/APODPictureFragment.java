package br.com.dgimenes.nasapic.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
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

import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Date;

import br.com.dgimenes.nasapic.R;
import br.com.dgimenes.nasapic.activity.ImageZoomActivity;
import br.com.dgimenes.nasapic.exception.APODIsNotAPictureException;
import br.com.dgimenes.nasapic.interactor.ApodInteractor;
import br.com.dgimenes.nasapic.interactor.OnFinishListener;

public class APODPictureFragment extends Fragment {

    public static final String DATE_PARAM = "DATE_PARAM";
    private ImageView previewImageView;
    private TextView errorMessageTextView;
    private Picasso picasso;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_apod_page, container, false);
        Date date = new Date(getArguments().getLong(DATE_PARAM));
        previewImageView = (ImageView) rootView.findViewById(R.id.apod_preview_image);
        errorMessageTextView = (TextView) rootView.findViewById(R.id.error_message);
        setupPicasso();
        loadNasaAPOD(date);
        return rootView;
    }

    private void setupImageZooming() {
        previewImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = APODPictureFragment.this.getActivity();
                Bitmap bmp = ((BitmapDrawable) previewImageView.getDrawable()).getBitmap();
                try {
                    String path = ImageZoomActivity.saveImageOnDiskTemporarily(activity, bmp);
                    Intent intent = new Intent(activity, ImageZoomActivity.class);
                    intent.putExtra(ImageZoomActivity.IMAGE_PATH_PARAM, path);
                    activity.startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadNasaAPOD(Date date) {
        setLoadingImage();
        new ApodInteractor(getActivity()).getNasaApodPictureURI(date, new OnFinishListener<String>() {

            @Override
            public void onSuccess(String pictureUrl) {
                downloadAndSetPicture(pictureUrl);
                setupImageZooming();
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

    public Bitmap getBitmap() throws IOException {
        if (previewImageView == null || previewImageView.getVisibility() != View.VISIBLE) {
            throw new IOException();
        }
        return ((BitmapDrawable) previewImageView.getDrawable()).getBitmap();
    }
}
