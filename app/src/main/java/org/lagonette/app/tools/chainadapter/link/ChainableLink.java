package org.lagonette.app.tools.chainadapter.link;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import org.lagonette.app.tools.chainadapter.component.AdapterComponent;
import org.lagonette.app.tools.chainadapter.identifier.Identifier;
import org.lagonette.app.tools.chainadapter.locator.AdapterLocator;

public class ChainableLink<VH extends RecyclerView.ViewHolder, C extends AdapterComponent<VH>>
        extends ComponentLink<VH, C> {

    @NonNull
    private final AdapterLocator<VH> mLocator;

    public ChainableLink(int rank, @NonNull Identifier identifier, @NonNull C component, @NonNull AdapterLocator<VH> locator) {
        super(rank, identifier, component);
        mLocator = locator;
    }

    @Override
    public int getItemCount() {
        if (mNextLink == null) {
            throw new IllegalStateException("There is no stub link !");
        }

        int itemCount = mNextLink.getItemCount();
        return itemCount + mLocator.calculateInsertedBeforeCount(itemCount, component, mNextLink);
    }

    @Override
    public int getItemViewType(int position) {
        if (mNextLink == null) {
            throw new IllegalStateException("There is no stub link !");
        }

        if (mLocator.handlePosition(position, component, mNextLink)) {
            return component.getItemViewType(position);
        }
        else {
            position -= mLocator.calculateInsertedBeforeCount(position, component, mNextLink);
            return mNextLink.getItemViewType(position);
        }
    }

    @Override
    public long getItemId(int position) {
        if (mNextLink == null) {
            throw new IllegalStateException("There is no stub link !");
        }

        if (mLocator.handlePosition(position, component, mNextLink)) {
            return genId(component.getItemId(position));
        }
        else {
            position -= mLocator.calculateInsertedBeforeCount(position, component, mNextLink);
            return mNextLink.getItemId(position);
        }
    }

    @NonNull
    @Override
    public VH createViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mNextLink == null) {
            throw new IllegalStateException("There is no stub link !");
        }

        if (component.handleViewType(viewType)) {
            return component.createViewHolder(parent, viewType);
        }
        else {
            return mNextLink.createViewHolder(parent, viewType);
        }
    }

    @Override
    public void bindViewHolder(@NonNull VH holder, int position) {
        if (mNextLink == null) {
            throw new IllegalStateException("There is no stub link !");
        }

        if (mLocator.handlePosition(position, component, mNextLink)) {
            component.bindViewHolder(holder, position);
        }
        else {
            position -= mLocator.calculateInsertedBeforeCount(position, component, mNextLink);
            mNextLink.bindViewHolder(holder, position);
        }
    }

    @Override
    public boolean isChained(int rank) {
        if (rank == mRank) {
            return true;
        }
        else if (mNextLink != null) {
            return mNextLink.isChained(rank);
        }
        else {
            return false;
        }
    }
}
