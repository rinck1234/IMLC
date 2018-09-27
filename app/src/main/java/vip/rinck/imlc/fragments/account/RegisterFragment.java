package vip.rinck.imlc.fragments.account;

import android.content.Context;
import android.widget.EditText;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;
import vip.rinck.imlc.R;
import vip.rinck.imlc.activities.MainActivity;
import vip.rinck.imlc.common.app.Fragment;
import vip.rinck.imlc.common.app.PresenterFragment;
import vip.rinck.imlc.factory.presenter.account.RegisterContract;
import vip.rinck.imlc.factory.presenter.account.RegisterPresenter;

/**
 * 注册界面
 */
public class RegisterFragment extends PresenterFragment<RegisterContract.Presenter> implements RegisterContract.View{

    private AccountTrigger mAccountTrigger;

    @BindView(R.id.et_phone)
    EditText mPhone;
    @BindView(R.id.et_username)
    EditText mUsername;
    @BindView(R.id.et_password)
    EditText mPassword;

    @BindView(R.id.loading)
    Loading mLoading;

    @BindView(R.id.btn_submit)
    Button mSubmit;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //拿到Activity的引用
        mAccountTrigger = (AccountTrigger)context;
    }

    /**
     * 初始化Presenter
     * @return
     */
    @Override
    protected RegisterContract.Presenter initPresenter() {
        return new RegisterPresenter(this);
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick(){
        String phone = mPhone.getText().toString();
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();
        //调用P层进行注册
        mPresenter.register(phone,username,password);
    }

    @OnClick(R.id.tv_go_login)
    void onShowLoginClick(){
        //让AccountActivity进行界面切换
        mAccountTrigger.triggerView();
    }

    @Override
    public void showError(int str) {
        super.showError(str);
        //当需要显示错误的时候触发，一定是结束了

        //停止Loading
        mLoading.stop();
        mPhone.setEnabled(true);
        mUsername.setEnabled(true);
        mPassword.setEnabled(true);
        //提交按钮可点击
        mSubmit.setEnabled(true);
    }

    @Override
    public void showLoading() {
        super.showLoading();
        //正在进行注册，界面不可操作
        //开始Loading
        mLoading.start();
        mPhone.setEnabled(false);
        mUsername.setEnabled(false);
        mPassword.setEnabled(false);
        //提交按钮禁止点击
        mSubmit.setEnabled(false);
    }

    @Override
    public void registerSuccess() {
        //注册成功，这个时候账户已经登录
        //跳转到MainActivity
        MainActivity.show(getContext());
        getActivity().finish();
    }

}
