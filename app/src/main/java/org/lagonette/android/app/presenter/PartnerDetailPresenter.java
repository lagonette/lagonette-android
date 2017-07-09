package org.lagonette.android.app.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.android.R;
import org.lagonette.android.app.contract.PartnerDetailContract;
import org.lagonette.android.app.fragment.PartnerDetailFragment;
import org.lagonette.android.app.presenter.base.LoaderPresenter;
import org.lagonette.android.content.contract.LaGonetteContract;
import org.lagonette.android.content.loader.CursorLoaderParams;
import org.lagonette.android.content.loader.PartnerCursorLoaderHelper;
import org.lagonette.android.content.loader.callbacks.LoadPartnerCallbacks;
import org.lagonette.android.content.reader.PartnerReader;
import org.lagonette.android.util.IntentUtil;

public class PartnerDetailPresenter
        extends LoaderPresenter<PartnerDetailContract.View>
        implements PartnerDetailContract.Presenter,
        LoadPartnerCallbacks.Callbacks {

    private static final String ARG_PARTNER_ID = "arg:partner_id";

    public static PartnerDetailFragment newInstance(long partnerId) {
        Bundle args = new Bundle(1);
        args.putLong(ARG_PARTNER_ID, partnerId);
        PartnerDetailFragment partnerDetailFragment = new PartnerDetailFragment();
        partnerDetailFragment.setArguments(args);
        return partnerDetailFragment;
    }

    @SuppressWarnings("NullableProblems")
    @NonNull
    private LoadPartnerCallbacks mLoadPartnerCallbacks;

    private long mPartnerId = LaGonetteContract.NO_ID;

    public PartnerDetailPresenter(@NonNull PartnerDetailContract.View view) {
        super(view);
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

    @Override
    public void startDirection(double latitude, double longitude) {
        boolean success = IntentUtil.startDirection(
                mView.getContext(),
                latitude,
                longitude
        );
        if (!success) {
            mView.errorNoDirectionAppFound();
        }
    }

    @Override
    public void makeCall(@NonNull String phoneNumber) {
        boolean success = IntentUtil.makeCall(
                mView.getContext(),
                phoneNumber
        );
        if (!success) {
            mView.errorNoCallAppFound();
        }
    }

    @Override
    public void goToWebsite(@NonNull String url) {
        boolean success = IntentUtil.goToWebsite(
                mView.getContext(),
                url
        );
        if (!success) {
            mView.errorNoBrowserAppFound();
        }
    }

    @Override
    public void writeEmail(@NonNull String email) {
        boolean success = IntentUtil.writeEmail(
                mView.getContext(),
                email
        );
        if (!success) {
            mView.errorNoEmailAppFound();
        }
    }
}
