package cn.zp.zpexoplayer.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.zp.zpexoplayer.R;
import cn.zp.zpexoplayer.util.DeviceUtil;

/**
 * Created by admin on 2017/6/29.
 */

public class TagTextView extends TextView {
    private int type = 1;//目前分三种类型1:球员类，2：技术类，3：战术类
    private int paddingLeftAndroidRight;
    private int paddingTopAndroidBottom;
    private int margenTopAndLeft;

    public TagTextView(Context context) {
        this(context, null);
    }

    public TagTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
        initView();
    }


    private void initData() {
        paddingLeftAndroidRight = DeviceUtil.dp2px(getContext(), 13);
        paddingTopAndroidBottom = DeviceUtil.dp2px(getContext(), 6);
        margenTopAndLeft = DeviceUtil.dp2px(getContext(), 10);
    }

    private void initView() {
        int resourceId;
        if (type == 1)
            resourceId = R.drawable.bg_tag_orange;
        else if (type == 2)
            resourceId = R.drawable.bg_tag_yellow;
        else
            resourceId = R.drawable.bg_tag_blue;
        setBackgroundResource(resourceId);

        setTextSize(DeviceUtil.dp2px(getContext(), 4));
        setTextColor(Color.parseColor("#333333"));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        setPaddingRelative(paddingLeftAndroidRight, paddingTopAndroidBottom, paddingLeftAndroidRight, paddingTopAndroidBottom);
        setLayoutParams(layoutParams);
    }
}
