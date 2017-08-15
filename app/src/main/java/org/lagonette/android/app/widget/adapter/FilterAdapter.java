package org.lagonette.android.app.widget.adapter;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.lagonette.android.R;
import org.lagonette.android.room.reader.FilterReader;
import org.lagonette.android.room.statement.FilterStatement;
import org.lagonette.android.util.AdapterUtil;

public class FilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnFilterClickListener {

        void onPartnerClick(@NonNull PartnerViewHolder holder);

        void onCategoryVisibilityClick(@NonNull CategoryViewHolder holder);

        void onCategoryCollapsedClick(@NonNull CategoryViewHolder holder);

        void onPartnerVisibilityClick(@NonNull PartnerViewHolder holder);

    }

    private static final String TAG = "FilterAdapter";

    private static final int LOADING_COUNT = 1;

    private static final int SHORTCUT_COUNT = 1;

    private static final int ROW_TYPE_LOADING = FilterStatement.ROW_TYPE_COUNT + 1;

    private static final int ROW_TYPE_SHORTCUT = ROW_TYPE_LOADING + 1;

    public static final int ROW_TYPE_COUNT = FilterStatement.ROW_TYPE_COUNT + 2;

    @NonNull
    private final StringBuilder mStringBuilder;

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
    private OnFilterClickListener mOnFilterClickListener;

    public FilterAdapter(@NonNull Context context, @NonNull Resources resources, @Nullable OnFilterClickListener onFilterClickListener) {
        mStringBuilder = new StringBuilder();
        mOnFilterClickListener = onFilterClickListener;

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
                        return R.id.view_type_partner;
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
            return AdapterUtil.createItemId(
                    ROW_TYPE_LOADING,
                    ROW_TYPE_COUNT,
                    0
            );
        } else if (position < SHORTCUT_COUNT) {
            return AdapterUtil.createItemId(
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
                        return AdapterUtil.createItemId(
                                rowType,
                                ROW_TYPE_COUNT,
                                mFilterReader.getCategoryId()
                        );
                    case FilterStatement.VALUE_ROW_MAIN_PARTNER:
                        return AdapterUtil.createItemId(
                                rowType,
                                ROW_TYPE_COUNT,
                                mFilterReader.getPartnerId()
                        );
                    case FilterStatement.VALUE_ROW_SIDE_PARTNER:
                        return AdapterUtil.createItemId(
                                rowType,
                                ROW_TYPE_COUNT,
                                mFilterReader.getPartnerId()
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
                return onCreateShortcutViewHolder(parent);
            case R.id.view_type_category:
                return onCreateCategoryViewHolder(parent);
            case R.id.view_type_partner:
                return onCreatePartnerViewHolder(parent);
            case R.id.view_type_footer:
                return onCreateFooterViewHolder(parent);
            case R.id.view_type_loading:
                return onCreateLoadingViewHolder(parent);
            default:
                throw new IllegalArgumentException("Unknown view type:" + viewType);
        }
    }

    private ShortcutViewHolder onCreateShortcutViewHolder(@NonNull ViewGroup parent) {
        return new ShortcutViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.row_shortcut,
                                parent,
                                false
                        )
        );
    }

    private LoadingViewHolder onCreateLoadingViewHolder(@NonNull ViewGroup parent) {
        return new LoadingViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.row_loading,
                                parent,
                                false
                        )
        );
    }

    private FooterViewHolder onCreateFooterViewHolder(@NonNull ViewGroup parent) {
        return new FooterViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.row_footer,
                                parent,
                                false
                        )
        );
    }

    private PartnerViewHolder onCreatePartnerViewHolder(@NonNull ViewGroup parent) {
        PartnerViewHolder holder = new PartnerViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.row_partner,
                                parent,
                                false
                        )
        );

        if (mOnFilterClickListener != null) {
            holder.itemView.setTag(holder);
            holder.itemView.setOnClickListener(
                    v -> mOnFilterClickListener.onPartnerClick((PartnerViewHolder) v.getTag())
            );

            holder.visibilityButton.setTag(holder);
            holder.visibilityButton.setOnClickListener(
                    v -> mOnFilterClickListener.onPartnerVisibilityClick((PartnerViewHolder) v.getTag())
            );
        }

        return holder;
    }

    private CategoryViewHolder onCreateCategoryViewHolder(@NonNull ViewGroup parent) {
        CategoryViewHolder holder = new CategoryViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.row_category,
                                parent,
                                false
                        )
        );

        if (mOnFilterClickListener != null) {
            holder.visibilityButton.setTag(holder);
            holder.visibilityButton.setOnClickListener(
                    v -> mOnFilterClickListener.onCategoryVisibilityClick((CategoryViewHolder) v.getTag())
            );

            holder.collapsedButton.setTag(holder);
            holder.collapsedButton.setOnClickListener(
                    v -> mOnFilterClickListener.onCategoryCollapsedClick((CategoryViewHolder) v.getTag())
            );
        }

        return holder;
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
            case R.id.view_type_partner:
                onBindPartnerViewHolder((PartnerViewHolder) holder, position - SHORTCUT_COUNT);
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
            holder.categoryId = mFilterReader.getCategoryId();
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

    private void onBindPartnerViewHolder(@NonNull PartnerViewHolder holder, int position) {
        if (mFilterReader.moveToPosition(position)) {
            holder.partnerId = mFilterReader.getPartnerId();
            holder.isVisible = mFilterReader.isPartnerVisible();
            holder.isCategoryVisible = mFilterReader.isCategoryVisible();
            holder.isExchangeOffice = mFilterReader.isPartnerExchangeOffice();
            holder.isMainPartner = mFilterReader.getRowType() == FilterStatement.VALUE_ROW_MAIN_PARTNER;

            holder.nameTextView.setText(mFilterReader.getPartnerName());
            holder.itemView.setClickable(holder.isVisible);

            String address = mFilterReader.getPartnerAddress();
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

    public class PartnerViewHolder extends RecyclerView.ViewHolder {

        public long partnerId;

        public boolean isVisible;

        public boolean isCategoryVisible;

        public boolean isMainPartner;

        public boolean isExchangeOffice;

        @NonNull
        public final TextView nameTextView;

        @NonNull
        public final TextView addressTextView;

        @NonNull
        public final ImageView exchangeOfficeIndicatorImage;

        @NonNull
        public final ImageButton visibilityButton;

        public PartnerViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.partner_name);
            addressTextView = itemView.findViewById(R.id.partner_address);
            exchangeOfficeIndicatorImage = itemView.findViewById(R.id.partner_exchange_office_indicator);
            visibilityButton = itemView.findViewById(R.id.partner_visibility);
        }
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        public long categoryId;

        public boolean isVisible;

        public boolean isPartnersVisible;

        public boolean isCollapsed;

        @NonNull
        public final ImageView iconImageView;

        @NonNull
        public final ImageButton visibilityButton;

        @NonNull
        public final ImageButton collapsedButton;

        @NonNull
        public final TextView categoryTextView;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.category_icon);
            visibilityButton = itemView.findViewById(R.id.category_visibility);
            collapsedButton = itemView.findViewById(R.id.category_collapsed);
            categoryTextView = itemView.findViewById(R.id.category_label);
        }
    }

    public class ShortcutViewHolder extends RecyclerView.ViewHolder {

        public ShortcutViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }

    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

}
