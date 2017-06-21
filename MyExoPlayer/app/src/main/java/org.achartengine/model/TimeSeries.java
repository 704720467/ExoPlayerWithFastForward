//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.achartengine.model;

import java.util.Date;
import org.achartengine.model.XYSeries;

public class TimeSeries extends XYSeries {
    public TimeSeries(String title) {
        super(title);
    }

    public synchronized void add(Date x, double y) {
        super.add((double)x.getTime(), y);
    }

    protected double getPadding(double x) {
        return 1.0D;
    }
}
