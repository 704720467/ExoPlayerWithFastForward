//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.achartengine.chart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.List;
import org.achartengine.model.Point;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer.Orientation;

public abstract class AbstractChart implements Serializable {
    public AbstractChart() {
    }

    public abstract void draw(Canvas var1, int var2, int var3, int var4, int var5, Paint var6);

    protected void drawBackground(DefaultRenderer renderer, Canvas canvas, int x, int y, int width, int height, Paint paint, boolean newColor, int color) {
        if(renderer.isApplyBackgroundColor() || newColor) {
            if(newColor) {
                paint.setColor(color);
            } else {
                paint.setColor(renderer.getBackgroundColor());
            }

            paint.setStyle(Style.FILL);
            canvas.drawRect((float)x, (float)y, (float)(x + width), (float)(y + height), paint);
        }

    }

    protected int drawLegend(Canvas canvas, DefaultRenderer renderer, String[] titles, int left, int right, int y, int width, int height, int legendSize, Paint paint, boolean calculate) {
        float size = 32.0F;
        if(renderer.isShowLegend()) {
            float currentX = (float)left;
            float currentY = (float)(y + height - legendSize) + size;
            paint.setTextAlign(Align.LEFT);
            paint.setTextSize(renderer.getLegendTextSize());
            int sLength = Math.min(titles.length, renderer.getSeriesRendererCount());

            for(int i = 0; i < sLength; ++i) {
                SimpleSeriesRenderer r = renderer.getSeriesRendererAt(i);
                float lineSize = (float)this.getLegendShapeWidth(i);
                if(r.isShowLegendItem()) {
                    String text = titles[i];
                    if(titles.length == renderer.getSeriesRendererCount()) {
                        paint.setColor(r.getColor());
                    } else {
                        paint.setColor(-3355444);
                    }

                    float[] widths = new float[text.length()];
                    paint.getTextWidths(text, widths);
                    float sum = 0.0F;
                    float[] extraSize = widths;
                    int currentWidth = widths.length;

                    for(int maxWidth = 0; maxWidth < currentWidth; ++maxWidth) {
                        float nr = extraSize[maxWidth];
                        sum += nr;
                    }

                    float var26 = lineSize + 10.0F + sum;
                    float var27 = currentX + var26;
                    if(i > 0 && this.getExceed(var27, renderer, right, width)) {
                        currentX = (float)left;
                        currentY += renderer.getLegendTextSize();
                        size += renderer.getLegendTextSize();
                        var27 = currentX + var26;
                    }

                    if(this.getExceed(var27, renderer, right, width)) {
                        float var28 = (float)right - currentX - lineSize - 10.0F;
                        if(this.isVertical(renderer)) {
                            var28 = (float)width - currentX - lineSize - 10.0F;
                        }

                        int var29 = paint.breakText(text, true, var28, widths);
                        text = text.substring(0, var29) + "...";
                    }

                    if(!calculate) {
                        this.drawLegendShape(canvas, r, currentX, currentY, i, paint);
                        this.drawString(canvas, text, currentX + lineSize + 5.0F, currentY + 5.0F, paint);
                    }

                    currentX += var26;
                }
            }
        }

        return Math.round(size + renderer.getLegendTextSize());
    }

    protected void drawString(Canvas canvas, String text, float x, float y, Paint paint) {
        if(text != null) {
            String[] lines = text.split("\n");
            Rect rect = new Rect();
            int yOff = 0;

            for(int i = 0; i < lines.length; ++i) {
                canvas.drawText(lines[i], x, y + (float)yOff, paint);
                paint.getTextBounds(lines[i], 0, lines[i].length(), rect);
                yOff = yOff + rect.height() + 5;
            }
        }

    }

    protected boolean getExceed(float currentWidth, DefaultRenderer renderer, int right, int width) {
        boolean exceed = currentWidth > (float)right;
        if(this.isVertical(renderer)) {
            exceed = currentWidth > (float)width;
        }

        return exceed;
    }

    public boolean isVertical(DefaultRenderer renderer) {
        return renderer instanceof XYMultipleSeriesRenderer && ((XYMultipleSeriesRenderer)renderer).getOrientation() == Orientation.VERTICAL;
    }

    protected String getLabel(NumberFormat format, double label) {
        String text = "";
        if(format != null) {
            text = format.format(label);
        } else if(label == (double)Math.round(label)) {
            text = Math.round(label) + "";
        } else {
            text = label + "";
        }

        return text;
    }

