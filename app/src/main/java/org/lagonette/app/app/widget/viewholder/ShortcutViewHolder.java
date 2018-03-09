package org.lagonette.app.app.widget.viewholder;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.lagonette.app.R;

public class ShortcutViewHolder extends RecyclerView.ViewHolder {

    @NonNull
    public final View locationView;

    @NonNull
    public final View exchangeOfficeView;

    @NonNull
    public final View officeView;

    @NonNull
    public final View backgroundOfficeView;

    @NonNull
    public final ImageView iconOfficeView;

    @NonNull
    public final TextView textOfficeView;

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

        locationView = itemView.findViewById(R.id.layout_shortcut_location);

        exchangeOfficeView = itemView.findViewById(R.id.layout_shortcut_exchange_office);

        officeView = itemView.findViewById(R.id.layout_shortcut_headquarter);
        backgroundOfficeView = itemView.findViewById(R.id.layout_shortcut_headquarter_background);
        iconOfficeView = itemView.findViewById(R.id.view_shortcut_headquarter_icon);
        textOfficeView = itemView.findViewById(R.id.view_shortcut_headquarter_text);

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