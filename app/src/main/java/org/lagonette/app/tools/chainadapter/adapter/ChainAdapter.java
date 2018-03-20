package org.lagonette.app.tools.chainadapter.adapter;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import org.lagonette.app.tools.chainadapter.component.AdapterComponent;
import org.lagonette.app.tools.chainadapter.identifier.Identifier;
import org.lagonette.app.tools.chainadapter.link.AdapterLink;
import org.lagonette.app.tools.chainadapter.link.ChainableLink;
import org.lagonette.app.tools.chainadapter.link.ComponentLink;
import org.lagonette.app.tools.chainadapter.link.StubLink;
import org.lagonette.app.tools.chainadapter.locator.AdapterLocator;

public class ChainAdapter<VH extends RecyclerView.ViewHolder>
        extends DelegationAdapter<VH, AdapterLink<VH>> {

    @NonNull
    private final Identifier mIdentifier;

    public ChainAdapter(int linkCount) {
        mIdentifier = new Identifier(linkCount);
    }

    public ComponentLink<VH, AdapterComponent<VH>> chainUp(
            int rank,
            @NonNull AdapterComponent<VH> component,
            @NonNull AdapterLocator<VH> locator) {
        ComponentLink<VH, AdapterComponent<VH>> link = new ChainableLink<>(rank, mIdentifier, component, locator);
        chainUp(link);
        return link;
    }

    public ComponentLink<VH, AdapterComponent<VH>> chainUp(
            int rank,
            @NonNull AdapterComponent<VH> component) {
        ComponentLink<VH, AdapterComponent<VH>> link = new StubLink<>(rank, mIdentifier, component);
        chainUp(link);
        return link;
    }

    public void chainUp(@NonNull AdapterLink<VH> link) {
        AdapterLink<VH> chain = get();
        if (chain != null) {
            set(chain.chainUp(link));
        }
        else {
            set(link);
        }
        notifyDataSetChanged();
    }

    public void unchain(@NonNull AdapterLink<VH> link) {
        AdapterLink<VH> chain = get();
        if (chain != null) {
            set(chain.unchain(link));
            notifyDataSetChanged();
        }
    }

    public void unchain(int rank) {
        AdapterLink<VH> chain = get();
        if (chain != null) {
            set(chain.unchain(rank));
            notifyDataSetChanged();
        }
    }

    public boolean isChained(int rank) {
        AdapterLink<VH> chain = get();
        if (chain != null) {
            return chain.isChained(rank);
        }
        else {
            return false;
        }
    }
}
