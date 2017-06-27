package cn.zp.zpexoplayer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.exoplayer2.C;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;

import cn.zp.zpexoplayer.R;
import cn.zp.zpexoplayer.application.MyApplication;
import cn.zp.zpexoplayer.model.MyPoint;
import cn.zp.zpexoplayer.model.MyTime;
import cn.zp.zpexoplayer.util.BitMapUtil;
import cn.zp.zpexoplayer.util.DeviceUtil;

public class TagEditDynamicTimeLine extends View {
    private String TAG = "TagEditDynamicTimeLine";
    private StringBuilder formatBuilder;
    private Formatter formatter;
    private Paint mPaint;
    private Paint mTextPaint;
    private Paint mLinePaint;
    private Bitmap bitmap;
    private Bitmap bitmapRedFlag;
    private float screenWidthSize;
    private int margenSize;
    private int timeLineHeight;//时间长线长度
    private int timeSortLineHeight;//时间短线长度
    private int bannerolHeight;
    private int topLineY;//最顶部线x坐标
    private int bottomLineY;//最底部线x坐标
    private int timeLongLineStartY;//时间轴上长竖线x开始坐标
    private int timeLongLineEndY;//时间轴上长竖线x结束坐标
    private int timeShortLineStartY;//时间轴上短竖线x结束坐标
    private int timeShortLineEndY;//时间轴上短竖线x结束坐标
    private int bannerolStartY;//旗帜的开始坐标
    private int bannerolEndY;//旗帜结束的Y坐标
    private int timeTextStartY;//旗帜结束的Y坐标
    private int textNumberStartY;//旗帜结束的Y坐标
    private int timeTextSize;//旗帜结束的Y坐标
    private int numberTextSize;//旗帜结束的Y坐标
    private long length;//
    private long currentPosition = 1;//当前选中的tag编号

    public TagEditDynamicTimeLine(Context context) {
        this(context, null);
    }

    public TagEditDynamicTimeLine(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagEditDynamicTimeLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        screenWidthSize = DeviceUtil.getScreenWidthSize(context);
        formatBuilder = new StringBuilder();
        formatter = new Formatter(formatBuilder, Locale.getDefault());
        initData();
    }

