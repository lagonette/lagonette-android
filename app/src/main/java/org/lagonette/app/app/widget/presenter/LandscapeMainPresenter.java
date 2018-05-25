package org.lagonette.app.app.widget.presenter;

import android.support.annotation.NonNull;

import org.lagonette.app.app.activity.PresenterActivity;
import org.lagonette.app.app.widget.coordinator.state.action.ShowFullMapAction;
import org.lagonette.app.app.widget.performer.impl.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.impl.SharedPreferencesPerformer;
import org.lagonette.app.app.widget.performer.landscape.LandscapeBottomSheetPerformer;
import org.lagonette.app.app.widget.performer.landscape.LandscapeFabButtonsPerformer;
import org.lagonette.app.app.widget.performer.landscape.LandscapeMapFragmentPerformer;
import org.lagonette.app.app.widget.performer.landscape.LandscapeSearchBarPerformer;
import org.lagonette.app.app.widget.performer.landscape.LandscapeShowcasePerformer;

import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Action.TOGGLE_BOTTOM_SHEET;

public class LandscapeMainPresenter
		extends MainPresenter<
		LandscapeFabButtonsPerformer,
		LandscapeMapFragmentPerformer,
		LandscapeSearchBarPerformer,
		LandscapeShowcasePerformer> {

	public LandscapeMainPresenter(@NonNull SharedPreferencesPerformer preferencesPerformer) {
		super(preferencesPerformer);
	}

	@Override
	public void init(@NonNull PresenterActivity activity) {
		mFiltersFragmentPerformer.loadFragment();
		if (!mFiltersFragmentPerformer.isLoaded()) {
			mFiltersFragmentPerformer.loadFragment();
		}
		super.init(activity);
	}

	@Override
	public void onConstructed(@NonNull PresenterActivity activity) {
		super.onConstructed(activity);

		mEventBus.subscribe(
				TOGGLE_BOTTOM_SHEET,
				activity,
				aVoid -> mUiActionStore.start(new ShowFullMapAction())
		);

		// Performer > Performer
		mBottomSheetPerformer.onSlideChanged = mLocationDetailFragmentPerformer::updateTopPadding;
		mSearchBarPerformer.onBottomChanged = mFiltersFragmentPerformer::updateTopPadding;
	}

	@NonNull
	@Override
	protected LandscapeShowcasePerformer createShowcasePerformer(@NonNull PresenterActivity activity) {
		return new LandscapeShowcasePerformer(activity);
	}

	@NonNull
	@Override
	protected BottomSheetPerformer createBottomSheetPerformer(@NonNull PresenterActivity activity) {
		return new LandscapeBottomSheetPerformer(activity.getResources());
	}

	@NonNull
	@Override
	protected LandscapeSearchBarPerformer createSearchBarPerformer(@NonNull PresenterActivity activity) {
		return new LandscapeSearchBarPerformer();
	}

	@NonNull
	@Override
	protected LandscapeFabButtonsPerformer createFabButtonPerformer(@NonNull PresenterActivity activity) {
		return new LandscapeFabButtonsPerformer();
	}

	@NonNull
	@Override
	protected LandscapeMapFragmentPerformer createMapFragmentPerformer(@NonNull PresenterActivity activity) {
		return new LandscapeMapFragmentPerformer(activity);
	}
}
