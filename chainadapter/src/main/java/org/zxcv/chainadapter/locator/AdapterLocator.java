package org.zxcv.chainadapter.locator;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import org.zxcv.chainadapter.component.AdapterComponent;
import org.zxcv.chainadapter.link.AdapterLink;

public interface AdapterLocator<VH extends RecyclerView.ViewHolder> {

	boolean handlePosition(
			int position,
			@NonNull AdapterComponent<VH> component,
			@NonNull AdapterLink<VH> nextLink);

	int calculateInsertedBeforeCount(
			int position,
			@NonNull AdapterComponent<VH> component,
			@NonNull AdapterLink<VH> nextLink);

	int getItemCount(
			int nextItemCount,
			@NonNull AdapterComponent<VH> component,
			@NonNull AdapterLink<VH> nextLink);

}
