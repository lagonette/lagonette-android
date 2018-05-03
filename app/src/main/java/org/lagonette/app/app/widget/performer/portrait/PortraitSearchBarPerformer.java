package org.lagonette.app.app.widget.performer.portrait;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import org.lagonette.app.app.widget.behavior.TopEscapeBehavior;
import org.lagonette.app.app.widget.performer.impl.SearchBarPerformer;

public class PortraitSearchBarPerformer
		extends SearchBarPerformer {

	public interface OnBehaviorEnabledCommand {

		void notifyBehaviorEnabled(boolean enable);
	}

	@Nullable
	private OnBehaviorEnabledCommand mOnBehaviorEnabledCommand;

	@Nullable
	private TopEscapeBehavior mBehavior;

	public PortraitSearchBarPerformer(
			@IdRes int searchBarRes,
			@IdRes int progressBarRes,
			@IdRes int searchTextRes,
			@IdRes int clearButtonRes) {
		super(searchBarRes, progressBarRes, searchTextRes, clearButtonRes);
	}

	@Override
	public void inject(@NonNull View view) {
		super.inject(view);

		mBehavior = TopEscapeBehavior.from(mSearchBar);
		mBehavior.setOnMoveListener(
				(child, translationY) -> onBottomChanged(child)
		);
	}

	public void onBehaviorEnabled(@Nullable OnBehaviorEnabledCommand command) {
		mOnBehaviorEnabledCommand = command;
	}

	public void enableBehavior(boolean enable) {
		if (mBehavior != null) {
			if (enable) {
				mBehavior.enable();
			}
			else {
				mBehavior.disable();
			}

			if (mOnBehaviorEnabledCommand != null) {
				mOnBehaviorEnabledCommand.notifyBehaviorEnabled(enable);
			}
		}
	}
}
