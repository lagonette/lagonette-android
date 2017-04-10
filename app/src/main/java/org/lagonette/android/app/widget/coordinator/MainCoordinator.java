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

import org.lagonette.android.R;
import org.lagonette.android.app.fragment.FiltersFragment;
import org.lagonette.android.app.widget.behavior.GonetteDisappearBehavior;
import org.lagonette.android.app.widget.behavior.ParallaxBehavior;
import org.lagonette.android.content.contract.GonetteContract;
import org.lagonette.android.util.UiUtil;
import com.google.android.gms.maps.GoogleMap;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MainCoordinator
        extends BottomSheetBehavior.BottomSheetCallback
        implements BaseCoordinator,
        ParallaxBehavior.OnParallaxTranslationListener,
        GonetteDisappearBehavior.OnMoveListener, View.OnClickListener {

    public interface Callbacks {

        Context getContext();

        Resources getResources();

        Fragment getBottomSheetFragment();

        CoordinatorLayout getCoordinatorLayout();

        FloatingActionButton getMyLocationFab();

        FloatingActionButton getBottomSheetFab();

        View getSearchBar();

        TextView getSearchText();

        View getBottomSheet();

        void updateMapPaddingTop(int paddingTop);

        void updateMapPaddingBottom(int paddingBottom);

        void showPartner(long partnerId, boolean zoom, @Nullable GoogleMap.CancelableCallback callback);

        void replaceBottomSheetByPartnerDetails(long partnerId, boolean b);

        void replaceBottomSheetByFilters();

        void removeBottomSheetFragment();

        void startDirection(long partnerId);
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

    private long mSelectedPartnerId = GonetteContract.NO_ID;

    private boolean mZoomForPartnerId;

    private float mFABElevationPrimary;

    private float mFABElevationSecondary;

    private int mBottomSheetBackgroundColor;

    private int mFiltersBottomSheetBackgroundColor;

    private int mStatusBarHeight;

    private int mFabBottomMargin;

    private int mSearchBarVerticalMargin;

    private int mParallaxMapsPaddingTop = 0;

    private int mSearchBarMapsPaddingTop = 0;

    private boolean mIsDirectionVisible = false;

    private GonetteDisappearBehavior mMyLocationFabBehavior;

    private GonetteDisappearBehavior mBottomSheetFabBehavior;

    private GonetteDisappearBehavior mSearchBarBehavior;

    private BottomSheetBehavior<View> mBottomSheetBehavior;

    private Callbacks mCallbacks;

    public MainCoordinator(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        Context context = mCallbacks.getContext();
        Resources resources = context.getResources();
        FloatingActionButton bottomSheetFab = mCallbacks.getBottomSheetFab();
        FloatingActionButton myLocationFab = mCallbacks.getMyLocationFab();
        View searchBar = mCallbacks.getSearchBar();

        mFABElevationPrimary = resources.getDimension(R.dimen.fab_elevation_primary);
        mFABElevationSecondary = resources.getDimension(R.dimen.fab_elevation_secondary);
        mFiltersBottomSheetBackgroundColor = ContextCompat.getColor(context, R.color.colorPrimary);
        mBottomSheetBackgroundColor = ContextCompat.getColor(context, android.R.color.background_light);
        mFabBottomMargin = ((CoordinatorLayout.LayoutParams) bottomSheetFab.getLayoutParams()).bottomMargin;
        mMyLocationFabBehavior = GonetteDisappearBehavior.from(myLocationFab);
        mBottomSheetFabBehavior = GonetteDisappearBehavior.from(bottomSheetFab);
        mSearchBarBehavior = GonetteDisappearBehavior.from(searchBar);
        mSearchBarBehavior.setOnMoveListener(MainCoordinator.this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mStatusBarHeight = UiUtil.getStatusBarHeight(mCallbacks.getResources());
        mSearchBarVerticalMargin = setupSearchBarMargin();

        mBottomSheetBehavior = BottomSheetBehavior.from(mCallbacks.getBottomSheet());
        mBottomSheetBehavior.setPeekHeight(500);
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBottomSheetBehavior.setBottomSheetCallback(MainCoordinator.this);

        mCallbacks.getBottomSheetFab().setOnClickListener(MainCoordinator.this);
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(STATE_BOTTOM_SHEET_TYPE, mBottomSheetType);
    }

    // TODO put this in onCreate ?
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        FloatingActionButton bottomSheetFab = mCallbacks.getBottomSheetFab();
        View searchBar = mCallbacks.getSearchBar();
        final View bottomSheet = mCallbacks.getBottomSheet();

        //noinspection WrongConstant
        mBottomSheetType = savedInstanceState.getInt(STATE_BOTTOM_SHEET_TYPE, BOTTOM_SHEET_NONE);

        // TODO put in xml
        if (mBottomSheetType == BOTTOM_SHEET_FILTERS) {
            bottomSheet.setBackgroundColor(mFiltersBottomSheetBackgroundColor);
        } else if (mBottomSheetType == BOTTOM_SHEET_PARTNER) {
            bottomSheetFab.setImageResource(R.drawable.ic_directions_white_24dp);
            mIsDirectionVisible = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                bottomSheetFab.setCompatElevation(mFABElevationPrimary);
            }
        }

        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                updateBottomSheetPadding(bottomSheet, mSearchBarVerticalMargin + searchBar.getHeight());
            }
        } else if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            // Workaround to prevent undefined fab state on configuration change.
            bottomSheet.post(new Runnable() {
                @Override
                public void run() {
                    onSlide(bottomSheet, 0);
                }
            });
        }
    }

    @Override
    public void onStateChanged(@NonNull View bottomSheet, int newState) {
        if (BottomSheetBehavior.STATE_HIDDEN == newState) {
            FloatingActionButton bottomSheetFab = mCallbacks.getBottomSheetFab();
            FloatingActionButton myLocationFab = mCallbacks.getMyLocationFab();

            mCallbacks.removeBottomSheetFragment();
            mBottomSheetType = BOTTOM_SHEET_NONE;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                bottomSheetFab.setCompatElevation(mFABElevationSecondary);
            }
            bottomSheetFab.setTag(null);
            bottomSheet.setBackgroundColor(mBottomSheetBackgroundColor);
            bottomSheet.getLayoutParams().height = CoordinatorLayout.LayoutParams.MATCH_PARENT;
            mMyLocationFabBehavior.setLeaveLength(myLocationFab.getHeight());
            mMyLocationFabBehavior.setLeaveOffset(0);
            mMyLocationFabBehavior.setMoveOffset(0);
            mBottomSheetFabBehavior.setLeaveLength(bottomSheetFab.getHeight());
            mBottomSheetFabBehavior.setLeaveOffset(0);
            mBottomSheetFabBehavior.setMoveLength(bottomSheetFab.getHeight());
            mBottomSheetFabBehavior.setMoveOffset(0);
            mBottomSheetFabBehavior.setLeaveMethod(GonetteDisappearBehavior.LEAVE_METHOD_ALPHA);
            if (mShowPartnerAfterBottomSheetClose) {
                mShowPartnerAfterBottomSheetClose = false;
                showPartner(mSelectedPartnerId, mZoomForPartnerId);
            }
        } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
            if (mSwitchPartnerAfterBottomSheetCollapsed) {
                mSwitchPartnerAfterBottomSheetCollapsed = false;
                switchPartner();
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
            int translationY = mCallbacks.getCoordinatorLayout().getBottom() - bottomSheet.getTop();
            manageBottomSheetFabOnPartnerSlide(translationY, slideOffset);
        }
    }

    private void manageBottomSheetFabOnPartnerSlide(int translationY, float slideOffset) {
        //TODO Optimize !
        FloatingActionButton bottomSheetFab = mCallbacks.getBottomSheetFab();
        float scale = slideOffset * 4 - 1;
        scale = scale > 1
                ? 1
                : scale < 0
                ? 0
                : scale;
        scale = 1 - scale;
        scale = mInterpolator.getInterpolation(scale);
        bottomSheetFab.setScaleX(scale);
        bottomSheetFab.setScaleY(scale);

        int fabCenter = bottomSheetFab.getHeight() / 2 + mFabBottomMargin;
        if (!mIsDirectionVisible && translationY > fabCenter) {
            bottomSheetFab.setImageResource(R.drawable.ic_directions_white_24dp);
            mIsDirectionVisible = true;
        } else if (mIsDirectionVisible && translationY <= fabCenter) {
            bottomSheetFab.setImageResource(R.drawable.ic_filter_list_white_24dp);
            mIsDirectionVisible = false;
        }
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
            switchPartner();
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
        View searchBar = mCallbacks.getSearchBar();
        FloatingActionButton bottomSheetFab = mCallbacks.getBottomSheetFab();
        FloatingActionButton myLocationFab = mCallbacks.getMyLocationFab();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bottomSheetFab.setCompatElevation(mFABElevationPrimary);
        }
        bottomSheetFab.setTag(mSelectedPartnerId);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheet.getLayoutParams().height = coordinatorLayout.getHeight() * 3 / 4;
        int offset = bottomSheetFab.getHeight() / 2 + ((CoordinatorLayout.LayoutParams) bottomSheetFab.getLayoutParams()).bottomMargin;
        mMyLocationFabBehavior.setLeaveLength(myLocationFab.getHeight());
        mMyLocationFabBehavior.setLeaveOffset(offset);
        mMyLocationFabBehavior.setMoveOffset(offset);
        mBottomSheetFabBehavior.setLeaveLength(bottomSheetFab.getHeight());
        mBottomSheetFabBehavior.setLeaveOffset(mBottomSheetBehavior.getPeekHeight() + offset * 2);
        mBottomSheetFabBehavior.setMoveLength(bottomSheetFab.getHeight() + mBottomSheetBehavior.getPeekHeight() + offset * 2);
        mBottomSheetFabBehavior.setMoveOffset(offset);
        mBottomSheetFabBehavior.setLeaveMethod(GonetteDisappearBehavior.LEAVE_METHOD_SCALE);
        mSearchBarBehavior.enable();
        mSearchBarBehavior.setLeaveLength(myLocationFab.getHeight());
        mSearchBarBehavior.setLeaveOffset(offset); // TODO improve: do this only one time.
        mSearchBarBehavior.setMoveLength(searchBar.getBottom() - mStatusBarHeight);
        mSearchBarBehavior.setMoveOffset(offset);
        mBottomSheetType = BOTTOM_SHEET_PARTNER;
    }

    private void switchPartner() {
        if (BottomSheetBehavior.STATE_COLLAPSED != mBottomSheetBehavior.getState()) {
            mSwitchPartnerAfterBottomSheetCollapsed = true;
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            long currentPartnerId = (long) mCallbacks.getBottomSheetFab().getTag();
            if (currentPartnerId != mSelectedPartnerId) {
                mCallbacks.replaceBottomSheetByPartnerDetails(mSelectedPartnerId, true);
                mCallbacks.getBottomSheetFab().setTag(mSelectedPartnerId);
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
//        mParallaxMapsPaddingTop = (int) -translationY;
//        updateMapPaddingTop();
//        updateMapPaddingBottom();
    }

    public void updateMapPaddingTop() {
        mCallbacks.updateMapPaddingTop(mParallaxMapsPaddingTop + mSearchBarMapsPaddingTop);
    }

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
            case R.id.bottom_sheet_fab:
                onBottomSheetFabClick();
                break;
            default:
                throw new IllegalArgumentException("Unknown view id: " + id);
        }
    }

    private void onBottomSheetFabClick() {
        if (isPartnerBottomSheetOpened()) {
            mCallbacks.startDirection((long) mCallbacks.getBottomSheetFab().getTag());
        } else if (isBottomSheetClose()) {
            showFilters();
        }
    }

}
