package org.lagonette.android.app.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;

import org.lagonette.android.R;
import org.lagonette.android.app.contract.PartnerDetailContract;
import org.lagonette.android.app.fragment.PartnerDetailFragment;
import org.lagonette.android.content.contract.LaGonetteContract;
import org.lagonette.android.content.loader.CursorLoaderParams;
import org.lagonette.android.content.loader.PartnerCursorLoaderHelper;
import org.lagonette.android.content.loader.callbacks.LoadPartnerCallbacks;
import org.lagonette.android.content.reader.PartnerReader;

public class PartnerDetailPresenter
        implements PartnerDetailContract.Presenter, LoadPartnerCallbacks.Callbacks {

    private static final String ARG_PARTNER_ID = "arg:partner_id";

    public static PartnerDetailFragment newInstance(long partnerId) {
        Bundle args = new Bundle(1);
        args.putLong(ARG_PARTNER_ID, partnerId);
        PartnerDetailFragment partnerDetailFragment = new PartnerDetailFragment();
        partnerDetailFragment.setArguments(args);
        return partnerDetailFragment;
    }

    @NonNull
    private final PartnerDetailContract.View mView;

    @NonNull
    private LoadPartnerCallbacks mLoadPartnerCallbacks;

    private long mPartnerId = LaGonetteContract.NO_ID;

    public PartnerDetailPresenter(@NonNull PartnerDetailContract.View view) {
        mView = view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle args = mView.getArguments();
        mPartnerId = args.getLong(ARG_PARTNER_ID, LaGonetteContract.NO_ID);

        mLoadPartnerCallbacks = new LoadPartnerCallbacks(
                PartnerDetailPresenter.this,
                R.id.loader_query_detail_partner
        );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mLoadPartnerCallbacks.loadPartners(
                PartnerCursorLoaderHelper.createArgs(mPartnerId)
        );
    }

    @Override
    public void onStart() {
        // TODO Remove

    }

    @Override
    public void onStop() {
        // TODO Remove
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Remove
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // TODO Remove
    }

    //TODO Factorize ?
    @Override
    public Context getContext() {
        return mView.getContext();
    }

    //TODO Factorize ?
    @Override
    public LoaderManager getLoaderManager() {
        return mView.getLoaderManager();
    }

    @Override
    public void setPartnerReader(@Nullable PartnerReader reader) {
        mView.displayPartner(reader);
    }

    @Override
    public CursorLoaderParams getPartnerLoaderParams(@Nullable Bundle args) {
        return new CursorLoaderParams(
                LaGonetteContract.Partner.EXTENDED_CONTENT_URI,
                mView.getPartnerDetailColumns()
        )
                .setSelection(LaGonetteContract.Partner.ID + " = ?")
                .setSelectionArgs(
                        new String[]{
                                String.valueOf(PartnerCursorLoaderHelper.getPartnerId(args))
                        }
                );
    }
}
