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
        return position == 0
                ? R.id.view_type_partner_all
                : position == getItemCount() - 1
                ? R.id.view_type_footer
                : R.id.view_type_partner;
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
            default:
                throw new IllegalArgumentException("Unknown view type:" + viewType);
        }
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
                onBindAllPartnerViewHolder((AllPartnerViewHolder) holder, position);
                break;
            case R.id.view_type_partner:
                onBindPartnerViewHolder((PartnerViewHolder) holder, position - 1);
                break;
            case R.id.view_type_footer:
                break;
            default:
                throw new IllegalArgumentException("Unknown view type:" + viewType);
        }
    }

    private void onBindAllPartnerViewHolder(AllPartnerViewHolder holder, int position) {
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

    @Override
    public int getItemCount() {
        return mPartnerReader != null
                ? 1 + mPartnerReader.getCount() + 1
                : 0;
    }

    @Override
    public long getItemId(int position) {
        if (position == 0) {
            return HEADER_ID;
        } else if (position == getItemCount() - 1) {
            return FOOTER_ID;
        } else if (mPartnerReader.moveToPosition(position)) {
            return mPartnerReader.getId();
        } else {
            return super.getItemId(position);
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
}
