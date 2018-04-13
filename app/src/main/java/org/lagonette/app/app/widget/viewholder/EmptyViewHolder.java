package org.lagonette.app.app.widget.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.lagonette.app.R;

public class EmptyViewHolder
		extends RecyclerView.ViewHolder {

	public EmptyViewHolder(@NonNull ViewGroup parent) {
		super(
				LayoutInflater
						.from(parent.getContext())
						.inflate(
								R.layout.row_empty_view,
								parent,
								false
						)
		);
	}
}
