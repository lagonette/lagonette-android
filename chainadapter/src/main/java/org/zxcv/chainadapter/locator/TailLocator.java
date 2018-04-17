package org.zxcv.chainadapter.locator;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import org.zxcv.chainadapter.component.AdapterComponent;
import org.zxcv.chainadapter.link.AdapterLink;

public class TailLocator<VH extends RecyclerView.ViewHolder>
		implements AdapterLocator<VH> {

	@Override
	public boolean handlePosition(
			int position,
			@NonNull AdapterComponent<VH> component,
			@NonNull AdapterLink<VH> nextLink) {
		return position >= nextLink.getItemCount();
	}

	@Override
	public int calculateInsertedBeforeCount(
			int position,
			@NonNull AdapterComponent<VH> component,
			@NonNull AdapterLink<VH> nextLink) {
		int nextLinkItemCount = nextLink.getItemCount();
		if (position < nextLink.getItemCount()) {
			return 0;
		}
		else {
			int deltaItemCount = position - nextLinkItemCount;
			int componentItemCount = component.getItemCount();
			if (deltaItemCount < componentItemCount) {
				return deltaItemCount;
			}
			else {
				return componentItemCount;
			}
		}
	}

	@Override
	public int getItemCount(
			int nextLinkItemCount,
			@NonNull AdapterComponent<VH> component,
			@NonNull AdapterLink<VH> nextLink) {
		return component.getItemCount();
	}
}
