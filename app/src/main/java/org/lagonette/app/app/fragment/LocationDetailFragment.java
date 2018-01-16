package org.lagonette.app.app.fragment;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.lagonette.app.R;
import org.lagonette.app.app.viewmodel.LocationDetailViewModel;
import org.lagonette.app.app.widget.performer.impl.LocationDetailPerformer;
import org.lagonette.app.repo.Resource;
import org.lagonette.app.room.entity.statement.LocationDetail;
import org.lagonette.app.room.statement.Statement;
import org.lagonette.app.util.IntentUtils;
import org.lagonette.app.util.SnackbarUtils;

public class LocationDetailFragment
        extends Fragment {

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

    private LocationDetailViewModel mViewModel;

    private MutableLiveData<Long> mLocationId;

    private LocationDetailPerformer mLocationDetailPerformer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders
                .of(LocationDetailFragment.this)
                .get(LocationDetailViewModel.class);

        mLocationId = mViewModel.getLocationId();

        mLocationDetailPerformer = new LocationDetailPerformer();
        mLocationDetailPerformer.onAddressClick = this::startDirection;
        mLocationDetailPerformer.onEmailClick = this::writeEmail;
        mLocationDetailPerformer.onPhoneClick = this::makeCall;
        mLocationDetailPerformer.onWebsiteClick = this::goToWebsite;

        //TODO Check
        if (savedInstanceState == null) {
            long locationId = getArguments().getLong(ARG_LOCATION_ID, Statement.NO_ID);
            mLocationId.setValue(locationId);
        }

        mViewModel.getLocationDetail().observe(
                LocationDetailFragment.this,
                this::dispatchLocationDetailResource
        );
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mLocationDetailPerformer.inject(view);
    }

    private void dispatchLocationDetailResource(@NonNull Resource<LocationDetail> resource) {
        mLocationDetailPerformer.displayLocation(getContext(), resource.data);

        switch (resource.status) {

            case Resource.LOADING:
                //TODO
                break;

            case Resource.SUCCESS:
                break;

            case Resource.ERROR:
                //TODO
                break;
        }
    }

    //TODO factorize with MapsFragment
    // Maybe it is to the activity to manage that
    public void errorNoDirectionAppFound() {
        Snackbar
                .make(
                        SnackbarUtils.getViewGroup(getActivity()).getChildAt(0),
                        R.string.error_no_direction_app_found,
                        Snackbar.LENGTH_LONG
                )
                .show();
    }

    public void errorNoCallAppFound() {
        Snackbar
                .make(
                        SnackbarUtils.getViewGroup(getActivity()).getChildAt(0),
                        R.string.error_no_call_app_found,
                        Snackbar.LENGTH_LONG
                )
                .show();
    }

    public void errorNoBrowserAppFound() {
        Snackbar
                .make(
                        SnackbarUtils.getViewGroup(getActivity()).getChildAt(0),
                        R.string.error_no_browser_app_found,
                        Snackbar.LENGTH_LONG
                )
                .show();
    }

    public void errorNoEmailAppFound() {
        Snackbar
                .make(
                        SnackbarUtils.getViewGroup(getActivity()).getChildAt(0),
                        R.string.error_no_email_app_found,
                        Snackbar.LENGTH_LONG
                )
                .show();
    }

    public void startDirection(double latitude, double longitude) {
        boolean success = IntentUtils.startDirection(
                getContext(),
                latitude,
                longitude
        );
        if (!success) {
            errorNoDirectionAppFound();
        }
    }

    public void makeCall(@NonNull String phoneNumber) {
        boolean success = IntentUtils.makeCall(
                getContext(),
                phoneNumber
        );
        if (!success) {
            errorNoCallAppFound();
        }
    }

    public void goToWebsite(@NonNull String url) {
        boolean success = IntentUtils.goToWebsite(
                getContext(),
                url
        );
        if (!success) {
            errorNoBrowserAppFound();
        }
    }

    public void writeEmail(@NonNull String email) {
        boolean success = IntentUtils.writeEmail(
                getContext(),
                email
        );
        if (!success) {
            errorNoEmailAppFound();
        }
    }

    public void updateTopPadding(int top) {
        mLocationDetailPerformer.updateTopPadding(top);
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

}
