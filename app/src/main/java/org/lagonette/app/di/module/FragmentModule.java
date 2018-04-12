package org.lagonette.app.di.module;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import org.lagonette.app.di.scope.ActivityScope;
import org.lagonette.app.di.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

@Module
public class FragmentModule {

	@NonNull
	private final Fragment mFragment;

	public FragmentModule(@NonNull Fragment fragment) {
		mFragment = fragment;
	}

	@Provides
	@FragmentScope
	public Fragment providesFragment() {
		return mFragment;
	}

}
