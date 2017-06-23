package cn.zp.zpexoplayer.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

import cn.zp.zpexoplayer.model.MyTime;
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
    private float bitMapWidth;
    private int screenWidthSize;
    private int margenSize = 20;
    private int timeLineHeight = 40;//时间长线长度
    private int timeSortLineHeight = 10;//时间短线长度
    private int bannerolHeight = 120;


    public MyDrawable(int screenWidthSize, Bitmap bitmap) {
        this.screenWidthSize = screenWidthSize;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#F4F5F9"));
        mPaint.setAntiAlias(true);// 抗锯齿
        mPaint.setDither(true);  // 防止抖动
        mPaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setAntiAlias(true);// 抗锯齿
        mTextPaint.setDither(true);  // 防止抖动
        mTextPaint.setStyle(Paint.Style.FILL);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.parseColor("#ebebeb"));
        mLinePaint.setAntiAlias(true);// 抗锯齿
        mLinePaint.setDither(true);  // 防止抖动
        mLinePaint.setStyle(Paint.Style.FILL);


        this.bitmap = zoomImg(bitmap, 36, 120);
        bitMapWidth = this.bitmap.getWidth();
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
    private float length;

    public void refreshView(float dou) {
        long newTime = System.currentTimeMillis();
        length = length + dou;
        //        length = dou;
        Log.e("", "要刷新的长度为length=" + length + ";步长=" + dou + ";耗时：" + (newTime - oldTime));
        oldTime = newTime;
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
        canvas.drawLine(0, 0, screenWidthSize, 0, mPaint);

        //画底部
        canvas.drawRect(0, margenSize + timeLineHeight, screenWidthSize, margenSize + timeLineHeight + bannerolHeight, mPaint); //在10,60处开始绘制图片

        //draw content
        for (int index = 0; index < myTimes.size(); index++) {
            float x = myTimes.get(index).getNowX() - length;
            if (x + myTimes.get(index).getWidth() < 0)
                continue;
            if (x > screenWidthSize)
                break;
            float nextX = x + myTimes.get(index).getWidth() / 2;
            float textTimeX = x + (myTimes.get(index).getWidth() - mPaint.measureText(myTimes.get(index).getTimeText())) / 2;
            float textNumberX = nextX + (bitMapWidth - mPaint.measureText(index + "")) / 2;

            canvas.drawLine(x, margenSize, x, margenSize + timeLineHeight, mLinePaint);
            canvas.drawLine(nextX, margenSize + timeLineHeight - timeSortLineHeight, nextX, margenSize + timeLineHeight, mLinePaint);
            if (index == myTimes.size() - 1)
                canvas.drawLine(x + myTimes.get(index).getWidth(), margenSize, x + myTimes.get(index).getWidth(), margenSize + timeLineHeight, mLinePaint);

            canvas.drawBitmap(bitmap, nextX, margenSize + timeLineHeight, mPaint);

            mTextPaint.setTextSize(20);
            mTextPaint.setColor(Color.parseColor("#333333"));
            canvas.drawText(myTimes.get(index).getTimeText(), textTimeX, margenSize + timeLineHeight - timeSortLineHeight, mTextPaint);
            mTextPaint.setTextSize(18);
            mTextPaint.setColor(Color.parseColor("#fc5f3e"));
            canvas.drawText(index + "", textNumberX, 60 + 50, mTextPaint);
        }

        //drawBottomLine
        canvas.drawLine(0, 200, screenWidthSize, 200, mPaint);
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
