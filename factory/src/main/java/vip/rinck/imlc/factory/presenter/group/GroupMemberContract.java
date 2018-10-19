package vip.rinck.imlc.factory.presenter.group;

import vip.rinck.imlc.factory.model.db.view.MemberUserModel;
import vip.rinck.imlc.factory.presenter.BaseContract;

/**
 * 群成员的契约
 */
public interface GroupMemberContract {
    interface Presenter extends BaseContract.Presenter{
        //具有一个刷新的方法
        void refresh();
    }

    //界面
    interface View extends BaseContract.RecyclerView<Presenter,MemberUserModel>{
        //获取群Id
        String getmGroupId();
    }
}
