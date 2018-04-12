package org.lagonette.app.di.component;

import android.support.annotation.NonNull;

import org.lagonette.app.app.activity.MainActivity;
import org.lagonette.app.di.module.ActivityModule;
import org.lagonette.app.di.module.LandscapeMainActivityModule;
import org.lagonette.app.di.module.ViewModelModule;
import org.lagonette.app.di.scope.ActivityScope;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = {LandscapeMainActivityModule.class})
public interface LandscapeMainActivityComponent {

	void inject(@NonNull MainActivity activity);
}