package com.ysmstudio.doittomorrow;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        DoItTomorrow doItTomorrow;

        SharedPreferences timePreference;

        Preference prefResetTime;
        Preference prefAbout;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            init();

            if(prefResetTime != null) {
                displayResetTime();
                prefResetTime.setOnPreferenceClickListener(onPreferenceClickListenerReset);
            }

            if(prefAbout != null) prefAbout.setOnPreferenceClickListener(onPreferenceClickListenerAbout);
        }

        private void init() {
            doItTomorrow = (DoItTomorrow) getActivity().getApplication();
            timePreference = getContext().getSharedPreferences("pref_time", MODE_PRIVATE);

            prefResetTime = findPreference("pref_reset_time");
            prefAbout = findPreference("pref_about");
        }

        /**
         * Reset time 설정을 클릭했을 때 수행
         */
        private Preference.OnPreferenceClickListener onPreferenceClickListenerReset =
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        View dialogView = View.inflate(getContext(), R.layout.dialog_settings_time_picker, null);
                        final TimePicker timePicker = dialogView.findViewById(R.id.time_picker);

                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            timePicker.setHour(timePreference.getInt("reset_hour", 6));
                            timePicker.setMinute(timePreference.getInt("reset_minute", 0));
                        } else {
                            timePicker.setCurrentHour(timePreference.getInt("reset_hour", 6));
                            timePicker.setCurrentMinute(timePreference.getInt("reset_minute", 0));
                        }

                        new MaterialAlertDialogBuilder(getContext())
                                .setView(dialogView)
                                .setPositiveButton(getString(R.string.str_dialog_button_ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int h, m;
                                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            h = timePicker.getHour();
                                            m = timePicker.getMinute();
                                        } else {
                                            h = timePicker.getCurrentHour();
                                            m = timePicker.getCurrentMinute();
                                        }

                                        setResetTime(h, m);
                                        displayResetTime();
                                        doItTomorrow.createAlarm();
                                    }
                                })
                                .setNegativeButton(getString(R.string.str_dialog_button_cancel), null)
                                .show();

                        return false;
                    }
                };

        private Preference.OnPreferenceClickListener onPreferenceClickListenerAbout = preference -> {
            startActivity(new Intent(getActivity(), AppInfoActivity.class));
            return false;
        };

        /**
         * Reset time을 저장한다.
         * @param h
         * @param m
         */
        private void setResetTime(int h, int m) {
            SharedPreferences.Editor editor = timePreference.edit();
            editor.putInt("reset_hour", h);
            editor.putInt("reset_minute", m);
            editor.apply();
        }

        /**
         * Reset time을 summary에 보여준다.
         */
        private void displayResetTime() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("a hh:mm");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, timePreference.getInt("reset_hour", 6));
            calendar.set(Calendar.MINUTE, timePreference.getInt("reset_minute", 0));

            Log.d("resethour", String.valueOf(timePreference.getInt("reset_hour", 6)));
            Log.d("resetminute", String.valueOf(timePreference.getInt("reset_minute", 0)));

            if(prefResetTime != null) prefResetTime.setSummary(
                    getString(R.string.str_settings_pref_reset_time_summary, simpleDateFormat.format(calendar.getTimeInMillis()))
            );
        }
    }


}