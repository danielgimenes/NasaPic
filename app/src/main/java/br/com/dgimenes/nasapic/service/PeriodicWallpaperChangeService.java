package br.com.dgimenes.nasapic.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;

import br.com.dgimenes.nasapic.R;
import br.com.dgimenes.nasapic.service.interactor.ApodInteractor;
import br.com.dgimenes.nasapic.service.interactor.OnFinishListener;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class PeriodicWallpaperChangeService extends JobService {
    public static final int JOB_ID = 666;

    @Override
    public boolean onStartJob(JobParameters params) {
        ApodInteractor apodInteractor = new ApodInteractor(this);
        apodInteractor.setTodaysApodAsWallpaper(new OnFinishListener<Void>() {
            @Override
            public void onSuccess(Void result) {
                createNotification("Wallpaper changed");
            }

            @Override
            public void onError(Throwable throwable) {
                createNotification("Error downloading wallpaper");
            }
        });
        return false; // means everything is done
    }

    private void createNotification(CharSequence message) {
        Context context = PeriodicWallpaperChangeService.this;
        Bitmap largeNotificationBmp =
                ((BitmapDrawable) getResources().getDrawable(R.mipmap.logo, null)).getBitmap();
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_update_notification)
                .setLargeIcon(largeNotificationBmp)
                .setColor(ContextCompat.getColor(context, R.color.palette_primary))
                .setContentTitle("NasaPic")
                .setContentText(message).build();
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(55, notification);
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        createNotification("NasaPic PeriodicWallpaperChangeService stopping...");
        return false; // means everything is done
    }
}
