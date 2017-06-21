//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.achartengine.chart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import org.achartengine.chart.RoundChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DialRenderer;
import org.achartengine.renderer.DialRenderer.Type;

public class DialChart extends RoundChart {
    private static final int NEEDLE_RADIUS = 10;
    private DialRenderer mRenderer;

    public DialChart(CategorySeries dataset, DialRenderer renderer) {
        super(dataset, renderer);
        this.mRenderer = renderer;
    }

    public void draw(Canvas canvas, int x, int y, int width, int height, Paint paint) {
        paint.setAntiAlias(this.mRenderer.isAntialiasing());
        paint.setStyle(Style.FILL);
        paint.setTextSize(this.mRenderer.getLabelsTextSize());
        int legendSize = this.getLegendSize(this.mRenderer, height / 5, 0.0F);
        int right = x + width;
        int sLength = this.mDataset.getItemCount();
        String[] titles = new String[sLength];

        int bottom;
        for(bottom = 0; bottom < sLength; ++bottom) {
            titles[bottom] = this.mDataset.getCategory(bottom);
        }

        if(this.mRenderer.isFitLegend()) {
            legendSize = this.drawLegend(canvas, this.mRenderer, titles, x, right, y, width, height, legendSize, paint, true);
        }

        bottom = y + height - legendSize;
        this.drawBackground(this.mRenderer, canvas, x, y, width, height, paint, false, 0);
        int mRadius = Math.min(Math.abs(right - x), Math.abs(bottom - y));
        int radius = (int)((double)mRadius * 0.35D * (double)this.mRenderer.getScale());
        if(this.mCenterX == 2147483647) {
            this.mCenterX = (x + right) / 2;
        }

        if(this.mCenterY == 2147483647) {
            this.mCenterY = (bottom + y) / 2;
        }

        float shortRadius = (float)radius * 0.9F;
        float longRadius = (float)radius * 1.1F;
        double min = this.mRenderer.getMinValue();
        double max = this.mRenderer.getMaxValue();
        double angleMin = this.mRenderer.getAngleMin();
        double angleMax = this.mRenderer.getAngleMax();
        double majorTicks;
        if(!this.mRenderer.isMinValueSet() || !this.mRenderer.isMaxValueSet()) {
            int minorTicks = this.mRenderer.getSeriesRendererCount();

            for(int i = 0; i < minorTicks; ++i) {
                majorTicks = this.mDataset.getValue(i);
                if(!this.mRenderer.isMinValueSet()) {
                    min = Math.min(min, majorTicks);
                }

                if(!this.mRenderer.isMaxValueSet()) {
                    max = Math.max(max, majorTicks);
                }
            }
        }

        if(min == max) {
            min *= 0.5D;
            max *= 1.5D;
        }

        paint.setColor(this.mRenderer.getLabelsColor());
        double var35 = this.mRenderer.getMinorTicksSpacing();
        majorTicks = this.mRenderer.getMajorTicksSpacing();
        if(var35 == 1.7976931348623157E308D) {
            var35 = (max - min) / 30.0D;
        }

        if(majorTicks == 1.7976931348623157E308D) {
            majorTicks = (max - min) / 10.0D;
        }

        this.drawTicks(canvas, min, max, angleMin, angleMax, this.mCenterX, this.mCenterY, (double)longRadius, (double)radius, var35, paint, false);
        this.drawTicks(canvas, min, max, angleMin, angleMax, this.mCenterX, this.mCenterY, (double)longRadius, (double)shortRadius, majorTicks, paint, true);
        int count = this.mRenderer.getSeriesRendererCount();

        for(int i1 = 0; i1 < count; ++i1) {
            double angle = this.getAngleForValue(this.mDataset.getValue(i1), angleMin, angleMax, min, max);
            paint.setColor(this.mRenderer.getSeriesRendererAt(i1).getColor());
            boolean type = this.mRenderer.getVisualTypeForIndex(i1) == Type.ARROW;
            this.drawNeedle(canvas, angle, this.mCenterX, this.mCenterY, (double)shortRadius, type, paint);
        }

        this.drawLegend(canvas, this.mRenderer, titles, x, right, y, width, height, legendSize, paint, false);
        this.drawTitle(canvas, x, y, width, paint);
    }

    private double getAngleForValue(double value, double minAngle, double maxAngle, double min, double max) {
        double angleDiff = maxAngle - minAngle;
        double diff = max - min;
        return Math.toRadians(minAngle + (value - min) * angleDiff / diff);
    }

    private void drawTicks(Canvas canvas, double min, double max, double minAngle, double maxAngle, int centerX, int centerY, double longRadius, double shortRadius, double ticks, Paint paint, boolean labels) {
        for(double i = min; i <= max; i += ticks) {
            double angle = this.getAngleForValue(i, minAngle, maxAngle, min, max);
            double sinValue = Math.sin(angle);
            double cosValue = Math.cos(angle);
            int x1 = Math.round((float)centerX + (float)(shortRadius * sinValue));
            int y1 = Math.round((float)centerY + (float)(shortRadius * cosValue));
            int x2 = Math.round((float)centerX + (float)(longRadius * sinValue));
            int y2 = Math.round((float)centerY + (float)(longRadius * cosValue));
            canvas.drawLine((float)x1, (float)y1, (float)x2, (float)y2, paint);
            if(labels) {
                paint.setTextAlign(Align.LEFT);
                if(x1 <= x2) {
                    paint.setTextAlign(Align.RIGHT);
                }

                String text = i + "";
                if(Math.round(i) == (long)i) {
                    text = (long)i + "";
                }

                canvas.drawText(text, (float)x1, (float)y1, paint);
            }
        }

    }

    private void drawNeedle(Canvas canvas, double angle, int centerX, int centerY, double radius, boolean arrow, Paint paint) {
        double diff = Math.toRadians(90.0D);
        int needleSinValue = (int)(10.0D * Math.sin(angle - diff));
        int needleCosValue = (int)(10.0D * Math.cos(angle - diff));
        int needleX = (int)(radius * Math.sin(angle));
        int needleY = (int)(radius * Math.cos(angle));
        int needleCenterX = centerX + needleX;
        int needleCenterY = centerY + needleY;
        float[] points;
        if(arrow) {
            int arrowBaseX = centerX + (int)(radius * 0.85D * Math.sin(angle));
            int arrowBaseY = centerY + (int)(radius * 0.85D * Math.cos(angle));
            points = new float[]{(float)(arrowBaseX - needleSinValue), (float)(arrowBaseY - needleCosValue), (float)needleCenterX, (float)needleCenterY, (float)(arrowBaseX + needleSinValue), (float)(arrowBaseY + needleCosValue)};
            float width = paint.getStrokeWidth();
            paint.setStrokeWidth(5.0F);
            canvas.drawLine((float)centerX, (float)centerY, (float)needleCenterX, (float)needleCenterY, paint);
            paint.setStrokeWidth(width);
        } else {
            points = new float[]{(float)(centerX - needleSinValue), (float)(centerY - needleCosValue), (float)needleCenterX, (float)needleCenterY, (float)(centerX + needleSinValue), (float)(centerY + needleCosValue)};
        }

        this.drawPath(canvas, points, paint, true);
    }
}
