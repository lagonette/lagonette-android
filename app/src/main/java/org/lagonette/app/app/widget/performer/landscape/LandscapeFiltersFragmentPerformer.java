package org.lagonette.app.app.widget.performer.landscape;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import org.lagonette.app.app.widget.performer.base.FiltersFragmentPerformer;

public class LandscapeFiltersFragmentPerformer extends FiltersFragmentPerformer {

    public LandscapeFiltersFragmentPerformer(@NonNull AppCompatActivity activity, int filtersContainerRes) {
        super(activity, filtersContainerRes);
    }

    @Override
    public void restore() {
        super.restore();
        if (isLoaded()) {
            loadFragment();
        }
    }
}
