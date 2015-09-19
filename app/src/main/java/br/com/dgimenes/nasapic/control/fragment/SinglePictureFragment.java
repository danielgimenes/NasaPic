package br.com.dgimenes.nasapic.control.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Date;

import br.com.dgimenes.nasapic.R;
import br.com.dgimenes.nasapic.control.activity.ImageZoomActivity;
import br.com.dgimenes.nasapic.exception.APODIsNotAPictureException;
import br.com.dgimenes.nasapic.service.DefaultPicasso;
import br.com.dgimenes.nasapic.service.interactor.ApodInteractor;
import br.com.dgimenes.nasapic.service.interactor.OnFinishListener;
import br.com.dgimenes.nasapic.view.LoadingDialog;

public class SinglePictureFragment extends Fragment {

    public static final String DATE_PARAM = "DATE_PARAM";
    private ImageView previewImageView;
    private TextView errorMessageTextView;
    private String pictureUrl;
    private LoadingDialog loadingDialog;
    private ApodInteractor apodInteractor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_apod_page, container, false);
        Date date = new Date(getArguments().getLong(DATE_PARAM));
        previewImageView = (ImageView) rootView.findViewById(R.id.apod_preview_image);
        errorMessageTextView = (TextView) rootView.findViewById(R.id.error_message);
        loadingDialog = new LoadingDialog(getActivity());
        apodInteractor = new ApodInteractor(getActivity());
        loadNasaAPOD(date);
        return rootView;
    }

    private void setupImageZooming() {
        previewImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Activity activity = SinglePictureFragment.this.getActivity();
                loadingDialog.show();
                apodInteractor.downloadPictureAndDecodeInWallpaperSize(pictureUrl,
                        new OnFinishListener<Bitmap>() {
                            @Override
                            public void onSuccess(Bitmap bmp) {
                                try {
                                    String path = ImageZoomActivity
                                            .saveImageOnDiskTemporarily(activity, bmp);
                                    Intent intent = new Intent(activity, ImageZoomActivity.class);
                                    intent.putExtra(ImageZoomActivity.IMAGE_PATH_PARAM, path);
                                    activity.startActivity(intent);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                loadingDialog.dismiss();
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                displayErrorMessage(R.string.error_loading_apod);
                                loadingDialog.dismiss();
                            }
                        });
            }
        });
    }

    private void loadNasaAPOD(Date date) {
        setLoadingImage();
        new ApodInteractor(getActivity()).getNasaApodPictureURI(date, new OnFinishListener<String>() {

            @Override
            public void onSuccess(String pictureUrl) {
                SinglePictureFragment.this.pictureUrl = pictureUrl;
                downloadAndSetPicture(pictureUrl);
                setupImageZooming();
            }

            @Override
            public void onError(Throwable throwable) {
                if (throwable instanceof APODIsNotAPictureException) {
                    displayErrorMessage(R.string.apod_is_not_a_picture);
                    return;
                } else if (throwable != null) {
                    throwable.printStackTrace();
                }
                displayErrorMessage(R.string.error_loading_apod);
            }
        });
    }

    private void setLoadingImage() {
        previewImageView.setImageDrawable(getResources().getDrawable(R.drawable.loading));
    }

    private void displayErrorMessage(int errorMessageResource) {
        try {
            String errorMessage = getString(errorMessageResource);
            previewImageView.setVisibility(View.GONE);
            errorMessageTextView.setText(errorMessage);
            errorMessageTextView.setVisibility(View.VISIBLE);
        } catch (RuntimeException e) {
            // TODO refactor this...
        }
    }

    private void downloadAndSetPicture(String pictureUrl) {
        errorMessageTextView.setVisibility(View.GONE);
        try {
            DefaultPicasso.get(getActivity(), new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    displayErrorMessage(R.string.error_downloading_apod);
                }
            }).load(pictureUrl).placeholder(R.drawable.loading)
                    .resize(previewImageView.getMeasuredWidth(), 0).into(previewImageView);
        } catch (RuntimeException e) {
            displayErrorMessage(R.string.error_loading_apod);
        }
    }

    public String getPictureUrl() {
        return pictureUrl;
    }
}
