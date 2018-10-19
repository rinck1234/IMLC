package vip.rinck.imlc.factory.presenter.group;

import java.util.List;

import vip.rinck.imlc.factory.Factory;
import vip.rinck.imlc.factory.data.helper.GroupHelper;
import vip.rinck.imlc.factory.model.db.view.MemberUserModel;
import vip.rinck.imlc.factory.presenter.BaseRecyclerPresenter;

public class GroupMembersPresenter extends BaseRecyclerPresenter<MemberUserModel,GroupMemberContract.View>
implements GroupMemberContract.Presenter{


    public GroupMembersPresenter(GroupMemberContract.View view) {
        super(view);
    }

    @Override
    public void refresh() {
        //显示Loading
        start();

        //异步加载
        Factory.runOnAsync(loader);
    }

    private Runnable loader = new Runnable() {
        @Override
        public void run() {

            GroupMemberContract.View view = getView();
            if(view==null)
                return;

            String groupId = view.getmGroupId();

            //传递数量为-1 查询所有
            List<MemberUserModel> models = GroupHelper.getMemberUsers(groupId,-1);

            refreshData(models);
        }
    };
}
