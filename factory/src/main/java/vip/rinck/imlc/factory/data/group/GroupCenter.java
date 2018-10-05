package vip.rinck.imlc.factory.data.group;

import vip.rinck.imlc.factory.model.card.GroupCard;
import vip.rinck.imlc.factory.model.card.GroupMemberCard;

/**
 * 群中心的接口定义
 */
public interface GroupCenter {
    //群卡片的处理
    void dispatch(GroupCard... cards);
    //群成员的处理
    void dispatch(GroupMemberCard... cards);
}
