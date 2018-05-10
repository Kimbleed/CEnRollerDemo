package com.example.a49479.cenrollerdemo;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class MainActivity extends Activity {

    private RecyclerView recycler;

    private Button btn_locate;

    private Button btn_scroll;

    private EditText edit;

    private int speed = SPEED_MAX;

    private static final int SPEED_MAX = -30;

    private static final int SPEED_MIN = -10;

    private static final int SPEED_BACK = 15;

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

    class RollerAdapter extends RecyclerView.Adapter<RollerViewHolder> {

        private String[] mCells = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        private LayoutInflater mInflater;

        public RollerAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public RollerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.layout_cell, parent, false);
            RollerViewHolder viewHolder = new RollerViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RollerViewHolder holder, int position) {
            holder.tv.setText(mCells[position % mCells.length]);
        }

        @Override
        public int getItemCount() {
            return Integer.MAX_VALUE;
        }
    }

    class RollerViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public RollerViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }

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
