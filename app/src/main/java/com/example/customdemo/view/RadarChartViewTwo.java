package com.example.customdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.example.customdemo.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by ${LZQ} on 2019/5/13.
 */
public class RadarChartViewTwo extends View {

    private Paint textPaint;
    private Paint linePaint;

    private int edge = 5;   //多边形边数
    private int dial = 5;   //刻度数
    private float radius;   //最大半径

    private HashMap<String, Float> valueMap = new LinkedHashMap<>();
    private int starRatingScore;

    public RadarChartViewTwo(Context context) {
        this(context, null);
        initPaint();
    }

    public RadarChartViewTwo(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initPaint();
    }

    public RadarChartViewTwo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    public float dptopx(float dp) {
        float density = getResources().getDisplayMetrics().density;
        return dp * density + 0.5f;
    }

    public float sptopx(float sp) {
        float density = getResources().getDisplayMetrics().scaledDensity;
        return sp * density + 0.5f;
    }

    private void initPaint() {
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(sptopx(11));

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(3);
    }

    /*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    private int measureHeight(int heightMeasureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            return specSize;
        } else {
            result = 200;
            if (specMode == MeasureSpec.AT_MOST) {
                return Math.min(result, specSize);
            }
        }
        return result;
    }

    private int measureWidth(int widthMeasureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            return specSize;
        } else {
            result = 200;
            if (specMode == MeasureSpec.AT_MOST) {
                return Math.min(result, specSize);
            }
        }
        return result;
    }*/

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        canvas.save();
        canvas.translate(width / 2, height / 2);
        drawGrid(canvas);
        if (valueMap.isEmpty()) {
            return;
        }
        drawData(canvas);
        drawText(canvas);
        canvas.restore();
    }

    private void drawGrid(Canvas canvas) {
        linePaint.setColor(0xffe3e3e3);
        linePaint.setStrokeWidth(3);
        float angle = (float) (Math.PI * 2 / edge);
        Path path = new Path();
        Path line = new Path();
        float roff = radius / dial;

        for (int i = 1; i <= dial; i++) {
            path.reset();
            float r = roff * i;
            for (int j = 0; j < edge; j++) {
                float x = (float) (r * Math.cos(-Math.PI / 2 + angle * j));
                float y = (float) (r * Math.sin(-Math.PI / 2 + angle * j));
                if (i == dial) {
                    line.lineTo(x, y);
                    canvas.drawPath(line, linePaint);
                    line.reset();
                }
                if (j == 0) {
                    path.moveTo(x, y);
                } else {
                    path.lineTo(x, y);
                }
            }
            path.close();
            canvas.drawPath(path, linePaint);
        }
    }

    private void drawText(Canvas canvas) {
        textPaint.setColor(0xff506060);
        float angleoff = (float) (Math.PI * 2 / edge);
        float space = dptopx(3);
        Iterator<String> iterator = valueMap.keySet().iterator();
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        //float r = radius * (edge - 1) / edge;
        for (int i = 0; i < edge; i++) {
            if (!iterator.hasNext()) break;
            String text = iterator.next();
            float x = (float) ((space + radius) * Math.cos(-Math.PI / 2 + angleoff * i));
            float y = (float) ((space + radius) * Math.sin(-Math.PI / 2 + angleoff * i));
            double angle = toAngle(angleoff * i) % 360;
            if (angle <= 10 || angle > 350) {
                textPaint.setTextAlign(Paint.Align.CENTER);
                y = fitBottom(y, fontMetrics);
            } else if (angle <= 140) {
                textPaint.setTextAlign(Paint.Align.LEFT);
                y = fitCenter(y, fontMetrics);
            } else if (angle <= 220) {
                textPaint.setTextAlign(Paint.Align.CENTER);
                y = fitTop(y, fontMetrics);
            } else if (angle <= 350) {
                textPaint.setTextAlign(Paint.Align.RIGHT);
                y = fitCenter(y, fontMetrics);
            }
            canvas.drawText(text, x, y, textPaint);
        }
    }

    public static double toAngle(double radian) {
        return (radian * 180f) / Math.PI;
    }

    private void drawData(Canvas canvas) {
        linePaint.setColor(getResources().getColor(R.color.main_color));
        linePaint.setStrokeWidth(dptopx(2));
        float angle = (float) (Math.PI * 2 / edge);
        Iterator<Float> iter = valueMap.values().iterator();
        int valueSum = 0;
        Path path = new Path();
        for (int i = 0; i < edge; i++) {
            if (!iter.hasNext()) break;
            float value = Math.max(iter.next(), 0);
            valueSum += value;
            float x = (float) Math.cos(-Math.PI / 2 + angle * i) * radius * value / 100f;
            float y = (float) Math.sin(-Math.PI / 2 + angle * i) * radius * value / 100f;
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        path.close();
        canvas.drawPath(path, linePaint);

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(getResources().getColor(R.color.main_color));
        canvas.drawText(String.valueOf(starRatingScore), 0, fitCenter(0, fontMetrics), textPaint);
    }

    public float fitTop(float y, Paint.FontMetrics fm) {
        y = y - fm.ascent;
        return y;
    }

    public float fitCenter(float y, Paint.FontMetrics fm) {
        y = y - (fm.ascent + fm.descent) / 2f;
        return y;
    }

    public float fitBottom(float y, Paint.FontMetrics fm) {
        y = y - fm.descent;
        return y;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        radius = Math.min(w, h) / 2 * 0.8f;
        postInvalidate();
    }

    public void setData(HashMap<String, Float> data) {
        valueMap.clear();
        valueMap.putAll(data);
        postInvalidate();
    }


    public void setRatingData(int starRatingScore) {
        this.starRatingScore = starRatingScore;
        postInvalidate();
    }
}
