package com.zxcv.gonette.app.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxcv.gonette.R;
import com.zxcv.gonette.content.contract.GonetteContract;
import com.zxcv.gonette.content.reader.PartnerReader;

public class PartnerDetailFragment
        extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = "PartnerDetailFragment";

    private static final String ARG_PARTNER_ID = "arg:partner_id";

    private TextView mNameTextView;

    private TextView mDescriptionTextView;

    private TextView mLatitudeTextView;

    private TextView mLongitudeTextView;

    private long mPartnerId = GonetteContract.NO_ID;

    public static PartnerDetailFragment newInstance(long partnerId) {
        Bundle args = new Bundle(1);
        args.putLong(ARG_PARTNER_ID, partnerId);
        PartnerDetailFragment partnerDetailFragment = new PartnerDetailFragment();
        partnerDetailFragment.setArguments(args);
        return partnerDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mPartnerId = args.getLong(ARG_PARTNER_ID, GonetteContract.NO_ID);
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_partner_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mNameTextView = (TextView) view.findViewById(R.id.name);
        mDescriptionTextView = (TextView) view.findViewById(R.id.description);
        mLatitudeTextView = (TextView) view.findViewById(R.id.latitude);
        mLongitudeTextView = (TextView) view.findViewById(R.id.longitude);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        queryPartner();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case R.id.loader_query_detail_partner:
                return new CursorLoader(
                        getContext(),
                        GonetteContract.Partner.CONTENT_URI,
                        new String[]{
                                GonetteContract.Partner.ID,
                                GonetteContract.Partner.NAME,
                                GonetteContract.Partner.DESCRIPTION,
                                GonetteContract.Partner.LATITUDE,
                                GonetteContract.Partner.LONGITUDE
                        },
                        GonetteContract.Partner.ID + " = ?",
                        new String[]{
                                String.valueOf(args.getLong(ARG_PARTNER_ID, GonetteContract.NO_ID))
                        },
                        null
                );
            default:
                throw new IllegalArgumentException("Unknown loader id: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_query_detail_partner:
                onQueryPartnerLoadFinished(cursor);
                break;
            default:
                throw new IllegalArgumentException("Unknown loader id: " + id);
        }
    }

    private void onQueryPartnerLoadFinished(Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            PartnerReader partnerReader = new PartnerReader(cursor);
            mNameTextView.setText(partnerReader.getName());
            mDescriptionTextView.setText(partnerReader.getDescription());
            mLatitudeTextView.setText(String.valueOf(partnerReader.getLatitude()));
            mLongitudeTextView.setText(String.valueOf(partnerReader.getLongitude()));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        int id = loader.getId();
        switch (id) {
            case R.id.loader_query_detail_partner:
                // Do nothing.
                break;
            default:
                throw new IllegalArgumentException("Unknown loader id: " + id);
        }
    }


    private void queryPartner() {
        Bundle args = new Bundle(1);
        args.putLong(ARG_PARTNER_ID, mPartnerId);
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(R.id.loader_query_detail_partner, args, PartnerDetailFragment.this);
    }
}
