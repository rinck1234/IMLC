package vip.rinck.imlc.factory.presenter.contact;

import vip.rinck.imlc.factory.model.card.UserCard;
import vip.rinck.imlc.factory.presenter.BaseContract;

public interface FollowContract {
    //任务调度者
    interface Presenter extends BaseContract.Presenter{
        void follow(String userId);
    }

    interface View extends BaseContract.View<Presenter>{
        //成功返回用户信息
        void onFollowSuccess(UserCard userCard);
    }

}
