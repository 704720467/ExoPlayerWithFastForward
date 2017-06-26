package cn.zp.zpexoplayer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.view.MotionEvent;

import java.util.ArrayList;

import cn.zp.zpexoplayer.R;
import cn.zp.zpexoplayer.model.MyPoint;
import cn.zp.zpexoplayer.model.MyTime;
import cn.zp.zpexoplayer.util.DeviceUtil;
import cn.zp.zpexoplayer.util.MyLogUtil;

/**
 * Created by zp on 2017/6/23.
 */

public class MyDrawable extends Drawable {

    private String TAG = "MyDrawable";
    private ArrayList<MyTime> myTimes = new ArrayList<>();
    private Paint mPaint;
    private Paint mTextPaint;
    private Paint mLinePaint;
    private Paint mRectPaint;
    private Bitmap bitmap;
    private Bitmap verticalLineBt;
    private float bitMapWidth;
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


    private Context context;


    public MyDrawable(Context context, int screenWidthSize) {
        this.context = context;
        this.screenWidthSize = screenWidthSize;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#ebebeb"));
        mPaint.setStrokeWidth((float) DeviceUtil.dp2px(context, 1));
        mPaint.setAntiAlias(true);// 抗锯齿
        mPaint.setDither(true);  // 防止抖动
        mPaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setAntiAlias(true);// 抗锯齿
        mTextPaint.setDither(true);  // 防止抖动
        mTextPaint.setStyle(Paint.Style.FILL);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.parseColor("#ebebeb"));
        mLinePaint.setStrokeWidth((float) DeviceUtil.dp2px(context, 1));
        mLinePaint.setAntiAlias(true);// 抗锯齿
        mLinePaint.setDither(true);  // 防止抖动
        mLinePaint.setStyle(Paint.Style.FILL);

        margenSize = DeviceUtil.dp2px(context, 10);
        timeLineHeight = DeviceUtil.dp2px(context, 20);//时间长线长度
        timeSortLineHeight = DeviceUtil.dp2px(context, 5);//时间短线长度
        bannerolHeight = DeviceUtil.dp2px(context, 60);
        timeTextSize = DeviceUtil.dp2px(context, 10);
        numberTextSize = DeviceUtil.dp2px(context, 9);

        topLineY = 0;
        bottomLineY = margenSize * 2 + timeLineHeight + bannerolHeight;
        timeLongLineStartY = margenSize;
        timeLongLineEndY = margenSize + timeLineHeight;
        timeShortLineStartY = timeLongLineStartY + timeLineHeight - timeSortLineHeight;
        timeShortLineEndY = timeLongLineEndY;
        bannerolStartY = timeShortLineEndY;
        bannerolEndY = bannerolStartY + bannerolHeight;
        timeTextStartY = margenSize + (timeLineHeight - timeSortLineHeight) / 2;
        textNumberStartY = bannerolStartY + DeviceUtil.dp2px(context, 9);


