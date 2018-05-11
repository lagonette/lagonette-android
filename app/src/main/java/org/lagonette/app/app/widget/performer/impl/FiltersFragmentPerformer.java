package org.lagonette.app.app.widget.performer.impl;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import org.lagonette.app.R;
import org.lagonette.app.app.fragment.FiltersFragment;
import org.lagonette.app.app.widget.performer.base.FragmentPerformer;

import java.util.ArrayList;

public class FiltersFragmentPerformer
		implements FragmentPerformer {

	public interface FragmentLoadedCommand {

		void onFiltersLoaded();
	}

	public interface FragmentUnloadedCommand {

		void onFiltersUnloaded();
	}

	@NonNull
	private final ArrayList<FragmentUnloadedCommand> mFragmentUnloadedCommands;

	@NonNull
	private final ArrayList<FragmentLoadedCommand> mFragmentLoadedCommands;

	//TODO Use EventBus ? Or maybe all performer>fragment com' should be done with public method ?
	@NonNull
	private final MutableLiveData<Integer> mTopPadding;

	@NonNull
	private final MutableLiveData<Void> mLoadedNotifier;

	@Nullable
	private FiltersFragment mFragment;

	@NonNull
	private FragmentManager mFragmentManager;

	public FiltersFragmentPerformer(
			@NonNull AppCompatActivity activity) {
		mFragmentManager = activity.getSupportFragmentManager();
		mFragmentLoadedCommands = new ArrayList<>();
		mFragmentUnloadedCommands = new ArrayList<>();
		mLoadedNotifier = new MutableLiveData<>();
		mLoadedNotifier.setValue(null);
		mTopPadding = new MutableLiveData<>();
	}

	@Override
	public void restoreFragment() {
		mFragment = (FiltersFragment) mFragmentManager.findFragmentByTag(FiltersFragment.TAG);
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

		for (FragmentUnloadedCommand command : mFragmentUnloadedCommands) {
			command.onFiltersUnloaded();
		}
	}

	public void loadFragment() {
		mFragment = FiltersFragment.newInstance();
		mFragmentManager
				.beginTransaction()
				.add(
						R.id.fragment_filters,
						mFragment,
						FiltersFragment.TAG
				)
				.commit();

		mTopPadding.observe(
				mFragment,
				mFragment::updateTopPadding
		);

		mLoadedNotifier.observe(
				mFragment,
				aVoid -> {
					for (FragmentLoadedCommand command : mFragmentLoadedCommands) {
						command.onFiltersLoaded();
					}
				}
		);
	}

	public boolean isLoaded() {
		return mFragment != null;
	}

	public void updateTopPadding(int topPadding) {
		mTopPadding.setValue(topPadding);
	}

	public void onFragmentLoaded(@Nullable FragmentLoadedCommand command) {
		mFragmentLoadedCommands.add(command);
	}

	public void onFragmentUnloaded(@Nullable FragmentUnloadedCommand command) {
		mFragmentUnloadedCommands.add(command);
	}
}
