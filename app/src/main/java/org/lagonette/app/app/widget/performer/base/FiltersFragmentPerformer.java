package org.lagonette.app.app.widget.performer.base;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.lagonette.app.app.fragment.FiltersFragment;

import java.util.ArrayList;

public class FiltersFragmentPerformer implements Performer {

    public interface FragmentLoadedCommand {

        void onFiltersLoaded();
    }

    public interface FragmentUnloadedCommand {

        void onFiltersUnloaded();
    }


    @Nullable
    private FiltersFragment mFragment;

    @NonNull
    private FragmentManager mFragmentManager;

    @NonNull
    private final ArrayList<FragmentUnloadedCommand> mFragmentUnloadedCommands;

    @NonNull
    private final ArrayList<FragmentLoadedCommand> mFragmentLoadedCommands;

    @NonNull
    private final MutableLiveData<Integer> mTopPadding;

    @NonNull
    private final MutableLiveData<Void> mLoadedNotifier;

    @IdRes
    private final int mFiltersContainerRes;

    public FiltersFragmentPerformer(
            @NonNull AppCompatActivity activity,
            @IdRes int filtersContainerRes) {
        mFragmentManager = activity.getSupportFragmentManager();
        mFragmentLoadedCommands = new ArrayList<>();
        mFragmentUnloadedCommands = new ArrayList<>();
        mLoadedNotifier = new MutableLiveData<>();
        mLoadedNotifier.setValue(null);
        mFiltersContainerRes = filtersContainerRes;
        mTopPadding = new MutableLiveData<>();
    }

    @Override
    public void inject(@NonNull View view) {
        // Do nothing.
    }

    public void restoreFragment() {
        mFragment = (FiltersFragment) mFragmentManager.findFragmentByTag(FiltersFragment.TAG);
        if (mFragment != null) {
            mTopPadding.observe(
                    mFragment,
                    mFragment::updateTopPadding
            );
        }
    }

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
                        mFiltersContainerRes,
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
