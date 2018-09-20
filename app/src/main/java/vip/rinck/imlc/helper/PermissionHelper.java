package vip.rinck.imlc.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import vip.rinck.imlc.MainActivity;


public class PermissionHelper extends Activity{


    public static boolean applypermission(Activity activity, Context context, String[] allpermissions){
        if(Build.VERSION.SDK_INT>=23){
            boolean needapply=false;
            for(int i=0;i<allpermissions.length;i++){
                int chechpermission= ContextCompat.checkSelfPermission(context,
                        allpermissions[i]);
                if(chechpermission!= PackageManager.PERMISSION_GRANTED){
                    needapply=true;
                }
            }
            if(needapply){
                ActivityCompat.requestPermissions(activity,allpermissions,1);
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean flag = false;
        for(int i=0;i<grantResults.length;i++){
            if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                flag=true;
                break;
            }
        }
        if(flag){
            Toast.makeText(this,"未完成授权 功能可能无法正常使用",Toast.LENGTH_LONG).show();
        }

    }
}
