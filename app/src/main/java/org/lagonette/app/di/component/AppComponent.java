package org.lagonette.app.di.component;


import android.support.annotation.NonNull;

import org.lagonette.app.di.module.ActivityModule;
import org.lagonette.app.di.module.AppModule;
import org.lagonette.app.di.module.FragmentModule;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

	@Named("orientation")
	int getOrientation();

	@NonNull
	ActivityComponent getActivityComponent(@NonNull ActivityModule module);

}
