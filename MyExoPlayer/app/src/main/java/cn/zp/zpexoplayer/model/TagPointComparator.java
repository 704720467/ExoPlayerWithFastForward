package cn.zp.zpexoplayer.model;

import java.util.Comparator;

/**
 * 大点不计较器
 * <p>
 * Created by zp on 2017/6/25.
 */

public class TagPointComparator implements Comparator<MyPoint> {


    @Override
    public int compare(MyPoint o1, MyPoint o2) {
        if (o1.getTrealTime() < o2.getTrealTime()) {
            return -1;
        } else {
            return 0;
        }
    }
}
