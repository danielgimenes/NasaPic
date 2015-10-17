package br.com.dgimenes.nasapic.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;

import br.com.dgimenes.nasapic.R;
import br.com.dgimenes.nasapic.control.activity.MainActivity;
import br.com.dgimenes.nasapic.control.activity.SettingsActivity;

public class WallpaperChangeNotification {
    public static final int NOTIFICATION_ID = 123;
    private static final int UNDO_REQUEST_CODE = 458;

    public static void createNotification(Context context, CharSequence message) {
        Resources res = context.getResources();
        boolean notificationEnabled = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean(
                        res.getString(R.string.notification_preference),
                        res.getBoolean(R.bool
                                .notification_preference_default_value));
        if (notificationEnabled) {
            Bitmap largeNotificationBmp =
                    ((BitmapDrawable) res.getDrawable(R.mipmap.logo)).getBitmap();

            PendingIntent contentPendingIntent = PendingIntent.getActivities(context, 0,
                    new Intent[]{new Intent(context, MainActivity.class)},
                    PendingIntent.FLAG_UPDATE_CURRENT);

            Intent undoChangeIntent = new Intent(context, PeriodicWallpaperChangeService.class);
            undoChangeIntent.putExtra(PeriodicWallpaperChangeService.UNDO_OPERATION_EXTRA, true);
            PendingIntent undoChangePendingIntent = PendingIntent.getService(context, UNDO_REQUEST_CODE,
                    undoChangeIntent, PendingIntent.FLAG_ONE_SHOT);
            String undoChangeButtonTitle = context.getResources()
                    .getString(R.string.undo_change_button_title);

            PendingIntent settingsPendingIntent = PendingIntent.getActivities(context, 0,
                    new Intent[]{new Intent(context, SettingsActivity.class)},
                    PendingIntent.FLAG_UPDATE_CURRENT);
            String settingsButtonTitle = context.getResources()
                    .getString(R.string.action_settings);

            Notification notification = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_update_notification)
                    .setLargeIcon(largeNotificationBmp)
                    .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    .setContentTitle(res.getString(R.string.app_name))
                    .setContentText(message)
                    .setContentIntent(contentPendingIntent)
                    .addAction(R.drawable.ic_undo_change, undoChangeButtonTitle, undoChangePendingIntent)
                    .addAction(R.drawable.ic_settings, settingsButtonTitle, settingsPendingIntent)
                    .build();
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(NOTIFICATION_ID, notification);
        }
    }
}
