package cn.zp.zpexoplayer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.exoplayer2.C;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;

import cn.zp.zpexoplayer.model.MyTime;
import cn.zp.zpexoplayer.view.DynamicLine;

public class TrainGradeActivity extends AppCompatActivity {
    private static final int MSG_DATA_CHANGE = 0x11;
    private DynamicLine dynamicLine;
    private int mX = -150;
    private ArrayList<MyTime> myTimes;
    StringBuilder formatBuilder;
    private Formatter formatter;
    private int timeWidth;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏

        setContentView(R.layout.activity_train_grade);
        initData();
        //动态绘制压力曲线视图
        dynamicLine = (DynamicLine) this.findViewById(R.id.DynamicLine);
        dynamicLine.setMyTimes(myTimes);

        //新建线程,模拟消息发送，重绘压力曲线
        new Thread() {
            public void run() {
                try {
                    while (true) {
                        long dou = 20;
                        Message message = mHandler.obtainMessage(MSG_DATA_CHANGE);
                        message.obj = dou;
                        mHandler.sendMessage(message);
                        sleep(5);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
    private void initData() {
        timeWidth = 200;
        formatBuilder = new StringBuilder();
        formatter = new Formatter(formatBuilder, Locale.getDefault());
        if (myTimes == null)
            myTimes = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            MyTime m = new MyTime(null, i * 1000, stringForTime(i * 1000), timeWidth, i * timeWidth);
            myTimes.add(m);
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
