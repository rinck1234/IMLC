package vip.rinck.imlc.fragments.user;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import vip.rinck.imlc.R;
import vip.rinck.imlc.activities.AccountActivity;
import vip.rinck.imlc.activities.MainActivity;
import vip.rinck.imlc.common.app.Application;
import vip.rinck.imlc.common.app.Fragment;
import vip.rinck.imlc.common.app.PresenterFragment;
import vip.rinck.imlc.common.widget.PortraitView;
import vip.rinck.imlc.factory.Factory;
import vip.rinck.imlc.factory.model.db.User;
import vip.rinck.imlc.factory.net.UploadHelper;
import vip.rinck.imlc.factory.persistence.Account;
import vip.rinck.imlc.factory.presenter.BaseContract;
import vip.rinck.imlc.factory.presenter.user.UpdateInfoContract;
import vip.rinck.imlc.factory.presenter.user.UpdateInfoPresenter;
import vip.rinck.imlc.fragments.media.GalleryFragment;

import static vip.rinck.imlc.factory.persistence.Account.getUser;


/**
 * 用户更新信息界面
 */
public class UpdateInfoFragment extends PresenterFragment<UpdateInfoContract.Presenter> implements UpdateInfoContract.View{
    @BindView(R.id.iv_sex)
    ImageView mSex;
    @BindView(R.id.et_desc)
    EditText mDesc;

    @BindView(R.id.iv_portrait)
    PortraitView mPortrait;

    @BindView(R.id.loading)
    Loading mLoading;

    @BindView(R.id.btn_submit)
    Button mSubmit;

    private String mPortraitPath;
    private boolean isMan;


    public UpdateInfoFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_update_info;
    }

    @Override
    protected void initData() {
        User user = Account.getUser();
        if(user==null|| TextUtils.isEmpty(user.getId())){
            AccountActivity.show(getContext());
            getActivity().finish();
        }

        super.initData();
    }

    @OnClick(R.id.iv_portrait)
    void onPortraitClick(){
        new GalleryFragment()
                .setListener(new GalleryFragment.OnSelectedListener() {
                    @Override
                    public void onSelectedImage(String path) {
                        UCrop.Options options = new UCrop.Options();
                        //设置图片处理的格式
                        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                        //设置压缩后的图片精度
                        options.setCompressionQuality(96);
                        //得到头像的缓存地址
                        File dPath = Application.getPortraitTmpFile();
                        //发起剪切
                        UCrop.of(Uri.fromFile(new File(path)),Uri.fromFile(dPath))
                                .withAspectRatio(1,1)
                                .withMaxResultSize(520,520)//返回最大尺寸
                                .withOptions(options)
                                .start(getActivity());

                    }
                })
                // show的时候建议使用getChildFragmentManager
                .show(getChildFragmentManager(),GalleryFragment.class.getName());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //收到从Activity传递过来的回调，取出其中的值进行图片加载
        //如果是我能够处理的类型
        if (requestCode == UCrop.REQUEST_CROP) {
            //通过UCrop得到对应的Uri
            final Uri resultUri = UCrop.getOutput(data);

            if (resultUri != null) {
                loadPortrait(resultUri);
            }
        }else if (resultCode == UCrop.RESULT_ERROR) {
            Application.showToast(R.string.data_rsp_error_unknown);
            final Throwable cropError = UCrop.getError(data);
        }
    }

    /**
     * 加载Uri到当前
     * @param uri
     */
    private void loadPortrait(Uri uri){
        //得到头像地址
        mPortraitPath = uri.getPath();

        Glide.with(this)
                .load(uri)
                .asBitmap()
                .centerCrop()
                .into(mPortrait);


    }

    @OnClick(R.id.iv_sex)
    void onSexClick(){
        //性别图片点击触发
        isMan = !isMan;
        Drawable drawable = getResources().getDrawable(isMan?
        R.drawable.ic_sex_man:R.drawable.ic_sex_woman);
        mSex.setImageDrawable(drawable);
        mSex.getBackground().setLevel(isMan?0:1);
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick(){
        String desc = mDesc.getText().toString();
        mPresenter.update(mPortraitPath,desc,isMan);

    }

    @Override
    public void showError(int str) {
        super.showError(str);
        mLoading.stop();
        mPortrait.setEnabled(true);
        mDesc.setEnabled(true);
        mSex.setEnabled(true);
        mSubmit.setEnabled(true);
    }

    @Override
    public void showLoading() {
        super.showLoading();
        mLoading.start();
        mPortrait.setEnabled(false);
        mDesc.setEnabled(false);
        mSex.setEnabled(false);
        mSubmit.setEnabled(false);
    }

    @Override
    protected UpdateInfoContract.Presenter initPresenter() {
        return new UpdateInfoPresenter(this);
    }

    @Override
    public void updateSucceed() {
        MainActivity.show(getContext());
        getActivity().finish();
    }


}
