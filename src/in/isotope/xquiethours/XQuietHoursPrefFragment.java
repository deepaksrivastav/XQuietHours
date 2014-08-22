package in.isotope.xquiethours;

/*
 * Copyright (C) 2014 Deepak Srivastav for XQuietHours Project 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;

public class XQuietHoursPrefFragment extends PreferenceFragment implements
		OnSharedPreferenceChangeListener {
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getPreferenceManager().setSharedPreferencesMode(
				Context.MODE_WORLD_READABLE);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.settings);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// if none of the days are selected
		// uncheck enable days of week control
		if (key.equals(QuietHoursHelper.KEY_DAYS_OF_WEEK)) {
			if (sharedPreferences.getString(QuietHoursHelper.KEY_DAYS_OF_WEEK,
					"").length() == 0) {
				CheckBoxPreference exercisesPref = (CheckBoxPreference) findPreference(QuietHoursHelper.KEY_ENABLE_DAYS_OF_WEEK);
				exercisesPref.setChecked(false);
			}
		}

		// if enable days of week is checked
		// and if none of the days of week are selected
		// select all by default
		if (key.equals(QuietHoursHelper.KEY_ENABLE_DAYS_OF_WEEK)) {
			if (sharedPreferences.getBoolean(
					QuietHoursHelper.KEY_ENABLE_DAYS_OF_WEEK, false)) {
				if (sharedPreferences.getString(
						QuietHoursHelper.KEY_DAYS_OF_WEEK, "").length() == 0) {
					DayOfWeekPreference exercisesPref = (DayOfWeekPreference) findPreference(QuietHoursHelper.KEY_DAYS_OF_WEEK);
					exercisesPref.enableAll();
				}
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		getPreferenceManager().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);

	}

	@Override
	public void onPause() {
		getPreferenceManager().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
		super.onPause();
	}
}
