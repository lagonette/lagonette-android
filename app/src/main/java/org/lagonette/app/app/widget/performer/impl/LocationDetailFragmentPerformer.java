package org.lagonette.app.app.widget.performer.impl;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import org.lagonette.app.app.fragment.LocationDetailFragment;
import org.lagonette.app.app.widget.performer.base.FragmentPerformer;
import org.lagonette.app.room.statement.Statement;
import org.zxcv.functions.main.LongConsumer;

import java.util.ArrayList;

public class LocationDetailFragmentPerformer
		implements FragmentPerformer {

	@NonNull
	private final ArrayList<Runnable> mFragmentUnloadedCommands;

	@NonNull
	private final ArrayList<LongConsumer> mFragmentLoadedCommands;

	@NonNull
	private final MutableLiveData<Integer> mTopPadding;

	@NonNull
	private final MutableLiveData<Void> mLoadedNotifier;

	@IdRes
	private final int mLocationDetailContainerRes;

	@Nullable
	private LocationDetailFragment mFragment;

	@NonNull
	private FragmentManager mFragmentManager;

	public LocationDetailFragmentPerformer(
			@NonNull AppCompatActivity activity,
			@IdRes int locationDetailContainerRed) {
		mFragmentManager = activity.getSupportFragmentManager();
		mFragmentLoadedCommands = new ArrayList<>();
		mFragmentUnloadedCommands = new ArrayList<>();
		mLocationDetailContainerRes = locationDetailContainerRed;
		mTopPadding = new MutableLiveData<>();
		mLoadedNotifier = new MutableLiveData<>();
		mLoadedNotifier.setValue(null);
	}

	public void restoreFragment() {
		mFragment = (LocationDetailFragment) mFragmentManager.findFragmentByTag(LocationDetailFragment.TAG);
		if (mFragment != null) {
			mTopPadding.observe(
					mFragment,
					mFragment::updateTopPadding
			);
		}
	}

	@Override
	public void unloadFragment() {
		if (mFragment != null) {
			mFragmentManager.beginTransaction()
					.remove(mFragment)
					.commit();
			mFragment = null;
		}

		for (Runnable command : mFragmentUnloadedCommands) {
			command.run();
		}
	}

	public boolean isLoaded() {
		return mFragment != null;
	}

	public boolean isLoaded(long locationId) {
		return isLoaded() && mFragment.isLoaded(locationId);
	}

	public long getLoadedId() {
		return isLoaded() ? mFragment.getLocationId() : Statement.NO_ID;
	}

	public void loadFragment(long locationId) {
		mFragment = LocationDetailFragment.newInstance(locationId);
		FragmentTransaction transaction = mFragmentManager.beginTransaction();

		if (isLoaded()) {
			transaction.setCustomAnimations(
					android.R.anim.fade_in,
					android.R.anim.fade_out
			);
		}

		transaction.replace(
				mLocationDetailContainerRes,
				mFragment,
				LocationDetailFragment.TAG
		);

		transaction.commit();

		mTopPadding.observe(
				mFragment,
				mFragment::updateTopPadding
		);


		mLoadedNotifier.observe(
				mFragment,
				aVoid -> {
					for (LongConsumer command : mFragmentLoadedCommands) {
						command.accept(locationId);
					}
				}
		);
	}

	public void updateTopPadding(int topPadding) {
		mTopPadding.setValue(topPadding);
	}

	public void onFragmentLoaded(@NonNull LongConsumer command) {
		mFragmentLoadedCommands.add(command);
	}

	public void onFragmentUnloaded(@NonNull Runnable command) {
		mFragmentUnloadedCommands.add(command);
	}
}
