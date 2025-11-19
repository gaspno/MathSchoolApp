package com.example.mathschool.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;
//Custom scrollview that allows you to momentarily stop scrolling
public class CustomSchoolView extends ScrollView {

    private boolean isScrollable=true;

    public CustomSchoolView(Context context) {
        super(context);
    }

    public CustomSchoolView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSchoolView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomSchoolView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isScrollable)
            return super.onTouchEvent(ev);
        else
            return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (isScrollable) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }


    public void setScrollable(boolean scrollable) {
        isScrollable = scrollable;
    }
}
