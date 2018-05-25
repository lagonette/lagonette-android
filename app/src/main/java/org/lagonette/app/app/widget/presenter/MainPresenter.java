package org.lagonette.app.app.widget.presenter;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;

import org.lagonette.app.R;
import org.lagonette.app.app.activity.OnboardingActivity;
import org.lagonette.app.app.activity.PresenterActivity;
import org.lagonette.app.app.viewmodel.DataViewModel;
import org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel;
import org.lagonette.app.app.viewmodel.MapViewModel;
import org.lagonette.app.app.viewmodel.UiActionStore;
import org.lagonette.app.app.widget.coordinator.state.UiState;
import org.lagonette.app.app.widget.coordinator.state.action.BackAction;
import org.lagonette.app.app.widget.coordinator.state.action.MoveToClusterAction;
import org.lagonette.app.app.widget.coordinator.state.action.MoveToFootprintAction;
import org.lagonette.app.app.widget.coordinator.state.action.MoveToMyLocationAction;
import org.lagonette.app.app.widget.coordinator.state.action.OpenLocationAction;
import org.lagonette.app.app.widget.coordinator.state.action.RestoreAction;
import org.lagonette.app.app.widget.coordinator.state.action.ShowFullMapAction;
import org.lagonette.app.app.widget.coordinator.state.action.UiAction;
import org.lagonette.app.app.widget.performer.impl.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.impl.FabButtonsPerformer;
import org.lagonette.app.app.widget.performer.impl.FiltersFragmentPerformer;
import org.lagonette.app.app.widget.performer.impl.LocationDetailFragmentPerformer;
import org.lagonette.app.app.widget.performer.impl.MapFragmentPerformer;
import org.lagonette.app.app.widget.performer.impl.PermissionsPerformer;
import org.lagonette.app.app.widget.performer.impl.SearchBarPerformer;
import org.lagonette.app.app.widget.performer.impl.SharedPreferencesPerformer;
import org.lagonette.app.app.widget.performer.impl.ShowcasePerformer;
import org.lagonette.app.app.widget.performer.impl.SnackbarPerformer;
import org.lagonette.app.room.statement.Statement;
import org.lagonette.app.tools.arch.LocationViewModel;
import org.lagonette.app.tools.arch.LongObserver;
import org.zxcv.functions.main.Runnable;

import static android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED;
import static android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Action.MOVE_TO_CLUSTER;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Action.OPEN_LOCATION_ITEM;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Action.SHOW_FULL_MAP;

