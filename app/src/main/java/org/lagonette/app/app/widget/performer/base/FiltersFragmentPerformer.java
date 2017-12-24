package org.lagonette.app.app.widget.performer.base;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import org.lagonette.app.app.fragment.FiltersFragment;

public class FiltersFragmentPerformer
        implements BottomSheetPerformer.Slideable {

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

    @Nullable
    private FragmentUnloadedCommand mFragmentUnloadedCommand;

    @Nullable
    private FragmentLoadedCommand mFragmentLoadedCommand;

    @IdRes
    private final int mFiltersContainerRes;

    public FiltersFragmentPerformer(
            @NonNull AppCompatActivity activity,
            @IdRes int filtersContainerRes) {
        mFragmentManager = activity.getSupportFragmentManager();
        mFiltersContainerRes = filtersContainerRes;
    }

    public void restoreFragment() {
        mFragment = (FiltersFragment) mFragmentManager.findFragmentByTag(FiltersFragment.TAG);
    }

    public void unloadFragment() {
        if (mFragment != null) {
            mFragmentManager.beginTransaction()
                    .remove(mFragment)
                    .commit();
            mFragment = null;
        }

        if (mFragmentUnloadedCommand != null) {
            mFragmentUnloadedCommand.onFiltersUnloaded();
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

        if (mFragmentLoadedCommand != null) {
            mFragmentLoadedCommand.onFiltersLoaded();
        }
    }

    public boolean isLoaded() {
        return mFragment != null;
    }

    @Override
    public void updateTopPadding(int topPadding) {
        if (mFragment != null) {
            mFragment.updateTopPadding(topPadding);
        }
    }

    public void onFragmentLoaded(@Nullable FragmentLoadedCommand command) {
        mFragmentLoadedCommand = command;
    }

    public void onFragmentUnloaded(@Nullable FragmentUnloadedCommand command) {
        mFragmentUnloadedCommand = command;
    }
}
