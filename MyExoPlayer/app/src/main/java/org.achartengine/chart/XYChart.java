//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.achartengine.chart;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.Map.Entry;
import org.achartengine.chart.AbstractChart;
import org.achartengine.chart.ClickableArea;
import org.achartengine.chart.ScatterChart;
import org.achartengine.model.Point;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.BasicStroke;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer.Orientation;
import org.achartengine.util.MathHelper;

public abstract class XYChart extends AbstractChart {
    protected XYMultipleSeriesDataset mDataset;
    protected XYMultipleSeriesRenderer mRenderer;
    private float mScale;
    private float mTranslate;
    private Point mCenter;
    private Rect mScreenR;
    private final Map<Integer, double[]> mCalcRange = new HashMap();
    protected transient Paint mGridPaint;
    private Map<Integer, List<ClickableArea>> clickableAreas = new HashMap();

    protected XYChart() {
    }

    public XYChart(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer) {
        this.mDataset = dataset;
        this.mRenderer = renderer;
    }

    protected void setDatasetRenderer(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer) {
        this.mDataset = dataset;
        this.mRenderer = renderer;
    }

    public void draw(Canvas canvas, int x, int y, int width, int height, Paint paint) {
        paint.setAntiAlias(this.mRenderer.isAntialiasing());
        int legendSize = this.getLegendSize(this.mRenderer, height / 5, this.mRenderer.getAxisTitleTextSize());
        int[] margins = this.mRenderer.getMargins();
        int left = x + margins[1];
        int top = y + margins[0];
        int right = x + width - margins[3];
        int sLength = this.mDataset.getSeriesCount();
        String[] titles = new String[sLength];

        int bottom;
        for(bottom = 0; bottom < sLength; ++bottom) {
            titles[bottom] = this.mDataset.getSeriesAt(bottom).getTitle();
        }

        if(this.mRenderer.isFitLegend() && this.mRenderer.isShowLegend()) {
            legendSize = this.drawLegend(canvas, this.mRenderer, titles, left, right, y, width, height, legendSize, paint, true);
        }

        bottom = y + height - margins[2] - legendSize;
        if(this.mScreenR == null) {
            this.mScreenR = new Rect();
        }

        this.mScreenR.set(left, top, right, bottom);
        this.drawBackground(this.mRenderer, canvas, x, y, width, height, paint, false, 0);
        if(paint.getTypeface() == null || this.mRenderer.getTextTypeface() != null && paint.getTypeface().equals(this.mRenderer.getTextTypeface()) || !paint.getTypeface().toString().equals(this.mRenderer.getTextTypefaceName()) || paint.getTypeface().getStyle() != this.mRenderer.getTextTypefaceStyle()) {
            if(this.mRenderer.getTextTypeface() != null) {
                paint.setTypeface(this.mRenderer.getTextTypeface());
            } else {
                paint.setTypeface(Typeface.create(this.mRenderer.getTextTypefaceName(), this.mRenderer.getTextTypefaceStyle()));
            }
        }

        Orientation or = this.mRenderer.getOrientation();
        if(or == Orientation.VERTICAL) {
            right -= legendSize;
            bottom += legendSize - 20;
        }

        int angle = or.getAngle();
        boolean rotate = angle == 90;
        this.mScale = (float)height / (float)width;
        this.mTranslate = (float)(Math.abs(width - height) / 2);
        if(this.mScale < 1.0F) {
            this.mTranslate *= -1.0F;
        }

        this.mCenter = new Point((float)((x + width) / 2), (float)((y + height) / 2));
        if(rotate) {
            this.transform(canvas, (float)angle, false);
        }

        int maxScaleNumber = -2147483647;

        for(int minX = 0; minX < sLength; ++minX) {
            maxScaleNumber = Math.max(maxScaleNumber, this.mDataset.getSeriesAt(minX).getScaleNumber());
        }

        ++maxScaleNumber;
        if(maxScaleNumber >= 0) {
            double[] var53 = new double[maxScaleNumber];
            double[] maxX = new double[maxScaleNumber];
            double[] minY = new double[maxScaleNumber];
            double[] maxY = new double[maxScaleNumber];
            boolean[] isMinXSet = new boolean[maxScaleNumber];
            boolean[] isMaxXSet = new boolean[maxScaleNumber];
            boolean[] isMinYSet = new boolean[maxScaleNumber];
            boolean[] isMaxYSet = new boolean[maxScaleNumber];

            for(int xPixelsPerUnit = 0; xPixelsPerUnit < maxScaleNumber; ++xPixelsPerUnit) {
                var53[xPixelsPerUnit] = this.mRenderer.getXAxisMin(xPixelsPerUnit);
                maxX[xPixelsPerUnit] = this.mRenderer.getXAxisMax(xPixelsPerUnit);
                minY[xPixelsPerUnit] = this.mRenderer.getYAxisMin(xPixelsPerUnit);
                maxY[xPixelsPerUnit] = this.mRenderer.getYAxisMax(xPixelsPerUnit);
                isMinXSet[xPixelsPerUnit] = this.mRenderer.isMinXSet(xPixelsPerUnit);
                isMaxXSet[xPixelsPerUnit] = this.mRenderer.isMaxXSet(xPixelsPerUnit);
                isMinYSet[xPixelsPerUnit] = this.mRenderer.isMinYSet(xPixelsPerUnit);
                isMaxYSet[xPixelsPerUnit] = this.mRenderer.isMaxYSet(xPixelsPerUnit);
                if(this.mCalcRange.get(Integer.valueOf(xPixelsPerUnit)) == null) {
                    this.mCalcRange.put(Integer.valueOf(xPixelsPerUnit), new double[4]);
                }
            }

            double[] var54 = new double[maxScaleNumber];
            double[] yPixelsPerUnit = new double[maxScaleNumber];

            int hasValues;
            for(hasValues = 0; hasValues < sLength; ++hasValues) {
                XYSeries showLabels = this.mDataset.getSeriesAt(hasValues);
                int showGridX = showLabels.getScaleNumber();
                if(showLabels.getItemCount() != 0) {
                    double showGridY;
                    if(!isMinXSet[showGridX]) {
                        showGridY = showLabels.getMinX();
                        var53[showGridX] = Math.min(var53[showGridX], showGridY);
                        ((double[])this.mCalcRange.get(Integer.valueOf(showGridX)))[0] = var53[showGridX];
                    }

                    if(!isMaxXSet[showGridX]) {
                        showGridY = showLabels.getMaxX();
                        maxX[showGridX] = Math.max(maxX[showGridX], showGridY);
                        ((double[])this.mCalcRange.get(Integer.valueOf(showGridX)))[1] = maxX[showGridX];
                    }

                    if(!isMinYSet[showGridX]) {
                        showGridY = showLabels.getMinY();
                        minY[showGridX] = Math.min(minY[showGridX], (double)((float)showGridY));
                        ((double[])this.mCalcRange.get(Integer.valueOf(showGridX)))[2] = minY[showGridX];
                    }

                    if(!isMaxYSet[showGridX]) {
                        showGridY = showLabels.getMaxY();
                        maxY[showGridX] = Math.max(maxY[showGridX], (double)((float)showGridY));
                        ((double[])this.mCalcRange.get(Integer.valueOf(showGridX)))[3] = maxY[showGridX];
                    }
                }
            }

            for(hasValues = 0; hasValues < maxScaleNumber; ++hasValues) {
                if(maxX[hasValues] - var53[hasValues] != 0.0D) {
                    var54[hasValues] = (double)(right - left) / (maxX[hasValues] - var53[hasValues]);
                }

                if(maxY[hasValues] - minY[hasValues] != 0.0D) {
                    yPixelsPerUnit[hasValues] = (double)((float)((double)(bottom - top) / (maxY[hasValues] - minY[hasValues])));
                }

                if(hasValues > 0) {
                    var54[hasValues] = var54[0];
                    var53[hasValues] = var53[0];
                    maxX[hasValues] = maxX[0];
                }
            }

            boolean var55 = false;

            for(int var56 = 0; var56 < sLength; ++var56) {
                XYSeries var58 = this.mDataset.getSeriesAt(var56);
                if(var58.getItemCount() != 0) {
                    var55 = true;
                }
            }

            boolean var57 = this.mRenderer.isShowLabels() && var55;
            boolean var59 = this.mRenderer.isShowGridX();
            boolean var60 = this.mRenderer.isShowGridY();
            if(var59 || var60) {
                List showTickMarks = this.getValidLabels(this.getXLabels(var53[0], maxX[0], this.mRenderer.getXLabels()));
                Map showCustomTextGridY = this.getYLabels(minY, maxY, maxScaleNumber);
                boolean i = this.mRenderer.isShowXLabels();
                boolean xLabelsLeft = this.mRenderer.isShowYLabels();
                this.mRenderer.setShowLabels(false);
                if(this.mGridPaint == null) {
                    this.mGridPaint = new Paint(1);
                }

                this.drawXLabels(showTickMarks, this.mRenderer.getXTextLabelLocations(), canvas, paint, left, top, bottom, var54[0], var53[0], maxX[0]);
                this.drawYLabels(showCustomTextGridY, canvas, paint, maxScaleNumber, left, right, bottom, yPixelsPerUnit, minY);
                this.mRenderer.setShowLabels(i, xLabelsLeft);
            }

            this.clickableAreas = new HashMap();

            int i$;
            for(int var61 = 0; var61 < sLength; ++var61) {
                XYSeries var63 = this.mDataset.getSeriesAt(var61);
                int rightAxis = var63.getScaleNumber();
                if(var63.getItemCount() != 0) {
                    XYSeriesRenderer var67 = (XYSeriesRenderer)this.mRenderer.getSeriesRendererAt(var61);
                    ArrayList var70 = new ArrayList();
                    ArrayList size = new ArrayList();
                    float i1 = Math.min((float)bottom, (float)((double)bottom + yPixelsPerUnit[rightAxis] * minY[rightAxis]));
                    LinkedList axisAlign = new LinkedList();
                    this.clickableAreas.put(Integer.valueOf(var61), axisAlign);
                    synchronized(var63) {
                        SortedMap len$ = var63.getRange(var53[rightAxis], maxX[rightAxis], var67.isDisplayBoundingPoints());
                        i$ = -1;
                        Iterator location = len$.entrySet().iterator();

                        while(location.hasNext()) {
                            Entry yLabel = (Entry)location.next();
                            double label = ((Double)yLabel.getKey()).doubleValue();
                            double yS = ((Double)yLabel.getValue()).doubleValue();
                            if(i$ < 0 && (!this.isNullValue(yS) || this.isRenderNullValues())) {
                                i$ = var63.getIndexForKey(label);
                            }

                            size.add(yLabel.getKey());
                            size.add(yLabel.getValue());
                            if(!this.isNullValue(yS)) {
                                var70.add(Float.valueOf((float)((double)left + var54[rightAxis] * (label - var53[rightAxis]))));
                                var70.add(Float.valueOf((float)((double)bottom - yPixelsPerUnit[rightAxis] * (yS - minY[rightAxis]))));
                            } else if(this.isRenderNullValues()) {
                                var70.add(Float.valueOf((float)((double)left + var54[rightAxis] * (label - var53[rightAxis]))));
                                var70.add(Float.valueOf((float)((double)bottom - yPixelsPerUnit[rightAxis] * -minY[rightAxis])));
                            } else {
                                if(var70.size() > 0) {
                                    this.drawSeries(var63, canvas, paint, var70, var67, i1, var61, or, i$);
                                    ClickableArea[] clickableAreasForSubSeries = this.clickableAreasForPoints(var70, size, i1, var61, i$);
                                    axisAlign.addAll(Arrays.asList(clickableAreasForSubSeries));
                                    var70.clear();
                                    size.clear();
                                    i$ = -1;
                                }

                                axisAlign.add((Object)null);
                            }
                        }

                        int var78 = var63.getAnnotationCount();
                        if(var78 > 0) {
                            paint.setColor(var67.getAnnotationsColor());
                            paint.setTextSize(var67.getAnnotationsTextSize());
                            paint.setTextAlign(var67.getAnnotationsTextAlign());
                            Rect var80 = new Rect();

                            for(int var83 = 0; var83 < var78; ++var83) {
                                float xS = (float)((double)left + var54[rightAxis] * (var63.getAnnotationX(var83) - var53[rightAxis]));
                                float var85 = (float)((double)bottom - yPixelsPerUnit[rightAxis] * (var63.getAnnotationY(var83) - minY[rightAxis]));
                                paint.getTextBounds(var63.getAnnotationAt(var83), 0, var63.getAnnotationAt(var83).length(), var80);
                                if(xS < xS + (float)var80.width() && var85 < (float)canvas.getHeight()) {
                                    this.drawString(canvas, var63.getAnnotationAt(var83), xS, var85, paint);
                                }
                            }
                        }

                        if(var70.size() > 0) {
                            this.drawSeries(var63, canvas, paint, var70, var67, i1, var61, or, i$);
                            ClickableArea[] var81 = this.clickableAreasForPoints(var70, size, i1, var61, i$);
                            axisAlign.addAll(Arrays.asList(var81));
                        }
                    }
                }
            }

            this.drawBackground(this.mRenderer, canvas, x, bottom, width, height - bottom, paint, true, this.mRenderer.getMarginsColor());
            this.drawBackground(this.mRenderer, canvas, x, y, width, margins[0], paint, true, this.mRenderer.getMarginsColor());
            if(or == Orientation.HORIZONTAL) {
                this.drawBackground(this.mRenderer, canvas, x, y, left - x, height - y, paint, true, this.mRenderer.getMarginsColor());
                this.drawBackground(this.mRenderer, canvas, right, y, margins[3], height - y, paint, true, this.mRenderer.getMarginsColor());
            } else if(or == Orientation.VERTICAL) {
                this.drawBackground(this.mRenderer, canvas, right, y, width - right, height - y, paint, true, this.mRenderer.getMarginsColor());
                this.drawBackground(this.mRenderer, canvas, x, y, left - x, height - y, paint, true, this.mRenderer.getMarginsColor());
            }

            boolean var62 = this.mRenderer.isShowTickMarks();
            boolean var64 = this.mRenderer.isShowCustomTextGridY();
            if(var57) {
                List var65 = this.getValidLabels(this.getXLabels(var53[0], maxX[0], this.mRenderer.getXLabels()));
                Map var68 = this.getYLabels(minY, maxY, maxScaleNumber);
                if(var57) {
                    paint.setColor(this.mRenderer.getXLabelsColor());
                    paint.setTextSize(this.mRenderer.getLabelsTextSize());
                    paint.setTextAlign(this.mRenderer.getXLabelsAlign());
                }

                this.mRenderer.setShowGrid(false);
                this.drawXLabels(var65, this.mRenderer.getXTextLabelLocations(), canvas, paint, left, top, bottom, var54[0], var53[0], maxX[0]);
                this.drawYLabels(var68, canvas, paint, maxScaleNumber, left, right, bottom, yPixelsPerUnit, minY);
                this.mRenderer.setShowGridX(var59);
                this.mRenderer.setShowGridY(var60);
                if(var57) {
                    paint.setColor(this.mRenderer.getLabelsColor());

                    for(int var71 = 0; var71 < maxScaleNumber; ++var71) {
                        Align var73 = this.mRenderer.getYAxisAlign(var71);
                        Double[] var75 = this.mRenderer.getYTextLabelLocations(var71);
                        Double[] arr$ = var75;
                        int var77 = var75.length;

                        for(i$ = 0; i$ < var77; ++i$) {
                            Double var79 = arr$[i$];
                            if(minY[var71] <= var79.doubleValue() && var79.doubleValue() <= maxY[var71]) {
                                float var82 = (float)((double)bottom - yPixelsPerUnit[var71] * (var79.doubleValue() - minY[var71]));
                                String var84 = this.mRenderer.getYTextLabel(var79, var71);
                                paint.setColor(this.mRenderer.getYLabelsColor(var71));
                                paint.setTextAlign(this.mRenderer.getYLabelsAlign(var71));
                                if(or == Orientation.HORIZONTAL) {
                                    if(var73 == Align.LEFT) {
                                        if(var62) {
                                            canvas.drawLine((float)(left + this.getLabelLinePos(var73)), var82, (float)left, var82, paint);
                                        }

                                        this.drawText(canvas, var84, (float)left - this.mRenderer.getYLabelsPadding(), var82 - this.mRenderer.getYLabelsVerticalPadding(), paint, this.mRenderer.getYLabelsAngle());
                                    } else {
                                        if(var62) {
                                            canvas.drawLine((float)right, var82, (float)(right + this.getLabelLinePos(var73)), var82, paint);
                                        }

                                        this.drawText(canvas, var84, (float)right - this.mRenderer.getYLabelsPadding(), var82 - this.mRenderer.getYLabelsVerticalPadding(), paint, this.mRenderer.getYLabelsAngle());
                                    }

                                    if(var64) {
                                        paint.setColor(this.mRenderer.getGridColor(var71));
                                        canvas.drawLine((float)left, var82, (float)right, var82, paint);
                                    }
                                } else {
                                    if(var62) {
                                        canvas.drawLine((float)(right - this.getLabelLinePos(var73)), var82, (float)right, var82, paint);
                                    }

                                    this.drawText(canvas, var84, (float)(right + 10), var82 - this.mRenderer.getYLabelsVerticalPadding(), paint, this.mRenderer.getYLabelsAngle());
                                    if(var64) {
                                        paint.setColor(this.mRenderer.getGridColor(var71));
                                        canvas.drawLine((float)right, var82, (float)left, var82, paint);
                                    }
                                }
                            }
                        }
                    }
                }

                if(var57) {
                    paint.setColor(this.mRenderer.getLabelsColor());
                    float var72 = this.mRenderer.getAxisTitleTextSize();
                    paint.setTextSize(var72);
                    paint.setTextAlign(Align.CENTER);
                    if(or == Orientation.HORIZONTAL) {
                        this.drawText(canvas, this.mRenderer.getXTitle(), (float)(x + width / 2), (float)bottom + this.mRenderer.getLabelsTextSize() * 4.0F / 3.0F + this.mRenderer.getXLabelsPadding() + var72, paint, 0.0F);

                        for(int var74 = 0; var74 < maxScaleNumber; ++var74) {
                            Align var76 = this.mRenderer.getYAxisAlign(var74);
                            if(var76 == Align.LEFT) {
                                this.drawText(canvas, this.mRenderer.getYTitle(var74), (float)x + var72, (float)(y + height / 2), paint, -90.0F);
                            } else {
                                this.drawText(canvas, this.mRenderer.getYTitle(var74), (float)(x + width), (float)(y + height / 2), paint, -90.0F);
                            }
                        }

                        paint.setTextSize(this.mRenderer.getChartTitleTextSize());
                        this.drawText(canvas, this.mRenderer.getChartTitle(), (float)(x + width / 2), (float)y + this.mRenderer.getChartTitleTextSize(), paint, 0.0F);
                    } else if(or == Orientation.VERTICAL) {
                        this.drawText(canvas, this.mRenderer.getXTitle(), (float)(x + width / 2), (float)(y + height) - var72 + this.mRenderer.getXLabelsPadding(), paint, -90.0F);
                        this.drawText(canvas, this.mRenderer.getYTitle(), (float)(right + 20), (float)(y + height / 2), paint, 0.0F);
                        paint.setTextSize(this.mRenderer.getChartTitleTextSize());
                        this.drawText(canvas, this.mRenderer.getChartTitle(), (float)x + var72, (float)(top + height / 2), paint, 0.0F);
                    }
                }
            }

            if(or == Orientation.HORIZONTAL) {
                this.drawLegend(canvas, this.mRenderer, titles, left, right, y + (int)this.mRenderer.getXLabelsPadding(), width, height, legendSize, paint, false);
            } else if(or == Orientation.VERTICAL) {
                this.transform(canvas, (float)angle, true);
                this.drawLegend(canvas, this.mRenderer, titles, left, right, y + (int)this.mRenderer.getXLabelsPadding(), width, height, legendSize, paint, false);
                this.transform(canvas, (float)angle, false);
            }

            if(this.mRenderer.isShowAxes()) {
                paint.setColor(this.mRenderer.getXAxisColor());
                canvas.drawLine((float)left, (float)bottom, (float)right, (float)bottom, paint);
                paint.setColor(this.mRenderer.getYAxisColor());
                boolean var66 = false;

                for(int var69 = 0; var69 < maxScaleNumber && !var66; ++var69) {
                    var66 = this.mRenderer.getYAxisAlign(var69) == Align.RIGHT;
                }

                if(or == Orientation.HORIZONTAL) {
                    canvas.drawLine((float)left, (float)top, (float)left, (float)bottom, paint);
                    if(var66) {
                        canvas.drawLine((float)right, (float)top, (float)right, (float)bottom, paint);
                    }
                } else if(or == Orientation.VERTICAL) {
                    canvas.drawLine((float)right, (float)top, (float)right, (float)bottom, paint);
                }
            }

            if(rotate) {
                this.transform(canvas, (float)angle, true);
            }

        }
    }

