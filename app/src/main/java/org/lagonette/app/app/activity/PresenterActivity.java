package org.lagonette.app.app.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import org.lagonette.app.app.widget.presenter.ActivityPresenter;

public abstract class PresenterActivity<Presenter extends ActivityPresenter> extends BaseActivity {

    @NonNull
    protected Presenter mPresenter;

    @Override
    protected void construct() {
        mPresenter = getPresenter();
        mPresenter.construct(PresenterActivity.this);
    }

    protected abstract Presenter getPresenter();

    @Override
    protected void onViewCreated(@NonNull View view) {
        mPresenter.onViewCreated(view);
    }

    @Override
    protected void init() {
        mPresenter.init(PresenterActivity.this);
    }

    @Override
    protected void restore(@NonNull Bundle savedInstanceState) {
        mPresenter.restore(PresenterActivity.this, savedInstanceState);
    }

    @Override
    protected void onActivityCreated() {
        mPresenter.onActivityCreated(PresenterActivity.this);
    }
}
