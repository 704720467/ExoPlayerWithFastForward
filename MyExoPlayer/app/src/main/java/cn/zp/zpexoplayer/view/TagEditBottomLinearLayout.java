package cn.zp.zpexoplayer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.zp.zpexoplayer.R;

/**
 * 编辑标签底部布局
 * Created by zp on 2017/6/26.
 */

public class TagEditBottomLinearLayout extends LinearLayout implements View.OnClickListener {
    private View mView;
    private LinearLayout mLastLayout;
    private LinearLayout mNextLayout;
    private LinearLayout mMiddleLayout;
    private TextView mLastTv;
    private TextView mNextTv;
    private ComponentListener mComponentListener;
    private int tagCount;
    private int currentTagCount=1;

    public TagEditBottomLinearLayout(Context context) {
        super(context);
        initView();
    }

    public TagEditBottomLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TagEditBottomLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setOrientation(VERTICAL);
        LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = (LinearLayout) mInflater.inflate(R.layout.bottom_tag_edit_lay, this, false);
        mLastLayout = (LinearLayout) mView.findViewById(R.id.last_lay);
        mLastLayout.setOnClickListener(this);
        mNextLayout = (LinearLayout) mView.findViewById(R.id.next_lay);
        mNextLayout.setOnClickListener(this);
        mMiddleLayout = (LinearLayout) mView.findViewById(R.id.middle_lay);
        mMiddleLayout.setOnClickListener(this);
        mLastTv = (TextView) mView.findViewById(R.id.last_tv);
        mNextTv = (TextView) mView.findViewById(R.id.next_tv);
        addView(mView);
    }


    public void setComponentListener(ComponentListener mComponentListener) {
        this.mComponentListener = mComponentListener;
    }


    @Override
    public void onClick(View v) {
        if (mComponentListener == null)
            return;
        switch (v.getId()) {
            case R.id.last_lay:
                mComponentListener.onTouchLast();
                break;
            case R.id.next_lay:
                mComponentListener.onTouchNext();
                break;
            case R.id.middle_lay:
                mComponentListener.onTouchAddLabel();
                break;
        }
    }


    public void setTagCount(int tagCount) {
        this.tagCount = tagCount;
    }

    public void setCurrentTagCount(int currentTagCount) {
        this.currentTagCount = currentTagCount;
    }

    /**
     * 顶部布局点击事件监听
     */
    public interface ComponentListener {
        /**
         * 上一个标签
         */
        void onTouchLast();

        /**
         * 下一个标签
         */
        void onTouchNext();

        /**
         * 添加标签
         */
        void onTouchAddLabel();
    }

}
