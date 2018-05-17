package org.lagonette.app.app.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.lagonette.app.R;
import org.lagonette.app.app.widget.performer.impl.PermissionsPerformer;
import org.lagonette.app.app.widget.performer.impl.TutorialPerformer;
import org.lagonette.app.app.widget.viewpager.TutorialViewPagerAdapter;

public class TutorialActivity
		extends BaseActivity {

	private ViewPager mViewPager;

	private DotsIndicator mDotsIndicator;

	private TutorialViewPagerAdapter mViewPagerAdapter;

	private PermissionsPerformer mPermissionsPerformer;

	private TutorialPerformer mTutorialPerformer;

	@Override
	protected void construct() {
		mViewPagerAdapter = new TutorialViewPagerAdapter();
		mPermissionsPerformer = new PermissionsPerformer(this);
		mTutorialPerformer = new TutorialPerformer();
	}

	@Override
	protected int getContentView() {
		return R.layout.activity_tutorial;
	}

	@Override
	protected void inject(@NonNull View view) {
		mViewPager = view.findViewById(R.id.tutorial_pager);
		mDotsIndicator = view.findViewById(R.id.tutorial_dots);
		mTutorialPerformer.inject(view);
	}

	@Override
	protected void init() {

	}

	@Override
	protected void restore(@NonNull Bundle savedInstanceState) {

	}

	@Override
	protected void onConstructed() {

		mViewPager.setAdapter(mViewPagerAdapter);
		mDotsIndicator.setViewPager(mViewPager);

		mViewPager.addOnPageChangeListener(mTutorialPerformer);

		mViewPagerAdapter.callbacks.finish = this::finish;
		mViewPagerAdapter.callbacks.goToNextPage = mTutorialPerformer::goToNextPage;
		mViewPagerAdapter.callbacks.askForFineLocation = mPermissionsPerformer::askForFineLocation;
		mViewPagerAdapter.callbacks.checkForFineLocation = mPermissionsPerformer::checkForFineLocation;

		mTutorialPerformer.finish = this::finish;
		mTutorialPerformer.getCurrentPage = mViewPager::getCurrentItem;
		mTutorialPerformer.getPageCount = mViewPagerAdapter::getCount;
		mTutorialPerformer.goToNextPage = () -> mViewPager.arrowScroll(View.FOCUS_RIGHT);

		mPermissionsPerformer.onFineLocationPermissionResult = mViewPagerAdapter::onFineLocationPermissionResult;

		mTutorialPerformer.start();
	}

	@Override
	public void onRequestPermissionsResult(
			int requestCode,
			@NonNull String permissions[],
			@NonNull int[] grantResults) {
		mPermissionsPerformer.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	public void finish(int resultCode) {
		setResult(resultCode);
		finish();
	}
}
