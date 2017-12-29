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

        mBottomSheetFragmentStateLiveData = new BottomSheetFragmentStateLiveData();

        MainStatefulAction statefulAction = new MainStatefulAction(
                new MainAction(),
                new MainState(mBottomSheetFragmentStateLiveData.getValue())
        );

        mMainStatefulActionLiveData = new MainStatefulActionLiveData(statefulAction);
        mMainActionLiveData = new MainActionLiveData(statefulAction.action);
        mMainStateLiveData = new MainStateLiveData(statefulAction.state);
        mSearch = new MutableLiveData<>();
        mWorkStatus = new MutableLiveData<>();

        mMainStatefulActionLiveData.addSource(
                mMainActionLiveData,
                action -> mMainStatefulActionLiveData.setAction(action)
        );
        mMainStatefulActionLiveData.addSource(
                mMainStateLiveData,
                state -> mMainStatefulActionLiveData.setState(state)
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
