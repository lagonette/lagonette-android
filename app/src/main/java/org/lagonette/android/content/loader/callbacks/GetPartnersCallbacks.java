package org.lagonette.android.content.loader.callbacks;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.content.Loader;

import org.lagonette.android.R;
import org.lagonette.android.content.loader.GetPartnersLoader;
import org.lagonette.android.content.loader.callbacks.base.BaseLoaderCallbacks;
import org.lagonette.android.content.loader.callbacks.base.BundleLoaderCallbacks;

public class GetPartnersCallbacks extends BundleLoaderCallbacks<GetPartnersCallbacks.Callbacks> {

    public interface Callbacks extends BaseLoaderCallbacks.Callbacks {

        void errorGettingPartners();

    }

    public GetPartnersCallbacks(@NonNull Callbacks callbacks) {
        super(callbacks);
    }

    @Override
    public void reattachLoader() {
        reattachLoader(R.id.loader_get_partners);
    }

    @CallSuper
    @Override
    public Loader<Bundle> onCreateLoader(int id, Bundle args) {
        return new GetPartnersLoader(mCallbacks.getContext(), args);
    }

    @CallSuper
    @Override
    public void onLoadFinished(@NonNull Loader<Bundle> loader, @NonNull Bundle data) {
        super.onLoadFinished(loader, data);
        onGetPartnersLoaderFinished(data);
    }

    private void onGetPartnersLoaderFinished(@NonNull Bundle data) {
        int status = data.getInt(GetPartnersLoader.ARG_STATUS);
        switch (status) {
            case GetPartnersLoader.STATUS_OK:
                break;
            case GetPartnersLoader.STATUS_ERROR:
                mCallbacks.errorGettingPartners();
                break;
        }
    }

    @CallSuper
    @Override
    public void onLoaderReset(@NonNull Loader<Bundle> loader) {
        // Do nothing
    }

    public void getParners() {
        initLoader(
                R.id.loader_get_partners,
                null
        );
    }

}
