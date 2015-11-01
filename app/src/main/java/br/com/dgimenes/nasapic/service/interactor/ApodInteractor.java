package br.com.dgimenes.nasapic.service.interactor;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.dgimenes.nasapic.R;
import br.com.dgimenes.nasapic.exception.APODIsNotAPictureException;
import br.com.dgimenes.nasapic.model.APOD;
import br.com.dgimenes.nasapic.model.api.ApodDTO;
import br.com.dgimenes.nasapic.service.DefaultPicasso;
import br.com.dgimenes.nasapic.service.WallpaperChangeNotification;
import br.com.dgimenes.nasapic.service.web.NasaWebservice;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ApodInteractor extends RetrofitWithCacheInteractor {
    private static final String NASA_API_KEY = "biwbr55t29bUSURh2hMbkccNkpvRoNyVi8XBHxm1";
    private static final String NASA_API_BASE_URL = "https://api.nasa.gov/planetary";
    private static final String NASA_API_DATE_FORMAT = "yyyy-MM-dd";
    private static final String LAST_WALLPAPER_FILE_NAME = "last_wallpaper";

    private final NasaWebservice nasaWebservice;
    private Context context;

    public ApodInteractor(Context context) {
        super(context, NASA_API_BASE_URL);
        this.context = context;
        this.nasaWebservice = getRestAdapter().create(NasaWebservice.class);
    }

    public void getNasaApod(final Date date,
                            final OnFinishListener<APOD> onFinishListener) {
        String formattedDate = new SimpleDateFormat(NASA_API_DATE_FORMAT).format(date);
        nasaWebservice.getAPOD(NASA_API_KEY, false, formattedDate, new Callback<ApodDTO>() {

            @Override
            public void success(ApodDTO apodDTO, Response response) {
                if (apodDTO.getMediaType() == null) {
                    String errorMessage = "Invalid response media type. Response: " +
                            response.getStatus() + " " + response.getReason();
                    Log.e(ApodInteractor.class.getSimpleName(), errorMessage);
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!apodDTO.getMediaType().equals("image")) {
                    onFinishListener.onError(new APODIsNotAPictureException());
                    return;
                }
                onFinishListener.onSuccess(new APOD(apodDTO, date));
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
        getNasaApod(today, new OnFinishListener<APOD>() {
            @Override
            public void onSuccess(APOD apod) {
                setWallpaper(apod.getUrl(), new OnFinishListener<Void>() {
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
                            storeLastWallpaper();
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
                            Resources res = ApodInteractor.this.context.getResources();
                            String successMessage = res.getString(R.string.periodic_change_sucess);
                            WallpaperChangeNotification.createNotification(
                                        ApodInteractor.this.context, successMessage);
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

    private void storeLastWallpaper() throws IOException {
        WallpaperManager wallpaperMgr = WallpaperManager.getInstance(context);
        File lastWallpaperFile = new File(context.getFilesDir(), LAST_WALLPAPER_FILE_NAME);
        if (!lastWallpaperFile.createNewFile()) {
            lastWallpaperFile.delete();
            lastWallpaperFile.createNewFile();
        }
        if (wallpaperMgr.getDrawable() instanceof BitmapDrawable) {
            FileOutputStream outputStream = context.openFileOutput(LAST_WALLPAPER_FILE_NAME,
                    Context.MODE_PRIVATE);
            ((BitmapDrawable)wallpaperMgr.getDrawable()).getBitmap().compress(
                    Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();
        }
    }

    private Bitmap retrieveLastWallpaper() throws IOException {
        File lastWallpaperFile = new File(context.getFilesDir(), LAST_WALLPAPER_FILE_NAME);
        if (lastWallpaperFile.exists() && lastWallpaperFile.length() > 0) {
            FileInputStream inputStream = null;
            try {
                inputStream = context.openFileInput(LAST_WALLPAPER_FILE_NAME);
                return BitmapFactory.decodeStream(inputStream);
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        } else {
            return null;
        }
    }

    public void undoLastWallpaperChangeSync() throws IOException {
        Bitmap bitmap = retrieveLastWallpaper();
        if (bitmap != null) {
            WallpaperManager wallpaperMgr = WallpaperManager.getInstance(context);
            wallpaperMgr.setWallpaperOffsetSteps(0.5f, 1.0f);
            wallpaperMgr.setBitmap(bitmap);
        }
    }
}