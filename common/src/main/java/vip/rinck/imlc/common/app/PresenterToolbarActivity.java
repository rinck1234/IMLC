package vip.rinck.imlc.common.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.tv.TvContract;
import android.widget.ProgressBar;

import vip.rinck.imlc.common.R;
import vip.rinck.imlc.common.widget.PortraitView;
import vip.rinck.imlc.factory.presenter.BaseContract;

public abstract class PresenterToolbarActivity<Presenter extends BaseContract.Presenter> extends ToolbarActivity
implements BaseContract.View<Presenter>{
    protected Presenter mPresenter;

    protected ProgressDialog mLoadingDialog;

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
        //无论如何 先隐藏
        hideDialogLoading();
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
        }else {
            ProgressDialog dialog = mLoadingDialog;
            if(dialog==null){
                dialog = new ProgressDialog(this,R.style.AppTheme_Dialog_Alert_Light);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(true);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                });
                mLoadingDialog = dialog;
            }
            dialog.setMessage(getText(R.string.prompt_loading));
            dialog.show();

        }
    }

    protected void hideDialogLoading(){
        ProgressDialog dialog = mLoadingDialog;
        if(dialog!=null){
            mLoadingDialog=null;
            dialog.dismiss();
        }
    }

    protected void hideLoading(){
        //无论如何先隐藏
        hideDialogLoading();
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