package org.lagonette.app.app.widget.performer.impl;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import org.lagonette.app.BuildConfig;
import org.lagonette.app.R;
import org.lagonette.app.app.activity.PresenterActivity;
import org.zxcv.functions.main.BooleanSupplier;
import org.zxcv.functions.main.IntConsumer;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;

public abstract class ShowcasePerformer {

	public static final String SEQUENCE_ID_MAIN = BuildConfig.APPLICATION_ID + ".sequenceId:main";

	public static final String SEQUENCE_ID_DETAIL = BuildConfig.APPLICATION_ID + ".sequenceId:details";

	protected final int mSearchRadius;

	@ColorInt
	protected final int mMaskColor;

	protected final int mShapePadding;

	protected final int mFirstDelay;

	protected final int mDelay;

	@NonNull
	public BooleanSupplier isLocationDetailLoaded = BooleanSupplier::getFalse;

	@NonNull
	public BooleanSupplier checkForFineLocationPermission = BooleanSupplier::getFalse;

	public ShowcasePerformer(@NonNull PresenterActivity activity) {
		mSearchRadius = activity.getResources().getDimensionPixelSize(R.dimen.showcase_search_radius);

		mShapePadding = activity.getResources().getDimensionPixelSize(R.dimen.showcase_shape_padding);
		mMaskColor = ContextCompat.getColor(activity, R.color.showcase_mask);
		mFirstDelay = 1000;
		mDelay = 500;
	}

	public void startShowcaseIfNeeded(@NonNull PresenterActivity activity) {
		startMainSequenceIfNeeded(activity);
	}

	private void startMainSequenceIfNeeded(@NonNull PresenterActivity activity) {
		MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(activity, SEQUENCE_ID_MAIN);
		if (!sequence.hasFired()) {
			buildMainSequence(activity, sequence)
					.start();
		}
	}

	@NonNull
	protected abstract MaterialShowcaseSequence buildMainSequence(
			@NonNull PresenterActivity activity,
			@NonNull MaterialShowcaseSequence sequence);

	@NonNull
	public abstract IntConsumer appendBottomSheetListener(
			@NonNull PresenterActivity activity,
			@NonNull IntConsumer bottomSheetStateChanged);
}
