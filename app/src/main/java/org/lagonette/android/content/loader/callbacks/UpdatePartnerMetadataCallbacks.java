package org.lagonette.android.content.loader.callbacks;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.content.Loader;

import org.lagonette.android.R;
import org.lagonette.android.content.loader.UpdatePartnerMetadataLoader;
import org.lagonette.android.content.loader.callbacks.base.BaseLoaderCallbacks;
import org.lagonette.android.content.loader.callbacks.base.BundleLoaderCallbacks;

public class UpdatePartnerMetadataCallbacks extends BundleLoaderCallbacks<UpdatePartnerMetadataCallbacks.Callbacks> {


    public interface Callbacks extends BaseLoaderCallbacks.Callbacks {

    }

    public UpdatePartnerMetadataCallbacks(@NonNull Callbacks callbacks) {
        super(callbacks);
    }

    @Override
    public void reattachLoader() {
        reattachLoader(R.id.loader_update_partner_metadata);
    }

    @Override
    public Loader<Bundle> onCreateLoader(int id, Bundle args) {
        return new UpdatePartnerMetadataLoader(mCallbacks.getContext(), args);
    }

    @CallSuper
    @Override
    public void onLoadFinished(@NonNull Loader<Bundle> loader, @NonNull Bundle data) {
        super.onLoadFinished(loader, data);
        // Do nothing
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Bundle> loader) {
        // Do nothing
    }

    public void updatePartnerVisibility(long partnerId, boolean isVisible) {
        Bundle args = UpdatePartnerMetadataLoader.getUpdateVisibilityArgs(
                partnerId,
                isVisible
        );
        initLoader(
                R.id.loader_update_partner_metadata,
                args
        );
    }

}
