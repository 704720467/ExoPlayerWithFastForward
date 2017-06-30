package cn.zp.zpexoplayer.model;

import java.io.Serializable;

/**
 * tag类型对象
 * Created by admin on 2017/6/29.
 */

public class MyTag implements Serializable {
    static final long serialVersionUID = 1L;
    private String tagName = "";
    private boolean selected = false;
    private TagType.EnumTagType type = TagType.EnumTagType.PLAYER;

    public MyTag(boolean selected, String tagName, TagType.EnumTagType type) {
        this.selected = selected;
        this.tagName = tagName;
        this.type = type;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public TagType.EnumTagType getType() {
        return type;
    }

    public String getTypeName() {
        String typeName = "";
        switch (type) {
            case PLAYER:
                typeName = "球员类";
                break;
            case TECHNOLOGY:
                typeName = "技术类";
                break;
            case TACTICS:
                typeName = "战术类";
                break;
        }
        return typeName;
    }

    public int getTypeNumber() {
        int typeNumber = 0;
        switch (type) {
            case PLAYER:
                typeNumber = 0;
                break;
            case TECHNOLOGY:
                typeNumber = 1;
                break;
            case TACTICS:
                typeNumber = 2;
                break;
        }
        return typeNumber;
    }


    public void setType(TagType.EnumTagType type) {
        this.type = type;
    }
}
