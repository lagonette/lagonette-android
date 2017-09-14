package org.lagonette.android.app.widget.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.lagonette.android.R;

public class FooterViewHolder extends RecyclerView.ViewHolder {
    public FooterViewHolder(@NonNull ViewGroup parent) {
        super(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.row_footer,
                                parent,
                                false
                        )
        );
    }

}
