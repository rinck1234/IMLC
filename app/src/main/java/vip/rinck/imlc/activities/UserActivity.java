package vip.rinck.imlc.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import net.qiujuer.genius.ui.compat.UiCompat;

import butterknife.BindView;
import vip.rinck.imlc.R;
import vip.rinck.imlc.common.app.Activity;
import vip.rinck.imlc.common.app.Fragment;
import vip.rinck.imlc.fragments.user.UpdateInfoFragment;

/**
 * 用户信息界面
 * 可以提供用户信息修改
 */
public class UserActivity extends Activity {
    private Fragment mCurFragment;
    @BindView(R.id.iv_bg)
    ImageView mBg;

    /**
     * 显示界面入口方法
     */
    public static void show(Context context){
        context.startActivity(new Intent(context,UserActivity.class));
    }
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mCurFragment = new UpdateInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container, mCurFragment)
                .commit();

        //初始化背景
        Glide.with(this)
                .load(R.drawable.bg_launch)
                .centerCrop()
                .into(new ViewTarget<ImageView,GlideDrawable>(mBg) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        //拿到glide的Drawable
                        Drawable drawable = resource.getCurrent();
                        //使用适配类进行包装
                        drawable = DrawableCompat.wrap(drawable);
                        drawable.setColorFilter(UiCompat.getColor(getResources(),R.color.colorAccent),
                                PorterDuff.Mode.SCREEN);//设置着色的效果和颜色，蒙版模式
                        //设置给ImageView
                        this.view.setImageDrawable(drawable);

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCurFragment.onActivityResult(requestCode, resultCode, data);

    }
}
