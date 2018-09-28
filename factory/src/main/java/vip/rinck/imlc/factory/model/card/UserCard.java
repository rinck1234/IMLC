package vip.rinck.imlc.factory.model.card;

import java.util.Date;

import vip.rinck.imlc.factory.model.db.User;

public class UserCard {
    private String id;
    private String username;
    private String phone;
    private String portrait;
    private String desc;
    private int sex = 0;

    //用户关注的人的数量
    private int follows;

    //用户粉丝的数量
    private int following;

    //我与当前User的关注状态
    private boolean isFollow;

    //用户最后的更新时间
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

    //缓存一个对应的User,不能被Gson解析使用
    private transient User user;
    public User build(){
        if(user==null){
            User user = new User();
            user.setId(id);
            user.setUsername(username);
            user.setPortrait(portrait);
            user.setPhone(phone);
            user.setSex(sex);
            user.setDesc(desc);
            user.setFollow(isFollow);
            user.setFollows(follows);
            user.setFollowing(following);
            user.setModifyAt(modifyAt);
            this.user = user;
        }
        return user;
    }

    @Override
    public String toString() {
        return "UserCard{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", portrait='" + portrait + '\'' +
                ", desc='" + desc + '\'' +
                ", sex=" + sex +
                ", follows=" + follows +
                ", following=" + following +
                ", isFollow=" + isFollow +
                ", modifyAt=" + modifyAt +
                '}';
    }
}
