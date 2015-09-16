package br.com.dgimenes.nasapic.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;

import br.com.dgimenes.nasapic.R;
import br.com.dgimenes.nasapic.service.interactor.ApodInteractor;
import br.com.dgimenes.nasapic.service.interactor.OnFinishListener;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class PeriodicWallpaperChangeService extends JobService {
    public static final int JOB_ID = 666;
    private static final String LAST_APOD_CHECK_DAY = "LAST_APOD_CHECK_DAY";
    private static final String LOG_TAG = PeriodicWallpaperChangeService.class.getSimpleName();

    @Override
    public boolean onStartJob(JobParameters params) {
        if (haventTriedChangedToday()) {
            ApodInteractor apodInteractor = new ApodInteractor(this);
            apodInteractor.setTodaysApodAsWallpaper(new OnFinishListener<Void>() {
                @Override
                public void onSuccess(Void result) {
                    String successMessage = PeriodicWallpaperChangeService.this.getResources()
                            .getString(R.string.periodic_change_sucess);
                    createNotification(successMessage);
                    SharedPreferences.Editor editor = PreferenceManager
                            .getDefaultSharedPreferences(PeriodicWallpaperChangeService.this)
                            .edit();
                    editor.putInt(LAST_APOD_CHECK_DAY,
                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                    editor.apply();
                }

                @Override
                public void onError(Throwable throwable) {
                    String errorMessage = PeriodicWallpaperChangeService.this.getResources()
                            .getString(R.string.periodic_change_error);
                    createNotification(errorMessage);
                }
            });
        } else {
            Log.d(LOG_TAG, "Skipping periodic wallpaper change because already did it today.");
        }
        return false; // means everything is done
    }

    private boolean haventTriedChangedToday() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int dayOfMonth = preferences.getInt(LAST_APOD_CHECK_DAY, -1);
        int currentDayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        return dayOfMonth != currentDayOfMonth;
    }

    private void createNotification(CharSequence message) {
        Context context = PeriodicWallpaperChangeService.this;
        Bitmap largeNotificationBmp =
                ((BitmapDrawable) getResources().getDrawable(R.mipmap.logo, null)).getBitmap();
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_update_notification)
                .setLargeIcon(largeNotificationBmp)
                .setColor(ContextCompat.getColor(context, R.color.palette_primary))
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(message).build();
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(55, notification);
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        String stopMessage = getResources().getString(R.string.periodic_change_stopping_service);
        createNotification(stopMessage);
        return false; // means everything is done
    }
}
