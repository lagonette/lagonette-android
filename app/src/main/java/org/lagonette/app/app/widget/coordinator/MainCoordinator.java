package org.lagonette.app.app.widget.coordinator;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;

import org.lagonette.app.app.widget.performer.BottomSheetFragmentManager;
import org.lagonette.app.app.widget.performer.BottomSheetPerformer;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class MainCoordinator {

    public interface BottomSheetCallback {

        void closeBottomSheet();

        void openBottomSheet();

    }

    public interface FragmentLoader {

        void loadFragment(@BottomSheetFragmentManager.FragmentType int type);

    }

    private static final int ACTION_IDLE = 0;

    private static final int ACTION_OPEN_FILTERS = 1;

    @Retention(SOURCE)
    @IntDef({
            ACTION_IDLE,
            ACTION_OPEN_FILTERS
    })
    private @interface Action {

    }

    @NonNull
    private BottomSheetCallback mBottomSheetCallback;

    @NonNull
    private FragmentLoader mFragmentLoader;

    @Action
    private int mPendingAction;

    @BottomSheetPerformer.State
    private int mBottomSheetState;

    @BottomSheetFragmentManager.FragmentType
    private int mBottomSheetFragment;

    public MainCoordinator(
            @NonNull BottomSheetCallback bottomSheetCallback,
            @NonNull FragmentLoader fragmentLoader) {
        mBottomSheetCallback = bottomSheetCallback;
        mFragmentLoader = fragmentLoader;
        mPendingAction = ACTION_IDLE;
        mBottomSheetState = BottomSheetBehavior.STATE_HIDDEN;
        mBottomSheetFragment = BottomSheetFragmentManager.FRAGMENT_NONE;
    }

    public void openFilters() {
        mPendingAction = ACTION_OPEN_FILTERS;
        dispatchAction();
    }

    public void notifyBottomSheetStateChanged(@BottomSheetPerformer.State int newState) {
        mBottomSheetState = newState;
        dispatchAction();
    }

    public void notifyBottomSheetFragmentChanged(@BottomSheetFragmentManager.FragmentType int newFragment) {
        mBottomSheetFragment = newFragment;
        dispatchAction();
    }

    private void dispatchAction() {
        switch (mPendingAction) {

            case ACTION_OPEN_FILTERS:
                computeFiltersOpening();
                break;

            default:
            case ACTION_IDLE:
                markPendingActionDone();
                break;
        }
    }

    private void computeFiltersOpening() {
        switch (mBottomSheetState) {

            case BottomSheetBehavior.STATE_HIDDEN:
                switch (mBottomSheetFragment) {

                    case BottomSheetFragmentManager.FRAGMENT_NONE:
                        loadFiltersFragment();
                        break;

                    case BottomSheetFragmentManager.FRAGMENT_PARTNER:
                        loadFiltersFragment();
                        break;

                    case BottomSheetFragmentManager.FRAGMENT_FILTERS:
                        openBottomSheet();
                        break;
                }
                break;

            case BottomSheetBehavior.STATE_COLLAPSED:
                switch (mBottomSheetFragment) {

                    case BottomSheetFragmentManager.FRAGMENT_NONE:
                        wtf();
                        closeBottomSheet();
                        break;

                    case BottomSheetFragmentManager.FRAGMENT_PARTNER:
                        closeBottomSheet();
                        break;

                    case BottomSheetFragmentManager.FRAGMENT_FILTERS:
                        markPendingActionDone();
                        break;
                }
                break;

            case BottomSheetBehavior.STATE_EXPANDED:
                switch (mBottomSheetFragment) {

                    case BottomSheetFragmentManager.FRAGMENT_NONE:
                        wtf();
                        closeBottomSheet();
                        break;

                    case BottomSheetFragmentManager.FRAGMENT_PARTNER:
                        closeBottomSheet();
                        break;

                    case BottomSheetFragmentManager.FRAGMENT_FILTERS:
                        markPendingActionDone();
                        break;
                }
                break;

            case BottomSheetBehavior.STATE_DRAGGING:
                switch (mBottomSheetFragment) {

                    case BottomSheetFragmentManager.FRAGMENT_NONE:
                        wtf();
                        closeBottomSheet();
                        break;

                    case BottomSheetFragmentManager.FRAGMENT_FILTERS:
                        markPendingActionDone();
                        break;

                    case BottomSheetFragmentManager.FRAGMENT_PARTNER:
                        markPendingActionDone();
                        break;
                }
                break;

            case BottomSheetBehavior.STATE_SETTLING:
                switch (mBottomSheetFragment) {

                    case BottomSheetFragmentManager.FRAGMENT_NONE:
                        wtf();
                        closeBottomSheet();
                        break;

                    case BottomSheetFragmentManager.FRAGMENT_FILTERS:
                        markPendingActionDone();
                        break;

                    case BottomSheetFragmentManager.FRAGMENT_PARTNER:
                        closeBottomSheet();
                        break;
                }
                break;
        }
    }

    private void markPendingActionDone() {
        mPendingAction = ACTION_IDLE;
    }

    private void closeBottomSheet() {
        mBottomSheetCallback.closeBottomSheet();
    }

    private void openBottomSheet() {
        mBottomSheetCallback.openBottomSheet();
    }

    private void loadFiltersFragment() {
        mFragmentLoader.loadFragment(BottomSheetFragmentManager.FRAGMENT_FILTERS);
    }

    private void loadPartnerFragment() {
        mFragmentLoader.loadFragment(BottomSheetFragmentManager.FRAGMENT_PARTNER);
    }

    private void unloadFragment() {
        mFragmentLoader.loadFragment(BottomSheetFragmentManager.FRAGMENT_NONE);
    }

    private void wtf() {
        //TODO log exception, just in case
    }
}
