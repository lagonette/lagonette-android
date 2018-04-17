package org.zxcv.chainadapter.datasource;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public abstract class ListDataSource<Item>
		extends AbstractDataSource<Item, List<Item>> {

	@Override
	public long getItemId(int position) {
		Item item = getItem(position);
		if (item == null) {
			return RecyclerView.NO_ID;
		}
		else {
			return getItemId(position, item);
		}
	}

	@Nullable
	@Override
	public Item getItem(int position) {
		if (mSource != null) {
			int size = mSource.size();
			if (position > size) {
				throw new IllegalStateException("Position should not be greater than list size.");
			}
			return mSource.get(position);
		}
		else {
			throw new IllegalStateException("List is empty, you should not get item.");
		}

	}

	@Override
	public int getCount() {
		if (mSource != null) {
			return mSource.size();
		}
		else {
			return 0;
		}
	}

	protected abstract long getItemId(int position, @NonNull Item item);

}
