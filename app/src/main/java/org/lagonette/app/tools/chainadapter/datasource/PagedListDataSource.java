package org.lagonette.app.tools.chainadapter.datasource;

import android.arch.paging.AsyncPagedListDiffer;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import org.lagonette.app.tools.chainadapter.adapter.WholeListUpdateCallback;

public abstract class PagedListDataSource<Item>
		implements AdapterDataSource<Item, PagedList<Item>> {

	@NonNull
	private AsyncPagedListDiffer<Item> mDiffer;

	public PagedListDataSource(
			@NonNull RecyclerView.Adapter adapter,
			@NonNull DiffUtil.ItemCallback<Item> diffCallback) {
		// We use WholeListUpdateCallback to notify all items have changed
		// rather than the specified range
		// because with chained links the range may be wrong.
		mDiffer = new AsyncPagedListDiffer<>(
				new WholeListUpdateCallback(adapter),
				new AsyncDifferConfig.Builder<>(diffCallback).build()
		);
	}

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

	@Override
	public Item getItem(int position) {
		return mDiffer.getItem(position);
	}

	@Override
	public int getCount() {
		return mDiffer.getItemCount();
	}

	@Override
	public void setSource(@Nullable PagedList<Item> pagedList) {
		mDiffer.submitList(pagedList);
	}

	protected abstract long getItemId(int position, @NonNull Item item);
}
