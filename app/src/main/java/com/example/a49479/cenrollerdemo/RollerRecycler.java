package com.example.a49479.cenrollerdemo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by 49479 on 2018/5/10.
 */

public class RollerRecycler extends RecyclerView {

    private int mRollSpeed = SPEED_MAX;

    public static final int SPEED_MAX = 30;
    public static final int SPEED_MIN = 10;
    public static final int SPEED_BACK = 5;

    public static final int SCROLL_DIRECTION_UP = -1;       // 速度 mRollSpeed <0  方向 向上滚动
    public static final int SCROLL_DIRECTION_DOWN = 1;     // 速度 mRollSpeed >0  方向 向下滚动
    public static final int SCROLL_STOP = 0;

    private static int mDirection =SCROLL_STOP;

    public RollerRecycler(Context context) {
        super(context);
    }

    public RollerRecycler(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RollerRecycler(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void computeScroll() {
        if(mDirection == SCROLL_STOP)
            return;
        scrollBy(0, -getSpeedVector());
        postInvalidate();
    }

    /**
     * 开始滚动
     */
    public void startRoll(boolean isUpDirection){
        mDirection = isUpDirection?SCROLL_DIRECTION_UP:SCROLL_DIRECTION_DOWN;
        mRollSpeed = SPEED_MAX;
        invalidate();
    }


    /**
     * 停止滚动
     */
    public void stopRoll(){
        mDirection = SCROLL_STOP;
    }

    /**
     * 获取滚动速率(没有方向，为正数)
     * @return
     */
    public int getSpeed(){
        return mRollSpeed;
    }

    /**
     * 设置滚动速率(没有方向，为正数)
     * @param speed
     */
    public void setSpeed(int speed){
        mRollSpeed = speed;
    }

    /**
     * 获取滚动方向
     * @return  mDirection   0.停止滚动  1.向上滚动(item向上移动)  2.向下滚动(item向下移动)
     */
    public int getDirection(){
        return mDirection;
    }

    /**
     * 设置滚动方向
     * @param direction
     */
    public void setDirection( int direction){
        mDirection= direction;
    }

    /**
     * 获取滚动速度 (向量)
     * @return
     */
    public int getSpeedVector(){
        if(mDirection == SCROLL_DIRECTION_UP){
            return -mRollSpeed;
        }
        else {
            return mRollSpeed;
        }
    }

}
