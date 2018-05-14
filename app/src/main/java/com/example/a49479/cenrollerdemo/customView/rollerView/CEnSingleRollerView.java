package com.example.a49479.cenrollerdemo.customView.rollerView;

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
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.example.a49479.cenrollerdemo.R;

/**
 * Created by 49479 on 2018/5/10.
 */

public class CEnSingleRollerView extends RelativeLayout {

    private Context mContext;

    //装载元素item 的 Recycler
    private RollerRecycler mRoller;

    private LinearLayoutManager mLinearLayoutManager;

    private LayoutInflater mInflater;

    //显示的元素
    private String arrDisplay[];

    //加载中的滚动速度
    private int loadingSpeed = -1;

    //指定目标后，回弹速度
    private int springbackSpeed = -1;

    //item布局 res
    private int itemLayoutRes = -1;

    //滚动的起点index
    private int mStartIndex = 0;

    // 滚轮滚动的正方向
    private int positiveDirection = RollerRecycler.SCROLL_DIRECTION_UP;

    //从加载中的滚动速度 减速到 最小值 的 时间
    private int decelerationTime = 1000;

    //指定目标后，回弹加速的时间
    private int springbackAccelerateTime = 300;

    //目标数字
    private int mTargetIndex = -1;

    private RollerStopResponse rollerStopResponse;

    public CEnSingleRollerView(Context context) {
        super(context);
    }

    public CEnSingleRollerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        initAttrs(context,attrs);
    }

    public CEnSingleRollerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        initAttrs(context,attrs);
    }

    /**
     * 初始化
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    /**
     * 获取xml 中设值
     * @param context
     * @param attrs
     */
    public void initAttrs(Context context,AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CEnSingleRollerView);
        itemLayoutRes = a.getResourceId(R.styleable.CEnSingleRollerView_item_layout, R.layout.layout_cell);
        positiveDirection = a.getInteger(R.styleable.CEnSingleRollerView_roll_direction,RollerRecycler.SCROLL_DIRECTION_UP);
        loadingSpeed = a.getInteger(R.styleable.CEnSingleRollerView_loading_speed,30);
        springbackSpeed = a.getInteger(R.styleable.CEnSingleRollerView_springback_speed,10);
        mStartIndex = a.getInteger(R.styleable.CEnSingleRollerView_start_index,0);
        decelerationTime = a.getInteger(R.styleable.CEnSingleRollerView_decelerate_time,1000);
        springbackAccelerateTime = a.getInteger(R.styleable.CEnSingleRollerView_springback_acc_time,300);
        arrDisplay = context.getResources().getStringArray(a.getResourceId(R.styleable.CEnSingleRollerView_display_value,R.array.number_arr));
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mRoller == null) {
            //RollerRecycler 的 配置
            View containerView = mInflater.inflate(R.layout.layout_recycler, this, false);
            mRoller = containerView.findViewById(R.id.recycler);
            mRoller.setSPEED_MAX(loadingSpeed);
            mRoller.setSPEED_BACK(springbackSpeed);
            mRoller.setClickable(false);

            mRoller.setAdapter(itemLayoutRes == -1 ? new RollerAdapter(getContext()) : new RollerAdapter(getContext(), itemLayoutRes, arrDisplay));
            mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, positiveDirection != RollerRecycler.SCROLL_DIRECTION_UP
            );
            mRoller.setLayoutManager(mLinearLayoutManager);

            mRoller.scrollToPosition(mStartIndex);

            mRoller.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {

                    int position = mLinearLayoutManager.findFirstVisibleItemPosition()+1;
                    int index = position % arrDisplay.length;


                    if (mTargetIndex != -1) {
                        if (index == mTargetIndex && mRoller.getDirection() != positiveDirection) {
                            Log.i(CEnSingleRollerView.class.getName(),"target value:"+ mTargetIndex);
                            mRoller.stopRoll();
                            mRoller.scrollToPosition(position);
                            if(rollerStopResponse!=null)
                                rollerStopResponse.onResponse();
                            return;
                        }
                    }

                    //正在加载的滚动方向
                    if (mRoller.getDirection() == positiveDirection) {
                        if (index == mTargetIndex) {
                            if (mRoller.getSpeed() <= Math.abs(mRoller.SPEED_MIN)) {
                                //滚动停止，停止后，反向
                                int direction = mRoller.getDirection();
                                controlSpeedVector(springbackAccelerateTime, direction * mRoller.getSpeed(), 0, new AnimEndResponse() {
                                    @Override
                                    public void onResponse() {
                                        mRoller.setSpeed(mRoller.SPEED_BACK);
                                        mRoller.setDirection(positiveDirection == RollerRecycler.SCROLL_DIRECTION_UP ? RollerRecycler.SCROLL_DIRECTION_DOWN : RollerRecycler.SCROLL_DIRECTION_UP);
                                    }
                                });
                            }
                        }
                        else if(mRoller.getSpeed()>=Math.abs(mRoller.SPEED_MAX) && (index+1)%arrDisplay.length == mTargetIndex){
                            decelerateLoadingSpeed();
                        }
                    }

                }
            });

            //控制RollerRecycler 的高度，只展示一个item的高度
            RelativeLayout rl = containerView.findViewById(R.id.rl_parent_size);
            View parentSize =  mInflater.inflate(itemLayoutRes == -1?R.layout.layout_cell:itemLayoutRes,rl,false);
            rl.addView(parentSize);

            //居中
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

    /**
     * 开始滚动
     */
    public void startRoll(RollerStopResponse response) {
        rollerStopResponse = response;
        mTargetIndex = -1;
        mRoller.startRoll(positiveDirection == RollerRecycler.SCROLL_DIRECTION_UP);
    }

    /**
     * 停止滚动，停在指定位置
     * @param targetIndex
     */
    public void stopOnTarget(int targetIndex) {
        mTargetIndex = targetIndex;
//        decelerateLoadingSpeed();
    }

    /**
     * 停止滚动，停在指定字符
     * @param targetStr
     */
    public void stopOnTarget(String targetStr) {
        for(int i=0;i<arrDisplay.length;i++){
            if(targetStr.equals(arrDisplay[i])){
                mTargetIndex = i;
                break;
            }
        }
//        int direction = mRoller.getDirection() ;
//        controlSpeedVector(decelerationTime, direction * mRoller.getSpeed(), direction * mRoller.SPEED_MIN, null);
    }

    /**
     * 降速
     */
    private void decelerateLoadingSpeed(){
        int direction = mRoller.getDirection() ;
        controlSpeedVector(decelerationTime, direction * mRoller.getSpeed(), direction * mRoller.SPEED_MIN, null);
    }



    /**
     * 控制滚动的 矢量速度 ： speed 速度大小  direction 方向
     * @param time
     * @param startSpeed
     * @param endSpeed
     * @param response
     */
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

    public int getRollDirection(){
        return mRoller.getDirection();
    }

    public interface AnimEndResponse {
        void onResponse();
    }

    public interface RollerStopResponse{
        void onResponse();
    }
}
