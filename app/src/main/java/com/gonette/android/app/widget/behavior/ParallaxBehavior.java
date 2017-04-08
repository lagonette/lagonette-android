package com.gonette.android.app.widget.behavior;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.gonette.android.R;

public class ParallaxBehavior<V extends View>
        extends CoordinatorLayout.Behavior<V> {

    public interface OnParallaxTranslationListener {

        void onParallaxTranslation(float translationY);
    }

    @IdRes
    private int mDependencyId = 0;

    @Nullable
    private OnParallaxTranslationListener mOnParallaxTranslationListener;

    public ParallaxBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.GonetteBehavior
        );
        TypedValue value = a.peekValue(R.styleable.GonetteBehavior_dependency);
        if (value != null) {
            mDependencyId = a.getResourceId(
                    R.styleable.GonetteBehavior_dependency, 0);
        }
        a.recycle();
    }

    @Override
    public boolean layoutDependsOn(
            CoordinatorLayout parent,
            V child,
            View dependency) {
        return dependency.getId() == mDependencyId;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, V child, View dependency) {
        float translationY = (dependency.getTop() - parent.getBottom()) / 2;
        translationY = Math.min(translationY, 0);
        child.setTranslationY(translationY);
        if (mOnParallaxTranslationListener != null) {
            mOnParallaxTranslationListener.onParallaxTranslation(translationY);
        }
        return true;
    }

    public void setOnParallaxTranslationListener(@Nullable OnParallaxTranslationListener onParallaxTranslationListener) {
        mOnParallaxTranslationListener = onParallaxTranslationListener;
    }

    /**
     * A utility function to get the {@link ParallaxBehavior} associated with the {@code view}.
     *
     * @param view The {@link View} with {@link ParallaxBehavior}.
     * @return The {@link ParallaxBehavior} associated with the {@code view}.
     */
    @SuppressWarnings("unchecked")
    public static <V extends View> ParallaxBehavior<V> from(V view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof CoordinatorLayout.LayoutParams)) {
            throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
        }
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) params)
                .getBehavior();
        if (!(behavior instanceof ParallaxBehavior)) {
            throw new IllegalArgumentException(
                    "The view is not associated with ParallaxBehavior");
        }
        return (ParallaxBehavior<V>) behavior;
    }
}
