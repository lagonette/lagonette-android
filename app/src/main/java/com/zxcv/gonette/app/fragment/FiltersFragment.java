package com.zxcv.gonette.app.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxcv.gonette.R;
import com.zxcv.gonette.app.contract.FiltersContract;
import com.zxcv.gonette.app.widget.adapter.FilterAdapter;
import com.zxcv.gonette.content.reader.PartnerReader;
import com.zxcv.gonette.content.reader.PartnersVisibilityReader;

public class FiltersFragment
        extends Fragment
        implements FiltersContract.Fragment, FilterAdapter.OnPartnerClickListener {

    public interface Callback {

        void showPartner(long partnerId, boolean zoom);

    }

    public static final String TAG = "FiltersFragment";

    protected FiltersContract.Presenter mPresenter;

    private Callback mCallback;

    private RecyclerView mFilterList;

    private FilterAdapter mFilterAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filters, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mFilterList = (RecyclerView) view.findViewById(R.id.filter_list);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mFilterAdapter = new FilterAdapter(FiltersFragment.this);
        mFilterAdapter.setHasStableIds(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );

        mFilterList.setLayoutManager(layoutManager);
        mFilterList.setAdapter(mFilterAdapter);

        try {
            mCallback = (Callback) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(mCallback.toString() + " must implement " + Callback.class);
        }

        mPresenter.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPartnerClick(@NonNull FilterAdapter.PartnerViewHolder holder) {
        mCallback.showPartner(holder.partnerId, true);
    }

    @Override
    public void onAllPartnerVisibilityClick(@NonNull FilterAdapter.AllPartnerViewHolder holder) {
        mPresenter.setPartnersVisibility(!holder.isVisible);
    }

    @Override
    public void onPartnerVisibilityClick(@NonNull FilterAdapter.PartnerViewHolder holder) {
        mPresenter.setPartnerVisibility(holder.partnerId, !holder.isVisible);
    }

    public void filterPartner(@NonNull String search) {
        mPresenter.filterPartner(search);
    }

    @Override
    public void displayPartnersVisibility(@Nullable PartnersVisibilityReader visibilityReader) {
        mFilterAdapter.setPartnersVisibilityReader(visibilityReader);
    }

    @Override
    public void displayPartners(@Nullable PartnerReader partnerReader) {
        mFilterAdapter.setPartnerReader(partnerReader);
    }

    @Override
    public void resetPartnersVisibility() {
        mFilterAdapter.setPartnersVisibilityReader(null);
    }

    @Override
    public void resetPartners() {
        mFilterAdapter.setPartnerReader(null);
    }

    @Override
    public void setPresenter(FiltersContract.Presenter presenter) {
        mPresenter = presenter;
    }

}