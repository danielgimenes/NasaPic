package br.com.dgimenes.nasapic.service;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import br.com.dgimenes.nasapic.R;
import br.com.dgimenes.nasapic.control.ErrorMessage;
import br.com.dgimenes.nasapic.service.interactor.OnFinishListener;
import br.com.dgimenes.nasapic.service.interactor.SpacePicInteractor;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class PeriodicWallpaperChangeService extends JobService {
    public static final int JOB_ID = 666;
    private static final String LAST_APOD_CHECK_DAY = "LAST_APOD_CHECK_DAY";
    private static final String LOG_TAG = PeriodicWallpaperChangeService.class.getName();
    private static final int PERIOD_IN_HOURS = 6;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public static void updatePeriodicWallpaperChangeSetup(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Resources res = context.getResources();
            boolean periodicChangeActivated = PreferenceManager.getDefaultSharedPreferences(context)
                    .getBoolean(res.getString(R.string.periodic_change_preference),
                            res.getBoolean(R.bool.periodic_change_preference_default_value));
            if (periodicChangeActivated) {
                setupIfNeededPeriodicWallpaperChange(context);
            } else {
                unschedulePeriodicWallpaperChange(context);
            }
        }
    }

    public static void setupIfNeededPeriodicWallpaperChange(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Resources res = context.getResources();
            JobScheduler scheduler = (JobScheduler) context
                    .getSystemService(Context.JOB_SCHEDULER_SERVICE);

            if (scheduler.getAllPendingJobs().size() == 0) {
                ComponentName serviceEndpoint = new ComponentName(context,
                        PeriodicWallpaperChangeService.class);
                JobInfo wallpaperChangeJob = new JobInfo.Builder(
                        PeriodicWallpaperChangeService.JOB_ID, serviceEndpoint)
                        .setRequiresCharging(false)
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .setPersisted(true)
                        .setRequiresDeviceIdle(true)
                        .setPeriodic(PERIOD_IN_HOURS * 60 * 60 * 1000)
                        .build();

                scheduler.schedule(wallpaperChangeJob);
                String scheduledMessage = res.getString(R.string.periodic_change_scheduled);
                Toast.makeText(context, scheduledMessage, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void unschedulePeriodicWallpaperChange(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            JobScheduler scheduler = (JobScheduler) context
                    .getSystemService(Context.JOB_SCHEDULER_SERVICE);

            if (scheduler.getAllPendingJobs().size() > 0) {
                scheduler.cancelAll();
                String unscheduledMessage = context.getResources()
                        .getString(R.string.periodic_change_unscheduled);
                Toast.makeText(context, unscheduledMessage, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        EventsLogger.initialize(this);
        if (haventTriedChangedToday()) {
            new SpacePicInteractor(this).setTodaysApodAsWallpaper(new OnFinishListener<Void>() {
                @Override
                public void onSuccess(Void result) {
                    EventsLogger.logEvent("Wallpaper set automatically");
                    SharedPreferences.Editor editor = PreferenceManager
                            .getDefaultSharedPreferences(PeriodicWallpaperChangeService.this)
                            .edit();
                    editor.putInt(LAST_APOD_CHECK_DAY,
                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                    editor.apply();
                }

                @Override
                public void onError(Throwable throwable) {
                    ErrorMessage error = ErrorMessage.SETTING_WALLPAPER_AUTO;
                    EventsLogger.logError(error, throwable);
                    String errorMessage = PeriodicWallpaperChangeService.this.getResources()
                            .getString(error.userMessageRes);
                    WallpaperChangeNotification.createChangedNotification(
                            PeriodicWallpaperChangeService.this, errorMessage);
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

    @Override
    public boolean onStopJob(JobParameters params) {
        WallpaperChangeNotification.createChangedNotification(this,
                "NasaPic PeriodicWallpaperChangeService stopping...");
        return false; // means everything is done
    }
}
