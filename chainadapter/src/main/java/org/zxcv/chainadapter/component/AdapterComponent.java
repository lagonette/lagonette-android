package org.zxcv.chainadapter.component;

import android.support.v7.widget.RecyclerView;

import org.zxcv.chainadapter.delegate.AdapterDelegate;

public interface AdapterComponent<VH extends RecyclerView.ViewHolder>
		extends AdapterDelegate<VH> {

	boolean handleViewType(int viewType);
}