    protected List<Double> getXLabels(double min, double max, int count) {
        return MathHelper.getLabels(min, max, count);
    }

    protected Map<Integer, List<Double>> getYLabels(double[] minY, double[] maxY, int maxScaleNumber) {
        HashMap allYLabels = new HashMap();

        for(int i = 0; i < maxScaleNumber; ++i) {
            allYLabels.put(Integer.valueOf(i), this.getValidLabels(MathHelper.getLabels(minY[i], maxY[i], this.mRenderer.getYLabels())));
        }

        return allYLabels;
    }

    protected Rect getScreenR() {
        return this.mScreenR;
    }

    protected void setScreenR(Rect screenR) {
        this.mScreenR = screenR;
    }

    private List<Double> getValidLabels(List<Double> labels) {
        ArrayList result = new ArrayList(labels);
        Iterator i$ = labels.iterator();

        while(i$.hasNext()) {
            Double label = (Double)i$.next();
            if(label.isNaN()) {
                result.remove(label);
            }
        }

        return result;
    }

    protected void drawSeries(XYSeries series, Canvas canvas, Paint paint, List<Float> pointsList, XYSeriesRenderer seriesRenderer, float yAxisValue, int seriesIndex, Orientation or, int startIndex) {
        BasicStroke stroke = seriesRenderer.getStroke();
        Cap cap = paint.getStrokeCap();
        Join join = paint.getStrokeJoin();
        float miter = paint.getStrokeMiter();
        PathEffect pathEffect = paint.getPathEffect();
        Style style = paint.getStyle();
        if(stroke != null) {
            DashPathEffect effect = null;
            if(stroke.getIntervals() != null) {
                effect = new DashPathEffect(stroke.getIntervals(), stroke.getPhase());
            }

            this.setStroke(stroke.getCap(), stroke.getJoin(), stroke.getMiter(), Style.FILL_AND_STROKE, effect, paint);
        }

        this.drawSeries(canvas, paint, pointsList, seriesRenderer, yAxisValue, seriesIndex, startIndex);
        this.drawPoints(canvas, paint, pointsList, seriesRenderer, yAxisValue, seriesIndex, startIndex);
        paint.setTextSize(seriesRenderer.getChartValuesTextSize());
        if(or == Orientation.HORIZONTAL) {
            paint.setTextAlign(Align.CENTER);
        } else {
            paint.setTextAlign(Align.LEFT);
        }

        if(seriesRenderer.isDisplayChartValues()) {
            paint.setTextAlign(seriesRenderer.getChartValuesTextAlign());
            this.drawChartValuesText(canvas, series, seriesRenderer, paint, pointsList, seriesIndex, startIndex);
        }

        if(stroke != null) {
            this.setStroke(cap, join, miter, style, pathEffect, paint);
        }

    }

