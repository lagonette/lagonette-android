package com.zxcv.gonette.app.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.zxcv.gonette.R;
import com.zxcv.gonette.app.fragment.MapsFragment;
import com.zxcv.gonette.app.fragment.PartnerDetailFragment;
import com.zxcv.gonette.app.ui.behavior.ParallaxBehavior;
import com.zxcv.gonette.content.contract.GonetteContract;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MapsActivity
        extends AppCompatActivity
        implements MapsFragment.Callback,
        ParallaxBehavior.OnParallaxTranslationListener,
        View.OnClickListener {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({BOTTOM_SHEET_NONE, BOTTOM_SHEET_PARTNER, BOTTOM_SHEET_FILTERS})
    public @interface BottomSheetType {
    }

    public static final int BOTTOM_SHEET_NONE = 0;

    public static final int BOTTOM_SHEET_PARTNER = 1;

    public static final int BOTTOM_SHEET_FILTERS = 2;

    private static final String TAG = "MapsActivity";

    private MapsFragment mMapsFragment;

    private PartnerDetailFragment mBottomSheetFragment;

    private BottomSheetBehavior<View> mBottomSheetBehavior;

    private View mBottomSheet;

    private FloatingActionButton mMyLocationFAB;

    private FloatingActionButton mBottomSheetFAB;

    private View mCoordinatorLayout;

    private boolean mIsDirectionVisible = false;

    private BottomSheetManager mBottomSheetManager;

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

        mBottomSheetManager = new BottomSheetManager(getResources());

        mMyLocationFAB.setOnClickListener(MapsActivity.this);
        mBottomSheetFAB.setOnClickListener(MapsActivity.this);

        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehavior.setPeekHeight(500);
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBottomSheetBehavior.setBottomSheetCallback(mBottomSheetManager);

        View content = findViewById(R.id.content);
        ParallaxBehavior<View> parallaxBehavior = ParallaxBehavior.from(content);
        parallaxBehavior.setOnParallaxTranslationListener(MapsActivity.this);
    }

    private void onViewCreated() {
        mCoordinatorLayout = findViewById(R.id.coordinator_layout);
        mBottomSheet = findViewById(R.id.bottom_sheet);
        mMyLocationFAB = (FloatingActionButton) findViewById(R.id.my_location_fab);
        mBottomSheetFAB = (FloatingActionButton) findViewById(R.id.bottom_sheet_fab);
    }

    @Override
    public void onParallaxTranslation(float translationY) {
        mMapsFragment.processParallaxTranslation(translationY);
    }

    @Override
    public void hideMyLocationButton() {
        mMyLocationFAB.setVisibility(View.GONE);
    }

    @Override
    public void showMyLocationButton() {
        mMyLocationFAB.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFullMap() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
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

    private void onMyLocationFabClick() {
        mMapsFragment.moveOnMyLocation();
    }

    private void onBottomSheetFabClick() {
        if (mIsDirectionVisible) {
            mMapsFragment.startDirection();
        } else if (BottomSheetBehavior.STATE_HIDDEN == mBottomSheetBehavior.getState()) {
            mBottomSheetManager.showFilters();
        }
    }

    @Override
    public void showPartner(long partnerId) {
        mBottomSheetManager.showPartner(partnerId);
    }

    public class BottomSheetManager extends BottomSheetBehavior.BottomSheetCallback {

        @BottomSheetType
        private int mBottomSheetType = BOTTOM_SHEET_NONE;

        private Interpolator mInterpolator = new DecelerateInterpolator();

        private boolean mIsAnchored = true;

        private boolean mShowPartnerAfterBottomSheetClose = false;

        private long mPartnerToShowAfterBottomSheetClose = GonetteContract.NO_ID;

        private final float mFABElevationPrimary;

        private final float mFABElevationSecondary;

        public BottomSheetManager(@NonNull Resources resources) {
            mFABElevationPrimary = resources.getDimension(R.dimen.fab_elevation_primary);
            mFABElevationSecondary = resources.getDimension(R.dimen.fab_elevation_secondary);
        }

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (BottomSheetBehavior.STATE_HIDDEN == newState) {
                removeBottomSheetFragment();
                mBottomSheetType = BOTTOM_SHEET_NONE;
                mBottomSheetFAB.setCompatElevation(mFABElevationPrimary);
                if (mShowPartnerAfterBottomSheetClose) {
                    mShowPartnerAfterBottomSheetClose = false;
                    showPartner(mPartnerToShowAfterBottomSheetClose);
                }
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            if (mBottomSheetType == BOTTOM_SHEET_PARTNER) {
                onPartnerSlide(bottomSheet, slideOffset);
            }
        }

        public void showPartner(long partnerId) {
            if (mBottomSheetType == BOTTOM_SHEET_FILTERS) {
                mShowPartnerAfterBottomSheetClose = true;
                mPartnerToShowAfterBottomSheetClose = partnerId;
                hideBottomSheet();
            } else if (mBottomSheetType == BOTTOM_SHEET_PARTNER) {
                switchPartner(partnerId);
            } else {
                openPartner(partnerId);
            }
        }

        public void openPartner(long partnerId) {
            mBottomSheetFragment = PartnerDetailFragment.newInstance(partnerId);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.bottom_sheet,
                            mBottomSheetFragment,
                            PartnerDetailFragment.TAG
                    )
                    .commit();
            anchorBottomSheetFab();
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            mBottomSheetType = BOTTOM_SHEET_PARTNER;
        }

        public void switchPartner(long partnerId) {
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
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            mBottomSheetType = BOTTOM_SHEET_PARTNER;
        }

        public void showFilters() {
            if (mBottomSheetType == BOTTOM_SHEET_NONE) {
                openFilters();
            }
        }

        private void openFilters() {
            detachBottomSheetFab();
            mBottomSheetFAB.setCompatElevation(mFABElevationSecondary);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            mBottomSheetType = BOTTOM_SHEET_FILTERS;
        }

        private void hideBottomSheet() {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }

        private void onPartnerSlide(@NonNull View bottomSheet, float slideOffset) {
            int translationY = mCoordinatorLayout.getBottom() - bottomSheet.getTop();

            float scale = slideOffset * 4 - 1;
            scale = scale > 1
                    ? 1
                    : scale < 0
                    ? 0
                    : scale;
            scale = 1 - scale;
            scale = mInterpolator.getInterpolation(scale);
            if (scale <= 0) {
                if (mIsAnchored) {
                    detachBottomSheetFab();
                    mIsAnchored = false;
                }
            } else {
                if (!mIsAnchored) {
                    anchorBottomSheetFab();
                    mIsAnchored = true;
                }
            }
            mBottomSheetFAB.setScaleX(scale);
            mBottomSheetFAB.setScaleY(scale);

            // TODO optimize
            int fabCenter = mBottomSheetFAB.getHeight() / 2 + ((CoordinatorLayout.LayoutParams) mBottomSheetFAB.getLayoutParams()).bottomMargin;
            if (!mIsDirectionVisible && translationY > fabCenter) {
                mBottomSheetFAB.setImageResource(R.drawable.ic_directions_white_24dp);
                mIsDirectionVisible = true;
            } else if (mIsDirectionVisible && translationY <= fabCenter) {
                mBottomSheetFAB.setImageResource(R.drawable.ic_filter_list_white_24dp);
                mIsDirectionVisible = false;
            }
        }

        private void anchorBottomSheetFab() {
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) mBottomSheetFAB.getLayoutParams();
            p.setAnchorId(R.id.bottom_sheet);
            p.anchorGravity = Gravity.TOP | GravityCompat.END;
            mBottomSheetFAB.setLayoutParams(p);
        }

        private void detachBottomSheetFab() {
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) mBottomSheetFAB.getLayoutParams();
            p.setAnchorId(R.id.content);
            p.anchorGravity = Gravity.BOTTOM | GravityCompat.END;
            mBottomSheetFAB.setLayoutParams(p);
        }

    }

}
