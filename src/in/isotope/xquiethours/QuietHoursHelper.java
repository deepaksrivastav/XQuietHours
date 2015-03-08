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

package in.isotope.xquiethours;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.Time;
import android.util.Log;
import de.robv.android.xposed.XSharedPreferences;

public final class QuietHoursHelper {

	private static final String PACKAGE_NAME = "in.isotope.xquiethours";
	private static final String KEY_QUIET_HOURS_ENABLED = "quiet_hours_enabled";
	private static final String ADDLN_KEY_QUIET_HOURS_ENABLED = "add_quiet_hours_enabled";
	private static final String QUIET_HOURS_1_KEY = "quiet_hours_1";
	private static final String QUIET_HOURS_2_KEY = "quiet_hours_2";

	public static final String KEY_QUIET_HOURS_TIME_RANGE = "time";
	public static final String KEY_MUTE_SOUND = "noSound";
	public static final String KEY_NO_VIBE = "noVibe";
	public static final String KEY_NO_LED = "noLed";
	public static final String KEY_DAYS_OF_WEEK = "dayOfWeek";

	private XSharedPreferences preferences = null;

	public QuietHoursHelper() {
		preferences = new XSharedPreferences(PACKAGE_NAME);
		preferences.makeWorldReadable();
		Log.i(PACKAGE_NAME, "Quiet Hours Helper initialized");
	}

	public String isInQuietHours() {

		if (isQuietHoursEnabled(KEY_QUIET_HOURS_ENABLED)) {
			Time t = new Time();
			t.setToNow();
			long now = ((t.hour * 60) + t.minute);
			int weekDay = t.weekDay;

			if (isInQuietHours(QUIET_HOURS_1_KEY, weekDay, now)) {
				return QUIET_HOURS_1_KEY;
			} else if (isQuietHoursEnabled(ADDLN_KEY_QUIET_HOURS_ENABLED)
					&& isInQuietHours(QUIET_HOURS_2_KEY, weekDay, now)) {
				return QUIET_HOURS_2_KEY;
			}
		}
		return null;
	}

	private boolean isInQuietHours(String preference, int weekDay, long now) {
		preferences.reload();
		try {

			JSONObject preferenceObj = new JSONObject(preferences.getString(
					preference, ""));
			// if days are enabled
			// return false if quiet hours is not applicable on current day
			if (!preferenceObj.getString(KEY_DAYS_OF_WEEK).contains(
					Integer.toString(weekDay))) {
				return false;
			}

			String[] quietTimeRange = preferenceObj.getString(
					KEY_QUIET_HOURS_TIME_RANGE).split("\\|");

			int start = Integer.parseInt(quietTimeRange[0]);
			int end = Integer.parseInt(quietTimeRange[1]);

			if (end <= start) {
				if (now >= start && now <= 1440)
					return true;
				if (now >= 0 && now <= end)
					return true;
			} else {
				if (now >= start && now <= end)
					return true;
			}
		} catch (Exception e) {
			Log.i(PACKAGE_NAME,
					"Exception while determining if the current time is inside quite time range",
					e);
		}
		return false;
	}

	public boolean isQuietHoursEnabled(String key) {
		preferences.reload();
		return preferences.getBoolean(key, false);
	}

	public boolean isNoVibrateEnabled(String key) {
		return getFlag(key, KEY_NO_VIBE);
	}

	public boolean isMuteSoundEnabled(String key) throws JSONException {
		return getFlag(key, KEY_MUTE_SOUND);
	}

	public boolean isNoLedEnabled(String key) {
		return getFlag(key, KEY_NO_LED);
	}

	public boolean getFlag(String mainKey, String optKey) {
		preferences.reload();
		try {
			JSONObject preferenceObj = new JSONObject(preferences.getString(
					mainKey, ""));
			return preferenceObj.getBoolean(optKey);
		} catch (JSONException e) {
			return false;
		}
	}
}
