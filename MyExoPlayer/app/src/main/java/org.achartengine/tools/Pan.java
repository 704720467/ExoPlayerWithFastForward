//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.achartengine.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.achartengine.chart.AbstractChart;
import org.achartengine.chart.RoundChart;
import org.achartengine.chart.XYChart;
import org.achartengine.tools.AbstractTool;
import org.achartengine.tools.PanListener;

public class Pan extends AbstractTool {
    private List<PanListener> mPanListeners = new ArrayList();
    private boolean limitsReachedX = false;
    private boolean limitsReachedY = false;

    public Pan(AbstractChart chart) {
        super(chart);
    }

    public void apply(float oldX, float oldY, float newX, float newY) {
        boolean notLimitedUp = true;
        boolean notLimitedBottom = true;
        boolean notLimitedLeft = true;
        boolean notLimitedRight = true;
        if(!(this.mChart instanceof XYChart)) {
            RoundChart var28 = (RoundChart)this.mChart;
            var28.setCenterX(var28.getCenterX() + (int)(newX - oldX));
            var28.setCenterY(var28.getCenterY() + (int)(newY - oldY));
        } else {
            int chart = this.mRenderer.getScalesCount();
            double[] limits = this.mRenderer.getPanLimits();
            boolean limited = limits != null && limits.length == 4;
            XYChart chart1 = (XYChart)this.mChart;

            for(int i = 0; i < chart; ++i) {
                double[] range = this.getRange(i);
                double[] calcRange = chart1.getCalcRange(i);
                if(this.limitsReachedX && this.limitsReachedY && (range[0] == range[1] && calcRange[0] == calcRange[1] || range[2] == range[3] && calcRange[2] == calcRange[3])) {
                    return;
                }

                this.checkRange(range, i);
                double[] realPoint = chart1.toRealPoint(oldX, oldY, i);
                double[] realPoint2 = chart1.toRealPoint(newX, newY, i);
                double deltaX = realPoint[0] - realPoint2[0];
                double deltaY = realPoint[1] - realPoint2[1];
                double ratio = this.getAxisRatio(range);
                if(chart1.isVertical(this.mRenderer)) {
                    double newDeltaX = -deltaY * ratio;
                    double newDeltaY = deltaX / ratio;
                    deltaX = newDeltaX;
                    deltaY = newDeltaY;
                }

                if(this.mRenderer.isPanXEnabled()) {
                    if(limits != null) {
                        if(notLimitedLeft) {
                            notLimitedLeft = limits[0] <= range[0] + deltaX;
                        }

                        if(notLimitedRight) {
                            notLimitedRight = limits[1] >= range[1] + deltaX;
                        }
                    }

                    if(limited && (!notLimitedLeft || !notLimitedRight)) {
                        this.limitsReachedX = true;
                    } else {
                        this.setXRange(range[0] + deltaX, range[1] + deltaX, i);
                        this.limitsReachedX = false;
                    }
                }

                if(this.mRenderer.isPanYEnabled()) {
                    if(limits != null) {
                        if(notLimitedBottom) {
                            notLimitedBottom = limits[2] <= range[2] + deltaY;
                        }

                        if(notLimitedUp) {
                            notLimitedUp = limits[3] >= range[3] + deltaY;
                        }
                    }

                    if(limited && (!notLimitedBottom || !notLimitedUp)) {
                        this.limitsReachedY = true;
                    } else {
                        this.setYRange(range[2] + deltaY, range[3] + deltaY, i);
                        this.limitsReachedY = false;
                    }
                }
            }
        }

        this.notifyPanListeners();
    }

    private double getAxisRatio(double[] range) {
        return Math.abs(range[1] - range[0]) / Math.abs(range[3] - range[2]);
    }

    private synchronized void notifyPanListeners() {
        Iterator i$ = this.mPanListeners.iterator();

        while(i$.hasNext()) {
            PanListener listener = (PanListener)i$.next();
            listener.panApplied();
        }

    }

    public synchronized void addPanListener(PanListener listener) {
        this.mPanListeners.add(listener);
    }

    public synchronized void removePanListener(PanListener listener) {
        this.mPanListeners.remove(listener);
    }
}
