package com.zxcv.gonette.app.widget.adapter;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zxcv.gonette.R;
import com.zxcv.gonette.app.fragment.FiltersFragment;
import com.zxcv.gonette.content.reader.PartnerReader;
import com.zxcv.gonette.content.reader.PartnersVisibilityReader;

public class FilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnPartnerClickListener {

        void onPartnerClick(@NonNull FilterAdapter.PartnerViewHolder holder);

        void onAllPartnerVisibilityClick(@NonNull FilterAdapter.AllPartnerViewHolder holder);

        void onPartnerVisibilityClick(@NonNull FilterAdapter.PartnerViewHolder holder);

        void onSearchTextChanged(@NonNull String search);

        void onSearchClick(@NonNull SearchViewHolder tag);
    }

    private static final String TAG = "FilterAdapter";

    private static final int HEADER_ID = -2;

    private static final int FOOTER_ID = -3;

    private static final int LOADING_ID = -4;

    private static final int SEARCH_ID = -5;

    private static final int HEADER_COUNT = 1;

    private static final int FOOTER_COUNT = 1;

    private static final int LOADING_COUNT = 1;

    private static final int SEARCH_COUNT = 1;

    @Nullable
    private PartnerReader mPartnerReader;

    @Nullable
    private PartnersVisibilityReader mPartnersVisibilityReader;

    @Nullable
    private OnPartnerClickListener mOnPartnerClickListener;

    private boolean mIsExpanded = true;

    private SearchBarManager mSearchBarManager;

    public FilterAdapter(@Nullable OnPartnerClickListener onPartnerClickListener, @NonNull String search) {
        mOnPartnerClickListener = onPartnerClickListener;
        mSearchBarManager = new SearchBarManager(onPartnerClickListener, search);
    }

    @Override
    public int getItemCount() {
        if (mPartnersVisibilityReader == null) {
            return SEARCH_COUNT + LOADING_COUNT;
        } else if (mPartnerReader == null) {
            return SEARCH_COUNT + HEADER_COUNT + LOADING_COUNT;
        } else {
            return SEARCH_COUNT + HEADER_COUNT + getPartnerCount() + FOOTER_COUNT;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < SEARCH_COUNT) {
            return R.id.view_type_search;
        }
        position -= SEARCH_COUNT;

        if (mPartnersVisibilityReader == null) {
            return R.id.view_type_loading;
        } else {
            if (position < HEADER_COUNT) {
                return R.id.view_type_partner_all;
            }
            position -= HEADER_COUNT;

            if (mPartnerReader == null) {
                return R.id.view_type_loading;
            } else {
                if (position < getPartnerCount()) {
                    return R.id.view_type_partner;
                } else {
                    return R.id.view_type_footer;
                }
            }
        }
    }

    @Override
    public long getItemId(int position) {
        if (position < SEARCH_COUNT) {
            return SEARCH_ID;
        }
        position -= SEARCH_COUNT;

        if (mPartnersVisibilityReader == null) {
            return LOADING_ID;
        } else {
            if (position < HEADER_COUNT) {
                return HEADER_ID;
            }
            position -= HEADER_COUNT;

            if (mPartnerReader == null) {
                return LOADING_ID;
            } else {
                if (position < getPartnerCount()) {
                    if (mPartnerReader.moveToPosition(position)) {
                        return mPartnerReader.getId();
                    } else {
                        return RecyclerView.NO_ID;
                    }
                } else {
                    return FOOTER_ID;
                }
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case R.id.view_type_partner_all:
                return onCreateAllPartnerViewHolder(parent);
            case R.id.view_type_partner:
                return onCreatePartnerViewHolder(parent);
            case R.id.view_type_footer:
                return onCreateFooterViewHolder(parent);
            case R.id.view_type_loading:
                return onCreateLoadingViewHolder(parent);
            case R.id.view_type_search:
                return onCreateSearchViewHolder(parent);
            default:
                throw new IllegalArgumentException("Unknown view type:" + viewType);
        }
    }

    private LoadingViewHolder onCreateLoadingViewHolder(ViewGroup parent) {
        return new LoadingViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.row_loading,
                                parent,
                                false
                        )
        );
    }

    private FooterViewHolder onCreateFooterViewHolder(ViewGroup parent) {
        return new FooterViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.row_footer,
                                parent,
                                false
                        )
        );
    }

    private SearchViewHolder onCreateSearchViewHolder(ViewGroup parent) {
        SearchViewHolder holder = new SearchViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.row_search,
                                parent,
                                false
                        )
        );

        holder.clearButton.setTag(holder);
        holder.clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchClearClick((SearchViewHolder) v.getTag());
            }
        });

        if (mOnPartnerClickListener != null) {

            holder.searchTextView.setTag(holder);
            holder.searchTextView.setOnFocusChangeListener(mSearchBarManager);
            holder.searchTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnPartnerClickListener.onSearchClick((SearchViewHolder) v.getTag());
                }
            });

            holder.searchTextView.addTextChangedListener(mSearchBarManager);
        }

        return holder;
    }

    private PartnerViewHolder onCreatePartnerViewHolder(ViewGroup parent) {
        PartnerViewHolder holder = new PartnerViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.row_partner,
                                parent,
                                false
                        )
        );

        if (mOnPartnerClickListener != null) {
            holder.itemView.setTag(holder);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnPartnerClickListener.onPartnerClick((PartnerViewHolder) v.getTag());
                }
            });

            holder.visibilityButton.setTag(holder);
            holder.visibilityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnPartnerClickListener.onPartnerVisibilityClick((PartnerViewHolder) v.getTag());
                }
            });
        }

        return holder;
    }

    private AllPartnerViewHolder onCreateAllPartnerViewHolder(ViewGroup parent) {
        AllPartnerViewHolder holder = new AllPartnerViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.row_all_partner,
                                parent,
                                false
                        )
        );

        holder.expandButton.setTag(holder);
        holder.expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAllPartnerExpandClick((AllPartnerViewHolder) v.getTag());
            }
        });

        if (mOnPartnerClickListener != null) {
            holder.visibilityButton.setTag(holder);
            holder.visibilityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnPartnerClickListener.onAllPartnerVisibilityClick((AllPartnerViewHolder) v.getTag());
                }
            });
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case R.id.view_type_partner_all:
                onBindAllPartnerViewHolder((AllPartnerViewHolder) holder);
                break;
            case R.id.view_type_partner:
                onBindPartnerViewHolder((PartnerViewHolder) holder, position - SEARCH_COUNT - HEADER_COUNT);
                break;
            case R.id.view_type_search:
                onBindSearchViewHolder((SearchViewHolder) holder);
                break;
            case R.id.view_type_footer:
            case R.id.view_type_loading:
                break;
            default:
                throw new IllegalArgumentException("Unknown view type:" + viewType);
        }
    }

    private void onBindSearchViewHolder(@NonNull SearchViewHolder holder) {
        mSearchBarManager.onBind(holder);
    }

    private void onBindAllPartnerViewHolder(@NonNull AllPartnerViewHolder holder) {
        if (mPartnersVisibilityReader.moveToFirst()) {
            holder.isVisible = mPartnersVisibilityReader.getPartnersVisibilityCount() > 0;
            if (holder.isVisible) {
                holder.visibilityButton.setImageResource(R.drawable.ic_visibility_accent_24dp);
            } else {
                holder.visibilityButton.setImageResource(R.drawable.ic_visibility_off_grey_24dp);
            }

            holder.isExpanded = mIsExpanded;
            if (holder.isExpanded) {
                holder.expandButton.setImageResource(R.drawable.ic_expand_less_grey_24dp);
            } else {
                holder.expandButton.setImageResource(R.drawable.ic_expand_more_grey_24dp);
            }
        }
    }

    private void onBindPartnerViewHolder(@NonNull PartnerViewHolder holder, int position) {
        if (mPartnerReader.moveToPosition(position)) {
            holder.partnerId = mPartnerReader.getId();
            holder.isVisible = mPartnerReader.getIsVisible();
            holder.nameTextView.setText(mPartnerReader.getName());
            holder.itemView.setClickable(holder.isVisible);
            if (holder.isVisible) {
                holder.visibilityButton.setImageResource(R.drawable.ic_visibility_accent_24dp);
            } else {
                holder.visibilityButton.setImageResource(R.drawable.ic_visibility_off_grey_24dp);
            }
        }
    }

    private void onAllPartnerExpandClick(@NonNull AllPartnerViewHolder holder) {
        mIsExpanded = !mIsExpanded;
        notifyDataSetChanged();
    }

    private void onSearchClearClick(@NonNull SearchViewHolder holder) {
        holder.searchTextView.setText(FiltersFragment.DEFAULT_SEARCH);
    }

    private int getPartnerCount() {
        if (mPartnerReader != null && mIsExpanded) {
            return mPartnerReader.getCount();
        } else {
            return 0;
        }
    }

    public void setPartnerReader(@Nullable PartnerReader partnerReader) {
        if (mPartnerReader == partnerReader) {
            return;
        }
        mPartnerReader = partnerReader;
        notifyDataSetChanged();
    }

    public void setPartnersVisibilityCursor(@Nullable PartnersVisibilityReader partnersVisibilityReader) {
        if (mPartnersVisibilityReader == partnersVisibilityReader) {
            return;
        }
        mPartnersVisibilityReader = partnersVisibilityReader;
        notifyDataSetChanged();
    }

    public class PartnerViewHolder extends RecyclerView.ViewHolder {

        public long partnerId;

        public boolean isVisible;

        public final TextView nameTextView;

        public final ImageButton visibilityButton;

        public PartnerViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.partner_name);
            visibilityButton = (ImageButton) itemView.findViewById(R.id.partner_visibility);
        }
    }

    public class AllPartnerViewHolder extends RecyclerView.ViewHolder {

        public boolean isVisible;

        public boolean isExpanded;

        public final ImageButton visibilityButton;

        public final ImageButton expandButton;

        public AllPartnerViewHolder(View itemView) {
            super(itemView);
            visibilityButton = (ImageButton) itemView.findViewById(R.id.partners_visibility);
            expandButton = (ImageButton) itemView.findViewById(R.id.partners_expand);
        }
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        public final EditText searchTextView;

        public final ImageButton clearButton;

        public SearchViewHolder(View itemView) {
            super(itemView);
            searchTextView = (EditText) itemView.findViewById(R.id.search_text);
            clearButton = (ImageButton) itemView.findViewById(R.id.search_clear);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class SearchBarManager implements TextWatcher, View.OnFocusChangeListener {

        @Nullable
        private OnPartnerClickListener mOnPartnerClickListener;

        @Nullable
        private String mCurrentSearch;

        private boolean mSearchHasFocus = false;

        private boolean mTextChangedComesFromCode = false;

        public SearchBarManager(@NonNull OnPartnerClickListener onPartnerClickListener, @Nullable String search) {
            mOnPartnerClickListener = onPartnerClickListener;
            mCurrentSearch = search;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            // Save current search for the bind
            mCurrentSearch = s.toString();
            if (mOnPartnerClickListener != null) {
                mOnPartnerClickListener.onSearchTextChanged(mCurrentSearch);
            }
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            // Call the listener (To open bottom sheet) when the focus is requested from user and not by code.
            mSearchHasFocus = hasFocus;
            if (!mTextChangedComesFromCode && hasFocus) {
                if (mOnPartnerClickListener != null) {
                    mOnPartnerClickListener.onSearchClick((SearchViewHolder) v.getTag());
                }
            } else if (mTextChangedComesFromCode) {
                mTextChangedComesFromCode = false;
            }
        }

        public void onBind(@NonNull SearchViewHolder holder) {
            // Bind search bar with current search and manage to have the correct focus
            if (mCurrentSearch != null) {
                mTextChangedComesFromCode = true;
                holder.searchTextView.setText(mCurrentSearch);
                if (mSearchHasFocus) {
                    holder.searchTextView.requestFocus();
                    holder.searchTextView.setSelection(holder.searchTextView.getText().length());
                } else {
                    holder.searchTextView.clearFocus();
                }
            }
        }
    }
}
