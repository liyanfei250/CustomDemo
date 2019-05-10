package com.example.customdemo.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.example.customdemo.R;


public final class RoundButton extends TextView {
    private int strokeWidth;
    private int strokeColor;

    public RoundButton(Context context) {
        this(context, null);
    }

    public RoundButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundButton);
        int cornerRadius = a.getLayoutDimension(R.styleable.RoundButton_btnCornerRadius, 0);
        int solidColor = a.getColor(R.styleable.RoundButton_btnSolidColor, getResources().getColor(R.color.white));
        int pressedSolidColor = a.getColor(R.styleable.RoundButton_btnPressedSolidColor, solidColor);
        int disableSolidColor = a.getColor(R.styleable.RoundButton_btnDisableSolidColor, solidColor);


        strokeColor = a.getColor(R.styleable.RoundButton_btnStrokeColor, getResources().getColor(R.color.my_coupon));
        int pressedStrokeColor = a.getColor(R.styleable.RoundButton_btnPressedStrokeColor, strokeColor);
        int disableStrokeColor = a.getColor(R.styleable.RoundButton_btnDisableStrokeColor, strokeColor);

        int textColor = a.getColor(R.styleable.RoundButton_textNormalColor, getResources().getColor(R.color.my_coupon));
        int textPressedColor = a.getColor(R.styleable.RoundButton_textPressedColor, textColor);
        int textDisableColor = a.getColor(R.styleable.RoundButton_textDisableColor, textColor);


        strokeWidth = a.getDimensionPixelSize(R.styleable.RoundButton_btnStrokeWidth, 0);
        int strokeDashWidth = a.getDimensionPixelSize(R.styleable.RoundButton_btnStrokeDashWidth, 0);
        int strokeDashGap = a.getDimensionPixelSize(R.styleable.RoundButton_btnStrokeDashGap, 0);

        a.recycle();

        setSingleLine(true);
        setGravity(Gravity.CENTER);

        RoundDrawable rdNormal = new RoundDrawable(cornerRadius == -1);
        rdNormal.setCornerRadius(cornerRadius == -1 ? 0 : cornerRadius);
        rdNormal.setStroke(strokeWidth, strokeColor, strokeDashWidth, strokeDashGap);
        rdNormal.setSolidColors(createColorStateList(solidColor, pressedSolidColor, disableSolidColor));


        RoundDrawable rdPressed = new RoundDrawable(cornerRadius == -1);
        rdPressed.setCornerRadius(cornerRadius == -1 ? 0 : cornerRadius);
        rdPressed.setStroke(strokeWidth, pressedStrokeColor, strokeDashWidth, strokeDashGap);
        rdPressed.setSolidColors(createColorStateList(solidColor, pressedSolidColor, disableSolidColor));

        RoundDrawable rdDisable = new RoundDrawable(cornerRadius == -1);
        rdDisable.setCornerRadius(cornerRadius == -1 ? 0 : cornerRadius);
        rdDisable.setStroke(strokeWidth, disableStrokeColor, strokeDashWidth, strokeDashGap);
        rdDisable.setSolidColors(createColorStateList(solidColor, pressedSolidColor, disableSolidColor));
        if (Build.VERSION.SDK_INT >= 16) {
            setBackground(generateSelectorFromDrawables(rdPressed, rdNormal, rdDisable));
        } else {
            setBackgroundDrawable(generateSelectorFromDrawables(rdPressed, rdNormal, rdDisable));
        }

        setTextColor(createColorStateList(textColor, textPressedColor, textDisableColor));
    }

    private Drawable generateSelectorFromDrawables(Drawable pressed, Drawable normal, Drawable disable) {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled,}, pressed);
        states.addState(new int[]{android.R.attr.state_enabled}, normal);
        states.addState(new int[]{android.R.attr.state_window_focused}, disable);
        states.addState(new int[]{}, normal);

        return states;
    }

    private ColorStateList createColorStateList(int normal, int pressed, int disable) {
        int[] colors = new int[]{pressed, normal, disable, normal};
        int[][] states = new int[4][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
//        states[1] = new int[] { android.R.attr.state_enabled, android.R.attr.state_focused };
        states[1] = new int[]{android.R.attr.state_enabled};
//        states[3] = new int[] { android.R.attr.state_focused };
        states[2] = new int[]{android.R.attr.state_window_focused};
        states[3] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();

        Drawable drawableLeft = drawables[0];
        Drawable drawableRight = drawables[2];
        if (drawableLeft != null) {
            float textWidth = getPaint().measureText(getText().toString());
            int drawablePadding = getCompoundDrawablePadding();

            int drawableWidth = drawableLeft.getIntrinsicWidth();
            float bodyWidth = textWidth + drawableWidth + drawablePadding;
            setPadding(0, getPaddingTop(), 0, getPaddingBottom());
            canvas.translate((getWidth() - bodyWidth) / 2, 0);
        } else if (drawableRight != null) {
            float textWidth = getPaint().measureText(getText().toString());
            int drawablePadding = getCompoundDrawablePadding();
            int drawableWidth = drawableRight.getIntrinsicWidth();
            float bodyWidth = textWidth + drawableWidth + drawablePadding;

            setPadding(0, getPaddingTop(), (int) (getWidth() - bodyWidth), getPaddingBottom());
            canvas.translate((getWidth() - bodyWidth) / 2, 0);
        }

        super.onDraw(canvas);
    }


    private static class RoundDrawable extends GradientDrawable {
        private boolean mIsStadium = false;

        private ColorStateList mSolidColors;
        private int mFillColor;

        public RoundDrawable(boolean isStadium) {
            mIsStadium = isStadium;
        }

        public void setSolidColors(ColorStateList colors) {
            mSolidColors = colors;
            setColor(colors.getDefaultColor());
        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            if (mIsStadium) {
                RectF rect = new RectF(getBounds());
                setCornerRadius((rect.height() > rect.width() ? rect.width() : rect.height()) / 2);
            }
        }

        @Override
        public void setColor(int argb) {
            mFillColor = argb;
            super.setColor(argb);
        }

        @Override
        protected boolean onStateChange(int[] stateSet) {
            if (mSolidColors != null) {
                final int newColor = mSolidColors.getColorForState(stateSet, 0);
                if (mFillColor != newColor) {
                    setColor(newColor);
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean isStateful() {
            return super.isStateful() || (mSolidColors != null && mSolidColors.isStateful());
        }
    }

    public void setTextNormalColor(int color) {
        createColorStateList(color, color, color);
    }

    public void setStrokeColor(int color, int strokeWidth) {
        this.strokeWidth = strokeWidth;
        this.strokeColor = color;
        RoundDrawable rdNormal = new RoundDrawable(false);
        rdNormal.setStroke(strokeWidth, color);
    }
}
