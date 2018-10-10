package vip.rinck.imlc.factory.data.group;

import android.text.TextUtils;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import vip.rinck.imlc.factory.data.BaseDbRepository;
import vip.rinck.imlc.factory.data.DataSource;
import vip.rinck.imlc.factory.data.helper.GroupHelper;
import vip.rinck.imlc.factory.model.db.Group;
import vip.rinck.imlc.factory.model.db.Group_Table;
import vip.rinck.imlc.factory.model.db.view.MemberUserModel;

/**
 * 群组数据仓库
 */
public class GroupsRepository extends BaseDbRepository<Group>
        implements GroupsDataSource {

    @Override
    public void load(SucceedCallback<List<Group>> callback) {
        super.load(callback);

        SQLite.select()
                .from(Group.class)
                .orderBy(Group_Table.name,true)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected boolean isRequired(Group group) {
        //一个群的信息，只可能出现在数据库 一下两种情况
        //1.被拉入群 2.创建群
        //无论如何 拿到的只有群信息 没有成员信息
        //要先进行成员的初始化
        if(group.getGroupMemberCount()>0){
            //已经初始化了群成员信息
            group.holder = buildGroupHolder(group);
        }else {
            //待初始化
            group.holder = null;
            GroupHelper.refreshGroupMember(group);
        }
        //所有群都关注
        return true;
    }

    //初始化界面显示的成员信息
    private String buildGroupHolder(Group group) {
        List<MemberUserModel> userModels = group.getLatelyGroupMembers();
        if(userModels==null||userModels.size()==0)
            return null;
        StringBuilder builder = new StringBuilder();
        for (MemberUserModel userModel : userModels) {
            builder.append(TextUtils.isEmpty(userModel.alias)?userModel.username:userModel.alias);
            builder.append(", ");
        }
        builder.delete(builder.lastIndexOf(", "),builder.length());
        return builder.toString();
    }
}
