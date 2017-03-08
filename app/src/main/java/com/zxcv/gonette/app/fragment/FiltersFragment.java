package com.zxcv.gonette.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxcv.gonette.R;
import com.zxcv.gonette.app.widget.adapter.FilterAdapter;

public class FiltersFragment
        extends Fragment
        implements FilterAdapter.OnPartnerClickListener {

    public static final String TAG = "FiltersFragment";

    private RecyclerView mFilterList;

    private FilterAdapter mFilterAdapter;

    public static FiltersFragment newInstance() {
        return new FiltersFragment();
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

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );

        mFilterList.setLayoutManager(layoutManager);
        mFilterList.setAdapter(mFilterAdapter);
    }

    @Override
    public void onPartnerClick(FilterAdapter.PartnerViewHolder holder) {

    }

    @Override
    public void onAllPartnerVisibilityClick(FilterAdapter.AllPartnerViewHolder holder) {

    }

    @Override
    public void onPartnerVisibilityClick(FilterAdapter.PartnerViewHolder holder) {

    }
}