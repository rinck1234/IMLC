package vip.rinck.imlc.factory.data.helper;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import vip.rinck.imlc.factory.model.db.Session;
import vip.rinck.imlc.factory.model.db.Session_Table;

/**
 * 会话的辅助工具类
 */
public class SessionHelper {
    public static Session findFromLocal(String id) {
        //从本地查询
        return SQLite.select()
                .from(Session.class)
                .where(Session_Table.id.eq(id))
                .querySingle();
    }
}
