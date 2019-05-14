package com.example.customdemo.utils;

import java.text.DecimalFormat;

public class NumUtil {

    public static String format(String d) {
        double data = 0.00;
        try {
            data = Double.parseDouble(d);
        } catch (Exception e) {
        }
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(data);
    }

    public static String format(double d) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(d);
    }

    public static String format(float d) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(d);
    }

    public static String formatPercent(double d) {
        DecimalFormat df = new DecimalFormat("0.00%");
        return df.format(d);
    }
    public static String formatPercentOne(double d) {
        DecimalFormat df = new DecimalFormat("0.0%");
        return df.format(d);
    }




}
