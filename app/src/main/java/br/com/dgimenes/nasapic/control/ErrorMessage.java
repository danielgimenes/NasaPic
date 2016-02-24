package br.com.dgimenes.nasapic.control;

import br.com.dgimenes.nasapic.R;

public enum ErrorMessage {
    DOWNLOADING_IMAGE("Error downloading image", R.string.error_downloading_apod), //
    LOADING_RECENT_PICS("Error loading recent pictures", R.string.error_loading_feed),
    LOADING_BEST_PICS("Error loading best pictures", R.string.error_loading_feed), //
    SETTING_WALLPAPER("Error setting wallpaper set manually", R.string.error_setting_wallpaper), //
    SETTING_WALLPAPER_AUTO("Error setting wallpaper set automatically", R.string.periodic_change_error), //
    UNDOING_WALLPAPER_CHANGE("Error undoing wallpaper change", R.string.undo_error_message), //
    ; //

    public final String id;
    public final String analyticsMessage;
    public final int userMessageRes;

    ErrorMessage(String analyticsMessage, int userMessageRes) {
        this.id = this.name();
        this.analyticsMessage = analyticsMessage;
        this.userMessageRes = userMessageRes;
    }
}
