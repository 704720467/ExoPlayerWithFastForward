//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.achartengine.model;

import java.util.ArrayList;
import java.util.List;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYSeries;

public class RangeCategorySeries extends CategorySeries {
    private List<Double> mMaxValues = new ArrayList();

    public RangeCategorySeries(String title) {
        super(title);
    }

    public synchronized void add(double minValue, double maxValue) {
        super.add(minValue);
        this.mMaxValues.add(Double.valueOf(maxValue));
    }

    public synchronized void add(String category, double minValue, double maxValue) {
        super.add(category, minValue);
        this.mMaxValues.add(Double.valueOf(maxValue));
    }

    public synchronized void remove(int index) {
        super.remove(index);
        this.mMaxValues.remove(index);
    }

    public synchronized void clear() {
        super.clear();
        this.mMaxValues.clear();
    }

    public double getMinimumValue(int index) {
        return this.getValue(index);
    }

    public double getMaximumValue(int index) {
        return ((Double)this.mMaxValues.get(index)).doubleValue();
    }

    public XYSeries toXYSeries() {
        XYSeries xySeries = new XYSeries(this.getTitle());
        int length = this.getItemCount();

        for(int k = 0; k < length; ++k) {
            xySeries.add((double)(k + 1), this.getMinimumValue(k));
            xySeries.add((double)k + 1.000001D, this.getMaximumValue(k));
        }

        return xySeries;
    }
}
