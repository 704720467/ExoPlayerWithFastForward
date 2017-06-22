package cn.zp.zpexoplayer.util;

import android.content.Context;
import android.util.DisplayMetrics;

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


}
