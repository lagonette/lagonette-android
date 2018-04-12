package org.lagonette.app.di.module;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import org.lagonette.app.app.viewmodel.DataViewModel;
import org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel;
import org.lagonette.app.app.viewmodel.MapDataViewModel;
import org.lagonette.app.app.viewmodel.MapLocationViewModel;
import org.lagonette.app.app.viewmodel.UiActionStore;
import org.lagonette.app.di.scope.ActivityScope;
import org.lagonette.app.di.scope.FragmentScope;
import org.lagonette.app.tools.arch.LocationViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class ViewModelModule {

	@Provides
	@ActivityScope
	public LocationViewModel providesActivity(FragmentActivity activity) {
		return ViewModelProviders
				.of(activity)
				.get(LocationViewModel.class);
	}

	@Provides
	@ActivityScope
	public DataViewModel providesDataViewModel(FragmentActivity activity) {
		return ViewModelProviders
				.of(activity)
				.get(DataViewModel.class);
	}

	@Provides
	@ActivityScope
	public MainLiveEventBusViewModel providesMainLiveEventBusViewModel(FragmentActivity activity) {
		return ViewModelProviders
				.of(activity)
				.get(MainLiveEventBusViewModel.class);
	}

	@Provides
	@ActivityScope
	public UiActionStore providesUiActionStore(FragmentActivity activity) {
		return ViewModelProviders
				.of(activity)
				.get(UiActionStore.class);
	}

	@Provides
	@FragmentScope
	public MapLocationViewModel providesMapLocationViewModel(Fragment fragment) {
		return ViewModelProviders
				.of(fragment)
				.get(MapLocationViewModel.class);
	}

	@Provides
	@FragmentScope
	public MapDataViewModel providesMapViewModel(Fragment fragment) {
		return ViewModelProviders
				.of(fragment)
				.get(MapDataViewModel.class);
	}

}
