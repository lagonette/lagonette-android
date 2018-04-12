package org.lagonette.app.app.widget.performer.landscape;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.lagonette.app.app.widget.performer.impl.MapFragmentPerformer;

import javax.inject.Inject;

public class LandscapeMapFragmentPerformer extends MapFragmentPerformer {

    @Inject
    public LandscapeMapFragmentPerformer(@NonNull AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void inject(@NonNull View view) {

    }
}
