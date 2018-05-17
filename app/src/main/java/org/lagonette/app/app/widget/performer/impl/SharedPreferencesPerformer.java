package org.lagonette.app.app.widget.performer.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import org.lagonette.app.util.SharedPreferencesUtils;
import org.zxcv.functions.main.Runnable;

public class SharedPreferencesPerformer {

	@NonNull
	private final SharedPreferences mSharedPrefs;

	@NonNull
	public Runnable enableCrashlitycs = Runnable::doNothing;

	public SharedPreferencesPerformer(@NonNull Context context) {
		mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

		mSharedPrefs.registerOnSharedPreferenceChangeListener(
				(sharedPreferences, pref) -> onPrefsChanged(pref)
		);
	}

	private void onPrefsChanged(String pref) {
		if (pref.equals(SharedPreferencesUtils.PREFERENCE_CRASHLITYCS_ENABLED)) {
			enableCrashlitycs.run();
		}
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

	public boolean isCrashlitycsEnabled() {
		return mSharedPrefs.getBoolean(
				SharedPreferencesUtils.PREFERENCE_CRASHLITYCS_ENABLED,
				SharedPreferencesUtils.DEFAULT_VALUE_CRASHLITYCS_ENABLED
		);
	}

	public void setCrashlitycsEnabled(boolean enabled) {
		mSharedPrefs.edit()
				.putBoolean(
						SharedPreferencesUtils.PREFERENCE_CRASHLITYCS_ENABLED,
						enabled
				)
				.apply();
	}
}
