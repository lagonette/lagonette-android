package org.zxcv.chainadapter.delegate;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public interface AdapterDelegate<VH extends RecyclerView.ViewHolder> {

	int getItemCount();

	int getItemViewType(int position);

	long getItemId(int position);

	@NonNull
	VH createViewHolder(@NonNull ViewGroup parent, int viewType);

	void bindViewHolder(@NonNull VH holder, int position);

}
