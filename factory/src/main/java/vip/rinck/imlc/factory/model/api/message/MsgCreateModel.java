package vip.rinck.imlc.factory.model.api.message;

import android.os.Build;

import com.google.gson.annotations.Expose;

import java.util.Date;
import java.util.UUID;

import vip.rinck.imlc.factory.model.card.MessageCard;
import vip.rinck.imlc.factory.model.db.Message;
import vip.rinck.imlc.factory.persistence.Account;

public class MsgCreateModel {


    private String id;

    private String content;

    private String attach;

    private int type = Message.TYPE_STR;


    //接收者可为空
    private String receiverId;

    //接收者类型
    private int receiverType = Message.RECEIVER_TYPE_NONE;

    private MsgCreateModel() {
        //随机生成一个UUID
        this.id = UUID.randomUUID().toString();
    }

    public String getId(){
        return id;
    }

    public String getContent() {
        return content;
    }


    public String getAttach() {
        return attach;
    }


    public int getType() {
        return type;
    }


    public String getReceiverId() {
        return receiverId;
    }


    public int getReceiverType() {
        return receiverType;
    }


    /**
     * 建造者模式，快速创建一个发送Model
     */
    public static class Builder{
        private MsgCreateModel model;

        public Builder(){
            this.model = new MsgCreateModel();
        }

        //设置接收者
        public Builder receiver(String receiverId,int receiverType){
            model.receiverId = receiverId;
            model.receiverType = receiverType;
            return this;
        }

        //设置内容
        public Builder content(String content,int type){
            this.model.content = content;
            this.model.type = type;
            return this;
        }

        public Builder attach(String attach){
            this.model.attach = attach;
            return this;
        }

        public MsgCreateModel build() {
            return this.model;
        }
    }

    //当需要发送一个文件时，content刷新的问题

    private MessageCard card;
    //返回一个Card
    public MessageCard buildCard(){
        if(card==null){
            MessageCard card = new MessageCard();
            card.setId(id);
            card.setContent(content);
            card.setAttach(attach);
            card.setType(type);
            card.setSenderId(Account.getUserId());
            if(receiverType==Message.RECEIVER_TYPE_GROUP){
                card.setGroupId(receiverId);
            }else {
                card.setReceiverId(receiverId);
            }
            //通过当前model建立的card就是一个初步状态的Card
            card.setStatus(Message.STATUS_CREATED);
            card.setCreateAt(new Date());
            this.card = card;
        }
        return card;
    }

    /**
     * 把一个Message消息 转换为一个创建状态的CreateModel
     * @param message
     * @return
     */
    public static MsgCreateModel buildWithMessage(Message message){
        MsgCreateModel model = new MsgCreateModel();
        model.id = message.getId();
        model.content = message.getContent();
        model.attach = message.getAttach();
        model.type = message.getType();

        //如果接收者不为null，则发送给人
        if(message.getReceiver()!=null){
            model.receiverId = message.getReceiver().getId();
            model.receiverType = Message.RECEIVER_TYPE_NONE;
        }else {
            model.receiverId = message.getGroup().getId();
            model.receiverType = Message.RECEIVER_TYPE_GROUP;
        }

        return model;
    }

}
