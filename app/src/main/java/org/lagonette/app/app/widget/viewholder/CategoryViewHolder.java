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
import org.lagonette.app.tools.functions.LongBooleanConsumer;
import org.lagonette.app.tools.functions.ObjBooleanConsumer;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    @NonNull
    public CategoryKey categoryKey;

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

    public CategoryViewHolder(
            @NonNull ViewGroup parent,
            @Nullable ObjBooleanConsumer<CategoryKey> onCollapsedClick,
            @Nullable ObjBooleanConsumer<CategoryKey> onVisibilityClick) {
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

        if (onCollapsedClick != null) {
            collapsedButton.setOnClickListener(view -> onCollapsedClick.accept(categoryKey, !isCollapsed));
        }

        if (onVisibilityClick != null) {
            visibilityButton.setOnClickListener(view -> onVisibilityClick.accept(categoryKey, !isVisible));
        }
    }
}
