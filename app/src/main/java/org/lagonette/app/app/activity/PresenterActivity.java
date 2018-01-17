package org.lagonette.app.app.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;

public abstract class PresenterActivity<Presenter extends PresenterActivity.Lifecycle> extends BaseActivity {

    public interface Lifecycle {

        void construct(@NonNull PresenterActivity owner);

        @LayoutRes
        int getContentView();

        void inject(@NonNull View view);

        void init(@NonNull PresenterActivity owner);

        void restore(@NonNull PresenterActivity owner, @NonNull Bundle savedInstanceState);

        void connect(@NonNull PresenterActivity owner);
    }

    protected Presenter mPresenter;

    @Override
    protected void construct() {
        mPresenter = getPresenter();
        mPresenter.construct(PresenterActivity.this);
    }

    @NonNull
    protected abstract Presenter getPresenter();

    @Override
    @LayoutRes
    protected int getContentView() {
        return mPresenter.getContentView();
    }

    @Override
    protected void inject(@NonNull View view) {
        mPresenter.inject(view);
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
    protected void connect() {
        mPresenter.connect(PresenterActivity.this);
    }
}
