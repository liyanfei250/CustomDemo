package com.example.customdemo.utils;

import android.content.Context;

/**
 *
 */
public class Utils {
    public static int dp2px(Context context, int dpValue) {
        return (int) context.getResources().getDisplayMetrics().density * dpValue;
    }
}
