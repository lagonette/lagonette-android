package com.zxcv.gonette.app.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;

import com.zxcv.gonette.app.contract.FiltersContract;
import com.zxcv.gonette.app.fragment.FiltersFragment;
import com.zxcv.gonette.content.reader.PartnerReader;
import com.zxcv.gonette.content.reader.PartnersVisibilityReader;
import com.zxcv.gonette.content.repo.PartnerRepo;
import com.zxcv.gonette.util.SearchUtil;

public class FiltersPresenter
        extends FragmentPresenter
        implements FiltersContract.Presenter, PartnerRepo.Callback {

    private static final String ARG_SEARCH = "arg:search";

    public static FiltersFragment newInstance(@NonNull String search) {
        Bundle args = new Bundle(1);
        args.putString(ARG_SEARCH, search);
        FiltersFragment fragment = new FiltersFragment();
        fragment.setArguments(args);
        new FiltersPresenter(fragment);
        return fragment;
    }

    @NonNull
    private FiltersContract.Fragment mFragment;

    @NonNull
    private PartnerRepo mPartnerRepo;

    @NonNull
    private String mCurrentSearch = SearchUtil.DEFAULT_SEARCH;

    public FiltersPresenter(@NonNull FiltersContract.Fragment fragment) {
        mFragment = fragment;
        mFragment.setPresenter(FiltersPresenter.this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = mFragment.getArguments();
        if (arguments != null) {
            mCurrentSearch = arguments.getString(ARG_SEARCH, SearchUtil.DEFAULT_SEARCH);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPartnerRepo = new PartnerRepo(FiltersPresenter.this);
        mPartnerRepo.loadPartnersVisibility();
    }

    @Override
    public void setPartnerVisibility(long partnerId, boolean isVisible) {
        mPartnerRepo.setPartnerVisibility(partnerId, isVisible);
    }

    @Override
    public void setPartnersVisibility(boolean isVisible) {
        mPartnerRepo.setPartnersVisibility(isVisible);
    }

    @Override
    public void filterPartner(@NonNull String search) {
        if (!mCurrentSearch.equals(search)) {
            mCurrentSearch = search;
            mPartnerRepo.loadPartners(mCurrentSearch);
        }
    }

    @Override
    public void onPartnerLoaded(@Nullable PartnerReader reader) {
        mFragment.displayPartners(reader);
    }

    @Override
    public void onPartnersVisibilityLoaded(@Nullable PartnersVisibilityReader reader) {
        mFragment.displayPartnersVisibility(reader);
        //TODO maybe load only at start?
        mPartnerRepo.loadPartners(mCurrentSearch);
    }

    @Override
    public void onPartnerReset() {
        mFragment.resetPartners();
    }

    @Override
    public void onPartnersVisibilityReset() {
        mFragment.resetPartnersVisibility();
    }

    @NonNull
    @Override
    public Context getContext() {
        return mFragment.getContext();
    }

    @NonNull
    @Override
    public LoaderManager getLoaderManager() {
        return mFragment.getLoaderManager();
    }
}
