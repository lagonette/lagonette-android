package com.zxcv.gonette.app.activity;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.zxcv.gonette.R;
import com.zxcv.gonette.app.fragment.MapsFragment;
import com.zxcv.gonette.app.ui.behavior.ParallaxBehavior;
import com.zxcv.gonette.app.ui.maps.PartnerItem;

public class MapsActivity
        extends AppCompatActivity
        implements MapsFragment.Callback,
                   ParallaxBehavior.OnParallaxTranslationListener,
                   View.OnLayoutChangeListener {

    private static final String TAG = "MapsActivity";

    private MapsFragment mMapsFragment;

    private BottomSheetBehavior<View> mBottomSheetBehavior;

    private View mBottomSheet;

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

        mBottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheet.findViewById(R.id.bottom_sheet_content)
                    .addOnLayoutChangeListener(MapsActivity.this);
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehavior.setPeekHeight(300);
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        View content = findViewById(R.id.content);
        ParallaxBehavior<View> parallaxBehavior = ParallaxBehavior.from(content);
        parallaxBehavior.setOnParallaxTranslationListener(MapsActivity.this);
    }

    @Override
    public void onParallaxTranslation(float translationY) {
        mMapsFragment.processParallaxTranslation(translationY);
    }

    @Override
    public void showPartner(PartnerItem partnerItem) {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        TextView name = (TextView) findViewById(R.id.name);
        TextView description = (TextView) findViewById(R.id.description);
        name.setText(partnerItem.getTitle());
        description.setText(partnerItem.getSnippet());
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void showFullMap() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Override
    public void onLayoutChange(
            View v,
            int left,
            int top,
            int right,
            int bottom,
            int oldLeft,
            int oldTop,
            int oldRight,
            int oldBottom) {
        Log.d(TAG, "onLayoutChange: " + top + ":" + bottom);
        mBottomSheetBehavior.setPeekHeight(bottom - top);
    }
}
