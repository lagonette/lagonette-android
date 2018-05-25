package org.lagonette.app.app.widget.presenter;

import android.support.annotation.NonNull;

import org.lagonette.app.app.activity.PresenterActivity;
import org.lagonette.app.app.widget.coordinator.state.action.OpenFiltersAction;
import org.lagonette.app.app.widget.coordinator.state.action.ToggleBottomSheetAction;
import org.lagonette.app.app.widget.performer.impl.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.impl.SharedPreferencesPerformer;
import org.lagonette.app.app.widget.performer.portrait.PortraitBottomSheetPerformer;
import org.lagonette.app.app.widget.performer.portrait.PortraitFabButtonsPerformer;
import org.lagonette.app.app.widget.performer.portrait.PortraitMapFragmentPerformer;
import org.lagonette.app.app.widget.performer.portrait.PortraitSearchBarPerformer;
import org.lagonette.app.app.widget.performer.portrait.PortraitShowcasePerformer;

import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Action.TOGGLE_BOTTOM_SHEET;

public class PortraitMainPresenter
		extends MainPresenter<
		PortraitFabButtonsPerformer,
		PortraitMapFragmentPerformer,
		PortraitSearchBarPerformer,
		PortraitShowcasePerformer> {

	public PortraitMainPresenter(@NonNull SharedPreferencesPerformer preferencesPerformer) {
		super(preferencesPerformer);
	}

	@Override
	public void onConstructed(@NonNull PresenterActivity activity) {

		mEventBus.subscribe(
				TOGGLE_BOTTOM_SHEET,
				activity,
				aVoid -> mUiActionStore.start(new ToggleBottomSheetAction())
		);

		// Performer > LiveData
		mFabButtonsPerformer.onFiltersClick(() -> mUiActionStore.start(new OpenFiltersAction()));

		// LiveData > Performer
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

		// Showcase
		mShowcasePerformer.isFiltersLoaded = mFiltersFragmentPerformer::isLoaded;

		super.onConstructed(activity);
	}

	@NonNull
	@Override
	protected PortraitShowcasePerformer createShowcasePerformer(@NonNull PresenterActivity activity) {
		return new PortraitShowcasePerformer(activity);
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
