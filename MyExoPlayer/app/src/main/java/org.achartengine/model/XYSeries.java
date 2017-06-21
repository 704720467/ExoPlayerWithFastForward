//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.achartengine.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import org.achartengine.util.IndexXYMap;
import org.achartengine.util.XYEntry;

public class XYSeries implements Serializable {
    private String mTitle;
    private final IndexXYMap<Double, Double> mXY;
    private double mMinX;
    private double mMaxX;
    private double mMinY;
    private double mMaxY;
    private final int mScaleNumber;
    private List<String> mAnnotations;
    private final IndexXYMap<Double, Double> mStringXY;

    public XYSeries(String title) {
        this(title, 0);
    }

    public XYSeries(String title, int scaleNumber) {
        this.mXY = new IndexXYMap();
        this.mMinX = 1.7976931348623157E308D;
        this.mMaxX = -1.7976931348623157E308D;
        this.mMinY = 1.7976931348623157E308D;
        this.mMaxY = -1.7976931348623157E308D;
        this.mAnnotations = new ArrayList();
        this.mStringXY = new IndexXYMap();
        this.mTitle = title;
        this.mScaleNumber = scaleNumber;
        this.initRange();
    }

    public int getScaleNumber() {
        return this.mScaleNumber;
    }

    private void initRange() {
        this.mMinX = 1.7976931348623157E308D;
        this.mMaxX = -1.7976931348623157E308D;
        this.mMinY = 1.7976931348623157E308D;
        this.mMaxY = -1.7976931348623157E308D;
        int length = this.getItemCount();

        for(int k = 0; k < length; ++k) {
            double x = this.getX(k);
            double y = this.getY(k);
            this.updateRange(x, y);
        }

    }

    private void updateRange(double x, double y) {
        this.mMinX = Math.min(this.mMinX, x);
        this.mMaxX = Math.max(this.mMaxX, x);
        this.mMinY = Math.min(this.mMinY, y);
        this.mMaxY = Math.max(this.mMaxY, y);
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public synchronized void add(double x, double y) {
        while(this.mXY.get(Double.valueOf(x)) != null) {
            x += this.getPadding(x);
        }

        this.mXY.put(Double.valueOf(x), Double.valueOf(y));
        this.updateRange(x, y);
    }

    public synchronized void add(int index, double x, double y) {
        while(this.mXY.get(Double.valueOf(x)) != null) {
            x += this.getPadding(x);
        }

        this.mXY.put(index, Double.valueOf(x), Double.valueOf(y));
        this.updateRange(x, y);
    }

    protected double getPadding(double x) {
        return Math.ulp(x);
    }

    public synchronized void remove(int index) {
        XYEntry removedEntry = this.mXY.removeByIndex(index);
        double removedX = ((Double)removedEntry.getKey()).doubleValue();
        double removedY = ((Double)removedEntry.getValue()).doubleValue();
        if(removedX == this.mMinX || removedX == this.mMaxX || removedY == this.mMinY || removedY == this.mMaxY) {
            this.initRange();
        }

    }

    public synchronized void clear() {
        this.clearAnnotations();
        this.clearSeriesValues();
    }

    public synchronized void clearSeriesValues() {
        this.mXY.clear();
        this.initRange();
    }

    public synchronized void clearAnnotations() {
        this.mAnnotations.clear();
        this.mStringXY.clear();
    }

    public synchronized IndexXYMap<Double, Double> getXYMap() {
        return this.mXY;
    }

    public synchronized double getX(int index) {
        return ((Double)this.mXY.getXByIndex(index)).doubleValue();
    }

    public synchronized double getY(int index) {
        return ((Double)this.mXY.getYByIndex(index)).doubleValue();
    }

    public void addAnnotation(String annotation, double x, double y) {
        this.mAnnotations.add(annotation);

        while(this.mStringXY.get(Double.valueOf(x)) != null) {
            x += this.getPadding(x);
        }

        this.mStringXY.put(Double.valueOf(x), Double.valueOf(y));
    }

    public void removeAnnotation(int index) {
        this.mAnnotations.remove(index);
        this.mStringXY.removeByIndex(index);
    }

    public double getAnnotationX(int index) {
        return ((Double)this.mStringXY.getXByIndex(index)).doubleValue();
    }

    public double getAnnotationY(int index) {
        return ((Double)this.mStringXY.getYByIndex(index)).doubleValue();
    }

    public int getAnnotationCount() {
        return this.mAnnotations.size();
    }

    public String getAnnotationAt(int index) {
        return (String)this.mAnnotations.get(index);
    }

    public synchronized SortedMap<Double, Double> getRange(double start, double stop, boolean beforeAfterPoints) {
        if(beforeAfterPoints) {
            SortedMap headMap = this.mXY.headMap(Double.valueOf(start));
            if(!headMap.isEmpty()) {
                start = ((Double)headMap.lastKey()).doubleValue();
            }

            SortedMap tailMap = this.mXY.tailMap(Double.valueOf(stop));
            if(!tailMap.isEmpty()) {
                Iterator tailIterator = tailMap.keySet().iterator();
                Double next = (Double)tailIterator.next();
                if(tailIterator.hasNext()) {
                    stop = ((Double)tailIterator.next()).doubleValue();
                } else {
                    stop += next.doubleValue();
                }
            }
        }

        return (SortedMap)(start <= stop?this.mXY.subMap(Double.valueOf(start), Double.valueOf(stop)):new TreeMap());
    }

    public int getIndexForKey(double key) {
        return this.mXY.getIndexForKey(Double.valueOf(key));
    }

    public synchronized int getItemCount() {
        return this.mXY.size();
    }

    public double getMinX() {
        return this.mMinX;
    }

    public double getMinY() {
        return this.mMinY;
    }

    public double getMaxX() {
        return this.mMaxX;
    }

    public double getMaxY() {
        return this.mMaxY;
    }
}
