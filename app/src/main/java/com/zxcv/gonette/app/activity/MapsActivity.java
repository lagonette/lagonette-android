package com.zxcv.gonette.app.activity;

import android.animation.Animator;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.maps.GoogleMap;
import com.zxcv.gonette.BuildConfig;
import com.zxcv.gonette.R;
import com.zxcv.gonette.app.fragment.FiltersFragment;
import com.zxcv.gonette.app.fragment.MapsFragment;
import com.zxcv.gonette.app.fragment.PartnerDetailFragment;
import com.zxcv.gonette.app.presenter.FiltersPresenter;
import com.zxcv.gonette.app.widget.behavior.GonetteDisappearBehavior;
import com.zxcv.gonette.app.widget.behavior.ParallaxBehavior;
import com.zxcv.gonette.content.contract.GonetteContract;
import com.zxcv.gonette.database.GonetteDatabaseOpenHelper;
import com.zxcv.gonette.util.SearchUtil;
import com.zxcv.gonette.util.UiUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MapsActivity
        extends BaseActivity
        implements MapsFragment.Callback,
        FiltersFragment.Callback,
        ParallaxBehavior.OnParallaxTranslationListener,
        View.OnClickListener,
        View.OnLongClickListener, GonetteDisappearBehavior.OnMoveListener {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({BOTTOM_SHEET_NONE, BOTTOM_SHEET_PARTNER, BOTTOM_SHEET_FILTERS})
    public @interface BottomSheetType {
    }

    public static final int BOTTOM_SHEET_NONE = 0;

    public static final int BOTTOM_SHEET_PARTNER = 1;

    public static final int BOTTOM_SHEET_FILTERS = 2;

    private static final String TAG = "MapsActivity";

    private MapsFragment mMapsFragment;

    private Fragment mBottomSheetFragment;

    private BottomSheetBehavior<View> mBottomSheetBehavior;

    private View mSearchBar;

    private EditText mSearchText;

    private ImageButton mSearchClear;

    private View mBottomSheet;

    private View mContentView;

    private FloatingActionButton mMyLocationFab;

    private FloatingActionButton mBottomSheetFab;

    private View mCoordinatorLayout;

    private BottomSheetManager mBottomSheetManager;

    private int mStatusBarHeight;

    private int mSearchBarVerticalMargin;

    private int mParallaxMapsPaddingTop = 0;

    private int mSearchBarMapsPaddingTop = 0;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_maps);
    }

    @Override
    protected void onViewCreated() {
        mCoordinatorLayout = findViewById(R.id.coordinator_layout);
        mSearchBar = findViewById(R.id.search_bar);
        mSearchText = (EditText) findViewById(R.id.search_text);
        mSearchClear = (ImageButton) findViewById(R.id.search_clear);
        mBottomSheet = findViewById(R.id.bottom_sheet);
        mMyLocationFab = (FloatingActionButton) findViewById(R.id.my_location_fab);
        mBottomSheetFab = (FloatingActionButton) findViewById(R.id.bottom_sheet_fab);
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

        mStatusBarHeight = UiUtil.getStatusBarHeight(getResources());

        mSearchBarVerticalMargin = setupSearchBarMargin();

        mBottomSheetManager = new BottomSheetManager(MapsActivity.this, getResources());

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
        mBottomSheetFab.setOnClickListener(MapsActivity.this);

        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehavior.setPeekHeight(500);
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBottomSheetBehavior.setBottomSheetCallback(mBottomSheetManager);

        mContentView = findViewById(R.id.content);

        if (BuildConfig.INSERT_DATA) {
            GonetteDatabaseOpenHelper.parseData(MapsActivity.this);
        }
    }

    private int setupSearchBarMargin() {
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
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mBottomSheetManager.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (mBottomSheetManager.isBottomSheetOpen()) {
            mBottomSheetManager.closeBottomSheet();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onParallaxTranslation(float translationY) {
        mParallaxMapsPaddingTop = (int) -translationY;
        updateMapsPaddingTop();
        updateMapsPaddingBottom();
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
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Override
    public void onMapReady() {
        ParallaxBehavior<View> parallaxBehavior = ParallaxBehavior.from(mContentView);
        parallaxBehavior.setOnParallaxTranslationListener(MapsActivity.this);
    }

    private void removeBottomSheetFragment() {
        if (mBottomSheetFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(mBottomSheetFragment)
                    .commit();
            mBottomSheetFragment = null;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.my_location_fab:
                onMyLocationFabClick();
                break;
            case R.id.bottom_sheet_fab:
                onBottomSheetFabClick();
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

    private void onBottomSheetFabClick() {
        if (mBottomSheetManager.isPartnerBottomSheetOpened()) {
            mMapsFragment.startDirection((long) mBottomSheetFab.getTag());
        } else if (mBottomSheetManager.isBottomSheetClose()) {
            mBottomSheetManager.showFilters();
        }
    }

    private void onSearchClearClick() {
        String currentText = mSearchText.getText().toString();
        if (!SearchUtil.DEFAULT_SEARCH.equals(currentText)) {
            mSearchText.setText(SearchUtil.DEFAULT_SEARCH);
        }
    }

    @Override
    public void showPartner(long partnerId, boolean zoom) {
        mBottomSheetManager.showPartner(partnerId, zoom);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBottomSheetManager.onSaveInstanceState(outState);
    }

    private void onSearchTextChanged(String search) {
        mMapsFragment.filterPartner(search);
        if (mBottomSheetManager.isFiltersBottomSheetOpened()) {
            //noinspection ConstantConditions
            mBottomSheetManager.geFiltersFragment().filterPartner(search);
        }
    }

    @Override
    public void onMove(View child, int translationY) {
        mSearchBarMapsPaddingTop = child.getBottom() + translationY;
        updateMapsPaddingTop();
    }

    private void updateMapsPaddingTop() {
        mMapsFragment.updateMapPaddingTop(mParallaxMapsPaddingTop + mSearchBarMapsPaddingTop);
    }

    private void updateMapsPaddingBottom() {
        mMapsFragment.updateMapPaddingBottom(mParallaxMapsPaddingTop);
    }

    public class BottomSheetManager extends BottomSheetBehavior.BottomSheetCallback {

        public static final String STATE_BOTTOM_SHEET_TYPE = "state:bottom_sheet_type";

        @BottomSheetType
        private int mBottomSheetType = BOTTOM_SHEET_NONE;

        private Interpolator mInterpolator = new DecelerateInterpolator();

        private boolean mShowPartnerAfterBottomSheetClose = false;

        private boolean mSwitchPartnerAfterBottomSheetCollapsed = false;

        private long mSelectedPartnerId = GonetteContract.NO_ID;

        private boolean mZoomForPartnerId;

        private final float mFABElevationPrimary;

        private final float mFABElevationSecondary;

        private int mBottomSheetBackgroundColor;

        private int mFiltersBottomSheetBackgroundColor;

        private final int mFabBottomMargin;

        private boolean mIsDirectionVisible = false;

        private GonetteDisappearBehavior mMyLocationFabBehavior;

        private GonetteDisappearBehavior mBottomSheetFabBehavior;

        private GonetteDisappearBehavior mSearchBarBehavior;

        public BottomSheetManager(@NonNull Context context, @NonNull Resources resources) {
            mFABElevationPrimary = resources.getDimension(R.dimen.fab_elevation_primary);
            mFABElevationSecondary = resources.getDimension(R.dimen.fab_elevation_secondary);
            mFiltersBottomSheetBackgroundColor = ContextCompat.getColor(context, R.color.colorPrimary);
            mBottomSheetBackgroundColor = ContextCompat.getColor(context, android.R.color.background_light);
            mFabBottomMargin = ((CoordinatorLayout.LayoutParams) mBottomSheetFab.getLayoutParams()).bottomMargin;
            mMyLocationFabBehavior = GonetteDisappearBehavior.from(mMyLocationFab);
            mBottomSheetFabBehavior = GonetteDisappearBehavior.from(mBottomSheetFab);
            mSearchBarBehavior = GonetteDisappearBehavior.from(mSearchBar);
            mSearchBarBehavior.setOnMoveListener(MapsActivity.this);
        }

        public void onSaveInstanceState(@NonNull Bundle outState) {
            outState.putInt(STATE_BOTTOM_SHEET_TYPE, mBottomSheetType);
        }

        // TODO put this in onCreate ?
        public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
            //noinspection WrongConstant
            mBottomSheetType = savedInstanceState.getInt(STATE_BOTTOM_SHEET_TYPE, BOTTOM_SHEET_NONE);

            if (mBottomSheetType == BOTTOM_SHEET_FILTERS) {
                mBottomSheet.setBackgroundColor(mFiltersBottomSheetBackgroundColor);
            } else if (mBottomSheetType == BOTTOM_SHEET_PARTNER) {
                mBottomSheetFab.setImageResource(R.drawable.ic_directions_white_24dp);
                mIsDirectionVisible = true;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mBottomSheetFab.setCompatElevation(mFABElevationPrimary);
                }
            }

            if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    updateBottomSheetPadding(mBottomSheet, mSearchBarVerticalMargin + mSearchBar.getHeight());
                }
            } else if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                // Workaround to prevent undefined fab state on configuration change.
                mBottomSheet.post(new Runnable() {
                    @Override
                    public void run() {
                        onSlide(mBottomSheet, 0);
                    }
                });
            }
        }

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (BottomSheetBehavior.STATE_HIDDEN == newState) {
                removeBottomSheetFragment();
                mBottomSheetType = BOTTOM_SHEET_NONE;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mBottomSheetFab.setCompatElevation(mFABElevationSecondary);
                }
                mBottomSheetFab.setTag(null);
                mBottomSheet.setBackgroundColor(mBottomSheetBackgroundColor);
                mBottomSheet.getLayoutParams().height = CoordinatorLayout.LayoutParams.MATCH_PARENT;
                mMyLocationFabBehavior.setLeaveLength(mMyLocationFab.getHeight());
                mMyLocationFabBehavior.setLeaveOffset(0);
                mMyLocationFabBehavior.setMoveOffset(0);
                mBottomSheetFabBehavior.setLeaveLength(mBottomSheetFab.getHeight());
                mBottomSheetFabBehavior.setLeaveOffset(0);
                mBottomSheetFabBehavior.setMoveLength(mBottomSheetFab.getHeight());
                mBottomSheetFabBehavior.setMoveOffset(0);
                mBottomSheetFabBehavior.setLeaveMethod(GonetteDisappearBehavior.LEAVE_METHOD_ALPHA);
                if (mShowPartnerAfterBottomSheetClose) {
                    mShowPartnerAfterBottomSheetClose = false;
                    showPartner(mSelectedPartnerId, mZoomForPartnerId);
                }
            } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                if (mSwitchPartnerAfterBottomSheetCollapsed) {
                    mSwitchPartnerAfterBottomSheetCollapsed = false;
                    switchPartner(mSelectedPartnerId);
                }
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            manageStatusBarPaddingOnBottomSheetSlide(bottomSheet);
            if (mBottomSheetType == BOTTOM_SHEET_PARTNER) {
                int translationY = mCoordinatorLayout.getBottom() - bottomSheet.getTop();
                manageBottomSheetFabOnPartnerSlide(translationY, slideOffset);
            }
        }

        private void manageBottomSheetFabOnPartnerSlide(int translationY, float slideOffset) {
            float scale = slideOffset * 4 - 1;
            scale = scale > 1
                    ? 1
                    : scale < 0
                    ? 0
                    : scale;
            scale = 1 - scale;
            scale = mInterpolator.getInterpolation(scale);
            mBottomSheetFab.setScaleX(scale);
            mBottomSheetFab.setScaleY(scale);

            int fabCenter = mBottomSheetFab.getHeight() / 2 + mFabBottomMargin;
            if (!mIsDirectionVisible && translationY > fabCenter) {
                mBottomSheetFab.setImageResource(R.drawable.ic_directions_white_24dp);
                mIsDirectionVisible = true;
            } else if (mIsDirectionVisible && translationY <= fabCenter) {
                mBottomSheetFab.setImageResource(R.drawable.ic_filter_list_white_24dp);
                mIsDirectionVisible = false;
            }
        }

        private void manageStatusBarPaddingOnBottomSheetSlide(@NonNull View bottomSheet) {
            int height = mSearchBar.getHeight();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                height += mSearchBarVerticalMargin;
            }
            int padding = mBottomSheet.getTop();
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
            if (mBottomSheetType == BOTTOM_SHEET_FILTERS) {
                mShowPartnerAfterBottomSheetClose = true;
                mSelectedPartnerId = partnerId;
                mZoomForPartnerId = zoom;
                closeBottomSheet();
            } else if (mBottomSheetType == BOTTOM_SHEET_PARTNER) {
                switchPartner(partnerId);
            } else {
                openPartner(partnerId, zoom);
            }
        }

        public void openPartner(final long partnerId, boolean zoom) {
            mMapsFragment.showPartner(partnerId, zoom, new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    mBottomSheetFragment = PartnerDetailFragment.newInstance(partnerId);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(
                                    R.id.bottom_sheet,
                                    mBottomSheetFragment,
                                    PartnerDetailFragment.TAG
                            )
                            .commit();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mBottomSheetFab.setCompatElevation(mFABElevationPrimary);
                    }
                    mBottomSheetFab.setTag(partnerId);
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    mBottomSheet.getLayoutParams().height = mCoordinatorLayout.getHeight() * 3 / 4;
                    int offset = mBottomSheetFab.getHeight() / 2 + ((CoordinatorLayout.LayoutParams) mBottomSheetFab.getLayoutParams()).bottomMargin;
                    mMyLocationFabBehavior.setLeaveLength(mMyLocationFab.getHeight());
                    mMyLocationFabBehavior.setLeaveOffset(offset);
                    mMyLocationFabBehavior.setMoveOffset(offset);
                    mBottomSheetFabBehavior.setLeaveLength(mBottomSheetFab.getHeight());
                    mBottomSheetFabBehavior.setLeaveOffset(mBottomSheetBehavior.getPeekHeight() + offset * 2);
                    mBottomSheetFabBehavior.setMoveLength(mBottomSheetFab.getHeight() + mBottomSheetBehavior.getPeekHeight() + offset * 2);
                    mBottomSheetFabBehavior.setMoveOffset(offset);
                    mBottomSheetFabBehavior.setLeaveMethod(GonetteDisappearBehavior.LEAVE_METHOD_SCALE);
                    mSearchBarBehavior.enable();
                    mSearchBarBehavior.setLeaveLength(mMyLocationFab.getHeight());
                    mSearchBarBehavior.setLeaveOffset(offset); // TODO improve: do this only one time.
                    mSearchBarBehavior.setMoveLength(mSearchBar.getBottom() - mStatusBarHeight);
                    mSearchBarBehavior.setMoveOffset(offset);
                    mBottomSheetType = BOTTOM_SHEET_PARTNER;
                }

                @Override
                public void onCancel() {

                }
            });
        }

        public void switchPartner(long partnerId) {
            if (BottomSheetBehavior.STATE_COLLAPSED != mBottomSheetBehavior.getState()) {
                mSwitchPartnerAfterBottomSheetCollapsed = true;
                mSelectedPartnerId = partnerId;
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            } else {
                long currentPartnerId = (long) mBottomSheetFab.getTag();
                if (currentPartnerId != partnerId) {
                    mBottomSheetFragment = PartnerDetailFragment.newInstance(partnerId);
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(
                                    android.R.anim.fade_in,
                                    android.R.anim.fade_out
                            )
                            .replace(
                                    R.id.bottom_sheet,
                                    mBottomSheetFragment,
                                    PartnerDetailFragment.TAG
                            )
                            .commit();
                    mBottomSheetFab.setTag(partnerId);
                }
                mMapsFragment.showPartner(partnerId, false, null);
                mBottomSheetType = BOTTOM_SHEET_PARTNER;
            }
        }

        public void showFilters() {
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
            mBottomSheetFragment = FiltersPresenter.newInstance(mSearchText.getText().toString());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.bottom_sheet,
                            mBottomSheetFragment,
                            FiltersFragment.TAG
                    )
                    .commit();
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            mBottomSheet.setBackgroundColor(mFiltersBottomSheetBackgroundColor);
            mSearchBarBehavior.disable();
            mBottomSheetType = BOTTOM_SHEET_FILTERS;
        }

        private void closeBottomSheet() {
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
                return (FiltersFragment) mBottomSheetFragment;
            } else {
                return null;
            }
        }
    }

}
