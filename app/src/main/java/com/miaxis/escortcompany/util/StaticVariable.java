package com.miaxis.escortcompany.util;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.miaxis.escortcompany.app.EscortCompanyApp;

import java.net.FileNameMap;
import java.net.URLConnection;

/**
 * Created by 一非 on 2018/4/17.
 */

public class StaticVariable {
    public static final String CONFIG = "config";
    public static final String WORKER = "worker";
    public static final String SUCCESS = "200";
    public static final String FAILED = "400";
    public static final String LOGIN_SUCCESS = "login_success";
    public static final String FLAG = "flag";
    public static final int FINGER1ST = 1;
    public static final int FINGER2ND = 2;

    public static String upTaskTypeTurnToString(String type) {
        if ("常规网点接箱".equals(type)) {
            return "1";
        } else if ("常规网点送箱".equals(type)) {
            return "2";
        } else if ("临时网点接箱".equals(type)) {
            return "3";
        } else if ("临时网点送箱".equals(type)) {
            return "4";
        }
        return "0";
    }

    public static String getTasktypeName(String tasktype, String tasklevel) {
        if (tasktype.equals("1")) {
            if(!tasklevel.equals("1")){
                return "常规接箱";
            }else{
                return "出库";
            }
        } else if (tasktype.equals("2")) {
            if(!tasklevel.equals("1")){
                return "常规送箱";
            }else{
                return "入库";
            }
        } else if (tasktype.equals("3")) {
            return "临时接箱";
        } else if (tasktype.equals("4")) {
            return "临时送箱";
        } else {
            return tasktype;
        }
    }

    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }

    public static String getVersion() {
        // 得到系统的包管理器。已经得到了apk的面向对象的包装
        PackageManager pm = EscortCompanyApp.getInstance().getApplicationContext().getPackageManager();
        try {
            // 参数一：当前应用程序的包名 参数二：可选的附加消息，这里我们用不到 ，可以定义为0
            PackageInfo info = pm.getPackageInfo(EscortCompanyApp.getInstance().getApplicationContext().getPackageName(), 0);
            // 返回当前应用程序的版本号
            return info.versionName;
        } catch (Exception e) {// 包名未找到的异常，理论上， 该异常不可能会发生
            e.printStackTrace();
            return "";
        }
    }

    public static String getMimeType(String fileUrl)
            throws java.io.IOException
    {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String type = fileNameMap.getContentTypeFor(fileUrl);

        return type;
    }

}
