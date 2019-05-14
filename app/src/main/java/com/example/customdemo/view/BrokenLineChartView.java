package com.example.customdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.example.customdemo.R;
import com.example.customdemo.utils.NumUtil;

public class BrokenLineChartView extends View {

    private int rows = 5;

    private float[] values;
    private String[] weeks;

    public BrokenLineChartView(Context context) {
        super(context);
    }

    public BrokenLineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BrokenLineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint linePaint = getPaint();
        linePaint.setStrokeWidth(2);

        Paint textPaint = getPaint();
        textPaint.setColor(0xff767676);
        textPaint.setTextSize(sp2px(11));

        Paint.FontMetrics fm = textPaint.getFontMetrics();
        float th = fm.descent - fm.ascent;

        if (values == null || weeks == null
                || values.length == 0 || weeks.length == 0) {
            linePaint.setColor(0xff767676);
            canvas.drawLine(0, getHeight(), getWidth(), getHeight(), linePaint);

            //绘制横向坐标线
            linePaint.setColor(0xffbfbfbf);
            float rowoff = getHeight() / rows;
            for (int i = 0; i < rows; i++) {
                float y = i * rowoff;
                canvas.drawLine(0, y, getWidth(), y, linePaint);
            }
            return;
        }
        float max = Float.MIN_VALUE;
        float min = Float.MAX_VALUE;
        for (int i = 0; i < values.length; i++) {
            max = Math.max(max, values[i]);
            min = Math.min(min, values[i]);
        }
        if (max - min < 1) {
            max += max * 0.1f;
            min -= max * 0.1f;
        } else {
            max += ((max - min) * 0.1f);
            min -= ((max - min) * 0.1f);
        }

        float left = textPaint.measureText(NumUtil.format(max)) * 1.6f;
        float right = dp2px(15);
        float top = dp2px(10);
        float bottom = getHeight() - th * 2;

        linePaint.setColor(0xff767676);
        canvas.drawLine(0, bottom, getWidth(), bottom, linePaint);

        //绘制横向坐标线
        linePaint.setColor(0xffbfbfbf);
        float rowoff = (bottom - top) / rows;
        for (int i = 0; i < rows; i++) {
            float y = top + i * rowoff;
            canvas.drawLine(left, y, getWidth(), y, linePaint);
        }

        //绘制 X轴坐标值
        textPaint.setTextAlign(Paint.Align.CENTER);
        float coloff = (getWidth() - left - right) / 4;
        for (int i = 0; i < weeks.length; i++) {
            float x = left + i * coloff;
            float y = fitCenter(getHeight() - th, fm);
            canvas.drawText(weeks[i], x, y, textPaint);
        }

        //绘制 Y轴坐标值
        textPaint.setTextAlign(Paint.Align.LEFT);
        float valueoff = (max - min) / (float) rows;
        for (int i = 0; i < rows; i++) {
            float value = (max - i * valueoff);
            float x = 0;
            float y = fitCenter(top + i * rowoff, fm);
            canvas.drawText("$" + NumUtil.format(value), x, y, textPaint);
        }

        //绘制折线
        linePaint.setStrokeWidth(3);
        linePaint.setColor(getResources().getColor(R.color.main_color));
        Path path = new Path();
        Path bgpath = new Path();
        bgpath.moveTo(left, bottom);

        float unit = (bottom - top) / (max - min);
        for (int i = 0; i < values.length; i++) {
            float x = left + i * coloff;
            float y = (max - values[i]) * unit + top;
            if (path.isEmpty()) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
            bgpath.lineTo(x, y);
            if (i == values.length - 1) {
                bgpath.lineTo(x, bottom);
            }
        }

        linePaint.setShader(getShade());
        linePaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(bgpath, linePaint);

        linePaint.setShader(null);
        linePaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, linePaint);

        //绘制小圆点
        linePaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < values.length; i++) {
            float x = left + i * coloff;
            float y = (max - values[i]) * unit + top;
            linePaint.setColor(getResources().getColor(R.color.main_color));
            canvas.drawCircle(x, y, 6, linePaint);
            linePaint.setColor(Color.WHITE);
            canvas.drawCircle(x, y, 3, linePaint);
        }
    }

    public float fitCenter(float y, Paint.FontMetrics fm) {
        y = y - (fm.ascent + fm.descent) / 2f;
        return y;
    }

    private Paint getPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        return paint;
    }

    private Shader getShade() {
        Shader shader = new LinearGradient(0, 0, 0, getHeight(), new int[]{0x605280f1, 0x0}, null, Shader.TileMode.CLAMP);
        // 新建一个线性渐变，前两个参数是渐变开始的点坐标，第三四个参数是渐变结束的点的坐标。连接这2个点就拉出一条渐变线了，玩过PS的都懂。然后那个数组是渐变的颜色。
        // 下一个参数是渐变颜色的分布，如果为空，每个颜色就是均匀分布的。最后是模式，这里设置的是循环渐变
        return shader;
    }

    private int dp2px(int value) {
        float v = getResources().getDisplayMetrics().density;
        return (int) (v * value + 0.5f);
    }

    private int sp2px(int value) {
        float v = getResources().getDisplayMetrics().scaledDensity;
        return (int) (v * value + 0.5f);
    }

    public void setValues(float[] values) {
        this.values = values;
        postInvalidate();
    }

    public void setWeeks(String[] weeks) {
        this.weeks = weeks;
        postInvalidate();
    }
}
