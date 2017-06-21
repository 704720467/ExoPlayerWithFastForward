//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.achartengine.chart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import java.util.List;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.ScatterChart;
import org.achartengine.model.Point;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

public class CubicLineChart extends LineChart {
    public static final String TYPE = "Cubic";
    private float mFirstMultiplier;
    private float mSecondMultiplier;
    private PathMeasure mPathMeasure;

    public CubicLineChart() {
        this.mFirstMultiplier = 0.33F;
        this.mSecondMultiplier = 1.0F - this.mFirstMultiplier;
    }

    public CubicLineChart(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer, float smoothness) {
        super(dataset, renderer);
        this.mFirstMultiplier = smoothness;
        this.mSecondMultiplier = 1.0F - this.mFirstMultiplier;
    }

    protected void drawPath(Canvas canvas, List<Float> points, Paint paint, boolean circular) {
        Path p = new Path();
        float x = ((Float)points.get(0)).floatValue();
        float y = ((Float)points.get(1)).floatValue();
        p.moveTo(x, y);
        int length = points.size();
        if(circular) {
            length -= 4;
        }

        Point p1 = new Point();
        Point p2 = new Point();
        Point p3 = new Point();

        int i;
        for(i = 0; i < length; i += 2) {
            int nextIndex = i + 2 < length?i + 2:i;
            int nextNextIndex = i + 4 < length?i + 4:nextIndex;
            this.calc(points, p1, i, nextIndex, this.mSecondMultiplier);
            p2.setX(((Float)points.get(nextIndex)).floatValue());
            p2.setY(((Float)points.get(nextIndex + 1)).floatValue());
            this.calc(points, p3, nextIndex, nextNextIndex, this.mFirstMultiplier);
            p.cubicTo(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY());
        }

        this.mPathMeasure = new PathMeasure(p, false);
        if(circular) {
            for(i = length; i < length + 4; i += 2) {
                p.lineTo(((Float)points.get(i)).floatValue(), ((Float)points.get(i + 1)).floatValue());
            }

            p.lineTo(((Float)points.get(0)).floatValue(), ((Float)points.get(1)).floatValue());
        }

        canvas.drawPath(p, paint);
    }

    private void calc(List<Float> points, Point result, int index1, int index2, float multiplier) {
        float p1x = ((Float)points.get(index1)).floatValue();
        float p1y = ((Float)points.get(index1 + 1)).floatValue();
        float p2x = ((Float)points.get(index2)).floatValue();
        float p2y = ((Float)points.get(index2 + 1)).floatValue();
        float diffX = p2x - p1x;
        float diffY = p2y - p1y;
        result.setX(p1x + diffX * multiplier);
        result.setY(p1y + diffY * multiplier);
    }

    protected void drawPoints(Canvas canvas, Paint paint, List<Float> pointsList, XYSeriesRenderer seriesRenderer, float yAxisValue, int seriesIndex, int startIndex) {
        if(this.isRenderPoints(seriesRenderer)) {
            ScatterChart pointsChart = this.getPointsChart();
            if(pointsChart != null) {
                int length = (int)this.mPathMeasure.getLength();
                int pointsLength = pointsList.size();
                float[] coords = new float[2];

                for(int i = 0; i < length; ++i) {
                    this.mPathMeasure.getPosTan((float)i, coords, (float[])null);
                    double prevDiff = 1.7976931348623157E308D;
                    boolean ok = true;

                    for(int j = 0; j < pointsLength && ok; j += 2) {
                        double diff = (double)Math.abs(((Float)pointsList.get(j)).floatValue() - coords[0]);
                        if(diff < 1.0D) {
                            pointsList.set(j + 1, Float.valueOf(coords[1]));
                            prevDiff = diff;
                        }

                        ok = prevDiff > diff;
                    }
                }

                pointsChart.drawSeries(canvas, paint, pointsList, seriesRenderer, yAxisValue, seriesIndex, startIndex);
            }
        }

    }

    public String getChartType() {
        return "Cubic";
    }
}
