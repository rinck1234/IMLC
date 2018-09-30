package vip.rinck.imlc.common.app;

import android.content.Context;

import vip.rinck.imlc.factory.presenter.BaseContract;

public abstract class PresenterToolbarActivity<Presenter extends BaseContract.Presenter> extends ToolbarActivity
implements BaseContract.View<Presenter>{
    protected Presenter mPresenter;


    @Override
    protected void initBefore() {
        super.initBefore();
        initPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //界面关闭时进行销毁
        if(mPresenter!=null)
            mPresenter.destory();
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

    protected void hideLoading(){
        if(mPlaceholderView!=null){
            mPlaceholderView.triggerOk();
        }
    }

    @Override
    public void setPresenter(Presenter presenter) {
        //View中赋值Presenter
        mPresenter = presenter;
    }
}