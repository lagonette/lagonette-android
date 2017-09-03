package org.lagonette.android.app.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.google.android.gms.maps.GoogleMap;

import org.lagonette.android.R;
import org.lagonette.android.app.fragment.FiltersFragment;
import org.lagonette.android.app.fragment.MapsFragment;
import org.lagonette.android.app.fragment.PartnerDetailFragment;
import org.lagonette.android.app.viewmodel.SharedMapsActivityViewModel;
import org.lagonette.android.app.widget.coordinator.MainCoordinator;
import org.lagonette.android.util.SoftKeyboardUtil;

public class MapsActivity
        extends BaseActivity
        implements MainCoordinator.Callbacks {

    private static final String TAG = "MapsActivity";

    private MapsFragment mMapsFragment;

    private Fragment mBottomSheetFragment;

    private MainCoordinator mCoordinator;

    private SharedMapsActivityViewModel mViewModel;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_maps);
    }

    @Override
    protected void onViewCreated() {
        // TODO Change activity lifecycle dans BaseActivity
    }

    @Override
    protected void onActivityCreated(Bundle savedInstanceState) {

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

        mViewModel
                .getWorkInProgress()
                .observe(
                        MapsActivity.this,
                        workInProgress -> {
                            if (workInProgress != null && workInProgress) {
                                mCoordinator.showProgressBar();
                            } else {
                                mCoordinator.hideProgressBar();
                            }
                        }
                );

        mViewModel
                .getMapIsReady()
                .observe(
                        MapsActivity.this,
                        aVoid -> mCoordinator.onMapReady()
                );

        mViewModel
                .getEnableMyPositionFAB()
                .observe(
                        MapsActivity.this,
                        enable -> {
                            if (enable != null && enable) {
                                mCoordinator.showMyLocationButton();
                            } else {
                                mCoordinator.hideMyLocationButton();
                            }
                        }
                );

        mViewModel
                .getShowPartnerRequest()
                .observe(
                        MapsActivity.this,
                        request -> {
                            if (request != null) {
                                showPartner(
                                        request.partnerId,
                                        request.zoom
                                );
                            } else {
                                showFullMap();
                            }
                        }
                );

        // TODO Maybe use LiveData to exchange info between Coordinator & Activity?
        mCoordinator = new MainCoordinator(
                MapsActivity.this,
                search -> mViewModel.search(search),
                MapsActivity.this
        )
                .injectView(findViewById(android.R.id.content))
                .start(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCoordinator.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (!mCoordinator.onBackPressed()) {
            super.onBackPressed();
        }
    }

    public void showFullMap() {
        mCoordinator.focusOnMap();
        mCoordinator.closeBottomSheet();
    }

    public void showPartner(long partnerId, boolean zoom) {
        mCoordinator.focusOnMap();
        mCoordinator.showPartner(partnerId, zoom);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mCoordinator.onSaveInstanceState(outState);
    }

    // TODO Fix progress bar

    @Override
    public void loadFilter() {
        FiltersFragment fragment = geFiltersFragment();
        if (fragment != null) {
            fragment.LoadFilters();
        }
    }

    @Override
    public void updateMapPaddingTop(int paddingTop) {
        mMapsFragment.updateMapPaddingTop(paddingTop);
    }

    @Override
    public void updateMapPaddingBottom(int paddingBottom) {
        mMapsFragment.updateMapPaddingBottom(paddingBottom);
    }

    @Override
    public void showPartner(long partnerId, boolean zoom, @Nullable GoogleMap.CancelableCallback callback) {
        mMapsFragment.showPartner(partnerId, zoom, callback);
    }

    @Override
    public void replaceBottomSheetByPartnerDetails(long partnerId, boolean animate) {
        mBottomSheetFragment = PartnerDetailFragment.newInstance(partnerId);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (animate) {
            transaction
                    .setCustomAnimations(
                            android.R.anim.fade_in,
                            android.R.anim.fade_out
                    );
        }
        transaction
                .replace(
                        R.id.bottom_sheet,
                        mBottomSheetFragment,
                        PartnerDetailFragment.TAG
                )
                .commit();
    }

    @Override
    public void replaceBottomSheetByFilters() {
        mBottomSheetFragment = FiltersFragment.newInstance(mCoordinator.getSearchText());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.bottom_sheet,
                        mBottomSheetFragment,
                        FiltersFragment.TAG
                )
                .commit();
    }

    @Override
    public void removeBottomSheetFragment() {
        if (mBottomSheetFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(mBottomSheetFragment)
                    .commit();
            mBottomSheetFragment = null;
        }
    }

    @Override
    public void moveOnFootprint() {
        mMapsFragment.moveOnFootprint();
    }

    @Override
    public void moveOnMyLocation() {
        mMapsFragment.moveOnMyLocation();

    }

    @Override
    public void hideSoftKeyboard() {
        SoftKeyboardUtil.hideSoftKeyboard(MapsActivity.this);
    }

    @Nullable
    public FiltersFragment geFiltersFragment() {
        if (mCoordinator.isFiltersBottomSheetOpened()) {
            return (FiltersFragment) mBottomSheetFragment;
        } else {
            return null;
        }
    }



}
