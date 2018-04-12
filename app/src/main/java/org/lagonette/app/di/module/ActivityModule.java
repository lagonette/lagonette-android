package org.lagonette.app.di.module;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import org.lagonette.app.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

	@NonNull
	private final FragmentActivity mActivity;

	public ActivityModule(@NonNull FragmentActivity activity) {
		mActivity = activity;
	}

	@Provides
	@ActivityScope
	public FragmentActivity providesActivity() {
		return mActivity;
	}

}
