package org.lagonette.app.app.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.livedata.BottomSheetFragmentTypeLiveData;
import org.lagonette.app.repo.Resource;

public class StateMapActivityViewModel extends AndroidViewModel {

    @NonNull
    private final MutableLiveData<Integer> mBottomSheetState;

    @NonNull
    private final MutableLiveData<String> mSearch;

    @NonNull
    private final MutableLiveData<Integer> mWorkStatus;

    @NonNull
    private final BottomSheetFragmentTypeLiveData mBottomSheetFragmentType;

    public StateMapActivityViewModel(Application application) {
        super(application);
        mBottomSheetState = new MutableLiveData<>();
        mBottomSheetFragmentType = new BottomSheetFragmentTypeLiveData();
        mSearch = new MutableLiveData<>();
        mWorkStatus = new MutableLiveData<>();

        mBottomSheetState.setValue(BottomSheetBehavior.STATE_HIDDEN);
        mBottomSheetFragmentType.notifyUnload();
        mSearch.setValue("");
    }

    @NonNull
    public MutableLiveData<Integer> getBottomSheetState() {
        return mBottomSheetState;
    }

    @NonNull
    public BottomSheetFragmentTypeLiveData getBottomSheetFragmentType() {
        return mBottomSheetFragmentType;
    }

    @NonNull
    public MutableLiveData<String> getSearch() {
        return mSearch;
    }

    @NonNull
    public LiveData<Integer> getWorkStatus() {
        return mWorkStatus;
    }

    public void setWorkStatus(@Resource.Status int status) {
        mWorkStatus.setValue(status);
    }
}
