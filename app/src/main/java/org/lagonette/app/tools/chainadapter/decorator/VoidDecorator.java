package org.lagonette.app.tools.chainadapter.decorator;

import android.support.v7.widget.RecyclerView;

public abstract class VoidDecorator<VH extends RecyclerView.ViewHolder>
		extends SimpleAdapterDecorator<VH, Void> {

	protected VoidDecorator(int viewType) {
		super(viewType);
	}

}
