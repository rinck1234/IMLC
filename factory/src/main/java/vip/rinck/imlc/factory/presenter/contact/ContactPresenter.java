package vip.rinck.imlc.factory.presenter.contact;

import android.support.v7.util.DiffUtil;

import java.util.List;

import vip.rinck.imlc.common.widget.recycler.RecyclerAdapter;
import vip.rinck.imlc.factory.data.DataSource;
import vip.rinck.imlc.factory.data.helper.UserHelper;
import vip.rinck.imlc.factory.data.user.ContactDataSource;
import vip.rinck.imlc.factory.data.user.ContactRepository;
import vip.rinck.imlc.factory.model.db.User;
import vip.rinck.imlc.factory.presenter.BaseRecyclerPresenter;
import vip.rinck.imlc.factory.presenter.BaseSourcePresenter;
import vip.rinck.imlc.factory.utils.DiffUiDataCallback;

/**
 * 联系人的Presenter实现
 */
public class ContactPresenter extends BaseSourcePresenter<User,User,ContactDataSource,ContactContract.View>
        implements ContactContract.Presenter,DataSource.SucceedCallback<List<User>>{


    public ContactPresenter(ContactContract.View view) {
        super(new ContactRepository(), view);

    }

    @Override
    public void start() {
        super.start();
        //加载网络数据
        UserHelper.refreshContacts();
    }


    private void diff(List<User> oldList, List<User> newList) {
        //进行数据对比
        DiffUtil.Callback callback = new DiffUiDataCallback<User>(oldList, newList);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        //在对比完成后进行数据的赋值
        getView().getRecyclerAdapter().replace(newList);

        //尝试刷新界面
        result.dispatchUpdatesTo(getView().getRecyclerAdapter());
        getView().onAdapterDataChanged();
    }

    //运行到这里时是子线程
    @Override
    public void onDataLoaded(List<User> users) {
        //最终数据都会通知到这里

        final ContactContract.View view = getView();
        if(view==null)
            return;
        RecyclerAdapter<User> adapter = view.getRecyclerAdapter();
        List<User> old = adapter.getItems();

        //进行数据对比
        DiffUtil.Callback callback = new DiffUiDataCallback<User>(old, users);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        //调用基类方法进行界面刷新
        refreshData(result,users);
    }

}
