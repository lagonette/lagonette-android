package org.lagonette.app.tools.chainadapter.decorator;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

public abstract class SimpleAdapterDecorator<VH extends RecyclerView.ViewHolder, I>
		extends AbstractAdapterDecorator<VH, I> {

	private final int mViewType;

	protected SimpleAdapterDecorator(int viewType) {
		mViewType = viewType;
	}

	public final boolean handleViewType(int viewType) {
		return viewType == mViewType;
	}

	public final int getViewType(@Nullable I item) {
		return mViewType;
	}

}
