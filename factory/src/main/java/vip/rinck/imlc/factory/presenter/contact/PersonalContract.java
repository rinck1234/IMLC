package vip.rinck.imlc.factory.presenter.contact;

import vip.rinck.imlc.factory.model.Author;
import vip.rinck.imlc.factory.model.db.User;
import vip.rinck.imlc.factory.presenter.BaseContract;
import vip.rinck.imlc.factory.presenter.BasePresenter;

public interface PersonalContract {

    interface Prsenter extends BaseContract.Presenter{
        //获取用户信息
        User getUserPersonal();
    }

    interface View extends BaseContract.View<Prsenter>{
        String getUserId();
        //加载数据完成
        void onLoadDone(User user);
        //是否发起聊天
        void allowSayHello(boolean isAllow);
        //设置关注状态
        void setFollowStatus(boolean isFollow);
    }
}
