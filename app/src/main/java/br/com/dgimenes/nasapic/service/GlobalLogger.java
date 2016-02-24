package br.com.dgimenes.nasapic.service;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

public class GlobalLogger {
    public static void logEvent(String description) {
        Answers.getInstance().logCustom(new CustomEvent(description));
    }
}
