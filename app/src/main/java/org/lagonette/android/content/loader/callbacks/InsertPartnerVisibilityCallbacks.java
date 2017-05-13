package org.lagonette.android.content.loader.callbacks;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.content.Loader;

import org.lagonette.android.R;
import org.lagonette.android.content.loader.InsertPartnerVisibilityLoader;
import org.lagonette.android.content.loader.callbacks.base.BaseLoaderCallbacks;
import org.lagonette.android.content.loader.callbacks.base.BundleLoaderCallbacks;

public class InsertPartnerVisibilityCallbacks extends BundleLoaderCallbacks<InsertPartnerVisibilityCallbacks.Callbacks> {


    public interface Callbacks extends BaseLoaderCallbacks.Callbacks {

    }

    public InsertPartnerVisibilityCallbacks(@NonNull Callbacks callbacks) {
        super(callbacks);
    }

    @Override
    public void reattachLoader() {
        reattachLoader(R.id.loader_insert_partner_visibility);
    }

    @Override
    public Loader<Bundle> onCreateLoader(int id, Bundle args) {
        return new InsertPartnerVisibilityLoader(mCallbacks.getContext(), args);
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

    public void insertPartnerVisibility(long partnerId, boolean isVisible) {
        Bundle args = InsertPartnerVisibilityLoader.getArgs(
                partnerId,
                isVisible
        );
        initLoader(
                R.id.loader_insert_partner_visibility,
                args
        );
    }

    public void insertPartnersVisibility(boolean isVisible) {
        Bundle args = InsertPartnerVisibilityLoader.getArgs(isVisible);
        initLoader(
                R.id.loader_insert_partner_visibility,
                args
        );
    }

}
