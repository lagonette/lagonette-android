package com.zxcv.gonette.app.widget.behavior;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.zxcv.gonette.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class GonetteFabBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {

    private static final String TAG = "GonetteFabBehavior";

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LEAVE_METHOD_ALPHA, LEAVE_METHOD_SCALE})
    public @interface LeaveMethod {
    }

    public static final int LEAVE_METHOD_ALPHA = 0;

    public static final int LEAVE_METHOD_SCALE = 1;

    @IdRes
    private int mDependencyId = 0;

    private int mMarginBottom = 0;

    private int mLeaveLength = 0;

    private int mLeaveOffset = 0;

    private int mMoveOffset = 0;

    @LeaveMethod
    private int mLeaveMethod = LEAVE_METHOD_ALPHA;

    public GonetteFabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        //check attributes you need, for example all paddings
        int[] attributes = new int[]{
                android.R.attr.layout_margin,
                android.R.attr.layout_marginBottom
        };
        //then obtain typed array
        TypedArray arr = context.obtainStyledAttributes(attrs, attributes);
        //and get values you need by indexes from your array attributes defined above
        mMarginBottom = arr.getDimensionPixelOffset(0, 0);
        mMarginBottom = arr.getDimensionPixelOffset(1, mMarginBottom);
        // Finally recycle
        arr.recycle();

        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.GonetteBehavior
        );
        mDependencyId = a.getResourceId(R.styleable.GonetteBehavior_dependency, 0);
        mLeaveLength = a.getDimensionPixelOffset(R.styleable.GonetteBehavior_leave_length, 250);
        mLeaveOffset = a.getDimensionPixelOffset(R.styleable.GonetteBehavior_leave_offset, 0);
        mMoveOffset = a.getDimensionPixelOffset(R.styleable.GonetteBehavior_move_offset, 0);
        a.recycle();
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return dependency.getId() == mDependencyId || super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        int height = child.getHeight() + mMarginBottom;
        int dependencyTop = dependency.getTop();
        int limitMoveDependencyBottom = parent.getBottom() - mMoveOffset;
        int limitLeaveDependencyBottom = parent.getBottom() - mLeaveOffset;
        int limitLeaveDependencyTop = limitLeaveDependencyBottom - mLeaveLength;

        if (dependencyTop >= limitMoveDependencyBottom) {
            int parentBottom = parent.getBottom();
            int initY = parentBottom - height;
            child.setY(initY);
        } else {
            int currentY = dependencyTop - height + mMoveOffset;
            child.setY(currentY);
        }

        if (dependencyTop >= limitLeaveDependencyBottom) {
            child.setVisibility(View.VISIBLE);
            if (mLeaveMethod == LEAVE_METHOD_SCALE) {
                child.setScaleX(1f);
                child.setScaleY(1f);
            } else {
                child.setAlpha(1f);
            }
        } else if (dependencyTop <= limitLeaveDependencyTop) {
            child.setVisibility(View.GONE);
        } else {
            float interpolation = (float) (dependencyTop - limitLeaveDependencyTop) / (float) (limitLeaveDependencyBottom - limitLeaveDependencyTop);
            child.setVisibility(View.VISIBLE);
            if (mLeaveMethod == LEAVE_METHOD_SCALE) {
                child.setScaleX(interpolation);
                child.setScaleY(interpolation);
            } else {
                child.setAlpha(interpolation);
            }
        }

        return true;
    }

    public void setMoveOffset(int offset) {
        mMoveOffset = offset;
    }

    public void setLeaveOffset(int offset) {
        mLeaveOffset = offset;
    }

    public void setLeaveLength(int leaveLength) {
        mLeaveLength = leaveLength;
    }

    public void setLeaveMethod(@LeaveMethod int leaveMethod) {
        mLeaveMethod = leaveMethod;
    }

    /**
     * A utility function to get the {@link GonetteFabBehavior} associated with the {@code view}.
     *
     * @param view The {@link View} with {@link GonetteFabBehavior}.
     * @return The {@link GonetteFabBehavior} associated with the {@code view}.
     */
    @SuppressWarnings("unchecked")
    public static GonetteFabBehavior from(FloatingActionButton view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof CoordinatorLayout.LayoutParams)) {
            throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
        }
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) params)
                .getBehavior();
        if (!(behavior instanceof GonetteFabBehavior)) {
            throw new IllegalArgumentException(
                    "The view is not associated with GonetteFabBehavior");
        }
        return (GonetteFabBehavior) behavior;
    }
}
