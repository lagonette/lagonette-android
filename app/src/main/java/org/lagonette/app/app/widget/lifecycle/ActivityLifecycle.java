package org.lagonette.app.app.widget.lifecycle;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

public interface ActivityLifecycle extends Lifecycle<AppCompatActivity> {

    @LayoutRes
    int getContentView(@NonNull AppCompatActivity owner);
}
