package br.com.dgimenes.nasapic.service.interactor;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import br.com.dgimenes.nasapic.R;
import br.com.dgimenes.nasapic.model.SpacePic;
import br.com.dgimenes.nasapic.model.api.FeedDTO;
import br.com.dgimenes.nasapic.model.api.SpacePicDTO;
import br.com.dgimenes.nasapic.service.DefaultPicasso;
import br.com.dgimenes.nasapic.service.WallpaperChangeNotification;
import br.com.dgimenes.nasapic.service.web.NasaPicServerWebservice;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SpacePicInteractor extends RetrofitWithCacheInteractor {
    private static String NASAPICSERVER_BASEURL = "http://nasapicserver.dgimenes.info:8080";
    private static final String LAST_WALLPAPER_FILE_NAME = "last_wallpaper";
    private final NasaPicServerWebservice webservice;

    private WeakReference<Context> contextWeakReference;

    public SpacePicInteractor(Context context) {
        super(context, NASAPICSERVER_BASEURL);
        contextWeakReference = new WeakReference<>(context);
        webservice = getRestAdapter().create(NasaPicServerWebservice.class);
    }

    public void getFeed(int page, final OnFinishListener<List<SpacePic>> onFinishListener) {
        webservice.getFeed(page, new Callback<FeedDTO>() {
            @Override
            public void success(FeedDTO feed, Response response) {
                List<SpacePic> spacePics = new ArrayList<>();
                for (SpacePicDTO spacePicDTO : feed.getSpacePics()) {
                    spacePics.add(new SpacePic(spacePicDTO));
                }
                onFinishListener.onSuccess(spacePics);
            }

            @Override
            public void failure(RetrofitError error) {
                onFinishListener.onError(error);
            }
        });
    }

    public void getBest(int page, final OnFinishListener<List<SpacePic>> onFinishListener) {
        webservice.getBest(page, new Callback<FeedDTO>() {
            @Override
            public void success(FeedDTO feed, Response response) {
                List<SpacePic> spacePics = new ArrayList<>();
                for (SpacePicDTO spacePicDTO : feed.getSpacePics()) {
                    spacePics.add(new SpacePic(spacePicDTO));
                }
                onFinishListener.onSuccess(spacePics);
            }

            @Override
            public void failure(RetrofitError error) {
                onFinishListener.onError(error);
            }
        });
    }

    public void setTodaysApodAsWallpaper(final OnFinishListener<Void> onFinishListener) {
        getFeed(1, new OnFinishListener<List<SpacePic>>() {
            @Override
            public void onSuccess(List<SpacePic> spacePics) {
                setWallpaper(spacePics.get(0).getHdImageUrl(), new OnFinishListener<Void>() {
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
                            WallpaperManager wallpaperMgr = WallpaperManager.getInstance(
                                    contextWeakReference.get());
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
                            Context context = SpacePicInteractor.this.contextWeakReference.get();
                            Resources res = context.getResources();
                            String successMessage = res.getString(R.string.periodic_change_sucess);
                            WallpaperChangeNotification.createChangedNotification(context,
                                    successMessage);
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

    public void downloadPictureAndDecodeInWallpaperSize(final String pictureUrl,
                                                        final OnFinishListener<Bitmap> onFinishListener) {
        Context context = contextWeakReference.get();
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
                    return DefaultPicasso.get(contextWeakReference.get(), null).load(pictureUrl)
                            .resize(0, ideal_height).get();
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

    private void storeLastWallpaper() throws IOException {
        Context context = contextWeakReference.get();
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

    public void undoLastWallpaperChangeSync() throws IOException {
        Bitmap bitmap = retrieveLastWallpaper();
        if (bitmap != null) {
            WallpaperManager wallpaperMgr = WallpaperManager.getInstance(
                    contextWeakReference.get());
            wallpaperMgr.setWallpaperOffsetSteps(0.5f, 1.0f);
            wallpaperMgr.setBitmap(bitmap);
        }
    }

    private Bitmap retrieveLastWallpaper() throws IOException {
        Context context = contextWeakReference.get();
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
}
