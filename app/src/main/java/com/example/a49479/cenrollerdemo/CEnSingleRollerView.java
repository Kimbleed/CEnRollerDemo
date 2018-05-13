package com.example.a49479.cenrollerdemo;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by 49479 on 2018/5/10.
 */

public class CEnSingleRollerView extends RelativeLayout {

    private Context mContext;

    //装载元素item 的 Recycler
    private RollerRecycler mRoller;

    private LinearLayoutManager mLinearLayoutManager;

    private LayoutInflater mInflater;

    private int speedMax = -1;

    private int itemLayoutRes = -1;

    private int mStartIndex = 0;

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
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CEnSingleRollerView);
        itemLayoutRes = a.getResourceId(R.styleable.CEnSingleRollerView_item_layout, R.layout.layout_cell);
        positiveDirection = a.getInteger(R.styleable.CEnSingleRollerView_roll_direction,RollerRecycler.SCROLL_DIRECTION_UP);
        speedMax = a.getInteger(R.styleable.CEnSingleRollerView_roller_speed_max,-1);
        mStartIndex = a.getInteger(R.styleable.CEnSingleRollerView_start_index,0);
        a.recycle();
    }

    public CEnSingleRollerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mRoller == null) {
            //RollerRecycler 的 配置
            View containerView = mInflater.inflate(R.layout.layout_recycler, this, false);
            mRoller = containerView.findViewById(R.id.recycler);
            if(speedMax!=-1) {
                mRoller.setSPEED_MAX(speedMax);
            }
            mRoller.setAdapter(itemLayoutRes == -1 ? new RollerAdapter(getContext()) : new RollerAdapter(getContext(), itemLayoutRes));
            mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, positiveDirection != RollerRecycler.SCROLL_DIRECTION_UP
            );
            mRoller.setLayoutManager(mLinearLayoutManager);

            mRoller.scrollToPosition(mStartIndex);

            mRoller.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {

                    int position = mLinearLayoutManager.findFirstVisibleItemPosition()+1;
                    int value = position % 10;


                    if (mTarget != -1) {
                        if (value == mTarget && mRoller.getDirection() != positiveDirection) {
                            Log.i(CEnSingleRollerView.class.getName(),"target value:"+mTarget);
                            mRoller.stopRoll();
                            return;
                        }
                    }

                    //向上滚动(正在加载的滚动方向)
                    if (mRoller.getDirection() == positiveDirection) {
                        if (value == mTarget) {
                            if (mRoller.getSpeed() <= Math.abs(mRoller.SPEED_MIN)) {
                                int direction = mRoller.getDirection();
                                controlSpeedVector(300, direction * mRoller.getSpeed(), 0, new AnimEndResponse() {
                                    @Override
                                    public void onResponse() {
                                        mRoller.setSpeed(mRoller.SPEED_BACK);
                                        mRoller.setDirection(positiveDirection == RollerRecycler.SCROLL_DIRECTION_UP ? RollerRecycler.SCROLL_DIRECTION_DOWN : RollerRecycler.SCROLL_DIRECTION_UP);
                                    }
                                });
                            }
                        }
                    }

                }
            });

            //控制RollerRecycler 的高度，只展示一个item的高度
            RelativeLayout rl = containerView.findViewById(R.id.rl_parent_size);
            View parentSize =  mInflater.inflate(itemLayoutRes == -1?R.layout.layout_cell:itemLayoutRes,rl,false);
            rl.addView(parentSize);

            RelativeLayout.LayoutParams containerLp = (RelativeLayout.LayoutParams) containerView.getLayoutParams();
            containerLp.addRule(RelativeLayout.CENTER_IN_PARENT);
            containerView.setLayoutParams(containerLp);

            addView(containerView);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public void startRoll() {
        mTarget = -1;
        mRoller.startRoll(positiveDirection == RollerRecycler.SCROLL_DIRECTION_UP);
    }

    public void stopRoll(int target) {
        mTarget = target;
        int direction = mRoller.getDirection() ;
        controlSpeedVector(1000, direction * mRoller.getSpeed(), direction * mRoller.SPEED_MIN, null);
    }

    private void controlSpeedVector(long time, int startSpeed, int endSpeed, final AnimEndResponse response) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(startSpeed, endSpeed);
        valueAnimator.setDuration(time);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                mRoller.setSpeed(Math.abs(value));
                mRoller.setDirection(value < 0 ? RollerRecycler.SCROLL_DIRECTION_UP : RollerRecycler.SCROLL_DIRECTION_DOWN);
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

    public interface AnimEndResponse {
        void onResponse();
    }
}
