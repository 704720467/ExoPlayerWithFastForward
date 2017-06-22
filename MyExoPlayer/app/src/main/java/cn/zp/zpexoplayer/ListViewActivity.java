package cn.zp.zpexoplayer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.exoplayer2.C;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;

import cn.zp.zpexoplayer.util.DeviceUtil;

import static java.lang.Thread.sleep;

public class ListViewActivity extends AppCompatActivity {
    private RecyclerView ptr_listview;
    private TransitionAdapter adapter;
    private ArrayList<String> myTimes;
    StringBuilder formatBuilder;
    private Formatter formatter;
    private static final int MSG_DATA_CHANGE = 0x11;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_DATA_CHANGE:
                    ptr_listview.smoothScrollBy((int) s, 0);
                    //                    ptr_listview.scrollBy(msg.arg1, 0);
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private double s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        initData();
        ptr_listview = (RecyclerView) findViewById(R.id.list_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        ptr_listview.setLayoutManager(linearLayoutManager);
        adapter = new TransitionAdapter(this, myTimes);
        ptr_listview.setAdapter(adapter);
        s = Math.floor(40.0 * DeviceUtil.dp2px(this, 103) / 1000);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    while (true) {
                        Message message = mHandler.obtainMessage(MSG_DATA_CHANGE);
                        message.arg1 = 500;
                        mHandler.sendMessage(message);
                        sleep(30);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
            }
        }).start();

    }

    private void initData() {
        formatBuilder = new StringBuilder();
        formatter = new Formatter(formatBuilder, Locale.getDefault());
        if (myTimes == null)
            myTimes = new ArrayList<>();
        for (int i = 0; i <= 200; i++) {
            myTimes.add(stringForTime(i * 1000));
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
