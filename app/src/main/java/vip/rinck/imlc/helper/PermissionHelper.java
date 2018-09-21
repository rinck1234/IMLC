package vip.rinck.imlc.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;


public class PermissionHelper extends Activity{


    public static boolean applypermission(Activity activity, Context context, String[] allpermissions){
        if(Build.VERSION.SDK_INT>=23){
                ActivityCompat.requestPermissions(activity,allpermissions,1);
        }
        return true;
    }

    public static boolean checkpermission(Context context,String[] allpermissions){
        for(int i=0;i<allpermissions.length;i++){
            int chechpermission= ContextCompat.checkSelfPermission(context,
                    allpermissions[i]);
            if(chechpermission!= PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }


}
