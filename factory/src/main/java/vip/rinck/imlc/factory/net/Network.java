package vip.rinck.imlc.factory.net;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vip.rinck.imlc.common.Common;
import vip.rinck.imlc.factory.Factory;
import vip.rinck.imlc.factory.persistence.Account;

/**
 * 网络请求的封装
 */
public class Network {

    private static Network instance;
    private Retrofit retrofit;

    static {
        instance = new Network();
    }

    private Network(){

    }

    //构建一个Retrofit
    public static Retrofit getRetrofit(){
        if(instance.retrofit!=null)
            return instance.retrofit;


        OkHttpClient client = new OkHttpClient.Builder()
                //给所有的请求添加一个拦截器
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        //拿到请求
                        Request origin = chain.request();
                        //重新进行构建
                        Request.Builder builder = origin.newBuilder();
                        if(!TextUtils.isEmpty(Account.getToken())){
                            //在请求头添加一个token
                            builder.addHeader("token",Account.getToken());
                        }
                        builder.addHeader("Content-Type","application/json");
                        Request newRequest = builder.build();
                        //返回
                        return chain.proceed(newRequest);
                    }
                })
                .build();

        Retrofit.Builder builder = new Retrofit.Builder();

        //设置链接
        instance.retrofit = builder.baseUrl(Common.Constance.API_URL)
                .client(client)//设置client
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))//设置Json解析器
                .build();
        return instance.retrofit;
    }

    /**
     * 返回一个请求代理
     * @return
     */
    public static RemoteService remote(){
        return Network.getRetrofit().create(RemoteService.class);
    }
}
