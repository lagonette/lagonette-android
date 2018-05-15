package org.lagonette.app.app.widget.performer.impl;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import org.lagonette.app.R;
import org.lagonette.app.app.widget.performer.base.ViewPerformer;
import org.zxcv.functions.main.IntConsumer;
import org.zxcv.functions.main.IntSupplier;
import org.zxcv.functions.main.Runnable;

public class TutorialPerformer
		extends ViewPager.SimpleOnPageChangeListener
		implements ViewPerformer {

	public IntConsumer finish = IntConsumer::doNothing;

	public Runnable goToNextPage = Runnable::doNothing;

	public IntSupplier getCurrentPage = IntSupplier.get(0);

	public IntSupplier getPageCount = IntSupplier.get(0);

	private Button mNextButton;

	@Override
	public void inject(@NonNull View view) {
		mNextButton = view.findViewById(R.id.tutorial_button_next);

		mNextButton.setOnClickListener(
				button -> goToNextPage()
		);
	}

	// TODO Put this in performer
	private void updateNextButton() {
		if (getCurrentPage.get() == getPageCount.get() - 1) {
			mNextButton.setText(R.string.all_button_finish);
		}
		else {
			mNextButton.setText(R.string.all_button_next);
		}
	}

	public void goToNextPage() {
		if (getCurrentPage.get() == getPageCount.get() - 1) {
			finish.accept(Activity.RESULT_OK);
		}
		else {
			goToNextPage.run();
		}
	}

	@Override
	public void onPageSelected(int position) {
		updateNextButton();
	}
}
