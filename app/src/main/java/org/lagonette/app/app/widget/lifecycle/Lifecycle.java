package org.lagonette.app.app.widget.lifecycle;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;

public interface Lifecycle<Owner> {

    void construct(@NonNull Owner owner);

    void inject(@NonNull View view);

    void init(@NonNull Owner owner);

    void restore(@NonNull Owner owner, @NonNull Bundle savedInstanceState);

    void connect(@NonNull Owner owner);
}
