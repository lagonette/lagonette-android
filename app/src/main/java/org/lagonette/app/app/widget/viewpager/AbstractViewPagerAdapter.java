package org.lagonette.app.app.widget.viewpager;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public abstract class AbstractViewPagerAdapter<PH extends PageHolder>
		extends PagerAdapter {

	protected abstract void bind(@NonNull PH pageHolder, int position);

	@NonNull
	@Override
	public Object instantiateItem(@NonNull ViewGroup parent, int position) {
		PH pageHolder = create(parent, position);
		parent.addView(pageHolder.itemView);
		bind(pageHolder, position);
		return pageHolder;
	}

	@Override
	public void destroyItem(@NonNull ViewGroup parent, int position, @NonNull Object view) {
		parent.removeView(((PageHolder) view).itemView);
	}

	@Override
	public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
		return view == ((PageHolder) object).itemView;
	}

	public int getItemPosition(Object object) {
		return POSITION_NONE; // TODO Find better way ?
	}

	@NonNull
	protected abstract PH create(@NonNull ViewGroup container, int position);

}
