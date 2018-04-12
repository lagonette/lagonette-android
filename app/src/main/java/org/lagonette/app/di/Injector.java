package org.lagonette.app.di;


import android.content.res.Configuration;
import android.support.annotation.NonNull;

import org.lagonette.app.app.LaGonetteApplication;
import org.lagonette.app.app.activity.MainActivity;
import org.lagonette.app.app.fragment.MapsFragment;
import org.lagonette.app.di.component.AppComponent;
import org.lagonette.app.di.module.ActivityModule;
import org.lagonette.app.di.module.FragmentModule;

public class Injector {

	public static void inject(@NonNull MainActivity activity) {

		AppComponent appComponent = LaGonetteApplication
				.get(activity)
				.getAppComponent();

		ActivityModule activityModule = new ActivityModule(activity);

		if (appComponent.getOrientation() == Configuration.ORIENTATION_LANDSCAPE) {
			appComponent
					.getActivityComponent(activityModule)
					.getLandscapeActivityComponent()
					.inject(activity);
		}
		else {
			appComponent
					.getActivityComponent(activityModule)
					.getPortraitActivityComponent()
					.inject(activity);
		}
	}

	public static void inject(@NonNull MapsFragment fragment) {

		ActivityModule activityModule = new ActivityModule(fragment.getActivity());
		FragmentModule fragmentModule = new FragmentModule(fragment);

		LaGonetteApplication
				.get(fragment.getContext())
				.getAppComponent()
				.getActivityComponent(activityModule)
				.getFragmentComponent(fragmentModule)
				.inject(fragment);
	}
}
