package vip.rinck.imlc.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class Fragment extends android.support.v4.app.Fragment {

    protected View mRoot;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //初始化参数
        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(mRoot==null) {
            int layId = getContentLayoutId();
            //初始化当前的根布局，但是不在创建时就添加到container里面
            View root = inflater.inflate(layId, container, false);
            initWidget(root);
            return super.onCreateView(inflater, container, savedInstanceState);
        }else{
            if(mRoot.getParent()!=null){
                //把当前Root从其父控件中移除
                ( (ViewGroup)mRoot.getParent()).removeView(mRoot);
            }
        }

        return mRoot;


    }

    //初始化相关参数
    protected void initArgs(Bundle bundle){
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //当view创建完成后初始化数据
        initData();
    }

    protected abstract int getContentLayoutId();

    protected void initWidget(View root){

    }

    //初始化数据
    protected void initData(){

    }

    /**
     * 返回按键触发时调用
     * 返回True代表我已处理逻辑，Activity不用自己finish
     * 返回false代表我未处理逻辑，Activity走自己的逻辑
     */
    public boolean onBackPressed(){
        return false;
    }
}
