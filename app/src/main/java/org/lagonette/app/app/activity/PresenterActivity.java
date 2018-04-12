package org.lagonette.app.app.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;

import javax.inject.Inject;

public abstract class PresenterActivity<Presenter extends PresenterActivity.Lifecycle> extends BaseActivity {

    public interface Lifecycle {

        void construct(@NonNull PresenterActivity owner);

        @LayoutRes
        int getContentView();

        void inject(@NonNull View view);

        void init(@NonNull PresenterActivity owner);

        void restore(@NonNull PresenterActivity owner, @NonNull Bundle savedInstanceState);

        void onConstructed(@NonNull PresenterActivity owner);

        void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
    }

    @Inject
    protected Presenter mPresenter;

    @Override
    protected void construct() {
        mPresenter.construct(PresenterActivity.this);
    }

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
    protected void onConstructed() {
        mPresenter.onConstructed(PresenterActivity.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
