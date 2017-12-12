package org.lagonette.app.app.widget.performer.base;

import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.lagonette.app.repo.Resource;
import org.lagonette.app.util.UiUtil;

public abstract class SearchBarPerformer {

    public interface OnSearchCommand {

        void notifySearch(@Nullable String search);
    }

    public interface OnOffsetChangedCommand {

        void notifyOffsetChanged(int offset);
    }

    @Nullable
    private OnSearchCommand mOnSearchCommand;

    @Nullable
    protected View mSearchBar;

    @Nullable
    private ProgressBar mProgressBar;

    @Nullable
    private TextView mSearchTextView;

    @IdRes
    private int mSearchBarRes;

    @IdRes
    private final int mProgressBarRes;

    @IdRes
    private final int mSearchTextRes;

    @Resource.Status
    private int mWorkStatus;

    public SearchBarPerformer(
            @IdRes int searchBarRes,
            @IdRes int progressBarRes,
            @IdRes int searchTextRes) {
        mSearchBarRes = searchBarRes;
        mProgressBarRes = progressBarRes;
        mSearchTextRes = searchTextRes;
    }

    public void inject(@NonNull View view) {
        mSearchBar = view.findViewById(mSearchBarRes);
        mProgressBar = view.findViewById(mProgressBarRes);
        mSearchTextView = view.findViewById(mSearchTextRes);

        setupSearchBarMarginTop(mSearchBar);
        setupSearchTextView(mSearchTextView);
    }

//    public void init() {
//        if (mBehavior == null || mSearchBar == null) {
//            throw new IllegalStateException("inject() must be called before init()");
//        }
//
//        setWorkStatus(Resource.SUCCESS);
//    }
//
//    public void restore(@Resource.Status int workState) {
//        if (mBehavior == null) {
//            throw new IllegalStateException("inject() must be called before restore()");
//        }
//
//        setWorkStatus(workState);
//    }

    public void setWorkStatus(@Resource.Status int workState) {
        mWorkStatus = workState;
        switch (mWorkStatus) {

            case Resource.LOADING:
                mProgressBar.setVisibility(View.VISIBLE);
                break;

            case Resource.ERROR:
            case Resource.SUCCESS:
                mProgressBar.setVisibility(View.GONE);
                break;
        }
    }

    private void setupSearchBarMarginTop(@NonNull View searchBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) searchBar.getLayoutParams();
            params.setMargins(
                    params.leftMargin,
                    params.topMargin + UiUtil.getStatusBarHeight(searchBar.getResources()),
                    params.rightMargin,
                    params.bottomMargin
            );
        }
    }

    private void setupSearchTextView(@NonNull TextView searchText) {
        //TODO Activity leaks ?
        // Add TextWatcher later to avoid callback called on configuration changed.
        searchText.post(
                () -> searchText.addTextChangedListener(
                        new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                if (mOnSearchCommand != null) {
                                    mOnSearchCommand.notifySearch(editable.toString());
                                }
                            }
                        }
                )
        );
    }

    public void onSearch(@NonNull OnSearchCommand onSearch) {
        mOnSearchCommand = onSearch;
    }
}
