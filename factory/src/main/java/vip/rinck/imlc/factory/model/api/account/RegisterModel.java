package vip.rinck.imlc.factory.model.api.account;

/**
 * 注册使用的Model
 */
public class RegisterModel {
    private String account;
    private String password;
    private String username;
    private String pushId;

    public RegisterModel(String account, String password, String username, String pushId) {
        this.account = account;
        this.password = password;
        this.username = username;
        this.pushId = pushId;
    }

    public RegisterModel(String account, String password, String username) {
        this(account,password,username,null);
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }
}
