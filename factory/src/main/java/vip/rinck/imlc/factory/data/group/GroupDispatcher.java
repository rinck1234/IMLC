package vip.rinck.imlc.factory.data.group;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import vip.rinck.imlc.factory.data.helper.DbHelper;
import vip.rinck.imlc.factory.data.helper.GroupHelper;
import vip.rinck.imlc.factory.data.helper.UserHelper;
import vip.rinck.imlc.factory.model.card.GroupCard;
import vip.rinck.imlc.factory.model.card.GroupMemberCard;
import vip.rinck.imlc.factory.model.db.Group;
import vip.rinck.imlc.factory.model.db.GroupMember;
import vip.rinck.imlc.factory.model.db.User;

public class GroupDispatcher implements GroupCenter {
    private static GroupCenter instance;
    private Executor executor = Executors.newSingleThreadExecutor();

    public static GroupCenter getInstance(){
        if(instance==null){
            synchronized (GroupDispatcher.class){
                if(instance==null)
                    instance = new GroupDispatcher();
            }
        }
        return instance;
    }
    @Override
    public void dispatch(GroupCard... cards) {
        if(cards==null||cards.length==0)
            return;
        executor.execute(new GroupHandler(cards));
    }

    @Override
    public void dispatch(GroupMemberCard... cards) {
        if(cards==null||cards.length==0)
            return;
        executor.execute(new GroupMemberRspHandler(cards));

    }

    private class GroupMemberRspHandler implements Runnable{

        private final GroupMemberCard[] cards;

        GroupMemberRspHandler(GroupMemberCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {
            List<GroupMember> members = new ArrayList<>();
            for(GroupMemberCard model :cards){
                //每个成员的信息
                User user = UserHelper.search(model.getUserId());
                //成员对应的群信息
                Group group = GroupHelper.find(model.getGroupId());
                if(user!=null&& group!=null){
                    GroupMember member = model.build(group,user);
                    members.add(member);
                }
            }
            if(members.size()>0)
                DbHelper.save(GroupMember.class,members.toArray(new GroupMember[0]));
        }
    }

    /**
     * 把群Card处理为群Db类
     */
    private class GroupHandler implements Runnable{
        private final GroupCard[] cards;

        GroupHandler(GroupCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {
            List<Group> groups = new ArrayList<>();
            for(GroupCard card:cards){
                User owner = UserHelper.search(card.getOwnerId());
                if(owner!=null){
                    Group group = card.build(owner);
                    groups.add(group);
                }
            }
            if(groups.size()>0)
                DbHelper.save(Group.class, groups.toArray(new Group[0]));
        }
    }
}
