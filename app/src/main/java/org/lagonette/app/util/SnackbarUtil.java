package org.lagonette.app.util;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;

public abstract class SnackbarUtil {

    public static ViewGroup getViewGroup(@NonNull Fragment fragment) {
        return (ViewGroup) fragment.getActivity().findViewById(android.R.id.content);
    }

    public static ViewGroup getViewGroup(@NonNull Activity activity) {
        return (ViewGroup) activity.findViewById(android.R.id.content);
    }

}
