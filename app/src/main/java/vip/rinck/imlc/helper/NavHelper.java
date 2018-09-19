package vip.rinck.imlc.helper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;



/**
 * 完成对Fragment的调度与重用问题
 * 达到最优的Fragment切换
 */
public class NavHelper<T> {
    //所有的Tab集合
    private final SparseArray<Tab<T>> tabs = new SparseArray<>();
    //用于初始化的参数
    private final Context context;
    private final FragmentManager fragmentManager;
    private final int containerId;
    private final OnTabChangedListener<T> listener;
    //当前选中的Tab
    private Tab<T> currentTab;

    public NavHelper(Context context, int containerId,
                     FragmentManager fragmentManager,
                     OnTabChangedListener<T> listener) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
        this.listener = listener;
    }

    /**
     * 添加Tab
     * @param menuId Tab对应的Id
     * @param tab
     */
    public NavHelper<T> add(int menuId, Tab<T> tab){
        tabs.put(menuId,tab);
        return this;
    }

    /**
     * 获取当前显示的Tab
     * @return
     */
    public Tab<T> getCurrentTab(){
        return currentTab;
    }

    /**
     * 执行点击菜单操作
     *
     * @param menuId 菜单的Id
     * @return 是否能够处理本次点击
     */
    public boolean performClickMenu(int menuId) {
        //集合中寻找点击的菜单对应的Tab
        Tab<T> tab = tabs.get(menuId);
        if(tab!=null){
            doSelect(tab);
            return true;
        }
        return false;
    }

    /**
     * 进行真实的Tab选择操作
     * @param tab
     */
    private void doSelect(Tab<T> tab){
        Tab<T> oldTab = null;
        if(currentTab!=null){
            oldTab=currentTab;
            if(oldTab==tab){
                //如果当前显示的Tab就是点击的Tab
                //不进行任何处理
                notifyReselect(tab);
                return;
            }
        }
        //赋值并进行切换方法的调用
        currentTab = tab;
        doTabChanged(currentTab,oldTab);
    }

    private void doTabChanged(Tab<T> newTab,Tab<T> oldTab){
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if(oldTab!=null){
            //从界面移除，但还在Fragment的缓存空间中
            ft.detach(oldTab.fragment);
        }
        if(newTab!=null){
            if(newTab.fragment==null){
                //首次新建
                Fragment fragment = (Fragment) Fragment.instantiate(context,newTab.clazz.getName(),null);
                //缓存起来
                newTab.fragment = fragment;
                //提交到FragmentManager
                ft.add(containerId,fragment,newTab.clazz.getName());
            }else{
                //从FragmentManager的缓存中重新取出
                ft.attach(newTab.fragment);
            }
        }

        //提交事务
        ft.commit();
        //通知回调
        notifiTabSelected(newTab,oldTab);
    }

    /**
     * 回调监听器
     * @param newTab
     * @param oldTab
     */
    private void notifiTabSelected(Tab<T> newTab,Tab<T> oldTab){
        if(listener!=null){
            listener.onTabChanged(newTab,oldTab);
        }
    }

    private void notifyReselect(Tab<T> tab){
       // TODO 二次点击Tab所做的操作
    }

    /**
     * 所有Tab基础属性
     *
     * @param <T>
     */
    public static class Tab<T> {

        public Tab(Class<?> clazz, T extra) {
            this.clazz = clazz;
            this.extra = extra;
        }

        public Class<?> clazz;
        //额外的字段，用户自己设定需要的东西
        public T extra;
        //内部缓存的对应的Fragment
        //
        Fragment fragment;
    }

    /**
     * 定义事件处理完成后的回调接口
     *
     * @param <T>
     */
    public interface OnTabChangedListener<T> {
        void onTabChanged(Tab<T> newTab, Tab<T> oldTab);
    }
}
