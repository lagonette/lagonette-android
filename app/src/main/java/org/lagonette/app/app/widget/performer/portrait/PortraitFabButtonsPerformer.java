package org.lagonette.app.app.widget.performer.portrait;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import org.lagonette.app.R;
import org.lagonette.app.app.widget.performer.impl.FabButtonsPerformer;

public class PortraitFabButtonsPerformer
		extends FabButtonsPerformer {

	public interface OnFiltersClickCommand {

		void notifyFiltersButtonClick();
	}

	@Nullable
	private OnFiltersClickCommand mOnFiltersClickCommand;

	@Override
	public void inject(@NonNull View view) {
		super.inject(view);

		FloatingActionButton filtersFab = view.findViewById(R.id.filters_fab);
		filtersFab.setOnClickListener(
				button -> {
					if (mOnFiltersClickCommand != null) {
						mOnFiltersClickCommand.notifyFiltersButtonClick();
					}
				}
		);
	}

	public void onFiltersClick(@Nullable OnFiltersClickCommand command) {
		mOnFiltersClickCommand = command;
	}
}
