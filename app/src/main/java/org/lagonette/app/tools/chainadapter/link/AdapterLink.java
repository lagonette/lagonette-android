package org.lagonette.app.tools.chainadapter.link;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import org.lagonette.app.tools.chainadapter.delegate.AdapterDelegate;

public interface AdapterLink<VH extends RecyclerView.ViewHolder>
		extends AdapterDelegate<VH> {

	int getRank();

	@Nullable
	AdapterLink<VH> chainUp(@NonNull AdapterLink<VH> link);

	@Nullable
	AdapterLink<VH> unchain(@NonNull AdapterLink<VH> link);

	@Nullable
	AdapterLink<VH> unchain(int rank);

	boolean isChained(int rank);
}
