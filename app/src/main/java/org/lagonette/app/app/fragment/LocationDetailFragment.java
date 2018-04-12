package org.lagonette.app.app.fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
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
import org.lagonette.app.room.entity.statement.LocationDetail;
import org.lagonette.app.room.statement.Statement;

public class LocationDetailFragment
        extends BaseFragment {

    public static final String TAG = "LocationDetailFragment";

    private static final String ARG_LOCATION_ID = "arg:location_id";

    @NonNull
    public static LocationDetailFragment newInstance(long locationId) {
        Bundle args = new Bundle(1);
        args.putLong(ARG_LOCATION_ID, locationId);
        LocationDetailFragment locationDetailFragment = new LocationDetailFragment();
        locationDetailFragment.setArguments(args);
        return locationDetailFragment;
    }

    private MutableLiveData<Long> mLocationId;

    private LiveData<LocationDetail> mLocationDetail;

    private LocationDetailPerformer mLocationPerformer;

    private IntentPerformer mIntentPerformer;

    private SnackbarPerformer mSnackbarPerformer;

    @Override
    protected void construct() {
        LocationDetailViewModel viewModel = ViewModelProviders
                .of(LocationDetailFragment.this)
                .get(LocationDetailViewModel.class);

        mLocationId = viewModel.getLocationId();
        mLocationDetail = viewModel.getLocationDetail();

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
        //TODO Check
        long locationId = getArguments().getLong(ARG_LOCATION_ID, Statement.NO_ID);
        mLocationId.setValue(locationId);
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

        mLocationDetail.observe(
                LocationDetailFragment.this,
                mLocationPerformer::displayLocation
        );
    }

    public void updateTopPadding(int top) {
        mLocationPerformer.updateTopPadding(top);
    }

    public boolean isLoaded(long locationId) {
        Long loadedId = mLocationId.getValue();
        if (loadedId != null) {
            return locationId == loadedId;
        }
        else {
            return false;
        }
    }

    public long getLocationId() {
        Long id = mLocationId.getValue();
        if (id != null) {
            return id;
        }
        else {
            return Statement.NO_ID;
        }
    }
}
