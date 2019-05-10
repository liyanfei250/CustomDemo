package com.example.customdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import com.example.customdemo.R;


/**
 * Created by zhangjunzhong on 2018/3/23.
 */

public class PasswordEditText extends LinearLayout {
    private CheckedTextView cbPasswordSwitch;
    private ClearEditText editPassword;

    public PasswordEditText(Context context) {
        this(context, null);
    }

    public PasswordEditText(final Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.passwordedit);
        String hint = a.getString(R.styleable.passwordedit_hintedit);
        final int maxLength = a.getInt(R.styleable.passwordedit_maxLengthedit, 50);
        final int inputType = a.getInt(R.styleable.passwordedit_inputTypeedit, InputType.TYPE_CLASS_TEXT);
        boolean hide = a.getBoolean(R.styleable.passwordedit_hideedit, false);
        String text = a.getString(R.styleable.passwordedit_defalutText);

        Drawable drawableLeft = a.getDrawable(R.styleable.passwordedit_leftdrawableicon);
        Drawable drawableRight = a.getDrawable(R.styleable.passwordedit_rightdrawable);
        a.recycle();

        View view = LayoutInflater.from(context).inflate(R.layout.view_password, this, true);
        cbPasswordSwitch = view.findViewById(R.id.cbPasswordSwitch);
        editPassword = view.findViewById(R.id.editPassword);
        if (drawableLeft != null) {
//            drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());// 设置图片宽高
            drawableLeft.setBounds(0, 0, drawableLeft.getIntrinsicWidth(), drawableLeft.getIntrinsicHeight());
            editPassword.setCompoundDrawables(drawableLeft, null, null, null);// 设置到控件中
            editPassword.setCompoundDrawablePadding(36);
        }
        editPassword.setText(text);
        editPassword.setHint(hint);
        editPassword.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        cbPasswordSwitch.setChecked(hide);
        if (drawableRight!=null){
            cbPasswordSwitch.setCheckMarkDrawable(drawableRight);
        }
        cbPasswordSwitch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cbPasswordSwitch.toggle();
                editPassword.passwordShowOrHide(inputType, cbPasswordSwitch.isChecked());
            }
        });
        editPassword.passwordShowOrHide(inputType, cbPasswordSwitch.isChecked());
    }

    public ClearEditText getEditText() {
        return editPassword;
    }

    public Editable getText() {
        return editPassword.getText();
    }

    public void setSelection(int len) {
        editPassword.setSelection(len);
    }
}