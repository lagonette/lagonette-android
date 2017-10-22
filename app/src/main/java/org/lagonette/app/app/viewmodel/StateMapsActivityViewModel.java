package org.lagonette.app.app.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.performer.BottomSheetFragmentManager;

public class StateMapsActivityViewModel extends AndroidViewModel {

    @NonNull
    private final MutableLiveData<Integer> mBottomSheetState;

    @NonNull
    private final MutableLiveData<Integer> mBottomSheetFragment;

    public StateMapsActivityViewModel(Application application) {
        super(application);
        mBottomSheetState = new MutableLiveData<>();
        mBottomSheetFragment = new MutableLiveData<>();
        mBottomSheetState.setValue(BottomSheetBehavior.STATE_HIDDEN);
        mBottomSheetFragment.setValue(BottomSheetFragmentManager.FRAGMENT_NONE);
    }

    @NonNull
    public MutableLiveData<Integer> getBottomSheetState() {
        return mBottomSheetState;
    }

    @NonNull
    public MutableLiveData<Integer> getBottomSheetFragment() {
        return mBottomSheetFragment;
    }
}
