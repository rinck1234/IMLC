package vip.rinck.imlc.factory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import vip.rinck.imlc.common.app.Application;

public class Factory {

    private static final Factory instance;
    private final Executor executor;

    static {
        instance = new Factory();
    }

    private Factory(){
        //新建一个4线程的线程池
        executor = Executors.newFixedThreadPool(4);
    }

    /**
     * 返回全局的Application
     * @return
     */
    public static Application app(){
        return Application.getInstance();
    }


    /**
     * 异步运行的方法
     * @param runnable
     */
    public static void runOnAsync(Runnable runnable){
        //拿到单例，拿到线程池，异步执行
        instance.executor.execute(runnable);
    }
}
