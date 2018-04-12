package org.lagonette.app.di.component;


import android.support.annotation.NonNull;

import org.lagonette.app.app.fragment.MapsFragment;
import org.lagonette.app.di.module.FragmentModule;
import org.lagonette.app.di.module.ViewModelModule;
import org.lagonette.app.di.scope.FragmentScope;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = {FragmentModule.class, ViewModelModule.class})
public interface FragmentComponent {

	void inject(@NonNull MapsFragment fragment);

}
