package vip.rinck.imlc.factory;

import android.support.annotation.StringRes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import vip.rinck.imlc.common.app.Application;
import vip.rinck.imlc.factory.data.DataSource;
import vip.rinck.imlc.factory.model.api.RspModel;
import vip.rinck.imlc.factory.persistence.Account;
import vip.rinck.imlc.factory.utils.DBFlowExclusionStrategy;

public class Factory {

    private static final Factory instance;
    //全局的线程池
    private final Executor executor;
    //全局的Gson
    private final Gson gson;

    static {
        instance = new Factory();
    }

    private Factory(){
        //新建一个4线程的线程池
        executor = Executors.newFixedThreadPool(4);
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")//设置时间格式
                //设置一个过滤器，数据库级别的Model不进行Json转换
                .setExclusionStrategies(new DBFlowExclusionStrategy())
                .create();
    }

    /**
     * Factory中的初始化
     */
    public static void setup(){
        //初始化数据库
        FlowManager.init(new FlowConfig.Builder(app())
        .openDatabasesOnInit(true)//数据库初始化时就打开
        .build());

        //持久化的数据进行初始化
        Account.load(app());
    }

    /**
     * 返回全局的Application
     * @return
     */
    public static Application app(){
        return Application.getInstance();
    }


    /**
     * 异步运行的方法
     * @param runnable
     */
    public static void runOnAsync(Runnable runnable){
        //拿到单例，拿到线程池，异步执行
        instance.executor.execute(runnable);
    }

    public static Gson getGson(){
        return instance.gson;
    }

    /**
     * 进行错误Code的解析，把服务器返回的Code值返回为一个String资源
     * @param model
     * @param callback 返回一个错误的资源Id
     */
    public static void decodeRspCode(RspModel model, DataSource.FailedCallback callback){
        if(model==null){
            return;
        }

        switch (model.getCode()){
            case RspModel.SUCCEED:
                return;
            case RspModel.ERROR_SERVICE:
                decodeRspCode(R.string.data_rsp_error_service,callback);
                break;
            case RspModel.ERROR_NOT_FOUND_USER:
                decodeRspCode(R.string.data_rsp_error_not_found_user, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP:
                decodeRspCode(R.string.data_rsp_error_not_found_group, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP_MEMBER:
                decodeRspCode(R.string.data_rsp_error_not_found_group_member, callback);
                break;
            case RspModel.ERROR_CREATE_USER:
                decodeRspCode(R.string.data_rsp_error_create_user, callback);
                break;
            case RspModel.ERROR_CREATE_GROUP:
                decodeRspCode(R.string.data_rsp_error_create_group, callback);
                break;
            case RspModel.ERROR_CREATE_MESSAGE:
                decodeRspCode(R.string.data_rsp_error_create_message, callback);
                break;
            case RspModel.ERROR_PARAMETERS:
                decodeRspCode(R.string.data_rsp_error_parameters, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_ACCOUNT:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_account, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_NAME:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_name, callback);
                break;
            case RspModel.ERROR_ACCOUNT_TOKEN:
                Application.showToast(R.string.data_rsp_error_account_token);
                instance.logout();
                break;
            case RspModel.ERROR_ACCOUNT_LOGIN:
                decodeRspCode(R.string.data_rsp_error_account_login, callback);
                break;
            case RspModel.ERROR_ACCOUNT_REGISTER:
                decodeRspCode(R.string.data_rsp_error_account_register, callback);
                break;
            case RspModel.ERROR_ACCOUNT_NO_PERMISSION:
                decodeRspCode(R.string.data_rsp_error_account_no_permission, callback);
                break;
            case RspModel.ERROR_UNKNOWN:
            default:
                decodeRspCode(R.string.data_rsp_error_unknown, callback);
                break;
        }
    }

    private static void decodeRspCode(@StringRes int resId,DataSource.FailedCallback callback){
        if(callback!=null)
            callback.onDataNotAvailable(resId);
    }

    /**
     * 收到账户退出的消息需要退出
     */
    private void logout(){

    }

    /**
     * 处理推送来的消息
     * @param message
     */
    public static void dispatchPush(String message){

    }
}
