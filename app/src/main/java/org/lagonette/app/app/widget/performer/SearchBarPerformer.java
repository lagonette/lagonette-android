package org.lagonette.app.app.widget.performer;

import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.lagonette.app.app.widget.behavior.TopEscapeBehavior;
import org.lagonette.app.repo.Resource;
import org.lagonette.app.util.UiUtil;

public class SearchBarPerformer {

    @Nullable
    private TopEscapeBehavior mBehavior;

    @Nullable
    private View mSearchBar;

    @Nullable
    private ProgressBar mProgressBar;

    @IdRes
    private int mSearchBarRes;

    @IdRes
    private final int mProgressBarRes;

    @Resource.Status
    private int mWorkStatus;

    public SearchBarPerformer(int searchBarRes, int progressBarRes) {
        mSearchBarRes = searchBarRes;
        mProgressBarRes = progressBarRes;
    }

    public void inject(@NonNull View view) {
        mSearchBar = view.findViewById(mSearchBarRes);
        mBehavior = TopEscapeBehavior.from(mSearchBar);

        mProgressBar = view.findViewById(mProgressBarRes);

        setupSearchBarMarginTop(mSearchBar);
    }

//    public void init() {
//        if (mBehavior == null || mSearchBar == null) {
//            throw new IllegalStateException("inject() must be called before init()");
//        }
//
//        setWorkState(Resource.SUCCESS);
//    }
//
//    public void restore(@Resource.Status int workState) {
//        if (mBehavior == null) {
//            throw new IllegalStateException("inject() must be called before restore()");
//        }
//
//        setWorkState(workState);
//    }

    public void setWorkState(@Resource.Status int workState) {
        mWorkStatus = workState;
        switch (mWorkStatus) {

            case Resource.LOADING:
                mProgressBar.setVisibility(View.VISIBLE);
                break;

            case Resource.ERROR:
            case Resource.SUCCESS:
                mProgressBar.setVisibility(View.GONE);
                break;
        }
    }

    private void setupSearchBarMarginTop(@NonNull View searchBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) searchBar.getLayoutParams();
            params.setMargins(
                    params.leftMargin,
                    params.topMargin + UiUtil.getStatusBarHeight(searchBar.getResources()),
                    params.rightMargin,
                    params.bottomMargin
            );
        }
    }
}
