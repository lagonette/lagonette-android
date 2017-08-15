package org.lagonette.android.app.widget.coordinator;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import org.lagonette.android.R;
import org.lagonette.android.app.fragment.FiltersFragment;
import org.lagonette.android.app.widget.behavior.LaGonetteDisappearBehavior;
import org.lagonette.android.app.widget.behavior.ParallaxBehavior;
import org.lagonette.android.room.statement.Statement;
import org.lagonette.android.util.UiUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MainCoordinator
        extends BottomSheetBehavior.BottomSheetCallback
        implements BaseCoordinator,
        ParallaxBehavior.OnParallaxTranslationListener,
        LaGonetteDisappearBehavior.OnMoveListener, View.OnClickListener {

    public interface Callbacks {

        Context getContext();

        Resources getResources();

        Fragment getBottomSheetFragment();

        CoordinatorLayout getCoordinatorLayout();

        FloatingActionButton getMyLocationFab();

        FloatingActionButton getFiltersFab();

        View getSearchBar();

        TextView getSearchText();

        View getBottomSheet();

        void updateMapPaddingTop(int paddingTop);

        void updateMapPaddingBottom(int paddingBottom);

        void showPartner(long partnerId, boolean zoom, @Nullable GoogleMap.CancelableCallback callback);

        void replaceBottomSheetByPartnerDetails(long partnerId, boolean b);

        void replaceBottomSheetByFilters();

        void removeBottomSheetFragment();

    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({BOTTOM_SHEET_NONE, BOTTOM_SHEET_PARTNER, BOTTOM_SHEET_FILTERS})
    public @interface BottomSheetType {
    }

    public static final String STATE_BOTTOM_SHEET_TYPE = "state:bottom_sheet_type";

    public static final int BOTTOM_SHEET_NONE = 0;

    public static final int BOTTOM_SHEET_PARTNER = 1;

    public static final int BOTTOM_SHEET_FILTERS = 2;

    private GoogleMap.CancelableCallback mOpenPartnerCallback = new GoogleMap.CancelableCallback() {
        @Override
        public void onFinish() {
            openPartner();
        }

        @Override
        public void onCancel() {
            // Nothing to do here.
        }
    };

    @BottomSheetType
    private int mBottomSheetType = BOTTOM_SHEET_NONE;

    private Interpolator mInterpolator = new FastOutSlowInInterpolator();

    private boolean mShowPartnerAfterBottomSheetClose = false;

    private boolean mSwitchPartnerAfterBottomSheetCollapsed = false;

    private boolean mLoadFiltersAfterBottomSheetCollapsed = false;

    private long mSelectedPartnerId = Statement.NO_ID;

    private boolean mZoomForPartnerId;

    private int mBottomSheetBackgroundColor;

    private int mFiltersBottomSheetBackgroundColor;

    private int mStatusBarHeight;

    private int mSearchBarVerticalMargin;

    private int mParallaxMapsPaddingTop = 0;

    private int mSearchBarMapsPaddingTop = 0;

    private LaGonetteDisappearBehavior mSearchBarBehavior;

    private BottomSheetBehavior<View> mBottomSheetBehavior;

    private Callbacks mCallbacks;

    public MainCoordinator(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        Context context = mCallbacks.getContext();
        View searchBar = mCallbacks.getSearchBar();

        mFiltersBottomSheetBackgroundColor = ContextCompat.getColor(context, R.color.colorPrimary);
        mBottomSheetBackgroundColor = ContextCompat.getColor(context, android.R.color.background_light);
        mSearchBarBehavior = LaGonetteDisappearBehavior.from(searchBar);
        mSearchBarBehavior.setOnMoveListener(MainCoordinator.this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mStatusBarHeight = UiUtil.getStatusBarHeight(mCallbacks.getResources());
        mSearchBarVerticalMargin = setupSearchBarMargin();

        mBottomSheetBehavior = BottomSheetBehavior.from(mCallbacks.getBottomSheet());
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBottomSheetBehavior.setBottomSheetCallback(MainCoordinator.this);

        mCallbacks.getFiltersFab().setOnClickListener(MainCoordinator.this);
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(STATE_BOTTOM_SHEET_TYPE, mBottomSheetType);
    }

    // TODO put this in onCreate ?
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        View searchBar = mCallbacks.getSearchBar();
        final View bottomSheet = mCallbacks.getBottomSheet();

        //noinspection WrongConstant
        mBottomSheetType = savedInstanceState.getInt(STATE_BOTTOM_SHEET_TYPE, BOTTOM_SHEET_NONE);

        // TODO put in xml
        if (mBottomSheetType == BOTTOM_SHEET_FILTERS) {
            bottomSheet.setBackgroundColor(mFiltersBottomSheetBackgroundColor);
        }

        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                updateBottomSheetPadding(bottomSheet, mSearchBarVerticalMargin + searchBar.getHeight());
            }
        } else if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            // Workaround to prevent undefined fab state on configuration change.
            bottomSheet.post(() -> onSlide(bottomSheet, 0));
        }
    }

    @Override
    public void onStateChanged(@NonNull View bottomSheet, int newState) {
        if (BottomSheetBehavior.STATE_HIDDEN == newState) {
            mCallbacks.removeBottomSheetFragment();
            mBottomSheetType = BOTTOM_SHEET_NONE;
            bottomSheet.setTag(null);
            bottomSheet.setBackgroundColor(mBottomSheetBackgroundColor);
            bottomSheet.getLayoutParams().height = CoordinatorLayout.LayoutParams.MATCH_PARENT;
            if (mShowPartnerAfterBottomSheetClose) {
                mShowPartnerAfterBottomSheetClose = false;
                showPartner(mSelectedPartnerId, mZoomForPartnerId);
            }
        } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
            if (mSwitchPartnerAfterBottomSheetCollapsed) {
                mSwitchPartnerAfterBottomSheetCollapsed = false;
                switchPartner(bottomSheet);
            }

            if (mLoadFiltersAfterBottomSheetCollapsed) {
                mLoadFiltersAfterBottomSheetCollapsed = false;
                FiltersFragment fragment = geFiltersFragment();
                if (fragment != null) {
                    fragment.LoadFilters();
                }
            }
        }
    }

    @Override
    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        manageStatusBarPaddingOnBottomSheetSlide(bottomSheet);
        if (mBottomSheetType == BOTTOM_SHEET_PARTNER) {
            //TODO Optimize !
            manageBottomSheetFabOnPartnerSlide(slideOffset);
        }
    }

    private void manageBottomSheetFabOnPartnerSlide(float slideOffset) {
        //TODO Optimize !
        FloatingActionButton filtersFab = mCallbacks.getFiltersFab();
        float scale = slideOffset * 4 - 1;
        scale = scale > 1
                ? 1
                : scale < 0
                ? 0
                : scale;
        scale = 1 - scale;
        scale = mInterpolator.getInterpolation(scale);
        filtersFab.setScaleX(scale);
        filtersFab.setScaleY(scale);
    }

    private void manageStatusBarPaddingOnBottomSheetSlide(@NonNull View bottomSheet) {
        //TODO Optimize !
        int height = mCallbacks.getSearchBar().getHeight();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            height += mSearchBarVerticalMargin;
        }
        int padding = bottomSheet.getTop();
        padding = padding > height
                ? height
                : padding < 0
                ? 0
                : padding;
        padding = height - padding;
        updateBottomSheetPadding(bottomSheet, padding);
    }

    private void updateBottomSheetPadding(@NonNull View bottomSheet, int top) {
        bottomSheet.setPadding(
                bottomSheet.getPaddingLeft(),
                top,
                bottomSheet.getPaddingRight(),
                bottomSheet.getPaddingBottom()
        );
    }

    public void showPartner(long partnerId, boolean zoom) {
        mSelectedPartnerId = partnerId;
        if (mBottomSheetType == BOTTOM_SHEET_FILTERS) {
            mShowPartnerAfterBottomSheetClose = true;
            mZoomForPartnerId = zoom;
            closeBottomSheet();
        } else if (mBottomSheetType == BOTTOM_SHEET_PARTNER) {
            switchPartner(mCallbacks.getBottomSheet());
        } else {
            moveMapAndOpenPartner(zoom);
        }
    }

    public void moveMapAndOpenPartner(boolean zoom) {
        mCallbacks.showPartner(mSelectedPartnerId, zoom, mOpenPartnerCallback);
    }

    public void openPartner() {
        mCallbacks.replaceBottomSheetByPartnerDetails(mSelectedPartnerId, false);

        View coordinatorLayout = mCallbacks.getCoordinatorLayout();
        View bottomSheet = mCallbacks.getBottomSheet();

        bottomSheet.setTag(mSelectedPartnerId);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheet.getLayoutParams().height = coordinatorLayout.getHeight() * 3 / 4;
        mSearchBarBehavior.enable();
        mBottomSheetType = BOTTOM_SHEET_PARTNER;
    }

    private void switchPartner(@NonNull View bottomSheet) {
        if (BottomSheetBehavior.STATE_COLLAPSED != mBottomSheetBehavior.getState()) {
            mSwitchPartnerAfterBottomSheetCollapsed = true;
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            long currentPartnerId = (long) bottomSheet.getTag();
            if (currentPartnerId != mSelectedPartnerId) {
                mCallbacks.replaceBottomSheetByPartnerDetails(mSelectedPartnerId, true);
                bottomSheet.setTag(mSelectedPartnerId);
            }
            mCallbacks.showPartner(mSelectedPartnerId, false, null);
            mBottomSheetType = BOTTOM_SHEET_PARTNER;
        }
    }

    private void showFilters() {
        if (mBottomSheetType == BOTTOM_SHEET_NONE) {
            openFilters();
        }
    }

    public void expandFilters() {
        if (mBottomSheetType == BOTTOM_SHEET_FILTERS) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    private void openFilters() {
        mCallbacks.replaceBottomSheetByFilters();
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mCallbacks.getBottomSheet().setBackgroundColor(mFiltersBottomSheetBackgroundColor);
        mSearchBarBehavior.disable();
        mLoadFiltersAfterBottomSheetCollapsed = true;
        mBottomSheetType = BOTTOM_SHEET_FILTERS;
    }

    public void closeBottomSheet() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    public boolean isBottomSheetOpen() {
        return mBottomSheetType != BOTTOM_SHEET_NONE;
    }

    public boolean isPartnerBottomSheetOpened() {
        return mBottomSheetType == BOTTOM_SHEET_PARTNER;
    }

    public boolean isBottomSheetClose() {
        return mBottomSheetType == BOTTOM_SHEET_NONE;
    }

    public boolean isFiltersBottomSheetOpened() {
        return mBottomSheetType == BOTTOM_SHEET_FILTERS;
    }

    @Nullable
    public FiltersFragment geFiltersFragment() {
        if (mBottomSheetType == BOTTOM_SHEET_FILTERS) {
            return (FiltersFragment) mCallbacks.getBottomSheetFragment();
        } else {
            return null;
        }
    }

    private int setupSearchBarMargin() {
        // Why is not CoordinatorLayout.LayoutParams ?
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mCallbacks.getSearchBar().getLayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            params.setMargins(
                    params.leftMargin,
                    params.topMargin + mStatusBarHeight,
                    params.rightMargin,
                    params.bottomMargin
            );
        }
        return params.topMargin + params.bottomMargin;
    }

    @Override
    public void onMove(View child, int translationY) {
        mSearchBarMapsPaddingTop = child.getBottom() + translationY;
        updateMapPaddingTop();
    }

    @Override
    public void onParallaxTranslation(float translationY) {
        // TODO useless ?
//        mParallaxMapsPaddingTop = (int) -translationY;
//        updateMapPaddingTop();
//        updateMapPaddingBottom();
    }

    public void updateMapPaddingTop() {
        mCallbacks.updateMapPaddingTop(mParallaxMapsPaddingTop + mSearchBarMapsPaddingTop);
    }

    // TODO useless ?
    public void updateMapPaddingBottom() {
        mCallbacks.updateMapPaddingBottom(mParallaxMapsPaddingTop);
    }

    public boolean onBackPressed() {
        if (isBottomSheetOpen()) {
            closeBottomSheet();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.filters_fab:
                onBottomSheetFabClick();
                break;
            default:
                throw new IllegalArgumentException("Unknown view id: " + id);
        }
    }

    private void onBottomSheetFabClick() {
        showFilters();
    }

}
