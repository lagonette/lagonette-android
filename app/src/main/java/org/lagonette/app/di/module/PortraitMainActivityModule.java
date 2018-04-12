package org.lagonette.app.di.module;

import org.lagonette.app.app.widget.coordinator.MainCoordinator;
import org.lagonette.app.app.widget.coordinator.portrait.PortraitMainCoordinator;
import org.lagonette.app.app.widget.performer.impl.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.impl.FabButtonsPerformer;
import org.lagonette.app.app.widget.performer.impl.MapFragmentPerformer;
import org.lagonette.app.app.widget.performer.impl.SearchBarPerformer;
import org.lagonette.app.app.widget.performer.portrait.PortraitBottomSheetPerformer;
import org.lagonette.app.app.widget.performer.portrait.PortraitFabButtonsPerformer;
import org.lagonette.app.app.widget.performer.portrait.PortraitMapFragmentPerformer;
import org.lagonette.app.app.widget.performer.portrait.PortraitSearchBarPerformer;
import org.lagonette.app.app.widget.presenter.MainPresenter;
import org.lagonette.app.app.widget.presenter.PortraitMainPresenter;
import org.lagonette.app.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

@Module
public interface PortraitMainActivityModule {

	@Binds
	@ActivityScope
	MainPresenter providesMainPresenter(PortraitMainPresenter presenter);

	@Binds
	@ActivityScope
	MainCoordinator providesCoordinator(PortraitMainCoordinator coordinator);

	@Binds
	@ActivityScope
	BottomSheetPerformer providesBottomSheetPerformer(PortraitBottomSheetPerformer performer);

	@Binds
	@ActivityScope
	FabButtonsPerformer providesFabButtonsPerformer(PortraitFabButtonsPerformer performer);

	@Binds
	@ActivityScope
	MapFragmentPerformer providesMapFragmentPerformer(PortraitMapFragmentPerformer performer);

	@Binds
	@ActivityScope
	SearchBarPerformer providesSearchBarPerformer(PortraitSearchBarPerformer performer);
}
