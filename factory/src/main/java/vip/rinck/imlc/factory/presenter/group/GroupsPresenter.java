package vip.rinck.imlc.factory.presenter.group;

import android.support.v7.util.DiffUtil;

import java.util.List;

import vip.rinck.imlc.factory.data.group.GroupsRepository;
import vip.rinck.imlc.factory.data.group.GroupsDataSource;
import vip.rinck.imlc.factory.data.helper.GroupHelper;
import vip.rinck.imlc.factory.model.db.Group;
import vip.rinck.imlc.factory.presenter.BaseSourcePresenter;
import vip.rinck.imlc.factory.utils.DiffUiDataCallback;

/**
 * 群组Presenter实现
 */
public class GroupsPresenter extends BaseSourcePresenter<Group,Group,GroupsDataSource,GroupsContract.View>
implements GroupsContract.Presenter{

    public GroupsPresenter(GroupsContract.View view) {
        super(new GroupsRepository(), view);
    }

    @Override
    public void start() {
        super.start();
        //加载网络数据，可优化到下拉刷新中
        GroupHelper.refreshGroups();
    }

    @Override
   public void onDataLoaded(List<Group> groups) {
        final GroupsContract.View view = getView();
        if(view==null)
            return;

        List<Group> old = view.getRecyclerAdapter().getItems();

        DiffUiDataCallback<Group> callback = new DiffUiDataCallback<>(old,groups);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        //界面刷新
        refreshData(result,groups);
    }
}
