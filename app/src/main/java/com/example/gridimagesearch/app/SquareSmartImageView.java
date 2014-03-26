package com.example.gridimagesearch.app;

import android.content.Context;
import android.util.AttributeSet;

import com.loopj.android.image.SmartImageView;

public class SquareSmartImageView extends SmartImageView {
    public SquareSmartImageView(Context context) {
        super(context);
    }

    public SquareSmartImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareSmartImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width
    }
}