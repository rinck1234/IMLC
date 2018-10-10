package vip.rinck.imlc.factory.data.helper;

import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vip.rinck.imlc.factory.Factory;
import vip.rinck.imlc.factory.R;
import vip.rinck.imlc.factory.data.DataSource;
import vip.rinck.imlc.factory.model.api.RspModel;
import vip.rinck.imlc.factory.model.api.user.UserUpdateModel;
import vip.rinck.imlc.factory.model.card.UserCard;
import vip.rinck.imlc.factory.model.db.User;
import vip.rinck.imlc.factory.model.db.User_Table;
import vip.rinck.imlc.factory.model.db.view.UserSampleModel;
import vip.rinck.imlc.factory.net.Network;
import vip.rinck.imlc.factory.net.RemoteService;
import vip.rinck.imlc.factory.persistence.Account;
import vip.rinck.imlc.utils.CollectionUtil;

public class UserHelper {
    //更新用户信息
    public static void update(UserUpdateModel model, final DataSource.Callback<UserCard> callback){
        RemoteService service = Network.remote();
        //得到一个Call
        Call<RspModel<UserCard>> call = service.userUpdate(model);
        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if(rspModel.success()){
                    UserCard userCard = rspModel.getResult();
                    //唤起进行保存的操作
                    Factory.getUserCenter().dispatch(userCard);
                    /*//数据库的存储操作，需要把Usercard转换成User
                    //保存用户信息
                    User user = userCard.build();
                    //异步的统一保存
                    DbHelper.save(User.class,user);*/
                    //返回成功
                    callback.onDataLoaded(userCard);
                }else {
                    Factory.decodeRspCode(rspModel,callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                    callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    //搜索的方法
    public static Call search(String username, final DataSource.Callback<List<UserCard>> callback){
        RemoteService service = Network.remote();
        //得到一个Call
        Call<RspModel<List<UserCard>>> call = service.userSearch(username);
        call.enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> rspModel = response.body();
                if(rspModel.success()){
                    callback.onDataLoaded(rspModel.getResult());
                }else{
                    Factory.decodeRspCode(rspModel,callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });

        //返回当前的调度者
        return call;
    }

    /**
     * 关注网络请求
     * @param id
     * @param callback
     */
    public static void follow(String id, final DataSource.Callback<UserCard> callback){
        RemoteService service = Network.remote();
        Call<RspModel<UserCard>> call = service.userFollow(id);

        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if(rspModel.success()){
                    UserCard userCard = rspModel.getResult();
                    //唤起进行保存的操作
                    Factory.getUserCenter().dispatch(userCard);

                    /*User user = userCard.build();
                    //保存并通知联系人列表刷新
                    DbHelper.save(User.class,user);*/
                    //返回数据
                    callback.onDataLoaded(userCard);
                }else{
                    Factory.decodeRspCode(rspModel,callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    //刷新联系人的操作，直接存储到数据库
    //通过数据库观察者进行通知界面更新
    //界面更新的时候进行对比，差异更新
    public static void refreshContacts() {
        RemoteService service = Network.remote();

        service.userContacts()
                .enqueue(new Callback<RspModel<List<UserCard>>>() {
                    @Override
                    public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                        RspModel<List<UserCard>> rspModel = response.body();
                        if (rspModel.success()) {
                            List<UserCard> cards = rspModel.getResult();
                            if(cards==null||cards.size()==0)
                                return;
                            UserCard[] cards1 = CollectionUtil.toArray(cards,UserCard.class);
                            Factory.getUserCenter().dispatch(cards1);
                        } else {
                            Factory.decodeRspCode(rspModel, null);
                        }
                    }

                    @Override
                    public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                        // Nothing
                    }
                });
    }

    //从本地查询一个用户的信息
    public static User findFromLocal(String id){
        return SQLite.select()
                .from(User.class)
                .where(User_Table.id.eq(id))
                .querySingle();
    }

    //从网络查询一个用户的信息
    public static User findFromNet(String id){

        RemoteService remoteService = Network.remote();
        try {
            Response<RspModel<UserCard>> response =  remoteService.userFind(id).execute();
            UserCard card = response.body().getResult();
            if(card!=null){
                User user = card.build();
                //存储并通知
                Factory.getUserCenter().dispatch(card);
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 搜索一个用户，优先本地缓存
     * 没有再从网络获取
     * @param id
     * @return
     */
    public static User search(String id){
        User user = findFromLocal(id);
        if(user==null){
            return findFromNet(id);
        }
        return user;
    }

    /**
     * 搜索一个用户，优先网络获取
     * 没有再从本地获取
     * @param id
     * @return
     */
    public static User searchFirstOfNet(String id){
        User user = findFromNet(id);
        if(user==null){
            return findFromLocal(id);
        }
        return user;
    }

    /**
     * 获取联系人
     */
    public static List<User> getContact(){
        return SQLite.select()
                .from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.username,true)
                .limit(100)
                .queryList();

    }

    /**
     * 获取一个联系人列表
     * 简单数据类型
     * @return
     */
    public static List<UserSampleModel> getSampleContact(){
        return SQLite.select(User_Table.id.withTable().as("id"),
                User_Table.username.withTable().as("username"),
                User_Table.portrait.withTable().as("portrait"))
                .from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.username,true)
                .queryCustomList(UserSampleModel.class);

    }

}
