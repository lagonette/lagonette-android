package org.lagonette.app.app.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public abstract class BaseActivity
        extends AppCompatActivity {

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        construct();
        setContentView();
        onViewCreated(getWindow().getDecorView().getRootView());
        if (savedInstanceState == null) {
            init();
        } else {
            restore(savedInstanceState);
        }
        onActivityCreated();
    }

    protected abstract void construct();

    protected abstract void setContentView();

    protected abstract void onViewCreated(@NonNull View view);

    protected abstract void init();

    protected abstract void restore(@NonNull Bundle savedInstanceState);

    protected abstract void onActivityCreated();
}
