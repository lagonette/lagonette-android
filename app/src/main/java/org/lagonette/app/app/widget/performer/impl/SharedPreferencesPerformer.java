package org.lagonette.app.app.widget.performer.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import org.lagonette.app.util.SharedPreferencesUtils;

public class SharedPreferencesPerformer {

	private final SharedPreferences mSharedPrefs;

	public SharedPreferencesPerformer(@NonNull Context context) {
		mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public boolean isTutorialComplete() {
		return mSharedPrefs.getInt(
				SharedPreferencesUtils.PREFERENCE_TUTORIAL_COMPLETE_VERSION,
				SharedPreferencesUtils.DEFAULT_VALUE_TUTORIAL_COMPLETE_VERSION
		) >= SharedPreferencesUtils.CURRENT_VALUE_TUTORIAL_VERSION;
	}

	public void setTutorialAsComplete() {
		mSharedPrefs.edit()
				.putInt(
						SharedPreferencesUtils.PREFERENCE_TUTORIAL_COMPLETE_VERSION,
						SharedPreferencesUtils.CURRENT_VALUE_TUTORIAL_VERSION
				)
				.apply();
	}

}
