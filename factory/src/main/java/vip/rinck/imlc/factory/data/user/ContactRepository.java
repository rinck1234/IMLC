package vip.rinck.imlc.factory.data.user;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import vip.rinck.imlc.factory.data.BaseDbRepository;
import vip.rinck.imlc.factory.data.DataSource;
import vip.rinck.imlc.factory.data.helper.DbHelper;
import vip.rinck.imlc.factory.model.db.User;
import vip.rinck.imlc.factory.model.db.User_Table;
import vip.rinck.imlc.factory.persistence.Account;

/**
 * 联系人仓库
 */
public class ContactRepository extends BaseDbRepository<User> implements ContactDataSource{



    @Override
    public void load(DataSource.SucceedCallback<List<User>> callback) {
        super.load(callback);

        //加载本地数据库数据
        SQLite.select()
                .from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.username, true)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();
    }
    /**
     * 检查一个User是否是我需要关注的数据
     *
     * @param user
     * @return True是我关注的数据
     */
    @Override
    protected boolean isRequired(User user) {
        return user.isFollow() && !user.getId().equals(Account.getUserId());
    }

}
