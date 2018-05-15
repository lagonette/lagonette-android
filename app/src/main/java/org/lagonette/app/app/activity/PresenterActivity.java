package org.lagonette.app.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;

public abstract class PresenterActivity<Presenter extends PresenterActivity.Lifecycle>
		extends BaseActivity {

	public interface Lifecycle {

		void construct(@NonNull PresenterActivity owner);

		@LayoutRes
		int getContentView();

		void inject(@NonNull View view);

		void init(@NonNull PresenterActivity owner);

		void restore(@NonNull PresenterActivity owner, @NonNull Bundle savedInstanceState);

		void onConstructed(@NonNull PresenterActivity owner);

		void onRequestPermissionsResult(
				int requestCode,
				@NonNull String[] permissions,
				@NonNull int[] grantResults);

		void onActivityResult(int requestCode, int resultCode, @NonNull Intent data);
	}

	protected Presenter mPresenter;

	@Override
	protected void construct() {
		mPresenter = getPresenter();
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

	@NonNull
	protected abstract Presenter getPresenter();

	@Override
	public void onRequestPermissionsResult(
			int requestCode,
			@NonNull String[] permissions,
			@NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		mPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mPresenter.onActivityResult(requestCode, resultCode, data);
	}
}
