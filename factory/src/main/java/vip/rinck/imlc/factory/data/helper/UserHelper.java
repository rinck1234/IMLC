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
import vip.rinck.imlc.factory.net.Network;
import vip.rinck.imlc.factory.net.RemoteService;

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
                    //数据库的存储操作，需要把Usercard转换成User
                    //保存用户信息
                    Log.e("TAG_CARD",userCard.toString());
                    User user = userCard.build();
                    Log.e("TAG_USER",user.toString());
                    user.save();
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
                    User user = userCard.build();
                    user.save();
                    //TODO 通知联系人列表刷新

                    //返回数据
                    callback.onDataLoaded(rspModel.getResult());
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

    //搜索的方法
    public static void refreshContacts(final DataSource.Callback<List<UserCard>> callback) {
        RemoteService service = Network.remote();

        service.userContacts()
                .enqueue(new Callback<RspModel<List<UserCard>>>() {
                    @Override
                    public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                        RspModel<List<UserCard>> rspModel = response.body();
                        if (rspModel.success()) {
                            callback.onDataLoaded(rspModel.getResult());
                        } else {
                            Factory.decodeRspCode(rspModel, callback);
                        }
                    }

                    @Override
                    public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                        callback.onDataNotAvailable(R.string.data_network_error);
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
                //TODO 数据库的刷新但是没有通知
                User user = card.build();
                user.save();
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

}
