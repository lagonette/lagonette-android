package org.lagonette.app.app.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import org.lagonette.app.app.widget.coordinator.state.MainAction;
import org.lagonette.app.app.widget.coordinator.state.MainState;
import org.lagonette.app.app.widget.coordinator.state.MainStatefulAction;
import org.lagonette.app.app.widget.livedata.BottomSheetFragmentStateLiveData;
import org.lagonette.app.app.widget.livedata.MainActionLiveData;
import org.lagonette.app.app.widget.livedata.MainStateLiveData;
import org.lagonette.app.app.widget.livedata.MainStatefulActionLiveData;
import org.lagonette.app.app.widget.performer.state.BottomSheetFragmentState;
import org.lagonette.app.repo.Resource;

public class StateMapActivityViewModel extends AndroidViewModel {

    @NonNull
    private final MutableLiveData<String> mSearch;

    @NonNull
    private final MutableLiveData<Integer> mWorkStatus;

    @NonNull
    private final MainActionLiveData mMainActionLiveData;

    @NonNull
    private final MainStateLiveData mMainStateLiveData;

    @NonNull
    private final MainStatefulActionLiveData mMainStatefulActionLiveData;

    @NonNull
    private final BottomSheetFragmentStateLiveData mBottomSheetFragmentStateLiveData;

    public StateMapActivityViewModel(Application application) {
        super(application);

        BottomSheetFragmentState bottomSheetFragmentState = new BottomSheetFragmentState();
        MainStatefulAction statefulAction = new MainStatefulAction(
                new MainAction(),
                new MainState(bottomSheetFragmentState)
        );

        mBottomSheetFragmentStateLiveData = new BottomSheetFragmentStateLiveData(bottomSheetFragmentState);
        mMainStateLiveData = new MainStateLiveData(statefulAction.state);
        mMainActionLiveData = new MainActionLiveData(statefulAction.action);
        mMainStatefulActionLiveData = new MainStatefulActionLiveData(statefulAction);
        mSearch = new MutableLiveData<>();
        mWorkStatus = new MutableLiveData<>();

        mMainStatefulActionLiveData.addSource(
                mMainActionLiveData,
                mMainStatefulActionLiveData::setAction
        );
        mMainStatefulActionLiveData.addSource(
                mMainStateLiveData,
                mMainStatefulActionLiveData::setState
        );
        mMainStatefulActionLiveData.addSource(
                mBottomSheetFragmentStateLiveData,
                mMainStateLiveData::notifyBottomSheetFragmentChanged
        );

        mSearch.setValue("");
    }

    @NonNull
    public MainActionLiveData getMainActionLiveData() {
        return mMainActionLiveData;
    }

    @NonNull
    public MainStateLiveData getMainStateLiveData() {
        return mMainStateLiveData;
    }

    @NonNull
    public BottomSheetFragmentStateLiveData getBottomSheetFragmentState() {
        return mBottomSheetFragmentStateLiveData;
    }

    @NonNull
    public MainStatefulActionLiveData getMainStatefulActionLiveData() {
        return mMainStatefulActionLiveData;
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
