package org.lagonette.app.app.widget.presenter;

import android.support.annotation.NonNull;

import org.lagonette.app.app.activity.PresenterActivity;
import org.lagonette.app.app.widget.coordinator.MainCoordinator;
import org.lagonette.app.app.widget.coordinator.portrait.PortraitMainCoordinator;
import org.lagonette.app.app.widget.coordinator.state.UiAction;
import org.lagonette.app.app.widget.performer.impl.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.portrait.PortraitBottomSheetPerformer;
import org.lagonette.app.app.widget.performer.portrait.PortraitFabButtonsPerformer;
import org.lagonette.app.app.widget.performer.portrait.PortraitMapFragmentPerformer;
import org.lagonette.app.app.widget.performer.portrait.PortraitSearchBarPerformer;

public class PortraitMainPresenter
		extends MainPresenter<
		PortraitFabButtonsPerformer,
		PortraitMapFragmentPerformer,
		PortraitSearchBarPerformer> {

	@Override
	public void onConstructed(@NonNull PresenterActivity activity) {
		super.onConstructed(activity);

		// Performer > LiveData
		mFabButtonsPerformer.onFiltersClick(() -> mUiActionStore.startAction(UiAction.openFilters()));

		// LiveData > Performer, Coordinator
		mLocationDetailFragmentPerformer.onFragmentLoaded(locationId -> mSearchBarPerformer.enableBehavior(true));
		mFiltersFragmentPerformer.onFragmentLoaded(() -> mSearchBarPerformer.enableBehavior(false));

		// Performer > Performer
		mBottomSheetPerformer.onSlideChanged = topPadding -> {
			mFiltersFragmentPerformer.updateTopPadding(topPadding);
			mLocationDetailFragmentPerformer.updateTopPadding(topPadding);
		};
		mSearchBarPerformer.onBottomChanged = offset -> {
			mMapFragmentPerformer.notifySearchBarBottomChanged(offset);
			mBottomSheetPerformer.notifySearchBarBottomChanged(offset);
		};
	}

	@NonNull
	@Override
	protected MainCoordinator createCoordinator() {
		return new PortraitMainCoordinator();
	}

	@NonNull
	@Override
	protected BottomSheetPerformer createBottomSheetPerformer(@NonNull PresenterActivity activity) {
		return new PortraitBottomSheetPerformer(activity.getResources());
	}

	@NonNull
	@Override
	protected PortraitSearchBarPerformer createSearchBarPerformer(@NonNull PresenterActivity activity) {
		return new PortraitSearchBarPerformer();
	}

	@NonNull
	@Override
	protected PortraitFabButtonsPerformer createFabButtonPerformer(@NonNull PresenterActivity activity) {
		return new PortraitFabButtonsPerformer();
	}

	@NonNull
	@Override
	protected PortraitMapFragmentPerformer createMapFragmentPerformer(@NonNull PresenterActivity activity) {
		return new PortraitMapFragmentPerformer(activity);
	}

}
