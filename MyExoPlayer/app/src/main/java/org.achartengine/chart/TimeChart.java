//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.achartengine.chart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.achartengine.chart.LineChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

public class TimeChart extends LineChart {
    public static final String TYPE = "Time";
    public static final long DAY = 86400000L;
    private String mDateFormat;
    private Double mStartPoint;

    TimeChart() {
    }

    public TimeChart(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer) {
        super(dataset, renderer);
    }

    public String getDateFormat() {
        return this.mDateFormat;
    }

    public void setDateFormat(String format) {
        this.mDateFormat = format;
    }

    protected void drawXLabels(List<Double> xLabels, Double[] xTextLabelLocations, Canvas canvas, Paint paint, int left, int top, int bottom, double xPixelsPerUnit, double minX, double maxX) {
        int length = xLabels.size();
        if(length > 0) {
            boolean showXLabels = this.mRenderer.isShowXLabels();
            boolean showGridY = this.mRenderer.isShowGridY();
            if(showGridY) {
                this.mGridPaint.setStyle(Style.STROKE);
                this.mGridPaint.setStrokeWidth(this.mRenderer.getGridLineWidth());
            }

            boolean showTickMarks = this.mRenderer.isShowTickMarks();
            DateFormat format = this.getDateFormat(((Double)xLabels.get(0)).doubleValue(), ((Double)xLabels.get(length - 1)).doubleValue());

            for(int i = 0; i < length; ++i) {
                long label = Math.round(((Double)xLabels.get(i)).doubleValue());
                float xLabel = (float)((double)left + xPixelsPerUnit * ((double)label - minX));
                if(showXLabels) {
                    paint.setColor(this.mRenderer.getXLabelsColor());
                    if(showTickMarks) {
                        canvas.drawLine(xLabel, (float)bottom, xLabel, (float)bottom + this.mRenderer.getLabelsTextSize() / 3.0F, paint);
                    }

                    this.drawText(canvas, format.format(new Date(label)), xLabel, (float)bottom + this.mRenderer.getLabelsTextSize() * 4.0F / 3.0F + this.mRenderer.getXLabelsPadding(), paint, this.mRenderer.getXLabelsAngle());
                }

                if(showGridY) {
                    this.mGridPaint.setColor(this.mRenderer.getGridColor(0));
                    canvas.drawLine(xLabel, (float)bottom, xLabel, (float)top, this.mGridPaint);
                }
            }
        }

        this.drawXTextLabels(xTextLabelLocations, canvas, paint, true, left, top, bottom, xPixelsPerUnit, minX, maxX);
    }

    private DateFormat getDateFormat(double start, double end) {
        DateFormat format;
        if(this.mDateFormat != null) {
            format = null;

            try {
                SimpleDateFormat format1 = new SimpleDateFormat(this.mDateFormat);
                return format1;
            } catch (Exception var8) {
                ;
            }
        }

        format = SimpleDateFormat.getDateInstance(2);
        double diff = end - start;
        if(diff > 8.64E7D && diff < 4.32E8D) {
            format = SimpleDateFormat.getDateTimeInstance(3, 3);
        } else if(diff < 8.64E7D) {
            format = SimpleDateFormat.getTimeInstance(2);
        }

        return format;
    }

    public String getChartType() {
        return "Time";
    }

    protected List<Double> getXLabels(double min, double max, int count) {
        ArrayList result = new ArrayList();
        int i;
        if(!this.mRenderer.isXRoundedLabels()) {
            if(this.mDataset.getSeriesCount() <= 0) {
                return super.getXLabels(min, max, count);
            } else {
                XYSeries var16 = this.mDataset.getSeriesAt(0);
                int length = var16.getItemCount();
                int var17 = 0;
                int startIndex = -1;

                int var18;
                for(var18 = 0; var18 < length; ++var18) {
                    double intervalCount = var16.getX(var18);
                    if(min <= intervalCount && intervalCount <= max) {
                        ++var17;
                        if(startIndex < 0) {
                            startIndex = var18;
                        }
                    }
                }

                if(var17 < count) {
                    for(var18 = startIndex; var18 < startIndex + var17; ++var18) {
                        result.add(Double.valueOf(var16.getX(var18)));
                    }
                } else {
                    float var20 = (float)var17 / (float)count;
                    int var19 = 0;

                    for(i = 0; i < length && var19 < count; ++i) {
                        double value = var16.getX(Math.round((float)i * var20));
                        if(min <= value && value <= max) {
                            result.add(Double.valueOf(value));
                            ++var19;
                        }
                    }
                }

                return result;
            }
        } else {
            if(this.mStartPoint == null) {
                this.mStartPoint = Double.valueOf(min - min % 8.64E7D + 8.64E7D + (double)((new Date(Math.round(min))).getTimezoneOffset() * 60 * 1000));
            }

            if(count > 25) {
                count = 25;
            }

            double cycleMath = (max - min) / (double)count;
            if(cycleMath <= 0.0D) {
                return result;
            } else {
                double cycle = 8.64E7D;
                if(cycleMath <= 8.64E7D) {
                    while(cycleMath < cycle / 2.0D) {
                        cycle /= 2.0D;
                    }
                } else {
                    while(cycleMath > cycle) {
                        cycle *= 2.0D;
                    }
                }

                double val = this.mStartPoint.doubleValue() - Math.floor((this.mStartPoint.doubleValue() - min) / cycle) * cycle;

                for(i = 0; val < max && i++ <= count; val += cycle) {
                    result.add(Double.valueOf(val));
                }

                return result;
            }
        }
    }
}
