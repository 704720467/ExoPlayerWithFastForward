package cn.zp.zpexoplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.zp.zpexoplayer.R;
import cn.zp.zpexoplayer.model.MyTag;
import cn.zp.zpexoplayer.model.TagType;

/**
 * 右侧ListViewAdapter
 */
public class RightAdapter extends CustomizeLVBaseAdapter {
    //上下文
    private Context mContext;
    //标题
    private ArrayList<TagType> leftStr;
    //内容
    private List<List<MyTag>> rightStr;
    private LayoutInflater inflater;
    private MyItemClickListener mItemClickListener;

    public RightAdapter(Context mContext, ArrayList<TagType> leftStr, List<List<MyTag>> rightStr) {
        this.mContext = mContext;
        this.leftStr = leftStr;
        this.rightStr = rightStr;
        //系统服务
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object getItem(int section, int position) {
        return rightStr.get(section).get(position);
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
        return leftStr.size();
    }

    @Override
    public int getCountForSection(int section) {
        return rightStr.get(section).size();
    }

    @Override
    public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
        ChildViewHolder holder = null;
        if (convertView == null) {
            holder = new ChildViewHolder();
            //加载
            convertView = inflater.inflate(R.layout.item_tag, parent, false);
            //绑定
            //            holder.mTextViewTypeName = (TextView) convertView.findViewById(R.id.tv_tag_type);
            holder.mTagName = (TextView) convertView.findViewById(R.id.tag_name);
            holder.mImageSelect = (ImageView) convertView.findViewById(R.id.img_select);
            holder.mBottomLine = (View) convertView.findViewById(R.id.bottom_line);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }


        //设置内容
        //        holder.lv_customize_item_text.setText(rightStr.get(section).get(position));
        reflushView(holder, section, position);
        //点击事件
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rightStr.get(section).get(position).setSelected(!rightStr.get(section).get(position).isSelected());
                if (mItemClickListener != null)
                    mItemClickListener.onTransitionItemClick(view, section, position, rightStr.get(section).get(position).isSelected());
                ((ImageView) view.findViewById(R.id.img_select)).setImageResource(rightStr.get(section).get(position).isSelected() ? R.mipmap.ic_select_light : R.mipmap.ic_select);
            }
        });


        return convertView;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        HeaderViewHolder holder = null;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            //加载
            convertView = inflater.inflate(R.layout.lv_customize_item_header, parent, false);
            //绑定
            holder.lv_customize_item_header_text = (TextView) convertView.findViewById(R.id.lv_customize_item_header_text);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        //不可点击
        convertView.setClickable(false);
        //设置标题
        holder.lv_customize_item_header_text.setText(leftStr.get(section).getTypeName());
        return convertView;
    }


    /**
     * 刷新界面
     */
    private void reflushView(ChildViewHolder holder, int section, int position) {
        MyTag myTag = rightStr.get(section).get(position);
        //        holder.mTextViewTypeName.setText(myTag.getTypeName());
        holder.mTagName.setText(myTag.getTagName());
        holder.mImageSelect.setImageResource(myTag.isSelected() ? R.mipmap.ic_select_light : R.mipmap.ic_select);
        boolean sameAsLast = position != 0;
        //        holder.mTextViewTypeName.setVisibility(sameAsLast ? View.GONE : View.VISIBLE);
        holder.mBottomLine.setVisibility(sameAsLast ? View.VISIBLE : View.GONE);
    }

    class ChildViewHolder {
        //        TextView mTextViewTypeName;
        TextView mTagName;
        ImageView mImageSelect;
        View mBottomLine;
    }

    class HeaderViewHolder {
        //标题
        private TextView lv_customize_item_header_text;
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(RightAdapter.MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface MyItemClickListener {
        void onTransitionItemClick(View view, int section, int postion, boolean isPlus);
    }
}

