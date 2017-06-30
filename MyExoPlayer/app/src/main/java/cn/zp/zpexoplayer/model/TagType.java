package cn.zp.zpexoplayer.model;

/**
 * tag类型对象
 * Created by admin on 2017/6/29.
 */

public class TagType {
    public enum EnumTagType {
        PLAYER, TECHNOLOGY, TACTICS;
    }

    private EnumTagType type = EnumTagType.PLAYER;
    private String typeName = "";
    private int selectCount = 0;

    public TagType(EnumTagType type, String typeName) {
        this.type = type;
        this.typeName = typeName;
    }


    public EnumTagType getType() {
        return type;
    }

    public void setType(EnumTagType type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getSelectCount() {
        return selectCount;
    }

    public void setSelectCount(int selectCount) {
        this.selectCount = selectCount;
    }
}
