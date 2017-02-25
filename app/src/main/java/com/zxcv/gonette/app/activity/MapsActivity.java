package com.zxcv.gonette.app.activity;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zxcv.gonette.R;
import com.zxcv.gonette.app.fragment.MapsFragment;
import com.zxcv.gonette.app.ui.behavior.ParallaxBehavior;

public class MapsActivity
        extends AppCompatActivity
        implements ParallaxBehavior.OnParallaxTranslationListener {

    private static final String TAG = "MapsActivity";

    private MapsFragment mMapsFragment;

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

        View bottomSheet = findViewById(R.id.bottom_sheet);
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(300);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setHideable(true);

        View content = findViewById(R.id.content);
        ParallaxBehavior<View> parallaxBehavior = ParallaxBehavior.from(content);
        parallaxBehavior.setOnParallaxTranslationListener(MapsActivity.this);
    }

    @Override
    public void onParallaxTranslation(float translationY) {
        mMapsFragment.processParallaxTranslation(translationY);
    }
}
