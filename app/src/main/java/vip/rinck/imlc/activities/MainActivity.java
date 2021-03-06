package vip.rinck.imlc.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import vip.rinck.imlc.R;
import vip.rinck.imlc.activities.AccountActivity;
import vip.rinck.imlc.common.app.Activity;
import vip.rinck.imlc.common.widget.PortraitView;
import vip.rinck.imlc.factory.persistence.Account;
import vip.rinck.imlc.fragments.assist.PermissionsFragment;
import vip.rinck.imlc.fragments.main.ActiveFragment;
import vip.rinck.imlc.fragments.main.ContactFragment;
import vip.rinck.imlc.fragments.main.GroupFragment;
import vip.rinck.imlc.helper.NavHelper;
import vip.rinck.imlc.helper.PermissionHelper;


public class MainActivity extends Activity implements BottomNavigationView.OnNavigationItemSelectedListener,
        NavHelper.OnTabChangedListener<Integer> {

    @BindView(R.id.appbar)
    View mLayAppbar;

    @BindView(R.id.iv_portrait)
    PortraitView mPortrait;

    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.lay_container)
    FrameLayout mContainer;

    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;

    @BindView(R.id.btn_action)
    FloatActionButton mAction;

    private NavHelper<Integer> mNavHelper;

    public static void show(Context context){
        context.startActivity(new Intent(context,MainActivity.class));
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        if(Account.isComplete()){
            return super.initArgs(bundle);
        }else {
            UserActivity.show(this);
            return false;
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();


        mNavHelper = new NavHelper<>(this, R.id.lay_container, getSupportFragmentManager(), this);
        mNavHelper.add(R.id.action_home, new NavHelper.Tab<>(ActiveFragment.class, R.string.title_home))
                .add(R.id.action_group, new NavHelper.Tab<>(GroupFragment.class, R.string.title_group))
                .add(R.id.action_contact, new NavHelper.Tab<>(ContactFragment.class, R.string.title_contact));


        mNavigation.setOnNavigationItemSelectedListener(this);

        Glide.with(this)
                .load(R.drawable.bg_src_crash)
                .centerCrop()
                .into(new ViewTarget<View, GlideDrawable>(mLayAppbar) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setBackground(resource.getCurrent());
                    }
                });

    }

    @Override
    protected void initData() {
        super.initData();


        Menu menu = mNavigation.getMenu();
        menu.performIdentifierAction(R.id.action_home, 0);

        //初始化头像加载
        mPortrait.setup(Glide.with(this),Account.getUser());
    }
    @OnClick(R.id.iv_portrait)
    void onPortraitClick(){
        PersonalActivity.show(this,Account.getUserId());
    }

    @OnClick(R.id.iv_search)
    void onSearchMenuClick() {
        //在群界面时，点击顶部搜索 则进入群搜索
        int type = Objects.equals(mNavHelper.getCurrentTab().extra,R.string.title_group)?
                SearchActivity.TYPE_GROUP:SearchActivity.TYPE_USER;
        SearchActivity.show(this,type);

    }

    @OnClick(R.id.btn_action)
    void onActionClick() {
        //浮动按钮点击 判断当前界面是群还是联系人
        //如果是群，则打开群创建的界面
        if(Objects.equals(mNavHelper.getCurrentTab().extra,R.string.title_group)){
            //打开群创建界面
            GroupCreateActivity.show(this);
        }else {
            //打开添加用户界面
            SearchActivity.show(this,SearchActivity.TYPE_USER);
        }
    }

    /**
     * 当底部导航条被点击时触发
     *
     * @param menuItem 点击的项目
     * @return True表示能够处理本次点击
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        //转接事件流到工具类中
        return mNavHelper.performClickMenu(menuItem.getItemId());
    }


    /**
     * NavHelper处理后回调的方法
     *
     * @param newTab
     * @param oldTab
     */
    @Override
    public void onTabChanged(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {
        //从额外字段中取出Title资源Id
        mTitle.setText(newTab.extra);

        //对浮动按钮进行隐藏于显示的动画
        float transY = 0;
        float rotation = 0;
        if (Objects.equals(newTab.extra, R.string.title_home)) {
            //主界面隐藏
            transY = Ui.dipToPx(getResources(), 76);
        }
        else if (Objects.equals(newTab.extra, R.string.title_group)) {
            //群
            mAction.setImageResource(R.drawable.ic_group_add);
            rotation = -180;
        } else {
            mAction.setImageResource(R.drawable.ic_contact_add);
            rotation = 180;
        }

        //开始动画
        //旋转，Y轴位移，弹性差值器，时间
        mAction.animate()
                .rotationY(rotation)
                .translationY(transY)
                .setInterpolator(new AnticipateInterpolator(1))
                .setDuration(360)
                .start();
    }
}