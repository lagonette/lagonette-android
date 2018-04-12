package org.lagonette.app.tools.chainadapter.locator;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import org.lagonette.app.tools.chainadapter.component.AdapterComponent;
import org.lagonette.app.tools.chainadapter.link.AdapterLink;

public class HeaderLocator<VH extends RecyclerView.ViewHolder>
		implements AdapterLocator<VH> {

	@Override
	public boolean handlePosition(
			int position,
			@NonNull AdapterComponent component,
			@NonNull AdapterLink nextLink) {
		int componentItemCount = component.getItemCount();
		return position < componentItemCount;
	}

	@Override
	public int calculateInsertedBeforeCount(
			int position,
			@NonNull AdapterComponent component,
			@NonNull AdapterLink nextLink) {
		int componentItemCount = component.getItemCount();
		if (position < componentItemCount) {
			return position;
		}
		else {
			return componentItemCount;
		}
	}

	@Override
	public int getItemCount(
			int nextItemCount,
			@NonNull AdapterComponent component,
			@NonNull AdapterLink nextLink) {
		return component.getItemCount();
	}
}
