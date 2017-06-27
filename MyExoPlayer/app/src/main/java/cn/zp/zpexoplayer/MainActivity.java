package cn.zp.zpexoplayer;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.util.ArrayList;
import java.util.Random;

import cn.zp.zpexoplayer.application.MyApplication;
import cn.zp.zpexoplayer.exoplayer.IMediaPlayer;
import cn.zp.zpexoplayer.exoplayer.KExoMediaPlayer;
import cn.zp.zpexoplayer.util.DeviceUtil;
import cn.zp.zpexoplayer.view.DynamicLine2;
import cn.zp.zpexoplayer.view.SeekBarLinearLayout;
import cn.zp.zpexoplayer.view.SpeedLinearLayout;
import cn.zp.zpexoplayer.view.TopLinearLayout;

public class MainActivity extends AppCompatActivity implements SeekBarLinearLayout.MySeekBarListener, TopLinearLayout.TopLinearLayListener, SpeedLinearLayout.MyCircleLinearLayListener, PlaybackControlView.VideoControlLinstion, IMediaPlayer.OnPreparedListener, View.OnClickListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnCompletionListener {
    private int seekPoint = 0;
    private Settings mSettings;
    private int tagCount = 0;
    private TextView mTagCount;
    private LinearLayout mTagLay;
    private ImageView mImageViewPlayOrPause;

    private IMediaPlayer mediaPlayer;
    private SimpleExoPlayerView simpleExoPlayerView;
    private ArrayList<Integer> myRandom = new ArrayList<>();
    private SpeedLinearLayout speedLinearLayout;

    private final Runnable updateProgressAction = new Runnable() {
        @Override
        public void run() {
            seekVideo();
        }
    };
    PlaybackControlView controller;
    SeekBar seekBar;
    //进度回调
    private TopLinearLayout topLinearLayout;

