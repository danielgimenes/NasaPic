package br.com.dgimenes.nasapic.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import br.com.dgimenes.nasapic.control.ErrorMessage;
import br.com.dgimenes.nasapic.service.interactor.SpacePicInteractor;

public class UndoWallpaperChangeService extends IntentService {

    private static final String LOG_TAG = UndoWallpaperChangeService.class.getName();

    public static final String UNDO_OPERATION_EXTRA = "UNDO_OPERATION_EXTRA";

    public UndoWallpaperChangeService() {
        super(LOG_TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        EventsLogger.initialize(this);
        boolean undoOperation = intent.getExtras().getBoolean(UNDO_OPERATION_EXTRA, false);
        if (undoOperation) {
            undoLastWallpaperChange();
        }
    }

    private void undoLastWallpaperChange() {
        try {
            WallpaperChangeNotification.dismissChangedNotification(this);
            WallpaperChangeNotification.createChangingNotification(this);
            new SpacePicInteractor(this).undoLastWallpaperChangeSync();
            WallpaperChangeNotification.dismissChangingNotification(this);
            EventsLogger.logEvent("Undid wallpaper change");
        } catch (IOException e) {
            ErrorMessage error = ErrorMessage.UNDOING_WALLPAPER_CHANGE;
            EventsLogger.logError(error, e);
            String errorMessage = getResources().getString(error.userMessageRes);
            Log.e(LOG_TAG, errorMessage);
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }
}
