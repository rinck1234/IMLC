package vip.rinck.imlc.fragments.assist;


import android.Manifest;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import vip.rinck.imlc.R;
import vip.rinck.imlc.fragments.media.GalleryFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class PermissionsFragment extends BottomSheetDialogFragment implements EasyPermissions.PermissionCallbacks {


    public PermissionsFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //返回一个我们重写的
        return new GalleryFragment.TransStatusBottomSheetDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 获取布局中的控件
        View root =  inflater.inflate(R.layout.fragment_permissions, container, false);

        root.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPerms=new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPerm();
            }
        });

        return root;
    }

    public boolean beginPermissionRequest(String[] perms){
        requestPerms=perms;
        requestPerm();
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshState(getView());

    }

    /**
     * 刷新布局中的图片的状态
     * @param root 根布局
     */
    private void refreshState(View root){
        if (root == null) {
            return;
        }
        Context context = getContext();
        root.findViewById(R.id.iv_state_permission_network).setVisibility(haveNetwork(context)?View.VISIBLE:View.GONE);
        root.findViewById(R.id.iv_state_permission_read).setVisibility(haveRead(context)?View.VISIBLE:View.GONE);
        root.findViewById(R.id.iv_state_permission_write).setVisibility(haveWrite(context)?View.VISIBLE:View.GONE);
        root.findViewById(R.id.iv_state_permission_record_audio).setVisibility(haveRecord(context)?View.VISIBLE:View.GONE);
    }

    /**
     * 获取是否有网络权限
     * @param context 上下文
     * @return True则有
     */
    private static boolean haveNetwork(Context context){
        String[] perms = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE
        };
        return EasyPermissions.hasPermissions(context,perms);
    }

    private static boolean haveRead(Context context){
        String[] perms = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        return EasyPermissions.hasPermissions(context,perms);
    }

    private static boolean haveWrite(Context context){
        String[] perms = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        return EasyPermissions.hasPermissions(context,perms);
    }

    private static boolean haveRecord(Context context){
        String[] perms = new String[]{
                Manifest.permission.RECORD_AUDIO
        };
        return EasyPermissions.hasPermissions(context,perms);
    }

    //私有的show方法
    private static void show(FragmentManager fragmentManager){
        //调用BottomSheetDialogFragment以及准备好的显示方法
        new PermissionsFragment()
                .show(fragmentManager,PermissionsFragment.class.getName());
    }



    /**
     * 检查是否具有所有的权限
     * @param context
     * @param fragmentManager
     * @return
     */
    public static boolean haveAll(Context context,FragmentManager fragmentManager){
        boolean haveAll = haveNetwork(context)
                &&haveRead(context)
                &&haveWrite(context)
                &&haveRecord(context);
        if(!haveAll){
            show(fragmentManager);
        }
        return haveAll;
    }

    private String[] requestPerms;


    private static final int RC = 0x0100;
    /**
     * 申请权限
     */
    @AfterPermissionGranted(RC)
    private void requestPerm(){
        if(EasyPermissions.hasPermissions(getContext(),requestPerms)){
            vip.rinck.imlc.common.app.Application.showToast(R.string.label_permission_ok);
            //Fragment中得到根布局，前提是在onCreateView方法后
            refreshState(getView());
        }else{
            EasyPermissions.requestPermissions(this,getString(R.string.title_assist_permissions),
                   RC,requestPerms );
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        //如果权限没有申请成功，则弹出到设置 用户自行授权
        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            new AppSettingsDialog
                    .Builder(this)
                    .build()
                    .show();
        }
    }

    /**
     * 权限申请时回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //传递对应的参数，并告知接收权限的处理者是自己
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }
}
