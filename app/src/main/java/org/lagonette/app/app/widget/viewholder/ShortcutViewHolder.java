package org.lagonette.app.app.widget.viewholder;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.lagonette.app.R;

public class ShortcutViewHolder extends RecyclerView.ViewHolder {

    @NonNull
    public final View locationView;

    @NonNull
    public final View exchangeOfficeView;

    @NonNull
    public final View officeView;

    public ShortcutViewHolder(
            @NonNull ViewGroup parent,
            @Nullable Runnable onLocationClick,
            @Nullable Runnable onExchangeOfficeClick,
            @Nullable Runnable onOfficeClick) {
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

        if (onLocationClick != null) {
            locationView.setOnClickListener(view -> onLocationClick.run());
        }

        if (onExchangeOfficeClick != null) {
            exchangeOfficeView.setOnClickListener(view -> onExchangeOfficeClick.run());
        }

        if (onOfficeClick != null) {
            officeView.setOnClickListener(view -> onOfficeClick.run());
        }
    }
}