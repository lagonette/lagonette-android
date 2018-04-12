package org.lagonette.app.app.widget.presenter;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import org.lagonette.app.R;
import org.lagonette.app.app.activity.PresenterActivity;
import org.lagonette.app.app.viewmodel.DataViewModel;
import org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel;
import org.lagonette.app.app.viewmodel.UiActionStore;
import org.lagonette.app.app.widget.coordinator.MainCoordinator;
import org.lagonette.app.app.widget.coordinator.state.UiAction;
import org.lagonette.app.app.widget.coordinator.state.UiState;
import org.lagonette.app.app.widget.error.Error;
import org.lagonette.app.app.widget.performer.impl.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.impl.FabButtonsPerformer;
import org.lagonette.app.app.widget.performer.impl.FiltersFragmentPerformer;
import org.lagonette.app.app.widget.performer.impl.LocationDetailFragmentPerformer;
import org.lagonette.app.app.widget.performer.impl.MapFragmentPerformer;
import org.lagonette.app.app.widget.performer.impl.PermissionsPerformer;
import org.lagonette.app.app.widget.performer.impl.SearchBarPerformer;
import org.lagonette.app.app.widget.performer.impl.SnackbarPerformer;
import org.lagonette.app.room.statement.Statement;
import org.lagonette.app.tools.arch.LocationViewModel;
import org.lagonette.app.tools.arch.LongObserver;

import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Action.MOVE_TO_CLUSTER;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Action.OPEN_LOCATION_ID;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Action.OPEN_LOCATION_ITEM;
import static org.lagonette.app.app.viewmodel.MainLiveEventBusViewModel.Action.SHOW_FULL_MAP;

