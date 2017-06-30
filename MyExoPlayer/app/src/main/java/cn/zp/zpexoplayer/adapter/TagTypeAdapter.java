package cn.zp.zpexoplayer.adapter;

import android.content.Context;
import android.graphics.Color;
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
import cn.zp.zpexoplayer.model.TagType;

/**
 * Tag类型适配器
 */
public class TagTypeAdapter extends RecyclerView.Adapter<TagTypeAdapter.ViewHolder> implements OnClickListener {

    private ArrayList<TagType> tagTypeAry;
    private LayoutInflater mInflater;
    private int selectPosition = 0;
    private MyItemClickListener mItemClickListener;

    public TagTypeAdapter(Context context, ArrayList<TagType> tagTypeAry) {
        mInflater = LayoutInflater.from(context);
        this.tagTypeAry = tagTypeAry;
    }

    @Override
    public int getItemCount() {
        return tagTypeAry.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_tag_type, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mViewSelectState = (View) view.findViewById(R.id.select_state);
        viewHolder.mImageViewType = (ImageView) view.findViewById(R.id.img_type);
        viewHolder.mTextViewTypeName = (TextView) view.findViewById(R.id.tv_type_name);
        viewHolder.mSelectNumber = (TextView) view.findViewById(R.id.selected_number);
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
        TagType tagType = tagTypeAry.get(position);
        int imgSrc = 0;
        int bgColor = R.color.gray;
        switch (tagType.getType()) {
            case PLAYER:
                imgSrc = (position == selectPosition) ? R.mipmap.ic_type_player_light : R.mipmap.ic_type_player;
                break;
            case TECHNOLOGY:
                imgSrc = (position == selectPosition) ? R.mipmap.ic_type_technology_light : R.mipmap.ic_type_technology;
                break;
            case TACTICS:
                imgSrc = (position == selectPosition) ? R.mipmap.ic_type_tactics_light : R.mipmap.ic_type_tactics;
                break;
        }
        viewHolder.mImageViewType.setImageResource(imgSrc);
        viewHolder.mViewSelectState.setVisibility((position == selectPosition) ? View.VISIBLE : View.GONE);
        viewHolder.itemView.setBackgroundColor(Color.parseColor((position == selectPosition) ? "#ffffff" : "#ebebeb"));
        viewHolder.mTextViewTypeName.setText(tagType.getTypeName());
        viewHolder.mSelectNumber.setVisibility(View.GONE);
        if (tagType.getSelectCount() > 0) {
            viewHolder.mSelectNumber.setVisibility(View.VISIBLE);
            viewHolder.mSelectNumber.setText(tagType.getSelectCount() + "");
        }
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
        View mViewSelectState;
        ImageView mImageViewType;
        TextView mTextViewTypeName;
        TextView mSelectNumber;

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
    public void changSelectState(int newPosition) {
        int oldPosition = selectPosition;//旧的选中的位置
        selectPosition = newPosition;
        if (oldPosition != -1) {
            notifyItemChanged(oldPosition, "reflushView");
        }
        notifyItemChanged(selectPosition, "reflushView");
    }


    public interface MyItemClickListener {
        public void onTransitionItemClick(View view, int postion);
    }


}
