package org.lagonette.app.app.widget.performer.landscape;

import android.support.annotation.NonNull;

import org.lagonette.app.R;
import org.lagonette.app.app.activity.PresenterActivity;
import org.lagonette.app.app.widget.performer.impl.ShowcasePerformer;
import org.lagonette.app.tools.showcase.RoundRectangleShape;
import org.zxcv.functions.main.IntConsumer;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.target.ViewTarget;

public class LandscapeShowcasePerformer
		extends ShowcasePerformer {

	public LandscapeShowcasePerformer(@NonNull PresenterActivity activity) {
		super(activity);
	}

	@NonNull
	@Override
	protected MaterialShowcaseSequence buildMainSequence(
			@NonNull PresenterActivity activity,
			@NonNull MaterialShowcaseSequence sequence) {
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
				);
	}

	@NonNull
	@Override
	public IntConsumer appendBottomSheetListener(
			@NonNull PresenterActivity activity, @NonNull IntConsumer bottomSheetStateChanged) {
		return bottomSheetStateChanged;
	}
}
