package org.lagonette.app.app.widget.performer.portrait;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.BuildConfig;
import org.lagonette.app.R;
import org.lagonette.app.app.activity.PresenterActivity;
import org.lagonette.app.app.widget.performer.impl.ShowcasePerformer;
import org.lagonette.app.tools.showcase.RoundRectangleShape;
import org.zxcv.functions.main.BooleanSupplier;
import org.zxcv.functions.main.IntConsumer;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.target.ViewTarget;

public class PortraitShowcasePerformer extends ShowcasePerformer {

	public static final String SEQUENCE_ID_FILTERS = BuildConfig.APPLICATION_ID + ".sequenceId:filters";

	private final int mBottomSheetShapePadding;

	private final int mBottomSheetXRadius;

	private final int mBottomSheetYRadius;

	@NonNull
	public BooleanSupplier isFiltersLoaded = BooleanSupplier::getFalse;

	public PortraitShowcasePerformer(@NonNull PresenterActivity activity) {
		super(activity);
		mBottomSheetXRadius = activity.getResources().getDimensionPixelSize(R.dimen.showcase_bottom_sheet_radius_x);
		mBottomSheetYRadius = activity.getResources().getDimensionPixelSize(R.dimen.showcase_bottom_sheet_radius_y);
		mBottomSheetShapePadding = activity.getResources().getDimensionPixelSize(R.dimen.showcase_bottom_sheet_shape_padding);
	}

	@NonNull
	@Override
	protected MaterialShowcaseSequence buildMainSequence(
			@NonNull PresenterActivity activity, @NonNull MaterialShowcaseSequence sequence) {
		return sequence
				.addSequenceItem(
						new MaterialShowcaseView.Builder(activity)
								.setTarget(activity.findViewById(R.id.search_bar))
								.setTitleText(R.string.showcase_search_title)
								.setContentText(R.string.showcase_search_content)
								.setDismissText(R.string.all_button_ok)
								.setMaskColour(mMaskColor)
								.setShapePadding(mShapePadding)
								.setShape(
										new RoundRectangleShape(
												new ViewTarget(activity.findViewById(R.id.search_bar)).getBounds(),
												mSearchRadius,
												mSearchRadius
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
								.setShapePadding(mShapePadding)
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
								.setShapePadding(mShapePadding)
								.setDelay(mDelay)
								.build()
				);
	}

	@NonNull
	@Override
	public IntConsumer appendBottomSheetListener(
			@NonNull PresenterActivity activity,
			@NonNull IntConsumer bottomSheetStateChanged) {
		MaterialShowcaseSequence filtersSequence = new MaterialShowcaseSequence(activity, SEQUENCE_ID_FILTERS);
		MaterialShowcaseSequence detailsSequence = new MaterialShowcaseSequence(activity, SEQUENCE_ID_DETAIL);
		if (filtersSequence.hasFired() && detailsSequence.hasFired()) {
			return bottomSheetStateChanged;
		}
		else {
			return state -> {
				if (state == BottomSheetBehavior.STATE_COLLAPSED) {
					if (isFiltersLoaded.get()) {
						startFiltersSequence(activity, filtersSequence);
					}
					else if (isLocationDetailLoaded.get()) {
						startDetailSequence(activity, detailsSequence);
					}
				}
				bottomSheetStateChanged.accept(state);
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
								.setShapePadding(mBottomSheetShapePadding)
								.setShape(
										new RoundRectangleShape(
												new ViewTarget(activity.findViewById(R.id.search_bar)).getBounds(),
												mBottomSheetXRadius,
												mBottomSheetYRadius
												)
								)
								.setTargetTouchable(true)
								.setDelay(mNoDelay)
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
								.setShapePadding(mBottomSheetShapePadding)
								.setShape(
										new RoundRectangleShape(
												new ViewTarget(activity.findViewById(R.id.search_bar)).getBounds(),
												mBottomSheetXRadius,
												mBottomSheetYRadius
										)
								)
								.setTargetTouchable(true)
								.setDelay(mNoDelay)
								.build()
				)
				.start();
	}
}
