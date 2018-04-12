package org.lagonette.app.app.widget.adapter.decorator;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import org.lagonette.app.R;
import org.lagonette.app.app.widget.viewholder.CategoryViewHolder;
import org.lagonette.app.room.entity.statement.Filter;
import org.lagonette.app.room.statement.FilterStatement;
import org.lagonette.app.tools.chainadapter.decorator.AbstractAdapterDecorator;
import org.lagonette.app.tools.functions.main.LongBooleanConsumer;

public class CategoryDecorator
		extends AbstractAdapterDecorator<CategoryViewHolder, Filter> {

	public static class Callbacks {

		@Nullable
		public LongBooleanConsumer onCollapsedClick;

		@Nullable
		public LongBooleanConsumer onVisibilityClick;

	}

	@NonNull
	private final Callbacks mCallbacks;

	private final int mCategoryIconSize;

	public CategoryDecorator(
			@NonNull Resources resources,
			@NonNull Callbacks callbacks) {
		super(R.id.view_type_category);

		mCallbacks = callbacks;

		mCategoryIconSize = resources.getDimensionPixelSize(R.dimen.filters_category_icon_size);
	}

	@NonNull
	@Override
	public CategoryViewHolder createViewHolder(@NonNull ViewGroup parent) {
		CategoryViewHolder holder = new CategoryViewHolder(parent);

		if (mCallbacks.onCollapsedClick != null) {
			holder.collapsedButton.setOnClickListener(view -> mCallbacks.onCollapsedClick.accept(holder.categoryId, !holder.isCollapsed));
		}

		if (mCallbacks.onVisibilityClick != null) {
			holder.visibilityButton.setOnClickListener(view -> mCallbacks.onVisibilityClick.accept(holder.categoryId, !holder.isVisible));
		}

		return holder;
	}

	@Override
	public boolean handleItem(@Nullable Filter filter) {
		return filter != null && filter.rowType == FilterStatement.VALUE_ROW_CATEGORY;
	}

	@Override
	protected void onBindViewHolder(@Nullable Filter filter, @NonNull CategoryViewHolder holder) {
		if (filter != null) {
			holder.categoryId = filter.categoryId;
			holder.isPartnersVisible = filter.isCategoryPartnersVisible;
			holder.isVisible = filter.isCategoryVisible;
			holder.isCollapsed = filter.isCategoryCollapsed;

			if (holder.isVisible && holder.isPartnersVisible) {
				holder.visibilityButton.setImageResource(R.drawable.ic_visibility_accent_24dp);
			}
			else if (holder.isVisible) {
				holder.visibilityButton.setImageResource(R.drawable.ic_visibility_grey_24dp);
			}
			else {
				holder.visibilityButton.setImageResource(R.drawable.ic_visibility_off_grey_24dp);
			}

			if (holder.isCollapsed) {
				holder.collapsedButton.setImageResource(R.drawable.ic_expand_more_grey_24dp);
			}
			else {
				holder.collapsedButton.setImageResource(R.drawable.ic_expand_less_grey_24dp);
			}

			holder.categoryTextView.setText(filter.categoryLabel);

			Glide.with(holder.itemView.getContext())
					.load(filter.categoryIcon)
					.asBitmap()
					.override(mCategoryIconSize, mCategoryIconSize)
					.placeholder(R.drawable.img_item_default)
					.into(holder.iconImageView);
		}
	}
}
