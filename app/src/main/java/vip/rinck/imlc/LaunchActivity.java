package vip.rinck.imlc;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Property;
import android.view.View;

import net.qiujuer.genius.res.Resource;
import net.qiujuer.genius.ui.compat.UiCompat;

import java.util.jar.Manifest;

import pub.devrel.easypermissions.AppSettingsDialog;
import vip.rinck.imlc.activities.AccountActivity;
import vip.rinck.imlc.activities.MainActivity;
import vip.rinck.imlc.factory.persistence.Account;
import vip.rinck.imlc.fragments.assist.PermissionsFragment;
import vip.rinck.imlc.helper.PermissionHelper;

public class LaunchActivity extends vip.rinck.imlc.common.app.Activity {

    private String[] perms;
    private ColorDrawable mBgDrawable;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        //拿到根布局
        View root = findViewById(R.id.activity_launch);
        //获取颜色
        int color = UiCompat.getColor(getResources(),R.color.colorPrimary);
        //创建一个Drawable
        ColorDrawable drawable = new ColorDrawable(color);
        //设置给背景
        root.setBackground(drawable);
        mBgDrawable = drawable;
    }



    @Override
    protected void initData() {
        super.initData();
        //动画进入到50%等待PushID获取到
        startAnim(0.8f, new Runnable() {
            @Override
            public void run() {
                waitPushReceiverId();
            }
        });

    }

    /**
     * 等待个推框架对我们的PushId设置好值
     */
    private void waitPushReceiverId(){
        if(Account.isLogin()){
            //已经登录，判断是否绑定
            //如果没有绑定则等待广播接收器进行绑定
            if(Account.isBind()){
                skip();
                return;
            }
        }else {
            //没有登录
            //如果拿到了PushId,此时是不能绑定PushId的
            if(!TextUtils.isEmpty(Account.getPushId())){
                //跳转
                skip();
                return;
            }
        }



        //循环等待
        getWindow().getDecorView()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        waitPushReceiverId();
                    }
                },500);

    }

    /**
     * 在跳转之前要把剩下的50%完成
     */
    private void skip(){
        startAnim(1f, new Runnable() {
            @Override
            public void run() {
                reallySkip();
            }
        });

    }

    private void reallySkip(){

        perms = new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE};
        if(!PermissionHelper.checkpermission(this,perms)){
            PermissionHelper.applypermission(this,this,perms);
        }else{
            whereto();
        }
    }

    private void whereto(){
        //检查跳转到主页还是登录页
        if(Account.isLogin()){
            MainActivity.show(this);
        }else {
            AccountActivity.show(this);
        }
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(!PermissionHelper.checkpermission(this,perms)){
            finish();
        }else{
            whereto();
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
            whereto();
        }
    }

    /**
     * 给背景设置一个动画
     * @param endProgress 动画的结束进度
     * @param endCallback 动画结束时触发
     */
    private void startAnim(float endProgress,final Runnable endCallback){
        //获取一个结束的颜色
        int finalColor = Resource.Color.WHITE;
        //运算当前进度的颜色
        ArgbEvaluator evaluator = new ArgbEvaluator();
        int endColor = (int)evaluator.evaluate(endProgress,mBgDrawable.getColor(),finalColor);
        //构建一个属性动画
        ValueAnimator valueAnimator = ObjectAnimator.ofObject(this,property,evaluator,endColor);
        valueAnimator.setDuration(1500);
        valueAnimator.setIntValues(mBgDrawable.getColor(),endColor);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                endCallback.run();
            }
        });
        valueAnimator.start();
    }

    private Property<LaunchActivity,Object> property = new Property<LaunchActivity, Object>(Object.class,"color") {
        @Override
        public Object get(LaunchActivity object) {
            return object.mBgDrawable.getColor();
        }

        @Override
        public void set(LaunchActivity object, Object value) {
            object.mBgDrawable.setColor((Integer)value);
        }
    };

}
