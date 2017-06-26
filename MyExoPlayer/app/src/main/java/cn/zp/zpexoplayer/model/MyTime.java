package cn.zp.zpexoplayer.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by admin on 2017/6/22.
 */

public class MyTime implements Serializable {
    static final long serialVersionUID = -1485301123574023028L;
    private ArrayList<MyPoint> myPoints;
    private float time;
    private String timeText;
    private float x;//在屏幕中x位置
    private float nowX;//现在在屏幕中的位置
    private float width;//时间表的宽度

    public MyTime(ArrayList<MyPoint> myPoints, long time, String timeText, float width, float x) {
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

    public void setMyPoint(MyPoint myPoint) {
        if (myPoint == null)
            return;
        if (myPoints == null)
            myPoints = new ArrayList<>();
        myPoints.add(myPoint);
    }

    /**
     * 对已有的tag进行排序编号
     *
     * @param existTagCount
     */
    public void sortAndSetNumber(int existTagCount) {
        if (myPoints == null)
            return;
        TagPointComparator tagPointComparator = new TagPointComparator();
        //排序
        Collections.sort(myPoints, tagPointComparator);
        //编号
        for (int i = 0; i < myPoints.size(); i++)
            myPoints.get(i).setNumber(existTagCount + i + 1);
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
