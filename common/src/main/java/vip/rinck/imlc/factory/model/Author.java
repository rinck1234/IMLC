package vip.rinck.imlc.factory.model;

/**
 * 基础用户接口
 */
public interface Author {
    String getId() ;

    void setId(String id);

    String getUsername() ;

    void setUsername(String username);

    String getPortrait();

    void setPortrait(String portrait);

}
