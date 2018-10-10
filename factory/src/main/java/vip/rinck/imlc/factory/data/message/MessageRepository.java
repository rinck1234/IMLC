package vip.rinck.imlc.factory.data.message;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.Collections;
import java.util.List;

import vip.rinck.imlc.factory.data.BaseDbRepository;
import vip.rinck.imlc.factory.data.DataSource;
import vip.rinck.imlc.factory.model.db.Message;
import vip.rinck.imlc.factory.model.db.Message_Table;

/**
 * 跟某人聊天的时候的聊天列表
 */
public class MessageRepository extends BaseDbRepository<Message> implements MessageDataSource {

    //聊天对象Id
    private String receiverId;

    public MessageRepository(String receiverId) {
        super();
        this.receiverId = receiverId;
    }

    @Override
    public void load(SucceedCallback<List<Message>> callback) {
        super.load(callback);

        //(sender_id == receiverId and group_id == null)
        // or(receiver_id==receiverId)
        //↓
        SQLite.select()
                .from(Message.class)
                .where(OperatorGroup.clause()
                        .and(Message_Table.sender_id.eq(receiverId))
                        .and(Message_Table.group_id.isNull()))
                .or(Message_Table.receiver_id.eq(receiverId))
                .orderBy(Message_Table.createAt,false)
                .limit(30)
                .async()
                .queryListResultCallback(this)
                .execute();

    }

    @Override
    protected boolean isRequired(Message message) {
        //receiverId如果是发送者，那么Group==null情况下一定是发送给我的消息
        //如果消息的接收者不为空，那么一定是发送给某人的，这个人只能是我或者是某个人
        //如果这个某人就是receiverId 那么就是我关注的信息
        return (receiverId.equalsIgnoreCase(message.getSender().getId())
                && message.getGroup() == null)
                || (message.getReceiver() != null
                && receiverId.equalsIgnoreCase(message.getReceiver().getId())
        );
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Message> tResult) {
        //反转返回的集合
        Collections.reverse(tResult);
        super.onListQueryResult(transaction, tResult);
    }
}
