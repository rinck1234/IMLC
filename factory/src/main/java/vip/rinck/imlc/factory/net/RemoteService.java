package vip.rinck.imlc.factory.net;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import vip.rinck.imlc.factory.model.api.RspModel;
import vip.rinck.imlc.factory.model.api.account.AccountRspModel;
import vip.rinck.imlc.factory.model.api.account.LoginModel;
import vip.rinck.imlc.factory.model.api.account.RegisterModel;
import vip.rinck.imlc.factory.model.api.user.UserUpdateModel;
import vip.rinck.imlc.factory.model.card.UserCard;

/**
 * 网络请求所有的接口
 */
public interface RemoteService {
    /**
     * 网络请求一个注册接口
     * @param model 传入的是RegisterModel
     * @return
     */
    @POST("account/register")
    Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel model);

    /**
     * 登录接口
     * @param model LoginModel
     * @return
     */
    @POST("account/login")
    Call<RspModel<AccountRspModel>> accountLogin(@Body LoginModel model);

    /**
     * 绑定设备Id
     * @param pushId 设备Id
     * @return
     */
    @POST("account/bind/{pushId}")
    Call<RspModel<AccountRspModel>> accountBind(@Path(encoded = true,value = "pushId")String pushId);

    //用户更新的接口
    @PUT("user")
    Call<RspModel<UserCard>> userUpdate(@Body UserUpdateModel model);
}
