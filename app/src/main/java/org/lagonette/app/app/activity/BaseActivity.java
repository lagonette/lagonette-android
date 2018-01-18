package org.lagonette.app.app.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public abstract class BaseActivity
        extends AppCompatActivity {

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        construct();
        setContentView(getContentView());
        inject(getWindow().getDecorView().getRootView());
        if (savedInstanceState == null) {
            init();
        } else {
            restore(savedInstanceState);
        }
        connect();
    }

    protected abstract void construct();

    @LayoutRes
    protected abstract int getContentView();

    protected abstract void inject(@NonNull View view);

    protected abstract void init();

    protected abstract void restore(@NonNull Bundle savedInstanceState);

    protected abstract void connect();
}
