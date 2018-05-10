package com.example.a49479.cenrollerdemo;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

/**
 * Created by 49479 on 2018/5/10.
 */

public class CEnSingleRollerView extends LinearLayout {

    private Context mContext;

    //装载元素item 的 Recycler
    private RollerRecycler mRoller;

    private LinearLayoutManager mLinearLayoutManager;

    private LayoutInflater mInflater;

    // 滚轮的转到正方向
    private int positiveDirection = RollerRecycler.SCROLL_DIRECTION_UP;

    //目标数字
    private int mTarget = -1;

    public CEnSingleRollerView(Context context) {
        super(context);
    }

    public CEnSingleRollerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CEnSingleRollerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context){
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(mRoller ==null) {
            View view = mInflater.inflate(R.layout.layout_recycler, this, false);
            mRoller = (RollerRecycler) view.findViewById(R.id.recycler);
            mRoller.setAdapter(new RollerAdapter(getContext()));
            mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            mRoller.setLayoutManager(mLinearLayoutManager);
            mRoller.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {

                    int position = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    int value = position % 10;


                    if (mTarget != -1) {
                        if(value == mTarget)
                            mRoller.stopRoll();
                        return;
                    }

                    //向上滚动(正在加载的滚动方向)
                    if (mRoller.getDirection() == positiveDirection) {
                        if (value == mTarget) {
                            if (mRoller.getSpeed() <= Math.abs(RollerRecycler.SPEED_MIN)) {
                                controlSpeedVector(600, RollerRecycler.SPEED_MIN, 0, new MainActivity.AnimEndResponse() {
                                    @Override
                                    public void onResponse() {
                                        mRoller.setSpeed(RollerRecycler.SPEED_BACK);
                                    }
                                });
                            }
                        }
                    }

                }
            });
            addView(view);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public void startRoll(){
        mTarget = -1;
        mRoller.startRoll(true);
    }

    public void stopRoll(int target){
        mTarget = target;
        controlSpeedVector(2000,mRoller.getSpeed(),RollerRecycler.SPEED_MIN,null);
    }

    private void controlSpeedVector(long time, int startSpeed, int endSpeed, final MainActivity.AnimEndResponse response) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(startSpeed, endSpeed);
        valueAnimator.setDuration(time);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                mRoller.setSpeed(value);
                mRoller.setDirection(value>0?RollerRecycler.SCROLL_DIRECTION_UP:RollerRecycler.SCROLL_DIRECTION_DOWN);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (response != null)
                    response.onResponse();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }
}
