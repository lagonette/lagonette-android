package org.lagonette.android.app.widget.viewholder;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.lagonette.android.R;

public class PartnerViewHolder extends RecyclerView.ViewHolder {

    public interface OnClickListener {

        void onClick(long partnerId);
    }

    public interface OnVisibilityClickListener {

        void onClick(long partnerId, boolean visibility);
    }

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

    public PartnerViewHolder(@NonNull ViewGroup parent) {
        super(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.row_partner,
                                parent,
                                false
                        )
        );
        nameTextView = itemView.findViewById(R.id.partner_name);
        addressTextView = itemView.findViewById(R.id.partner_address);
        exchangeOfficeIndicatorImage = itemView.findViewById(R.id.partner_exchange_office_indicator);
        visibilityButton = itemView.findViewById(R.id.partner_visibility);
    }

    public PartnerViewHolder setOnPartnerClick(@Nullable OnClickListener listener) {
        if (listener != null) {
            itemView.setOnClickListener(
                    v -> listener.onClick(partnerId)
            );
        }
        return PartnerViewHolder.this;
    }

    public PartnerViewHolder setOnVisibilityClick(@Nullable OnVisibilityClickListener listener) {
        if (listener != null) {
            visibilityButton.setOnClickListener(
                    v -> listener.onClick(partnerId, !isVisible)
            );
        }
        return PartnerViewHolder.this;
    }
}