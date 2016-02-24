package br.com.dgimenes.nasapic.service;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.flurry.android.FlurryAgent;

import br.com.dgimenes.nasapic.control.ErrorMessage;
import io.fabric.sdk.android.Fabric;

public class EventsLogger {

    private static boolean initialized = false;

    public static void initialize(Context context) {
        if (!initialized) {
            initialized = true;

            // Fabric
            Fabric.with(context, new Crashlytics(), new Answers());

            // Flurry
            try {
                ApplicationInfo ai = context.getPackageManager()
                        .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                Bundle bundle = ai.metaData;
                String flurryApiKey = bundle.getString("flurry.ApiKey");
                FlurryAgent.setLogEnabled(false);
                FlurryAgent.setContinueSessionMillis(30000);
                FlurryAgent.init(context, flurryApiKey);
            } catch (Exception e) {
                Crashlytics.logException(e);
            }
        }
    }

    public static void logSessionStart(Context context) {
        // Flurry
        FlurryAgent.onStartSession(context);
    }

    public static void logSessionEnd(Context context) {
        // Flurry
        FlurryAgent.onEndSession(context);
    }

    public static void logEvent(String description) {
        // Fabric
        Answers.getInstance().logCustom(new CustomEvent(description));

        // Flurry
        FlurryAgent.logEvent(description);
    }

    public static void logError(ErrorMessage error, Throwable e) {
        // Fabric
        Answers.getInstance().logCustom(new CustomEvent("ERROR" + error.analyticsMessage));
        if (e != null) {
            Crashlytics.logException(e);
        }

        // Flurry
        if (e != null) {
            FlurryAgent.onError(error.id, error.analyticsMessage, e);
        } else {
            FlurryAgent.onError(error.id, error.analyticsMessage, "unknown");
        }
    }
}
