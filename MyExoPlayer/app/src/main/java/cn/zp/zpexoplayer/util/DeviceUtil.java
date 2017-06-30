package cn.zp.zpexoplayer.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by admin on 2017/6/19.
 */

public class DeviceUtil {

    /**
     * 获取屏幕宽单位是像素
     *
     * @param context
     * @return
     */
    public static int getScreenWidthSize(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高单位是像素
     *
     * @param context
     * @return
     */
    public static int getScreenHeightSize(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }


    /**
     * 获取屏幕高单位是像素
     * <p>
     * 高根据宽以16:9算出来的
     *
     * @param context
     * @return
     */
    public static int getPlayerHeightSize(Context context) {
        int screenWidth = getScreenWidthSize(context);
        return Math.round((float) screenWidth / 16 * 9);
    }

    /**
     * dp转像素
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * 像素转dp
     *
     * @param context
     * @param px
     * @return
     */
    public static int px2dp(Context context, int px) {
        return (int) (px / context.getResources().getDisplayMetrics().density + 0.5f);
    }


    public static void hideSystemUI(View decorView) {
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE //
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION //
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN //
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //
                | View.SYSTEM_UI_FLAG_FULLSCREEN //
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public static void showSystemUI(View decorView) {
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE //
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION //
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
