package vip.rinck.imlc.factory.presenter.message;

import android.support.v7.util.DiffUtil;

import java.util.List;

import vip.rinck.imlc.factory.data.message.SessionDataSource;
import vip.rinck.imlc.factory.data.message.SessionRepository;
import vip.rinck.imlc.factory.model.db.Session;
import vip.rinck.imlc.factory.presenter.BaseSourcePresenter;
import vip.rinck.imlc.factory.utils.DiffUiDataCallback;

/**
 * 最近聊天列表的Presenter
 */
public class SessionPresenter extends BaseSourcePresenter<Session,Session,SessionDataSource,SessionContract.View>
implements SessionContract.Presenter{
    public SessionPresenter(SessionContract.View view) {
        super(new SessionRepository(), view);
    }

    @Override
    public void onDataLoaded(List<Session> sessions) {
        SessionContract.View view = getView();
        if(view==null)
            return;

        //差异对比
        List<Session> old = view.getRecyclerAdapter().getItems();
        DiffUiDataCallback<Session> callback = new DiffUiDataCallback<>(old,sessions);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        //刷新界面
        refreshData(result,sessions);
    }
}
