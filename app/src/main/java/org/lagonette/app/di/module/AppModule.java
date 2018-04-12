package org.lagonette.app.di.module;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import org.lagonette.app.app.LaGonetteApplication;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

	@NonNull
	private final LaGonetteApplication mApplication;

	public AppModule(@NonNull LaGonetteApplication application) {
		mApplication = application;
	}

	@Provides
	@Singleton
	public LaGonetteApplication providesApplication() {
		return mApplication;
	}

	@Provides
	@Singleton
	public Context providesContext() {
		return mApplication;
	}

	@Provides
	@Singleton
	public Resources providesResources() {
		return mApplication.getResources();
	}

	@Provides
	@Named("orientation")
	public int providesOrientation(Resources resources) {
		return resources.getConfiguration().orientation;
	}

}
