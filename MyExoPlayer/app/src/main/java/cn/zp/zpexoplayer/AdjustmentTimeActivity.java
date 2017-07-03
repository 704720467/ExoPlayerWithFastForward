package cn.zp.zpexoplayer;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.zp.zpexoplayer.util.DeviceUtil;
import cn.zp.zpexoplayer.util.videoUitl.ExtractFrameWorkThread;
import cn.zp.zpexoplayer.util.videoUitl.ExtractVideoInfoUtil;
import cn.zp.zpexoplayer.util.videoUitl.PictureUtils;
import cn.zp.zpexoplayer.util.videoUitl.VideoEditInfo;

public class AdjustmentTimeActivity extends FillScreenBaseActivity {
    private String TAG = "AdjustmentTimeActivity";
    private final String path = Environment.getExternalStorageDirectory() + "/reee/6.mp4";
    private final String OutPutFileDirPath = Environment.getExternalStorageDirectory() + "/reee/ExtractPic";

    private RelativeLayout contentLeftLayour;// 左布局
    private LinearLayout contentLayour;// 内容布局
    private RelativeLayout contentRightLayour;// 右布局
    private RelativeLayout availableLayout;// 中间可用部分布局
    private ImageView imageHend;
    private ImageView imageLast;
    private ImageView currentImageBar;// 当前是哪个把手
    private int currentSeekBar = -1;// 0:左边的bar1：右边的bar 2：中间的用区域
    private float downXLeft = 0;// 左边按下的位置
    private float downXRight = 0;// 左边按下的位置
    private float downXMiddle = 0;// 左边按下的位置
    private int mActivePointerId = -1;
    private int currentLength = 0;// 遮挡的中宽度（包含了两个移动的bar）
    private float startTime;// 左边遮挡层的宽度
    private float endTime;// 右边遮挡层的宽度
    private float videoDuration = 0;// 视频的总时长
    private int currentLayoutWidth = 0;// 所有图片的宽度，也就是内容布局的宽度
    private int imageWidth = 0;//图片的宽度
    private int imageCount = 10;
    private ImageView imgCover;

