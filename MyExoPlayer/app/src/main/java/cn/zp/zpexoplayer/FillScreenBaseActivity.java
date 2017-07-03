package cn.zp.zpexoplayer;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;

/**
 * Activity基类
 */
public class FillScreenBaseActivity extends FragmentActivity {
    private boolean mDestroy;

    @Override
    protected void onCreate(Bundle arg0) {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE //
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION //
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN //
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //
                | View.SYSTEM_UI_FLAG_FULLSCREEN //
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        super.onCreate(arg0);
        mDestroy = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDestroy = true;
    }

    protected boolean destroyed() {
        return mDestroy;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
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
}
