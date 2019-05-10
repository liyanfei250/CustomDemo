package com.example.customdemo.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by zhangjunzhong on 2018/6/8.
 */

public class FilePath {
    public static String appSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator+"JuFengJY"+ File.separator;
    public static String appAppRootPath = "";

    public static String appDownLoadFilePath = getAppRootPath() +"apk";
    public static String fileName = getAppRootPath() +System.currentTimeMillis() + ".jpg";
    public static String fileImage = getAppRootPath() +"/Image";
    public static String appName = "jf9999.apk";

    public static void init(Context mContext){
        appAppRootPath = mContext.getFilesDir().getAbsolutePath()+ File.separator;
    }
    public static String getAppRootPath(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File f = new File(appSDCardPath);
            if (!f.exists()){
                f.mkdir();
            }
            return  appSDCardPath;
        }else {
            return appAppRootPath;
        }
    }
}
