package vip.rinck.imlc.factory.model.db.view;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.QueryModel;

import vip.rinck.imlc.factory.model.db.AppDatabase;

/**
 * 群成员对应的用户简单信息表
 */

@QueryModel(database = AppDatabase.class)
public class MemberUserModel {
    @Column
    public String userId;//User-id || Member-userId
    @Column
    public String username;//User-name
    @Column
    public String alias;//Member-alias
    @Column
    public String portrait;//User-portrait
}
