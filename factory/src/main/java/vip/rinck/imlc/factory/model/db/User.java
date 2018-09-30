package vip.rinck.imlc.factory.model.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;
import java.util.Objects;

import vip.rinck.imlc.factory.model.Author;
import vip.rinck.imlc.factory.utils.DiffUiDataCallback;

@Table(database = AppDatabase.class)
public class User extends BaseModel implements Author,DiffUiDataCallback.UiDataDiffer<User> {
    public static final int SEX_MAN = 1;
    public static final int SEX_WOMAN = 2;

    //主键
    @PrimaryKey
    private String id;
    @Column
    private String username;
    @Column
    private String phone;
    @Column
    private String portrait;
    @Column
    private String desc;
    @Column
    private int sex = 0;
    @Column
    //对别人的备注信息
    private String alias;
    @Column
    //用户关注人的数量
    private int follows;
    @Column
    //用户粉丝的数量
    private int following;
    @Column
    //关注状态
    private boolean isFollow;
    @Column
    //时间字段
    private Date modifyAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public Date getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(Date modifyAt) {
        this.modifyAt = modifyAt;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (portrait != null ? portrait.hashCode() : 0);
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        result = 31 * result + sex;
        result = 31 * result + (alias != null ? alias.hashCode() : 0);
        result = 31 * result + follows;
        result = 31 * result + following;
        result = 31 * result + (isFollow ? 1 : 0);
        result = 31 * result + (modifyAt != null ? modifyAt.hashCode() : 0);
        return result;
    }

    @Override
    public boolean isSame(User old) {

        return this == old||Objects.equals(id ,old.getId());

    }

    @Override
    public boolean isUiContentSame(User old) {
        //主要判断用户名，头像，性别，关注
        return this == old || (
                Objects.equals(username,old.username)
                && Objects.equals(portrait,old.portrait)
                && Objects.equals(sex,old.sex)
                && Objects.equals(isFollow,old.isFollow)
                );
    }
}
