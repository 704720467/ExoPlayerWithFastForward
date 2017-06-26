package cn.zp.zpexoplayer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.exoplayer2.C;

import java.util.Formatter;
import java.util.Locale;

import cn.zp.zpexoplayer.R;

/**
 * Created by zp on 2017/6/19.
 */

public class SeekBarLinearLayout extends LinearLayout {

    private View mView;
    private SeekBar mSeekBar;
    private TextView mTvPosition;
    private TextView mTvDuration;
    StringBuilder formatBuilder;
    private Formatter formatter;
    private MySeekBarListener mySeekBarListener;
    private ComponentListener componentListener;
    private long videoDuration = 0;//视频的长度


    public SeekBarLinearLayout(Context context) {
        super(context);
        initView();
    }

    public SeekBarLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SeekBarLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        formatBuilder = new StringBuilder();
        formatter = new Formatter(formatBuilder, Locale.getDefault());
        componentListener = new ComponentListener();
        LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = (LinearLayout) mInflater.inflate(R.layout.seekbar_lay, this, false);
        mTvDuration = (TextView) mView.findViewById(R.id.exo_duration);
        mTvPosition = (TextView) mView.findViewById(R.id.exo_position);
        mSeekBar = (SeekBar) mView.findViewById(R.id.exo_progress);
        mSeekBar.setOnSeekBarChangeListener(componentListener);
        mSeekBar.setMax(1000);
        addView(mView);
    }

    public void setMySeekBarListener(MySeekBarListener mySeekBarListener) {
        this.mySeekBarListener = mySeekBarListener;
    }


    /**
     * 布局点击监听
     */
    public interface MySeekBarListener {
        void seekTo(long position);
    }

    private boolean dragging;

    private final class ComponentListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            dragging = true;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                long position = positionValue(progress);
                if (mTvPosition != null) {
                    mTvPosition.setText(stringForTime(position));
                }
                if (!dragging && mySeekBarListener != null) {
                    mySeekBarListener.seekTo(position);
                }
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            dragging = false;
            if (mySeekBarListener != null) {
                mySeekBarListener.seekTo(positionValue(seekBar.getProgress()));
            }
        }

    }

    /**
     * 设置进度条的当前进度
     *
     * @param progress （单位毫秒）
     */
    public void setSeekBarProgress(long progress) {
        if (!dragging && mSeekBar != null)
            mSeekBar.setProgress(progressBarValue(progress));
    }


    public void setmTvPosition(long position) {
        if (!dragging && mTvPosition != null)
            mTvPosition.setText(stringForTime(position));
        if (!dragging && mSeekBar != null)
            mSeekBar.setProgress(progressBarValue(position));
    }

    public void setmTvDuration(long duration) {
        this.videoDuration = duration;
        if (!dragging && mTvDuration != null)
            mTvDuration.setText(stringForTime(videoDuration));
    }

    public void setVideoDuration(long duration, long position) {
        this.videoDuration = duration;
        setmTvPosition(position);
        setmTvDuration(videoDuration);
    }


    public long positionValue(int progress) {
        long duration = mSeekBar == null ? C.TIME_UNSET : videoDuration;
        return duration == C.TIME_UNSET ? 0 : ((duration * progress) / 1000);
    }

    public int progressBarValue(long position) {
        long duration = mSeekBar == null ? C.TIME_UNSET : videoDuration;
        return duration == C.TIME_UNSET || duration == 0 ? 0 : (int) ((position * 1000) / duration);
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
