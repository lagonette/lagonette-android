package org.lagonette.app.app.activity;

import android.content.res.Configuration;
import android.support.annotation.NonNull;

import org.lagonette.app.app.widget.presenter.LandscapeMainPresenter;
import org.lagonette.app.app.widget.presenter.MainPresenter;
import org.lagonette.app.app.widget.presenter.PortraitMainPresenter;

public class MainActivity
		extends PresenterActivity<MainPresenter> {

	@NonNull
	@Override
	protected MainPresenter getPresenter() {
		MainPresenter presenter;
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			presenter = new LandscapeMainPresenter(mSharedPreferencesPerformer);
		}
		else {
			presenter = new PortraitMainPresenter(mSharedPreferencesPerformer);
		}
		presenter.enableCrashlyticsIfNeeded = this::initCrashlyticsIfNeeded;
		return presenter;
	}

	@Override
	public void onBackPressed() {
		if (!mPresenter.onBackPressed(MainActivity.this)) {
			super.onBackPressed();
		}
	}

}
