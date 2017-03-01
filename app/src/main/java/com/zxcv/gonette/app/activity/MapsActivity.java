package com.zxcv.gonette.app.activity;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zxcv.gonette.R;
import com.zxcv.gonette.app.fragment.MapsFragment;
import com.zxcv.gonette.app.fragment.PartnerDetailFragment;
import com.zxcv.gonette.app.ui.behavior.ParallaxBehavior;

public class MapsActivity
        extends AppCompatActivity
        implements MapsFragment.Callback,
                   ParallaxBehavior.OnParallaxTranslationListener,
                   View.OnClickListener {

    private MapsFragment mMapsFragment;

    private PartnerDetailFragment mBottomSheetFragment;

    private BottomSheetBehavior<View> mBottomSheetBehavior;

    private View mBottomSheet;

    private FloatingActionButton mMyLocationFAB;

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

        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehavior.setPeekHeight(500);
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        View content = findViewById(R.id.content);
        ParallaxBehavior<View> parallaxBehavior = ParallaxBehavior.from(content);
        parallaxBehavior.setOnParallaxTranslationListener(MapsActivity.this);
    }

    private void onViewCreated() {
        mBottomSheet = findViewById(R.id.bottom_sheet);
        mMyLocationFAB = (FloatingActionButton) findViewById(R.id.my_location_fab);
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
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
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
    }

    @Override
    public void showFullMap() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        getSupportFragmentManager().beginTransaction()
                                   .setCustomAnimations(
                                           android.R.anim.fade_in,
                                           android.R.anim.fade_out
                                   )
                                   .remove(mBottomSheetFragment)
                                   .commit();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.my_location_fab:
                onMyLocationFabClick();
                break;
            default:
                throw new IllegalArgumentException("Unknown view id: " + id);
        }
    }

    private void onMyLocationFabClick() {
        mMapsFragment.moveOnMyLocation();
    }
}
