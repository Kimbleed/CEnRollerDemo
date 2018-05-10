package com.example.a49479.cenrollerdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 49479 on 2018/5/10.
 */

public class CEnSingleRollerView extends View {

    private String[] mCells;

    public CEnSingleRollerView(Context context) {
        super(context);
    }

    public CEnSingleRollerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CEnSingleRollerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
