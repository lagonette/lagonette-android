package org.lagonette.app.app.activity;

import android.content.res.Configuration;

import org.lagonette.app.R;
import org.lagonette.app.app.widget.presenter.LandscapeMainPresenter;
import org.lagonette.app.app.widget.presenter.MainPresenter;
import org.lagonette.app.app.widget.presenter.PortraitMainPresenter;

public class MainActivity
        extends PresenterActivity<MainPresenter> {

    private static final String TAG = "MapsActivity";

    @Override
    protected MainPresenter getPresenter() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return new LandscapeMainPresenter();
        }
        else {
            return new PortraitMainPresenter();
        }
    }

    @Override // TODO Maybe layout should be managed by presenter
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBackPressed() {
        if (!mPresenter.onBackPressed(MainActivity.this)) {
            super.onBackPressed();
        }
    }

}
