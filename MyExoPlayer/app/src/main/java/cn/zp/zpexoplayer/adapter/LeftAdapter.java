package cn.zp.zpexoplayer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.zp.zpexoplayer.R;
import cn.zp.zpexoplayer.model.TagType;

/**
 * 描述：    左侧Adapter
 */
public class LeftAdapter extends BaseAdapter {
    //标题
    private List<TagType> leftStr;
    //标志
    private List<Boolean> flagArray;
    private LayoutInflater inflater;

    public LeftAdapter(Context mContext, List<TagType> leftStr, List<Boolean> flagArray) {
        this.leftStr = leftStr;
        this.flagArray = flagArray;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return leftStr.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            //加载
            convertView = inflater.inflate(R.layout.item_tag_type, parent, false);
            viewHolder.mViewSelectState = (View) convertView.findViewById(R.id.select_state);
            viewHolder.mImageViewType = (ImageView) convertView.findViewById(R.id.img_type);
            viewHolder.mTextViewTypeName = (TextView) convertView.findViewById(R.id.tv_type_name);
            viewHolder.mSelectNumber = (TextView) convertView.findViewById(R.id.selected_number);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        TagType tagType = leftStr.get(position);
        int imgSrc = 0;
        int bgColor = R.color.gray;
        boolean isSelect = flagArray.get(position);
        switch (tagType.getType()) {
            case PLAYER:
                imgSrc = isSelect ? R.mipmap.ic_type_player_light : R.mipmap.ic_type_player;
                break;
            case TECHNOLOGY:
                imgSrc = isSelect ? R.mipmap.ic_type_technology_light : R.mipmap.ic_type_technology;
                break;
            case TACTICS:
                imgSrc = isSelect ? R.mipmap.ic_type_tactics_light : R.mipmap.ic_type_tactics;
                break;
        }
        viewHolder.mImageViewType.setImageResource(imgSrc);
        viewHolder.mViewSelectState.setVisibility(isSelect ? View.VISIBLE : View.GONE);
        convertView.setBackgroundColor(Color.parseColor(isSelect ? "#ffffff" : "#ebebeb"));
        viewHolder.mTextViewTypeName.setText(tagType.getTypeName());
        viewHolder.mSelectNumber.setVisibility(View.GONE);
        if (tagType.getSelectCount() > 0) {
            viewHolder.mSelectNumber.setVisibility(View.VISIBLE);
            viewHolder.mSelectNumber.setText(tagType.getSelectCount() + "");
        }
        return convertView;
    }

    class ViewHolder {
        View mViewSelectState;
        ImageView mImageViewType;
        TextView mTextViewTypeName;
        TextView mSelectNumber;
    }
}