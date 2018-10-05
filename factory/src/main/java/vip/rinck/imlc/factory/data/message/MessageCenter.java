package vip.rinck.imlc.factory.data.message;

import vip.rinck.imlc.factory.model.card.MessageCard;

/**
 * 消息中心，进行消息卡片的消费
 */
public interface MessageCenter {

    //分发处理一堆用户卡片信息，并更新到数据库
    void dispatch(MessageCard... cards);
}
