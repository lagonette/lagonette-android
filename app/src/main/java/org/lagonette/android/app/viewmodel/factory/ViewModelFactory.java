package org.lagonette.android.app.viewmodel.factory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import org.lagonette.android.app.viewmodel.FiltersViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    private final Context mContext;

    public ViewModelFactory(@NonNull Context context) {
        mContext = context;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {

        if (modelClass.equals(FiltersViewModel.class)) {
            //noinspection unchecked
            return (T) new FiltersViewModel(mContext);
        } else {
            throw new IllegalArgumentException("Can not instantiate: " + modelClass.getName());
        }
    }
}
