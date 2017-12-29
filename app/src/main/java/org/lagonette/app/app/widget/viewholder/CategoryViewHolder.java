package org.lagonette.app.app.widget.viewholder;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.lagonette.app.R;
import org.lagonette.app.room.embedded.CategoryKey;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    public interface OnCollapsedClickListener {

        void onClick(@NonNull CategoryKey categoryKey, boolean isCollapsed);

    }

    public interface OnVisibilityClickListener {

        void onClick(@NonNull CategoryKey categoryKey, boolean visibility);
    }

    @NonNull
    public final CategoryKey categoryKey;

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
        categoryKey = new CategoryKey();
        iconImageView = itemView.findViewById(R.id.category_icon);
        visibilityButton = itemView.findViewById(R.id.category_visibility);
        collapsedButton = itemView.findViewById(R.id.category_collapsed);
        categoryTextView = itemView.findViewById(R.id.category_label);
    }

    @NonNull
    public CategoryViewHolder setOnCollapsedClick(@Nullable CategoryViewHolder.OnCollapsedClickListener listener) {
        if (listener != null) {
            collapsedButton.setOnClickListener(
                    v -> listener.onClick(categoryKey, !isCollapsed)
            );
        }
        return CategoryViewHolder.this;
    }

    @NonNull
    public CategoryViewHolder setOnVisibilityClick(@Nullable CategoryViewHolder.OnVisibilityClickListener listener) {
        if (listener != null) {
            visibilityButton.setOnClickListener(
                    v -> listener.onClick(categoryKey, !isVisible)
            );
        }
        return CategoryViewHolder.this;
    }
}
