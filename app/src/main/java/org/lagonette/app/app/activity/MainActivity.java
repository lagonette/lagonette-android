package org.lagonette.app.app.activity;

import org.lagonette.app.app.widget.presenter.MainPresenter;
import org.lagonette.app.di.Injector;

public class MainActivity
        extends PresenterActivity<MainPresenter> {

	@Override
	protected void injectDependencies() {
		Injector.inject(this);
	}

    @Override
    public void onBackPressed() {
        if (!mPresenter.onBackPressed(MainActivity.this)) {
            super.onBackPressed();
        }
    }

}
