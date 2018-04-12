package org.lagonette.app.tools.chainadapter.datasource;

import android.support.annotation.Nullable;

public class SingleItemDataSource<Item>
		extends AbstractDataSource<Item, Item> {

	private boolean mDisplayWhenEmpty;

	public SingleItemDataSource(boolean displayWhenEmpty) {
		mDisplayWhenEmpty = displayWhenEmpty;
	}

	@Override
	public long getItemId(int position) {
		if (position > 0) {
			throw new IllegalStateException("Position should be equals to 0.");
		}

		return 0;
	}

	@Nullable
	@Override
	public Item getItem(int position) {
		if (!mDisplayWhenEmpty && mSource == null) {
			throw new IllegalStateException("Item is null, you should not get item.");
		}

		if (position > 0) {
			throw new IllegalStateException("Position should be equals to 0.");
		}

		return mSource;
	}

	@Override
	public int getCount() {
		return mDisplayWhenEmpty || mSource != null ? 1 : 0;
	}

	@Override
	public void setSource(@Nullable Item item) {
		mSource = item;
	}

}
