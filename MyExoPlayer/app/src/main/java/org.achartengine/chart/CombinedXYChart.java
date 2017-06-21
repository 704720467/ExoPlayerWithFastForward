//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.achartengine.chart;

import android.graphics.Canvas;
import android.graphics.Paint;
import java.io.Serializable;
import java.util.List;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.BubbleChart;
import org.achartengine.chart.ClickableArea;
import org.achartengine.chart.CubicLineChart;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.RangeBarChart;
import org.achartengine.chart.RangeStackedBarChart;
import org.achartengine.chart.ScatterChart;
import org.achartengine.chart.TimeChart;
import org.achartengine.chart.XYChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer.Orientation;

public class CombinedXYChart extends XYChart {
    private XYCombinedChartDef[] chartDefinitions;
    private XYChart[] mCharts;
    private Class<?>[] xyChartTypes = new Class[]{TimeChart.class, LineChart.class, CubicLineChart.class, BarChart.class, BubbleChart.class, ScatterChart.class, RangeBarChart.class, RangeStackedBarChart.class};

    public CombinedXYChart(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer, XYCombinedChartDef[] chartDefinitions) {
        super(dataset, renderer);
        this.chartDefinitions = chartDefinitions;
        int length = chartDefinitions.length;
        this.mCharts = new XYChart[length];

        for(int i = 0; i < length; ++i) {
            try {
                this.mCharts[i] = this.getXYChart(chartDefinitions[i].getType());
            } catch (Exception var12) {
                ;
            }

            if(this.mCharts[i] == null) {
                throw new IllegalArgumentException("Unknown chart type " + chartDefinitions[i].getType());
            }

            XYMultipleSeriesDataset newDataset = new XYMultipleSeriesDataset();
            XYMultipleSeriesRenderer newRenderer = new XYMultipleSeriesRenderer();
            int[] arr$ = chartDefinitions[i].getSeriesIndex();
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                int seriesIndex = arr$[i$];
                newDataset.addSeries(dataset.getSeriesAt(seriesIndex));
                newRenderer.addSeriesRenderer(renderer.getSeriesRendererAt(seriesIndex));
            }

            newRenderer.setBarSpacing(renderer.getBarSpacing());
            newRenderer.setPointSize(renderer.getPointSize());
            this.mCharts[i].setDatasetRenderer(newDataset, newRenderer);
        }

    }

    public CombinedXYChart(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer, XYCombinedChartDef[] chartDefinitions, XYChart[] charts) {
        super(dataset, renderer);
        this.chartDefinitions = chartDefinitions;
        this.mCharts = charts;
    }

    private XYChart getXYChart(String type) throws IllegalAccessException, InstantiationException {
        XYChart chart = null;
        int length = this.xyChartTypes.length;

        for(int i = 0; i < length && chart == null; ++i) {
            XYChart newChart = (XYChart)this.xyChartTypes[i].newInstance();
            if(type.equals(newChart.getChartType())) {
                chart = newChart;
            }
        }

        return chart;
    }

    public void drawSeries(Canvas canvas, Paint paint, List<Float> points, XYSeriesRenderer seriesRenderer, float yAxisValue, int seriesIndex, int startIndex) {
        XYChart chart = this.getXYChart(seriesIndex);
        chart.setScreenR(this.getScreenR());
        chart.setCalcRange(this.getCalcRange(this.mDataset.getSeriesAt(seriesIndex).getScaleNumber()), 0);
        chart.drawSeries(canvas, paint, points, seriesRenderer, yAxisValue, this.getChartSeriesIndex(seriesIndex), startIndex);
    }

    protected ClickableArea[] clickableAreasForPoints(List<Float> points, List<Double> values, float yAxisValue, int seriesIndex, int startIndex) {
        XYChart chart = this.getXYChart(seriesIndex);
        return chart.clickableAreasForPoints(points, values, yAxisValue, this.getChartSeriesIndex(seriesIndex), startIndex);
    }

    protected void drawSeries(XYSeries series, Canvas canvas, Paint paint, List<Float> pointsList, XYSeriesRenderer seriesRenderer, float yAxisValue, int seriesIndex, Orientation or, int startIndex) {
        XYChart chart = this.getXYChart(seriesIndex);
        chart.setScreenR(this.getScreenR());
        chart.setCalcRange(this.getCalcRange(this.mDataset.getSeriesAt(seriesIndex).getScaleNumber()), 0);
        chart.drawSeries(series, canvas, paint, pointsList, seriesRenderer, yAxisValue, this.getChartSeriesIndex(seriesIndex), or, startIndex);
    }

    public int getLegendShapeWidth(int seriesIndex) {
        XYChart chart = this.getXYChart(seriesIndex);
        return chart.getLegendShapeWidth(this.getChartSeriesIndex(seriesIndex));
    }

    public void drawLegendShape(Canvas canvas, SimpleSeriesRenderer renderer, float x, float y, int seriesIndex, Paint paint) {
        XYChart chart = this.getXYChart(seriesIndex);
        chart.drawLegendShape(canvas, renderer, x, y, this.getChartSeriesIndex(seriesIndex), paint);
    }

    public String getChartType() {
        return "Combined";
    }

    private XYChart getXYChart(int seriesIndex) {
        for(int i = 0; i < this.chartDefinitions.length; ++i) {
            if(this.chartDefinitions[i].containsSeries(seriesIndex)) {
                return this.mCharts[i];
            }
        }

        throw new IllegalArgumentException("Unknown series with index " + seriesIndex);
    }

    private int getChartSeriesIndex(int seriesIndex) {
        for(int i = 0; i < this.chartDefinitions.length; ++i) {
            if(this.chartDefinitions[i].containsSeries(seriesIndex)) {
                return this.chartDefinitions[i].getChartSeriesIndex(seriesIndex);
            }
        }

        throw new IllegalArgumentException("Unknown series with index " + seriesIndex);
    }

    public static class XYCombinedChartDef implements Serializable {
        private String type;
        private int[] seriesIndex;

        public XYCombinedChartDef(String type, int... seriesIndex) {
            this.type = type;
            this.seriesIndex = seriesIndex;
        }

        public boolean containsSeries(int seriesIndex) {
            return this.getChartSeriesIndex(seriesIndex) >= 0;
        }

        public int getChartSeriesIndex(int seriesIndex) {
            for(int i = 0; i < this.getSeriesIndex().length; ++i) {
                if(this.seriesIndex[i] == seriesIndex) {
                    return i;
                }
            }

            return -1;
        }

        public String getType() {
            return this.type;
        }

        public int[] getSeriesIndex() {
            return this.seriesIndex;
        }
    }
}
