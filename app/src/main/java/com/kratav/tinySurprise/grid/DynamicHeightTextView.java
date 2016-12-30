package com.kratav.tinySurprise.grid;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 *
 * A {@link TextView} that maintains a consistent width to height aspect ratio.
 * In the real world this would likely extend ImageView.
 */
public class DynamicHeightTextView extends TextView {

	private int imageHeight;

	public DynamicHeightTextView(Context context) {
	    super(context);
	}

	public DynamicHeightTextView(Context context, AttributeSet attrs) {
	    super(context, attrs);
	}

	public DynamicHeightTextView(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	}

	public void setHeight(int height) {
	    if (imageHeight != height) {
	        imageHeight = height;
	        requestLayout();
	    }
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    if (imageHeight > 0) {
	        int width = MeasureSpec.getSize(widthMeasureSpec);
	        setMeasuredDimension(width, imageHeight);
	    } else {
	        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    }
	}
}
