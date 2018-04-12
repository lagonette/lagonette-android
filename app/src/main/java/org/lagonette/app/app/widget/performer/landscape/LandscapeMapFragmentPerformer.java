package org.lagonette.app.app.widget.performer.landscape;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.lagonette.app.app.widget.performer.impl.MapFragmentPerformer;

public class LandscapeMapFragmentPerformer
		extends MapFragmentPerformer {

	public LandscapeMapFragmentPerformer(@NonNull AppCompatActivity activity, int mapFragmentRes) {
		super(activity, mapFragmentRes);
	}

	@Override
	public void inject(@NonNull View view) {

	}
}
