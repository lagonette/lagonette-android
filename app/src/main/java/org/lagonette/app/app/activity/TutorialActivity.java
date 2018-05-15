package org.lagonette.app.app.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.lagonette.app.R;
import org.lagonette.app.app.widget.performer.impl.PermissionsPerformer;
import org.lagonette.app.app.widget.viewpager.TutorialViewPagerAdapter;

public class TutorialActivity
		extends BaseActivity {

	private ViewPager mViewPager;

	private DotsIndicator mDotsIndicator;

	private TutorialViewPagerAdapter mViewPagerAdapter;

	private PermissionsPerformer mPermissionsPerformer;

	@Override
	protected void construct() {
		mPermissionsPerformer = new PermissionsPerformer(this);
	}

	@Override
	protected int getContentView() {
		return R.layout.activity_tutorial;
	}

	@Override
	protected void inject(@NonNull View view) {
		mViewPager = view.findViewById(R.id.tutorial_pager);
		mDotsIndicator = view.findViewById(R.id.tutorial_dots);
	}

	@Override
	protected void init() {

	}

	@Override
	protected void restore(@NonNull Bundle savedInstanceState) {

	}

	@Override
	protected void onConstructed() {

		TutorialViewPagerAdapter.Callbacks callbacks = new TutorialViewPagerAdapter.Callbacks();
		callbacks.finish = this::finish;
		callbacks.goToNextPage = this::goToNextPage;
		callbacks.askForFineLocation = mPermissionsPerformer::askForFineLocation;
		callbacks.checkForFineLocation = mPermissionsPerformer::checkForFineLocation;

		mViewPagerAdapter = new TutorialViewPagerAdapter(callbacks);
		mViewPager.setAdapter(mViewPagerAdapter);
		mDotsIndicator.setViewPager(mViewPager);

		mPermissionsPerformer.onFineLocationPermissionResult = mViewPagerAdapter::onFineLocationPermissionResult;

		mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				updateNextButton();
			}
		});

		// TODO Put this in performer
		findViewById(R.id.tutorial_button_next).setOnClickListener(
				view -> goToNextPage()
		);
	}

	private void goToNextPage() {
		PagerAdapter adapter = mViewPager.getAdapter();
		if (adapter != null) {
			if (mViewPager.getCurrentItem() == adapter.getCount() - 1) {
				finish();
			}
			else {
				mViewPager.arrowScroll(View.FOCUS_RIGHT);
			}
		}
	}

	// TODO Put this in performer
	private void updateNextButton() {
		PagerAdapter adapter = mViewPager.getAdapter();
		if (adapter != null) {
			if (mViewPager.getCurrentItem() == adapter.getCount() - 1) {
				((Button) findViewById(R.id.tutorial_button_next)).setText(R.string.all_button_finish);
			}
			else {
				((Button) findViewById(R.id.tutorial_button_next)).setText(R.string.all_button_next);
			}
		}
		else {
			((Button) findViewById(R.id.tutorial_button_next)).setText(R.string.all_button_finish);
		}
	}

	@Override
	public void onRequestPermissionsResult(
			int requestCode,
			@NonNull String permissions[],
			@NonNull int[] grantResults) {
		mPermissionsPerformer.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}
}
