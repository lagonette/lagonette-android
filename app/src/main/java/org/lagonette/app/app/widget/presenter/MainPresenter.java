package org.lagonette.app.app.widget.presenter;

import android.arch.lifecycle.ViewModelProviders;
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
import org.lagonette.app.app.viewmodel.MapViewModel;
import org.lagonette.app.app.viewmodel.UiActionStore;
import org.lagonette.app.app.widget.coordinator.MainCoordinator;
import org.lagonette.app.app.widget.coordinator.state.UiState;
import org.lagonette.app.app.widget.coordinator.state.action.BackAction;
import org.lagonette.app.app.widget.coordinator.state.action.MoveToClusterAction;
import org.lagonette.app.app.widget.coordinator.state.action.MoveToFootprintAction;
import org.lagonette.app.app.widget.coordinator.state.action.MoveToMyLocationAction;
import org.lagonette.app.app.widget.coordinator.state.action.OpenLocationAction;
import org.lagonette.app.app.widget.coordinator.state.action.RestoreAction;
import org.lagonette.app.app.widget.coordinator.state.action.ShowFullMapAction;
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

	protected MapViewModel mMapViewModel;

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

		mMapViewModel = ViewModelProviders
				.of(activity)
				.get(MapViewModel.class);

		mCoordinator = createCoordinator();
		mMapFragmentPerformer = createMapFragmentPerformer(activity);
		mFabButtonsPerformer = createFabButtonPerformer(activity);
		mSearchBarPerformer = createSearchBarPerformer(activity);
		mBottomSheetPerformer = createBottomSheetPerformer(activity);
		mFiltersFragmentPerformer = new FiltersFragmentPerformer(activity);
		mLocationDetailFragmentPerformer = new LocationDetailFragmentPerformer(activity);
		mPermissionsPerformer = new PermissionsPerformer(activity);
		mSnackbarPerformer = new SnackbarPerformer(activity);

		// === Coordinator > Performer === //
		mCoordinator.openBottomSheet = mBottomSheetPerformer::openBottomSheet;
		mCoordinator.closeBottomSheet = mBottomSheetPerformer::closeBottomSheet;

		mCoordinator.selectLocation = mMapViewModel::selectLocation;

		mCoordinator.moveMapToCluster = mMapFragmentPerformer::moveToCluster;
		mCoordinator.moveMapToSelectedLocation = mMapFragmentPerformer::moveToSelectedLocation;
		mCoordinator.moveMapToMyLocation = mMapFragmentPerformer::moveToMyLocation;
		mCoordinator.stopMapMoving = mMapFragmentPerformer::stopMoving;
		mCoordinator.moveMapToFootprint = mMapFragmentPerformer::moveToFootprint;

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
		mMapFragmentPerformer.inject(view);
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
		mUiActionStore.startAction(new RestoreAction(mUiActionStore.getAction().getValue()));
	}

	@Override
	@CallSuper
	public void onConstructed(@NonNull PresenterActivity activity) {

		mPermissionsPerformer.requestPermissions = (permissions, requestCode) -> ActivityCompat.requestPermissions(activity, permissions, requestCode);
		mPermissionsPerformer.onFineLocationPermissionGranted = () -> {
			mLocationViewModel
					.getLocation()
					.observe(activity, mFabButtonsPerformer::updateLocation);
			mFabButtonsPerformer.notifyFineLocationGranted();
			mMapFragmentPerformer.updateLocationUI();
		};

		mCoordinator.getCurrentState = this::retrieveCurrentState;

		// === Performer > Store === //
		mEventBus.subscribe(
				OPEN_LOCATION_ITEM,
				activity,
				LongObserver.unbox(
						Statement.NO_ID,
						locationId -> mUiActionStore.startAction(new OpenLocationAction(locationId))
				)
		);
		mEventBus.subscribe(
				MOVE_TO_CLUSTER,
				activity,
				cluster -> mUiActionStore.startAction(new MoveToClusterAction(cluster))
		);
		mEventBus.subscribe(
				SHOW_FULL_MAP,
				activity,
				aVoid -> mUiActionStore.startAction(new ShowFullMapAction())
		);

		mFabButtonsPerformer.onPositionClick = location -> mUiActionStore.startAction(new MoveToMyLocationAction(location));
		mFabButtonsPerformer.onPositionLongClick = () -> mUiActionStore.startAction(new MoveToFootprintAction());
		mFabButtonsPerformer.askForFineLocationPermission = mPermissionsPerformer::askForFineLocation;

		mMapViewModel.getSelectedLocation().observe(
				activity,
				locationItem -> mCoordinator.process()
		);

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
		mSearchBarPerformer.onSearch = mStateViewModel.getSearch()::setValue;

		// ViewModels > View
		mStateViewModel.getWorkStatus().observe(activity, mSearchBarPerformer::setWorkStatus);
		mStateViewModel.getWorkError().observe(activity, mSnackbarPerformer::show);


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

		mUiActionStore.startAction(new BackAction());
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
				mLocationDetailFragmentPerformer.getLoadedId(),
				mMapViewModel.getSelectedLocationId()
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
