package org.zxcv.chainadapter.decorator;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public interface AdapterDecorator<ViewHolder extends RecyclerView.ViewHolder, Item> {

	@NonNull
	ViewHolder createViewHolder(@NonNull ViewGroup parent, int viewType);

	void bindViewHolder(@Nullable Item item, @NonNull RecyclerView.ViewHolder holder);

	boolean handleViewType(int viewType);

	int getViewType(@Nullable Item item);

	boolean handleItem(@Nullable Item item);
}