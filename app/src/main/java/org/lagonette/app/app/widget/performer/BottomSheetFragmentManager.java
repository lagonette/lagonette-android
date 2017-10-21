package org.lagonette.app.app.widget.performer;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import org.lagonette.app.R;
import org.lagonette.app.app.fragment.FiltersFragment;
import org.lagonette.app.app.fragment.PartnerDetailFragment;
import org.lagonette.app.app.widget.coordinator.MainCoordinator;
import org.lagonette.app.room.statement.Statement;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class BottomSheetFragmentManager implements MainCoordinator.FragmentLoader {

    public BottomSheetFragmentManager() {
    }

    public interface Observer {

        void notifyBottomSheetFragmentChanged(@FragmentType int fragment);

    }

    public static final int FRAGMENT_NONE = 0;

    public static final int FRAGMENT_FILTERS = 1;

    public static final int FRAGMENT_PARTNER = 2;

    @Retention(SOURCE)
    @IntDef({
            FRAGMENT_NONE,
            FRAGMENT_FILTERS,
            FRAGMENT_PARTNER
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

    @Override
    public void unloadFragment() {
        if (mFragmentManager != null && mFragment != null) {
            mFragmentManager.beginTransaction()
                    .remove(mFragment)
                    .commit();
            mFragment = null;
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
            mObserver.notifyBottomSheetFragmentChanged(FRAGMENT_FILTERS);
        }
    }

    @Override
    public void loadPartnerFragment() {
        if (mFragmentManager != null) {
            mFragment = PartnerDetailFragment.newInstance(Statement.NO_ID);
            mFragmentManager.beginTransaction()
                    .replace(
                            R.id.bottom_sheet,
                            mFragment,
                            PartnerDetailFragment.TAG
                    )
                    .commit();
            mObserver.notifyBottomSheetFragmentChanged(FRAGMENT_PARTNER);
        }
    }

    public void setObserver(Observer observer) {
        mObserver = observer;
    }

}
