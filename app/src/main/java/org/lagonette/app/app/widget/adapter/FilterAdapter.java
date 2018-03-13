package org.lagonette.app.app.widget.adapter;


import android.arch.paging.AsyncPagedListDiffer;
import android.arch.paging.PagedList;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import org.lagonette.app.R;
import org.lagonette.app.app.widget.viewholder.CategoryViewHolder;
import org.lagonette.app.app.widget.viewholder.FooterViewHolder;
import org.lagonette.app.app.widget.viewholder.LoadingViewHolder;
import org.lagonette.app.app.widget.viewholder.LocationViewHolder;
import org.lagonette.app.app.widget.viewholder.ShortcutViewHolder;
import org.lagonette.app.room.embedded.CategoryKey;
import org.lagonette.app.room.entity.statement.Filter;
import org.lagonette.app.room.entity.statement.HeadquarterShortcut;
import org.lagonette.app.room.statement.FilterStatement;
import org.lagonette.app.room.statement.Statement;
import org.lagonette.app.tools.functions.Consumer;
import org.lagonette.app.tools.functions.LongBooleanConsumer;
import org.lagonette.app.tools.functions.LongConsumer;
import org.lagonette.app.tools.functions.ObjBooleanConsumer;
import org.lagonette.app.util.AdapterUtils;

