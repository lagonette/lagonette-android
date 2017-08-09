package org.lagonette.android.app.activity;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import org.lagonette.android.R;
import org.lagonette.android.app.fragment.FiltersFragment;
import org.lagonette.android.app.fragment.MapsFragment;
import org.lagonette.android.app.fragment.PartnerDetailFragment;
import org.lagonette.android.app.widget.behavior.ParallaxBehavior;
import org.lagonette.android.app.widget.coordinator.MainCoordinator;
import org.lagonette.android.util.SearchUtil;
import org.lagonette.android.util.SoftKeyboardUtil;

public class MapsActivity
        extends BaseActivity
        implements MapsFragment.Callback,
        FiltersFragment.Callback,
        MainCoordinator.Callbacks,
        View.OnClickListener,
        View.OnLongClickListener {

    private static final String TAG = "MapsActivity";

    private MapsFragment mMapsFragment;

    private Fragment mBottomSheetFragment;

    private View mSearchBar;

    private EditText mSearchText;

    private ImageButton mSearchClear;

    private View mBottomSheet;

    private View mContentView;

    private FloatingActionButton mMyLocationFab;

    private FloatingActionButton mFiltersFab;

    private CoordinatorLayout mCoordinatorLayout;

    private MainCoordinator mCoordinator;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_maps);
    }

    @Override
    protected void onViewCreated() {
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mSearchBar = findViewById(R.id.search_bar);
        mSearchText = (EditText) findViewById(R.id.search_text);
        mSearchClear = (ImageButton) findViewById(R.id.search_clear);
        mBottomSheet = findViewById(R.id.bottom_sheet);
        mMyLocationFab = (FloatingActionButton) findViewById(R.id.my_location_fab);
        mFiltersFab = (FloatingActionButton) findViewById(R.id.filters_fab);
    }

    @Override
    protected void onActivityCreated(Bundle savedInstanceState) {
        mCoordinator = new MainCoordinator(MapsActivity.this);
        mCoordinator.onCreate(savedInstanceState);
        mCoordinator.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            mMapsFragment = MapsFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content, mMapsFragment, MapsFragment.TAG)
                    .commit();
        } else {
            mMapsFragment = (MapsFragment) getSupportFragmentManager()
                    .findFragmentByTag(MapsFragment.TAG);
        }

        mSearchText.setCursorVisible(false);
        mSearchText.setOnClickListener(MapsActivity.this);
        //TODO Activity leaks ?
        // Add TextWatcher later to avoid callback called on configuration changed.
        mSearchText.post(
                new Runnable() {
                    @Override
                    public void run() {
                        mSearchText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                MapsActivity.this.onSearchTextChanged(s.toString());
                            }
                        });
                    }
                }
        );

        mSearchClear.setOnClickListener(MapsActivity.this);

        mMyLocationFab.setOnClickListener(MapsActivity.this);
        mMyLocationFab.setOnLongClickListener(MapsActivity.this);

        mContentView = findViewById(R.id.content);

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

    @Override
    public void hideMyLocationButton() {
        mMyLocationFab.animate()
                .scaleX(0f)
                .scaleY(0f)
                .setDuration(300)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mMyLocationFab.setVisibility(View.GONE);
                        mMyLocationFab.animate().setListener(null);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();
    }

    @Override
    public void showMyLocationButton() {
        mMyLocationFab.setVisibility(View.VISIBLE);
        mMyLocationFab.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(300)
                .start();
    }

    @Override
    public void showFullMap() {
        focusOnMap();
        mCoordinator.closeBottomSheet();
    }

    @Override
    public void onMapReady() {
        ParallaxBehavior<View> parallaxBehavior = ParallaxBehavior.from(mContentView);
        parallaxBehavior.setOnParallaxTranslationListener(mCoordinator);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.search_text:
                onSearchTextClick();
                break;
            case R.id.my_location_fab:
                onMyLocationFabClick();
                break;
            case R.id.search_clear:
                onSearchClearClick();
                break;
            default:
                throw new IllegalArgumentException("Unknown view id: " + id);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.my_location_fab:
                return onMyLocationFabLongClick();
            default:
                throw new IllegalArgumentException("Unknown view id: " + id);
        }
    }

    private void onMyLocationFabClick() {
        mMapsFragment.moveOnMyLocation();
    }

    private boolean onMyLocationFabLongClick() {
        mMapsFragment.moveOnFootprint();
        return true;
    }

    private boolean onSearchTextClick() {
        mSearchText.setCursorVisible(true);
        return true;
    }

    private void onSearchClearClick() {
        focusOnMap();
        String currentText = mSearchText.getText().toString();
        if (!SearchUtil.DEFAULT_SEARCH.equals(currentText)) {
            mSearchText.setText(SearchUtil.DEFAULT_SEARCH);
        }
    }

    @Override
    public void showPartner(long partnerId, boolean zoom) {
        focusOnMap();
        mCoordinator.showPartner(partnerId, zoom);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mCoordinator.onSaveInstanceState(outState);
    }

    private void onSearchTextChanged(String search) {
        mMapsFragment.filterPartner(search);
        FiltersFragment filtersFragment = mCoordinator.geFiltersFragment();
        if (filtersFragment != null) {
            //noinspection ConstantConditions
            filtersFragment.filterPartner(search);
        }
    }

    @Override
    public Context getContext() {
        return MapsActivity.this;
    }

    @Override
    public Fragment getBottomSheetFragment() {
        return mBottomSheetFragment;
    }

    @Override
    public CoordinatorLayout getCoordinatorLayout() {
        return mCoordinatorLayout;
    }

    @Override
    public FloatingActionButton getMyLocationFab() {
        return mMyLocationFab;
    }

    @Override
    public FloatingActionButton getFiltersFab() {
        return mFiltersFab;
    }

    @Override
    public View getSearchBar() {
        return mSearchBar;
    }

    @Override
    public TextView getSearchText() {
        return mSearchText;
    }

    @Override
    public View getBottomSheet() {
        return mBottomSheet;
    }

    @Override
    public void updateMapPaddingTop(int paddingTop) {
        mMapsFragment.updateMapPaddingTop(paddingTop);
    }

    @Override
    public void updateMapPaddingBottom(int paddingBottom) {
        mMapsFragment.updateMapPaddingBottom(paddingBottom);
    }

    private void focusOnMap() {
        mSearchText.setCursorVisible(false);
        SoftKeyboardUtil.hideSoftKeyboard(MapsActivity.this);
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
        mBottomSheetFragment = FiltersFragment.newInstance(mSearchText.getText().toString());
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

}
