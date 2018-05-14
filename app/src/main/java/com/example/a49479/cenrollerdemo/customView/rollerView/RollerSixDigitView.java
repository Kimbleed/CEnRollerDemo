package com.example.a49479.cenrollerdemo.customView.rollerView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.a49479.cenrollerdemo.R;

/**
 * Created by 49479 on 2018/5/14.
 */

public class RollerSixDigitView extends LinearLayout {

    private LinearLayout mLinearLayout;

    private CEnSingleRollerView mRollerViewArr[] = new CEnSingleRollerView[6];

    private CEnSingleRollerView.RollerStopResponse mRollerStopResponseArr[] = new CEnSingleRollerView.RollerStopResponse[6];

    private boolean mRollerStates[] = new boolean[6];

    private RollerSixDigitStopResponse mRollerSixDigitStopResponse;

    public RollerSixDigitView(Context context) {
        super(context);
    }

    public RollerSixDigitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RollerSixDigitView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mLinearLayout == null) {
            mLinearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_6digit, this, false);
            mRollerViewArr[0] = mLinearLayout.findViewById(R.id.rv_1);
            mRollerViewArr[1] = mLinearLayout.findViewById(R.id.rv_2);
            mRollerViewArr[2] = mLinearLayout.findViewById(R.id.rv_3);
            mRollerViewArr[3] = mLinearLayout.findViewById(R.id.rv_4);
            mRollerViewArr[4] = mLinearLayout.findViewById(R.id.rv_5);
            mRollerViewArr[5] = mLinearLayout.findViewById(R.id.rv_6);
            addView(mLinearLayout);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 停止转动
     *
     * @param content
     */
    public void stopRoll(String content) {
        if (content.length() != 6) {
            return;
        }
        for (int i = 0; i < content.length(); i++) {
            mRollerViewArr[i].stopOnTarget(content.charAt(i) + "");
        }
    }

    /**
     * 开始滚动
     */
    public void startRoll(RollerSixDigitStopResponse rollerSixDigitStopResponse) {
        mRollerSixDigitStopResponse = rollerSixDigitStopResponse;
        for (int i = 0; i < mRollerViewArr.length; i++) {
            final int index = i;
            mRollerStopResponseArr[index] = new CEnSingleRollerView.RollerStopResponse() {
                @Override
                public void onResponse() {
                    mRollerStates[index] = false;
                    for (boolean flag : mRollerStates) {
                        if (flag)
                            return;
                    }
                    if (mRollerSixDigitStopResponse != null)
                        mRollerSixDigitStopResponse.onResponse();
                }
            };
            mRollerViewArr[index].startRoll(mRollerStopResponseArr[i]);
            mRollerStates[index] = true;
        }
    }

    public interface RollerSixDigitStopResponse {
        void onResponse();
    }

}
