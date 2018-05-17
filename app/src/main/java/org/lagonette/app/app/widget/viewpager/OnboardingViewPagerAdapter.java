package org.lagonette.app.app.widget.viewpager;

import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import org.lagonette.app.R;
import org.zxcv.functions.main.BooleanSupplier;
import org.zxcv.functions.main.Runnable;

public class OnboardingViewPagerAdapter
		extends AbstractViewPagerAdapter {

	public static int PAGE_COUNT = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? 2 : 1;

	public static int PAGE_POSITION_PERMISSION = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? 0 : -1;

	public static int PAGE_POSITION_REPORT = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? 1 : 0;

	public static class PermissionsPageHolder
			extends PageHolder {

		@NonNull
		public final Button allowButton;

		@NonNull
		public final ImageView checkedImage;

		public PermissionsPageHolder(@NonNull ViewGroup parent) {
			super(parent, R.layout.page_onboarding_permissions);
			allowButton = itemView.findViewById(R.id.onboarding_button_allow);
			checkedImage = itemView.findViewById(R.id.onboarding_image_checked);
		}
	}

	public static class ReportPageHolder
			extends PageHolder {

		@NonNull
		public final Button allowButton;

		@NonNull
		public final ImageView checkedImage;

		public ReportPageHolder(@NonNull ViewGroup parent) {
			super(parent, R.layout.page_onboarding_report);
			allowButton = itemView.findViewById(R.id.onboarding_button_allow);
			checkedImage = itemView.findViewById(R.id.onboarding_image_checked);
		}
	}

	public static class Callbacks {

		@NonNull
		public Runnable goToNextPage = Runnable::doNothing;

		@NonNull
		public Runnable finish = Runnable::doNothing;

		public Runnable askForFineLocation = Runnable::doNothing;

		public BooleanSupplier checkForFineLocation = BooleanSupplier.get(false);

		public Runnable enableCrashlytics = Runnable::doNothing;

		public BooleanSupplier isCrashlyticsEnabled = BooleanSupplier.get(false);
	}


	@NonNull
	public final Callbacks callbacks;

	public OnboardingViewPagerAdapter() {
		callbacks = new Callbacks();
	}

	@Override
	protected void bind(@NonNull PageHolder pageHolder, int position) {
		if (position == PAGE_POSITION_PERMISSION) {
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
		else if (position == PAGE_POSITION_REPORT) {
			ReportPageHolder holder = (ReportPageHolder) pageHolder;
			if (callbacks.isCrashlyticsEnabled.get()) {
				holder.allowButton.setVisibility(View.GONE);
				holder.checkedImage.setVisibility(View.VISIBLE);
			}
			else {
				holder.allowButton.setVisibility(View.VISIBLE);
				holder.checkedImage.setVisibility(View.GONE);
				holder.allowButton.setOnClickListener(
						button -> enableCrashlytics()
				);
			}
		}
	}

	@NonNull
	@Override
	protected PageHolder create(
			@NonNull ViewGroup parent, int position) {
		if (position == PAGE_POSITION_PERMISSION) {
			return new PermissionsPageHolder(parent);
		}
		else if (position == PAGE_POSITION_REPORT) {
			return new ReportPageHolder(parent);
		}
		else {
			throw new IllegalArgumentException("The position " + position + " is wrong.");
		}
	}

	@Override
	public int getCount() {
		return PAGE_COUNT;
	}

	public void onFineLocationPermissionResult(boolean granted) {
		notifyDataSetChanged();
		callbacks.goToNextPage.run();
	}

	private void enableCrashlytics() {
		callbacks.enableCrashlytics.run();
		notifyDataSetChanged();
		callbacks.goToNextPage.run();

	}

}
