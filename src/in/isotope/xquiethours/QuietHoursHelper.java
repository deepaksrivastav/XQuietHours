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

import android.text.format.Time;
import android.util.Log;
import de.robv.android.xposed.XSharedPreferences;

public final class QuietHoursHelper {

	private static final String PACKAGE_NAME = "in.isotope.xquiethours";
	private static final String KEY_QUIET_HOURS_ENABLED = "quiet_hours_enabled";
	private static final String KEY_QUIET_HOURS_TIME_RANGE = "quiet_hours_timerange";
	private static final String KEY_MUTE_SOUND = "mute_sound";
	private static final String KEY_NO_VIBE = "no_vibe";
	private static final String KEY_NO_LED = "no_led";

	private XSharedPreferences preferences = null;

	public QuietHoursHelper() {
		preferences = new XSharedPreferences(PACKAGE_NAME);
		preferences.makeWorldReadable();
		Log.i(PACKAGE_NAME, "Quiet Hours Helper initialized");
	}

	public boolean isInQuietHours() {
		preferences.reload();
		try {
			String[] quietTimeRange = preferences.getString(
					KEY_QUIET_HOURS_TIME_RANGE, "").split("\\|");

			int start = Integer.parseInt(quietTimeRange[0]);
			int end = Integer.parseInt(quietTimeRange[1]);

			Time t = new Time();
			t.setToNow();
			long now = ((t.hour * 60) + t.minute);

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

	public boolean isQuietHoursEnabled() {
		preferences.reload();
		return preferences.getBoolean(KEY_QUIET_HOURS_ENABLED, false);
	}

	public boolean isNoVibrateEnabled() {
		preferences.reload();
		return preferences.getBoolean(KEY_NO_VIBE, false);
	}

	public boolean isMuteSoundEnabled() {
		preferences.reload();
		return preferences.getBoolean(KEY_MUTE_SOUND, false);
	}

	public boolean isNoLedEnabled() {
		preferences.reload();
		return preferences.getBoolean(KEY_NO_LED, false);
	}
}
