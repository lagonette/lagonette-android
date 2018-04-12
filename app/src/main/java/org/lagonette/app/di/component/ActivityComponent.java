package org.lagonette.app.di.component;

import android.support.annotation.NonNull;

import org.lagonette.app.di.module.ActivityModule;
import org.lagonette.app.di.module.FragmentModule;
import org.lagonette.app.di.module.ViewModelModule;
import org.lagonette.app.di.scope.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(modules = {ActivityModule.class, ViewModelModule.class, })
public interface ActivityComponent {

	@NonNull
	PortraitMainActivityComponent getPortraitActivityComponent();

	@NonNull
	LandscapeMainActivityComponent getLandscapeActivityComponent();

	@NonNull
	FragmentComponent getFragmentComponent(@NonNull FragmentModule module);
}
