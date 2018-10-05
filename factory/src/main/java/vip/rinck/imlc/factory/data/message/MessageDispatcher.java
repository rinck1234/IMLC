package vip.rinck.imlc.factory.data.message;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import vip.rinck.imlc.factory.data.helper.DbHelper;
import vip.rinck.imlc.factory.data.helper.GroupHelper;
import vip.rinck.imlc.factory.data.helper.MessageHelper;
import vip.rinck.imlc.factory.data.helper.UserHelper;
import vip.rinck.imlc.factory.data.user.UserDispatcher;
import vip.rinck.imlc.factory.model.card.MessageCard;
import vip.rinck.imlc.factory.model.db.Group;
import vip.rinck.imlc.factory.model.db.Message;
import vip.rinck.imlc.factory.model.db.User;

public class MessageDispatcher implements MessageCenter {

    private static MessageCenter instance;
    //单线程池；处理卡片一个个的消息进行处理
    private final Executor executor = Executors.newSingleThreadExecutor();


    public static MessageCenter getInstance(){
        if(instance==null){
            synchronized (UserDispatcher.class){
                if(instance==null)
                    instance = new MessageDispatcher();
            }
        }
        return instance;
    }

    @Override
    public void dispatch(MessageCard... cards) {
        if(cards==null||cards.length==0)
            return;
        //丢到单线程池中
        executor.execute(new MessageCardHandler(cards));
    }

    /**
     * 消息卡片的线程调度处理会触发run方法
     */
    private class MessageCardHandler implements Runnable{

        private final MessageCard[] cards;

        MessageCardHandler(MessageCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {
            List<Message> messages = new ArrayList<>();
            for(MessageCard card:cards){
                //卡片基础信息过滤，错误卡片直接过滤
                if(card==null|| TextUtils.isEmpty(card.getSenderId())
                        ||TextUtils.isEmpty(card.getId())
                        ||TextUtils.isEmpty(card.getReceiverId())
                        &&TextUtils.isEmpty(card.getGroupId()))
                    continue;

                Message message = MessageHelper.findFromLocal(card.getId());
                if(message!=null){
                    //消息字段发送后就不变了
                    //如果本地消息已经完成显示则不做处理
                    if(message.getStatus()==Message.STATUS_DONE)
                        continue;
                    //新状态为完成才更新服务器时间，否则不更新
                    if(card.getStatus()==Message.STATUS_DONE) {
                        //代表发送成功，此时需要修改为服务器的时间
                        message.setCreateAt(card.getCreateAt());
                        //没有进入判断，则发送失败
                    }
                    //更新一些会变化的内容
                    message.setContent(card.getContent());
                    message.setAttach(card.getAttach());
                    //更新状态
                    message.setStatus(card.getStatus());
                }else {
                    //没找到本地消息
                    User sender = UserHelper.search(card.getSenderId());
                    User receiver = null;
                    Group group = null;
                    if(!TextUtils.isEmpty(card.getReceiverId())){
                        receiver = UserHelper.search(card.getReceiverId());
                    }else if(!TextUtils.isEmpty(card.getGroupId())){
                        group = GroupHelper.findFromLocal(card.getGroupId());
                    }
                    //必需有一个接收者
                    if(receiver==null&&group==null&&sender!=null)
                        continue;

                    message = card.build(sender,receiver,group);
                }
                messages.add(message);
            }
            if(messages.size()>0)
                DbHelper.save(Message.class,messages.toArray(new Message[0]));
        }
    }
}
