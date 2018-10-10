package vip.rinck.imlc.factory.data.message;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.Collections;
import java.util.List;

import vip.rinck.imlc.factory.data.BaseDbRepository;
import vip.rinck.imlc.factory.data.DataSource;
import vip.rinck.imlc.factory.model.db.Session;
import vip.rinck.imlc.factory.model.db.Session_Table;

/**
 * 最近聊天列表仓库
 */
public class SessionRepository extends BaseDbRepository<Session>
implements SessionDataSource{

    @Override
    public void load(SucceedCallback<List<Session>> callback) {
        super.load(callback);
        //数据库查询
        SQLite.select()
                .from(Session.class)
                .orderBy(Session_Table.modifyAt,false)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected boolean isRequired(Session session) {
        //所有的会话都要，不需要过滤
        return true;
    }

    @Override
    protected void insert(Session session) {
        //重写方法，让新的数据加到头部
        dataList.addFirst(session);
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Session> tResult) {
        //重写方法，进行一次反转
        Collections.reverse(tResult);
        super.onListQueryResult(transaction, tResult);
    }
}
