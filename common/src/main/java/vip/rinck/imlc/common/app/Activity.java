package vip.rinck.imlc.common.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;
import vip.rinck.imlc.common.R;
import vip.rinck.imlc.common.widget.convention.PlaceHolderView;

public abstract class Activity extends AppCompatActivity{

    protected PlaceHolderView mPlaceholderView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //在界面未初始化之前调用的初始化窗口
        initWindows();
        if(initArgs(getIntent().getExtras())){
            //得到界面ID并设置到Activity界面中
            int layId = getContentLayoutId();
            setContentView(layId);
            initBefore();
            initWidget();
            initData();
        }else{
            finish();
        }
    }

    /**
     * 初始化控件调用之前
     */
    protected void initBefore(){

    }

    protected void initWindows(){

    }

    //初始化相关参数
    protected boolean initArgs(Bundle bundle){
        return true;
    }

    //得到控件ID
    protected abstract int getContentLayoutId();

    //初始化控件
    protected void initWidget(){


        ButterKnife.bind(this);
    }

    //初始化数据
    protected void initData(){

    }

    @Override
    public boolean onSupportNavigateUp() {
        //当点击导航上的返回按钮时结束当前页面
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {

        //得到当前Activity下的所有Fragment
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        //判断是否为空
        if(fragments!=null&&fragments.size()>0){
            for(Fragment fragment:fragments){
                //判断是否为我们能处理的Fragment类型
                if(fragment instanceof vip.rinck.imlc.common.app.Fragment){
                    //判断是否拦截了返回按钮
                    if(((vip.rinck.imlc.common.app.Fragment) fragment).onBackPressed()){
                        //如果有，直接
                        return;
                    }
                }
            }
        }

        super.onBackPressed();
        finish();
    }
    /**
     * 设置占位布局
     * @param placeholderView 继承
     */
    public void setPlaceholderView(PlaceHolderView placeholderView){
        this.mPlaceholderView = placeholderView;

    }
}
