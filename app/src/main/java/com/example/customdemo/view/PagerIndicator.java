package com.example.customdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.customdemo.R;

public class PagerIndicator extends View {
    // 空心圆半径
    private int RADIUS = 10;
    private int space = 3;
    // 空心圆画笔
    private Paint mBgPaint;
    // 实心圆画笔（当前页）
    private Paint mPaint;
    private Paint mPaint2;
    // 圆点个数,默认为3,设计布局时可以预览
    private int mCount = 1;
    // 当前实心圆的位置
    private int mPosition;
    // 偏移量（百分比）
    private float mOffset;
    // 第一个空心圆的圆心坐标
    private int startY;
    private int startX;
    //实心圆颜色
    private int roundColor;
    //空心圆颜色
    private int roundHollowColor;
    private RectF oval;
    private int width;
    private int height;
    private boolean paintHollow;
    public PagerIndicator(Context context) {
        super(context);
    }

    // 创建带AttributeSet参数的构造方法使控件可以直接拖动到布局中并预览
    public PagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        oval = new RectF();
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.PagerIndicator);
        roundColor = mTypedArray.getColor(R.styleable.PagerIndicator_roundSolidColor, Color.WHITE);
        roundHollowColor = mTypedArray.getColor(R.styleable.PagerIndicator_roundHollowColor, Color.GRAY);
        paintHollow = mTypedArray.getBoolean(R.styleable.PagerIndicator_paintHollow,false);
        RADIUS = mTypedArray.getDimensionPixelSize(R.styleable.PagerIndicator_roundRadius, 3);
        mTypedArray.recycle();
        initPaint(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        startX = (width - space * RADIUS * (getCount() - 1)) / 2;
        startY = height / 2;

        // 画出空心的小圆点
        for (int i = 0; i < getCount(); i++) {
            canvas.drawCircle(startX + i * space * RADIUS, startY, RADIUS, mBgPaint);
        }

        // 画出指示当前位置的原点,由于高度固定，所以只计算了X坐标
        if (mPosition < getCount() - 1) {
            float x = startX + (mPosition + mOffset) * space * RADIUS;
            canvas.drawCircle(x, startY, RADIUS, mPaint2);
        } else {
            float x = startX + (mPosition) * space * RADIUS;
            canvas.drawCircle(x, startY, RADIUS, mPaint2);
        }
        if (paintHollow){
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setAntiAlias(true);
        }
        // 画出指示当前位置的原点,由于高度固定，所以只计算了X坐标
        if (mPosition < getCount() - 1) {
            float x = startX + (mPosition + mOffset) * space * RADIUS;
            canvas.drawCircle(x, startY, RADIUS, mPaint);
        } else {
            float x = startX + (mPosition) * space * RADIUS;
            canvas.drawCircle(x, startY, RADIUS, mPaint);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        startX = (width - space * RADIUS * (getCount() - 1)) / 2;
        startY = height / 2;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    // 设置圆个数
    public void setCount(int count) {
        mCount = count;
        invalidate();

    }

    // 获取圆个数
    public int getCount() {
        return mCount;
    }

    // 获取偏移量并重绘indicator
    public void onPageScrolled(int position, float offset) {
        mPosition = position;
        mOffset = offset;
        invalidate();
    }

    // 初始化画笔
    private void initPaint(Context context) {
        // 空心圆画笔
        mBgPaint = new Paint();
        mBgPaint.setColor(roundHollowColor);
        mBgPaint.setAntiAlias(true);
        // 实心圆画笔
        mPaint = new Paint();
        mPaint2 = new Paint();
        mPaint2.setColor(Color.WHITE);
        mPaint2.setStyle(Paint.Style.FILL);
        mPaint.setColor(roundColor);

        mPaint.setAntiAlias(true);


    }
}