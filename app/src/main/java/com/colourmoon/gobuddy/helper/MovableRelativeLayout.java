package com.colourmoon.gobuddy.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.coordinatorlayout.widget.CoordinatorLayout;


@CoordinatorLayout.DefaultBehavior(MoveUpawardBehaviour.class)
public class MovableRelativeLayout extends RelativeLayout {
    public MovableRelativeLayout(Context context) {
        super(context);
    }

    public MovableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MovableRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MovableRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
