package cn.zp.zpexoplayer.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import cn.zp.zpexoplayer.R;

/**
 * Created by zp on 2017/6/19.
 */

public class SpeedLinearLayout extends LinearLayout implements View.OnClickListener {

    private ImageView mImageView;
    private TextView mTextView;
    public float minimumNumValue;
    public float maximumNumValue;
    private float num;
    private MyCircleLinearLayListener mMyCircleLinearLayListener;

    public SpeedLinearLayout(Context context) {
        super(context);
        num = minimumNumValue = 1.0f;
        maximumNumValue = 2.0f;
        initView();
    }

    public SpeedLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        num = minimumNumValue = 1.0f;
        maximumNumValue = 2.0f;
        initView();
    }

    public SpeedLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        num = minimumNumValue = 1.0f;
        maximumNumValue = 2.0f;
        initView();
    }

    private void initView() {
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);
        setBackgroundColor(getResources().getColor(R.color.white));
        mImageView = new ImageView(getContext());
        LayoutParams mImageLP = new LayoutParams(dpToPx(74), dpToPx(74));
        mImageView.setLayoutParams(mImageLP);
        mImageView.setImageResource(R.mipmap.ic_one_speed);
        mImageView.setOnClickListener(this);

        mTextView = new TextView(getContext());
        LayoutParams mTextLP = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mTextLP.setMargins(0, dpToPx(-30), 0, 0);
        mTextView.setLayoutParams(mTextLP);
        mTextView.setText((int) num + "X");
        mTextView.setTextColor(getResources().getColor(R.color.white));
        mTextView.setOnClickListener(this);

        setFocusable(true);
        setFocusableInTouchMode(true);

        addView(mImageView, 0);
        addView(mTextView, 1);
    }


    @Override
    public void onClick(View v) {
        plusMinusNumber();
    }

    public void setMyCircleLinearLayListener(MyCircleLinearLayListener mMyCircleLinearLayListener) {
        this.mMyCircleLinearLayListener = mMyCircleLinearLayListener;
    }


    /**
     * 布局点击监听
     */
    public interface MyCircleLinearLayListener {
        void onNumChange(View view, float num);
    }

    // ------------------------------K------------------------------@Calculate
    private void plusMinusNumber() {
        String numString = mTextView.getText().toString();
        if (TextUtils.isEmpty(numString)) {
            num = minimumNumValue;
        } else {
            num = (num == minimumNumValue) ? maximumNumValue : minimumNumValue;
        }

        if (mMyCircleLinearLayListener != null) {
            mMyCircleLinearLayListener.onNumChange(SpeedLinearLayout.this, num);
        }
        if (mImageView != null)
            mImageView.setImageResource(num == minimumNumValue ? R.mipmap.ic_one_speed : R.mipmap.ic_two_speed);
        mTextView.setText(String.valueOf((int) num) + "X");
    }


    // ============================@Timer@============================Auto Plus/Minus
    private Timer mTimer;
    private TimerTask mTimerTask;
    private boolean mTimerStart = false;
    // View
    private View curLongClickView;

    public void startTimer() {
        if (mTimerStart) {
            return;
        }
        if (mTimer == null)
            mTimer = new Timer();
        mTimerTask = new MyTimerTask();
        mTimer.schedule(mTimerTask, 0, 300);
        mTimerStart = true;
    }

    public void stopTimer() {
        if (!mTimerStart) {
            return;
        }
        mTimerStart = false;
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
            mTimer = null;
        }
    }

    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            // Child Thread
            if (curLongClickView != null)
                curLongClickView.post(new Runnable() {
                    @Override
                    public void run() {
                        //Main Thread
                        plusMinusNumber();
                    }
                });
        }
    }

    // ------------------------------K------------------------------@Assist
    public int dpToPx(float dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics()));
    }

    private Toast toast;

    private void showToast(String str) {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(getContext(), str, Toast.LENGTH_SHORT);
        toast.show();
    }
}
