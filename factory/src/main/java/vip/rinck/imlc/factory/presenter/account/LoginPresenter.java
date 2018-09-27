package vip.rinck.imlc.factory.presenter.account;

import android.text.TextUtils;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import vip.rinck.imlc.factory.R;
import vip.rinck.imlc.factory.data.DataSource;
import vip.rinck.imlc.factory.data.helper.AccountHelper;
import vip.rinck.imlc.factory.model.api.account.LoginModel;
import vip.rinck.imlc.factory.model.db.User;
import vip.rinck.imlc.factory.persistence.Account;
import vip.rinck.imlc.factory.presenter.BasePresenter;

/**
 * 登录的逻辑实现
 */
public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter, DataSource.Callback<User>{
    public LoginPresenter(LoginContract.View view) {
        super(view);
    }

    @Override
    public void login(String phone, String password) {
        start();
        final LoginContract.View view = getView();
        if(TextUtils.isEmpty(phone)||TextUtils.isEmpty((password))){
            view.showError(R.string.data_account_login_invalid_parameter);
        }else{
            //尝试传递PushId
            LoginModel model = new LoginModel(phone,password, Account.getPushId());
            AccountHelper.login(model,this);
        }

    }

    @Override
    public void onDataLoaded(User user) {
        final LoginContract.View view = getView();
        if(view==null)
            return;
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.loginSuccess();
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final LoginContract.View view = getView();
        if(view==null)
            return;
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.showError(strRes);
            }
        });
    }
}
