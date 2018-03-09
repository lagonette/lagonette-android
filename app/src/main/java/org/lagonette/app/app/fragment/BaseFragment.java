package org.lagonette.app.app.fragment;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        construct();
    }

    @Nullable
    @Override
    public final View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getContentView(), container, false);
    }

    @Override
    public final void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        inject(view);
    }

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        construct(getActivity());
        if (savedInstanceState == null) {
            init();
        }
        else {
            restore(savedInstanceState);
        }
        onConstructed();
    }

    protected abstract void construct();

    @LayoutRes
    protected abstract int getContentView();

    protected abstract void inject(@NonNull View view);

    protected abstract void construct(@NonNull FragmentActivity activity);

    protected abstract void init();

    protected abstract void restore(@NonNull Bundle savedInstanceState);

    protected abstract void onConstructed();
}
