package br.com.dgimenes.nasapic.service.interactor;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.dgimenes.nasapic.R;
import br.com.dgimenes.nasapic.exception.APODIsNotAPictureException;
import br.com.dgimenes.nasapic.service.DefaultPicasso;
import br.com.dgimenes.nasapic.webservice.ApodDTO;
import br.com.dgimenes.nasapic.webservice.NasaWebservice;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ApodInteractor extends RetrofitWithCacheInteractor {
    private static final String NASA_API_KEY = "biwbr55t29bUSURh2hMbkccNkpvRoNyVi8XBHxm1";
    private static final String NASA_API_BASE_URL = "https://api.nasa.gov/planetary";
    private static final String NASA_API_DATE_FORMAT = "yyyy-MM-dd";

    private final NasaWebservice nasaWebservice;
    private Context context;

    public ApodInteractor(Context context) {
        super(context, NASA_API_BASE_URL);
        this.context = context;
        this.nasaWebservice = getRestAdapter().create(NasaWebservice.class);
    }

    public void getNasaApodPictureURI(Date date,
                                      final OnFinishListener<String> onFinishListener) {
        String formattedDate = new SimpleDateFormat(NASA_API_DATE_FORMAT).format(date);
        nasaWebservice.getAPOD(NASA_API_KEY, false, formattedDate, new Callback<ApodDTO>() {

            @Override
            public void success(ApodDTO apodDTO, Response response) {
                if (apodDTO.getMediaType() == null) {
                    String errorMessage = "Invalid response. Response: " +
                            response.getStatus() + " " + response.getReason();
                    Log.e(ApodInteractor.class.getSimpleName(), errorMessage);
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!apodDTO.getMediaType().equals("image")) {
                    onFinishListener.onError(new APODIsNotAPictureException());
                    return;
                }
                onFinishListener.onSuccess(apodDTO.getUrl());
            }

            @Override
            public void failure(RetrofitError error) {
                onFinishListener.onError(error.getCause());
            }
        });
    }

    public void downloadPictureAndDecodeInWallpaperSize(final String pictureUrl,
                                                        final OnFinishListener<Bitmap> onFinishListener) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int idealHeight = size.y;
        new AsyncTask<Integer, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Integer... params) {
                int ideal_height = params[0];
                try {
                    return DefaultPicasso.get(context, null).load(pictureUrl)
                            .placeholder(R.drawable.loading).resize(0, ideal_height).get();
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

    public void setTodaysApodAsWallpaper(final OnFinishListener<Void> onFinishListener) {
        Date today = Calendar.getInstance().getTime();
        getNasaApodPictureURI(today, new OnFinishListener<String>() {
            @Override
            public void onSuccess(String pictureUrl) {
                setWallpaper(pictureUrl, new OnFinishListener<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        onFinishListener.onSuccess(null);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        onFinishListener.onError(throwable);
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {
                onFinishListener.onError(throwable);
            }
        });
    }

    public void setWallpaper(String pictureUrl, final OnFinishListener<Void> onFinishListener) {
        downloadPictureAndDecodeInWallpaperSize(pictureUrl, new OnFinishListener<Bitmap>() {
            @Override
            public void onSuccess(Bitmap bmp) {
                new AsyncTask<Bitmap, Void, Boolean>() {

                    @Override
                    protected Boolean doInBackground(Bitmap... params) {
                        try {
                            WallpaperManager wallpaperMgr = WallpaperManager.getInstance(context);
                            wallpaperMgr.setWallpaperOffsetSteps(0.5f, 1.0f);
                            wallpaperMgr.setBitmap(params[0]);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return false;
                        }
                        return true;
                    }

                    @Override
                    protected void onPostExecute(Boolean success) {
                        if (success) {
                            onFinishListener.onSuccess(null);
                        } else {
                            onFinishListener.onError(null);
                        }
                    }

                }.execute(bmp);
            }

            @Override
            public void onError(Throwable throwable) {
                onFinishListener.onError(throwable);
            }
        });
    }
}