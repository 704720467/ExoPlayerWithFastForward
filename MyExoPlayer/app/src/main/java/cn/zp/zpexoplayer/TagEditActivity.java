package cn.zp.zpexoplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.util.ArrayList;

import cn.zp.zpexoplayer.exoplayer.IMediaPlayer;
import cn.zp.zpexoplayer.exoplayer.KExoMediaPlayer;
import cn.zp.zpexoplayer.util.DeviceUtil;
import cn.zp.zpexoplayer.view.TagEditBottomLinearLayout;
import cn.zp.zpexoplayer.view.TagEditController;
import cn.zp.zpexoplayer.view.TagEditDynamicTimeLine;
import cn.zp.zpexoplayer.view.TopLinearLayout;

public class TagEditActivity extends AppCompatActivity implements TopLinearLayout.TopLinearLayListener, IMediaPlayer.OnPreparedListener, View.OnClickListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnCompletionListener {
    private int seekPoint = 0;
    private Settings mSettings;

    private IMediaPlayer mediaPlayer;
    private SimpleExoPlayerView simpleExoPlayerView;
    private ArrayList<Integer> myRandom = new ArrayList<>();
    private TagEditController mTagEditController;
    private TagEditBottomLinearLayout mTagEditBottomLinearLayout;
    PlaybackControlView controller;
    private TopLinearLayout topLinearLayout;
    private TagEditDynamicTimeLine tagEditDynamicTimeLine;
    private LinearLayout deleltTagLay;//删除Tag
    private LinearLayout adjustmentTagLay;//调整时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_edit);
        initData();
        initView();
        switchMediaEngine(null);
        goPlay(null);
        mTagEditController = new TagEditController(mediaPlayer, mTagEditBottomLinearLayout, tagEditDynamicTimeLine);
    }

    private void initData() {
        if (mSettings == null)
            mSettings = new Settings(this);
    }

    private void initView() {
        topLinearLayout = (TopLinearLayout) findViewById(R.id.my_top_lay);
        topLinearLayout.setTopLinearLayListener(this);
        topLinearLayout.setmTitleText("标签标记");
        deleltTagLay = (LinearLayout) findViewById(R.id.delelt_tag_lay);
        deleltTagLay.setOnClickListener(this);
        adjustmentTagLay = (LinearLayout) findViewById(R.id.adjustment_tag_lay);
        adjustmentTagLay.setOnClickListener(this);
        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
        ViewGroup.LayoutParams lp = simpleExoPlayerView.getLayoutParams();
        lp.width = DeviceUtil.getScreenWidthSize(this);
        lp.height = DeviceUtil.getPlayerHeightSize(this);
        simpleExoPlayerView.setLayoutParams(lp);
        controller = simpleExoPlayerView.getController();
        tagEditDynamicTimeLine = (TagEditDynamicTimeLine) this.findViewById(R.id.tagEditDynamicTimeLine);
        mTagEditBottomLinearLayout = (TagEditBottomLinearLayout) findViewById(R.id.tag_bottom_lay);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delelt_tag_lay:
                mTagEditController.deleteTag();
                break;
            case R.id.adjustment_tag_lay:
                break;
        }
    }


    @Override
    public void onPrepared(IMediaPlayer mp) {
        //准备完毕
    }

    @Override
    public boolean onError(IMediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onCompletion(IMediaPlayer mp) {

    }


    public void switchMediaEngine(View v) {
        if (mediaPlayer != null)
            mediaPlayer.stop();
        mediaPlayer = new KExoMediaPlayer(this, simpleExoPlayerView);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnCompletionListener(this);
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
        mTagEditController.reBackdeleteTag();
        finish();
    }

    @Override
    public void onTouchRightButton() {
        Toast.makeText(this, "提交数据", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer.stop();
        }
    }
}
