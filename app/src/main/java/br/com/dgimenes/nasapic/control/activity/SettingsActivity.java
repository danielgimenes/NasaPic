package br.com.dgimenes.nasapic.control.activity;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import br.com.dgimenes.nasapic.R;
import br.com.dgimenes.nasapic.service.PeriodicWallpaperChangeService;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment()).commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        LinearLayout root = (LinearLayout) findViewById(android.R.id.list)
                .getParent().getParent().getParent();
        Toolbar settingsToolbar = (Toolbar) LayoutInflater.from(this).inflate(
                R.layout.settings_toolbar, root, false);
        root.addView(settingsToolbar, 0); // insert at top
        settingsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);
            String preferenceKey = getActivity().getResources()
                    .getString(R.string.periodic_change_preference);
            findPreference(preferenceKey)
                    .setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                        @Override
                        public boolean onPreferenceChange(Preference preference, Object newValue) {
                            boolean periodicChange = (Boolean) newValue;
                            if (periodicChange) {
                                PeriodicWallpaperChangeService
                                        .setupIfNeededPeriodicWallpaperChange(getActivity());
                            } else {
                                PeriodicWallpaperChangeService
                                        .unschedulePeriodicWallpaperChange(getActivity());
                            }
                            return true;
                        }
                    });
        }
    }
}