    private static float[] calculateDrawPoints(float p1x, float p1y, float p2x, float p2y, int screenHeight, int screenWidth) {
        float drawP1x;
        float drawP1y;
        float m;
        if(p1y > (float)screenHeight) {
            m = (p2y - p1y) / (p2x - p1x);
            drawP1x = ((float)screenHeight - p1y + m * p1x) / m;
            drawP1y = (float)screenHeight;
            if(drawP1x < 0.0F) {
                drawP1x = 0.0F;
                drawP1y = p1y - m * p1x;
            } else if(drawP1x > (float)screenWidth) {
                drawP1x = (float)screenWidth;
                drawP1y = m * (float)screenWidth + p1y - m * p1x;
            }
        } else if(p1y < 0.0F) {
            m = (p2y - p1y) / (p2x - p1x);
            drawP1x = (-p1y + m * p1x) / m;
            drawP1y = 0.0F;
            if(drawP1x < 0.0F) {
                drawP1x = 0.0F;
                drawP1y = p1y - m * p1x;
            } else if(drawP1x > (float)screenWidth) {
                drawP1x = (float)screenWidth;
                drawP1y = m * (float)screenWidth + p1y - m * p1x;
            }
        } else {
            drawP1x = p1x;
            drawP1y = p1y;
        }

        float drawP2x;
        float drawP2y;
        if(p2y > (float)screenHeight) {
            m = (p2y - p1y) / (p2x - p1x);
            drawP2x = ((float)screenHeight - p1y + m * p1x) / m;
            drawP2y = (float)screenHeight;
            if(drawP2x < 0.0F) {
                drawP2x = 0.0F;
                drawP2y = p1y - m * p1x;
            } else if(drawP2x > (float)screenWidth) {
                drawP2x = (float)screenWidth;
                drawP2y = m * (float)screenWidth + p1y - m * p1x;
            }
        } else if(p2y < 0.0F) {
            m = (p2y - p1y) / (p2x - p1x);
            drawP2x = (-p1y + m * p1x) / m;
            drawP2y = 0.0F;
            if(drawP2x < 0.0F) {
                drawP2x = 0.0F;
                drawP2y = p1y - m * p1x;
            } else if(drawP2x > (float)screenWidth) {
                drawP2x = (float)screenWidth;
                drawP2y = m * (float)screenWidth + p1y - m * p1x;
            }
        } else {
            drawP2x = p2x;
            drawP2y = p2y;
        }

        return new float[]{drawP1x, drawP1y, drawP2x, drawP2y};
    }

    protected void drawPath(Canvas canvas, List<Float> points, Paint paint, boolean circular) {
        Path path = new Path();
        int height = canvas.getHeight();
        int width = canvas.getWidth();
        if(points.size() >= 4) {
            float[] tempDrawPoints = calculateDrawPoints(((Float)points.get(0)).floatValue(), ((Float)points.get(1)).floatValue(), ((Float)points.get(2)).floatValue(), ((Float)points.get(3)).floatValue(), height, width);
            path.moveTo(tempDrawPoints[0], tempDrawPoints[1]);
            path.lineTo(tempDrawPoints[2], tempDrawPoints[3]);
            int length = points.size();

            for(int i = 4; i < length; i += 2) {
                if((((Float)points.get(i - 1)).floatValue() >= 0.0F || ((Float)points.get(i + 1)).floatValue() >= 0.0F) && (((Float)points.get(i - 1)).floatValue() <= (float)height || ((Float)points.get(i + 1)).floatValue() <= (float)height)) {
                    tempDrawPoints = calculateDrawPoints(((Float)points.get(i - 2)).floatValue(), ((Float)points.get(i - 1)).floatValue(), ((Float)points.get(i)).floatValue(), ((Float)points.get(i + 1)).floatValue(), height, width);
                    if(!circular) {
                        path.moveTo(tempDrawPoints[0], tempDrawPoints[1]);
                    }

                    path.lineTo(tempDrawPoints[2], tempDrawPoints[3]);
                }
            }

            if(circular) {
                path.lineTo(((Float)points.get(0)).floatValue(), ((Float)points.get(1)).floatValue());
            }

            canvas.drawPath(path, paint);
        }
    }

