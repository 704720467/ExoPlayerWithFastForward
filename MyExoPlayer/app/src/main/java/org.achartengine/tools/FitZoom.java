//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.achartengine.tools;

import org.achartengine.chart.AbstractChart;
import org.achartengine.chart.RoundChart;
import org.achartengine.chart.XYChart;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.tools.AbstractTool;

public class FitZoom extends AbstractTool {
    public FitZoom(AbstractChart chart) {
        super(chart);
    }

    public void apply() {
        if(this.mChart instanceof XYChart) {
            if(((XYChart)this.mChart).getDataset() == null) {
                return;
            }

            int renderer = this.mRenderer.getScalesCount();
            if(this.mRenderer.isInitialRangeSet()) {
                for(int series = 0; series < renderer; ++series) {
                    if(this.mRenderer.isInitialRangeSet(series)) {
                        this.mRenderer.setRange(this.mRenderer.getInitialRange(series), series);
                    }
                }
            } else {
                XYSeries[] var11 = ((XYChart)this.mChart).getDataset().getSeries();
                Object range = null;
                int length = var11.length;
                if(length > 0) {
                    for(int i = 0; i < renderer; ++i) {
                        double[] var12 = new double[]{1.7976931348623157E308D, -1.7976931348623157E308D, 1.7976931348623157E308D, -1.7976931348623157E308D};

                        for(int marginX = 0; marginX < length; ++marginX) {
                            if(i == var11[marginX].getScaleNumber()) {
                                var12[0] = Math.min(var12[0], var11[marginX].getMinX());
                                var12[1] = Math.max(var12[1], var11[marginX].getMaxX());
                                var12[2] = Math.min(var12[2], var11[marginX].getMinY());
                                var12[3] = Math.max(var12[3], var11[marginX].getMaxY());
                            }
                        }

                        double var13 = Math.abs(var12[1] - var12[0]) / 40.0D;
                        double marginY = Math.abs(var12[3] - var12[2]) / 40.0D;
                        this.mRenderer.setRange(new double[]{var12[0] - var13, var12[1] + var13, var12[2] - marginY, var12[3] + marginY}, i);
                    }
                }
            }
        } else {
            DefaultRenderer var10 = ((RoundChart)this.mChart).getRenderer();
            var10.setScale(var10.getOriginalScale());
        }

    }
}
