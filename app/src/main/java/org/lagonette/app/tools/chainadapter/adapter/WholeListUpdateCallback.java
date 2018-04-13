package org.lagonette.app.tools.chainadapter.adapter;

import android.support.annotation.NonNull;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;

public class WholeListUpdateCallback
		implements ListUpdateCallback {

	@NonNull
	private final RecyclerView.Adapter mAdapter;

	public WholeListUpdateCallback(@NonNull RecyclerView.Adapter adapter) {
		mAdapter = adapter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onInserted(int position, int count) {
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onRemoved(int position, int count) {
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMoved(int fromPosition, int toPosition) {
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onChanged(int position, int count, Object payload) {
		mAdapter.notifyDataSetChanged();
	}
}