    private static final int MSG_DATA_CHANGE = 0x11;
    private DynamicLine2 dynamicLine;
    private SeekBarLinearLayout mSeekBarLinearLayout;
    private float s;
    private long oldProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        switchMediaEngine(null);
        Intent intent = getIntent();
        String intentAction = intent.getAction();
        if (!TextUtils.isEmpty(intentAction)) {
            //if (intentAction.equals(Intent.ACTION_VIEW)) {
            goPlay(null);
            // }
        }
    }

    private void initData() {
        if (mSettings == null)
            mSettings = new Settings(this);
        speed = 1.0f;
    }

    private void initView() {
        topLinearLayout = (TopLinearLayout) findViewById(R.id.my_top_lay);
        topLinearLayout.setTopLinearLayListener(this);
        topLinearLayout.setmTitleText("标签标记");
        mImageViewPlayOrPause = (ImageView) findViewById(R.id.img_play_or_pause);
        mImageViewPlayOrPause.setOnClickListener(this);
        mTagCount = (TextView) findViewById(R.id.tv_tag_count);
        mTagLay = (LinearLayout) findViewById(R.id.tag_lay);
        mTagLay.setOnClickListener(this);
        mTagCount = (TextView) findViewById(R.id.tv_tag_count);

        speedLinearLayout = (SpeedLinearLayout) findViewById(R.id.speedLinearLayout);
        speedLinearLayout.setMyCircleLinearLayListener(this);

        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
        ViewGroup.LayoutParams lp = simpleExoPlayerView.getLayoutParams();
        lp.width = DeviceUtil.getScreenWidthSize(this);
        lp.height = DeviceUtil.getPlayerHeightSize(this);
        simpleExoPlayerView.setLayoutParams(lp);
        controller = simpleExoPlayerView.getController();
        seekBar = controller.getProgressBar();
        mSeekBarLinearLayout = (SeekBarLinearLayout) findViewById(R.id.my_seek_bar);
        mSeekBarLinearLayout.setMySeekBarListener(this);
        dynamicLine = (DynamicLine2) this.findViewById(R.id.DynamicLine);
        dynamicLine.setSeekBarLayout(mSeekBarLinearLayout);
        dynamicLine.setTagView(mTagCount);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (simpleExoPlayerView != null) {
            ViewGroup.LayoutParams lp = simpleExoPlayerView.getLayoutParams();
            lp.width = DeviceUtil.getScreenWidthSize(this);
            lp.height = DeviceUtil.getPlayerHeightSize(this);
            simpleExoPlayerView.setLayoutParams(lp);
        }
        if (mTagCount != null)
            mTagCount.setText(MyApplication.getTagProject().getTagCount() + "");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying())
            mediaPlayer.stop();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_play_or_pause:
                if ("pause".equals(v.getTag())) {
                    mediaPlayer.start();
                    updateFabState(true);
                } else if ("start".equals(v.getTag())) {
                    mediaPlayer.pause();
                    updateFabState(false);
                }
                break;
            case R.id.tag_lay:
                dynamicLine.addTag(mediaPlayer.getCurrentPosition());
                break;
        }
    }


    @Override
    public void onPrepared(IMediaPlayer mp) {
        mediaPlayer.setSonicSpeed(speed);
        //mp.start();
        updateFabStatePost(mp.isPlaying());
        dynamicLine.setData(mp.getDuration());
    }

    @Override
    public boolean onError(IMediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onCompletion(IMediaPlayer mp) {
        updateFabStatePost(false);
    }


    private float speed;

    @Override
    public void onNumChange(View view, float num) {
        mSettings.setSonicSpeed(speed = num);
        mediaPlayer.setSonicSpeed(num);
    }


    public void switchMediaEngine(View v) {
        if (mediaPlayer != null)
            mediaPlayer.stop();

        mediaPlayer = new KExoMediaPlayer(this, simpleExoPlayerView, this);

        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnCompletionListener(this);
        ((KExoMediaPlayer) mediaPlayer).setDynamicLine2(dynamicLine);

        updateFabState(false);
    }


    public void goPlay(String path) {
        try {
            if (mediaPlayer.isPlaying())
                mediaPlayer.stop();
            mediaPlayer.reset();

            if (!TextUtils.isEmpty(path)) {
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepareAsync();
            } else {
                goPlayDemo();
            }
        } catch (Exception e) {//IO
            e.printStackTrace();
        }
    }


    String pathDemo;

    public void goPlayDemo() {
        pathDemo = "/sdcard/reee/6.mp4";
        //        pathDemo = "/sdcard/reee/7.MOV";
        //        pathDemo = "/sdcard/reee/5.mp4";

        try {
            mediaPlayer.setDataSource(pathDemo);
            mediaPlayer.setLooping(false);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {//IO
            e.printStackTrace();
        }
    }


    private Snackbar snackbar;

    private void updateFabState(boolean isPlaying) {
        if (isPlaying) {
            mImageViewPlayOrPause.setTag("start");
            mImageViewPlayOrPause.setImageResource(R.mipmap.ic_tag_pause);
        } else {
            mImageViewPlayOrPause.setTag("pause");
            mImageViewPlayOrPause.setImageResource(R.mipmap.ic_tag_play);
        }
        if (dynamicLine != null)
            dynamicLine.run(isPlaying);
    }

    private Runnable rTrue;
    private Runnable rFalse;

    private void updateFabStatePost(boolean isPlaying) {
        if (isPlaying) {
            if (rTrue == null)
                rTrue = new Runnable() {
                    @Override
                    public void run() {
                        updateFabState(true);
                    }
                };
            mImageViewPlayOrPause.post(rTrue);
        } else {
            if (rFalse == null)
                rFalse = new Runnable() {
                    @Override
                    public void run() {
                        updateFabState(false);
                    }
                };
            mImageViewPlayOrPause.post(rFalse);
        }
    }

    private long oldTime;

    @Override
    public boolean updateProgressBack(long duration, long currentPosition) {
        //dynamicLine.setmWaitRefreshLength(currentPosition);
        return false;
    }

    @Override
    public void fullScreen() {
        setRequestedOrientation((getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) ? //
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ://
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
    }

    @Override
    public void onProgressChanged(long currtentProgress) {
        long dou = currtentProgress - oldProgress;
        Log.i("===================>", "拖动跳转到currtentProgress=" + currtentProgress + "；oldProgress=" + oldProgress + ";dou=" + dou);
        oldProgress = currtentProgress;
    }

    private void createRandomNumber() {
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            int time = random.nextInt(seekBar.getMax());
            myRandom.add(time);
            Log.i("===================>", "添加seek到：i=" + i + "；；时间为=" + time + ";z最大时间=" + seekBar.getMax());
        }
    }


    private void seekVideo() {
        if (mediaPlayer != null && seekPoint < myRandom.size()) {
            controller.seekTo(controller.positionValue(myRandom.get(seekPoint)));
            seekPoint++;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE //
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION //
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN //
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //
                    | View.SYSTEM_UI_FLAG_FULLSCREEN //
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }


    @Override
    public void onTouchBackButton() {
        finish();
    }

    @Override
    public void onTouchRightButton() {
        //        Toast.makeText(this, "下个界面", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, TagEditActivity.class);
        startActivity(intent);
    }


    @Override
    public void seekTo(long position) {
        if (mediaPlayer != null)
            controller.seekTo(position);
    }
}
