package vip.rinck.imlc.factory.presenter.contact;

import vip.rinck.imlc.common.widget.recycler.RecyclerAdapter;
import vip.rinck.imlc.factory.model.db.User;
import vip.rinck.imlc.factory.presenter.BaseContract;

public interface ContactContract {
    //什么都不需要额外定义，开始就是调用start
    interface Presenter extends BaseContract.Presenter{

    }

    //都在基类完成了
    interface View extends BaseContract.RecyclerView<Presenter,User>{

    }
}
