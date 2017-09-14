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

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    public interface OnCollapsedClickListener {

        void onClick(long categoryId, boolean isCollapsed);
    }

    public interface OnVisibilityClickListener {

        void onClick(long categoryId, boolean visibility);
    }

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

    public CategoryViewHolder(@NonNull ViewGroup parent) {
        super(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.row_category,
                                parent,
                                false
                        )
        );
        iconImageView = itemView.findViewById(R.id.category_icon);
        visibilityButton = itemView.findViewById(R.id.category_visibility);
        collapsedButton = itemView.findViewById(R.id.category_collapsed);
        categoryTextView = itemView.findViewById(R.id.category_label);
    }

    public CategoryViewHolder setOnCollapsedClick(@Nullable CategoryViewHolder.OnCollapsedClickListener listener) {
        if (listener != null) {
            collapsedButton.setOnClickListener(
                    v -> listener.onClick(categoryId, !isCollapsed)
            );
        }
        return CategoryViewHolder.this;
    }

    public CategoryViewHolder setOnVisibilityClick(@Nullable CategoryViewHolder.OnVisibilityClickListener listener) {
        if (listener != null) {
            visibilityButton.setOnClickListener(
                    v -> listener.onClick(categoryId, !isVisible)
            );
        }
        return CategoryViewHolder.this;
    }
}
