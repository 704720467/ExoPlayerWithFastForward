package cn.zp.zpexoplayer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.zp.zpexoplayer.R;

/**
 * 标题布局
 * Created by zp on 2017/6/19.
 */

public class TopLinearLayout extends LinearLayout implements View.OnClickListener {
    private View mView;
    private LinearLayout backLay;
    private LinearLayout rightLayout;
    private ImageView mBack;
    private ImageView mRighrt;
    private TextView mTitle;
    private int height;
    private TopLinearLayListener mTopLinearLayListener;

    public TopLinearLayout(Context context) {
        super(context);
        initView();
    }

    public TopLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TopLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setOrientation(VERTICAL);
        LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = (LinearLayout) mInflater.inflate(R.layout.top_lay, this, false);
        backLay = (LinearLayout) mView.findViewById(R.id.back_lay);
        backLay.setOnClickListener(this);
        rightLayout = (LinearLayout) mView.findViewById(R.id.right_lay);
        rightLayout.setOnClickListener(this);
        mBack = (ImageView) mView.findViewById(R.id.img_back);
        mBack.setOnClickListener(this);
        mRighrt = (ImageView) mView.findViewById(R.id.img_right);
        mRighrt.setOnClickListener(this);
        mTitle = (TextView) mView.findViewById(R.id.tv_title);
        addView(mView);
    }


    @Override
    public void onClick(View v) {
        if (mTopLinearLayListener == null)
            return;
        switch (v.getId()) {
            case R.id.back_lay:
            case R.id.img_back:
                mTopLinearLayListener.onTouchBackButton();
                break;
            case R.id.right_lay:
            case R.id.img_right:
                mTopLinearLayListener.onTouchRightButton();
                break;
        }
    }

    public void setTopLinearLayListener(TopLinearLayListener mTopLinearLayListener) {
        this.mTopLinearLayListener = mTopLinearLayListener;
    }


    /**
     * 顶部布局点击事件监听
     */
    public interface TopLinearLayListener {
        void onTouchBackButton();

        void onTouchRightButton();
    }


    public LinearLayout getBackLay() {
        return backLay;
    }

    public void setBackLay(LinearLayout backLay) {
        this.backLay = backLay;
    }

    public ImageView getmBack() {
        return mBack;
    }

    public void setmBack(ImageView mBack) {
        this.mBack = mBack;
    }

    public ImageView getmRighrt() {
        return mRighrt;
    }

    public void setmRighrt(ImageView mRighrt) {
        this.mRighrt = mRighrt;
    }

    public TextView getmTitle() {
        return mTitle;
    }

    public void setmTitleText(String title) {
        if (mTitle != null)
            mTitle.setText(title);
    }

    public LinearLayout getRightLayout() {
        return rightLayout;
    }

    public void setRightLayout(LinearLayout rightLayout) {
        this.rightLayout = rightLayout;
    }

}
