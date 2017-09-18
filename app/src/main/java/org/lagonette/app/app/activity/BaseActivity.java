package org.lagonette.app.app.activity;

import android.arch.lifecycle.LifecycleActivity;
import android.os.Bundle;

public abstract class BaseActivity
        extends LifecycleActivity {

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        onViewCreated();
        onActivityCreated(savedInstanceState);
    }

    protected abstract void setContentView();

    protected abstract void onViewCreated();

    protected abstract void onActivityCreated(Bundle savedInstanceState);
}
