package org.lagonette.app.app.widget.viewpager;

import android.support.annotation.LayoutRes;
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

	public static class IntroPageHolder
			extends PageHolder {

		public IntroPageHolder(@NonNull ViewGroup parent, @LayoutRes int layoutRes) {
			super(parent, layoutRes);
		}
	}

	public static class PermissionsPageHolder
			extends PageHolder {

		@NonNull
		public final Button allowButton;

		@NonNull
		public final ImageView checkedImage;

		public PermissionsPageHolder(@NonNull ViewGroup parent, @LayoutRes int layoutRes) {
			super(parent, layoutRes);
			allowButton = itemView.findViewById(R.id.tutorial_button_allow);
			checkedImage = itemView.findViewById(R.id.tutorial_image_checked);
		}
	}

	public static class EndPageHolder
			extends PageHolder {

		public EndPageHolder(@NonNull ViewGroup parent, @LayoutRes int layoutRes) {
			super(parent, layoutRes);
		}
	}

	public static class Callbacks {

		@NonNull
		public Runnable goToNextPage = Runnable::doNothing;

		@NonNull
		public Runnable finish = Runnable::doNothing;

		public Runnable askForFineLocation = Runnable::doNothing;

		public BooleanSupplier checkForFineLocation = BooleanSupplier.create(false);
	}


	@NonNull
	private Callbacks mCallbacks;

	//TODO After tutorial -> Update location UI

	public TutorialViewPagerAdapter(@NonNull Callbacks callbacks) {
		mCallbacks = callbacks;
	}

	@Override
	protected void bind(@NonNull PageHolder pageHolder, int position) {
		if (position == TutorialPage.INTRO.ordinal()) {
			IntroPageHolder holder = (IntroPageHolder) pageHolder;
		}
		else if (position == TutorialPage.PERMISSIONS.ordinal()) {
			PermissionsPageHolder holder = (PermissionsPageHolder) pageHolder;
			if (mCallbacks.checkForFineLocation.get()) {
				holder.allowButton.setVisibility(View.GONE);
				holder.checkedImage.setVisibility(View.VISIBLE);
			}
			else {
				holder.allowButton.setVisibility(View.VISIBLE);
				holder.checkedImage.setVisibility(View.GONE);
				holder.allowButton.setOnClickListener(
						button -> mCallbacks.askForFineLocation.run()
				);
			}
		}
//		else if (position == TutorialPage.REPORT.ordinal()) {
//			view.findViewById(R.id.tutorial_button_no).setOnClickListener(
//					button -> mCallbacks.goToNextPage.run()
//			);
//		}
		else if (position == TutorialPage.END.ordinal()) {
			EndPageHolder holder = (EndPageHolder) pageHolder;
		}
	}

	@NonNull
	@Override
	protected PageHolder create(
			@NonNull ViewGroup container, int position) {
		if (position == TutorialPage.INTRO.ordinal()) {
			return new IntroPageHolder(container, R.layout.page_tutorial_intro);
		}
		else if (position == TutorialPage.PERMISSIONS.ordinal()) {
			return new PermissionsPageHolder(container, R.layout.page_tutorial_permissions);
		}
//		else if (position == TutorialPage.REPORT.ordinal()) {
//		}
		else /*if (position == TutorialPage.END.ordinal())*/ {
			return new EndPageHolder(container, R.layout.page_tutorial_end);
		}
	}

	@Override
	public int getCount() {
		return TutorialPage.values().length;
	}

	public void onFineLocationPermissionResult(boolean granted) {
		notifyDataSetChanged();
		mCallbacks.goToNextPage.run();
	}

	public enum TutorialPage {
		INTRO(),
		PERMISSIONS(),
		//		REPORT(R.layout.page_tutorial_report),
		END();
	}
}
