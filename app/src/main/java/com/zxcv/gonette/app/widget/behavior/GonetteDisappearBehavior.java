package com.zxcv.gonette.app.widget.behavior;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.zxcv.gonette.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class GonetteDisappearBehavior extends CoordinatorLayout.Behavior<View> {

    private static final String TAG = "GonetteDisappearBehavior";

    public interface OnMoveListener {
        void onMove(View child, int translationY);
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LEAVE_METHOD_ALPHA, LEAVE_METHOD_SCALE})
    public @interface LeaveMethod {
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            MOVING_STATE_INIT,
            MOVING_STATE_MOVE,
            MOVING_STATE_END
    })
    public @interface MovingState {
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            HIDING_STATE_INIT,
            HIDING_STATE_MOVE,
            HIDING_STATE_END
    })
    public @interface HidingState {
    }

    public static final int MOVING_STATE_INIT = 0;

    public static final int MOVING_STATE_MOVE = 1;

    public static final int MOVING_STATE_END = 2;

    public static final int HIDING_STATE_INIT = 0;

    public static final int HIDING_STATE_MOVE = 1;

    public static final int HIDING_STATE_END = 2;

    public static final int LEAVE_METHOD_ALPHA = 0;

    public static final int LEAVE_METHOD_SCALE = 1;

    @Nullable
    private OnMoveListener mOnMoveListener;

    @IdRes
    private int mDependencyId = 0;

    private int mLeaveLength = 0;

    private int mLeaveOffset = 0;

    private int mMoveLength = 0;

    private int mMoveOffset = 0;

    private boolean mEnable = true;

    @LeaveMethod
    private int mLeaveMethod = LEAVE_METHOD_ALPHA;

    @MovingState
    private int mMovingState = MOVING_STATE_INIT;

    @HidingState
    private int mHidingState = HIDING_STATE_INIT;

    public GonetteDisappearBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.GonetteBehavior
        );
        mDependencyId = a.getResourceId(R.styleable.GonetteBehavior_dependency, 0);
        mLeaveLength = a.getDimensionPixelOffset(R.styleable.GonetteBehavior_leave_length, 250);
        mLeaveOffset = a.getDimensionPixelOffset(R.styleable.GonetteBehavior_leave_offset, 0);
        mMoveLength = a.getDimensionPixelOffset(R.styleable.GonetteBehavior_move_length, 250);
        mMoveOffset = a.getDimensionPixelOffset(R.styleable.GonetteBehavior_move_offset, 0);
        a.recycle();
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return mEnable && dependency.getId() == mDependencyId || super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        int dependencyTop = dependency.getTop();
        int limitMoveDependencyBottom = parent.getBottom() - mMoveOffset;
        int limitMoveDependencyTop = limitMoveDependencyBottom - mMoveLength;
        int limitLeaveDependencyBottom = parent.getBottom() - mLeaveOffset;
        int limitLeaveDependencyTop = limitLeaveDependencyBottom - mLeaveLength;
        int parentBottom = parent.getBottom();

        if (mHidingState != HIDING_STATE_INIT && dependencyTop >= limitLeaveDependencyBottom) {
            mHidingState = HIDING_STATE_INIT;
            child.setVisibility(View.VISIBLE);
            if (mLeaveMethod == LEAVE_METHOD_SCALE) {
                child.setScaleX(1f);
                child.setScaleY(1f);
            } else {
                child.setAlpha(1f);
            }
        } else if (mHidingState != HIDING_STATE_END && dependencyTop <= limitLeaveDependencyTop) {
            mHidingState = HIDING_STATE_END;
            child.setVisibility(View.GONE);
        } else if (limitLeaveDependencyTop < dependencyTop && dependencyTop < limitLeaveDependencyBottom) {
            float interpolation = (float) (dependencyTop - limitLeaveDependencyTop) / (float) (limitLeaveDependencyBottom - limitLeaveDependencyTop);
            child.setVisibility(View.VISIBLE);
            if (mLeaveMethod == LEAVE_METHOD_SCALE) {
                child.setScaleX(interpolation);
                child.setScaleY(interpolation);
            } else {
                child.setAlpha(interpolation);
            }

            if (mHidingState != HIDING_STATE_MOVE) {
                mHidingState = HIDING_STATE_MOVE;
            }
        }

        if (mMovingState != MOVING_STATE_END && dependencyTop <= limitMoveDependencyTop) {
            int translationY = limitMoveDependencyTop - limitMoveDependencyBottom;
            child.setTranslationY(translationY);

            mMovingState = MOVING_STATE_END;
            if (mOnMoveListener != null) {
                mOnMoveListener.onMove(child, translationY);
            }
        } else if (mMovingState != MOVING_STATE_INIT && dependencyTop >= limitMoveDependencyBottom) {
            child.setTranslationY(0);

            mMovingState = MOVING_STATE_INIT;
            if (mOnMoveListener != null) {
                mOnMoveListener.onMove(child, 0);
            }
        } else if (limitMoveDependencyTop < dependencyTop && dependencyTop < limitMoveDependencyBottom) {
            int translationY = -(parentBottom - dependencyTop - mMoveOffset);
            child.setTranslationY(translationY);

            if (mMovingState != MOVING_STATE_MOVE) {
                mMovingState = MOVING_STATE_MOVE;
            }

            if (mOnMoveListener != null) {
                mOnMoveListener.onMove(child, translationY);
            }
        }

        return true;
    }

    public void setMoveLength(int length) {
        mMoveLength = length;
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

    public void enable() {
        mEnable = true;
    }

    public void disable() {
        mEnable = false;
    }

    public void setOnMoveListener(@Nullable OnMoveListener onMoveListener) {
        mOnMoveListener = onMoveListener;
    }

    /**
     * A utility function to get the {@link GonetteDisappearBehavior} associated with the {@code view}.
     *
     * @param view The {@link View} with {@link GonetteDisappearBehavior}.
     * @return The {@link GonetteDisappearBehavior} associated with the {@code view}.
     */
    @SuppressWarnings("unchecked")
    public static GonetteDisappearBehavior from(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof CoordinatorLayout.LayoutParams)) {
            throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
        }
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) params)
                .getBehavior();
        if (!(behavior instanceof GonetteDisappearBehavior)) {
            throw new IllegalArgumentException(
                    "The view is not associated with GonetteDisappearBehavior");
        }
        return (GonetteDisappearBehavior) behavior;
    }
}
