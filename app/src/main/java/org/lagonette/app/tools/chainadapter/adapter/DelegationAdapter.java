package org.lagonette.app.tools.chainadapter.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import org.lagonette.app.tools.chainadapter.delegate.AdapterDelegate;

public abstract class DelegationAdapter<VH extends RecyclerView.ViewHolder, D extends AdapterDelegate<VH>>
		extends RecyclerView.Adapter<VH> {

	@Nullable
	private D mDelegate;

	@NonNull
	@Override
	public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		if (mDelegate == null) {
			throw new IllegalStateException("You must provide a delegate !");
		}

		return mDelegate.createViewHolder(parent, viewType);
	}

	@Override
	public void onBindViewHolder(@NonNull VH holder, int position) {
		if (mDelegate == null) {
			throw new IllegalStateException("You must provide a delegate !");
		}

		mDelegate.bindViewHolder(holder, position);
	}

	@Override
	public int getItemViewType(int position) {
		if (mDelegate == null) {
			throw new IllegalStateException("You must provide a delegate !");
		}

		return mDelegate.getItemViewType(position);
	}

	@Override
	public long getItemId(int position) {
		if (mDelegate == null) {
			throw new IllegalStateException("You must provide a delegate !");
		}

		return mDelegate.getItemId(position);
	}

	@Override
	public int getItemCount() {
		if (mDelegate == null) {
			throw new IllegalStateException("You must provide a delegate !");
		}

		return mDelegate.getItemCount();
	}

	public void set(@Nullable D delegate) {
		mDelegate = delegate;
	}

	@Nullable
	public D get() {
		return mDelegate;
	}
}
