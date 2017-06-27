package cn.zp.zpexoplayer.model;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

import cn.zp.zpexoplayer.application.MyApplication;

/**
 * 每个项目的对应一个Tag项目
 * <p>
 * Created by admin on 2017/6/27.
 */

public class TagProject implements Serializable {
    static final long serialVersionUID = 1L;
    private ArrayList<MyTime> myTimes = new ArrayList<>();
    private int tagCount;
    private int timeWidth;

    public ArrayList<MyTime> getMyTimes() {
        return myTimes;
    }

    public void setMyTimes(ArrayList<MyTime> myTimes) {
        this.myTimes = myTimes;
    }


    public int getTimeWidth() {
        return timeWidth;
    }

    /**
     * 设置单位时间1秒的长度
     *
     * @param timeWidth
     */
    public void setTimeWidth(int timeWidth) {
        this.timeWidth = timeWidth;
    }

    public int getTagCount() {
        return tagCount;
    }

    /**
     * 获取有打了多少个Tag
     *
     * @param tagCount
     */
    public void setTagCount(int tagCount) {
        this.tagCount = tagCount;
    }

    /**
     * 清空当前的时间集合
     */
    public void clearTimes() {
        if (myTimes != null)
            myTimes.clear();
    }

    /**
     * 添加时间段Time
     *
     * @param m 要添加的时间段
     */
    public void addTime(MyTime m) {
        if (myTimes != null)
            myTimes.add(m);
    }

    /**
     * 获取指定的位置的时间段
     *
     * @param position 指定的位置从0开始
     * @return
     */
    public MyTime getMyTimeByPosition(int position) {
        if (myTimes == null || myTimes.size() <= position)
            return null;
        return myTimes.get(position);
    }

    /**
     * 获取有多少个时间段
     *
     * @return
     */
    public int getMyTimesCount() {
        if (myTimes == null)
            return 0;
        return myTimes.size();
    }

    /**
     * 获取最后一个时间段
     *
     * @return
     */
    public MyTime getLastMyTime() {
        if (myTimes == null || myTimes.size() == 0)
            return null;
        return myTimes.get(myTimes.size() - 1);
    }

    /**
     * 根据编号获取相应的tagPoint
     *
     * @param number tag 编号
     * @return 对应的TagPoint
     */
    public MyPoint getMyPointByNumber(int number) {
        MyPoint myPoint = null;
        for (int i = 0; i < MyApplication.getTagProject().getMyTimesCount(); i++) {
            //判断是否存在tag
            ArrayList<MyPoint> myPints = getMyTimeByPosition(i).getMyPoints();
            if (myPints != null && myPints.size() > 0)
                if (myPints.get(0).getNumber() == number) {
                    myPoint = myPints.get(0);
                    break;
                }
        }
        return myPoint;
    }

    /**
     * 添加tag
     *
     * @param tagTime       tag对应的真实时间
     * @param aSecondLength 每一秒对应的单位长度
     * @return
     */
    public boolean addTag(long tagTime, int aSecondLength) {
        boolean addScuress = false;
        boolean canAdd = true;

        int existTagCount = 0;//已经存在多少个tag了
        for (int i = 0; i < myTimes.size(); i++) {
            MyTime myTime = myTimes.get(i);
            if (!addScuress && myTime.getTime() >= tagTime) {
                if (!checkCanAddTag(i, tagTime))
                    break;
                long relativeTime = tagTime % 1000;
                float startX = aSecondLength * relativeTime / 1000f;
                MyPoint myPoint = new MyPoint(tagTime, relativeTime, startX);
                myTime.setMyPoint(myPoint);
                addScuress = true;
            }

            if (myTime.getMyPoints() == null)
                continue;
            //对已有的tag进行编号排序
            myTime.sortAndSetNumber(existTagCount);
            existTagCount += myTime.getMyPoints().size();
        }
        tagCount = existTagCount;
        return addScuress;
    }

    /**
     * 检测是否可以添加tag,距离前后tag的距离是1000毫秒的距离
     *
     * @param i       当前的位置
     * @param tagTime tag的时间
     */
    private boolean checkCanAddTag(int i, long tagTime) {
        boolean canAdd = true;
        MyTime lastMyTime = null;//上一个
        MyTime nextMyTime = null;//下一个
        MyTime nowMyTime = null;//当前的
        nowMyTime = myTimes.get(i);
        //如果当前的已经有了一个的话不能添加
        if (nowMyTime.getMyPoints() != null && nowMyTime.getMyPoints().size() > 0)
            return false;
        //一个还没有的情况
        if (i == 0) {
            nextMyTime = myTimes.get(i + 1);
        } else if (i == myTimes.size() - 1) {
            lastMyTime = myTimes.get(i - 1);
        } else {
            nextMyTime = myTimes.get(i + 1);
            lastMyTime = myTimes.get(i - 1);
        }

        //true离前面的太近
        boolean nearLast = (lastMyTime != null && lastMyTime.getMyPoints() != null && lastMyTime.getMyPoints().size() > 0 &&//
                ((tagTime - lastMyTime.getMyPoints().get(0).getTrealTime()) < 1000));
        //true离后面的太近
        boolean nearNext = (nextMyTime != null && nextMyTime.getMyPoints() != null && nextMyTime.getMyPoints().size() > 0 &&//
                ((nextMyTime.getMyPoints().get(0).getTrealTime() - tagTime) < 1000));

        if (nearLast || nearNext) {
            canAdd = false;
            Log.i("TagProject", "里的近正好nearLast=" + nearLast + ";nearNext=" + nearNext + ";tagTime=" + tagTime);
        } else {
            Log.i("TagProject", "里的不近正好nearLast=" + nearLast + ";nearNext=" + nearNext + ";tagTime=" + tagTime);
        }
        return canAdd;
    }

    /**
     * 删除Tag
     *
     * @return
     */
    public boolean deleteTag(MyPoint deleteMyPoint) {
        boolean deleteState = false;
        int nowTagPointCount = 0;
        try {
            for (int i = 0; i < myTimes.size(); i++) {
                MyTime myTime = getMyTimeByPosition(i);
                ArrayList<MyPoint> myPints = myTime.getMyPoints();
                if (myPints != null && myPints.size() > 0) {
                    if (myPints.get(0).getNumber() == deleteMyPoint.getNumber()) {
                        myPints.clear();
                        tagCount--;
                        deleteState = true;
                    } else {
                        nowTagPointCount++;
                        myPints.get(0).setNumber(nowTagPointCount);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("TagProject", "删除Tag报错：" + e.getMessage());
        }
        return deleteState;
    }

    public long timeToLenght(long time) {
        return Math.round(timeWidth * time / 1000f);
    }
}
