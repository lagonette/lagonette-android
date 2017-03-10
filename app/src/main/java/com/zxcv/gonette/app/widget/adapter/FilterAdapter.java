package com.zxcv.gonette.app.widget.adapter;


import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zxcv.gonette.R;
import com.zxcv.gonette.content.reader.PartnerReader;
import com.zxcv.gonette.content.reader.PartnersVisibilityReader;

public class FilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnPartnerClickListener {

        void onPartnerClick(FilterAdapter.PartnerViewHolder holder);

        void onAllPartnerVisibilityClick(FilterAdapter.AllPartnerViewHolder holder);

        void onPartnerVisibilityClick(FilterAdapter.PartnerViewHolder holder);
    }

    private static final String TAG = "FilterAdapter";

    private static final int HEADER_ID = -2;

    private static final int FOOTER_ID = -3;

    private static final int LOADING_ID = -4;

    private static final int HEADER_COUNT = 1;

    private static final int FOOTER_COUNT = 1;

    private static final int LOADING_COUNT = 1;

    @Nullable
    private PartnerReader mPartnerReader;

    @Nullable
    private PartnersVisibilityReader mPartnersVisibilityReader;

    @Nullable
    private OnPartnerClickListener mOnPartnerClickListener;

    public FilterAdapter(@Nullable OnPartnerClickListener onPartnerClickListener) {
        mOnPartnerClickListener = onPartnerClickListener;
    }

    @Override
    public int getItemViewType(int position) {

        if (mPartnersVisibilityReader == null) {
            return R.id.view_type_loading;
        } else if (mPartnerReader == null) {
            if (position < HEADER_COUNT) {
                return R.id.view_type_partner_all;
            } else {
                return R.id.view_type_loading;
            }
        } else {
            if (position < HEADER_COUNT) {
                return R.id.view_type_partner_all;
            } else if (position - HEADER_COUNT < mPartnerReader.getCount()) {
                return R.id.view_type_partner;
            } else {
                return R.id.view_type_footer;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mPartnersVisibilityReader == null) {
            return LOADING_COUNT;
        } else if (mPartnerReader == null) {
            return HEADER_COUNT + LOADING_COUNT;
        } else {
            return HEADER_COUNT + mPartnerReader.getCount() + FOOTER_COUNT;
        }
    }

    @Override
    public long getItemId(int position) {
        if (mPartnersVisibilityReader == null) {
            return LOADING_ID;
        } else if (mPartnerReader == null) {
            if (position < HEADER_COUNT) {
                return HEADER_ID;
            } else {
                return LOADING_ID;
            }
        } else {
            if (position < HEADER_COUNT) {
                return HEADER_ID;
            } else if (position - HEADER_COUNT < mPartnerReader.getCount()) {
                if (mPartnerReader.moveToPosition(position - HEADER_COUNT)) {
                    return mPartnerReader.getId();
                } else {
                    return RecyclerView.NO_ID;
                }
            } else {
                return FOOTER_ID;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case R.id.view_type_partner_all:
                return onCreateAllPartnerViewHolder(parent);
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

    private PartnerViewHolder onCreatePartnerViewHolder(ViewGroup parent) {
        PartnerViewHolder holder = new PartnerViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.row_partner,
                                parent,
                                false
                        )
        );

        if (mOnPartnerClickListener != null) {
            holder.itemView.setTag(holder);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnPartnerClickListener.onPartnerClick((PartnerViewHolder) v.getTag());
                }
            });

            holder.visibilityButton.setTag(holder);
            holder.visibilityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnPartnerClickListener.onPartnerVisibilityClick((PartnerViewHolder) v.getTag());
                }
            });
        }

        return holder;
    }

    private AllPartnerViewHolder onCreateAllPartnerViewHolder(ViewGroup parent) {
        AllPartnerViewHolder holder = new AllPartnerViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.row_all_partner,
                                parent,
                                false
                        )
        );

        if (mOnPartnerClickListener != null) {
            holder.visibilityButton.setTag(holder);
            holder.visibilityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnPartnerClickListener.onAllPartnerVisibilityClick((AllPartnerViewHolder) v.getTag());
                }
            });
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case R.id.view_type_partner_all:
                onBindAllPartnerViewHolder((AllPartnerViewHolder) holder);
                break;
            case R.id.view_type_partner:
                onBindPartnerViewHolder((PartnerViewHolder) holder, position - 1);
                break;
            case R.id.view_type_footer:
            case R.id.view_type_loading:
                break;
            default:
                throw new IllegalArgumentException("Unknown view type:" + viewType);
        }
    }

    private void onBindAllPartnerViewHolder(AllPartnerViewHolder holder) {
        if (mPartnersVisibilityReader.moveToFirst()) {
            holder.isVisible = mPartnersVisibilityReader.getPartnersVisibilityCount() > 0;
            if (holder.isVisible) {
                holder.visibilityButton.setImageResource(R.drawable.ic_visibility_accent_24dp);
            } else {
                holder.visibilityButton.setImageResource(R.drawable.ic_visibility_off_grey_24dp);
            }
        }
    }

    private void onBindPartnerViewHolder(PartnerViewHolder holder, int position) {
        if (mPartnerReader.moveToPosition(position)) {
            holder.partnerId = mPartnerReader.getId();
            holder.isVisible = mPartnerReader.getIsVisible();
            holder.nameTextView.setText(mPartnerReader.getName());
            holder.itemView.setClickable(holder.isVisible);
            if (holder.isVisible) {
                holder.visibilityButton.setImageResource(R.drawable.ic_visibility_accent_24dp);
            } else {
                holder.visibilityButton.setImageResource(R.drawable.ic_visibility_off_grey_24dp);
            }
        }
    }

    public void setPartnerReader(@Nullable PartnerReader partnerReader) {
        if (mPartnerReader == partnerReader) {
            return;
        }
        mPartnerReader = partnerReader;
        notifyDataSetChanged();
    }

    public void setPartnersVisibilityCursor(@Nullable PartnersVisibilityReader partnersVisibilityReader) {
        if (mPartnersVisibilityReader == partnersVisibilityReader) {
            return;
        }
        mPartnersVisibilityReader = partnersVisibilityReader;
        notifyDataSetChanged();
    }

    public class PartnerViewHolder extends RecyclerView.ViewHolder {

        public long partnerId;

        public boolean isVisible;

        public final TextView nameTextView;

        public final ImageButton visibilityButton;

        public PartnerViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.partner_name);
            visibilityButton = (ImageButton) itemView.findViewById(R.id.partner_visibility);
        }
    }

    public class AllPartnerViewHolder extends RecyclerView.ViewHolder {

        public boolean isVisible;

        public final ImageButton visibilityButton;

        public AllPartnerViewHolder(View itemView) {
            super(itemView);
            visibilityButton = (ImageButton) itemView.findViewById(R.id.partners_visibility);
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