    protected void drawPoints(Canvas canvas, Paint paint, List<Float> pointsList, XYSeriesRenderer seriesRenderer, float yAxisValue, int seriesIndex, int startIndex) {
        if(this.isRenderPoints(seriesRenderer)) {
            ScatterChart pointsChart = this.getPointsChart();
            if(pointsChart != null) {
                pointsChart.drawSeries(canvas, paint, pointsList, seriesRenderer, yAxisValue, seriesIndex, startIndex);
            }
        }

    }

    private void setStroke(Cap cap, Join join, float miter, Style style, PathEffect pathEffect, Paint paint) {
        paint.setStrokeCap(cap);
        paint.setStrokeJoin(join);
        paint.setStrokeMiter(miter);
        paint.setPathEffect(pathEffect);
        paint.setStyle(style);
    }

    protected void drawChartValuesText(Canvas canvas, XYSeries series, XYSeriesRenderer renderer, Paint paint, List<Float> points, int seriesIndex, int startIndex) {
        if(points.size() > 2) {
            float k = ((Float)points.get(0)).floatValue();
            float previousPointY = ((Float)points.get(1)).floatValue();

            for(int k1 = 0; k1 < points.size(); k1 += 2) {
                if(k1 == 2) {
                    if(Math.abs(((Float)points.get(2)).floatValue() - ((Float)points.get(0)).floatValue()) > (float)renderer.getDisplayChartValuesDistance() || Math.abs(((Float)points.get(3)).floatValue() - ((Float)points.get(1)).floatValue()) > (float)renderer.getDisplayChartValuesDistance()) {
                        this.drawText(canvas, this.getLabel(renderer.getChartValuesFormat(), series.getY(startIndex)), ((Float)points.get(0)).floatValue(), ((Float)points.get(1)).floatValue() - renderer.getChartValuesSpacing(), paint, 0.0F);
                        this.drawText(canvas, this.getLabel(renderer.getChartValuesFormat(), series.getY(startIndex + 1)), ((Float)points.get(2)).floatValue(), ((Float)points.get(3)).floatValue() - renderer.getChartValuesSpacing(), paint, 0.0F);
                        k = ((Float)points.get(2)).floatValue();
                        previousPointY = ((Float)points.get(3)).floatValue();
                    }
                } else if(k1 > 2 && (Math.abs(((Float)points.get(k1)).floatValue() - k) > (float)renderer.getDisplayChartValuesDistance() || Math.abs(((Float)points.get(k1 + 1)).floatValue() - previousPointY) > (float)renderer.getDisplayChartValuesDistance())) {
                    this.drawText(canvas, this.getLabel(renderer.getChartValuesFormat(), series.getY(startIndex + k1 / 2)), ((Float)points.get(k1)).floatValue(), ((Float)points.get(k1 + 1)).floatValue() - renderer.getChartValuesSpacing(), paint, 0.0F);
                    k = ((Float)points.get(k1)).floatValue();
                    previousPointY = ((Float)points.get(k1 + 1)).floatValue();
                }
            }
        } else {
            for(int k2 = 0; k2 < points.size(); k2 += 2) {
                this.drawText(canvas, this.getLabel(renderer.getChartValuesFormat(), series.getY(startIndex + k2 / 2)), ((Float)points.get(k2)).floatValue(), ((Float)points.get(k2 + 1)).floatValue() - renderer.getChartValuesSpacing(), paint, 0.0F);
            }
        }

    }

