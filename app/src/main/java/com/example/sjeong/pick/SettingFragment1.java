package com.example.sjeong.pick;

/**
 * Created by mijin on 2017-11-28.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;


public class SettingFragment1 extends PreferenceFragment {
    // TODO: Rename parameter arguments, choose names that match

    private SharedPreferences preferences;
    private SharedPreferences preferences1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);

        Preference app_info = (Preference) findPreference("app_info");
        Preference personal_setting = (Preference) findPreference("personal_setting");
        Preference search_setting = (Preference) findPreference("search_setting");

        app_info.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getActivity(), "NAME : PICK \n VERSION : Beta ", Toast.LENGTH_SHORT).show();
                return false;
            }
        });



        personal_setting.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getActivity(), PersonalSettingActivity.class);
                startActivity(intent);
                return false;
            }
        });

        search_setting.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getActivity(), SearchSettingActivity.class);
                startActivity(intent);
                return false;
            }
        });





    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
