package org.lagonette.app.app.widget.performer;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

public class FiltersButtonPerformer {

    public interface Observer {

        void notifyFilterButtonClick();
    }

    @Nullable
    private Observer mObserver;

    @IdRes
    private int mButtonRes;

    public FiltersButtonPerformer(int buttonRes) {
        mButtonRes = buttonRes;
    }

    public void inject(@NonNull View view) {
        FloatingActionButton fab = view.findViewById(mButtonRes);

        fab.setOnClickListener(
                button -> {
                    if (mObserver != null) {
                        mObserver.notifyFilterButtonClick();
                    }
                }
        );
    }

    public void observe(@Nullable Observer observer) {
        mObserver = observer;
    }
}
