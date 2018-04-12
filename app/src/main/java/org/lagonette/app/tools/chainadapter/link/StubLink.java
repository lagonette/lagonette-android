package org.lagonette.app.tools.chainadapter.link;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import org.lagonette.app.tools.chainadapter.component.AdapterComponent;

public class StubLink<VH extends RecyclerView.ViewHolder, C extends AdapterComponent<VH>>
		extends ComponentLink<VH, C> {

	public StubLink(int rank, @NonNull C component) {
		super(rank, component);
	}

	@Override
	public int getItemCount() {
		return component.getItemCount();
	}

	@Override
	public int getItemViewType(int position) {
		return component.getItemViewType(position);
	}

	@Override
	public long getItemId(int position) {
		return component.getItemId(position);
	}

	@NonNull
	@Override
	public VH createViewHolder(@NonNull ViewGroup parent, int viewType) {
		return component.createViewHolder(parent, viewType);
	}

	@Override
	public void bindViewHolder(@NonNull VH holder, int position) {
		component.bindViewHolder(holder, position);
	}

	@Override
	public boolean isChained(int rank) {
		return rank == mRank;
	}
}
