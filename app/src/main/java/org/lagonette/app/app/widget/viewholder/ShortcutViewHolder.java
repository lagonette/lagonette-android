package org.lagonette.app.app.widget.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.lagonette.app.R;

public class ShortcutViewHolder
		extends RecyclerView.ViewHolder {

	@NonNull
	public final View locationView;

	@NonNull
	public final View exchangeOfficeView;

	@NonNull
	public final View headquarterView;

	@NonNull
	public final View backgroundHeadquarterView;

	@NonNull
	public final ImageView iconHeadquarterView;

	@NonNull
	public final TextView textHeadquarterView;

	public ShortcutViewHolder(
			@NonNull ViewGroup parent) {
		super(
				LayoutInflater
						.from(parent.getContext())
						.inflate(
								R.layout.row_shortcut,
								parent,
								false
						)
		);

		locationView = itemView.findViewById(R.id.layout_shortcut_location);

		exchangeOfficeView = itemView.findViewById(R.id.layout_shortcut_exchange_office);

		headquarterView = itemView.findViewById(R.id.layout_shortcut_headquarter);
		backgroundHeadquarterView = itemView.findViewById(R.id.layout_shortcut_headquarter_background);
		iconHeadquarterView = itemView.findViewById(R.id.view_shortcut_headquarter_icon);
		textHeadquarterView = itemView.findViewById(R.id.view_shortcut_headquarter_text);
	}
}