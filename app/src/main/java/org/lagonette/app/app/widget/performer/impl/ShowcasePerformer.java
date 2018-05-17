package org.lagonette.app.app.widget.performer.impl;

import android.os.SystemClock;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;

import org.lagonette.app.BuildConfig;
import org.lagonette.app.R;
import org.lagonette.app.app.activity.PresenterActivity;
import org.lagonette.app.tools.showcase.RoundRectangleShape;
import org.zxcv.functions.main.BooleanSupplier;
import org.zxcv.functions.main.IntConsumer;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.target.ViewTarget;

public class ShowcasePerformer {

	public static final String SEQUENCE_ID_MAIN = BuildConfig.APPLICATION_ID + ".sequenceId:main";

	public static final String SEQUENCE_ID_FILTERS = BuildConfig.APPLICATION_ID + ".sequenceId:filters";

	public static final String SEQUENCE_ID_DETAIL = BuildConfig.APPLICATION_ID + ".sequenceId:detaile";

	private final int mRadius;

	@ColorInt
	private final int mMaskColor;

	private final int mFirstDelay;

	private final int mDelay;

	@NonNull
	public BooleanSupplier checkForFineLocationPermission = BooleanSupplier::getFalse;

	@NonNull
	public BooleanSupplier isLocationDetailLoaded = BooleanSupplier::getFalse;

	@NonNull
	public BooleanSupplier isFiltersLoaded = BooleanSupplier::getFalse;

	public ShowcasePerformer(@NonNull PresenterActivity activity) {
		mRadius = activity.getResources().getDimensionPixelSize(R.dimen.showcase_search_radius);
		mMaskColor = ContextCompat.getColor(activity, R.color.showcase_mask);
		mFirstDelay = 1500;
		mDelay = 500;
	}

	public void startShowcaseIfNeeded(@NonNull PresenterActivity activity) {
		startMainSequenceIfNeeded(activity);
	}

	private void startMainSequenceIfNeeded(@NonNull PresenterActivity activity) {
		MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(activity, SEQUENCE_ID_MAIN);
		if (!sequence.hasFired()) {
			sequence
					.addSequenceItem(
							new MaterialShowcaseView.Builder(activity)
									.setTarget(activity.findViewById(R.id.search_bar))
									.setTitleText(R.string.showcase_search_title)
									.setContentText(R.string.showcase_search_content)
									.setDismissText(R.string.all_button_ok)
									.setMaskColour(mMaskColor)
									.setShape(
											new RoundRectangleShape(
													new ViewTarget(activity.findViewById(R.id.search_bar)).getBounds(),
													mRadius,
													mRadius
											)
									)
									.setDelay(mFirstDelay)
									.build()
					)
					.addSequenceItem(
							new MaterialShowcaseView.Builder(activity)
									.setTarget(activity.findViewById(R.id.my_location_fab))
									.setTitleText(R.string.showcase_location_title)
									.setContentText(R.string.showcase_location_content)
									.setDismissText(R.string.all_button_ok)
									.setTargetTouchable(checkForFineLocationPermission.get())
									.setMaskColour(mMaskColor)
									.setDelay(mDelay)
									.build()
					)
					.addSequenceItem(
							new MaterialShowcaseView.Builder(activity)
									.setTarget(activity.findViewById(R.id.filters_fab))
									.setTitleText(R.string.showcase_filters_fab_title)
									.setContentText(R.string.showcase_filters_fab_content)
									.setDismissText(R.string.all_button_ok)
									.setTargetTouchable(true)
									.setMaskColour(mMaskColor)
									.setDelay(mDelay)
									.build()
					)
					.start();
		}
	}

	public IntConsumer appendBottomSheetListener(
			@NonNull PresenterActivity activity,
			@NonNull IntConsumer intConsumer) {
		MaterialShowcaseSequence filtersSequence = new MaterialShowcaseSequence(activity, SEQUENCE_ID_FILTERS);
		MaterialShowcaseSequence detailsSequence = new MaterialShowcaseSequence(activity, SEQUENCE_ID_DETAIL);
		if (filtersSequence.hasFired() && detailsSequence.hasFired()) {
			return intConsumer;
		}
		else {
			return state -> {
				if (state == BottomSheetBehavior.STATE_COLLAPSED) {
					if (isFiltersLoaded.get()) {
						startFiltersSequence(activity, filtersSequence);
					}
					else if (isFiltersLoaded.get()) {
						startDetailSequence(activity, detailsSequence);
					}
				}
				intConsumer.accept(state);
			};
		}
	}

	private void startFiltersSequence(
			@NonNull PresenterActivity activity,
			@NonNull MaterialShowcaseSequence sequence) {
		sequence
				.addSequenceItem(
						new MaterialShowcaseView.Builder(activity)
								.setTarget(activity.findViewById(R.id.bottom_sheet))
								.setTitleText(R.string.showcase_filters_title)
								.setContentText(R.string.showcase_filters_content)
								.setDismissText(R.string.all_button_ok)
								.setMaskColour(mMaskColor)
								.setTargetTouchable(true)
								.withRectangleShape()
								.setDelay(mDelay)
								.build()
				)
				.start();
	}

	private void startDetailSequence(
			@NonNull PresenterActivity activity,
			@NonNull MaterialShowcaseSequence sequence) {
		sequence
				.addSequenceItem(
						new MaterialShowcaseView.Builder(activity)
								.setTarget(activity.findViewById(R.id.bottom_sheet))
								.setTitleText(R.string.showcase_details_title)
								.setContentText(R.string.showcase_details_content)
								.setDismissText(R.string.all_button_ok)
								.setMaskColour(mMaskColor)
								.setTargetTouchable(true)
								.withRectangleShape()
								.setDelay(mDelay)
								.build()
				)
				.start();
	}
}
