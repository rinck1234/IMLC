package vip.rinck.imlc.factory.presenter.search;

import java.util.List;

import vip.rinck.imlc.factory.model.card.GroupCard;
import vip.rinck.imlc.factory.model.card.UserCard;
import vip.rinck.imlc.factory.presenter.BaseContract;

public interface SearchContract {
    interface Presenter extends BaseContract.Presenter {
        //搜索内容
        void search(String content);
    }

    //搜索人的界面
    interface UserView extends BaseContract.View<Presenter>{
        void onSearchDone(List<UserCard> userCards);
    }

    //搜索群的界面
    interface GroupView extends BaseContract.View<Presenter>{
        void onSearchDone(List<GroupCard> groupCards);
    }
}
