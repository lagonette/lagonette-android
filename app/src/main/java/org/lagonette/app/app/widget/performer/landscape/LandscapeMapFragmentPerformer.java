package org.lagonette.app.app.widget.performer.landscape;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.lagonette.app.app.widget.performer.base.MapFragmentPerformer;

public class LandscapeMapFragmentPerformer extends MapFragmentPerformer {

    public LandscapeMapFragmentPerformer(@NonNull AppCompatActivity activity, int mapFragmentRes, int searchBarHeightRes) {
        super(activity, mapFragmentRes, searchBarHeightRes);
    }

    @Override
    public void inject(@NonNull View view) {

    }
}
