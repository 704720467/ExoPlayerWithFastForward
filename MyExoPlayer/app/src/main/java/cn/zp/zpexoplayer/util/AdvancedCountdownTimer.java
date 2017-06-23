package cn.zp.zpexoplayer.util;

import android.os.Handler;
import android.os.Message;

/**
 * Created by zp on 2017/3/23.
 * <p>
 * 倒计时工具，带有快进、暂停、恢复、开始、取消。根据系统自带的CountDownTimer写的。
 */


public abstract class AdvancedCountdownTimer {
    private String TAG = "AdvancedCountdownTimer";
    private final long mCountdownInterval;

    private long mTotalTime;

    private long mRemainTime;
    private int timerStart = 0;


    public AdvancedCountdownTimer(long millisInFuture, long countDownInterval) {
        mTotalTime = millisInFuture;
        mCountdownInterval = countDownInterval;

        mRemainTime = millisInFuture;
    }

    /**
     * 返回当前计时器的状态
     *
     * @return
     */
    public int getTimerStart() {
        return timerStart;
    }

    /**
     * 返回当前还剩下的秒数
     *
     * @return
     */
    public int getNewTime() {
        return new Long(mTotalTime - mRemainTime).intValue();
    }

    public final void seek(int value) {
        synchronized (AdvancedCountdownTimer.this) {
            mRemainTime = ((100 - value) * mTotalTime) / 100;
        }
    }


    public final void cancel() {
        mHandler.removeMessages(MSG_RUN);
        mHandler.removeMessages(MSG_PAUSE);
    }

    public final void resume() {
        mHandler.removeMessages(MSG_PAUSE);
        mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_RUN));
    }

    public final void pause() {
        mHandler.removeMessages(MSG_RUN);
        mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_PAUSE));
    }

    public synchronized final AdvancedCountdownTimer start() {
        if (mRemainTime <= 0) {
            onFinish();
            return this;
        }
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_RUN), mCountdownInterval);
        return this;
    }

    public abstract void onTick(long millisUntilFinished, int percent);


    public abstract void onFinish();

    private static final int MSG_RUN = 1;
    public static final int MSG_PAUSE = 2;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            synchronized (AdvancedCountdownTimer.this) {
                if (msg.what == MSG_RUN) {
                    timerStart = MSG_RUN;
                    mRemainTime = mRemainTime - mCountdownInterval;
                    if (mRemainTime <= 0) {
                        onFinish();
                    } else if (mRemainTime < mCountdownInterval) {
                        sendMessageDelayed(obtainMessage(MSG_RUN), mRemainTime);
                    } else {

                        onTick(mRemainTime, new Long(100 * (mTotalTime - mRemainTime) / mTotalTime).intValue());


                        sendMessageDelayed(obtainMessage(MSG_RUN), mCountdownInterval);
                    }
                } else if (msg.what == MSG_PAUSE) {
                    timerStart = MSG_PAUSE;
                    MyLogUtil.showLogE(TAG, "计时器--暂停");
                }
            }
        }
    };


}