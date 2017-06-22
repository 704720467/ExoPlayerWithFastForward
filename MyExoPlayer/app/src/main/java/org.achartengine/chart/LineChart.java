//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.achartengine.chart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;

import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer.FillOutsideLine;
import org.achartengine.renderer.XYSeriesRenderer.FillOutsideLine.Type;

import java.util.ArrayList;
import java.util.List;

public class LineChart extends XYChart {
    public static final String TYPE = "Line";
    private static final int SHAPE_WIDTH = 30;
    private ScatterChart pointsChart;

    LineChart() {
    }

    public LineChart(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer) {
        super(dataset, renderer);
        this.pointsChart = new ScatterChart(dataset, renderer);
    }

    protected void setDatasetRenderer(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer) {
        super.setDatasetRenderer(dataset, renderer);
        this.pointsChart = new ScatterChart(dataset, renderer);
    }

    public void drawSeries(Canvas canvas, Paint paint, List<Float> points, XYSeriesRenderer renderer, float yAxisValue, int seriesIndex, int startIndex) {
        float lineWidth = paint.getStrokeWidth();
        paint.setStrokeWidth(renderer.getLineWidth());
        FillOutsideLine[] fillOutsideLine = renderer.getFillOutsideLine();
        FillOutsideLine[] arr$ = fillOutsideLine;
        int len$ = fillOutsideLine.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            FillOutsideLine fill = arr$[i$];
            if (fill.getType() != Type.NONE) {
                paint.setColor(fill.getColor());
                ArrayList fillPoints = new ArrayList();
                int[] range = fill.getFillRange();
                if (range == null) {
                    fillPoints.addAll(points);
                } else if (points.size() > range[0] * 2 && points.size() > range[1] * 2) {
                    fillPoints.addAll(points.subList(range[0] * 2, range[1] * 2));
                }

                float referencePoint;
                switch (fill.getType().ordinal()) {
                    case 1:
                        referencePoint = yAxisValue;
                        break;
                    case 2:
                        referencePoint = yAxisValue;
                        break;
                    case 3:
                        referencePoint = yAxisValue;
                        break;
                    case 4:
                        referencePoint = (float) canvas.getHeight();
                        break;
                    case 5:
                        referencePoint = 0.0F;
                        break;
                    default:
                        throw new RuntimeException("You have added a new type of filling but have not implemented.");
                }

                if (fill.getType() == Type.BOUNDS_ABOVE || fill.getType() == Type.BOUNDS_BELOW) {
                    ArrayList length = new ArrayList();
                    boolean i = false;
                    int length1 = fillPoints.size();
                    if (length1 > 0 && fill.getType() == Type.BOUNDS_ABOVE && ((Float) fillPoints.get(1)).floatValue() < referencePoint || fill.getType() == Type.BOUNDS_BELOW && ((Float) fillPoints.get(1)).floatValue() > referencePoint) {
                        length.add(fillPoints.get(0));
                        length.add(fillPoints.get(1));
                        i = true;
                    }

                    for (int i1 = 3; i1 < length1; i1 += 2) {
                        float prevValue = ((Float) fillPoints.get(i1 - 2)).floatValue();
                        float value = ((Float) fillPoints.get(i1)).floatValue();
                        if (prevValue < referencePoint && value > referencePoint || prevValue > referencePoint && value < referencePoint) {
                            float prevX = ((Float) fillPoints.get(i1 - 3)).floatValue();
                            float x = ((Float) fillPoints.get(i1 - 1)).floatValue();
                            length.add(Float.valueOf(prevX + (x - prevX) * (referencePoint - prevValue) / (value - prevValue)));
                            length.add(Float.valueOf(referencePoint));
                            if ((fill.getType() != Type.BOUNDS_ABOVE || value <= referencePoint) && (fill.getType() != Type.BOUNDS_BELOW || value >= referencePoint)) {
                                length.add(Float.valueOf(x));
                                length.add(Float.valueOf(value));
                                i = true;
                            } else {
                                i1 += 2;
                                i = false;
                            }
                        } else if (i || fill.getType() == Type.BOUNDS_ABOVE && value < referencePoint || fill.getType() == Type.BOUNDS_BELOW && value > referencePoint) {
                            length.add(fillPoints.get(i1 - 1));
                            length.add(Float.valueOf(value));
                        }
                    }

                    fillPoints.clear();
                    fillPoints.addAll(length);
                }

                int var25 = fillPoints.size();
                if (var25 > 0) {
                    fillPoints.set(0, Float.valueOf(((Float) fillPoints.get(0)).floatValue() + 1.0F));
                    fillPoints.add(fillPoints.get(var25 - 2));
                    fillPoints.add(Float.valueOf(referencePoint));
                    fillPoints.add(fillPoints.get(0));
                    fillPoints.add(fillPoints.get(var25 + 1));

                    for (int var26 = 0; var26 < var25 + 4; var26 += 2) {
                        if (((Float) fillPoints.get(var26 + 1)).floatValue() < 0.0F) {
                            fillPoints.set(var26 + 1, Float.valueOf(0.0F));
                        }
                    }

                    paint.setStyle(Style.FILL);
                    this.drawPath(canvas, fillPoints, paint, true);
                }
            }
        }

        paint.setColor(renderer.getColor());
        paint.setStyle(Style.STROKE);
        this.drawPath(canvas, points, paint, false);
        paint.setStrokeWidth(lineWidth);
    }

    protected ClickableArea[] clickableAreasForPoints(List<Float> points, List<Double> values, float yAxisValue, int seriesIndex, int startIndex) {
        int length = points.size();
        ClickableArea[] ret = new ClickableArea[length / 2];

        for (int i = 0; i < length; i += 2) {
            int selectableBuffer = this.mRenderer.getSelectableBuffer();
            ret[i / 2] = new ClickableArea(new RectF(((Float) points.get(i)).floatValue() - (float) selectableBuffer, ((Float) points.get(i + 1)).floatValue() - (float) selectableBuffer, ((Float) points.get(i)).floatValue() + (float) selectableBuffer, ((Float) points.get(i + 1)).floatValue() + (float) selectableBuffer), ((Double) values.get(i)).doubleValue(), ((Double) values.get(i + 1)).doubleValue());
        }

        return ret;
    }

    public int getLegendShapeWidth(int seriesIndex) {
        return 30;
    }

    public void drawLegendShape(Canvas canvas, SimpleSeriesRenderer renderer, float x, float y, int seriesIndex, Paint paint) {
        float oldWidth = paint.getStrokeWidth();
        paint.setStrokeWidth(((XYSeriesRenderer) renderer).getLineWidth());
        canvas.drawLine(x, y, x + 30.0F, y, paint);
        paint.setStrokeWidth(oldWidth);
        if (this.isRenderPoints(renderer)) {
            this.pointsChart.drawLegendShape(canvas, renderer, x + 5.0F, y, seriesIndex, paint);
        }

    }

    public boolean isRenderPoints(SimpleSeriesRenderer renderer) {
        return ((XYSeriesRenderer) renderer).getPointStyle() != PointStyle.POINT;
    }

    public ScatterChart getPointsChart() {
        return this.pointsChart;
    }

    public String getChartType() {
        return "Line";
    }
}
