package cn.zp.zpexoplayer;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.SeekBar;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;
import java.util.Random;

import cn.zp.zpexoplayer.exoplayer.IMediaPlayer;
import cn.zp.zpexoplayer.exoplayer.KExoMediaPlayer;
import cn.zp.zpexoplayer.model.MyTime;
import cn.zp.zpexoplayer.util.DeviceUtil;
import cn.zp.zpexoplayer.view.DynamicLine;
import cn.zp.zpexoplayer.view.MyCircleLinearLayout;

public class MainActivity extends AppCompatActivity implements PlaybackControlView.VideoControlLinstion, IMediaPlayer.OnPreparedListener, MyCircleLinearLayout.MyCircleLinearLayListener, View.OnClickListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnCompletionListener {
    private Settings mSettings;
    private IMediaPlayer mediaPlayer;


    private FloatingActionButton fab;
    private FloatingActionButton mSeek;
    private SimpleExoPlayerView simpleExoPlayerView;
    MyCircleLinearLayout myCircleLinearLayout;
    //    private SeekBar mSeekbar;
    private ArrayList<Integer> myRandom = new ArrayList<>();
    private int seekPoint = 0;
    private final Runnable updateProgressAction = new Runnable() {
        @Override
        public void run() {
            seekVideo();
        }
    };
    PlaybackControlView controller;
    SeekBar seekBar;
    //进度回调

    private static final int MSG_DATA_CHANGE = 0x11;
    private DynamicLine dynamicLine;
    private ArrayList<MyTime> myTimes;
    StringBuilder formatBuilder;
    private Formatter formatter;
    private int timeWidth;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_DATA_CHANGE:
                    dynamicLine.refreshView((Long) msg.obj);
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private long oldProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        initData();
        initView();

