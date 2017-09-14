package org.lagonette.android.app.widget.viewholder;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.lagonette.android.R;

public class ShortcutViewHolder extends RecyclerView.ViewHolder {

    public interface OnPartnerClickListener {

        void onClick();
    }

    public interface OnExchangeOfficeClickListener {

        void onClick();
    }

    public interface OnOfficeClickListener {

        void onClick();
    }

    @NonNull
    public final View partnerView;

    @NonNull
    public final View exchangeOfficeView;

    @NonNull
    public final View officeView;

    public ShortcutViewHolder(View itemView) {
        super(itemView);
        partnerView = itemView.findViewById(R.id.shortcut_partner);
        exchangeOfficeView = itemView.findViewById(R.id.shortcut_exchange_office);
        officeView = itemView.findViewById(R.id.shortcut_office);
    }

    public ShortcutViewHolder setOnPartnerClick(@Nullable ShortcutViewHolder.OnPartnerClickListener listener) {
        if (listener != null) {
            partnerView.setOnClickListener(
                    v -> listener.onClick()
            );
        }
        return ShortcutViewHolder.this;
    }

    public ShortcutViewHolder setOnExchangeOfficeClick(@Nullable ShortcutViewHolder.OnExchangeOfficeClickListener listener) {
        if (listener != null) {
            partnerView.setOnClickListener(
                    v -> listener.onClick()
            );
        }
        return ShortcutViewHolder.this;
    }

    public ShortcutViewHolder setOnOfficeClick(@Nullable ShortcutViewHolder.OnOfficeClickListener listener) {
        if (listener != null) {
            partnerView.setOnClickListener(
                    v -> listener.onClick()
            );
        }
        return ShortcutViewHolder.this;
    }
}