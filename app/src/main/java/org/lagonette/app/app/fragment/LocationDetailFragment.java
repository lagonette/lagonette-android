package org.lagonette.app.app.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import org.lagonette.app.R;
import org.lagonette.app.app.viewmodel.LocationDetailViewModel;
import org.lagonette.app.app.widget.performer.impl.IntentPerformer;
import org.lagonette.app.app.widget.performer.impl.LocationDetailPerformer;
import org.lagonette.app.app.widget.performer.impl.SnackbarPerformer;
import org.lagonette.app.room.statement.Statement;

public class LocationDetailFragment
		extends BaseFragment {

	public static final String TAG = "LocationDetailFragment";

	private static final String ARG_LOCATION_ID = "arg:location_id";

	private LocationDetailPerformer mLocationPerformer;

	private IntentPerformer mIntentPerformer;

	private SnackbarPerformer mSnackbarPerformer;

	private LocationDetailViewModel mViewModel;

	@NonNull
	public static LocationDetailFragment newInstance(long locationId) {
		Bundle args = new Bundle(1);
		args.putLong(ARG_LOCATION_ID, locationId);
		LocationDetailFragment locationDetailFragment = new LocationDetailFragment();
		locationDetailFragment.setArguments(args);
		return locationDetailFragment;
	}

	@Override
	protected void construct() {
		mViewModel = ViewModelProviders
				.of(LocationDetailFragment.this)
				.get(LocationDetailViewModel.class);

		mIntentPerformer = new IntentPerformer(getContext());
		mLocationPerformer = new LocationDetailPerformer();
	}

	@Override
	protected int getContentView() {
		return R.layout.fragment_location_detail;
	}

	@Override
	protected void inject(@NonNull View view) {
		mLocationPerformer.inject(view);
	}

	@Override
	protected void construct(@NonNull FragmentActivity activity) {
		mSnackbarPerformer = new SnackbarPerformer(activity);
	}

	@Override
	protected void init() {
		Bundle arguments = getArguments();
		if (arguments != null) {
			long locationId = arguments.getLong(ARG_LOCATION_ID, Statement.NO_ID);
			mViewModel.setLocationId(locationId);
		}
	}

	@Override
	protected void restore(@NonNull Bundle savedInstanceState) {
		// Do nothing
	}

	@Override
	protected void onConstructed() {
		mIntentPerformer.onError = mSnackbarPerformer::show;

		mLocationPerformer.onAddressClick = mIntentPerformer::startDirection;
		mLocationPerformer.onEmailClick = mIntentPerformer::writeEmail;
		mLocationPerformer.onPhoneClick = mIntentPerformer::makeCall;
		mLocationPerformer.onWebsiteClick = mIntentPerformer::goToWebsite;

		// Maybe we could use LocationDetail LiveData to notify than partner has changed rather than reload another fragment
		mViewModel.getLocationDetail().observe(
				LocationDetailFragment.this,
				mLocationPerformer::displayLocation
		);
	}

	public void updateTopPadding(int top) {
		mLocationPerformer.updateTopPadding(top);
	}

	public boolean isLoaded(long locationId) {
		long loadedId = mViewModel.getLoadedLocationId();
		return loadedId > Statement.NO_ID && loadedId == locationId;
	}

	public long getLocationId() {
		return mViewModel.getLoadedLocationId();
	}
}
