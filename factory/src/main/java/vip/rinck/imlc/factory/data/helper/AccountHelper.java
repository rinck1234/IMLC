package vip.rinck.imlc.factory.data.helper;

import android.text.TextUtils;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vip.rinck.imlc.factory.Factory;
import vip.rinck.imlc.factory.R;
import vip.rinck.imlc.factory.data.DataSource;
import vip.rinck.imlc.factory.model.api.RspModel;
import vip.rinck.imlc.factory.model.api.account.AccountRspModel;
import vip.rinck.imlc.factory.model.api.account.LoginModel;
import vip.rinck.imlc.factory.model.api.account.RegisterModel;
import vip.rinck.imlc.factory.model.db.AppDatabase;
import vip.rinck.imlc.factory.model.db.User;
import vip.rinck.imlc.factory.net.Network;
import vip.rinck.imlc.factory.net.RemoteService;
import vip.rinck.imlc.factory.persistence.Account;

public class AccountHelper {


    /**
     * 注册的接口
     *
     * @param model    传递一个注册的Model进来
     * @param callback 成功与失败的接口回送
     */
    public static void register(RegisterModel model, final DataSource.Callback<User> callback) {
        //调用Retrofit对网络请求接口作代理
        RemoteService service = Network.remote();

        Call<RspModel<AccountRspModel>> call = service.accountRegister(model);
        call.enqueue(new AccountRspCallback(callback));
    }

    /**
     * 登录的调用
     * @param model 登录的model
     * @param callback 成功与失败的回调
     */
    public static void login(LoginModel model, final DataSource.Callback<User> callback) {
        //调用Retrofit对网络请求接口作代理
        RemoteService service = Network.remote();

        Call<RspModel<AccountRspModel>> call = service.accountLogin(model);
        call.enqueue(new AccountRspCallback(callback));
    }

    /**
     * 对设备Id进行绑定
     *
     * @param callback
     */
    public static void bindPush(final DataSource.Callback<User> callback) {
        String pushId = Account.getPushId();
        //检查是否为空
        if (TextUtils.isEmpty(pushId))
            return;

        //调用Retrofit对我们的网络请求接口做代理
        RemoteService service = Network.remote();
        Call<RspModel<AccountRspModel>> call = service.accountBind(pushId);
        call.enqueue(new AccountRspCallback(callback));
    }

    /**
     * 请求的回调部分封装
     */
    private static class AccountRspCallback implements Callback<RspModel<AccountRspModel>> {

        final DataSource.Callback<User> callback;

        AccountRspCallback(DataSource.Callback<User> callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<RspModel<AccountRspModel>> call, Response<RspModel<AccountRspModel>> response) {
            //请求成功返回
            //从返回中得到全局model，内部使用Gson解析
            RspModel<AccountRspModel> rspModel = response.body();
            if (rspModel.success()) {
                //拿到实体
                AccountRspModel accountRspModel = rspModel.getResult();

                //获取我的信息
                User user = accountRspModel.getUser();
                DbHelper.save(User.class,user);
                //第一种，直接保存
                //user.save();
                /*
                //第二种，通过ModelAdapter
                FlowManager.getModelAdapter(User.class)
                        .save(user);

                //第三种，事务中
                DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
                definition.beginTransactionAsync(new ITransaction() {
                    @Override
                    public void execute(DatabaseWrapper databaseWrapper) {
                        FlowManager.getModelAdapter(User.class)
                                .save(user);
                    }
                }).build().execute();
                */
                //同步到XML持久化中
                Account.login(accountRspModel);
                //判断绑定状态，是否绑定设备
                if (accountRspModel.isBind()) {
                    //设置绑定状态
                    Account.setBind(true);
                    //然后返回
                    if (callback != null)
                        callback.onDataLoaded(user);
                } else {
                    //进行绑定的唤起
                    bindPush(callback);
                }

            } else {
                //错误解析
                Factory.decodeRspCode(rspModel, callback);
            }
        }

        @Override
        public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
            //网络请求失败
            if (callback != null)
                callback.onDataNotAvailable(R.string.data_network_error);
        }
    }
}