    protected void drawText(Canvas canvas, String text, float x, float y, Paint paint, float extraAngle) {
        float angle = (float)(-this.mRenderer.getOrientation().getAngle()) + extraAngle;
        if(angle != 0.0F) {
            canvas.rotate(angle, x, y);
        }

        this.drawString(canvas, text, x, y, paint);
        if(angle != 0.0F) {
            canvas.rotate(-angle, x, y);
        }

    }

    private void transform(Canvas canvas, float angle, boolean inverse) {
        if(inverse) {
            canvas.scale(1.0F / this.mScale, this.mScale);
            canvas.translate(this.mTranslate, -this.mTranslate);
            canvas.rotate(-angle, this.mCenter.getX(), this.mCenter.getY());
        } else {
            canvas.rotate(angle, this.mCenter.getX(), this.mCenter.getY());
            canvas.translate(-this.mTranslate, this.mTranslate);
            canvas.scale(this.mScale, 1.0F / this.mScale);
        }

    }

    protected void drawXLabels(List<Double> xLabels, Double[] xTextLabelLocations, Canvas canvas, Paint paint, int left, int top, int bottom, double xPixelsPerUnit, double minX, double maxX) {
        int length = xLabels.size();
        boolean showXLabels = this.mRenderer.isShowXLabels();
        boolean showGridY = this.mRenderer.isShowGridY();
        if(showGridY) {
            this.mGridPaint.setStyle(Style.STROKE);
            this.mGridPaint.setStrokeWidth(this.mRenderer.getGridLineWidth());
        }

        boolean showTickMarks = this.mRenderer.isShowTickMarks();

        for(int i = 0; i < length; ++i) {
            double label = ((Double)xLabels.get(i)).doubleValue();
            float xLabel = (float)((double)left + xPixelsPerUnit * (label - minX));
            if(showXLabels) {
                paint.setColor(this.mRenderer.getXLabelsColor());
                if(showTickMarks) {
                    canvas.drawLine(xLabel, (float)bottom, xLabel, (float)bottom + this.mRenderer.getLabelsTextSize() / 3.0F, paint);
                }

                this.drawText(canvas, this.getLabel(this.mRenderer.getXLabelFormat(), label), xLabel, (float)bottom + this.mRenderer.getLabelsTextSize() * 4.0F / 3.0F + this.mRenderer.getXLabelsPadding(), paint, this.mRenderer.getXLabelsAngle());
            }

            if(showGridY) {
                this.mGridPaint.setColor(this.mRenderer.getGridColor(0));
                canvas.drawLine(xLabel, (float)bottom, xLabel, (float)top, this.mGridPaint);
            }
        }

        this.drawXTextLabels(xTextLabelLocations, canvas, paint, showXLabels, left, top, bottom, xPixelsPerUnit, minX, maxX);
    }

