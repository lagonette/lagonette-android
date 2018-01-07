package org.lagonette.app.app.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import org.lagonette.app.app.arch.MutableLiveEvent;

public class ComViewModel extends ViewModel {

    @NonNull
    private final MutableLiveEvent<Long> mFiltersLocationClickEvent;

    public ComViewModel() {
        mFiltersLocationClickEvent = new MutableLiveEvent<>();
    }

    @NonNull
    public MutableLiveEvent<Long> getFiltersLocationClickEvent() {
        return mFiltersLocationClickEvent;
    }
}
