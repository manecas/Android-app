package com.example.luis.testyourspeed.settings;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;

import com.example.luis.testyourspeed.R;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        if(actionBar != null)
            actionBar.hide();
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content,new SettingsFragment()).commit();
    }

    public static class SettingsFragment extends PreferenceFragment implements
            SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            initSummary();
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            updatePreference(findPreference(key), key);
        }

        private void initSummary(){
            for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); ++i) {
                Preference preference = getPreferenceScreen().getPreference(i);
                if (preference instanceof PreferenceGroup) {
                    PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
                    for (int j = 0; j < preferenceGroup.getPreferenceCount(); ++j) {
                        Preference singlePref = preferenceGroup.getPreference(j);
                        updatePreference(singlePref, singlePref.getKey());
                    }
                } else {
                updatePreference(preference, preference.getKey());
                }
            }
        }

        private void updatePreference(Preference preference, String key) {
            if (preference == null) return;
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                listPreference.setSummary(listPreference.getEntry());
                return;
            }
            SharedPreferences sharedPrefs = getPreferenceManager().getSharedPreferences();
            preference.setSummary(sharedPrefs.getString(key, "Default"));
        }
    }
}