        switchMediaEngine(null);
        Intent intent = getIntent();
        String intentAction = intent.getAction();
        if (!TextUtils.isEmpty(intentAction)) {
            if (intentAction.equals(Intent.ACTION_VIEW)) {
                goPlay(intent.getDataString());
            }
        }
    }

    private void initData() {
        if (mSettings == null)
            mSettings = new Settings(this);
        speed = mSettings.getSonicSpeed();
        timeWidth = 200;
        formatBuilder = new StringBuilder();
        formatter = new Formatter(formatBuilder, Locale.getDefault());
        if (myTimes == null)
            myTimes = new ArrayList<>();

    }

    private void initView() {
        //        mSeekbar = (SeekBar) findViewById(R.id.seekBar);
        myCircleLinearLayout = (MyCircleLinearLayout) findViewById(R.id.pmn_speed1);
        myCircleLinearLayout.setOnClickListener(this);
        myCircleLinearLayout.setMyCircleLinearLayListener(this);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
        ViewGroup.LayoutParams lp = simpleExoPlayerView.getLayoutParams();
        lp.width = DeviceUtil.getScreenWidthSize(this);
        lp.height = DeviceUtil.getPlayerHeightSize(this);
        simpleExoPlayerView.setLayoutParams(lp);
        simpleExoPlayerView.setController(findViewById(R.id.mseebar), true);
        mSeek = (FloatingActionButton) findViewById(R.id.seek);
        mSeek.setOnClickListener(this);
        controller = simpleExoPlayerView.getController();
        seekBar = controller.getProgressBar();
        dynamicLine = (DynamicLine) this.findViewById(R.id.DynamicLine);
        dynamicLine.setMyTimes(myTimes);
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying())
            mediaPlayer.stop();
        mHandler.removeCallbacks(updateProgressAction);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pmn_speed1:
                myCircleLinearLayout.onClick();
                break;
            case R.id.fab:
                if ("pause".equals(v.getTag()))
                    goPlay(null);
                else if ("start".equals(v.getTag())) {
                    mediaPlayer.pause();
                    updateFabState(false);
                }
                break;
            case R.id.seek:
                mSeek.setVisibility(View.GONE);
                createRandomNumber();
                break;
        }
    }


    @Override
    public void onPrepared(IMediaPlayer mp) {
        mediaPlayer.setSonicSpeed(speed);
        mp.start();
        updateFabStatePost(true);
        //        mSeekbar.setMax(Math.round(mediaPlayer.getDuration()));
        //mSeekbar.setProgress(Math.round(mediaPlayer.getCurrentPosition()));
        myTimes.clear();
        for (int i = 0; i * 1000 <= Math.round(mediaPlayer.getDuration()); i++) {
            MyTime m = new MyTime(null, i * 1000, stringForTime(i * 1000), timeWidth, i * timeWidth);
            Log.e("添加tga", "i=" + i);
            myTimes.add(m);
        }

        Message message = mHandler.obtainMessage(MSG_DATA_CHANGE);
        message.obj = oldProgress;
        mHandler.sendMessage(message);
        oldProgress = 0;
    }

    @Override
    public boolean onError(IMediaPlayer mp, int what, int extra) {
        showSnackbar("onError : " + what + "#" + extra);
        return false;
    }

    @Override
    public void onCompletion(IMediaPlayer mp) {
        showSnackbar("Play Completion");
        updateFabStatePost(false);
    }


    private float speed;

    @Override
    public void onNumChange(View view, float num) {
        if (view.getId() == R.id.pmn_speed1) {
            mSettings.setSonicSpeed(speed = num);
            mediaPlayer.setSonicSpeed(num);
        }
    }

    public void switchMediaEngine(View v) {
        if (mediaPlayer != null)
            mediaPlayer.stop();

        mediaPlayer = new KExoMediaPlayer(this, simpleExoPlayerView, this);

        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnCompletionListener(this);

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
                showSnackbar("goPlayAudioPath...");
            } else {
                goPlayDemo();
                showSnackbar("goPlayDemo...");
            }
        } catch (Exception e) {//IO
            e.printStackTrace();
        }
    }


    String pathDemo;

    public void goPlayDemo() {
        //        pathDemo = "/sdcard/reee/6.mp4";
        pathDemo = "/sdcard/reee/7.MOV";
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

    public void showSnackbar(@NonNull CharSequence text) {

        //        if (fab != null && !TextUtils.isEmpty(text)) {
        //            if (snackbar == null)
        //                snackbar = Snackbar.make(fab, text, Snackbar.LENGTH_LONG).setAction("Action", null);
        //            else
        //                snackbar.setText(text);
        //
        //            if (!snackbar.isShown())
        //                snackbar.show();
        //        }
    }

    private void updateFabState(boolean isPlaying) {
        if (isPlaying) {
            fab.setTag("start");
            fab.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            fab.setTag("pause");
            fab.setImageResource(android.R.drawable.ic_media_play);
        }
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
            fab.post(rTrue);
        } else {
            if (rFalse == null)
                rFalse = new Runnable() {
                    @Override
                    public void run() {
                        updateFabState(false);
                    }
                };
            fab.post(rFalse);
        }
    }

    private long oldTime;

    @Override
    public boolean updateProgressBack(long duration, long currentPosition) {
        long dou = currentPosition - oldProgress;
        long nowTime = System.currentTimeMillis();
        //        dou=20;
        if (dou > 0) {
            Log.e("===================>", "跳转到currtentProgress=" + currentPosition + "；oldProgress=" + oldProgress + ";dou=" + dou + ";time=" + (nowTime - oldTime));
            oldTime = nowTime;
            //            Message message = mHandler.obtainMessage(MSG_DATA_CHANGE);
            //            message.obj = dou;
            //            mHandler.sendMessage(message);
            oldProgress = currentPosition;
            dynamicLine.refreshView(dou);
        }
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
        Log.e("===================>", "拖动跳转到currtentProgress=" + currtentProgress + "；oldProgress=" + oldProgress + ";dou=" + dou);
        Message message = mHandler.obtainMessage(MSG_DATA_CHANGE);
        message.obj = dou;
        mHandler.sendMessage(message);
        oldProgress = currtentProgress;
    }

    private void createRandomNumber() {
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            int time = random.nextInt(seekBar.getMax());
            myRandom.add(time);
            Log.e("===================>", "添加seek到：i=" + i + "；；时间为=" + time + ";z最大时间=" + seekBar.getMax());
        }
        mHandler.postDelayed(updateProgressAction, 2000);
    }


    private void seekVideo() {
        if (mediaPlayer != null && seekPoint < myRandom.size()) {
            controller.seekTo(controller.positionValue(myRandom.get(seekPoint)));
            seekPoint++;
            mHandler.postDelayed(updateProgressAction, 4000);
        }
    }

    private String stringForTime(long timeMs) {
        if (timeMs == C.TIME_UNSET) {
            timeMs = 0;
        }
        long totalSeconds = (timeMs + 500) / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        formatBuilder.setLength(0);
        return formatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
    }
}
