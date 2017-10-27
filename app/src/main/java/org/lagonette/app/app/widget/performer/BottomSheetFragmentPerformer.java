package org.lagonette.app.app.widget.performer;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import org.lagonette.app.R;
import org.lagonette.app.app.fragment.FiltersFragment;
import org.lagonette.app.app.fragment.LocationDetailFragment;
import org.lagonette.app.app.widget.coordinator.MainCoordinator;
import org.lagonette.app.room.statement.Statement;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class BottomSheetFragmentPerformer
        implements MainCoordinator.FragmentLoader {

    public interface Observer {

        void notifyBottomSheetFragmentChanged(@FragmentType int fragment);

    }

    public static final int FRAGMENT_NONE = 0;

    public static final int FRAGMENT_FILTERS = 1;

    public static final int FRAGMENT_LOCATION = 2;

    @Retention(SOURCE)
    @IntDef({
            FRAGMENT_NONE,
            FRAGMENT_FILTERS,
            FRAGMENT_LOCATION
    })
    public @interface FragmentType {

    }

    @Nullable
    private Fragment mFragment;

    private Observer mObserver;

    @Nullable
    private FragmentManager mFragmentManager;

    public void inject(@NonNull AppCompatActivity activity) {
        mFragmentManager = activity.getSupportFragmentManager();
    }

    public void init(@FragmentType int type) {
        if (mFragmentManager == null) {
            throw new IllegalStateException("inject() must be called before init()");
        }

        loadFragment(type);//TODO Check when it is a location detail fragment
    }

    public void restore(@FragmentType int type) {
        if (mFragmentManager == null) {
            throw new IllegalStateException("inject() must be called before restore()");
        }

        switch (type) {

            case FRAGMENT_NONE:
                mFragment = null;
                break;

            case FRAGMENT_FILTERS:
                mFragment = mFragmentManager.findFragmentByTag(FiltersFragment.TAG);
                break;

            case FRAGMENT_LOCATION:
                mFragment = mFragmentManager.findFragmentByTag(LocationDetailFragment.TAG);
                break;
        }
    }

    public void loadFragment(@FragmentType int type) {
        switch (type) {

            case FRAGMENT_NONE:
                unloadFragment();
                break;

            case FRAGMENT_FILTERS:
                loadFiltersFragment();
                break;

            case FRAGMENT_LOCATION:
                loadLocationFragment(Statement.NO_ID);
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
            mObserver.notifyBottomSheetFragmentChanged(FRAGMENT_NONE);
        }
    }

    @Override
    public void loadFiltersFragment() {
        if (mFragmentManager != null) {
            mFragment = FiltersFragment.newInstance("");
            mFragmentManager
                    .beginTransaction()
                    .replace(
                            R.id.bottom_sheet,
                            mFragment,
                            FiltersFragment.TAG
                    )
                    .commit();

            if (mObserver != null) {
                mObserver.notifyBottomSheetFragmentChanged(FRAGMENT_FILTERS);
            }
        }
    }

    @Override
    public void loadLocationFragment(long locationId) {
        if (mFragmentManager != null) {
            mFragment = LocationDetailFragment.newInstance(locationId);
            mFragmentManager.beginTransaction()
                    .replace(
                            R.id.bottom_sheet,
                            mFragment,
                            LocationDetailFragment.TAG
                    )
                    .commit();

            if (mObserver != null) {
                mObserver.notifyBottomSheetFragmentChanged(FRAGMENT_LOCATION);
            }
        }
    }

    public void observe(Observer observer) {
        mObserver = observer;
    }

}
