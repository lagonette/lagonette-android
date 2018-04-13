package org.lagonette.app.app.widget.adapter.decorator;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import org.lagonette.app.R;
import org.lagonette.app.app.widget.viewholder.EmptyViewHolder;
import org.lagonette.app.tools.chainadapter.decorator.VoidDecorator;

public class EmptyViewDecorator
		extends VoidDecorator<EmptyViewHolder> {

	public EmptyViewDecorator() {
		super(R.id.view_type_empty_view);
	}

	@NonNull
	@Override
	public EmptyViewHolder createViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new EmptyViewHolder(parent);
	}

	@Override
	protected void onBindViewHolder(@Nullable Void aVoid, @NonNull EmptyViewHolder viewHolder) {

	}
}
