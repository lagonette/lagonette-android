package org.lagonette.app.di.module;

import org.lagonette.app.app.widget.coordinator.MainCoordinator;
import org.lagonette.app.app.widget.coordinator.landscape.LandscapeMainCoordinator;
import org.lagonette.app.app.widget.performer.impl.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.impl.FabButtonsPerformer;
import org.lagonette.app.app.widget.performer.impl.MapFragmentPerformer;
import org.lagonette.app.app.widget.performer.impl.SearchBarPerformer;
import org.lagonette.app.app.widget.performer.landscape.LandscapeBottomSheetPerformer;
import org.lagonette.app.app.widget.performer.landscape.LandscapeFabButtonsPerformer;
import org.lagonette.app.app.widget.performer.landscape.LandscapeMapFragmentPerformer;
import org.lagonette.app.app.widget.performer.landscape.LandscapeSearchBarPerformer;
import org.lagonette.app.app.widget.presenter.LandscapeMainPresenter;
import org.lagonette.app.app.widget.presenter.MainPresenter;
import org.lagonette.app.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

@Module
public interface LandscapeMainActivityModule {

	@Binds
	@ActivityScope
	MainPresenter providesMainPresenter(LandscapeMainPresenter presenter);

	@Binds
	@ActivityScope
	MainCoordinator providesCoordinator(LandscapeMainCoordinator coordinator);

	@Binds
	@ActivityScope
	BottomSheetPerformer providesBottomSheetPerformer(LandscapeBottomSheetPerformer performer);

	@Binds
	@ActivityScope
	FabButtonsPerformer providesFabButtonsPerformer(LandscapeFabButtonsPerformer performer);

	@Binds
	@ActivityScope
	MapFragmentPerformer providesMapFragmentPerformer(LandscapeMapFragmentPerformer performer);

	@Binds
	@ActivityScope
	SearchBarPerformer providesSearchBarPerformer(LandscapeSearchBarPerformer performer);

}
