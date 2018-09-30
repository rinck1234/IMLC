package vip.rinck.imlc.common.app;

import android.content.Context;

import vip.rinck.imlc.factory.presenter.BaseContract;

public abstract class PresenterFragment<Presenter extends BaseContract.Presenter> extends Fragment implements BaseContract.View<Presenter> {

    protected Presenter mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //在界面Attach之后触发初始化
        initPresenter();
    }

    protected abstract Presenter initPresenter();

    @Override
    public void showError(int str) {
        //显示错误，优先使用占位布局
        if(mPlaceholderView!=null){
            mPlaceholderView.triggerError(str);
        }else {
            Application.showToast(str);
        }
    }

    @Override
    public void showLoading() {
        //TODO 显示一个Loading
        if(mPlaceholderView!=null){
            mPlaceholderView.triggerLoading();
        }
    }

    @Override
    public void setPresenter(Presenter presenter) {
        //View中赋值Presenter
        mPresenter = presenter;
    }
}
