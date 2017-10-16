package org.lagonette.app.app.widget.coordinator;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.android.gms.maps.GoogleMap;

import org.lagonette.app.R;
import org.lagonette.app.app.arch.EventShipper;
import org.lagonette.app.app.widget.behavior.LaGonetteDisappearBehavior;
import org.lagonette.app.app.widget.behavior.ParallaxBehavior;
import org.lagonette.app.room.statement.Statement;
import org.lagonette.app.util.MapMovement;
import org.lagonette.app.util.SearchUtil;
import org.lagonette.app.util.UiUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MainCoordinator
        extends ViewCoordinator
        implements ParallaxBehavior.OnParallaxTranslationListener,
        LaGonetteDisappearBehavior.OnMoveListener,
        View.OnClickListener {

    public interface Callbacks {

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

    private View mSearchBar;

    private EditText mSearchText;

    private ImageButton mSearchClear;

    private ProgressBar mProgressBar;

    private View mBottomSheet;

    private View mContentView;

    private FloatingActionButton mMyLocationFab;

    private FloatingActionButton mFiltersFab;

    private CoordinatorLayout mCoordinatorLayout;

    @BottomSheetType
    private int mBottomSheetType = BOTTOM_SHEET_NONE;

    private Interpolator mInterpolator = new FastOutSlowInInterpolator();

    private boolean mShowPartnerAfterBottomSheetClose = false;

    private boolean mSwitchPartnerAfterBottomSheetCollapsed = false;

    private boolean mLoadFiltersAfterBottomSheetCollapsed = false;

    private long mSelectedLocationId = Statement.NO_ID;

    private boolean mZoomForLocationId;

    private int mBottomSheetBackgroundColor;

    private int mFiltersBottomSheetBackgroundColor;

    private int mStatusBarHeight;

    private int mSearchBarVerticalMargin;

    private int mParallaxMapsPaddingTop = 0;

    private int mSearchBarMapsPaddingTop = 0;

    private LaGonetteDisappearBehavior mSearchBarBehavior;

    private BottomSheetBehavior<View> mBottomSheetBehavior;

    @NonNull
    private final EventShipper.Sender<String> mSearchSender;

    @NonNull
    private final EventShipper.Sender<Integer> mMapMovementSender;

    @NonNull
    private final EventShipper.Notifier mHideKeyboardNotifier;

    @NonNull
    private final EventShipper.Sender<Integer> mMapBottomPaddingSender;

    @NonNull
    private final EventShipper.Sender<Integer> mMapTopPaddingSender;

    @NonNull
    private Callbacks mCallbacks;

    public MainCoordinator(
            @NonNull Context context,
            @NonNull EventShipper.Sender<String> searchSender,
            @NonNull EventShipper.Sender<Integer> mapMovementSender,
            @NonNull EventShipper.Notifier hideKeyboardNotifier,
            @NonNull EventShipper.Sender<Integer> mapTopPaddingSender,
            @NonNull EventShipper.Sender<Integer> mapBottomPaddingSender,
            @NonNull Callbacks callbacks) {
        super(context);
        mSearchSender = searchSender;
        mMapMovementSender = mapMovementSender;
        mHideKeyboardNotifier = hideKeyboardNotifier;
        mMapTopPaddingSender = mapTopPaddingSender;
        mMapBottomPaddingSender = mapBottomPaddingSender;
        mCallbacks = callbacks;

        mStatusBarHeight = UiUtil.getStatusBarHeight(context.getResources());
        mFiltersBottomSheetBackgroundColor = ContextCompat.getColor(context, R.color.colorPrimary);
        mBottomSheetBackgroundColor = ContextCompat.getColor(context, android.R.color.background_light);
    }

    @Override
    public void inject(@NonNull View view) {
        mContentView = view.findViewById(R.id.content);
        mCoordinatorLayout = view.findViewById(R.id.coordinator_layout);
        mSearchBar = view.findViewById(R.id.search_bar);
        mSearchText = view.findViewById(R.id.search_text);
        mSearchClear = view.findViewById(R.id.search_clear);
        mProgressBar = view.findViewById(R.id.progress_bar);
        mBottomSheet = view.findViewById(R.id.bottom_sheet);
        mMyLocationFab = view.findViewById(R.id.my_location_fab);
        mFiltersFab = view.findViewById(R.id.filters_fab);
    }

    @Override
    public void start(@Nullable Bundle savedInstanceState) {
        mSearchBarVerticalMargin = setupSearchBarMargin();

        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                MainCoordinator.this.onStateChanged(bottomSheet, newState);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                MainCoordinator.this.onSlide(bottomSheet, slideOffset);
            }
        });

        mSearchBarBehavior = LaGonetteDisappearBehavior.from(mSearchBar);
        mSearchBarBehavior.setOnMoveListener(MainCoordinator.this);

        mFiltersFab.setOnClickListener(MainCoordinator.this);

        mSearchText.setCursorVisible(false);
        mSearchText.setOnClickListener(MainCoordinator.this);
        //TODO Activity leaks ?
        // Add TextWatcher later to avoid callback called on configuration changed.
        mSearchText.post(
                () -> mSearchText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mSearchSender.send(s.toString());
                    }
                })
        );

        mSearchClear.setOnClickListener(MainCoordinator.this);

        mMyLocationFab.setOnClickListener(
                view -> mMapMovementSender.send(MapMovement.MY_LOCATION)
        );
        mMyLocationFab.setOnLongClickListener(
                view -> {
                    mMapMovementSender.send(MapMovement.FOOTPRINT);
                    return true;
                }
        );
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(STATE_BOTTOM_SHEET_TYPE, mBottomSheetType);
    }

    // TODO put this in onCreate ?
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        //noinspection WrongConstant
        mBottomSheetType = savedInstanceState.getInt(STATE_BOTTOM_SHEET_TYPE, BOTTOM_SHEET_NONE);

        // TODO put in xml
        if (mBottomSheetType == BOTTOM_SHEET_FILTERS) {
            mBottomSheet.setBackgroundColor(mFiltersBottomSheetBackgroundColor);
        }

        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                updateBottomSheetPadding(mBottomSheet, mSearchBarVerticalMargin + mSearchBar.getHeight());
            }
        } else if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            // Workaround to prevent undefined fab state on configuration change.
            mBottomSheet.post(() -> onSlide(mBottomSheet, 0));
        }
    }

    public void onStateChanged(@NonNull View bottomSheet, int newState) {
        if (BottomSheetBehavior.STATE_HIDDEN == newState) {
            mCallbacks.removeBottomSheetFragment();
            mBottomSheetType = BOTTOM_SHEET_NONE;
            bottomSheet.setTag(null);
            bottomSheet.setBackgroundColor(mBottomSheetBackgroundColor);
            bottomSheet.getLayoutParams().height = CoordinatorLayout.LayoutParams.MATCH_PARENT;
            if (mShowPartnerAfterBottomSheetClose) {
                mShowPartnerAfterBottomSheetClose = false;
                showLocation(mSelectedLocationId, mZoomForLocationId);
            }
        } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
            if (mSwitchPartnerAfterBottomSheetCollapsed) {
                mSwitchPartnerAfterBottomSheetCollapsed = false;
                switchPartner(bottomSheet);
            }

            if (mLoadFiltersAfterBottomSheetCollapsed) { // TODO Maybe useless
                mLoadFiltersAfterBottomSheetCollapsed = false;
            }
        }
    }

    // TODO Show/hide Exchange office in filters ?

    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        manageStatusBarPaddingOnBottomSheetSlide(bottomSheet);
        if (mBottomSheetType == BOTTOM_SHEET_PARTNER) {
            //TODO Optimize !
            manageBottomSheetFabOnPartnerSlide(slideOffset);
        }
    }

    private void manageBottomSheetFabOnPartnerSlide(float slideOffset) {
        //TODO Optimize !
        float scale = slideOffset * 4 - 1;
        scale = scale > 1
                ? 1
                : scale < 0
                ? 0
                : scale;
        scale = 1 - scale;
        scale = mInterpolator.getInterpolation(scale);
        mFiltersFab.setScaleX(scale);
        mFiltersFab.setScaleY(scale);
    }

    private void manageStatusBarPaddingOnBottomSheetSlide(@NonNull View bottomSheet) {
        //TODO Optimize !
        int height = mSearchBar.getHeight();
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

    public void showLocation(long locationId, boolean zoom) {
        mSelectedLocationId = locationId;
        if (mBottomSheetType == BOTTOM_SHEET_FILTERS) {
            mShowPartnerAfterBottomSheetClose = true;
            mZoomForLocationId = zoom;
            closeBottomSheet();
        } else if (mBottomSheetType == BOTTOM_SHEET_PARTNER) {
            switchPartner(mBottomSheet);
        } else {
            moveMapAndOpenPartner(zoom);
        }
    }

    public void moveMapAndOpenPartner(boolean zoom) {
        mCallbacks.showPartner(mSelectedLocationId, zoom, mOpenPartnerCallback);
    }

    public void openPartner() {
        mCallbacks.replaceBottomSheetByPartnerDetails(mSelectedLocationId, false);

        mBottomSheet.setTag(mSelectedLocationId);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheet.getLayoutParams().height = mCoordinatorLayout.getHeight() * 3 / 4;
        mSearchBarBehavior.enable();
        mBottomSheetType = BOTTOM_SHEET_PARTNER;
    }

    private void switchPartner(@NonNull View bottomSheet) {
        if (BottomSheetBehavior.STATE_COLLAPSED != mBottomSheetBehavior.getState()) {
            mSwitchPartnerAfterBottomSheetCollapsed = true;
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            long currentPartnerId = (long) bottomSheet.getTag();
            if (currentPartnerId != mSelectedLocationId) {
                mCallbacks.replaceBottomSheetByPartnerDetails(mSelectedLocationId, true);
                bottomSheet.setTag(mSelectedLocationId);
            }
            mCallbacks.showPartner(mSelectedLocationId, false, null);
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
        mBottomSheet.setBackgroundColor(mFiltersBottomSheetBackgroundColor);
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

    private int setupSearchBarMargin() {
        // Why is not CoordinatorLayout.LayoutParams ?
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mSearchBar.getLayoutParams();
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
        mMapTopPaddingSender.send(mParallaxMapsPaddingTop + mSearchBarMapsPaddingTop);
    }

    // TODO useless ? Make the google logo move agqin!
    public void updateMapPaddingBottom() {
        mMapBottomPaddingSender.send(mParallaxMapsPaddingTop);
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
            case R.id.search_text:
                onSearchTextClick();
                break;
            case R.id.search_clear:
                clearSearch();
                break;
            default:
                throw new IllegalArgumentException("Unknown view id: " + id);
        }
    }

    private void onBottomSheetFabClick() {
        showFilters();
    }

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

    public void showMyLocationButton() {
        mMyLocationFab.setVisibility(View.VISIBLE);
        mMyLocationFab.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(300)
                .start();
    }

    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    public boolean onSearchTextClick() {
        mSearchText.setCursorVisible(true);
        return true;
    }

    public void clearSearch() {
        focusOnMap();
        String currentText = mSearchText.getText().toString();
        if (!SearchUtil.DEFAULT_SEARCH.equals(currentText)) {
            mSearchText.setText(SearchUtil.DEFAULT_SEARCH);
        }
    }

    public String getSearchText() {
        return mSearchText.getText().toString();
    }

    public void focusOnMap() {
        mSearchText.setCursorVisible(false);
        mHideKeyboardNotifier.call();
    }

    public void onMapReady() {
        ParallaxBehavior<View> parallaxBehavior = ParallaxBehavior.from(mContentView);
        parallaxBehavior.setOnParallaxTranslationListener(MainCoordinator.this);
    }

}
