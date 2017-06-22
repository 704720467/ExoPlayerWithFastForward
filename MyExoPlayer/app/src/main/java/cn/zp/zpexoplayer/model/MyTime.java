package cn.zp.zpexoplayer.model;


import java.util.ArrayList;

/**
 * Created by admin on 2017/6/22.
 */

public class MyTime {
    private ArrayList<MyPoint> myPoints;
    private long time;
    private String timeText;
    private int x;//在屏幕中x位置
    private int nowX;//现在在屏幕中的位置
    private int width;//时间表的宽度

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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTimeText() {
        return timeText;
    }

    public void setTimeText(String timeText) {
        this.timeText = timeText;
    }

    public int getNowX() {
        return nowX;
    }

    public void setNowX(int nowX) {
        this.nowX = nowX;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}
