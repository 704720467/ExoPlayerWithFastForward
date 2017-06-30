package cn.zp.zpexoplayer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioGroup;

import cn.zp.zpexoplayer.util.DeviceUtil;

/**
 * 流式布局的RadioGroup
 */
public class FlowRadioGroup extends RadioGroup {
    private int margenTopAndLeft;

    public FlowRadioGroup(Context context) {
        super(context);
        initData();
    }

    public FlowRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    private void initData() {
        margenTopAndLeft = DeviceUtil.dp2px(getContext(), 10);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int childCount = getChildCount();
        int x = 0;
        int y = 0;
        int row = 0;

        for (int index = 0; index < childCount; index++) {
            final View child = getChildAt(index);
            if (child.getVisibility() != View.GONE) {
                child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
                // 此处增加onlayout中的换行判断，用于计算所需的高度
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                x += (width + margenTopAndLeft);
                y = row * (height + margenTopAndLeft) + height + margenTopAndLeft;
                if (x > maxWidth) {
                    x = width + margenTopAndLeft;
                    row++;
                    y = row * (height + margenTopAndLeft) + height + margenTopAndLeft;
                }
            }
        }
        // 设置容器所需的宽度和高度
        setMeasuredDimension(maxWidth, y);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int childCount = getChildCount();
        int maxWidth = r - l;
        int x = 0;
        int y = 0;
        int row = 0;
        for (int i = 0; i < childCount; i++) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                x += (width + margenTopAndLeft);
                y = row * (height + margenTopAndLeft) + height + margenTopAndLeft;
                if (x > maxWidth) {
                    x = width + margenTopAndLeft;
                    row++;
                    y = row * (height + margenTopAndLeft) + height + margenTopAndLeft;
                }
                child.layout(x - width, y - height, x, y);
            }
        }
    }
}