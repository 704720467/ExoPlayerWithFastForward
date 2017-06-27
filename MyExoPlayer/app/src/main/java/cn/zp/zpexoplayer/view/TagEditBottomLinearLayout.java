package cn.zp.zpexoplayer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.zp.zpexoplayer.R;
import cn.zp.zpexoplayer.application.MyApplication;

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
    private int currentTagNumber = 1;//当前选中的标签的编号

    public TagEditBottomLinearLayout(Context context) {
        super(context);
        initData();
        initView();
    }


    public TagEditBottomLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
        initView();
    }

    public TagEditBottomLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
        initView();
    }

    private void initData() {
        tagCount = MyApplication.getTagProject().getTagCount();
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
        changeViewState();
        addView(mView);
    }

    /**
     * 修改View的状态
     */
    private void changeViewState() {
        int lastState = View.INVISIBLE;
        int nextState = View.INVISIBLE;
        if (currentTagNumber > 1)
            lastState = View.VISIBLE;
        if (currentTagNumber < tagCount)
            nextState = View.VISIBLE;
        if (mLastLayout != null)
            mLastLayout.setVisibility(lastState);
        if (mNextLayout != null)
            mNextLayout.setVisibility(nextState);
        if (mLastTv != null)
            mLastTv.setText(currentTagNumber - 1 + "");
        if (mNextTv != null)
            mNextTv.setText(currentTagNumber + 1 + "");
    }


    public void setComponentListener(ComponentListener mComponentListener) {
        this.mComponentListener = mComponentListener;
    }


    @Override
    public void onClick(View v) {
        if (mComponentListener == null)
            return;
        boolean touBack = false;
        switch (v.getId()) {
            case R.id.last_lay:
                touBack = mComponentListener.onTouchLast(currentTagNumber);
                if (touBack)
                    currentTagNumber--;
                break;
            case R.id.next_lay:
                touBack = mComponentListener.onTouchNext(currentTagNumber);
                if (touBack)
                    currentTagNumber++;
                break;
            case R.id.middle_lay:
                mComponentListener.onTouchAddLabel(currentTagNumber);
                break;
        }

        if (!touBack)
            return;
        changeViewState();
    }

    /**
     * 修改当前的标签的号码
     *
     * @param currentTagNumber
     */
    public void setCurrentTagNumber(int currentTagNumber) {
        this.currentTagNumber = currentTagNumber;
        tagCount = MyApplication.getTagProject().getTagCount();
        changeViewState();
    }

    /**
     * 顶部布局点击事件监听
     */
    public interface ComponentListener {
        /**
         * 上一个标签
         */
        boolean onTouchLast(int currentTagNumber);

        /**
         * 下一个标签
         */
        boolean onTouchNext(int currentTagNumber);

        /**
         * 添加标签
         */
        void onTouchAddLabel(int currentTagNumber);
    }

}
