package cn.zp.zpexoplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.zp.zpexoplayer.R;
import cn.zp.zpexoplayer.model.MyTag;

/**
 * Tag类型适配器
 */
public class TagContentAdapter extends RecyclerView.Adapter<TagContentAdapter.ViewHolder> implements OnClickListener {

    private ArrayList<MyTag> mytags;
    private LayoutInflater mInflater;
    private int selectPosition = 0;
    private MyItemClickListener mItemClickListener;

    public TagContentAdapter(Context context, ArrayList<MyTag> mytags) {
        mInflater = LayoutInflater.from(context);
        this.mytags = mytags;
    }

    @Override
    public int getItemCount() {
        return mytags.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_tag, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        //        viewHolder.mTextViewTypeName = (TextView) view.findViewById(R.id.tv_tag_type);
        viewHolder.mTagName = (TextView) view.findViewById(R.id.tag_name);
        viewHolder.mImageSelect = (ImageView) view.findViewById(R.id.img_select);
        viewHolder.mBottomLine = (View) view.findViewById(R.id.bottom_line);
        view.setOnClickListener(this);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        reflushView(viewHolder, position);
    }

    /**
     * 刷新界面
     *
     * @param viewHolder
     * @param position
     */
    private void reflushView(ViewHolder viewHolder, int position) {
        viewHolder.itemView.setTag(position);
        MyTag myTag = mytags.get(position);
        viewHolder.mTextViewTypeName.setText(myTag.getTypeName());
        viewHolder.mTagName.setText(myTag.getTagName());
        viewHolder.mImageSelect.setImageResource(myTag.isSelected() ? R.mipmap.ic_select_light : R.mipmap.ic_select);
        boolean sameAsLast = position != 0 && (mytags.get(position - 1).getType() == myTag.getType());
        viewHolder.mTextViewTypeName.setVisibility(sameAsLast ? View.GONE : View.VISIBLE);
        viewHolder.mBottomLine.setVisibility(sameAsLast ? View.VISIBLE : View.GONE);
    }

    /**
     * 局部刷新关键、实现某个控件的摔性能
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            reflushView(holder, position);
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextViewTypeName;
        TextView mTagName;
        ImageView mImageSelect;
        View mBottomLine;

        public ViewHolder(View arg0) {
            super(arg0);
        }
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
        if (mItemClickListener != null)
            mItemClickListener.onTransitionItemClick(v, selectPosition);
    }

    /**
     * @param newPosition 新的选中的位置
     */
    private void changSelectState(int newPosition) {
        mytags.get(newPosition).setSelected(!mytags.get(newPosition).isSelected());
        int oldPosition = selectPosition;//旧的选中的位置
        selectPosition = newPosition;
        notifyItemChanged(selectPosition, "reflushView");
    }


    public interface MyItemClickListener {
        public void onTransitionItemClick(View view, int postion);
    }

}
