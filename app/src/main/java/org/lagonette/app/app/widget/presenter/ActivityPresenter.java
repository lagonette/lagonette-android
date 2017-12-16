package org.lagonette.app.app.widget.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public interface ActivityPresenter {

    void construct(@NonNull AppCompatActivity activity);

    void onViewCreated(@NonNull View view);

    void init(@NonNull AppCompatActivity activity);

    void restore(@NonNull AppCompatActivity activity, @NonNull Bundle savedInstanceState);

    void onActivityCreated(@NonNull AppCompatActivity activity);
}
