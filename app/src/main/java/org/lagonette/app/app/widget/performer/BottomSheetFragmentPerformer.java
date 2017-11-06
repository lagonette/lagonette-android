package org.lagonette.app.app.widget.performer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import org.lagonette.app.R;
import org.lagonette.app.app.fragment.FiltersFragment;
import org.lagonette.app.app.fragment.LocationDetailFragment;
import org.lagonette.app.app.widget.coordinator.MainCoordinator;
import org.lagonette.app.app.widget.performer.state.BottomSheetFragmentType;

public class BottomSheetFragmentPerformer
        implements MainCoordinator.FragmentLoader {

    public interface Observer {

        void notifyUnload();

        void notifyFiltersLoaded();

        void notifyLocationLoaded(long locationId);

    }

    @Nullable
    private Fragment mFragment;

    private Observer mObserver;

    @Nullable
    private FragmentManager mFragmentManager;

    public void inject(@NonNull AppCompatActivity activity) {
        mFragmentManager = activity.getSupportFragmentManager();
    }

    public void init(@NonNull BottomSheetFragmentType type) {
        if (mFragmentManager == null) {
            throw new IllegalStateException("inject() must be called before init()");
        }

        loadFragment(type);
    }

    public void restore(@NonNull BottomSheetFragmentType type) {
        if (mFragmentManager == null) {
            throw new IllegalStateException("inject() must be called before restore()");
        }

        switch (type.getFragmentType()) {

            case BottomSheetFragmentType.FRAGMENT_NONE:
                mFragment = null;
                break;

            case BottomSheetFragmentType.FRAGMENT_FILTERS:
                mFragment = mFragmentManager.findFragmentByTag(FiltersFragment.TAG);
                break;

            case BottomSheetFragmentType.FRAGMENT_LOCATION:
                mFragment = mFragmentManager.findFragmentByTag(LocationDetailFragment.TAG);
                break;
        }
    }

    private void loadFragment(@NonNull BottomSheetFragmentType type) {
        switch (type.getFragmentType()) {

            case BottomSheetFragmentType.FRAGMENT_NONE:
                unloadFragment();
                break;

            case BottomSheetFragmentType.FRAGMENT_FILTERS:
                loadFiltersFragment();
                break;

            case BottomSheetFragmentType.FRAGMENT_LOCATION:
                loadLocationFragment(type.getLocationId(), false);
                break;
        }
    }

    @Override
    public void unloadFragment() {
        if (mFragmentManager != null && mFragment != null) {
            mFragmentManager.beginTransaction()
                    .remove(mFragment)
                    .commit();
            mFragment = null;
        }

        if (mObserver != null) {
            mObserver.notifyUnload();
        }
    }

    @Override
    public void loadFiltersFragment() {
        if (mFragmentManager != null) {
            mFragment = FiltersFragment.newInstance(""); //TODO put search string here
            mFragmentManager
                    .beginTransaction()
                    .replace(
                            R.id.bottom_sheet,
                            mFragment,
                            FiltersFragment.TAG
                    )
                    .commit();

            if (mObserver != null) {
                mObserver.notifyFiltersLoaded();
            }
        }
    }

    @Override
    public void loadLocationFragment(long locationId, boolean animation) {
        if (mFragmentManager != null) {
            mFragment = LocationDetailFragment.newInstance(locationId);
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            if (animation) {
                transaction.setCustomAnimations(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                );
            }
            transaction
                    .replace(
                            R.id.bottom_sheet,
                            mFragment,
                            LocationDetailFragment.TAG
                    )
                    .commit();

            if (mObserver != null) {
                mObserver.notifyLocationLoaded(locationId);
            }
        }
    }

    public void observe(Observer observer) {
        mObserver = observer;
    }

}
