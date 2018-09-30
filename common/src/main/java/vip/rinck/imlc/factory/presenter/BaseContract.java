package vip.rinck.imlc.factory.presenter;

import android.support.annotation.StringRes;

import vip.rinck.imlc.common.widget.recycler.RecyclerAdapter;

/**
 * MVP模式中公共的基本契约
 */
public interface BaseContract {
    //基本的界面职责

    interface View<T extends Presenter> {

        void showError(@StringRes int str);

        //显示进度条
        void showLoading();

        //支持设置一个Presenter
        void setPresenter(T presenter);
    }

    interface Presenter {

        //共用的开始
        void start();

        //共用的销毁
        void destory();
    }

    //基本的列表View的职责
    interface RecyclerView<T extends Presenter, ViewMode> extends View<T>{
        //拿到一个适配器，然后进行局部刷新
        RecyclerAdapter<ViewMode> getRecyclerAdapter();


        //当数据更改后触发
        void onAdapterDataChanged();
    }

}
