package org.lagonette.app.app.widget.performer.base;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.view.View;

//TODO Makes *Performer implement Performer
public interface Performer<VM extends ViewModel> {

    void inject(@NonNull View view);

    void init(@NonNull VM viewModel);

    void restore(@NonNull VM viewModel);
}
