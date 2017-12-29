package org.lagonette.app.app.widget.adapter;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
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
import org.lagonette.app.room.reader.FilterReader;
import org.lagonette.app.room.statement.FilterStatement;
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

    @ColorInt
    private final int mPartnerMainBackgroundColor;

    @ColorInt
    private final int mPartnerSideBackgroundColor;

    private final int mCategoryIconSize;

    @Nullable
    private FilterReader mFilterReader;

    @Nullable
    private LocationViewHolder.OnClickListener mOnLocationClickListener;

    @Nullable
    private LocationViewHolder.OnVisibilityClickListener mOnLocationVisibilityClickListener;

    @Nullable
    private CategoryViewHolder.OnCollapsedClickListener mOnCategoryCollapsedClickListener;

    @Nullable
    private CategoryViewHolder.OnVisibilityClickListener mOnCategoryVisibilityClickListener;

    @Nullable
    private ShortcutViewHolder.OnLocationClickListener mOnLocationShortcutClickListener;

    @Nullable
    private ShortcutViewHolder.OnExchangeOfficeClickListener mOnExchangeOfficeShortcutClickListener;

    @Nullable
    private ShortcutViewHolder.OnOfficeClickListener mOfficeShortcutClickListener;

    public FilterAdapter(@NonNull Context context, @NonNull Resources resources) {
        mCategoryIconSize = resources.getDimensionPixelSize(R.dimen.filters_category_icon_size);

        mPartnerMainBackgroundColor = ContextCompat.getColor(context, R.color.row_partner_main_background);
        mPartnerSideBackgroundColor = ContextCompat.getColor(context, R.color.row_partner_side_background);

        mPartnerIndicatorImage = ContextCompat.getDrawable(context, R.drawable.img_partner_indicator);
        mExchangeOfficeIndicatorImage = ContextCompat.getDrawable(context, R.drawable.img_exchange_office_indicator);
    }

    @Override
    public int getItemCount() {
        if (mFilterReader == null) {
            return LOADING_COUNT;
        } else {
            return mFilterReader.getCount();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mFilterReader == null) {
            return R.id.view_type_loading;
        } else if (position < SHORTCUT_COUNT) {
            return R.id.view_type_shortcut;
        }
        position -= SHORTCUT_COUNT;
        if (position < mFilterReader.getCount()) {
            if (mFilterReader.moveToPosition(position)) {
                @FilterStatement.RowType
                int rowType = mFilterReader.getRowType();
                switch (rowType) {
                    case FilterStatement.VALUE_ROW_CATEGORY:
                        return R.id.view_type_category;
                    case FilterStatement.VALUE_ROW_FOOTER:
                        return R.id.view_type_footer;
                    case FilterStatement.VALUE_ROW_MAIN_PARTNER:
                    case FilterStatement.VALUE_ROW_SIDE_PARTNER:
                        return R.id.view_type_location;
                    default:
                        throw new IllegalStateException("Filter row must be a PARTNER or a CATEGORY.");
                }
            } else {
                throw new IllegalStateException("Cursor can not reach position " + position);
            }
        } else {
            throw new IllegalStateException("Try to reach a position(" + position + ") out of bounds");
        }
    }

    @Override
    public long getItemId(int position) {
        if (mFilterReader == null) {
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
        if (position < mFilterReader.getCount()) {
            if (mFilterReader.moveToPosition(position)) {
                @FilterStatement.RowType
                int rowType = mFilterReader.getRowType();
                switch (rowType) {
                    case FilterStatement.VALUE_ROW_CATEGORY:
                    case FilterStatement.VALUE_ROW_FOOTER:
                        return AdapterUtils.createItemId(
                                rowType,
                                ROW_TYPE_COUNT,
                                mFilterReader.getCategoryKey().getUniqueId()
                        );
                    case FilterStatement.VALUE_ROW_MAIN_PARTNER:
                        return AdapterUtils.createItemId(
                                rowType,
                                ROW_TYPE_COUNT,
                                mFilterReader.getLocationId()
                        );
                    case FilterStatement.VALUE_ROW_SIDE_PARTNER:
                        return AdapterUtils.createItemId(
                                rowType,
                                ROW_TYPE_COUNT,
                                mFilterReader.getLocationId()
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {

            case R.id.view_type_shortcut:
                return new ShortcutViewHolder(parent)
                        .setOnLocationClick(mOnLocationShortcutClickListener)
                        .setOnExchangeOfficeClick(mOnExchangeOfficeShortcutClickListener)
                        .setOnOfficeClick(mOfficeShortcutClickListener);

            case R.id.view_type_category:
                return new CategoryViewHolder(parent)
                        .setOnCollapsedClick(mOnCategoryCollapsedClickListener)
                        .setOnVisibilityClick(mOnCategoryVisibilityClickListener);

            case R.id.view_type_location:
                return new LocationViewHolder(parent)
                        .setOnLocationClick(mOnLocationClickListener)
                        .setOnVisibilityClick(mOnLocationVisibilityClickListener);

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

    private void onBindShortcutViewHolder(@NonNull ShortcutViewHolder holder, int position) {

    }

    private void onBindCategoryViewHolder(@NonNull CategoryViewHolder holder, int position) {
        if (mFilterReader.moveToPosition(position)) {
            holder.categoryKey = mFilterReader.getCategoryKey(holder.categoryKey);
            holder.isPartnersVisible = mFilterReader.isCategoryPartnersVisible();
            holder.isVisible = mFilterReader.isCategoryVisible();
            holder.isCollapsed = mFilterReader.isCategoryCollapsed();

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

            holder.categoryTextView.setText(mFilterReader.getCategoryLabel());

            Glide.with(holder.itemView.getContext())
                    .load(mFilterReader.getCategoryIcon())
                    .asBitmap()
                    .override(mCategoryIconSize, mCategoryIconSize)
                    .placeholder(R.drawable.img_item_default)
                    .into(holder.iconImageView);
        }
    }

    private void onBindLocationViewHolder(@NonNull LocationViewHolder holder, int position) {
        if (mFilterReader.moveToPosition(position)) {
            holder.locationId = mFilterReader.getLocationId();
            holder.isVisible = mFilterReader.isLocationVisible();
            holder.isCategoryVisible = mFilterReader.isCategoryVisible();
            holder.isExchangeOffice = mFilterReader.isLocationExchangeOffice();
            holder.isMainPartner = mFilterReader.getRowType() == FilterStatement.VALUE_ROW_MAIN_PARTNER;

            holder.nameTextView.setText(mFilterReader.getPartnerName());
            holder.itemView.setClickable(holder.isVisible);

            String address = mFilterReader.getLocationAddress();
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

            if (holder.isMainPartner) {
                holder.itemView.setBackgroundColor(mPartnerMainBackgroundColor);
            } else {
                holder.itemView.setBackgroundColor(mPartnerSideBackgroundColor);
            }

            if (holder.isExchangeOffice) {
                holder.exchangeOfficeIndicatorImage.setImageDrawable(mExchangeOfficeIndicatorImage);
            } else {
                holder.exchangeOfficeIndicatorImage.setImageDrawable(mPartnerIndicatorImage);
            }
        }
    }

    public void setFilterReader(@Nullable FilterReader filterReader) {
        if (mFilterReader == filterReader) {
            return;
        }
        mFilterReader = filterReader;
        notifyDataSetChanged();
    }

    public void setOnLocationClickListener(@Nullable LocationViewHolder.OnClickListener onLocationClickListener) {
        mOnLocationClickListener = onLocationClickListener;
    }

    public void setOnLocationVisibilityClickListener(@Nullable LocationViewHolder.OnVisibilityClickListener onLocationVisibilityClickListener) {
        this.mOnLocationVisibilityClickListener = onLocationVisibilityClickListener;
    }

    public void setOnCategoryCollapsedClickListener(@Nullable CategoryViewHolder.OnCollapsedClickListener onCategoryCollapsedClickListener) {
        this.mOnCategoryCollapsedClickListener = onCategoryCollapsedClickListener;
    }

    public void setOnCategoryVisibilityClickListener(@Nullable CategoryViewHolder.OnVisibilityClickListener onCategoryVisibilityClickListener) {
        this.mOnCategoryVisibilityClickListener = onCategoryVisibilityClickListener;
    }

    public void setOnLocationShortcutClickListener(@Nullable ShortcutViewHolder.OnLocationClickListener locationShortcutClickListener) {
        mOnLocationShortcutClickListener = locationShortcutClickListener;
    }

    public void setOnExchangeOfficeShortcutClickListener(@Nullable ShortcutViewHolder.OnExchangeOfficeClickListener exchangeOfficeShortcutClickListener) {
        mOnExchangeOfficeShortcutClickListener = exchangeOfficeShortcutClickListener;
    }

    public void setOnOfficeShortcutClickListener(@Nullable ShortcutViewHolder.OnOfficeClickListener officeShortcutClickListener) {
        mOfficeShortcutClickListener = officeShortcutClickListener;
    }

}
