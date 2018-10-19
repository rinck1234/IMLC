package vip.rinck.imlc.factory.data.message;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.Collections;
import java.util.List;

import vip.rinck.imlc.factory.data.BaseDbRepository;
import vip.rinck.imlc.factory.model.db.Message;
import vip.rinck.imlc.factory.model.db.Message_Table;

/**
 * 群聊天的聊天列表
 * 关注的内容是我发送的消息 以及别人的推送
 */
public class MessageGroupRepository extends BaseDbRepository<Message> implements MessageDataSource {

    //聊天对象Id
    private String receiverId;

    public MessageGroupRepository(String receiverId) {
        super();
        this.receiverId = receiverId;
    }

    @Override
    public void load(SucceedCallback<List<Message>> callback) {
        super.load(callback);

       SQLite.select()
                .from(Message.class)
                .where(Message_Table.group_id.eq(receiverId))
                .orderBy(Message_Table.createAt,false)
                .limit(30)
                .async()
                .queryListResultCallback(this)
                .execute();

    }

    @Override
    protected boolean isRequired(Message message) {
        //如果消息的群Id不为空 则是发送给群的
        //如果群Id等于我们需要的 就通过
        return message.getGroup()!=null
                && receiverId.equalsIgnoreCase(message.getGroup().getId());

    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Message> tResult) {
        //反转返回的集合
        Collections.reverse(tResult);
        super.onListQueryResult(transaction, tResult);
    }
}
