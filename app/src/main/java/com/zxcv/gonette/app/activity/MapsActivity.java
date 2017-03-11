package com.zxcv.gonette.app.activity;

import android.animation.Animator;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.google.android.gms.maps.GoogleMap;
import com.zxcv.gonette.BuildConfig;
import com.zxcv.gonette.R;
import com.zxcv.gonette.app.fragment.FiltersFragment;
import com.zxcv.gonette.app.fragment.MapsFragment;
import com.zxcv.gonette.app.fragment.PartnerDetailFragment;
import com.zxcv.gonette.app.widget.behavior.ParallaxBehavior;
import com.zxcv.gonette.content.contract.GonetteContract;
import com.zxcv.gonette.database.GonetteDatabaseOpenHelper;
import com.zxcv.gonette.util.UiUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MapsActivity
        extends AppCompatActivity
        implements MapsFragment.Callback,
        FiltersFragment.Callback,
        ParallaxBehavior.OnParallaxTranslationListener,
        View.OnClickListener,
        View.OnLongClickListener {

    private static final String STATE_SEARCH = "state:search";

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

    private View mBottomSheet;

    private View mContentView;

    private FloatingActionButton mMyLocationFAB;

    private FloatingActionButton mBottomSheetFAB;

    private View mCoordinatorLayout;

    private boolean mIsDirectionVisible = false;

    private BottomSheetManager mBottomSheetManager;

    @NonNull
    private String mCurrentSearch = "";

    private int mStatusBarHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        if (savedInstanceState == null) {
            mMapsFragment = MapsFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content, mMapsFragment, MapsFragment.TAG)
                    .commit();
        } else {
            mMapsFragment = (MapsFragment) getSupportFragmentManager()
                    .findFragmentByTag(MapsFragment.TAG);
        }

        onViewCreated();

        mBottomSheetManager = new BottomSheetManager(MapsActivity.this, getResources());

        mMyLocationFAB.setOnClickListener(MapsActivity.this);
        mMyLocationFAB.setOnLongClickListener(MapsActivity.this);
        mBottomSheetFAB.setOnClickListener(MapsActivity.this);

        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehavior.setPeekHeight(500);
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBottomSheetBehavior.setBottomSheetCallback(mBottomSheetManager);

        mContentView = findViewById(R.id.content);

        mStatusBarHeight = UiUtil.getStatusBarHeight(getResources());

        if (BuildConfig.INSERT_DATA) {
            GonetteDatabaseOpenHelper.parseData(MapsActivity.this);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentSearch = savedInstanceState.getString(STATE_SEARCH, FiltersFragment.DEFAULT_SEARCH);
        mBottomSheetManager.onRestoreInstanceState(savedInstanceState);
    }

    private void onViewCreated() {
        mCoordinatorLayout = findViewById(R.id.coordinator_layout);
        mBottomSheet = findViewById(R.id.bottom_sheet);
        mMyLocationFAB = (FloatingActionButton) findViewById(R.id.my_location_fab);
        mBottomSheetFAB = (FloatingActionButton) findViewById(R.id.bottom_sheet_fab);
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
        mMapsFragment.processParallaxTranslation(translationY);
    }

    @Override
    public void hideMyLocationButton() {
        mMyLocationFAB.animate()
                .scaleX(0f)
                .scaleY(0f)
                .setDuration(300)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mMyLocationFAB.setVisibility(View.GONE);
                        mMyLocationFAB.animate().setListener(null);
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
        mMyLocationFAB.setVisibility(View.VISIBLE);
        mMyLocationFAB.animate()
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
        if (mIsDirectionVisible) {
            mMapsFragment.startDirection((long) mBottomSheetFAB.getTag());
        } else if (BottomSheetBehavior.STATE_HIDDEN == mBottomSheetBehavior.getState()) {
            mBottomSheetManager.showFilters();
        }
    }

    @Override
    public void showPartner(long partnerId, boolean zoom) {
        mBottomSheetManager.showPartner(partnerId, zoom);
    }

    @Override
    public void filterPartner(@NonNull String search) {
        mCurrentSearch = search;
        mMapsFragment.filterPartner(mCurrentSearch);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_SEARCH, mCurrentSearch);
        mBottomSheetManager.onSaveInstanceState(outState);
    }

    @Override
    public void expandFilters() {
        mBottomSheetManager.expandFilters();
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

        public BottomSheetManager(@NonNull Context context, @NonNull Resources resources) {
            mFABElevationPrimary = resources.getDimension(R.dimen.fab_elevation_primary);
            mFABElevationSecondary = resources.getDimension(R.dimen.fab_elevation_secondary);
            mFiltersBottomSheetBackgroundColor = ContextCompat.getColor(context, R.color.colorPrimary);
            mBottomSheetBackgroundColor = ContextCompat.getColor(context, android.R.color.background_light);
            mFabBottomMargin = ((CoordinatorLayout.LayoutParams) mBottomSheetFAB.getLayoutParams()).bottomMargin;
        }

        public void onSaveInstanceState(@NonNull Bundle outState) {
            outState.putInt(STATE_BOTTOM_SHEET_TYPE, mBottomSheetType);
        }

        public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
            //noinspection WrongConstant
            mBottomSheetType = savedInstanceState.getInt(STATE_BOTTOM_SHEET_TYPE, BOTTOM_SHEET_NONE);
        }

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (BottomSheetBehavior.STATE_HIDDEN == newState) {
                removeBottomSheetFragment();
                mBottomSheetType = BOTTOM_SHEET_NONE;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mBottomSheetFAB.setCompatElevation(mFABElevationSecondary);
                }
                mBottomSheetFAB.setTag(null);
                mBottomSheet.setBackgroundColor(mBottomSheetBackgroundColor);
                mBottomSheet.getLayoutParams().height = CoordinatorLayout.LayoutParams.MATCH_PARENT;
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
            int translationY = mCoordinatorLayout.getBottom() - bottomSheet.getTop();
            int maxTranslationY = mMyLocationFAB.getHeight() / 2 + mFabBottomMargin;
            manageStatusBarPaddingOnBottomSheetSlide(bottomSheet, slideOffset);
            if (mBottomSheetType == BOTTOM_SHEET_PARTNER) {
                manageBottomSheetFabOnPartnerSlide(translationY, slideOffset);
                manageMyLocationFabOnPartnerSlide(translationY, maxTranslationY);
            } else {
                manageFabOnSlide(mBottomSheetFAB, translationY, maxTranslationY);
                manageFabOnSlide(mMyLocationFAB, translationY, maxTranslationY);
            }
        }

        private void manageMyLocationFabOnPartnerSlide(int translationY, int maxTranslationY) {
            translationY -= mBottomSheetFAB.getHeight() / 2 + mFabBottomMargin;
            translationY = translationY > maxTranslationY
                    ? maxTranslationY
                    : translationY < 0
                    ? 0
                    : translationY;
            float scale = 1 - ((float) translationY) / ((float) maxTranslationY);
            mMyLocationFAB.setAlpha(scale);
            if (scale <= 0) {
                mMyLocationFAB.setTranslationY(0);
                // Workaround for old device, else it's like there is an invisible button preventing clicking behind.
                mMyLocationFAB.setTranslationX(mMyLocationFAB.getWidth() * 2);
            } else {
                mMyLocationFAB.setTranslationY(-translationY);
                // Workaround for old device, else it's like there is an invisible button preventing clicking behind.
                mMyLocationFAB.setTranslationX(0);
            }
        }

        private void manageFabOnSlide(@NonNull FloatingActionButton fab, int translationY, int maxTranslationY) {
            translationY = translationY > maxTranslationY
                    ? maxTranslationY
                    : translationY < 0
                    ? 0
                    : translationY;
            float scale = 1 - ((float) translationY) / ((float) maxTranslationY);
            fab.setAlpha(scale);
            if (scale <= 0) {
                fab.setTranslationY(0);
                // Workaround for old device, else it's like there is an invisible button preventing clicking behind.
                fab.setTranslationX(fab.getWidth() * 2);
            } else {
                fab.setTranslationY(-translationY);
                // Workaround for old device, else it's like there is an invisible button preventing clicking behind.
                fab.setTranslationX(0);
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
            mBottomSheetFAB.setScaleX(scale);
            mBottomSheetFAB.setScaleY(scale);

            // TODO optimize
            int fabCenter = mBottomSheetFAB.getHeight() / 2 + mFabBottomMargin;
            if (!mIsDirectionVisible && translationY > fabCenter) {
                mBottomSheetFAB.setImageResource(R.drawable.ic_directions_white_24dp);
                mIsDirectionVisible = true;
            } else if (mIsDirectionVisible && translationY <= fabCenter) {
                mBottomSheetFAB.setImageResource(R.drawable.ic_filter_list_white_24dp);
                mIsDirectionVisible = false;
            }
        }

        private void manageStatusBarPaddingOnBottomSheetSlide(@NonNull View bottomSheet, float slideOffset) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                    && bottomSheet.getLayoutParams().height == CoordinatorLayout.LayoutParams.MATCH_PARENT) {
                slideOffset = slideOffset * 5 - 4;
                slideOffset = slideOffset > 1
                        ? 1
                        : slideOffset < 0
                        ? 0
                        : slideOffset;
                bottomSheet.setPadding(
                        bottomSheet.getPaddingLeft(),
                        (int) (slideOffset * mStatusBarHeight),
                        mBottomSheet.getPaddingRight(),
                        mBottomSheet.getPaddingBottom()
                );
            }
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
                        mBottomSheetFAB.setCompatElevation(mFABElevationPrimary);
                    }
                    mBottomSheetFAB.setTag(partnerId);
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    mBottomSheet.getLayoutParams().height = mCoordinatorLayout.getHeight() * 3 / 4;
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
                long currentPartnerId = (long) mBottomSheetFAB.getTag();
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
                    mBottomSheetFAB.setTag(partnerId);
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
            mBottomSheetFragment = FiltersFragment.newInstance(mCurrentSearch);
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
            mBottomSheetType = BOTTOM_SHEET_FILTERS;
        }

        private void closeBottomSheet() {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }

        public boolean isBottomSheetOpen() {
            return mBottomSheetType != BOTTOM_SHEET_NONE;
        }

    }

}
