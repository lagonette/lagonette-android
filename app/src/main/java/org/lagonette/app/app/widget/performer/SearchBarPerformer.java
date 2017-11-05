package org.lagonette.app.app.widget.performer;

import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

import org.lagonette.app.app.widget.behavior.TopEscapeBehavior;
import org.lagonette.app.util.UiUtil;

public class SearchBarPerformer {

    @Nullable
    private TopEscapeBehavior mBehavior;

    @Nullable
    private View mSearchBar;

    @IdRes
    private int mSearchBarRes;

    public SearchBarPerformer(int searchBarRes) {
        mSearchBarRes = searchBarRes;
    }

    public void inject(@NonNull View view) {
        mSearchBar = view.findViewById(mSearchBarRes);
        mBehavior = TopEscapeBehavior.from(mSearchBar);

        setupSearchBarMarginTop(mSearchBar);
    }

    public void init() {
        if (mBehavior == null || mSearchBar == null) {
            throw new IllegalStateException("inject() must be called before init()");
        }
    }

    public void restore() {
        if (mBehavior == null) {
            throw new IllegalStateException("inject() must be called before restore()");
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