    private List<VideoEditInfo> lists = new ArrayList<>();
    private ExtractVideoInfoUtil mExtractVideoInfoUtil;
    private ExtractFrameWorkThread mExtractFrameWorkThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjustment_time);
        initView();
        initData();
        onClickExtractOne();
        onClickExtractMul();
        addChildLayoutForLayout();
    }


    private void initView() {
        imgCover = (ImageView) findViewById(R.id.img_cover);
        contentLeftLayour = (RelativeLayout) findViewById(R.id.content_left_tailor);
        contentLeftLayour.setOnClickListener(null);
        contentLayour = (LinearLayout) findViewById(R.id.content_tailor);
        contentRightLayour = (RelativeLayout) findViewById(R.id.content_right_tailor);
        contentRightLayour.setOnClickListener(null);
        availableLayout = (RelativeLayout) findViewById(R.id.content_available_tailor);
        availableLayout.setOnTouchListener(onTouchListenerForChildWidthChange);
        imageHend = (ImageView) findViewById(R.id.image_hend);
        imageHend.setOnTouchListener(onTouchListenerForChildWidthChange);
        imageLast = (ImageView) findViewById(R.id.image_last);
        imageLast.setOnTouchListener(onTouchListenerForChildWidthChange);

    }


    private void initData() {
        if (!new File(path).exists()) {
            Toast.makeText(this, "视频文件不存在", Toast.LENGTH_SHORT).show();
            finish();
        }
        mExtractVideoInfoUtil = new ExtractVideoInfoUtil(path);
        currentLayoutWidth = DeviceUtil.getScreenWidthSize(this) - DeviceUtil.dp2px(this, 20);
        imageWidth = currentLayoutWidth / imageCount;
        videoDuration = Long.valueOf(mExtractVideoInfoUtil.getVideoLength());
        startTime = 0;
        endTime = Long.valueOf(mExtractVideoInfoUtil.getVideoLength());
    }

    public void onClickExtractOne() {
        String path = mExtractVideoInfoUtil.extractFrames(OutPutFileDirPath, (long) startTime);
        Glide.with(this).load("file://" + path).into(imgCover);
    }

    public void onClickExtractMul() {
        int extractW = DeviceUtil.dp2px(this, 300);
        int extractH = DeviceUtil.dp2px(this, 300);
        mExtractFrameWorkThread = new ExtractFrameWorkThread(extractW, extractH, mUIHandler, path, OutPutFileDirPath, (long) startTime, (long) endTime, imageCount);
        mExtractFrameWorkThread.start();
    }

    /**
     * 为滑动View（准备添加图片）添加子布局
     */
    public void addChildLayoutForLayout() {
        contentLayour.removeAllViews();
        // 动态的设置宽度
        for (int i = 0; i < lists.size(); i++) {
            ImageView image = new ImageView(this);
            Glide.with(this).load("file://" + lists.get(i).path).override(300, 300).into(image);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);// 多余的部分自动裁剪
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(imageWidth, LinearLayout.LayoutParams.MATCH_PARENT);
            image.setLayoutParams(lp);
            contentLayour.addView(image);
        }

        int leftWidth = (int) (((float) currentLayoutWidth * startTime) / videoDuration);
        leftWidth = leftWidth == 0 ? 1 : leftWidth;
        Log.w(TAG, "leftWidth=" + leftWidth);
        int rightWidth = (int) (((videoDuration - endTime) / videoDuration) * currentLayoutWidth);
        if (endTime > videoDuration) {
            rightWidth = 0;
        }
        rightWidth = rightWidth == 0 ? 1 : rightWidth;
        contentLeftLayour.getLayoutParams().width = leftWidth;
        contentLeftLayour.setLayoutParams(contentLeftLayour.getLayoutParams());

        Log.w(TAG, "rightWidth=" + rightWidth);
        contentRightLayour.getLayoutParams().width = rightWidth;
        contentRightLayour.setLayoutParams(contentRightLayour.getLayoutParams());

        // 改变imagebar的大小,适配小米手机
        imageHend.setTranslationX(leftWidth);
        imageLast.setTranslationX(-rightWidth);
    }


    View.OnTouchListener onTouchListenerForChildWidthChange = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            // 获得当前点击的控件
            if (v.getId() == R.id.image_hend) {
                currentSeekBar = 0;
            } else if (v.getId() == R.id.image_last) {
                currentSeekBar = 1;
            } else if (v.getId() == R.id.content_available_tailor) {
                currentSeekBar = 2;
            }
            resetCanMoveViewStata(false);

            // 触摸事件返回值
            boolean back = false;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mActivePointerId = event.getPointerId(0);
                    int downX = (int) event.getX();
                    switch (currentSeekBar) {
                        case 0:
                            downXLeft = downX;
                            break;
                        case 1:
                            downXRight = downX;
                            break;
                        case 2:
                            // 只有第一指位置起作用
                            downXMiddle = (int) event.getX(mActivePointerId);
                            break;
                    }
                    back = true;
                    break;
                case MotionEvent.ACTION_MOVE:
                    int moveX = (int) event.getX();
                    switch (currentSeekBar) {
                        case 0:
                        case 1:
                            updateLeftLayoutOrRightLayoutView(moveX);
                            break;
                        case 2:
                            moveAvailableLayout((int) event.getX(mActivePointerId));
                            break;
                        default:
                            return false;
                    }
                    back = true;
                    break;
                case MotionEvent.ACTION_UP:
                    currentSeekBar = -1;
                    Log.i(TAG, "editResizeChunk:srcBegin=" + startTime + ";srcEnd=" + endTime);
                    resetCanMoveViewStata(true);
                    break;
                default:
                    break;
            }
            return back;
        }
    };

    /**
     * 修改移动控件的状态,canMove：true:都可以点击，false：仅有一部分控件可以点击
     */
    private void resetCanMoveViewStata(boolean canMove) {
        imageHend.setEnabled(canMove);
        imageLast.setEnabled(canMove);
        availableLayout.setEnabled(canMove);
        if (!canMove) {
            switch (currentSeekBar) {
                case 0:
                case 1:
                    imageHend.setEnabled(true);
                    imageLast.setEnabled(true);
                    break;
                case 2:
                    availableLayout.setEnabled(true);
                    break;
            }
        }
    }

    /**
     * 移动中间可用的位置
     */
    private synchronized void moveAvailableLayout(float endX) {
        int moveX = (int) (endX - downXMiddle);
        // 移动的时候，左、右边的遮挡宽度不能小于0
        if ((contentLeftLayour.getWidth() + moveX >= 0.1) && (contentRightLayour.getWidth() - moveX >= 0.1)) {
            int contentLeftLayourWidth = contentLeftLayour.getWidth();
            int contentRightLayourWidth = contentRightLayour.getWidth();
            contentLeftLayour.getLayoutParams().width = contentLeftLayourWidth + moveX;
            contentLeftLayour.setLayoutParams(contentLeftLayour.getLayoutParams());
            contentRightLayour.getLayoutParams().width = contentRightLayourWidth - moveX;
            contentRightLayour.setLayoutParams(contentRightLayour.getLayoutParams());
            imageHend.setTranslationX(contentLeftLayour.getLayoutParams().width);
            imageLast.setTranslationX(-contentRightLayour.getLayoutParams().width);
            // 播放器显示对应的图片
            moveShowVideoImage();
        } else {
            Log.i(TAG, "中间可用部分移动超越底线");
        }
        downXMiddle = endX;
    }


    /**
     * 更新试图
     * <p>
     * 移动后的两指之间的距离
     */
    private void updateLeftLayoutOrRightLayoutView(float endX) {
        View v;
        int addWidht = 0;// 要增加或减少的宽度
        // 获取相应的遮挡层，和变化的宽度
        if (currentSeekBar == 0) {
            v = contentLeftLayour;
            currentImageBar = imageHend;
            addWidht = (int) (endX - downXLeft);
        } else {
            v = contentRightLayour;
            currentImageBar = imageLast;
            addWidht = (int) (downXRight - endX);
        }
        // 获取当前遮挡板的总宽度
        currentLength = contentLeftLayour.getWidth() + contentRightLayour.getWidth();
        int minWidth = (int) (((float) currentLayoutWidth / videoDuration) * 2);// 最小宽度不能小于两秒的宽度
        // 变化后最小宽度不能小于bar的宽度，最大宽度不能超过所有视频图片布局的总宽度
        if ((v.getWidth() + addWidht > 0) && (minWidth <= (currentLayoutWidth - (currentLength + addWidht))) && ((currentLength + addWidht) <= currentLayoutWidth)) {
            // v.getLayoutParams().width = v.getWidth() + addWidht;
            // v.setLayoutParams(v.getLayoutParams());
            // 改变imagebar的大小,适配小米手机
            float tt = (currentSeekBar == 1) ? (currentImageBar.getTranslationX() - addWidht) : (currentImageBar.getTranslationX() + addWidht);
            currentImageBar.setTranslationX(tt);
            if (currentSeekBar == 0) {
                v.getLayoutParams().width = (int) currentImageBar.getTranslationX();
            } else {
                v.getLayoutParams().width = (int) (currentLayoutWidth - currentImageBar.getX());
            }
            v.setLayoutParams(v.getLayoutParams());
            // 播放器显示对应的图片
            moveShowVideoImage();
        } else {
            Log.i(TAG, "不能移动contentLeftLayour.width=" + contentLeftLayour.getWidth() + ";contentRightLayour.width=" + contentRightLayour.getWidth() + ";addWidht=" + addWidht + ";总图的宽度=" + currentLayoutWidth);
        }
    }


    /**
     * 移动时显示的图片
     */
    private void moveShowVideoImage() {
        int startX = 0;
        float seekPostion = 0;
        switch (currentSeekBar) {
            case 0:
                // 根据时间与宽度计算出开始时间
                startTime = (videoDuration / (float) currentLayoutWidth) * ((float) contentLeftLayour.getWidth());
                seekPostion = startTime;
                //                tvStartTime.setText(TimeUtil.millisToString(startTime * 1000));
                break;
            case 1:
                // 根据时间与宽度计算出开始和结束时间
                endTime = (videoDuration / (float) currentLayoutWidth) * ((float) currentLayoutWidth - (float) contentRightLayour.getWidth());
                seekPostion = endTime;
                //                tvEndTime.setText(TimeUtil.millisToString(endTime * 1000));
                break;
            case 2:
                // 根据时间与宽度计算出结束时间
                startTime = (videoDuration / (float) currentLayoutWidth) * ((float) contentLeftLayour.getWidth());
                endTime = (videoDuration / (float) currentLayoutWidth) * ((float) currentLayoutWidth - (float) contentRightLayour.getWidth());
                seekPostion = startTime;
                //                tvStartTime.setText(TimeUtil.millisToString(startTime * 1000));
                //                tvEndTime.setText(TimeUtil.millisToString(endTime * 1000));
                break;
        }
        for (VideoEditInfo list : lists) {
            if (list.time >= seekPostion) {
                Glide.with(this).load("file://" + list.path).into(imgCover);
                break;
            }
        }
    }

    private final MainHandler mUIHandler = new MainHandler(this);

    private static class MainHandler extends Handler {
        private final WeakReference<AdjustmentTimeActivity> mActivity;

        MainHandler(AdjustmentTimeActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            AdjustmentTimeActivity activity = mActivity.get();
            if (activity != null) {
                if (msg.what == ExtractFrameWorkThread.MSG_SAVE_SUCCESS) {
                    VideoEditInfo info = (VideoEditInfo) msg.obj;
                    if (info != null)
                        activity.lists.add(info);
                    activity.addChildLayoutForLayout();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mExtractVideoInfoUtil.release();
        if (mExtractFrameWorkThread != null) {
            mExtractFrameWorkThread.stopExtract();
        }
        mUIHandler.removeCallbacksAndMessages(null);
        if (!TextUtils.isEmpty(OutPutFileDirPath)) {
            PictureUtils.deleteFile(new File(OutPutFileDirPath));
        }
    }


}
