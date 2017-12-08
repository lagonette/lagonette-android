package org.lagonette.app.app.widget.performer.base;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import org.lagonette.app.app.fragment.FiltersFragment;

public class FiltersFragmentPerformer
        implements BottomSheetPerformer.Slideable {


    @Nullable
    private FiltersFragment mFragment;

    @NonNull
    private FragmentManager mFragmentManager;

    @IdRes
    private final int mFiltersContainerRes;

    public FiltersFragmentPerformer(
            @NonNull AppCompatActivity activity,
            @IdRes int filtersContainerRes) {
        mFragmentManager = activity.getSupportFragmentManager();
        mFiltersContainerRes = filtersContainerRes;
    }

    public void init () {
        loadFragment();
    }

    public void restore() {
        mFragment = (FiltersFragment) mFragmentManager.findFragmentByTag(FiltersFragment.TAG);
    }

    public void unloadFragment() {
        if (mFragment != null) {
            mFragmentManager.beginTransaction()
                    .remove(mFragment)
                    .commit();
            mFragment = null;
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
}
