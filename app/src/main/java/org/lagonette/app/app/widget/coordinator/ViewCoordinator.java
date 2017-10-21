//package org.lagonette.app.app.widget.coordinator;
//
//import android.content.Context;
//import android.content.res.Resources;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.view.View;
//
//public abstract class ViewCoordinator {
//
//    @NonNull
//    private final Context mContext;
//
//    public ViewCoordinator(@NonNull Context mContext) {
//        this.mContext = mContext;
//    }
//
//    protected Context getContext() {
//        return mContext;
//    }
//
//    protected Resources getResources() {
//        return mContext.getResources();
//    }
//
//    public abstract void inject(@NonNull View view);
//
//    public abstract void start(@Nullable Bundle savedInstanceState);
//}
