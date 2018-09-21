package vip.rinck.imlc;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.jar.Manifest;

import pub.devrel.easypermissions.AppSettingsDialog;
import vip.rinck.imlc.activities.MainActivity;
import vip.rinck.imlc.fragments.assist.PermissionsFragment;
import vip.rinck.imlc.helper.PermissionHelper;

public class LaunchActivity extends vip.rinck.imlc.common.app.Activity {

    private String[] perms;


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initData() {
        super.initData();
        perms = new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE};
        if(!PermissionHelper.checkpermission(this,perms)){
            PermissionHelper.applypermission(this,this,perms);
        }else{
            MainActivity.show(this);
            finish();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(!PermissionHelper.checkpermission(this,perms)){
            finish();
        }else{
            MainActivity.show(this);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean flag = true;
        for(int i=0;i<grantResults.length;i++){
            if(grantResults[i]!= PackageManager.PERMISSION_GRANTED){
                new AppSettingsDialog.Builder(this,"在设置-应用-失联权限中开启存储空间权限，以正常使用失联功能").setTitle("权限申请").setPositiveButton("去设置").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).build().show();
                flag = false;
                break;
            }
        }
        if(flag){
            MainActivity.show(this);
            finish();
        }
    }
/*
    @Override
    protected void onResume() {
        super.onResume();
        if(!PermissionHelper.checkpermission(this,perms)){
            finish();
        }else{
            MainActivity.show(this);
            finish();
        }

    }*/
}
