package org.lagonette.app.app.widget.performer.portrait;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import org.lagonette.app.app.widget.behavior.TopEscapeBehavior;
import org.lagonette.app.app.widget.performer.base.SearchBarPerformer;
import org.lagonette.app.app.widget.performer.state.BottomSheetFragmentState;

public class PortraitSearchBarPerformer
        extends SearchBarPerformer {

    @Nullable
    private TopEscapeBehavior mBehavior;

    public PortraitSearchBarPerformer(
            @IdRes int searchBarRes,
            @IdRes int progressBarRes,
            @IdRes int searchTextRes) {
        super(searchBarRes, progressBarRes, searchTextRes);
    }

    @Override
    public void inject(@NonNull View view) {
        super.inject(view);

        mBehavior = TopEscapeBehavior.from(mSearchBar);
        mBehavior.setOnMoveListener(
                (child, translationY) -> onBottomChanged(child)
        );
    }

    public void notifyBottomSheetFragmentChanged(@NonNull BottomSheetFragmentState bottomSheetFragmentState) {
        if (mBehavior != null) {
            if (bottomSheetFragmentState.isLocationDetailLoaded()) {
                mBehavior.enable();
            }
            else {
                mBehavior.disable();
            }
        }
    }
}
