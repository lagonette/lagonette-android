package org.lagonette.app.di.component;

import android.support.annotation.NonNull;

import org.lagonette.app.app.activity.MainActivity;
import org.lagonette.app.di.module.ActivityModule;
import org.lagonette.app.di.module.ViewModelModule;
import org.lagonette.app.di.module.PortraitMainActivityModule;
import org.lagonette.app.di.scope.ActivityScope;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = {PortraitMainActivityModule.class})
public interface PortraitMainActivityComponent {

	void inject(@NonNull MainActivity activity);
}