    protected void drawYLabels(Map<Integer, List<Double>> allYLabels, Canvas canvas, Paint paint, int maxScaleNumber, int left, int right, int bottom, double[] yPixelsPerUnit, double[] minY) {
        Orientation or = this.mRenderer.getOrientation();
        boolean showGridX = this.mRenderer.isShowGridX();
        if(showGridX) {
            this.mGridPaint.setStyle(Style.STROKE);
            this.mGridPaint.setStrokeWidth(this.mRenderer.getGridLineWidth());
        }

        boolean showYLabels = this.mRenderer.isShowYLabels();
        boolean showTickMarks = this.mRenderer.isShowTickMarks();

        for(int i = 0; i < maxScaleNumber; ++i) {
            paint.setTextAlign(this.mRenderer.getYLabelsAlign(i));
            List yLabels = (List)allYLabels.get(Integer.valueOf(i));
            int length = yLabels.size();

            for(int j = 0; j < length; ++j) {
                double label = ((Double)yLabels.get(j)).doubleValue();
                Align axisAlign = this.mRenderer.getYAxisAlign(i);
                boolean textLabel = this.mRenderer.getYTextLabel(Double.valueOf(label), i) != null;
                float yLabel = (float)((double)bottom - yPixelsPerUnit[i] * (label - minY[i]));
                if(or == Orientation.HORIZONTAL) {
                    if(showYLabels && !textLabel) {
                        paint.setColor(this.mRenderer.getYLabelsColor(i));
                        if(axisAlign == Align.LEFT) {
                            if(showTickMarks) {
                                canvas.drawLine((float)(left + this.getLabelLinePos(axisAlign)), yLabel, (float)left, yLabel, paint);
                            }

                            this.drawText(canvas, this.getLabel(this.mRenderer.getYLabelFormat(i), label), (float)left - this.mRenderer.getYLabelsPadding(), yLabel - this.mRenderer.getYLabelsVerticalPadding(), paint, this.mRenderer.getYLabelsAngle());
                        } else {
                            if(showTickMarks) {
                                canvas.drawLine((float)right, yLabel, (float)(right + this.getLabelLinePos(axisAlign)), yLabel, paint);
                            }

                            this.drawText(canvas, this.getLabel(this.mRenderer.getYLabelFormat(i), label), (float)right + this.mRenderer.getYLabelsPadding(), yLabel - this.mRenderer.getYLabelsVerticalPadding(), paint, this.mRenderer.getYLabelsAngle());
                        }
                    }

                    if(showGridX) {
                        this.mGridPaint.setColor(this.mRenderer.getGridColor(i));
                        canvas.drawLine((float)left, yLabel, (float)right, yLabel, this.mGridPaint);
                    }
                } else if(or == Orientation.VERTICAL) {
                    if(showYLabels && !textLabel) {
                        paint.setColor(this.mRenderer.getYLabelsColor(i));
                        if(showTickMarks) {
                            canvas.drawLine((float)(right - this.getLabelLinePos(axisAlign)), yLabel, (float)right, yLabel, paint);
                        }

                        this.drawText(canvas, this.getLabel(this.mRenderer.getLabelFormat(), label), (float)(right + 10) + this.mRenderer.getYLabelsPadding(), yLabel - this.mRenderer.getYLabelsVerticalPadding(), paint, this.mRenderer.getYLabelsAngle());
                    }

                    if(showGridX) {
                        this.mGridPaint.setColor(this.mRenderer.getGridColor(i));
                        if(showTickMarks) {
                            canvas.drawLine((float)right, yLabel, (float)left, yLabel, this.mGridPaint);
                        }
                    }
                }
            }
        }

    }

