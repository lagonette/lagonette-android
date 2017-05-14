package org.lagonette.android.app.widget.adapter;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.lagonette.android.R;
import org.lagonette.android.content.contract.GonetteContract;
import org.lagonette.android.content.reader.FilterReader;

public class FilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnPartnerClickListener {

        void onPartnerClick(@NonNull MainPartnerViewHolder holder);

        void onCategoryClick(@NonNull FilterAdapter.CategoryViewHolder holder);

        void onPartnerVisibilityClick(@NonNull MainPartnerViewHolder holder);

    }

    private static final String TAG = "FilterAdapter";

    private static final int LOADING_ID = -1;

    private static final int OFFSET_CATEGORY_ID = LOADING_ID;

    private static final int LOADING_COUNT = 1;

    @Nullable
    private FilterReader mFilterReader;

    @Nullable
    private OnPartnerClickListener mOnPartnerClickListener;

    public FilterAdapter(@Nullable OnPartnerClickListener onPartnerClickListener/*, @NonNull String search*/) {
        mOnPartnerClickListener = onPartnerClickListener;
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
        } else if (position < mFilterReader.getCount()) {
            if (mFilterReader.moveToPosition(position)) {
                int rowType = mFilterReader.getRowType();
                if (rowType == GonetteContract.Filter.VALUE_ROW_CATEGORY) {
                    return R.id.view_type_category;
                } else if (rowType == GonetteContract.Filter.VALUE_ROW_MAIN_PARTNER) {
                    return R.id.view_type_partner_main;
                } else if (rowType == GonetteContract.Filter.VALUE_ROW_SIDE_PARTNER) {
                    return R.id.view_type_partner_side;
                } else if (rowType == GonetteContract.Filter.VALUE_ROW_FOOTER) {
                    return R.id.view_type_footer;
                } else {
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
            return LOADING_ID;
        } else if (position < mFilterReader.getCount()) {
            if (mFilterReader.moveToPosition(position)) {
                int rowType = mFilterReader.getRowType();
                if (rowType == GonetteContract.Filter.VALUE_ROW_CATEGORY) {
                    return OFFSET_CATEGORY_ID - (mFilterReader.categoryReader.getId() * 2);
                } else if (rowType == GonetteContract.Filter.VALUE_ROW_FOOTER) {
                    return OFFSET_CATEGORY_ID - (mFilterReader.categoryReader.getId() * 2 + 1);
                } else if (rowType == GonetteContract.Filter.VALUE_ROW_MAIN_PARTNER) {
                    return mFilterReader.partnerReader.getId() * 2;
                } else if (rowType == GonetteContract.Filter.VALUE_ROW_SIDE_PARTNER) {
                    return mFilterReader.partnerReader.getId() * 2 + 1;
                } else {
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
            case R.id.view_type_category:
                return onCreateCategoryViewHolder(parent);
            case R.id.view_type_partner_main:
                return onCreateMainPartnerViewHolder(parent);
            case R.id.view_type_partner_side:
                return onCreateSidePartnerViewHolder(parent);
            case R.id.view_type_footer:
                return onCreateFooterViewHolder(parent);
            case R.id.view_type_loading:
                return onCreateLoadingViewHolder(parent);
            default:
                throw new IllegalArgumentException("Unknown view type:" + viewType);
        }
    }

    private LoadingViewHolder onCreateLoadingViewHolder(ViewGroup parent) {
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

    private FooterViewHolder onCreateFooterViewHolder(ViewGroup parent) {
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

    private MainPartnerViewHolder onCreateMainPartnerViewHolder(ViewGroup parent) {
        MainPartnerViewHolder holder = new MainPartnerViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.row_partner_main,
                                parent,
                                false
                        )
        );

        if (mOnPartnerClickListener != null) {
            holder.itemView.setTag(holder);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnPartnerClickListener.onPartnerClick((MainPartnerViewHolder) v.getTag());
                }
            });

            holder.visibilityButton.setTag(holder);
            holder.visibilityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnPartnerClickListener.onPartnerVisibilityClick((MainPartnerViewHolder) v.getTag());
                }
            });
        }

        return holder;
    }

    private SidePartnerViewHolder onCreateSidePartnerViewHolder(ViewGroup parent) {
        SidePartnerViewHolder holder = new SidePartnerViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.row_partner_side,
                                parent,
                                false
                        )
        );

        if (mOnPartnerClickListener != null) {
            holder.itemView.setTag(holder);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnPartnerClickListener.onPartnerClick((MainPartnerViewHolder) v.getTag());
                }
            });

            holder.visibilityButton.setTag(holder);
            holder.visibilityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnPartnerClickListener.onPartnerVisibilityClick((MainPartnerViewHolder) v.getTag());
                }
            });
        }

        return holder;
    }

    private CategoryViewHolder onCreateCategoryViewHolder(ViewGroup parent) {
        CategoryViewHolder holder = new CategoryViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.row_category,
                                parent,
                                false
                        )
        );

        holder.expandButton.setTag(holder);
        holder.expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
            }
        });

        if (mOnPartnerClickListener != null) {
            holder.visibilityButton.setTag(holder);
            holder.visibilityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnPartnerClickListener.onCategoryClick((CategoryViewHolder) v.getTag());
                }
            });
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case R.id.view_type_category:
                onBindCategoryViewHolder((CategoryViewHolder) holder, position);
                break;
            case R.id.view_type_partner_main:
                onBindMainPartnerViewHolder((MainPartnerViewHolder) holder, position);
                break;
            case R.id.view_type_partner_side:
                onBindSidePartnerViewHolder((SidePartnerViewHolder) holder, position);
                break;
            case R.id.view_type_footer:
            case R.id.view_type_loading:
                break;
            default:
                throw new IllegalArgumentException("Unknown view type:" + viewType);
        }
    }

    private void onBindCategoryViewHolder(@NonNull CategoryViewHolder holder, int position) {
        if (mFilterReader.moveToPosition(position)) {
            // TODO
            if (holder.isVisible) {
                holder.visibilityButton.setImageResource(R.drawable.ic_visibility_accent_24dp);
            } else {
                holder.visibilityButton.setImageResource(R.drawable.ic_visibility_off_grey_24dp);
            }

            // TODO
            if (holder.isExpanded) {
                holder.expandButton.setImageResource(R.drawable.ic_expand_less_grey_24dp);
            } else {
                holder.expandButton.setImageResource(R.drawable.ic_expand_more_grey_24dp);
            }

            holder.categoryTextView.setText(mFilterReader.categoryReader.getLabel());
        }
    }

    private void onBindMainPartnerViewHolder(@NonNull MainPartnerViewHolder holder, int position) {
        if (mFilterReader.moveToPosition(position)) {
            holder.partnerId = mFilterReader.partnerReader.getId();
            holder.isVisible = mFilterReader.partnerReader.isVisible();
            holder.nameTextView.setText(mFilterReader.partnerReader.getName());
            holder.itemView.setClickable(holder.isVisible);
            if (holder.isVisible) {
                holder.visibilityButton.setImageResource(R.drawable.ic_visibility_accent_24dp);
            } else {
                holder.visibilityButton.setImageResource(R.drawable.ic_visibility_off_grey_24dp);
            }
        }
    }

    private void onBindSidePartnerViewHolder(@NonNull SidePartnerViewHolder holder, int position) {
        if (mFilterReader.moveToPosition(position)) {
            holder.partnerId = mFilterReader.partnerReader.getId();
            holder.isVisible = mFilterReader.partnerReader.isVisible();
            holder.nameTextView.setText(mFilterReader.partnerReader.getName());
            holder.itemView.setClickable(holder.isVisible);
            if (holder.isVisible) {
                holder.visibilityButton.setImageResource(R.drawable.ic_visibility_accent_24dp);
            } else {
                holder.visibilityButton.setImageResource(R.drawable.ic_visibility_off_grey_24dp);
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

    public class MainPartnerViewHolder extends RecyclerView.ViewHolder {

        public long partnerId;

        public boolean isVisible;

        public final TextView nameTextView;

        public final ImageButton visibilityButton;

        public MainPartnerViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.partner_name);
            visibilityButton = (ImageButton) itemView.findViewById(R.id.partner_visibility);
        }
    }

    public class SidePartnerViewHolder extends MainPartnerViewHolder {

        public SidePartnerViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        public boolean isVisible;

        public boolean isExpanded;

        public final ImageButton visibilityButton;

        public final ImageButton expandButton;

        public final TextView categoryTextView;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            visibilityButton = (ImageButton) itemView.findViewById(R.id.category_visibility);
            expandButton = (ImageButton) itemView.findViewById(R.id.category_expand);
            categoryTextView = (TextView) itemView.findViewById(R.id.category_label);
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
