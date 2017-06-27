package cn.zp.zpexoplayer.view;

import cn.zp.zpexoplayer.application.MyApplication;
import cn.zp.zpexoplayer.exoplayer.IMediaPlayer;
import cn.zp.zpexoplayer.model.MyPoint;

/**
 * 标签编辑控制器
 * Created by zp on 2017/6/26.
 */

public class TagEditController implements TagEditBottomLinearLayout.ComponentListener {
    private TagEditBottomLinearLayout tagEditBottomLinearLayout;
    private IMediaPlayer mediaPlayer;
    private TagEditDynamicTimeLine tagEditDynamicTimeLine;
    private MyPoint currentPoint;
    private int NEXT = 1;//下一个
    private int LAST = 2;//上一个
    private int NOW = 3;//当前


    public TagEditController(IMediaPlayer mediaPlayer, TagEditBottomLinearLayout mTagEditBottomLinearLayout, TagEditDynamicTimeLine tagEditDynamicTimeLine) {
        this.mediaPlayer = mediaPlayer;
        this.tagEditDynamicTimeLine = tagEditDynamicTimeLine;
        this.tagEditBottomLinearLayout = mTagEditBottomLinearLayout;
        this.tagEditBottomLinearLayout.setComponentListener(this);
        currentPoint = MyApplication.getTagProject().getMyPointByNumber(1);
        touchOperation(1, NOW);
    }


    @Override
    public boolean onTouchLast(int currentTagNumber) {
        return touchOperation(currentTagNumber, LAST);
    }


    @Override
    public boolean onTouchNext(int currentTagNumber) {
        return touchOperation(currentTagNumber, NEXT);
    }

    /**
     * @param currentTagNumber 当前的选中的标签
     * @return true：操作成功，false:操作失败
     */
    private boolean touchOperation(int currentTagNumber, int start) {
        boolean touchState = false;
        if (mediaPlayer == null)
            return touchState;

        if (start == NEXT)
            currentTagNumber++;
        else if (start == LAST)
            currentTagNumber--;

        MyPoint point = MyApplication.getTagProject().getMyPointByNumber(currentTagNumber);

        if (point != null && tagEditDynamicTimeLine != null) {
            touchState = true;
            currentPoint = point;
            long time = point.getTrealTime();
            mediaPlayer.seekTo(time);
            tagEditDynamicTimeLine.refreshLayout(currentTagNumber, MyApplication.getTagProject().timeToLenght(time));
        }

        return touchState;
    }


    @Override
    public void onTouchAddLabel(int currentTagNumber) {

    }

    /**
     * 删除当前标签
     */
    public void deleteTag() {
        int curentPointNumber = currentPoint.getNumber();
        if (MyApplication.getTagProject().getTagCount() > 1) {
            MyApplication.getTagProject().deleteTag(currentPoint);
            if (MyApplication.getTagProject().getTagCount() == 1)
                curentPointNumber = 1;
            if (touchOperation(curentPointNumber, NOW))
                tagEditBottomLinearLayout.setCurrentTagNumber(curentPointNumber);
        } else {
            //touchOperation(curentPointNumber, true);
        }
    }

    public MyPoint getCurrentPoint() {
        return currentPoint;
    }

    public void setCurrentPoint(MyPoint currentPoint) {
        this.currentPoint = currentPoint;
    }
}
