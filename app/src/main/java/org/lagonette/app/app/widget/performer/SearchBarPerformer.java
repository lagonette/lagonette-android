package org.lagonette.app.app.widget.performer;

import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.ProgressBar;

import org.lagonette.app.app.widget.behavior.TopEscapeBehavior;
import org.lagonette.app.util.UiUtil;

public class SearchBarPerformer {

    public static final int IDLE = 0;

    public static final int IN_PROGRESS = 1;

    @IntDef({
            IDLE,
            IN_PROGRESS
    })
    public @interface WorkState {

    }

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

    @WorkState
    private int mWorkState;

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

    public void init() {
        if (mBehavior == null || mSearchBar == null) {
            throw new IllegalStateException("inject() must be called before init()");
        }

        setWorkState(IDLE);
    }

    public void restore(@WorkState int workState) {
        if (mBehavior == null) {
            throw new IllegalStateException("inject() must be called before restore()");
        }

        setWorkState(workState);
    }

    public void setWorkState(@WorkState int workState) {
        mWorkState = workState;
        switch (mWorkState) {

            case IN_PROGRESS:
                mProgressBar.setVisibility(View.VISIBLE);
                break;

            case IDLE:
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
