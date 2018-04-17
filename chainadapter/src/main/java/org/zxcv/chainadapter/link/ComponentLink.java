package org.zxcv.chainadapter.link;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import org.zxcv.chainadapter.component.AdapterComponent;

public abstract class ComponentLink<VH extends RecyclerView.ViewHolder, C extends AdapterComponent<VH>>
		implements AdapterLink<VH> {

	@NonNull
	public final C component;

	protected final int mRank;

	@Nullable
	protected AdapterLink<VH> mNextLink;

	protected ComponentLink(int rank, @NonNull C component) {
		mRank = rank;
		this.component = component;
	}

	@Override
	public int getRank() {
		return mRank;
	}

	@Nullable
	@Override
	public AdapterLink<VH> chainUp(@NonNull AdapterLink<VH> link) {
		if (link.getRank() > this.mRank) { // The added link has to be at start of this one.
			link = link.chainUp(ComponentLink.this); // So add this one to the added link.
			return link; // And the chain start is the added link.
		}
		else if (link.getRank() == this.mRank) { // The added link has to replace this one.
			if (mNextLink != null && link != ComponentLink.this) { // We are not at the end of the chain and this one is not the same as the added one.
				link = link.chainUp(mNextLink); // So link the added link to the next one.
				mNextLink = null; // Delete the reference to the next one.
			}
			return link; // And the chain start is the new one.
		}
		else { // The added link has to be at end of this one.
			if (mNextLink == null) { // We are at the end of the chain.
				mNextLink = link; // So the added link is set as the next one.
				return ComponentLink.this; // And the chain start is this one.
			}
			else { // We are not at the end of the chain
				mNextLink = mNextLink.chainUp(link); // So the added link is send to the next one
				return ComponentLink.this; // And the chain start is this one.
			}
		}
	}

	@Nullable
	@Override
	public AdapterLink<VH> unchain(@NonNull AdapterLink<VH> link) {
		if (link == this) { // This link has to be removed.
			AdapterLink<VH> chainedLink = mNextLink;
			mNextLink = null;
			return chainedLink; // The next link becomes this one.
		}
		else if (mNextLink != null) { // We are not at the end of the chain.
			mNextLink = mNextLink.unchain(link.getRank()); // Let the next link handle the remove.
			return ComponentLink.this; // The head of the chain remains this one.
		}
		else { // We are at the end of the chain, nothing to do.
			return ComponentLink.this; // The end of the chain remains this one.
		}
	}

	@Nullable
	@Override
	public AdapterLink<VH> unchain(int rank) {
		if (rank > this.mRank) { // The queried rank is to high, we will not found a link to remove anymore.
			return ComponentLink.this; // The head of the chain remains this one.
		}
		else if (rank == this.mRank) { // This link has to be removed.
			AdapterLink<VH> chainedLink = null;
			if (mNextLink != null) {
				chainedLink = mNextLink.unchain(rank); // Continue to remove further more in case of several link has the same rank.
				mNextLink = null;
			}
			return chainedLink; // The next link becomes this one.
		}
		else if (mNextLink != null) { // We are not at the end of the chain.
			mNextLink = mNextLink.unchain(rank);
			return ComponentLink.this; // The end of the chain remains this one.
		}
		else { // We are at the end of the chain, nothing to do.
			return ComponentLink.this; // The end of the chain remains this one.
		}
	}
}
