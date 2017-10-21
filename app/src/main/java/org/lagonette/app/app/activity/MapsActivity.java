package org.lagonette.app.app.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;

import org.lagonette.app.R;
import org.lagonette.app.app.fragment.FiltersFragment;
import org.lagonette.app.app.fragment.MapsFragment;
import org.lagonette.app.app.fragment.PartnerDetailFragment;
import org.lagonette.app.app.viewmodel.SharedMapsActivityViewModel;
import org.lagonette.app.app.widget.coordinator.MainCoordinator;
import org.lagonette.app.app.widget.performer.BottomSheetPerformer;
import org.lagonette.app.app.widget.performer.FiltersButtonPerformer;
import org.lagonette.app.app.widget.performer.BottomSheetFragmentManager;

public class MapsActivity
        extends BaseActivity {

    private static final String TAG = "MapsActivity";

    private MapsFragment mMapsFragment;

    private Fragment mBottomSheetFragment;

    private MainCoordinator mCoordinator;

    private SharedMapsActivityViewModel mViewModel;

    private BottomSheetPerformer mBottomSheetPerformer;

    private FiltersButtonPerformer mFiltersButtonPerformer;

    private BottomSheetFragmentManager mBottomSheetFragmentManager;

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        mBottomSheetFragmentManager = new BottomSheetFragmentManager();
        mBottomSheetPerformer = new BottomSheetPerformer(R.id.bottom_sheet);
        mFiltersButtonPerformer = new FiltersButtonPerformer(R.id.filters_fab);

        mCoordinator = new MainCoordinator(mBottomSheetPerformer, mBottomSheetFragmentManager);
        mBottomSheetFragmentManager.setObserver(mCoordinator::notifyBottomSheetFragmentChanged);
        mBottomSheetPerformer.setStateObserver(mCoordinator::notifyBottomSheetStateChanged);
        mFiltersButtonPerformer.setObserver(mCoordinator::openFilters);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_maps);
    }

    @Override
    protected void onViewCreated(@NonNull View view) {
        //TODO Change activity lifecycle in BaseActivity
        mBottomSheetPerformer.inject(view);
        mFiltersButtonPerformer.inject(view);
    }

    @Override
    protected void onActivityCreated(Bundle savedInstanceState) {

        mBottomSheetFragmentManager.inject(MapsActivity.this);

        if (savedInstanceState == null) {
            mMapsFragment = MapsFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content, mMapsFragment, MapsFragment.TAG)
                    .commit();
        } else {
            mMapsFragment = (MapsFragment) getSupportFragmentManager()
                    .findFragmentByTag(MapsFragment.TAG);
        }

        mViewModel = ViewModelProviders
                .of(MapsActivity.this)
                .get(SharedMapsActivityViewModel.class);

//        mViewModel
//                .getWorkInProgress()
//                .observe(
//                        MapsActivity.this,
//                        workInProgress -> {
//                            if (workInProgress != null && workInProgress) {
//                                mCoordinator.showProgressBar();
//                            } else {
//                                mCoordinator.hideProgressBar();
//                            }
//                        }
//                );

//        mViewModel
//                .getMapIsReady()
//                .observe(
//                        MapsActivity.this,
//                        aVoid -> mCoordinator.onMapReady()
//                );

//        mViewModel
//                .getEnableMyPositionFAB()
//                .observe(
//                        MapsActivity.this,
//                        enable -> {
//                            if (enable != null && enable) {
//                                mCoordinator.showMyLocationButton();
//                            } else {
//                                mCoordinator.hideMyLocationButton();
//                            }
//                        }
//                );

        mViewModel
                .getShowLocationRequest()
                .observe(
                        MapsActivity.this,
                        request -> {
                            if (request != null) {
                                showLocation(
                                        request.locationId,
                                        request.zoom
                                );
                            } else {
                                showFullMap();
                            }
                        }
                );

        //TODO Maybe make interface to send data xor just observe data, like LiveEvent but for LiveData
        //TODO Maybe use LiveData to exchange info between Coordinator & Activity?
//        mCoordinator = new OldMainCoordinator(
//                MapsActivity.this,
//                search -> mViewModel.search(search),
//                mViewModel.getMapMovementSender(),
//                () -> SoftKeyboardUtil.hideSoftKeyboard(MapsActivity.this),
//                topPadding ->  mViewModel.getMapTopPadding().setValue(topPadding),
//                bottomPadding ->  mViewModel.getMapBottomPadding().setValue(bottomPadding),
//                MapsActivity.this
//        );
//        mCoordinator.inject(findViewById(android.R.id.content));
//        mCoordinator.start(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        mCoordinator.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
//        if (!mCoordinator.onBackPressed()) {
            super.onBackPressed();
//        }
    }

    public void showFullMap() {
//        mCoordinator.focusOnMap();
//        mCoordinator.closeBottomSheet();
    }

    public void showLocation(long locationId, boolean zoom) {
//        mCoordinator.focusOnMap();
//        mCoordinator.showLocation(locationId, zoom);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        mCoordinator.onSaveInstanceState(outState);
    }

}
