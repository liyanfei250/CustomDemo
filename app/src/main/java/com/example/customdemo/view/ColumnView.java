package com.example.customdemo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.example.customdemo.R;
import com.example.customdemo.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by ${LZQ} on 2019/4/30.
 */
public class ColumnView extends View {

    protected int defaultBorderColor = Color.argb(255, 217, 217, 217);
    protected int titleTextColor = Color.argb(255, 217, 217, 217);
    protected int labelTextColor;
    protected int mTitleTextSize = 42;
    protected int mLabelTextSize = 20;
    protected String mTitle;
    private int mWidth;
    private int mHeight;
    private int mLeftTextSpace;
    private int mBottomTextSpace;
    private int mTopTextSpace;
    protected Paint mBorderLinePaint;
    private Double maxData;

    private List<Double> mDatas;

    /**
     * 备注文本画笔
     */
    private Paint mTextPaint;
    /**
     * 标题文本画笔
     */
    private Paint mTitleTextPaint;

    private BarChart barChartView;


    /**
     * 自定义view实现柱状图
     * 首先定义一个类实现View
     */

    //定义画笔
    private Paint mLinePaint;
    private Paint mGreenPaint;
    //定义上下文
    private Context mContext;
    //定义宽高
    private float weight;
    private float height;
    private float mScale;
    //这个数组是高度的值
    private String[] y_title = {"100", "50", "60", "40", "20", "0"};
    //分别为定义数据与数据源名称的集合
    private List<Long> mData;
    private List<String> mNames;


    public ColumnView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public ColumnView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ColumnView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColumnView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    private void init(Context context, AttributeSet attrs) {
        mDatas = new ArrayList<>();

        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.barCharts);
        defaultBorderColor = t.getColor(R.styleable.barCharts_borderColor, defaultBorderColor);
        titleTextColor = t.getColor(R.styleable.barCharts_titleTextColor, Color.GRAY);
        mTitleTextSize = (int) t.getDimension(R.styleable.barCharts_titleTextSize, mTitleTextSize);
        mLabelTextSize = (int) t.getDimension(R.styleable.barCharts_labelTextSize, mLabelTextSize);
        labelTextColor = t.getColor(R.styleable.barCharts_labelTextColor, Color.GRAY);

        mLeftTextSpace = (int) t.getDimension(R.styleable.barCharts_leftTextSpace, 30);
        mBottomTextSpace = (int) t.getDimension(R.styleable.barCharts_bottomTextSpace, 20);
        mTopTextSpace = (int) t.getDimension(R.styleable.barCharts_topTextSpace, 50);
        mTitle = t.getString(R.styleable.barCharts_title);
        t.recycle();

        mBorderLinePaint = generatePaint();
        mBorderLinePaint.setColor(defaultBorderColor);
        mBorderLinePaint.setStrokeWidth(Utils.dp2px(context, 1));

        mTextPaint = generatePaint();
        mTextPaint.setColor(labelTextColor);
        mTextPaint.setTextSize(mLabelTextSize);

        mTitleTextPaint = generatePaint();
        mTitleTextPaint.setColor(titleTextColor);
        mTitleTextPaint.setTextSize(mTitleTextSize);


        //给定义的画笔进行加工
        mContext = context;
        mLinePaint = new Paint();
        mGreenPaint = new Paint();
        mTextPaint = new Paint();

        mLinePaint.setARGB(255, 223, 233, 231);
        mGreenPaint.setARGB(255, 0, 200, 149);
        mTextPaint.setARGB(255, 153, 153, 153);

        mGreenPaint.setStyle(Paint.Style.FILL);

        mTextPaint.setAntiAlias(true);
        mGreenPaint.setAntiAlias(true);
        mLinePaint.setAntiAlias(true);

        mScale = context.getResources().getDisplayMetrics().density;
        //初始化数据
        mData = new ArrayList<>();
        mNames = new ArrayList<>();


    }

    private Paint generatePaint() {
        Paint m = new Paint();
        m.setAntiAlias(true);
        m.setStyle(Paint.Style.STROKE);
        return m;
    }


    //尺寸发生改变的时候调用
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        weight = 0.7F * w;
        height = 0.50F * h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float min_height = height / 5;
        List<Long> listData = new ArrayList<>();
        //x轴
        for (int i = 5; i >= 0; i--) {
            if (i == 5) {
                mLinePaint.setARGB(255, 131, 148, 144);
            } else {
                mLinePaint.setARGB(255, 223, 233, 231);
            }
            canvas.drawLine(50 * mScale, 30 * mScale + min_height * i, 50 * mScale + weight, 30 * mScale + min_height * i, mLinePaint);
            mTextPaint.setTextAlign(Paint.Align.RIGHT);
            mTextPaint.setTextSize(10 * mScale);

            canvas.drawText(y_title[i], 40 * mScale, 32 * mScale + min_height * i, mTextPaint);

        }
        //y轴
        mLinePaint.setARGB(255, 131, 148, 144);
        canvas.drawLine(50 * mScale, 30 * mScale + min_height * 5, 50 * mScale, 30 * mScale - 20, mLinePaint);

        float min_weight = (weight - 50 * mScale) / (mData.size());
        mTextPaint.setTextSize(12 * mScale);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        for (int i = 0; i < mData.size(); i++) {
            int leftR = (int) (50 * mScale + i * min_weight + min_weight / 2);
            int rightR = leftR + (int) (min_weight / 2);
            int buttomR = (int) (30 * mScale + min_height * 5);
            int topR = buttomR - (int) (height / 100 * mData.get(i));
            canvas.drawRect(new RectF(leftR, topR, rightR, buttomR), mGreenPaint);
            mTextPaint.setARGB(255, 153, 153, 153);
            canvas.drawText(mNames.get(i), leftR + min_weight / 4, buttomR + 20 * mScale, mTextPaint);
            mTextPaint.setARGB(255, 51, 51, 51);
            canvas.drawText(mData.get(i) + "", leftR + min_weight / 4, topR - 10 * mScale, mTextPaint);
        }
    }

    //传入数据并进行绘制
    public void updateThisData(List<Long> data, List<String> name) {
        mData = data;
        mNames = name;
        invalidate();
    }


}
