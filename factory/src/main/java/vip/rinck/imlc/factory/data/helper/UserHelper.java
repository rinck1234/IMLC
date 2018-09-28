package vip.rinck.imlc.factory.data.helper;

import android.util.Log;

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
}
