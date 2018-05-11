package org.lagonette.app.app.widget.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

	@NonNull
	public final ImageView locationImageView;

	@NonNull
	public final ImageView exchangeOfficeImageView;

	@NonNull
	public final ImageButton allCategoriesCollapsedButton;

	@NonNull
	public final ImageButton allCategoriesVisibleButton;

	@NonNull
	public final View divider;

	@NonNull
	public final View allCategoriesLayout;

	public boolean isAllCategoryVisible;

	public boolean isAllCategoryCollapsed;

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
		locationImageView = itemView.findViewById(R.id.layout_shortcut_location_icon);

		exchangeOfficeView = itemView.findViewById(R.id.layout_shortcut_exchange_office);
		exchangeOfficeImageView = itemView.findViewById(R.id.layout_shortcut_exchange_office_icon);

		headquarterView = itemView.findViewById(R.id.layout_shortcut_headquarter);
		backgroundHeadquarterView = itemView.findViewById(R.id.layout_shortcut_headquarter_background);
		iconHeadquarterView = itemView.findViewById(R.id.view_shortcut_headquarter_icon);
		textHeadquarterView = itemView.findViewById(R.id.view_shortcut_headquarter_text);

		divider = itemView.findViewById(R.id.shortcut_divider);

		allCategoriesLayout = itemView.findViewById(R.id.shortcut_all_categories);
		allCategoriesCollapsedButton = itemView.findViewById(R.id.shortcut_collapsed);
		allCategoriesVisibleButton = itemView.findViewById(R.id.shortcut_visibility);
	}
}