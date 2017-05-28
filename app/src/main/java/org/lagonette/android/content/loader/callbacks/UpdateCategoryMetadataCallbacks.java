package org.lagonette.android.content.loader.callbacks;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.content.Loader;

import org.lagonette.android.R;
import org.lagonette.android.content.loader.UpdateCategoryMetadataLoader;
import org.lagonette.android.content.loader.callbacks.base.BaseLoaderCallbacks;
import org.lagonette.android.content.loader.callbacks.base.BundleLoaderCallbacks;

public class UpdateCategoryMetadataCallbacks
        extends BundleLoaderCallbacks<UpdateCategoryMetadataCallbacks.Callbacks> {


    public interface Callbacks extends BaseLoaderCallbacks.Callbacks {

    }

    public UpdateCategoryMetadataCallbacks(@NonNull Callbacks callbacks) {
        super(callbacks);
    }

    @Override
    public void reattachLoader() {
        reattachLoader(R.id.loader_update_category_metadata);
    }

    @Override
    public Loader<Bundle> onCreateLoader(int id, Bundle args) {
        return new UpdateCategoryMetadataLoader(mCallbacks.getContext(), args);
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

    public void updateCategoryVisibility(long categoryId, boolean isVisible) {
        Bundle args = UpdateCategoryMetadataLoader.getUpdateVisibilityArgs(
                categoryId,
                isVisible
        );
        initLoader(
                R.id.loader_update_category_metadata,
                args
        );
    }

    public void updateCategoryCollapsedState(long categoryId, boolean isColapsed) {
        Bundle args = UpdateCategoryMetadataLoader.getUpdateCollapsedStateArgs(
                categoryId,
                isColapsed
        );
        initLoader(
                R.id.loader_update_category_metadata,
                args
        );
    }

}
