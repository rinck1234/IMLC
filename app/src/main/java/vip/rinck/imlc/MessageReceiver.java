package vip.rinck.imlc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.igexin.sdk.PushConsts;

import vip.rinck.imlc.factory.Factory;
import vip.rinck.imlc.factory.data.helper.AccountHelper;
import vip.rinck.imlc.factory.persistence.Account;

/**
 * 个推的消息接收器
 */
public class MessageReceiver extends BroadcastReceiver {

    private static final String TAG = MessageReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent==null){
            return;
        }
        Bundle bundle = intent.getExtras();
        //判断当前消息的意图
        switch (bundle.getInt(PushConsts.CMD_ACTION)){
            case PushConsts.GET_CLIENTID:{
                Log.i(TAG,"GET_CLIENTID:"+bundle.toString());
                //当Id初始化的时候
                //获取设备ID
                onClientInit(bundle.getString("clientid"));
                break;
            }
            case PushConsts.GET_MSG_DATA:{
                //常规的消息送达
                byte[] payload = bundle.getByteArray("payload");
                if(payload!=null){
                    String message = new String(payload);
                    Log.i(TAG,"GET_MSG_DATA:"+message);
                    onMessageArrived(message);
                }
                break;
            }
            default:
                Log.i(TAG,"OTHER:"+bundle.toString());
                break;
        }
    }

    /**
     * 当Id初始化的时候
     * @param cid 设备Id
     */
    private void onClientInit(String cid){
        Account.setPushId(cid);
        if(Account.isLogin()){
            //账户登录状态，进行一次PushId绑定
            //没有登录不能绑定
            AccountHelper.bindPush(null);
        }
    }

    /**
     * 消息到达时
     * @param message 新消息
     */
    private void onMessageArrived(String message){
        Factory.dispatchPush(message);
    }
}
