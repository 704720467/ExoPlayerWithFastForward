package cn.zp.zpexoplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.zp.zpexoplayer.R;

/**
 * 过场适配器
 */
public class TransitionAdapter extends RecyclerView.Adapter<TransitionAdapter.ViewHolder> implements OnClickListener {

    private List<String> mDatas;
    private ArrayList<Integer> selectArrayList;
    private ArrayList<Integer> unSelectArrayList;
    private LayoutInflater mInflater;
    private int selectPosition = 0;
    private MyItemClickListener mItemClickListener;

    public TransitionAdapter(Context context, List<String> datats) {
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_transition_style, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mTxt = (TextView) view.findViewById(R.id.time);
        view.setOnClickListener(this);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        viewHolder.itemView.setTag(position);
        viewHolder.mTxt.setText(mDatas.get(position));
        //        viewHolder.mTxt.setText(position+"");
        //viewHolder.mTxt.setTextColor(Color.parseColor(selectPosition == position ? "#ee5514" : "#999999"));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        TextView mTxt;
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        int newPosition = (Integer) v.getTag();//新的选中的位置
        changSelectState(newPosition);
        mItemClickListener.onTransitionItemClick(v, selectPosition);
    }

    /**
     * @param newPosition 新的选中的位置
     */
    private void changSelectState(int newPosition) {
        int oldPosition = selectPosition;//旧的选中的位置
        selectPosition = newPosition;
        if (oldPosition != -1) {
            notifyItemChanged(oldPosition);
        }
        notifyItemChanged(selectPosition);
    }


    public interface MyItemClickListener {
        public void onTransitionItemClick(View view, int postion);
    }

}
