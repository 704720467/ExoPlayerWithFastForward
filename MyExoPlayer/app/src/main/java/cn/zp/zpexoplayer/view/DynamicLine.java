package cn.zp.zpexoplayer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.zp.zpexoplayer.R;
import cn.zp.zpexoplayer.model.MyTime;

public class DynamicLine extends View {


    private final static String X_KEY = "Xpos";
    private final static String Y_KEY = "Ypos";
    private Bitmap bitmap;
    private float bitMapWidth;
    private List<Map<String, Integer>> mListPoint = new ArrayList<Map<String, Integer>>();

    private ArrayList<MyTime> myTimes = new ArrayList<>();

    Paint mPaint = new Paint();

    public DynamicLine(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        bitMapWidth = bitmap.getWidth();
    }

    public DynamicLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        bitMapWidth = bitmap.getWidth();
    }

    public DynamicLine(Context context) {
        super(context);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        bitMapWidth = bitmap.getWidth();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        //        mPaint.setColor(Color.parseColor("#ebebeb"));
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);

        for (int index = 0; index < myTimes.size(); index++) {
            float x = myTimes.get(index).getNowX();
            if (x + myTimes.get(index).getWidth() < 0)
                continue;
            if (x > getWidth())
                break;
            float nextX = myTimes.get(index).getNowX() + myTimes.get(index).getWidth() / 2;
            float textTimeX = x + (myTimes.get(index).getWidth() - mPaint.measureText(myTimes.get(index).getTimeText())) / 2;
            float textNumberX = nextX + (bitMapWidth - mPaint.measureText(index + "")) / 2;

            canvas.drawLine(x, 0, x, 60, mPaint);
            canvas.drawLine(nextX, 50, nextX, 60, mPaint);
            if (index == myTimes.size() - 1)
                canvas.drawLine(x + myTimes.get(index).getWidth(), 0, x + myTimes.get(index).getWidth(), 60, mPaint);
            canvas.drawBitmap(bitmap, nextX, 60, mPaint); //在10,60处开始绘制图片
            mPaint.setTextSize(30);
            canvas.drawText(myTimes.get(index).getTimeText(), textTimeX, 60 - 20, mPaint);
            canvas.drawText(index + "", textNumberX, 60 + 50, mPaint);
        }
    }

    /**
     * @param curX which x position you want to draw.
     * @param curY which y position you want to draw.
     */
    public void setLinePoint(int curX, int curY) {
        Map<String, Integer> temp = new HashMap<String, Integer>();
        temp.put(X_KEY, curX);
        temp.put(Y_KEY, curY);
        mListPoint.add(temp);
        invalidate();
    }

    public void setMyTimes(ArrayList<MyTime> myTimes) {
        this.myTimes = myTimes;
        invalidate();
    }

    public void refreshView(float dou) {
        for (MyTime myTime : myTimes) {
            myTime.setNowX(myTime.getNowX() - dou);
        }
    }
}