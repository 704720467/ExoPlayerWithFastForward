package cn.zp.zpexoplayer.model;

/**
 * Created by admin on 2017/6/22.
 */

public class MyPoint {
    private int trealTime;//实际时间，在整个视频中的时间
    private int relativeTime;//在一秒内的时间


    public int getRelativeTime() {
        return relativeTime;
    }

    public void setRelativeTime(int relativeTime) {
        this.relativeTime = relativeTime;
    }

    public int getTrealTime() {
        return trealTime;
    }

    public void setTrealTime(int trealTime) {
        this.trealTime = trealTime;
    }
}
