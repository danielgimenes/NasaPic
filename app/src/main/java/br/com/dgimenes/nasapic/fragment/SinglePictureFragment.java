package br.com.dgimenes.nasapic.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
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
import br.com.dgimenes.nasapic.view.LoadingDialog;

public class SinglePictureFragment extends Fragment {

    public static final String DATE_PARAM = "DATE_PARAM";
    private ImageView previewImageView;
    private TextView errorMessageTextView;
    private Picasso picasso;
    private String pictureUrl;
    private LoadingDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_apod_page, container, false);
        Date date = new Date(getArguments().getLong(DATE_PARAM));
        previewImageView = (ImageView) rootView.findViewById(R.id.apod_preview_image);
        errorMessageTextView = (TextView) rootView.findViewById(R.id.error_message);
        loadingDialog = new LoadingDialog(getActivity());
        setupPicasso();
        loadNasaAPOD(date);
        return rootView;
    }

    private void setupImageZooming() {
        previewImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Activity activity = SinglePictureFragment.this.getActivity();
                loadingDialog.show();
                downloadPictureAndDecodeInWallpaperSize(new OnFinishListener<Bitmap>() {
                    @Override
                    public void onSuccess(Bitmap bmp) {
                        try {
                            String path = ImageZoomActivity.saveImageOnDiskTemporarily(activity, bmp);
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
            picasso.load(pictureUrl).placeholder(R.drawable.loading)
                    .resize(previewImageView.getMeasuredWidth(), 0).into(previewImageView);
        } catch (RuntimeException e) {
            displayErrorMessage(R.string.error_loading_apod);
        }
    }

    private void setupPicasso() {
        if (picasso == null) {
            picasso = new Picasso.Builder(getActivity())
                    .downloader(new OkHttpDownloader(getActivity(), Integer.MAX_VALUE))
                    .listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                            displayErrorMessage(R.string.error_downloading_apod);
                        }
                    })
                    .build();
        }
    }

    public void getBitmap(final OnFinishListener<Bitmap> onFinishListener) {
        downloadPictureAndDecodeInWallpaperSize(onFinishListener);
    }

    private void downloadPictureAndDecodeInWallpaperSize(final OnFinishListener<Bitmap> onFinishListener) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int idealHeight = size.y;
        new AsyncTask<Integer, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Integer... params) {
                int ideal_height = params[0];
                try {
                    return picasso.load(pictureUrl).placeholder(R.drawable.loading).resize(0, ideal_height).get();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap == null) {
                    onFinishListener.onError(null);
                } else {
                    onFinishListener.onSuccess(bitmap);
                }
            }
        }.execute(idealHeight);

    }
}
