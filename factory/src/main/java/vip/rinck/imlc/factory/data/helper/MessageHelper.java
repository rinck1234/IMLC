package vip.rinck.imlc.factory.data.helper;

import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vip.rinck.imlc.factory.Factory;
import vip.rinck.imlc.factory.model.api.RspModel;
import vip.rinck.imlc.factory.model.api.message.MsgCreateModel;
import vip.rinck.imlc.factory.model.card.MessageCard;
import vip.rinck.imlc.factory.model.card.UserCard;
import vip.rinck.imlc.factory.model.db.Message;
import vip.rinck.imlc.factory.model.db.Message_Table;
import vip.rinck.imlc.factory.net.Network;
import vip.rinck.imlc.factory.net.RemoteService;

/**
 * 消息工具类
 */
public class MessageHelper {


    //从本地找消息
    public static Message findFromLocal(String id) {
        //TODO
        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.id.eq(id))
                .querySingle();
    }

    //发送是异步进行的
    public static void push(final MsgCreateModel model) {
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                //如果发送成功或正在发送，则不能重新发送
                Message message = findFromLocal(model.getId());
                if (message != null && message.getStatus() != Message.STATUS_FAILED)
                    return;

                //TODO 如果是文件类型的（语音，图片，文件），需要先上传后才发送

                //在发送的时候通知界面更新状态
                final MessageCard card = model.buildCard();
                Factory.getMessageCenter().dispatch(card);

                //直接发送 进行网络调度
                RemoteService service = Network.remote();
                service.msgPush(model).enqueue(new Callback<RspModel<MessageCard>>() {
                    @Override
                    public void onResponse(Call<RspModel<MessageCard>> call, Response<RspModel<MessageCard>> response) {
                        RspModel<MessageCard> rspModel = response.body();
                        if (rspModel != null && rspModel.success()) {
                            MessageCard rspCard = rspModel.getResult();
                            if (rspCard != null) {
                                //成功的调度
                                Factory.getMessageCenter().dispatch(rspCard);
                            }
                        } else {
                            //解析是否是账户异常
                            Factory.decodeRspCode(rspModel, null);
                            //走失败流程
                            onFailure(call, null);
                        }
                    }

                    @Override
                    public void onFailure(Call<RspModel<MessageCard>> call, Throwable t) {
                        card.setStatus(Message.STATUS_FAILED);
                        Factory.getMessageCenter().dispatch(card);
                    }
                });
            }
        });
    }

    /**
     * 查询一条消息
     *
     * @param groupId
     * @return 群中的最后一条消息
     */
    public static Message findLastWithGroup(String groupId) {

        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.group_id.eq(groupId))
                .orderBy(Message_Table.createAt, false)//倒序查询
                .querySingle();

    }

    /**
     * 查询一个消息，和某用户的最后一条消息
     * @param userId
     * @return 最后一条消息
     */
    public static Message findLastWithUser(String userId){
        return SQLite.select()
                .from(Message.class)
                .where(OperatorGroup.clause()
                        .and(Message_Table.sender_id.eq(userId))
                        .and(Message_Table.group_id.isNull()))
                .or(Message_Table.receiver_id.eq(userId))
                .orderBy(Message_Table.createAt, false)//倒序查询
                .querySingle();
    }
}
