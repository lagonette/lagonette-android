package org.lagonette.app.app.widget.adapter.decorator;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import org.lagonette.app.R;
import org.lagonette.app.app.widget.viewholder.ShortcutViewHolder;
import org.lagonette.app.room.entity.statement.HeadquarterShortcut;
import org.lagonette.app.room.statement.Statement;
import org.lagonette.app.tools.chainadapter.decorator.SimpleAdapterDecorator;
import org.lagonette.app.tools.functions.main.Consumer;

public class ShortcutDecorator
		extends SimpleAdapterDecorator<ShortcutViewHolder, HeadquarterShortcut> {

	public static class Callbacks {

		@Nullable
		public Runnable onLocationClick;

		@Nullable
		public Runnable onExchangeOfficeClick;

		@Nullable
		public Consumer<Long> onHeadquarterClick;

	}

	@NonNull
	private final Callbacks mCallbacks;

	private final int mCategoryIconSize;

	private int mDisabledTextColor;

	private int mSecondaryTextColor;

	public ShortcutDecorator(
			@NonNull Context context,
			@NonNull Resources resources,
			@NonNull Callbacks callbacks) {
		super(R.id.view_type_shortcut);

		mCallbacks = callbacks;

		mCategoryIconSize = resources.getDimensionPixelSize(R.dimen.filters_category_icon_size);

		mDisabledTextColor = ContextCompat.getColor(context, R.color.text_disabled);
		mSecondaryTextColor = ContextCompat.getColor(context, R.color.text_secondary);
	}

	@NonNull
	@Override
	public ShortcutViewHolder createViewHolder(@NonNull ViewGroup parent, int viewType) {
		ShortcutViewHolder holder = new ShortcutViewHolder(parent);

		if (mCallbacks.onLocationClick != null) {
			holder.locationView.setOnClickListener(view -> mCallbacks.onLocationClick.run());
		}

		if (mCallbacks.onExchangeOfficeClick != null) {
			holder.exchangeOfficeView.setOnClickListener(view -> mCallbacks.onExchangeOfficeClick.run());
		}

		if (mCallbacks.onHeadquarterClick != null) {
			holder.headquarterView.setOnClickListener(view -> mCallbacks.onHeadquarterClick.accept((Long) holder.headquarterView.getTag()));
		}

		return holder;
	}

	@Override
	protected void onBindViewHolder(
			@Nullable HeadquarterShortcut headquarterShortcut,
			@NonNull ShortcutViewHolder holder) {
		if (headquarterShortcut != null) {
			holder.headquarterView.setTag(headquarterShortcut.locationId);
			holder.headquarterView.setClickable(true);
			holder.backgroundHeadquarterView.setBackgroundResource(R.drawable.bg_item_partner);
			holder.textHeadquarterView.setTextColor(mSecondaryTextColor);
			Glide.with(holder.itemView.getContext())
					.load(headquarterShortcut.icon)
					.asBitmap()
					.override(mCategoryIconSize, mCategoryIconSize)
					.into(holder.iconHeadquarterView);
		}
		else {
			holder.headquarterView.setTag(Statement.NO_ID);
			holder.headquarterView.setClickable(false);
			holder.backgroundHeadquarterView.setBackgroundResource(R.drawable.bg_item_category);
			holder.textHeadquarterView.setTextColor(mDisabledTextColor);
			holder.iconHeadquarterView.setImageBitmap(null);
		}
	}
}
