package br.com.dgimenes.nasapic.control.activity;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

import br.com.dgimenes.nasapic.R;
import br.com.dgimenes.nasapic.service.PeriodicWallpaperChangeService;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment()).commit();
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
