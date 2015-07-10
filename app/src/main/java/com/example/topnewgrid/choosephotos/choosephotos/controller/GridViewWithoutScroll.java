package com.example.topnewgrid.choosephotos.choosephotos.controller;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * ScrollView中嵌套GridView时冲突
 * 重写GridView,把GridView的滚动条去掉
 * @author Lih
 *
 */
public class GridViewWithoutScroll extends GridView {

	public GridViewWithoutScroll(Context context, AttributeSet attrs) {

		super(context, attrs);

	}

	public GridViewWithoutScroll(Context context) {
		super(context);
	}

	public GridViewWithoutScroll(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
