package cn.zp.zpexoplayer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.android.exoplayer2.C;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.zp.zpexoplayer.model.MyPoint;
import cn.zp.zpexoplayer.model.MyTime;
import cn.zp.zpexoplayer.util.AdvancedCountdownTimer;
import cn.zp.zpexoplayer.util.DeviceUtil;

public class DynamicLine2 extends View {
    private String TAG = "DynamicLine2";
    private List<Map<String, Integer>> mListPoint = new ArrayList<Map<String, Integer>>();
    private ArrayList<MyTime> myTimes = new ArrayList<>();
    private MyDrawable myDrawable;
    private long mWaitRefreshTime;//等待刷新的时间
    long spacing = 20;
    int length;
    private Timer timer;
    TimerTask timerTask;
    private AdvancedCountdownTimer mCountDownTimer;// 系统用于倒计时控件
    private int count = 0;
    StringBuilder formatBuilder;
    private Formatter formatter;
    private int timeWidth;
    private boolean isPlaying;//播放器是否在播放视频
    private boolean runIng;//timer是否在运行
    private float helfScreenWidth;
    private SeekBarLinearLayout mSeekBarLinearLayout;
    private float MAX_MOVE_LENG;//移动的最大距离
    private int stepLength;

    public DynamicLine2(Context context) {
        this(context, null);
    }

    public DynamicLine2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DynamicLine2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        timeWidth = Math.round(DeviceUtil.dp2px(getContext(), 50));
        stepLength = length = Math.round(timeWidth * spacing / 1000f);
        helfScreenWidth = DeviceUtil.getScreenWidthSize(context) / 2.0f;
        myDrawable = new MyDrawable(context, DeviceUtil.getScreenWidthSize(context));
        myDrawable.setCallback(this);
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
        //        myDrawable.refreshView(timeWidth * dou / 1000f);
        myDrawable.refreshView(Math.round(dou));
    }

    public void setmWaitRefreshLength(long mWaitRefreshLength) {
        myDrawable.setmWaitRefreshLength(Math.round(timeWidth * mWaitRefreshLength / 1000f));
        //        myDrawable.setmWaitRefreshLength(length * count);
        updataSeekBarUI(mWaitRefreshLength);
    }

    /**
     * 设置时间
     *
     * @param duration
     */
    public void setData(long duration) {
        runIng = false;
        mWaitRefreshTime = duration;
        if (mSeekBarLinearLayout != null)
            mSeekBarLinearLayout.setVideoDuration(duration, 0);
        myTimes.clear();
        for (int i = 0; i * 1000 <= duration; i++) {
            MyTime m = new MyTime(null, (i + 1) * 1000, stringForTime((i + 1) * 1000), timeWidth, helfScreenWidth + (i) * timeWidth);
            Log.e("添加tga", "i=" + i);
            myTimes.add(m);
        }
        MyTime lastTime = myTimes.get(myTimes.size() - 1);
        lastTime.setTime(duration);
        lastTime.setWidth(Math.round(timeWidth * (duration % 1000) / 1000f));
        lastTime.setX(Math.round(timeWidth * duration / 1000f));
        myDrawable.setMyTimes(myTimes);
    }

    /**
     * 添加tag
     *
     * @param tagTime 点击tag的时间
     */
    private long startTime;

    public synchronized void addTag(long tagTime) {
        long newTime = System.currentTimeMillis();
        if (newTime - startTime < 1000)
            return;
        startTime = newTime;
        boolean tagExist = false;
        int existTagCount = 0;//已经存在多少个tag了
        for (int i = 0; i < myTimes.size(); i++) {
            MyTime myTime = myTimes.get(i);

            if (!tagExist && myTime.getTime() >= tagTime) {
                long relativeTime = tagTime % 1000;
                float startX = timeWidth * relativeTime / 1000f;
                Log.e("=====>", "添加tag，i=" + i + ";tagTime=" + tagTime + ";relativeTime=" + relativeTime + ";startX=" + startX);
                MyPoint myPoint = new MyPoint(tagTime, relativeTime, startX);
                myTime.setMyPoint(myPoint);
                tagExist = true;
            }

            if (myTime.getMyPoints() == null)
                continue;
            //对已有的tag进行编号排序
            myTime.sortAndSetNumber(existTagCount);
            existTagCount += myTime.getMyPoints().size();
        }

        final int tagCount = existTagCount;
        if (tagExist) {
            tagView.post(new Runnable() {
                @Override
                public void run() {
                    //Main Thread
                    tagView.setText(String.valueOf(tagCount));
                }
            });
        }
    }


    /**
     * 控制或者停止大点时间轴移动
     *
     * @param isPlaying true ：移动，false：不移动
     */
    public void run(boolean isPlaying) {
        this.isPlaying = isPlaying;
        if (!runIng) {
            //            checkUpTimer();
            //            timer.schedule(timerTask, 0, spacing);
        }
    }

    private long oldTime;

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
                long newTime = System.currentTimeMillis();
                if ((mWaitRefreshTime - spacing * count) <= 0 || !isPlaying) {
                    return;
                }
                runIng = true;
                count = count + stepLength;
                setmWaitRefreshLength(spacing * count);
                Log.e("", "要刷新的长度为length=" + length + ";count=" + count + ";耗时：" + (newTime - oldTime));
                oldTime = newTime;
            }
        };
    }

    /**
     * 跟新Seekbar  Ui
     *
     * @param mWaitRefreshLength
     */
    private void updataSeekBarUI(final long mWaitRefreshLength) {
        if (mSeekBarLinearLayout != null) {
            mSeekBarLinearLayout.post(new Runnable() {

                @Override
                public void run() {
                    mSeekBarLinearLayout.setmTvPosition(mWaitRefreshLength);
                }

            });
        }
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

    public void setSeekBarLayout(SeekBarLinearLayout mSeekBarLinearLayout) {
        this.mSeekBarLinearLayout = mSeekBarLinearLayout;
    }

    private TextView tagView;

    public void setTagView(TextView tagView) {
        this.tagView = tagView;
    }
}