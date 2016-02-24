package br.com.dgimenes.nasapic.control.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.dgimenes.nasapic.service.EventsLogger;

public class TrackedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventsLogger.initialize(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventsLogger.logSessionStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventsLogger.logSessionEnd(this);
    }
}