    private void initData() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#ebebeb"));
        mPaint.setStrokeWidth((float) DeviceUtil.dp2px(getContext(), 1));
        mPaint.setAntiAlias(true);// 抗锯齿
        mPaint.setDither(true);  // 防止抖动
        mPaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setAntiAlias(true);// 抗锯齿
        mTextPaint.setDither(true);  // 防止抖动
        mTextPaint.setStyle(Paint.Style.FILL);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.parseColor("#ebebeb"));
        mLinePaint.setStrokeWidth((float) DeviceUtil.dp2px(getContext(), 1));
        mLinePaint.setAntiAlias(true);// 抗锯齿
        mLinePaint.setDither(true);  // 防止抖动
        mLinePaint.setStyle(Paint.Style.FILL);

        margenSize = DeviceUtil.dp2px(getContext(), 10);
        timeLineHeight = DeviceUtil.dp2px(getContext(), 20);//时间长线长度
        timeSortLineHeight = DeviceUtil.dp2px(getContext(), 5);//时间短线长度
        bannerolHeight = DeviceUtil.dp2px(getContext(), 60);
        timeTextSize = DeviceUtil.dp2px(getContext(), 10);
        numberTextSize = DeviceUtil.dp2px(getContext(), 9);

        topLineY = 0;
        bottomLineY = margenSize * 2 + timeLineHeight + bannerolHeight;
        timeLongLineStartY = margenSize;
        timeLongLineEndY = margenSize + timeLineHeight;
        timeShortLineStartY = timeLongLineStartY + timeLineHeight - timeSortLineHeight;
        timeShortLineEndY = timeLongLineEndY;
        bannerolStartY = timeShortLineEndY;
        bannerolEndY = bannerolStartY + bannerolHeight;
        timeTextStartY = margenSize + (timeLineHeight - timeSortLineHeight) / 2;
        textNumberStartY = bannerolStartY + DeviceUtil.dp2px(getContext(), 9);


        bitmap = BitMapUtil.zoomImg(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.ic_flag),//
                DeviceUtil.dp2px(getContext(), 18), DeviceUtil.dp2px(getContext(), 60));

        bitmapRedFlag = BitMapUtil.zoomImg(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.ic_flag_red), //
                DeviceUtil.dp2px(getContext(), 18), DeviceUtil.dp2px(getContext(), 60));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0, topLineY, screenWidthSize, topLineY, mPaint);
        //画底部
        canvas.drawRect(0, bannerolStartY, screenWidthSize, bannerolEndY, mPaint); //在10,60处开始绘制图片
        //draw content
        drawContent(canvas, MyApplication.getTagProject().getMyTimes());
        //drawBottomLine
        canvas.drawLine(0, bottomLineY, screenWidthSize, bottomLineY, mPaint);
    }

    /**
     * 绘制中间部分
     *
     * @param canvas
     */
    private void drawContent(Canvas canvas, ArrayList<MyTime> myTimes) {
        for (int index = 0; index < myTimes.size(); index++) {
            long x = Math.round(myTimes.get(index).getNowX()) - length;
            if (x + myTimes.get(index).getWidth() < 0)
                continue;
            if (x > screenWidthSize)
                break;
            long nextX = x + Math.round(myTimes.get(index).getWidth() / 2);

            canvas.drawLine(x, timeLongLineStartY, x, timeLongLineEndY, mLinePaint);
            canvas.drawLine(nextX, timeShortLineStartY, nextX, timeShortLineEndY, mLinePaint);
            if (index == myTimes.size() - 1)
                canvas.drawLine(x + Math.round(myTimes.get(index).getWidth()), timeLongLineStartY, Math.round(x + myTimes.get(index).getWidth()), timeLongLineEndY, mLinePaint);

            mTextPaint.setTextSize(timeTextSize);
            long textTimeX = x + Math.round(((myTimes.get(index).getWidth() - mTextPaint.measureText(myTimes.get(index).getTimeText())) / 2));
            mTextPaint.setColor(Color.parseColor("#333333"));
            canvas.drawText(myTimes.get(index).getTimeText(), textTimeX, timeTextStartY, mTextPaint);
            //绘制旗帜和编号
            drawFlag(canvas, myTimes.get(index));
        }
    }

    /**
     * 绘制小旗
     *
     * @param canvas
     * @param myTime
     */
    private void drawFlag(Canvas canvas, MyTime myTime) {
        ArrayList<MyPoint> myPoints = myTime.getMyPoints();
        if (myPoints == null || myPoints.size() == 0)
            return;
        for (MyPoint point : myPoints) {
            int startX = Math.round(myTime.getNowX() - length + point.getStartX());
            int number = point.getNumber();
            int bitMapWidth = 0;
            bitMapWidth = (number == currentPosition) ? (bitmapRedFlag.getWidth()) : (bitmap.getWidth());
            canvas.drawBitmap((number == currentPosition) ? bitmapRedFlag : bitmap, startX, bannerolStartY, mPaint);
            mTextPaint.setTextSize(numberTextSize);
            int textNumberX = Math.round(startX + (bitMapWidth - mTextPaint.measureText(point.getNumber() + "")) / 2);
            mTextPaint.setColor(Color.parseColor((number == currentPosition) ? "#ffffff" : "#fc5f3e"));
            canvas.drawText(point.getNumber() + "", textNumberX, textNumberStartY, mTextPaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    /**
     * 刷新布局
     */
    public void refreshLayout(long currentPosition, long length) {
        this.currentPosition = currentPosition;
        this.length = length;
        invalidate();
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