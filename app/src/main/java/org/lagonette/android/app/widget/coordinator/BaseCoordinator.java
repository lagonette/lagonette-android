package org.lagonette.android.app.widget.coordinator;

import android.os.Bundle;
import android.support.annotation.Nullable;

public interface BaseCoordinator {

    void onCreate(@Nullable Bundle savedInstanceState);

    void onActivityCreated(@Nullable Bundle savedInstanceState);

    void onSaveInstanceState(Bundle outState);

}
