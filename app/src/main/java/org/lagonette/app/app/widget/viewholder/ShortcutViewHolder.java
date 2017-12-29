package org.lagonette.app.app.widget.viewholder;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.lagonette.app.R;

public class ShortcutViewHolder extends RecyclerView.ViewHolder {

    public interface OnLocationClickListener {

        void onClick();
    }

    public interface OnExchangeOfficeClickListener {

        void onClick();
    }

    public interface OnOfficeClickListener {

        void onClick();
    }

    @NonNull
    public final View locationView;

    @NonNull
    public final View exchangeOfficeView;

    @NonNull
    public final View officeView;

    public ShortcutViewHolder(@NonNull ViewGroup parent) {
        super(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.row_shortcut,
                                parent,
                                false
                        )
        );
        locationView = itemView.findViewById(R.id.shortcut_location);
        exchangeOfficeView = itemView.findViewById(R.id.shortcut_exchange_office);
        officeView = itemView.findViewById(R.id.shortcut_office);
    }

    @NonNull
    public ShortcutViewHolder setOnLocationClick(@Nullable OnLocationClickListener listener) {
        if (listener != null) {
            locationView.setOnClickListener(
                    v -> listener.onClick()
            );
        }
        return ShortcutViewHolder.this;
    }

    @NonNull
    public ShortcutViewHolder setOnExchangeOfficeClick(@Nullable OnExchangeOfficeClickListener listener) {
        if (listener != null) {
            exchangeOfficeView.setOnClickListener(
                    v -> listener.onClick()
            );
        }
        return ShortcutViewHolder.this;
    }

    @NonNull
    public ShortcutViewHolder setOnOfficeClick(@Nullable OnOfficeClickListener listener) {
        if (listener != null) {
            officeView.setOnClickListener(
                    v -> listener.onClick()
            );
        }
        return ShortcutViewHolder.this;
    }
}