public class FilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "FilterAdapter";

    private static final int LOADING_COUNT = 1;

    private static final int SHORTCUT_COUNT = 1;

    private static final int ROW_TYPE_LOADING = FilterStatement.ROW_TYPE_COUNT + 1;

    private static final int ROW_TYPE_SHORTCUT = ROW_TYPE_LOADING + 1;

    public static final int ROW_TYPE_COUNT = FilterStatement.ROW_TYPE_COUNT + 2;

    @NonNull
    private final Drawable mPartnerIndicatorImage;

    @NonNull
    private final Drawable mExchangeOfficeIndicatorImage;

    private final int mCategoryIconSize;

    @NonNull
    private AsyncPagedListDiffer<Filter> mDiffer;

    @Nullable
    public LongConsumer onLocationClick;

    @Nullable
    public LongBooleanConsumer onLocationVisibilityClick;

    @Nullable
    public ObjBooleanConsumer<CategoryKey> onCategoryCollapsedClick;

    @Nullable
    public ObjBooleanConsumer<CategoryKey> onCategoryVisibilityClick;

    @Nullable
    public Runnable onLocationShortcutClick;

    @Nullable
    public Runnable onExchangeOfficeShortcutClick;

    @Nullable
    public Consumer<Long> onHeadquarterShortcutClick;

    @Nullable
    private HeadquarterShortcut mHeadquarterShortcut;

    private int mDisabledTextColor;

    private int mSecondaryTextColor;

    public FilterAdapter(@NonNull Context context, @NonNull Resources resources) {

        mDiffer = new AsyncPagedListDiffer<>(
                FilterAdapter.this,
                new Filter.DiffCallback()
        );

        mCategoryIconSize = resources.getDimensionPixelSize(R.dimen.filters_category_icon_size);

        mPartnerIndicatorImage = ContextCompat.getDrawable(context, R.drawable.img_partner_indicator);
        mExchangeOfficeIndicatorImage = ContextCompat.getDrawable(context, R.drawable.img_exchange_office_indicator);

        mDisabledTextColor = ContextCompat.getColor(context, R.color.text_disabled);
        mSecondaryTextColor = ContextCompat.getColor(context, R.color.text_secondary);
    }

    @Override
    public int getItemCount() {
        return mDiffer.getItemCount() <= 0
                ? LOADING_COUNT
                : mDiffer.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (mDiffer.getCurrentList() == null) {
            return R.id.view_type_loading;
        } else if (position < SHORTCUT_COUNT) {
            return R.id.view_type_shortcut;
        }
        position -= SHORTCUT_COUNT;
        if (position < mDiffer.getItemCount()) {
            Filter filter = mDiffer.getItem(position);
            if (filter != null) {
                switch (filter.rowType) {
                    case FilterStatement.VALUE_ROW_CATEGORY:
                        return R.id.view_type_category;
                    case FilterStatement.VALUE_ROW_FOOTER:
                        return R.id.view_type_footer;
                    case FilterStatement.VALUE_ROW_MAIN_PARTNER:
                        return R.id.view_type_location;
                    default:
                        throw new IllegalStateException("Filter row must be a PARTNER or a CATEGORY.");
                }
            } else {
                throw new IllegalStateException("There is no item at " + position);
            }
        } else {
            throw new IllegalStateException("Try to reach a position(" + position + ") out of bounds");
        }
    }

    @Override
    public long getItemId(int position) {
        if (mDiffer.getItemCount() <= 0) {
            return AdapterUtils.createItemId(
                    ROW_TYPE_LOADING,
                    ROW_TYPE_COUNT,
                    0
            );
        } else if (position < SHORTCUT_COUNT) {
            return AdapterUtils.createItemId(
                    ROW_TYPE_SHORTCUT,
                    ROW_TYPE_COUNT,
                    0
            );
        }
        position -= SHORTCUT_COUNT;
        if (position < mDiffer.getItemCount()) {
            Filter filter = mDiffer.getItem(position);
            if (filter != null) {
                switch (filter.rowType) {
                    case FilterStatement.VALUE_ROW_CATEGORY:
                    case FilterStatement.VALUE_ROW_FOOTER:
                        return AdapterUtils.createItemId(
                                filter.rowType,
                                ROW_TYPE_COUNT,
                                filter.categoryKey.getUniqueId()
                        );
                    case FilterStatement.VALUE_ROW_MAIN_PARTNER:
                        return AdapterUtils.createItemId(
                                filter.rowType,
                                ROW_TYPE_COUNT,
                                filter.locationId
                        );
                    default:
                        return RecyclerView.NO_ID;
                }
            } else {
                return RecyclerView.NO_ID;
            }
        } else {
            return RecyclerView.NO_ID;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {

            case R.id.view_type_shortcut:
                return new ShortcutViewHolder(parent, onLocationShortcutClick, onExchangeOfficeShortcutClick, onHeadquarterShortcutClick);

            case R.id.view_type_category:
                return new CategoryViewHolder(parent, onCategoryCollapsedClick, onCategoryVisibilityClick);

            case R.id.view_type_location:
                return new LocationViewHolder(parent, onLocationClick, onLocationVisibilityClick);

            case R.id.view_type_footer:
                return new FooterViewHolder(parent);

            case R.id.view_type_loading:
                return new LoadingViewHolder(parent);

            default:
                throw new IllegalArgumentException("Unknown view type:" + viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case R.id.view_type_shortcut:
                onBindShortcutViewHolder((ShortcutViewHolder) holder, position);
                break;
            case R.id.view_type_category:
                onBindCategoryViewHolder((CategoryViewHolder) holder, position - SHORTCUT_COUNT);
                break;
            case R.id.view_type_location:
                onBindLocationViewHolder((LocationViewHolder) holder, position - SHORTCUT_COUNT);
                break;
            case R.id.view_type_footer:
            case R.id.view_type_loading:
                break;
            default:
                throw new IllegalArgumentException("Unknown view type:" + viewType);
        }
    }

    // TODO Make onBind into ViewHolder
    private void onBindShortcutViewHolder(@NonNull ShortcutViewHolder holder, int position) {
        if (mHeadquarterShortcut != null) {
            holder.headquarterView.setTag(mHeadquarterShortcut.locationId);
            holder.headquarterView.setClickable(true);
            holder.backgroundHeadquarterView.setBackgroundResource(R.drawable.bg_item_partner);
            holder.textHeadquarterView.setTextColor(mSecondaryTextColor);
            Glide.with(holder.itemView.getContext())
                    .load(mHeadquarterShortcut.icon)
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

    private void onBindCategoryViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Filter filter = mDiffer.getItem(position);
        if (filter != null) {
            holder.categoryKey = filter.categoryKey;
            holder.isPartnersVisible = filter.isCategoryPartnersVisible;
            holder.isVisible = filter.isCategoryVisible;
            holder.isCollapsed = filter.isCategoryCollapsed;

            if (holder.isVisible && holder.isPartnersVisible) {
                holder.visibilityButton.setImageResource(R.drawable.ic_visibility_accent_24dp);
            } else if (holder.isVisible) {
                holder.visibilityButton.setImageResource(R.drawable.ic_visibility_grey_24dp);
            } else {
                holder.visibilityButton.setImageResource(R.drawable.ic_visibility_off_grey_24dp);
            }

            if (holder.isCollapsed) {
                holder.collapsedButton.setImageResource(R.drawable.ic_expand_more_grey_24dp);
            } else {
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

    private void onBindLocationViewHolder(@NonNull LocationViewHolder holder, int position) {
        Filter filter = mDiffer.getItem(position);
        if (filter != null) {
            holder.locationId = filter.locationId;
            holder.isVisible = filter.isLocationVisible;
            holder.isCategoryVisible = filter.isCategoryVisible;
            holder.isExchangeOffice = filter.isLocationExchangeOffice;
            holder.isMainPartner = filter.rowType == FilterStatement.VALUE_ROW_MAIN_PARTNER;

            holder.nameTextView.setText(filter.partnerName);
            holder.itemView.setClickable(holder.isVisible);

            String address = filter.address.format(holder.itemView.getResources());
            if (!TextUtils.isEmpty(address)) {
                holder.addressTextView.setText(address);
                holder.addressTextView.setVisibility(View.VISIBLE);
            } else {
                holder.addressTextView.setVisibility(View.GONE);
            }

            if (holder.isVisible && holder.isCategoryVisible) {
                holder.visibilityButton.setImageResource(R.drawable.ic_visibility_accent_24dp);
            } else if (holder.isVisible) {
                holder.visibilityButton.setImageResource(R.drawable.ic_visibility_grey_24dp);
            } else {
                holder.visibilityButton.setImageResource(R.drawable.ic_visibility_off_grey_24dp);
            }

            if (holder.isExchangeOffice) {
                holder.exchangeOfficeIndicatorImage.setImageDrawable(mExchangeOfficeIndicatorImage);
            } else {
                holder.exchangeOfficeIndicatorImage.setImageDrawable(mPartnerIndicatorImage);
            }
        }
    }

    public void setFilter(@Nullable PagedList<Filter> filters) {
        mDiffer.submitList(filters);
    }

    public void setHeadquarterShortcut(@Nullable HeadquarterShortcut headquarterShortcut) {
        mHeadquarterShortcut = headquarterShortcut;
        notifyDataSetChanged();
    }
}
