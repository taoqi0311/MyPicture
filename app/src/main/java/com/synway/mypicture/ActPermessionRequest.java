package com.synway.mypicture;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class ActPermessionRequest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("申请权限界面");
        boolean permission = PermissionCheck.checkPermission(this);
        if (!permission) {
            PermissionCheck.requestPermission(this, 200);
        } else {
            finish();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isAllPermissionGet = true;

        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                isAllPermissionGet = false;
                break;
            }
        }
        if (isAllPermissionGet) {
            Intent intent = new Intent();
            intent.setClass(ActPermessionRequest.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("提示").setMessage("抱歉,因为您拒绝了某些权限,程序无法运行...").
                    setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        }
    }
}
