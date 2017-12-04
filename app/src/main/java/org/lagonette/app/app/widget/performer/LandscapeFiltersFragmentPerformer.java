package org.lagonette.app.app.widget.performer;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import org.lagonette.app.app.fragment.FiltersFragment;

public class LandscapeFiltersFragmentPerformer {


    @Nullable
    private FiltersFragment mFragment;

    @NonNull
    private FragmentManager mFragmentManager;

    @IdRes
    private final int mFiltersContainerRes;

    public LandscapeFiltersFragmentPerformer(
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
        if (mFragment == null) {
            loadFragment();
        }
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

}
