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

	public boolean isOnboardingComplete() {
		return mSharedPrefs.getInt(
				SharedPreferencesUtils.PREFERENCE_ONBOARDING_COMPLETE_VERSION,
				SharedPreferencesUtils.DEFAULT_VALUE_ONBOARDING_COMPLETE_VERSION
		) >= SharedPreferencesUtils.CURRENT_VALUE_ONBOARDING_VERSION;
	}

	public void setOnboardingAsComplete() {
		mSharedPrefs.edit()
				.putInt(
						SharedPreferencesUtils.PREFERENCE_ONBOARDING_COMPLETE_VERSION,
						SharedPreferencesUtils.CURRENT_VALUE_ONBOARDING_VERSION
				)
				.apply();
	}

}
