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
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			return new LandscapeMainPresenter();
		}
		else {
			return new PortraitMainPresenter();
		}
	}

	@Override
	public void onBackPressed() {
		if (!mPresenter.onBackPressed(MainActivity.this)) {
			super.onBackPressed();
		}
	}

}
