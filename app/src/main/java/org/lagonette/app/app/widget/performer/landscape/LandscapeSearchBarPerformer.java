package org.lagonette.app.app.widget.performer.landscape;

import android.support.annotation.IdRes;

import org.lagonette.app.app.widget.performer.base.SearchBarPerformer;

public class LandscapeSearchBarPerformer
        extends SearchBarPerformer {

    public LandscapeSearchBarPerformer(
            @IdRes int searchBarRes,
            @IdRes int progressBarRes,
            @IdRes int searchTextRes) {
        super(searchBarRes, progressBarRes, searchTextRes);
    }
}