public abstract class MainPresenter<
		FBP extends FabButtonsPerformer,
		MFP extends MapFragmentPerformer,
		SBP extends SearchBarPerformer>
		implements PresenterActivity.Lifecycle {

	private static final String TAG = "MainPresenter";

	// --- View Model --- //

	protected UiActionStore mUiActionStore;

	protected DataViewModel mStateViewModel;

	protected MainLiveEventBusViewModel mEventBus;

	protected LocationViewModel mLocationViewModel;

	// --- Live Data --- //

	protected MutableLiveData<String> mSearch;

	protected LiveData<Integer> mWorkStatus;

	protected LiveData<Error> mWorkError;

	// --- Performers --- //

	protected MainCoordinator mCoordinator;

	protected BottomSheetPerformer mBottomSheetPerformer;

	protected FBP mFabButtonsPerformer;

	protected MFP mMapFragmentPerformer;

	protected SBP mSearchBarPerformer;

	protected FiltersFragmentPerformer mFiltersFragmentPerformer;

	protected LocationDetailFragmentPerformer mLocationDetailFragmentPerformer;

	protected PermissionsPerformer mPermissionsPerformer;

	protected SnackbarPerformer mSnackbarPerformer;

	@Override
	@CallSuper
	public void construct(@NonNull PresenterActivity activity) {

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

		mSearch = mStateViewModel.getSearch();
		mWorkStatus = mStateViewModel.getWorkStatus();
		mWorkError = mStateViewModel.getWorkError();

		mCoordinator = createCoordinator();
		mMapFragmentPerformer = createMapFragmentPerformer(activity);
		mFabButtonsPerformer = createFabButtonPerformer(activity);
		mSearchBarPerformer = createSearchBarPerformer(activity);
		mBottomSheetPerformer = createBottomSheetPerformer(activity);
		mFiltersFragmentPerformer = new FiltersFragmentPerformer(activity, R.id.fragment_filters);
		mLocationDetailFragmentPerformer = new LocationDetailFragmentPerformer(activity, R.id.fragment_location_detail);
		mPermissionsPerformer = new PermissionsPerformer(activity);
		mSnackbarPerformer = new SnackbarPerformer(activity);

		// === Coordinator > Performer === //
		mCoordinator.openBottomSheet = mBottomSheetPerformer::openBottomSheet;
		mCoordinator.closeBottomSheet = mBottomSheetPerformer::closeBottomSheet;

		mCoordinator.moveMapToCluster = mMapFragmentPerformer::moveToCluster;
		mCoordinator.moveMapToLocation = mMapFragmentPerformer::moveToLocation;
		mCoordinator.moveMapToMyLocation = mMapFragmentPerformer::moveToMyLocation;
		mCoordinator.stopMapMoving = mMapFragmentPerformer::stopMoving;
		mCoordinator.moveMapToFootprint = mMapFragmentPerformer::moveToFootprint;
		mCoordinator.openLocation = mMapFragmentPerformer::openLocation;

		mCoordinator.loadMap = mMapFragmentPerformer::loadFragment;
		mCoordinator.restoreMap = mMapFragmentPerformer::restoreFragment;

		mCoordinator.loadFilters = mFiltersFragmentPerformer::loadFragment;
		mCoordinator.unloadFilters = mFiltersFragmentPerformer::unloadFragment;
		mCoordinator.restoreFilters = mFiltersFragmentPerformer::restoreFragment;

		mCoordinator.loadLocationDetail = mLocationDetailFragmentPerformer::loadFragment;
		mCoordinator.unloadLocationDetail = mLocationDetailFragmentPerformer::unloadFragment;
		mCoordinator.restoreLocationDetail = mLocationDetailFragmentPerformer::restoreFragment;

		// === Coordinator > Store === //
		mCoordinator.finishAction = mUiActionStore::finishAction;

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
	}

	@Override
	@CallSuper
	public void init(@NonNull PresenterActivity activity) {
		UiState currentState = retrieveCurrentState();
		mCoordinator.init(currentState);
	}

	@Override
	@CallSuper
	public void restore(@NonNull PresenterActivity activity, @NonNull Bundle savedInstanceState) {
		mCoordinator.restore();
		mUiActionStore.startAction(UiAction.restore(mUiActionStore.getAction().getValue()));
	}

	@Override
	@CallSuper
	public void onConstructed(@NonNull PresenterActivity activity) {

		mPermissionsPerformer.requestPermissions = (permissions, requestCode) -> ActivityCompat.requestPermissions(activity, permissions, requestCode);
		mPermissionsPerformer.onFineLocationPermissionGranted = () -> {
			mLocationViewModel
					.getLocation()
					.observe(activity, mFabButtonsPerformer::updateLocation);
			mFabButtonsPerformer.notifyFineLocationgranted();
			mMapFragmentPerformer.updateLocationUI();
		};

		mCoordinator.getCurrentState = this::retrieveCurrentState;

		// === Performer > Store === //
		mEventBus.subscribe(
				OPEN_LOCATION_ITEM,
				activity,
				locationItem -> mUiActionStore.startAction(UiAction.moveToAndOpenLocation(locationItem))
		);
		mEventBus.subscribe(
				MOVE_TO_CLUSTER,
				activity,
				cluster -> mUiActionStore.startAction(UiAction.moveToCluster(cluster))
		);
		mEventBus.subscribe(
				SHOW_FULL_MAP,
				activity,
				aVoid -> mUiActionStore.startAction(UiAction.showFullMap())
		);
		mEventBus.subscribe(
				OPEN_LOCATION_ID,
				activity,
				LongObserver.unbox(
						Statement.NO_ID,
						locationId -> mUiActionStore.startAction(UiAction.moveToAndOpenLocation(locationId))
				)
		);

		mFabButtonsPerformer.onPositionClick = location -> mUiActionStore.startAction(UiAction.moveToMyLocation(location));
		mFabButtonsPerformer.onPositionLongClick = () -> mUiActionStore.startAction(UiAction.moveToFootprint());
		mFabButtonsPerformer.askForFineLocationPermission = mPermissionsPerformer::askForFineLocation;

		mLocationDetailFragmentPerformer.onFragmentLoaded(locationId -> mCoordinator.process());
		mLocationDetailFragmentPerformer.onFragmentUnloaded(() -> mCoordinator.process());
		mFiltersFragmentPerformer.onFragmentLoaded(() -> mCoordinator.process());
		mFiltersFragmentPerformer.onFragmentUnloaded(() -> mCoordinator.process());
		mBottomSheetPerformer.onStateChanged = state -> mCoordinator.process();
		mMapFragmentPerformer.onMapMovementChanged = movement -> mCoordinator.process();


		// === Store > Coordinator === //
		mUiActionStore.getAction()
				.observe(
						activity,
						action -> mCoordinator.process()
				);

		// Setup loggers
		mCoordinator.setupLoggers();


		// ------------------------------- //
		//                                 //
		//    User > ViewModels > View     //
		//                                 //
		// ------------------------------- //

		// User > ViewModels
		mSearchBarPerformer.onSearch = mSearch::setValue;

		// ViewModels > View
		mWorkStatus.observe(activity, mSearchBarPerformer::setWorkStatus);
		mWorkError.observe(activity, mSnackbarPerformer::show);


		// --- Start --- //
		mPermissionsPerformer.askForFineLocation();

	}

	@Override
	public void onRequestPermissionsResult(
			int requestCode,
			@NonNull String permissions[],
			@NonNull int[] grantResults) {
		mPermissionsPerformer.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	public boolean onBackPressed(@NonNull PresenterActivity activity) {

		if (mBottomSheetPerformer.getState() == BottomSheetBehavior.STATE_HIDDEN) {
			return false;
		}

		mUiActionStore.startAction(UiAction.back());
		return true;
	}

	@NonNull
	private UiState retrieveCurrentState() {
		return new UiState(
				mUiActionStore.getAction().getValue(),
				mMapFragmentPerformer.getMapMovement(),
				mBottomSheetPerformer.getState(),
				mFiltersFragmentPerformer.isLoaded(),
				mLocationDetailFragmentPerformer.isLoaded(),
				mLocationDetailFragmentPerformer.getLoadedId()
		);
	}

	@NonNull
	protected abstract MainCoordinator createCoordinator();

	@NonNull
	protected abstract BottomSheetPerformer createBottomSheetPerformer(@NonNull PresenterActivity activity);

	@NonNull
	protected abstract SBP createSearchBarPerformer(@NonNull PresenterActivity activity);

	@NonNull
	protected abstract FBP createFabButtonPerformer(@NonNull PresenterActivity activity);

	@NonNull
	protected abstract MFP createMapFragmentPerformer(@NonNull PresenterActivity activity);

}
