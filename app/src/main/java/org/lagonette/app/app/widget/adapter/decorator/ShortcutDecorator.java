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
import org.lagonette.app.room.entity.statement.Shortcut;
import org.lagonette.app.room.statement.Statement;
import org.zxcv.chainadapter.decorator.SimpleAdapterDecorator;
import org.zxcv.functions.main.Consumer;

public class ShortcutDecorator
		extends SimpleAdapterDecorator<ShortcutViewHolder, Shortcut> {

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
			@Nullable Shortcut shortcut,
			@NonNull ShortcutViewHolder holder) {
		if (shortcut != null) {
			holder.locationImageView.setImageResource(
					shortcut.isPartnerShortcutSelected()
							? R.drawable.bg_item_partner
							: R.drawable.bg_item_partner_unselected
			);
			holder.exchangeOfficeImageView.setImageResource(
					shortcut.isExchangeOfficeShortcutSelected()
							? R.drawable.bg_item_exchange_office
							: R.drawable.bg_item_exchange_office_unselected
			);
			holder.headquarterView.setTag(shortcut.headquarterLocationId);
			holder.headquarterView.setClickable(true);
			holder.backgroundHeadquarterView.setBackgroundResource(R.drawable.bg_item_partner);
			holder.textHeadquarterView.setTextColor(mSecondaryTextColor);
			Glide.with(holder.itemView.getContext())
					.load(shortcut.headquarterIcon)
					.asBitmap()
					.placeholder(R.drawable.img_item_default)
					.override(mCategoryIconSize, mCategoryIconSize)
					.into(holder.iconHeadquarterView);
		}
		else {
			holder.locationImageView.setImageResource(R.drawable.bg_item_category);
			holder.exchangeOfficeImageView.setImageResource(R.drawable.bg_item_category);
			holder.headquarterView.setTag(Statement.NO_ID);
			holder.headquarterView.setClickable(false);
			holder.backgroundHeadquarterView.setBackgroundResource(R.drawable.bg_item_category);
			holder.textHeadquarterView.setTextColor(mDisabledTextColor);
			holder.iconHeadquarterView.setImageBitmap(null);
		}
	}
}
