package cn.zp.zpexoplayer.view;

import com.google.android.exoplayer2.ui.PlaybackControlView;

import cn.zp.zpexoplayer.application.MyApplication;
import cn.zp.zpexoplayer.exoplayer.IMediaPlayer;
import cn.zp.zpexoplayer.exoplayer.KExoMediaPlayer;
import cn.zp.zpexoplayer.model.MyPoint;

/**
 * 标签编辑控制器
 * Created by zp on 2017/6/26.
 */

public class TagEditController implements TagEditBottomLinearLayout.ComponentListener, PlaybackControlView.VideoControlLinstion {
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
        ((KExoMediaPlayer) this.mediaPlayer).setVideoControlLinstion(this);
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
            seekAndPlay();
            tagEditDynamicTimeLine.refreshLayout(currentTagNumber, MyApplication.getTagProject().timeToLenght(time));
        }

        return touchState;
    }

    /**
     * 跳转到指定的位置并且播放，无限循环
     *
     * @return
     */
    private void seekAndPlay() {
        if (currentPoint == null)
            return;
        mediaPlayer.seekTo(currentPoint.getPlayStartTime());
        if (!mediaPlayer.isPlaying())
            mediaPlayer.start();
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

            if (curentPointNumber > MyApplication.getTagProject().getTagCount())
                curentPointNumber = MyApplication.getTagProject().getTagCount();

            if (touchOperation(curentPointNumber, NOW))
                tagEditBottomLinearLayout.setCurrentTagNumber(curentPointNumber);

        } else {
            //touchOperation(curentPointNumber, true);
        }
    }

    /**
     * 找回删除的tag
     */
    public void reBackdeleteTag() {
        MyApplication.getTagProject().reBackdeleteTag();
    }

    public MyPoint getCurrentPoint() {
        return currentPoint;
    }

    public void setCurrentPoint(MyPoint currentPoint) {
        this.currentPoint = currentPoint;
    }


    @Override
    public boolean updateProgressBack(long duration, long currentPosition) {
        if (currentPoint != null && currentPosition >= currentPoint.getPleyEndTime())
            seekAndPlay();
        return false;
    }

    @Override
    public void fullScreen() {
        //        setRequestedOrientation((getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) ? //
        //                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ://
        //                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
    }

    @Override
    public void onProgressChanged(long currtentProgress) {
    }
}
