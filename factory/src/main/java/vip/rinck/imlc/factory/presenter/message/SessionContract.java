package vip.rinck.imlc.factory.presenter.message;

import vip.rinck.imlc.factory.model.db.Session;
import vip.rinck.imlc.factory.presenter.BaseContract;

public interface SessionContract {
    interface Presenter extends BaseContract.Presenter{

    }

    //都在基类完成了
    interface View extends BaseContract.RecyclerView<Presenter,Session>{

    }
}
