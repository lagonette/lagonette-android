package org.lagonette.app.app.widget.viewpager;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class PageHolder {

	@NonNull
	public final View itemView;

	public PageHolder(@NonNull ViewGroup parent, @LayoutRes int layoutRes) {
		this.itemView = LayoutInflater
				.from(parent.getContext())
				.inflate(
						layoutRes,
						parent,
						false
				);
	}
}
