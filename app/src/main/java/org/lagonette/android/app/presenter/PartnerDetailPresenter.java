package org.lagonette.android.app.presenter;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.lagonette.android.app.LaGonetteApplication;
import org.lagonette.android.app.contract.PartnerDetailContract;
import org.lagonette.android.app.fragment.PartnerDetailFragment;
import org.lagonette.android.app.presenter.base.LoaderPresenter;
import org.lagonette.android.content.contract.LaGonetteContract;
import org.lagonette.android.content.reader.PartnerReader;
import org.lagonette.android.room.database.LaGonetteDatabase;
import org.lagonette.android.util.IntentUtil;

public class PartnerDetailPresenter
        extends LoaderPresenter<PartnerDetailContract.View>
        implements PartnerDetailContract.Presenter {

    private static final String ARG_PARTNER_ID = "arg:partner_id";

    public static PartnerDetailFragment newInstance(long partnerId) {
        Bundle args = new Bundle(1);
        args.putLong(ARG_PARTNER_ID, partnerId);
        PartnerDetailFragment partnerDetailFragment = new PartnerDetailFragment();
        partnerDetailFragment.setArguments(args);
        return partnerDetailFragment;
    }

    private long mPartnerId = LaGonetteContract.NO_ID;

    public PartnerDetailPresenter(@NonNull PartnerDetailContract.View view) {
        super(view);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle args = mView.getArguments();
        mPartnerId = args.getLong(ARG_PARTNER_ID, LaGonetteContract.NO_ID);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        LaGonetteDatabase database = LaGonetteApplication.getDatabase(mView.getContext());
        Cursor cursor = database.partnerDao().getPartner(mPartnerId);
        PartnerReader reader = PartnerReader.create(cursor);
        mView.displayPartner(reader);
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
