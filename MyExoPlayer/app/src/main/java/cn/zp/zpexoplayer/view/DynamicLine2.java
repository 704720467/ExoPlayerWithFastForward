package cn.zp.zpexoplayer.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.exoplayer2.C;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.zp.zpexoplayer.R;
import cn.zp.zpexoplayer.model.MyTime;
import cn.zp.zpexoplayer.util.AdvancedCountdownTimer;
import cn.zp.zpexoplayer.util.DeviceUtil;

public class DynamicLine2 extends View {
    private String TAG = "DynamicLine2";
    private List<Map<String, Integer>> mListPoint = new ArrayList<Map<String, Integer>>();
    private ArrayList<MyTime> myTimes = new ArrayList<>();
    private MyDrawable myDrawable;
    private float mWaitRefreshTime;//等待刷新的时间
    int spacing = 5;
    float length;
    private Timer timer;
    TimerTask timerTask;
    private AdvancedCountdownTimer mCountDownTimer;// 系统用于倒计时控件
    private int count;
    StringBuilder formatBuilder;
    private Formatter formatter;
    private int timeWidth;
    private boolean isPlaying;//播放器是否在播放视频
    private boolean runIng;//timer是否在运行

    public DynamicLine2(Context context) {
        this(context, null);
    }

    public DynamicLine2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DynamicLine2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        myDrawable = new MyDrawable(DeviceUtil.getScreenWidthSize(context), BitmapFactory.decodeResource(getResources(), R.drawable.icon));
        myDrawable.setCallback(this);
        timeWidth = 100;
        length = timeWidth * spacing / 1000f;
        formatBuilder = new StringBuilder();
        formatter = new Formatter(formatBuilder, Locale.getDefault());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        myDrawable.setBounds(0, 0, getWidth(), getHeight());
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return who == myDrawable || super.verifyDrawable(who);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        myDrawable.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        myDrawable.onTouch(event);
        return super.onTouchEvent(event);
    }

    public void refreshView(float dou) {
        myDrawable.refreshView(dou);
    }

    /**
     * 设置时间
     *
     * @param duration
     */
    public void setData(long duration) {
        runIng = false;
        mWaitRefreshTime = duration;
        myTimes.clear();
        for (int i = 0; i * 1000 <= duration; i++) {
            MyTime m = new MyTime(null, i * 1000, stringForTime(i * 1000), timeWidth, i * timeWidth);
            Log.e("添加tga", "i=" + i);
            myTimes.add(m);
        }
        myDrawable.setMyTimes(myTimes);
    }


    /**
     * 控制或者停止大点时间轴移动
     *
     * @param isPlaying true ：移动，false：不移动
     */
    public void run(boolean isPlaying) {
        this.isPlaying = isPlaying;
        if (!runIng) {
            checkUpTimer();
            timer.schedule(timerTask, 0, spacing);
        }
    }

    /**
     * 检测定时器是否能用
     */
    private void checkUpTimer() {
        if (timer == null)
            timer = new Timer();
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (mWaitRefreshTime < 0 || !isPlaying)
                    return;
                runIng = true;
                refreshView(length);
                mWaitRefreshTime = mWaitRefreshTime - spacing;
                count++;
                Log.i(TAG, count + "次循环！");
            }
        };
    }


    /**
     * @param timeMs
     * @return
     */
    private String stringForTime(long timeMs) {
        if (timeMs == C.TIME_UNSET) {
            timeMs = 0;
        }
        long totalSeconds = (timeMs + 500) / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        formatBuilder.setLength(0);
        return formatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
    }
}