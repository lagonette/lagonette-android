package org.lagonette.app.app.widget.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.lagonette.app.R;
import org.lagonette.app.room.statement.Statement;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

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
        categoryId = Statement.NO_ID;
        iconImageView = itemView.findViewById(R.id.category_icon);
        visibilityButton = itemView.findViewById(R.id.category_visibility);
        collapsedButton = itemView.findViewById(R.id.category_collapsed);
        categoryTextView = itemView.findViewById(R.id.category_label);
    }
}
