package org.lagonette.app.tools.chainadapter.datasource;

import android.support.annotation.Nullable;

public class VoidDataSource
		implements AdapterDataSource<Void, Void> {

	private int mItemCount;

	public VoidDataSource(int itemCount) {
		mItemCount = itemCount;
	}

	@Override
	public long getItemId(int position) {
		if (position > mItemCount) {
			throw new IllegalStateException("Position should not be greater than item count.");
		}
		return position;
	}

	@Nullable
	@Override
	public Void getItem(int position) {
		return null;
	}

	@Override
	public int getCount() {
		return mItemCount;
	}

	@Override
	public void setSource(@Nullable Void v) {

	}
}
