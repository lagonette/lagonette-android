package org.lagonette.android.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.lagonette.android.R;
import org.lagonette.android.app.contract.PartnerDetailContract;
import org.lagonette.android.app.presenter.PartnerDetailPresenter;
import org.lagonette.android.content.reader.PartnerReader;

public class PartnerDetailFragment
        extends Fragment
        implements PartnerDetailContract.View {

    public static final String TAG = "PartnerDetailContract";

    private TextView mNameTextView;

    private TextView mDescriptionTextView;

    private TextView mLatitudeTextView;

    private TextView mLongitudeTextView;

    private PartnerDetailPresenter mPresenter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new PartnerDetailPresenter(PartnerDetailFragment.this);
        mPresenter.onCreate(savedInstanceState);
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
        mPresenter.onActivityCreated(savedInstanceState);
    }

    @Override
    public void displayPartner(@Nullable PartnerReader reader) {
        if (reader != null && reader.moveToFirst()) {
            mNameTextView.setText(reader.getName());
            mDescriptionTextView.setText(reader.getDescription());
            mLatitudeTextView.setText(String.valueOf(reader.getLatitude()));
            mLongitudeTextView.setText(String.valueOf(reader.getLongitude()));
        }
    }

}
