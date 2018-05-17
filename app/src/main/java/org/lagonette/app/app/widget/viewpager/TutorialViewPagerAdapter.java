package org.lagonette.app.app.widget.viewpager;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import org.lagonette.app.R;
import org.zxcv.functions.main.BooleanSupplier;
import org.zxcv.functions.main.Runnable;

public class TutorialViewPagerAdapter
		extends AbstractViewPagerAdapter {

	public static class PermissionsPageHolder
			extends PageHolder {

		@NonNull
		public final Button allowButton;

		@NonNull
		public final ImageView checkedImage;

		public PermissionsPageHolder(@NonNull ViewGroup parent) {
			super(parent, R.layout.page_tutorial_permissions);
			allowButton = itemView.findViewById(R.id.tutorial_button_allow);
			checkedImage = itemView.findViewById(R.id.tutorial_image_checked);
		}
	}

	public static class Callbacks {

		@NonNull
		public Runnable goToNextPage = Runnable::doNothing;

		@NonNull
		public Runnable finish = Runnable::doNothing;

		public Runnable askForFineLocation = Runnable::doNothing;

		public BooleanSupplier checkForFineLocation = BooleanSupplier.get(false);
	}


	@NonNull
	public final Callbacks callbacks;

	public TutorialViewPagerAdapter() {
		callbacks = new Callbacks();
	}

	@Override
	protected void bind(@NonNull PageHolder pageHolder, int position) {
		if (position == TutorialPage.PERMISSIONS.ordinal()) {
			PermissionsPageHolder holder = (PermissionsPageHolder) pageHolder;
			if (callbacks.checkForFineLocation.get()) {
				holder.allowButton.setVisibility(View.GONE);
				holder.checkedImage.setVisibility(View.VISIBLE);
			}
			else {
				holder.allowButton.setVisibility(View.VISIBLE);
				holder.checkedImage.setVisibility(View.GONE);
				holder.allowButton.setOnClickListener(
						button -> callbacks.askForFineLocation.run()
				);
			}
		}
//		else if (position == TutorialPage.REPORT.ordinal()) {
//			view.findViewById(R.id.tutorial_button_no).setOnClickListener(
//					button -> callbacks.goToNextPage.run()
//			);
//		}
	}

	@NonNull
	@Override
	protected PageHolder create(
			@NonNull ViewGroup parent, int position) {
//		if (position == TutorialPage.PERMISSIONS.ordinal()) {
			return new PermissionsPageHolder(parent);
//		}
//		else if (position == TutorialPage.REPORT.ordinal()) {
//		}
	}

	@Override
	public int getCount() {
		return TutorialPage.values().length;
	}

	public void onFineLocationPermissionResult(boolean granted) {
		notifyDataSetChanged();
		callbacks.goToNextPage.run();
	}

	public enum TutorialPage {
		PERMISSIONS(),
		//REPORT(),
	}
}