    protected void drawPath(Canvas canvas, float[] points, Paint paint, boolean circular) {
        Path path = new Path();
        int height = canvas.getHeight();
        int width = canvas.getWidth();
        if(points.length >= 4) {
            float[] tempDrawPoints = calculateDrawPoints(points[0], points[1], points[2], points[3], height, width);
            path.moveTo(tempDrawPoints[0], tempDrawPoints[1]);
            path.lineTo(tempDrawPoints[2], tempDrawPoints[3]);
            int length = points.length;

            for(int i = 4; i < length; i += 2) {
                if((points[i - 1] >= 0.0F || points[i + 1] >= 0.0F) && (points[i - 1] <= (float)height || points[i + 1] <= (float)height)) {
                    tempDrawPoints = calculateDrawPoints(points[i - 2], points[i - 1], points[i], points[i + 1], height, width);
                    if(!circular) {
                        path.moveTo(tempDrawPoints[0], tempDrawPoints[1]);
                    }

                    path.lineTo(tempDrawPoints[2], tempDrawPoints[3]);
                }
            }

            if(circular) {
                path.lineTo(points[0], points[1]);
            }

            canvas.drawPath(path, paint);
        }
    }

    public abstract int getLegendShapeWidth(int var1);

    public abstract void drawLegendShape(Canvas var1, SimpleSeriesRenderer var2, float var3, float var4, int var5, Paint var6);

    private String getFitText(String text, float width, Paint paint) {
        String newText = text;
        int length = text.length();

        int diff;
        for(diff = 0; paint.measureText(newText) > width && diff < length; newText = text.substring(0, length - diff) + "...") {
            ++diff;
        }

        if(diff == length) {
            newText = "...";
        }

        return newText;
    }

    protected int getLegendSize(DefaultRenderer renderer, int defaultHeight, float extraHeight) {
        int legendSize = renderer.getLegendHeight();
        if(renderer.isShowLegend() && legendSize == 0) {
            legendSize = defaultHeight;
        }

        if(!renderer.isShowLegend() && renderer.isShowXLabels()) {
            legendSize = (int)(renderer.getLabelsTextSize() * 4.0F / 3.0F + extraHeight);
        }

        return legendSize;
    }

    protected void drawLabel(Canvas canvas, String labelText, DefaultRenderer renderer, List<RectF> prevLabelsBounds, int centerX, int centerY, float shortRadius, float longRadius, float currentAngle, float angle, int left, int right, int color, Paint paint, boolean line, boolean display) {
        if(renderer.isShowLabels() || display) {
            paint.setColor(color);
            double rAngle = Math.toRadians((double)(90.0F - (currentAngle + angle / 2.0F)));
            double sinValue = Math.sin(rAngle);
            double cosValue = Math.cos(rAngle);
            int x1 = Math.round((float)centerX + (float)((double)shortRadius * sinValue));
            int y1 = Math.round((float)centerY + (float)((double)shortRadius * cosValue));
            int x2 = Math.round((float)centerX + (float)((double)longRadius * sinValue));
            int y2 = Math.round((float)centerY + (float)((double)longRadius * cosValue));
            float size = renderer.getLabelsTextSize();
            float extra = Math.max(size / 2.0F, 10.0F);
            paint.setTextAlign(Align.LEFT);
            if(x1 > x2) {
                extra = -extra;
                paint.setTextAlign(Align.RIGHT);
            }

            float xLabel = (float)x2 + extra;
            float yLabel = (float)y2;
            float width = (float)right - xLabel;
            if(x1 > x2) {
                width = xLabel - (float)left;
            }

            labelText = this.getFitText(labelText, width, paint);
            float widthLabel = paint.measureText(labelText);

            boolean intersects;
            for(boolean okBounds = false; !okBounds && line; okBounds = !intersects) {
                intersects = false;
                int length = prevLabelsBounds.size();

                for(int j = 0; j < length && !intersects; ++j) {
                    RectF prevLabelBounds = (RectF)prevLabelsBounds.get(j);
                    if(prevLabelBounds.intersects(xLabel, yLabel, xLabel + widthLabel, yLabel + size)) {
                        intersects = true;
                        yLabel = Math.max(yLabel, prevLabelBounds.bottom);
                    }
                }
            }

            if(line) {
                y2 = (int)(yLabel - size / 2.0F);
                canvas.drawLine((float)x1, (float)y1, (float)x2, (float)y2, paint);
                canvas.drawLine((float)x2, (float)y2, (float)x2 + extra, (float)y2, paint);
            } else {
                paint.setTextAlign(Align.CENTER);
            }

            canvas.drawText(labelText, xLabel, yLabel, paint);
            if(line) {
                prevLabelsBounds.add(new RectF(xLabel, yLabel, xLabel + widthLabel, yLabel + size));
            }
        }

    }

    public boolean isNullValue(double value) {
        return Double.isNaN(value) || Double.isInfinite(value) || value == 1.7976931348623157E308D;
    }

    public SeriesSelection getSeriesAndPointForScreenCoordinate(Point screenPoint) {
        return null;
    }
}
