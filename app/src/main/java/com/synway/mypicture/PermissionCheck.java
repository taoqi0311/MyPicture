package com.synway.mypicture;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by dell on 2017/4/12.
 */

public class PermissionCheck {

    //需要申请的运行时权限
    private static final String[] CHECK_PERMISSION_LIST = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,//读写存储卡,
//            Manifest.permission.ACCESS_FINE_LOCATION,//位置信息
//            Manifest.permission.READ_PHONE_STATE,//读取手机信息
//            Manifest.permission.RECORD_AUDIO,//录音
//            Manifest.permission.CAMERA//摄像头
//            Manifest.permission.SYSTEM_ALERT_WINDOW
    };

    public static  boolean checkPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //判断应用程序需要的权限
            for (int i = 0; i < CHECK_PERMISSION_LIST.length; i++) {
                if (ContextCompat.checkSelfPermission(context, CHECK_PERMISSION_LIST[i]) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static final void requestPermission(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity, CHECK_PERMISSION_LIST, requestCode);
    }
}
