package cn.zp.zpexoplayer.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 2017/6/22.
 */

public class MyPoint implements Serializable {
    static final long serialVersionUID = 1L;
    private long trealTime;//实际时间，在整个视频中的时间（单位毫秒）
    private long relativeTime;//在一秒内的时间(单位毫秒)
    private long playStartTime;//播放开始时间，前七妙
    private long playEndTime;//播放结束时间，后三秒
    private float startX;//绘制的时候开始的位置
    private int number;//多少号tag
    private boolean isDelete = false;//false不删除可用，true删除不可用
    private ArrayList<String> myTags;


    public MyPoint(long trealTime, long relativeTime, float startX, long playStartTime, long pleyEndTime) {
        this.trealTime = trealTime;
        this.relativeTime = relativeTime;
        this.startX = startX;
        this.playStartTime = playStartTime;
        this.playEndTime = pleyEndTime;
    }

    public long getRelativeTime() {
        return relativeTime;
    }

    public void setRelativeTime(long relativeTime) {
        this.relativeTime = relativeTime;
    }

    public long getPlayStartTime() {
        return playStartTime;
    }

    public void setPlayStartTime(long playStartTime) {
        this.playStartTime = playStartTime;
    }

    public long getPlayEndTime() {
        return playEndTime;
    }

    public void setPlayEndTime(long playEndTime) {
        this.playEndTime = playEndTime;
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

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public ArrayList<String> getMyTags() {
        return myTags;
    }

    public void setMyTags(ArrayList<String> myTags) {
        if (this.myTags == null)
            this.myTags = new ArrayList<>();
        this.myTags.addAll(myTags);
    }
}
