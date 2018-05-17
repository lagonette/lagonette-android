package org.lagonette.app.app.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.lagonette.app.R;
import org.lagonette.app.app.widget.performer.impl.PermissionsPerformer;
import org.lagonette.app.app.widget.performer.impl.OnboardingPerformer;
import org.lagonette.app.app.widget.viewpager.OnboardingViewPagerAdapter;

public class OnboardingActivity
		extends BaseActivity {

	private ViewPager mViewPager;

	private DotsIndicator mDotsIndicator;

	private OnboardingViewPagerAdapter mViewPagerAdapter;

	private PermissionsPerformer mPermissionsPerformer;

	private OnboardingPerformer mOnboardingPerformer;

	@Override
	protected void construct() {
		mViewPagerAdapter = new OnboardingViewPagerAdapter();
		mPermissionsPerformer = new PermissionsPerformer(this);
		mOnboardingPerformer = new OnboardingPerformer();
	}

	@Override
	protected int getContentView() {
		return R.layout.activity_onboarding;
	}

	@Override
	protected void inject(@NonNull View view) {
		mViewPager = view.findViewById(R.id.onboarding_pager);
		mDotsIndicator = view.findViewById(R.id.onboarding_dots);
		mOnboardingPerformer.inject(view);
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

		mViewPager.addOnPageChangeListener(mOnboardingPerformer);

		mViewPagerAdapter.callbacks.finish = this::finish;
		mViewPagerAdapter.callbacks.goToNextPage = mOnboardingPerformer::goToNextPage;
		mViewPagerAdapter.callbacks.askForFineLocation = mPermissionsPerformer::askForFineLocation;
		mViewPagerAdapter.callbacks.checkForFineLocation = mPermissionsPerformer::checkForFineLocation;

		mOnboardingPerformer.finish = this::finish;
		mOnboardingPerformer.getCurrentPage = mViewPager::getCurrentItem;
		mOnboardingPerformer.getPageCount = mViewPagerAdapter::getCount;
		mOnboardingPerformer.goToNextPage = () -> mViewPager.arrowScroll(View.FOCUS_RIGHT);

		mPermissionsPerformer.onFineLocationPermissionResult = mViewPagerAdapter::onFineLocationPermissionResult;

		mOnboardingPerformer.start();
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
