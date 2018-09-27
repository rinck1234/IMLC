package vip.rinck.imlc.factory.presenter.account;

import android.text.TextUtils;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.regex.Pattern;

import vip.rinck.imlc.common.Common;
import vip.rinck.imlc.factory.R;
import vip.rinck.imlc.factory.data.DataSource;
import vip.rinck.imlc.factory.data.helper.AccountHelper;
import vip.rinck.imlc.factory.model.api.account.RegisterModel;
import vip.rinck.imlc.factory.model.db.User;
import vip.rinck.imlc.factory.persistence.Account;
import vip.rinck.imlc.factory.presenter.BasePresenter;

public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter,DataSource.Callback<User>{
    public RegisterPresenter(RegisterContract.View view) {
        super(view);
    }

    @Override
    public void register(String phone, String username, String password) {
        //调用开始方法，默认启动Loading
        start();
        //得到View接口
        RegisterContract.View view = getView();

        if(!checkMobile(phone)){
            //提示
            view.showError(R.string.data_account_register_invalid_parameter_mobile);
        }else if(username.length()<2){
            view.showError(R.string.data_account_register_invalid_parameter_name);
        }else if(password.length()<6){
            view.showError(R.string.data_account_register_invalid_parameter_password);
        }else{
            //进行网络请求

            //构造Model，进行请求调用
            RegisterModel model = new RegisterModel(phone,password, username, Account.getPushId());
            //进行网络请求，并设置回送接口为自己
            AccountHelper.register(model, this);
        }
    }


    /**
     * 检查手机号是否合法
     * @param phone
     * @return 合法为true
     */
    @Override
    public boolean checkMobile(String phone) {
        //手机号不为空，并且满足格式
        return !TextUtils.isEmpty(phone)&& Pattern.matches(Common.Constance.REGEX_MOBILE,phone);
    }

    @Override
    public void onDataLoaded(User user) {
        //当网络请求成功，注册好了，回送一个用户信息回来
        //告知界面，注册成功
        final RegisterContract.View view = getView();
        if (view == null)
            return;
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                //调用主界面注册成功
                view.registerSuccess();
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final RegisterContract.View view = getView();
        if(view==null)
            return;
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                //主界面显示失败信息
                view.showError(strRes);
            }
        });
    }
}