        this.bitmap = zoomImg(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_flag), DeviceUtil.dp2px(context, 18), DeviceUtil.dp2px(context, 60));
        bitMapWidth = this.bitmap.getWidth();
        verticalLineBt = zoomImg(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_red_line), DeviceUtil.dp2px(context, 1), DeviceUtil.dp2px(context, 60));
    }

    public void onTouch(MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                onTouchDown(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                onTouchMove(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                onTouchUp(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_CANCEL:
                onTouchCancle(event.getX(), event.getY());
                break;
        }
    }


    private void onTouchDown(float x, float y) {
        MyLogUtil.showLogI(TAG, "手按下了" + ";mEnterDone=");
    }

    private void onTouchMove(float x, float y) {

    }

    private void onTouchUp(float x, float y) {
        MyLogUtil.showLogI(TAG, "收抬起了" + ";mEnterDone=");
    }

    private void onTouchCancle(float x, float y) {
        MyLogUtil.showLogI(TAG, "手取消了：" + ";mEnterDone=");
    }

    private void startEnterRunable() {
        unscheduleSelf(mRunnable);
        scheduleSelf(mRunnable, SystemClock.uptimeMillis()); // 注入一个进入动画
    }

    private Runnable mRunnable = new Runnable() {
        public void run() {
            invalidateSelf();
            scheduleSelf(mRunnable, SystemClock.uptimeMillis() + 16);
        }
    };


    public void setMyTimes(ArrayList<MyTime> myTimes) {
        this.myTimes.clear();
        this.myTimes.addAll(myTimes);
    }

    private long oldTime;
    private int length;

    public void refreshView(int dou) {
        long newTime = System.currentTimeMillis();
        //length = length + dou;
        //Log.e("===================>", "要刷新的长度为length=" + length + ";步长=" + dou + ";耗时：" + (newTime - oldTime));
        oldTime = newTime;
    }


    public void setmWaitRefreshLength(int mWaitRefreshLength) {
        length = mWaitRefreshLength;
    }

    /**
     * 界面大小改变时触发，我们在这里运算中心点，以及半径
     */
    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        startEnterRunable();
    }


    @Override
    public void draw(Canvas canvas) {
        //drawTopLine
        canvas.drawLine(0, topLineY, screenWidthSize, topLineY, mPaint);
        //画底部
        canvas.drawRect(0, bannerolStartY, screenWidthSize, bannerolEndY, mPaint); //在10,60处开始绘制图片
        //draw content
        for (int index = 0; index < myTimes.size(); index++) {
            int x = Math.round(myTimes.get(index).getNowX()) - length;
            if (x + myTimes.get(index).getWidth() < 0)
                continue;
            if (x > screenWidthSize)
                break;
            int nextX = x + Math.round(myTimes.get(index).getWidth() / 2);

            canvas.drawLine(x, timeLongLineStartY, x, timeLongLineEndY, mLinePaint);
            canvas.drawLine(nextX, timeShortLineStartY, nextX, timeShortLineEndY, mLinePaint);
            if (index == myTimes.size() - 1)
                canvas.drawLine(x + Math.round(myTimes.get(index).getWidth()), timeLongLineStartY, Math.round(x + myTimes.get(index).getWidth()), timeLongLineEndY, mLinePaint);

            mTextPaint.setTextSize(timeTextSize);
            int textTimeX = x + Math.round(((myTimes.get(index).getWidth() - mTextPaint.measureText(myTimes.get(index).getTimeText())) / 2));
            mTextPaint.setColor(Color.parseColor("#333333"));
            canvas.drawText(myTimes.get(index).getTimeText(), textTimeX, timeTextStartY, mTextPaint);
            //绘制旗帜和编号
            drawFlag(canvas, myTimes.get(index));
        }

        //画红线
        canvas.drawBitmap(verticalLineBt, screenWidthSize / 2 - 1, bannerolStartY, mPaint);
        //drawBottomLine
        canvas.drawLine(0, bottomLineY, screenWidthSize, bottomLineY, mPaint);
    }

    private void drawFlag(Canvas canvas, MyTime myTime) {
        ArrayList<MyPoint> myPoints = myTime.getMyPoints();
        if (myPoints == null || myPoints.size() == 0)
            return;
        for (MyPoint point : myPoints) {
            int startX = Math.round(myTime.getNowX() - length + point.getStartX());
            canvas.drawBitmap(bitmap, startX, bannerolStartY, mPaint);
            mTextPaint.setTextSize(numberTextSize);
            int textNumberX = Math.round(startX + (bitMapWidth - mTextPaint.measureText(point.getNumber() + "")) / 2);
            mTextPaint.setColor(Color.parseColor("#fc5f3e"));
            canvas.drawText(point.getNumber() + "", textNumberX, textNumberStartY, mTextPaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        // 颜色滤镜
        if (mPaint.getColorFilter() != colorFilter) {
            mPaint.setColorFilter(colorFilter);
        }
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        int alpha = mPaint.getAlpha();
        if (alpha == 255) {
            return PixelFormat.OPAQUE;
        } else if (alpha == 0) {
            return PixelFormat.TRANSPARENT;
        } else {
            return PixelFormat.TRANSLUCENT;
        }
    }


    /**
     * 处理图片
     *
     * @param bm        所要转换的bitmap
     * @param newWidth  新的宽
     * @param newHeight 新的高
     * @return 指定宽高的bitmap
     */
    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }
}
