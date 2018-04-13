package org.lagonette.app.tools.chainadapter.decorator;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;


public abstract class AbstractAdapterDecorator<VH extends RecyclerView.ViewHolder, I>
		implements AdapterDecorator<VH, I> {

	public final void bindViewHolder(@Nullable I item, @NonNull RecyclerView.ViewHolder holder) {
		//noinspection unchecked
		onBindViewHolder(item, (VH) holder);
	}

	@Override
	public boolean handleItem(@Nullable I item) {
		return true;
	}

	protected abstract void onBindViewHolder(@Nullable I item, @NonNull VH viewHolder);
}
