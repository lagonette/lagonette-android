package org.lagonette.android.app.widget.coordinator;

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

import org.lagonette.android.R;
import org.lagonette.android.app.widget.behavior.LaGonetteDisappearBehavior;
import org.lagonette.android.app.widget.behavior.ParallaxBehavior;
import org.lagonette.android.room.statement.Statement;
import org.lagonette.android.util.SearchUtil;
import org.lagonette.android.util.UiUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MainCoordinator
        extends BottomSheetBehavior.BottomSheetCallback
        implements ParallaxBehavior.OnParallaxTranslationListener,
        LaGonetteDisappearBehavior.OnMoveListener,
        View.OnClickListener,
        View.OnLongClickListener {

    public interface Callbacks {

        void updateMapPaddingTop(int paddingTop);

        void updateMapPaddingBottom(int paddingBottom);

        void showPartner(long partnerId, boolean zoom, @Nullable GoogleMap.CancelableCallback callback);

        void replaceBottomSheetByPartnerDetails(long partnerId, boolean b);

        void replaceBottomSheetByFilters();

        void removeBottomSheetFragment();

        void moveOnFootprint();

        void moveOnMyLocation();

        void hideSoftKeyboard();

        void doSearch(String search);

        void loadFilter();

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

    @NonNull
    private Callbacks mCallbacks;

    public MainCoordinator(@NonNull Context context, @NonNull Callbacks callbacks, @Nullable Bundle savedInstanceState) {
        mCallbacks = callbacks;

        mStatusBarHeight = UiUtil.getStatusBarHeight(context.getResources());
        mFiltersBottomSheetBackgroundColor = ContextCompat.getColor(context, R.color.colorPrimary);
        mBottomSheetBackgroundColor = ContextCompat.getColor(context, android.R.color.background_light);
    }

    public MainCoordinator injectView(@NonNull View view) {
        mContentView = view.findViewById(R.id.content);
        mCoordinatorLayout = view.findViewById(R.id.coordinator_layout);
        mSearchBar = view.findViewById(R.id.search_bar);
        mSearchText = view.findViewById(R.id.search_text);
        mSearchClear = view.findViewById(R.id.search_clear);
        mProgressBar = view.findViewById(R.id.progress_bar);
        mBottomSheet = view.findViewById(R.id.bottom_sheet);
        mMyLocationFab = view.findViewById(R.id.my_location_fab);
        mFiltersFab = view.findViewById(R.id.filters_fab);
        return MainCoordinator.this;
    }

    public MainCoordinator start() {
        mSearchBarVerticalMargin = setupSearchBarMargin();

        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBottomSheetBehavior.setBottomSheetCallback(MainCoordinator.this);

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
                        mCallbacks.doSearch(s.toString());
                    }
                })
        );

        mSearchClear.setOnClickListener(MainCoordinator.this);

        mMyLocationFab.setOnClickListener(MainCoordinator.this);
        mMyLocationFab.setOnLongClickListener(MainCoordinator.this);

        return MainCoordinator.this;
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
                mCallbacks.loadFilter();
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

    public void showPartner(long partnerId, boolean zoom) {
        mSelectedPartnerId = partnerId;
        if (mBottomSheetType == BOTTOM_SHEET_FILTERS) {
            mShowPartnerAfterBottomSheetClose = true;
            mZoomForPartnerId = zoom;
            closeBottomSheet();
        } else if (mBottomSheetType == BOTTOM_SHEET_PARTNER) {
            switchPartner(mBottomSheet);
        } else {
            moveMapAndOpenPartner(zoom);
        }
    }

    public void moveMapAndOpenPartner(boolean zoom) {
        mCallbacks.showPartner(mSelectedPartnerId, zoom, mOpenPartnerCallback);
    }

    public void openPartner() {
        mCallbacks.replaceBottomSheetByPartnerDetails(mSelectedPartnerId, false);

        mBottomSheet.setTag(mSelectedPartnerId);
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
            case R.id.search_text:
                onSearchTextClick();
                break;
            case R.id.my_location_fab:
                mCallbacks.moveOnMyLocation();
                break;
            case R.id.search_clear:
                clearSearch();
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
                mCallbacks.moveOnFootprint();
                return true;
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
        mCallbacks.hideSoftKeyboard();
    }

    public void onMapReady() {
        ParallaxBehavior<View> parallaxBehavior = ParallaxBehavior.from(mContentView);
        parallaxBehavior.setOnParallaxTranslationListener(MainCoordinator.this);
    }

}
