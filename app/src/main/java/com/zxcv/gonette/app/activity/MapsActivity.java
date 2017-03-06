package com.zxcv.gonette.app.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.zxcv.gonette.R;
import com.zxcv.gonette.app.fragment.MapsFragment;
import com.zxcv.gonette.app.fragment.PartnerDetailFragment;
import com.zxcv.gonette.app.ui.behavior.ParallaxBehavior;

public class MapsActivity
        extends AppCompatActivity
        implements MapsFragment.Callback,
        ParallaxBehavior.OnParallaxTranslationListener,
        View.OnClickListener {

    private static final String TAG = "MapsActivity";

    private MapsFragment mMapsFragment;

    private PartnerDetailFragment mBottomSheetFragment;

    private BottomSheetBehavior<View> mBottomSheetBehavior;

    private View mBottomSheet;

    private FloatingActionButton mMyLocationFAB;

    private FloatingActionButton mBottomSheetFAB;

    private View mCoordinatorLayout;

    private boolean mIsDirectionVisible = false;

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {

        private Interpolator mInterpolator = new DecelerateInterpolator();

        private boolean mIsAnchored = true;

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
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
            }
            else {
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
    };

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

        mMyLocationFAB.setOnClickListener(MapsActivity.this);
        mBottomSheetFAB.setOnClickListener(MapsActivity.this);

        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehavior.setPeekHeight(500);
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBottomSheetBehavior.setBottomSheetCallback(mBottomSheetCallback);

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
    public void showPartner(long partnerId) {
        mBottomSheetFragment = PartnerDetailFragment.newInstance(partnerId);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (BottomSheetBehavior.STATE_HIDDEN != mBottomSheetBehavior.getState()) {
            transaction.setCustomAnimations(
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
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void showFullMap() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        removeBottomSheetFragment();
    }

    private void removeBottomSheetFragment() {
        if (mBottomSheetFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(
                            android.R.anim.fade_in,
                            android.R.anim.fade_out
                    )
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
        }
    }

    private void anchorBottomSheetFab() {
        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) mBottomSheetFAB.getLayoutParams();
        p.setAnchorId(mBottomSheet.getId());
        mBottomSheetFAB.setLayoutParams(p);
    }

    private void detachBottomSheetFab() {
        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) mBottomSheetFAB.getLayoutParams();
        p.setAnchorId(View.NO_ID);
        mBottomSheetFAB.setLayoutParams(p);
    }
}
