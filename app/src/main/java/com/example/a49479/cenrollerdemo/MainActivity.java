package com.example.a49479.cenrollerdemo;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;

import com.example.a49479.cenrollerdemo.customView.rollerView.RollerAdapter;

import java.lang.ref.WeakReference;

public class MainActivity extends Activity {

    private RecyclerView recycler;

    private Button btn_locate;

    private Button btn_scroll;

    private EditText edit;

    private int speed = SPEED_MAX;

    private static final int SPEED_MAX = -30;

    private static final int SPEED_MIN = -10;

    private static final int SPEED_BACK = 5;

    private int mTarget = 3;

    private int mTargetPosition = -1;

    private LinearLayoutManager linearLayoutManager;

    private static final int UI_REFRESH = 0x1001;

    protected UIHandler mUIHandler = new UIHandler(MainActivity.this);

    protected class UIHandler extends Handler {
        private final WeakReference<Activity> mActivityReference;

        public UIHandler(Activity activity) {
            mActivityReference = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final Activity activity = mActivityReference.get();
            if (activity == null) {
                return;
            }
            if (activity.isFinishing()) {
                return;
            }
            switch (msg.what) {
                case UI_REFRESH:
                    recycler.scrollBy(0, 0 - speed);
                    sendEmptyMessageDelayed(UI_REFRESH, 10);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycler = (RecyclerView) findViewById(R.id.recycler);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler.setAdapter(new RollerAdapter(MainActivity.this));
        recycler.setLayoutManager(linearLayoutManager);
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
                if (mTargetPosition != -1 && speed <0)
                    return;
                int position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                int value = position % 10;
                if (speed < 0) {
                    if (value == mTarget) {
                        if (Math.abs(speed) <= Math.abs(SPEED_MIN)) {
                            mTargetPosition = position;
                            controlSpeed(600, SPEED_MIN, 0, new AnimEndResponse() {
                                @Override
                                public void onResponse() {
                                    speed = SPEED_BACK;
                                }
                            });
                        }
                    }
                } else if (speed >0 && value == mTarget) {
                    speed = 0;
                    mUIHandler.removeMessages(UI_REFRESH);
                }
            }
        });

        btn_locate = (Button) findViewById(R.id.btn_locate);
        btn_locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTarget = Integer.parseInt(edit.getText().toString());
                controlSpeed(1000, SPEED_MAX, SPEED_MIN, null);
            }
        });

        btn_scroll = (Button) findViewById(R.id.btn_scroll);
        btn_scroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speed = SPEED_MAX;
                mUIHandler.sendEmptyMessage(UI_REFRESH);
                mTargetPosition = -1;
            }
        });

        edit = (EditText)findViewById(R.id.et_locate);
    }


    public void controlSpeed(long time, int startSpeed, int endSpeed, final AnimEndResponse response) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(startSpeed, endSpeed);
        valueAnimator.setDuration(time);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                speed = value;
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
