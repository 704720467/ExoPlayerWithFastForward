package cn.zp.zpexoplayer.model;

/**
 * Created by admin on 2017/6/22.
 */

public class MyPoint {
    private long trealTime;//实际时间，在整个视频中的时间（单位毫秒）
    private long relativeTime;//在一秒内的时间(单位毫秒)
    private float startX;//绘制的时候开始的位置
    private int number;//多少号tag


    public MyPoint(long trealTime, long relativeTime, float startX) {
        this.trealTime = trealTime;
        this.relativeTime = relativeTime;
        this.startX = startX;
    }

    public long getRelativeTime() {
        return relativeTime;
    }

    public void setRelativeTime(long relativeTime) {
        this.relativeTime = relativeTime;
    }

    public long getTrealTime() {
        return trealTime;
    }

    public void setTrealTime(long trealTime) {
        this.trealTime = trealTime;
    }

    public float getStartX() {
        return startX;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
