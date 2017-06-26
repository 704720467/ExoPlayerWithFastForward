package cn.zp.zpexoplayer.view;

import java.util.ArrayList;

import cn.zp.zpexoplayer.exoplayer.IMediaPlayer;
import cn.zp.zpexoplayer.model.MyPoint;
import cn.zp.zpexoplayer.model.MyTime;

/**
 * 标签编辑控制器
 * Created by zp on 2017/6/26.
 */

public class TagEditController implements TagEditBottomLinearLayout.ComponentListener {
    private ArrayList<MyTime> myTimes = new ArrayList<>();
    private TagEditBottomLinearLayout tagEditBottomLinearLayout;
    private IMediaPlayer mediaPlayer;
    private int currentLabeNumber = 1;
    private int tagCount;

    public TagEditController(IMediaPlayer mediaPlayer, TagEditBottomLinearLayout mTagEditBottomLinearLayout, ArrayList<MyTime> myTimes, int tagCount) {
        this.mediaPlayer = mediaPlayer;
        this.tagEditBottomLinearLayout = mTagEditBottomLinearLayout;
        this.tagCount = tagCount;
        if (this.tagEditBottomLinearLayout != null)
            this.tagEditBottomLinearLayout.setTagCount(tagCount);
        this.myTimes = myTimes;
    }

    /**
     * 设置时间列表
     *
     * @param myTimes
     */
    public void setMyTimes(ArrayList<MyTime> myTimes) {
        this.myTimes = myTimes;
    }

    @Override
    public void onTouchLast() {
        if (mediaPlayer == null)
            return;


    }

    @Override
    public void onTouchNext() {
        if (mediaPlayer == null)
            return;
        for (int i = currentLabeNumber; i < myTimes.size() + 1; i++) {
            ArrayList<MyPoint> myPints = myTimes.get(i).getMyPoints();
            if (myPints != null && myPints.size() > 0) {
                currentLabeNumber++;
                mediaPlayer.seekTo(myPints.get(0).getTrealTime());
                break;
            }
        }
    }

    @Override
    public void onTouchAddLabel() {

    }
}
