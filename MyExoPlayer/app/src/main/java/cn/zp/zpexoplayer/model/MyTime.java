package cn.zp.zpexoplayer.model;


import java.util.ArrayList;

/**
 * Created by admin on 2017/6/22.
 */

public class MyTime {
    private ArrayList<MyPoint> myPoints;
    private float time;
    private String timeText;
    private float x;//在屏幕中x位置
    private float nowX;//现在在屏幕中的位置
    private float width;//时间表的宽度

    public MyTime(ArrayList<MyPoint> myPoints, long time, String timeText, int width, int x) {
        this.myPoints = myPoints;
        this.time = time;
        this.timeText = timeText;
        this.width = width;
        this.x = this.nowX = x;
    }

    public ArrayList<MyPoint> getMyPoints() {
        return myPoints;
    }

    public void setMyPoints(ArrayList<MyPoint> myPoints) {
        this.myPoints = myPoints;
    }

    public float getNowX() {
        return nowX;
    }

    public void setNowX(float nowX) {
        this.nowX = nowX;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public String getTimeText() {
        return timeText;
    }

    public void setTimeText(String timeText) {
        this.timeText = timeText;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }
}