    protected void drawXTextLabels(Double[] xTextLabelLocations, Canvas canvas, Paint paint, boolean showLabels, int left, int top, int bottom, double xPixelsPerUnit, double minX, double maxX) {
        boolean showCustomTextGridX = this.mRenderer.isShowCustomTextGridX();
        boolean showTickMarks = this.mRenderer.isShowTickMarks();
        if(showLabels) {
            paint.setColor(this.mRenderer.getXLabelsColor());
            Double[] arr$ = xTextLabelLocations;
            int len$ = xTextLabelLocations.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                Double location = arr$[i$];
                if(minX <= location.doubleValue() && location.doubleValue() <= maxX) {
                    float xLabel = (float)((double)left + xPixelsPerUnit * (location.doubleValue() - minX));
                    paint.setColor(this.mRenderer.getXLabelsColor());
                    if(showTickMarks) {
                        canvas.drawLine(xLabel, (float)bottom, xLabel, (float)bottom + this.mRenderer.getLabelsTextSize() / 3.0F, paint);
                    }

                    this.drawText(canvas, this.mRenderer.getXTextLabel(location), xLabel, (float)bottom + this.mRenderer.getLabelsTextSize() * 4.0F / 3.0F + this.mRenderer.getXLabelsPadding(), paint, this.mRenderer.getXLabelsAngle());
                    if(showCustomTextGridX) {
                        paint.setColor(this.mRenderer.getGridColor(0));
                        canvas.drawLine(xLabel, (float)bottom, xLabel, (float)top, paint);
                    }
                }
            }
        }

    }

    public XYMultipleSeriesRenderer getRenderer() {
        return this.mRenderer;
    }

    public XYMultipleSeriesDataset getDataset() {
        return this.mDataset;
    }

    public double[] getCalcRange(int scale) {
        return (double[])this.mCalcRange.get(Integer.valueOf(scale));
    }

    public void setCalcRange(double[] range, int scale) {
        this.mCalcRange.put(Integer.valueOf(scale), range);
    }

    public double[] toRealPoint(float screenX, float screenY) {
        return this.toRealPoint(screenX, screenY, 0);
    }

    public double[] toScreenPoint(double[] realPoint) {
        return this.toScreenPoint(realPoint, 0);
    }

    private int getLabelLinePos(Align align) {
        int pos = 4;
        if(align == Align.LEFT) {
            pos = -pos;
        }

        return pos;
    }

    public double[] toRealPoint(float screenX, float screenY, int scale) {
        double realMinX = this.mRenderer.getXAxisMin(scale);
        double realMaxX = this.mRenderer.getXAxisMax(scale);
        double realMinY = this.mRenderer.getYAxisMin(scale);
        double realMaxY = this.mRenderer.getYAxisMax(scale);
        if(!this.mRenderer.isMinXSet(scale) || !this.mRenderer.isMaxXSet(scale) || !this.mRenderer.isMinYSet(scale) || !this.mRenderer.isMaxYSet(scale)) {
            double[] calcRange = this.getCalcRange(scale);
            if(calcRange != null) {
                realMinX = calcRange[0];
                realMaxX = calcRange[1];
                realMinY = calcRange[2];
                realMaxY = calcRange[3];
            }
        }

        return this.mScreenR != null?new double[]{(double)(screenX - (float)this.mScreenR.left) * (realMaxX - realMinX) / (double)this.mScreenR.width() + realMinX, (double)((float)(this.mScreenR.top + this.mScreenR.height()) - screenY) * (realMaxY - realMinY) / (double)this.mScreenR.height() + realMinY}:new double[]{(double)screenX, (double)screenY};
    }

    public double[] toScreenPoint(double[] realPoint, int scale) {
        double realMinX = this.mRenderer.getXAxisMin(scale);
        double realMaxX = this.mRenderer.getXAxisMax(scale);
        double realMinY = this.mRenderer.getYAxisMin(scale);
        double realMaxY = this.mRenderer.getYAxisMax(scale);
        if(!this.mRenderer.isMinXSet(scale) || !this.mRenderer.isMaxXSet(scale) || !this.mRenderer.isMinYSet(scale) || !this.mRenderer.isMaxYSet(scale)) {
            double[] calcRange = this.getCalcRange(scale);
            realMinX = calcRange[0];
            realMaxX = calcRange[1];
            realMinY = calcRange[2];
            realMaxY = calcRange[3];
        }

        return this.mScreenR != null?new double[]{(realPoint[0] - realMinX) * (double)this.mScreenR.width() / (realMaxX - realMinX) + (double)this.mScreenR.left, (realMaxY - realPoint[1]) * (double)this.mScreenR.height() / (realMaxY - realMinY) + (double)this.mScreenR.top}:realPoint;
    }

    public SeriesSelection getSeriesAndPointForScreenCoordinate(Point screenPoint) {
        if(this.clickableAreas != null) {
            for(int seriesIndex = this.clickableAreas.size() - 1; seriesIndex >= 0; --seriesIndex) {
                int pointIndex = 0;
                if(this.clickableAreas.get(Integer.valueOf(seriesIndex)) != null) {
                    for(Iterator i$ = ((List)this.clickableAreas.get(Integer.valueOf(seriesIndex))).iterator(); i$.hasNext(); ++pointIndex) {
                        ClickableArea area = (ClickableArea)i$.next();
                        if(area != null) {
                            RectF rectangle = area.getRect();
                            if(rectangle != null && rectangle.contains(screenPoint.getX(), screenPoint.getY())) {
                                return new SeriesSelection(seriesIndex, pointIndex, area.getX(), area.getY());
                            }
                        }
                    }
                }
            }
        }

        return super.getSeriesAndPointForScreenCoordinate(screenPoint);
    }

    public abstract void drawSeries(Canvas var1, Paint var2, List<Float> var3, XYSeriesRenderer var4, float var5, int var6, int var7);

    protected abstract ClickableArea[] clickableAreasForPoints(List<Float> var1, List<Double> var2, float var3, int var4, int var5);

    protected boolean isRenderNullValues() {
        return false;
    }

    public boolean isRenderPoints(SimpleSeriesRenderer renderer) {
        return false;
    }

    public double getDefaultMinimum() {
        return 1.7976931348623157E308D;
    }

    public ScatterChart getPointsChart() {
        return null;
    }

    public abstract String getChartType();
}
