package vip.rinck.imlc;

import com.igexin.sdk.PushManager;

import vip.rinck.imlc.common.app.Application;
import vip.rinck.imlc.factory.Factory;

public class App extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        //调用Factory进行初始化
        Factory.setup();
        //推送进行初始化
        PushManager.getInstance().initialize(this);
    }
}
