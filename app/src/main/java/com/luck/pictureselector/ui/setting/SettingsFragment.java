package com.luck.pictureselector.ui.setting;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.luck.pictureselector.R;

public  class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}