public abstract class MainPresenter<
		FBP extends FabButtonsPerformer,
		MFP extends MapFragmentPerformer,
		SBP extends SearchBarPerformer,
		SP extends ShowcasePerformer>
		implements PresenterActivity.Lifecycle {

	public static final int REQUEST_CODE_ONBOARDING = 0;

	// Callbacks

	public Runnable enableCrashlyticsIfNeeded = Runnable::doNothing;

	// --- View Model --- //

	protected UiActionStore mUiActionStore;

	protected DataViewModel mStateViewModel;

	protected MainLiveEventBusViewModel mEventBus;

	protected LocationViewModel mLocationViewModel;

	protected MapViewModel mMapViewModel;

	// --- Performers --- //

	protected BottomSheetPerformer mBottomSheetPerformer;

	protected FBP mFabButtonsPerformer;

	protected MFP mMapFragmentPerformer;

	protected SBP mSearchBarPerformer;

	protected FiltersFragmentPerformer mFiltersFragmentPerformer;

	protected LocationDetailFragmentPerformer mLocationDetailFragmentPerformer;

	protected PermissionsPerformer mPermissionsPerformer;

	protected SnackbarPerformer mSnackbarPerformer;

	protected SharedPreferencesPerformer mPreferencesPerformer;

	protected SP mShowcasePerformer;

	// --- Factories --- //

	protected UiState.Factory mUiStateFactory;

	// --- Callbacks --- //

	protected UiAction.Callbacks mCallbacks;

	protected Runnable mFinishActivity;

	public MainPresenter(@NonNull SharedPreferencesPerformer preferencesPerformer) {
		mPreferencesPerformer = preferencesPerformer;
	}

	@Override
	@CallSuper
	public void construct(@NonNull PresenterActivity activity) {

		mCallbacks = new UiAction.Callbacks();

		mFinishActivity = activity::finish;

		mUiStateFactory = new UiState.Factory(activity.getResources().getConfiguration().orientation);

		mStateViewModel = ViewModelProviders
				.of(activity)
				.get(DataViewModel.class);

		mEventBus = ViewModelProviders
				.of(activity)
				.get(MainLiveEventBusViewModel.class);

		mUiActionStore = ViewModelProviders
				.of(activity)
				.get(UiActionStore.class);

		mLocationViewModel = ViewModelProviders
				.of(activity)
				.get(LocationViewModel.class);

		mMapViewModel = ViewModelProviders
				.of(activity)
				.get(MapViewModel.class);

		mMapFragmentPerformer = createMapFragmentPerformer(activity);
		mFabButtonsPerformer = createFabButtonPerformer(activity);
		mSearchBarPerformer = createSearchBarPerformer(activity);
		mBottomSheetPerformer = createBottomSheetPerformer(activity);
		mFiltersFragmentPerformer = new FiltersFragmentPerformer(activity);
		mLocationDetailFragmentPerformer = new LocationDetailFragmentPerformer(activity);
		mPermissionsPerformer = new PermissionsPerformer(activity);
		mSnackbarPerformer = new SnackbarPerformer(activity);
		mShowcasePerformer = createShowcasePerformer(activity);

		// === Coordinator > Performer === //
		mCallbacks.openBottomSheet = mBottomSheetPerformer::openBottomSheet;
		mCallbacks.closeBottomSheet = mBottomSheetPerformer::closeBottomSheet;
		mCallbacks.expandBottomSheet = () -> mBottomSheetPerformer.changeBottomSheetState(STATE_EXPANDED);
		mCallbacks.collapseBottomSheet = () -> mBottomSheetPerformer.changeBottomSheetState(STATE_COLLAPSED);

		mCallbacks.selectLocation = mMapViewModel::selectLocation;

		mCallbacks.moveMapToCluster = mMapFragmentPerformer::moveToCluster;
		mCallbacks.moveMapToSelectedLocation = mMapFragmentPerformer::moveToSelectedLocation;
		mCallbacks.moveMapToMyLocation = mMapFragmentPerformer::moveToMyLocation;
		mCallbacks.stopMapMoving = mMapFragmentPerformer::stopMoving;
		mCallbacks.moveMapToFootprint = mMapFragmentPerformer::moveToFootprint;

		mCallbacks.loadFilters = mFiltersFragmentPerformer::loadFragment;
		mCallbacks.unloadFilters = mFiltersFragmentPerformer::unloadFragment;

		mCallbacks.loadLocationDetail = mLocationDetailFragmentPerformer::loadFragment;
		mCallbacks.unloadLocationDetail = mLocationDetailFragmentPerformer::unloadFragment;

		// === Coordinator > Store === //
		mCallbacks.finishAction = mUiActionStore::finish;

	}

	@Override
	@LayoutRes
	public int getContentView() {
		return R.layout.activity_main;
	}

	@Override
	public void inject(@NonNull View view) {
		mBottomSheetPerformer.inject(view);
		mSearchBarPerformer.inject(view);
		mFabButtonsPerformer.inject(view);
		mMapFragmentPerformer.inject(view);
	}

	@Override
	@CallSuper
	public void init(@NonNull PresenterActivity activity) {
		mMapFragmentPerformer.loadFragment();

		if (mBottomSheetPerformer.getState() != BottomSheetBehavior.STATE_HIDDEN) {
			mBottomSheetPerformer.closeBottomSheet();
		}
	}

	@Override
	@CallSuper
	public void restore(@NonNull PresenterActivity activity, @NonNull Bundle savedInstanceState) {
		mMapFragmentPerformer.restoreFragment();
		mFiltersFragmentPerformer.restoreFragment();
		mLocationDetailFragmentPerformer.restoreFragment();
		mUiActionStore.start(new RestoreAction(mUiActionStore.getAction().getValue()));
	}

	@Override
	@CallSuper
	public void onConstructed(@NonNull PresenterActivity activity) {

		mPermissionsPerformer.onFineLocationPermissionResult = granted -> connectLocationProcessIfNeeded(activity);

		// === Performer > Store === //
		mEventBus.subscribe(
				OPEN_LOCATION_ITEM,
				activity,
				LongObserver.unbox(
						Statement.NO_ID,
						locationId -> mUiActionStore.start(new OpenLocationAction(locationId))
				)
		);
		mEventBus.subscribe(
				MOVE_TO_CLUSTER,
				activity,
				cluster -> mUiActionStore.start(new MoveToClusterAction(cluster))
		);
		mEventBus.subscribe(
				SHOW_FULL_MAP,
				activity,
				aVoid -> mUiActionStore.start(new ShowFullMapAction())
		);

		mFabButtonsPerformer.onPositionClick = location -> mUiActionStore.start(new MoveToMyLocationAction(location));
		mFabButtonsPerformer.onPositionLongClick = () -> mUiActionStore.start(new MoveToFootprintAction());
		mFabButtonsPerformer.askForFineLocationPermission = mPermissionsPerformer::askForFineLocation;
		mFabButtonsPerformer.checkForFineLocationPermission = mPermissionsPerformer::checkForFineLocation;

		mMapViewModel.getSelectedLocation().observe(
				activity,
				locationItem -> mUiActionStore.process(retrieveCurrentState(), mCallbacks)
		);

		mLocationDetailFragmentPerformer.onFragmentLoaded(locationId -> mUiActionStore.process(retrieveCurrentState(), mCallbacks));
		mLocationDetailFragmentPerformer.onFragmentUnloaded(() -> mUiActionStore.process(retrieveCurrentState(), mCallbacks));
		mFiltersFragmentPerformer.onFragmentLoaded(() -> mUiActionStore.process(retrieveCurrentState(), mCallbacks));
		mFiltersFragmentPerformer.onFragmentUnloaded(() -> mUiActionStore.process(retrieveCurrentState(), mCallbacks));
		mBottomSheetPerformer.onStateChanged = state -> mUiActionStore.process(retrieveCurrentState(), mCallbacks);
		mMapFragmentPerformer.onMapMovementChanged = movement -> mUiActionStore.process(retrieveCurrentState(), mCallbacks);


		// === Store > Coordinator === //
		mUiActionStore.getAction()
				.observe(
						activity,
						action -> mUiActionStore.process(retrieveCurrentState(), mCallbacks)
				);

		// Setup loggers
		mCallbacks.setupLoggers();


		// ------------------------------- //
		//                                 //
		//    User > ViewModels > View     //
		//                                 //
		// ------------------------------- //

		// User > ViewModels
		mSearchBarPerformer.onSearch = mStateViewModel.getSearch()::setValue;

		// ViewModels > View
		mStateViewModel.getWorkStatus().observe(activity, mSearchBarPerformer::setWorkStatus);
		mStateViewModel.getWorkError().observe(activity, mSnackbarPerformer::show);


		// Showcase
		mShowcasePerformer.checkForFineLocationPermission = mPermissionsPerformer::checkForFineLocation;
		mShowcasePerformer.isLocationDetailLoaded = mLocationDetailFragmentPerformer::isLoaded;

		mBottomSheetPerformer.onStateChanged = mShowcasePerformer.appendBottomSheetListener(activity, mBottomSheetPerformer.onStateChanged);

		// --- Start --- //
		connectLocationProcessIfNeeded(activity);
		startOnboardingOrShowcaseIfNeeded(activity);

	}

	@Override
	public void onRequestPermissionsResult(
			int requestCode,
			@NonNull String permissions[],
			@NonNull int[] grantResults) {
		mPermissionsPerformer.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	public void onActivityResult(
			@NonNull PresenterActivity activity,
			int requestCode,
			int resultCode,
			@NonNull Intent data) {
		if (requestCode == REQUEST_CODE_ONBOARDING) {
			if (resultCode == Activity.RESULT_OK) {
				mPreferencesPerformer.setOnboardingAsComplete();
				enableCrashlyticsIfNeeded.run();
				connectLocationProcessIfNeeded(activity);
				startOnboardingOrShowcaseIfNeeded(activity);
			}
			else {
				mFinishActivity.run();
			}
		}
	}

	@NonNull
	protected abstract SP createShowcasePerformer(@NonNull PresenterActivity activity);

	public boolean onBackPressed(@NonNull PresenterActivity activity) {

		if (mBottomSheetPerformer.getState() == BottomSheetBehavior.STATE_HIDDEN) {
			return false;
		}

		mUiActionStore.start(new BackAction());
		return true;
	}

	@NonNull
	private UiState retrieveCurrentState() {
		return mUiStateFactory.create(
				mMapFragmentPerformer.getMapMovement(),
				mBottomSheetPerformer.getState(),
				mFiltersFragmentPerformer.isLoaded(),
				mLocationDetailFragmentPerformer.isLoaded(),
				mLocationDetailFragmentPerformer.getLoadedId(),
				mMapViewModel.getSelectedLocationId()
		);
	}

	private void connectLocationProcessIfNeeded(@NonNull PresenterActivity activity) {
		if (mPermissionsPerformer.checkForFineLocation()) {
			mLocationViewModel
					.getLocation()
					.observe(
							activity,
							mFabButtonsPerformer::updateLocation
					);
			mFabButtonsPerformer.notifyFineLocationGranted();
			mMapFragmentPerformer.updateLocationUI();
		}
	}

	private void startOnboardingOrShowcaseIfNeeded(@NonNull PresenterActivity activity) {
		if (!mPreferencesPerformer.isOnboardingComplete()) {
			Intent intent = new Intent(activity, OnboardingActivity.class);
			activity.startActivityForResult(intent, REQUEST_CODE_ONBOARDING);
		}
		else {
			mShowcasePerformer.startShowcaseIfNeeded(activity);
		}
	}

	@NonNull
	protected abstract BottomSheetPerformer createBottomSheetPerformer(@NonNull PresenterActivity activity);

	@NonNull
	protected abstract SBP createSearchBarPerformer(@NonNull PresenterActivity activity);

	@NonNull
	protected abstract FBP createFabButtonPerformer(@NonNull PresenterActivity activity);

	@NonNull
	protected abstract MFP createMapFragmentPerformer(@NonNull PresenterActivity activity);

}
