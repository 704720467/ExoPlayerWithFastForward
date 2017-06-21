//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.achartengine.chart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import java.util.List;
import org.achartengine.chart.ClickableArea;
import org.achartengine.chart.XYChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYValueSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

public class BubbleChart extends XYChart {
    public static final String TYPE = "Bubble";
    private static final int SHAPE_WIDTH = 10;
    private static final int MIN_BUBBLE_SIZE = 2;
    private static final int MAX_BUBBLE_SIZE = 20;

    BubbleChart() {
    }

    public BubbleChart(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer) {
        super(dataset, renderer);
    }

    public void drawSeries(Canvas canvas, Paint paint, List<Float> points, XYSeriesRenderer renderer, float yAxisValue, int seriesIndex, int startIndex) {
        paint.setColor(renderer.getColor());
        paint.setStyle(Style.FILL);
        int length = points.size();
        XYValueSeries series = (XYValueSeries)this.mDataset.getSeriesAt(seriesIndex);
        double max = series.getMaxValue();
        double coef = 20.0D / max;

        for(int i = 0; i < length; i += 2) {
            double size = series.getValue(startIndex + i / 2) * coef + 2.0D;
            this.drawCircle(canvas, paint, ((Float)points.get(i)).floatValue(), ((Float)points.get(i + 1)).floatValue(), (float)size);
        }

    }

    protected ClickableArea[] clickableAreasForPoints(List<Float> points, List<Double> values, float yAxisValue, int seriesIndex, int startIndex) {
        int length = points.size();
        XYValueSeries series = (XYValueSeries)this.mDataset.getSeriesAt(seriesIndex);
        double max = series.getMaxValue();
        double coef = 20.0D / max;
        ClickableArea[] ret = new ClickableArea[length / 2];

        for(int i = 0; i < length; i += 2) {
            double size = series.getValue(startIndex + i / 2) * coef + 2.0D;
            ret[i / 2] = new ClickableArea(new RectF(((Float)points.get(i)).floatValue() - (float)size, ((Float)points.get(i + 1)).floatValue() - (float)size, ((Float)points.get(i)).floatValue() + (float)size, ((Float)points.get(i + 1)).floatValue() + (float)size), ((Double)values.get(i)).doubleValue(), ((Double)values.get(i + 1)).doubleValue());
        }

        return ret;
    }

    public int getLegendShapeWidth(int seriesIndex) {
        return 10;
    }

    public void drawLegendShape(Canvas canvas, SimpleSeriesRenderer renderer, float x, float y, int seriesIndex, Paint paint) {
        paint.setStyle(Style.FILL);
        this.drawCircle(canvas, paint, x + 10.0F, y, 3.0F);
    }

    private void drawCircle(Canvas canvas, Paint paint, float x, float y, float radius) {
        canvas.drawCircle(x, y, radius, paint);
    }

    public String getChartType() {
        return "Bubble";
    }
}
